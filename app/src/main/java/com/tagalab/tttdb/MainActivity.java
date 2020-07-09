package com.tagalab.tttdb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.tagalab.tttdb.db.DBOpenHelper;
import com.tagalab.tttdb.db.ExamHistoryInfo;
import com.tagalab.tttdb.db.WordExtendInfo;

public class MainActivity extends AppCompatActivity {
    // TODO 単語のカテゴリ分けを「易しい①」から「易しい１～２０」に変更
    final private Float SPEECH_SLOW   = 0.5f; // 再生速度(遅い)
    final private Float SPEECH_NORMAL = 1.0f; // 再生速度（標準）
    final private Float SPEECH_FAST   = 1.5f; // 再生速度（速い）
    final private Float PITCH_LOW     = 0.5f; // 再生ピッチ（低い）
    final private Float PITCH_NORMAL  = 1.0f; // 再生ピッチ（標準）
    final private Float PITCH_HIGH    = 1.5f; // 再生ピッチ（高い）

    private static final String LOG_TAG = "MainActivity";

    // 広告用オブジェクト
    private AdView AdvMainPromotion;

    // DB用オブジェクト
    DBOpenHelper   DBOpen;
    SQLiteDatabase DB;

    // 読み上げ用オブジェクト
    TextToSpeech Tts;
    Boolean      BlnTts;

    // ビュー設置用エリア
    LinearLayout LayMainArea;

    // 各ボタンの押下情報
    List<GroupLv1Button> LstLv1Buttons;
    List<GroupLv3Button> LstLv3Buttons;
    GroupLv1Button Lv1Button;
    GroupLv2Button Lv2Button;
    WordRow        WordRow;

    // 単語テスト情報
    String HintCD;      // ヒントあり・ヒントなしの選択値
    String InputValue;  // 単語テストの入力値
    String ResultValue; // 単語テスト判定結果
    int MenuScrollY;    // メニュースクロール位置
    int WordScrollY;    // 単語リストスクロール位置

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 縦画面固定にする
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*####################################
                     ここから実装
        ####################################*/
        LstLv1Buttons = null;
        LstLv3Buttons = null;
        BlnTts        = true;
        MenuScrollY   = 0;
        WordScrollY   = 0;

        // 広告の準備を行う
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // メインアクティビティを読込む
        setContentView(R.layout.activity_main);
        LayMainArea = findViewById(R.id.layMainArea);

        // 広告を読込む
        AdvMainPromotion = findViewById(R.id.advMainPromotion);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdvMainPromotion.loadAd(adRequest);

        // DBを開く（DBが未作成の時は自動的に生成する。DB更新後の更新も自動で行う。）
        DBOpen = new DBOpenHelper(this, getResources());
        DB = DBOpen.getWritableDatabase();

        // 単語読み上げの準備を行う
        Tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TTS初期化
                if (TextToSpeech.SUCCESS == status) {
                    Locale locale = Locale.ENGLISH;
                    if (Tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                        Tts.setLanguage(locale);
                        Log.d(LOG_TAG, "OK Init");
                    } else {
                        BlnTts = false;
                        Log.d(LOG_TAG, "Error SetLocale");
                    }
                } else {
                    BlnTts = false;
                    Log.d(LOG_TAG, "Error Init");
                }
            }
        });

        // 音声合成が有効なら再生速度を設定する
        if(BlnTts) Tts.setSpeechRate(SPEECH_SLOW);

        // Topメニューを生成する
        setTopView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // DBを閉じる
        if (null != DB) DB.close();
        if (null != DBOpen) DBOpen.close();

        // TextToSpeechのリソースを解放する
        if (null != Tts) Tts.shutdown();
    }

    // TOP画面を生成する
    private void setTopView() {
        // TopViewを設定する
        LayMainArea.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_top, LayMainArea);

        // Lv1ボタン追加用レイアウト取得
        LinearLayout linearLayout = findViewById(R.id.layTopList);

        // 単語リストを初期化する
        LstLv3Buttons = null;
        WordScrollY = 0;

        if(null == LstLv1Buttons) {
            // Lv1ボタンの情報をDBから取得しリストを作成する
            LstLv1Buttons = new ArrayList<>();

            Cursor objCursor = null;
            try {
                // 辞書「word_dictionary」に存在するLv1のみボタンを生成する
                String strSQLLv1 = "SELECT DISTINCT group_lv1.lv1_cd, lv1_name "
                        + "FROM group_lv1 "
                        + "INNER JOIN word_dictionary "
                        + "ON group_lv1.lv1_cd = word_dictionary.lv1_cd "
                        + "ORDER BY group_lv1.lv1_cd";
                objCursor = DB.rawQuery(strSQLLv1, null);

                // Lv1グループの検索結果が存在すればボタンを生成する
                while (objCursor.moveToNext()) {
                    // ボタンに対応するグループLv1情報を取得する
                    String[] strLv1 = new String[]{objCursor.getString((int) 0), objCursor.getString((int) 1)};
                    GroupLv1Button objWorkLv1 = new GroupLv1Button(strLv1);
                    LstLv1Buttons.add(objWorkLv1);
                }

                // 各Lv1に存在するLv2ボタンを作成しLv1に追加する
                for (GroupLv1Button objWorkLv1 : LstLv1Buttons) {
                    String strSQLLv2 = "SELECT DISTINCT group_lv2.lv2_order, lv2_name, lv2_name_short "
                            + "FROM group_lv2 "
                            + "ORDER BY group_lv2.lv2_order";
                    objCursor = DB.rawQuery(strSQLLv2, null);

                    // 検索結果からボタンを生成する
                    while(objCursor.moveToNext()) {
                        // グループLv2データを取得する
                        String[] strLv2 = new String[]{
                                objCursor.getString((int) 0),
                                objCursor.getString((int) 1),
                                objCursor.getString((int) 2)
                        };
                        GroupLv2Button objWorkLv2 = new GroupLv2Button(strLv2);
                        objWorkLv1.addChild(objWorkLv2);
                    }
                }
            } finally {
                if (objCursor != null) {
                    objCursor.close();
                }
            }
        }

        for (final GroupLv1Button objWorkLv1 : LstLv1Buttons) {
            // Lv1ボタンを作成し画面に追加する
            objWorkLv1.createButton(this, linearLayout);

            // ボタンにクリック時に実行する処理を設定する
            objWorkLv1.getButton().setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    objWorkLv1.pushButton();
                }
            });

            for(final GroupLv2Button objWorkLv2 : objWorkLv1.getChilds()) {
                // Lv2ボタンを作成し画面に追加する
                objWorkLv2.createGroupLv2Button(this, objWorkLv1.getLv1Area());

                // ボタンにクリック時に実行する処理を設定する
                objWorkLv2.getButton().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Lv1Button = objWorkLv2.getParent();
                        Lv2Button = objWorkLv2;
                        setWordView();
                    }
                });
            }
        }

        // メニュースクロール位置を復元する
        final ScrollView menuScroll = findViewById(R.id.scrTopList);
        menuScroll.post(new Runnable() {
            @Override
            public void run() {
                menuScroll.scrollTo(0, MenuScrollY);
            }
        });
    }

    // 単語リスト画面を生成する
    private void setWordView() {
        // メニュースクロール位置を保持する
        ScrollView menuScroll = findViewById(R.id.scrTopList);
        if(null != menuScroll) MenuScrollY = menuScroll.getScrollY();

        // WordViewを設定する
        LayMainArea.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_word, LayMainArea);

        // 戻るボタンを設定する
        Button btnBack = findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTopView();
            }
        });

        // 単語ボタン追加用レイアウト取得
        LinearLayout linearLayout = findViewById(R.id.wordList);

        if(null == LstLv3Buttons) {
            // Lv3ボタンの情報をDBから取得しリストを作成する
            LstLv3Buttons = new ArrayList<>();

            Cursor objCursor = null;
            try{
                // 辞書「word_dictionary」に存在するLv3のみボタンを生成する
                String strSQLLv3 = "SELECT DISTINCT group_lv3.lv3_cd, lv3_order, lv3_name "
                        + "FROM group_lv3 "
                        + "INNER JOIN word_dictionary "
                        +   "ON group_lv3.lv3_cd = word_dictionary.lv3_cd "
                        + "WHERE word_dictionary.lv1_cd = ? ";

                // 検索ワードが「全て」の場合
                if (Lv2Button.getInfo().getLv2_name_short().equals("全")) {
                    strSQLLv3 = strSQLLv3 + "ORDER BY group_lv3.lv3_order";
                    objCursor = DB.rawQuery(strSQLLv3, new String[]{Lv1Button.getInfo().getLv1_cd()});

                // 検索ワードが「全て」ではない場合
                } else {
                    strSQLLv3 = strSQLLv3
                            + "  AND word_dictionary.all_type LIKE ? "
                            + "ORDER BY group_lv3.lv3_order";
                    objCursor = DB.rawQuery(strSQLLv3,
                            new String[]{Lv1Button.getInfo().getLv1_cd(), "%" + Lv2Button.getInfo().getLv2_name() + "%"});

                }

                // Lv3グループの検索結果が存在すればボタンを生成する
                while (objCursor.moveToNext()) {
                    // ボタンに対応するグループLv3情報を取得する
                    String[] strLv3 = new String[]{objCursor.getString((int) 0), objCursor.getString((int) 1), objCursor.getString((int) 2)};
                    GroupLv3Button objWorkLv3 = new GroupLv3Button(strLv3);
                    LstLv3Buttons.add(objWorkLv3);
                }

                // 各Lv3に存在する単語行を作成しLv3に追加する
                for (GroupLv3Button objWorkLv3 : LstLv3Buttons) {
                    // 単語に関連するデータを取得する
                    String strSQLWord = "SELECT DISTINCT "
                            + "group_lv3.lv3_cd "
                            + ",group_lv3.lv3_order "
                            + ",group_lv3.lv3_name "
                            + ",word_dictionary.word_id "
                            + ",word_dictionary.lv1_cd "
                            + ",word_dictionary.lv3_cd "
                            + ",word_dictionary.word_order "
                            + ",word_dictionary.all_type "
                            + ",word_dictionary.word "
                            + ",word_dictionary.mean "
                            + ",word_dictionary.answer "
                            + ",word_dictionary.hint "
                            + ",word_dictionary.phonetic "
                            + ",word_dictionary.example "
                            + ",word_expansion.word_id "
                            + ",word_expansion.result_0 "
                            + ",word_expansion.result_1 "
                            + ",word_expansion.memo_01 "
                            + ",word_expansion.memo_02 "
                            + ",word_expansion.memo_03 "
                            + ",word_expansion.persent "
                            + "FROM word_dictionary "
                            + "INNER JOIN group_lv1 "
                            + "ON word_dictionary.lv1_cd = group_lv1.lv1_cd "
                            + "INNER JOIN group_lv3 "
                            + "ON word_dictionary.lv3_cd = group_lv3.lv3_cd "
                            + "LEFT OUTER JOIN word_expansion "
                            + "ON word_dictionary.word_id = word_expansion.word_id "
                            + "WHERE word_dictionary.lv1_cd = ? "
                            + "  AND word_dictionary.lv3_cd = ? ";

                    // 検索ワードが「全て」の場合
                    if (Lv2Button.getInfo().getLv2_name_short().equals("全")) {
                        strSQLWord = strSQLWord + "ORDER BY word_dictionary.word_order";
                        objCursor = DB.rawQuery(strSQLWord,
                                new String[]{Lv1Button.getInfo().getLv1_cd(), objWorkLv3.getInfo().getLv3_cd()} // SQL文のパラメータ「?」に置き換わる値の配列
                        );

                    } else {
                        strSQLWord = strSQLWord
                                + "  AND word_dictionary.all_type LIKE ? "
                                + "ORDER BY word_dictionary.word_order";
                        objCursor = DB.rawQuery(strSQLWord,
                                new String[]{Lv1Button.getInfo().getLv1_cd(), objWorkLv3.getInfo().getLv3_cd(), "%" + Lv2Button.getInfo().getLv2_name() + "%"} // SQL文のパラメータ「?」に置き換わる値の配列
                        );

                    }

                    // 検索結果からボタンを生成する
                    while (objCursor.moveToNext()) {
                        // グループLv2データを取得する
                        String[] strLv3 = new String[]{
                                objCursor.getString(0),
                                objCursor.getString(1),
                                objCursor.getString(2)
                        };
                        String[] strWord = new String[]{
                                objCursor.getString(3),
                                objCursor.getString(4),
                                objCursor.getString(5),
                                objCursor.getString(6),
                                objCursor.getString(7),
                                objCursor.getString(8),
                                objCursor.getString(9),
                                objCursor.getString(10),
                                objCursor.getString(11),
                                objCursor.getString(12),
                                objCursor.getString(13),
                        };
                        String[] strWordExt = new String[]{
                                objCursor.getString(14),
                                objCursor.getString(15),
                                objCursor.getString(16),
                                objCursor.getString(17),
                                objCursor.getString(18),
                                objCursor.getString(19),
                                objCursor.getString(20)
                        };

                        final WordRow objWorkWord = new WordRow(strLv3, strWord, strWordExt);
                        objWorkLv3.addChild(objWorkWord);

                        // ボタンにクリック時に実行する処理を設定する
                        // 「単語」テキストクリック時に実行する処理を設定する
                        objWorkWord.setWord_text_Listener(new View.OnClickListener() {
                            public void onClick(View view) {
                                WordRow = objWorkWord;
                                showWordInfoDialog();
                            }
                        });

                        // 「ヒントあり」ボタンクリック時に実行する処理を設定する
                        objWorkWord.setHint_button_Listener(new View.OnClickListener() {
                            public void onClick(View view) {
                                HintCD = "1";
                                WordRow = objWorkWord;
                                setExamView();
                            }
                        });
                        // 「ヒントなし」ボタンクリック時に実行する処理を設定する
                        objWorkWord.setNomal_button_Listener(new View.OnClickListener() {
                            public void onClick(View view) {
                                HintCD = "0";
                                WordRow = objWorkWord;
                                setExamView();
                            }
                        });
                    }
                }
            } finally {
                if(objCursor != null) {
                    objCursor.close();
                }
            }
        }

        for (final GroupLv3Button objWorkLv3 : LstLv3Buttons) {
            // Lv3ボタンを作成し画面に追加する
            objWorkLv3.createButton(this, linearLayout);
        }

        // 単語リストスクロール位置を復元する
        final ScrollView wordScroll = findViewById(R.id.wordScroll);
        wordScroll.post(new Runnable() {
            @Override
            public void run() {
                wordScroll.scrollTo(0, WordScrollY);
            }
        });
    }


    // 単語テスト画面を生成する
    private void setExamView() {
        // 単語リストスクロール位置を保持する
        ScrollView wordScroll = findViewById(R.id.wordScroll);
        if(null != wordScroll) WordScrollY = wordScroll.getScrollY();

        // ExamViewを設定する
        LayMainArea.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_exam, LayMainArea);

        // 戻るボタンを設定する
        Button btnBack = findViewById(R.id.btnBack3);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWordView();
            }
        });

        // 画面部品を取得
        TextView txtMean = findViewById(R.id.txtExamMean);
        txtMean.setText(WordRow.getWordDictionary().getMean());
        TextView txtHintTitle = findViewById(R.id.txtExamHintTitle);
        TextView txtHint = findViewById(R.id.txtExamHint);
        if(HintCD.equals("0")) {
            txtHintTitle.setText("");
            txtHint.setText("");
        } else {
            txtHintTitle.setText("ヒント：");
            txtHint.setText(WordRow.getWordDictionary().getHint());
        }
        final TextView txtInput = findViewById(R.id.txtExamInput);

        // 読み上げボタンを設定する
        Button btnExamSpeech = findViewById(R.id.btnExamSpeech);
        if(BlnTts) {
            btnExamSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String string = WordRow.getWordDictionary().getAnswer();
                    if (0 < string.length()) {
                        if (Tts.isSpeaking()) {
                            // 読み上げ中なら止める
                            Tts.stop();
                        }

                        // 読み上げ開始
                        Tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
        } else {
            btnExamSpeech.setEnabled(false);
        }

        // Goボタン押下時の処理
        final Button btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WordRow.getWordDictionary().getAnswer().compareTo(txtInput.getText().toString()) == (int) 0) {
                    ResultValue = "〇";
                } else {
                    ResultValue = "×";
                }

                InputValue = txtInput.getText().toString();
                setResultView();
            }
        });

        // BackSpaceボタン押下時の処理
        final Button btnBS = findViewById(R.id.btnBS);
        btnBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int intEnd = txtInput.getText().length() - 1;
                if (intEnd >= 0) {
                    txtInput.setText(txtInput.getText().subSequence(0, intEnd));
                }
            }
        });

        // 文字ボタン押下時の処理
        List<Button> lstKeys = new ArrayList<Button>();
        lstKeys.add((Button) findViewById(R.id.btnA));
        lstKeys.add((Button) findViewById(R.id.btnB));
        lstKeys.add((Button) findViewById(R.id.btnC));
        lstKeys.add((Button) findViewById(R.id.btnD));
        lstKeys.add((Button) findViewById(R.id.btnE));
        lstKeys.add((Button) findViewById(R.id.btnF));
        lstKeys.add((Button) findViewById(R.id.btnG));
        lstKeys.add((Button) findViewById(R.id.btnH));
        lstKeys.add((Button) findViewById(R.id.btnI));
        lstKeys.add((Button) findViewById(R.id.btnJ));
        lstKeys.add((Button) findViewById(R.id.btnK));
        lstKeys.add((Button) findViewById(R.id.btnL));
        lstKeys.add((Button) findViewById(R.id.btnM));
        lstKeys.add((Button) findViewById(R.id.btnN));
        lstKeys.add((Button) findViewById(R.id.btnO));
        lstKeys.add((Button) findViewById(R.id.btnP));
        lstKeys.add((Button) findViewById(R.id.btnQ));
        lstKeys.add((Button) findViewById(R.id.btnR));
        lstKeys.add((Button) findViewById(R.id.btnS));
        lstKeys.add((Button) findViewById(R.id.btnT));
        lstKeys.add((Button) findViewById(R.id.btnU));
        lstKeys.add((Button) findViewById(R.id.btnV));
        lstKeys.add((Button) findViewById(R.id.btnW));
        lstKeys.add((Button) findViewById(R.id.btnX));
        lstKeys.add((Button) findViewById(R.id.btnY));
        lstKeys.add((Button) findViewById(R.id.btnZ));
        for (final Button btnKey : lstKeys) {
            // 無名クラスの一つ外のfinal変数は、無名クラスから参照できる
            btnKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtInput.setText(txtInput.getText().toString() + btnKey.getText().toString());
                }
            });
        }
    }

    // 答え合わせ画面を生成する
    private void setResultView() {
        // ResultViewを設定する
        LayMainArea.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_result, LayMainArea);

        // 答え合わせ結果を、テスト結果履歴テーブルに保存する
        ContentValues objValues = new ContentValues();
        objValues.put(ExamHistoryInfo.CON_COL_02, WordRow.getWordDictionary().getWord_id()); // 単語コード
        objValues.put(ExamHistoryInfo.CON_COL_03, ResultValue);                              // 答え合わせ結果
        objValues.put(ExamHistoryInfo.CON_COL_04, HintCD);                                   // ヒントコード
        objValues.put(ExamHistoryInfo.CON_COL_05, InputValue);                               // 入力値
        long lngID = DB.insert(ExamHistoryInfo.CON_TBL_NAME, null, objValues);

        // テスト結果履歴テーブルの内容から単語情報拡張テーブルの値を生成する
        Cursor objCursor = null;
        String strHintHistory = "";
        String strNomalHistory = "";
        try{
            // テスト結果履歴（ヒント）の結果を生成する
            objCursor = DB.query(
                    false,
                    ExamHistoryInfo.CON_TBL_NAME,
                    new String[]{ExamHistoryInfo.CON_COL_03}, // 取得する項目名の配列
                    ExamHistoryInfo.CON_COL_02 + " = ? AND " + ExamHistoryInfo.CON_COL_04 + " = 1",
                    new String[]{WordRow.getWordDictionary().getWord_id()}, // selectionのパラメータ「?」に置き換わる値の配列
                    null,
                    null,
                    ExamHistoryInfo.CON_COL_06 + " DESC",
                    "10"
            );
            while(objCursor.moveToNext()){
                strHintHistory = objCursor.getString((int)0) + strHintHistory;
            }

            // テスト結果履歴（ノーマル）の結果を生成する
            objCursor = DB.query(
                    false,
                    ExamHistoryInfo.CON_TBL_NAME,
                    new String[]{ExamHistoryInfo.CON_COL_03}, // 取得する項目名の配列
                    ExamHistoryInfo.CON_COL_02 + " = ? AND " + ExamHistoryInfo.CON_COL_04 + " = 0",
                    new String[]{WordRow.getWordDictionary().getWord_id()}, // selectionのパラメータ「?」に置き換わる値の配列
                    null,
                    null,
                    ExamHistoryInfo.CON_COL_06 + " DESC",
                    "10"
            );
            while(objCursor.moveToNext()){
                strNomalHistory = objCursor.getString((int)0) + strNomalHistory;
            }

        } finally {
            if(objCursor != null) {
                objCursor.close();
            }
        }

        // 単語情報オブジェクトに入力内容を設定
        WordRow.getWordExtend().setWord_id(WordRow.getWordDictionary().getWord_id());
        WordRow.getWordExtend().setResult_0(strNomalHistory);
        WordRow.getWordExtend().setResult_1(strHintHistory);
        // TODO percentを設定する

        // 単語情報拡張テーブルの当該単語の行を削除する（行がなくても行う）
        int intID = DB.delete(WordExtendInfo.CON_TBL_NAME, WordExtendInfo.CON_COL_01 + " = ?", new String[]{WordRow.getWordDictionary().getWord_id()});

        // 答え合わせの直近10回分の結果を、単語情報拡張テーブルに保存する
        objValues = new ContentValues();
        objValues.put(WordExtendInfo.CON_COL_01, WordRow.getWordDictionary().getWord_id()); // 単語コード
        objValues.put(WordExtendInfo.CON_COL_02, WordRow.getWordExtend().getResult_0());    // テスト結果履歴（ノーマル）
        objValues.put(WordExtendInfo.CON_COL_03, WordRow.getWordExtend().getResult_1());    // テスト結果履歴（ヒント）
        objValues.put(WordExtendInfo.CON_COL_04, WordRow.getWordExtend().getMemo_01());     // メモ１
        objValues.put(WordExtendInfo.CON_COL_05, WordRow.getWordExtend().getMemo_02());     // メモ２(予約）
        objValues.put(WordExtendInfo.CON_COL_06, WordRow.getWordExtend().getMemo_03());     // メモ３(予約）
        objValues.put(WordExtendInfo.CON_COL_07, WordRow.getWordExtend().getPersent());     // 正解率
        lngID = DB.insert(WordExtendInfo.CON_TBL_NAME, null, objValues);

        // 画面部品を取得
        TextView txtResult       = findViewById(R.id.txtResult);
        TextView txtResultInput  = findViewById(R.id.txtResultInput);
        TextView txtResultAnswer = findViewById(R.id.txtResultAnswer);
        TextView txtResultType   = findViewById(R.id.txtResultType);
        TextView txtResultMean   = findViewById(R.id.txtResultMean);
        TextView txtResultMemo01 = findViewById(R.id.txtResultMemo01);
        Button   btnResultSpeech = findViewById(R.id.btnResultSpeech);

        // 部品に値を設定
        txtResult.setText(ResultValue);

        txtResultInput.setText(InputValue);
        txtResultAnswer.setText(WordRow.getWordDictionary().getWord());
        txtResultType.setText(WordRow.getWordDictionary().getAll_type());
        txtResultMean.setText(WordRow.getWordDictionary().getMean());
        txtResultMean.setMovementMethod(ScrollingMovementMethod.getInstance());
        txtResultMemo01.setText(WordRow.getWordExtend().getMemo_01());
        txtResultMemo01.setMovementMethod(ScrollingMovementMethod.getInstance());

        // 読み上げボタンを設定する
        if(BlnTts) {
            btnResultSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String string = WordRow.getWordDictionary().getAnswer();
                    if (0 < string.length()) {
                        if (Tts.isSpeaking()) {
                            // 読み上げ中なら止める
                            Tts.stop();
                        }

                        // 読み上げ開始
                        Tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });

        } else {
            btnResultSpeech.setEnabled(false);
        }

        // OKボタン押下時の処理
        final Button btnOK = findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWordView();
            }
        });
    }

    // 単語情報ダイアログを表示する
    private void showWordInfoDialog() {
        // 単語情報ダイアログのレイアウトを読込む
        final View objView = getLayoutInflater().inflate(R.layout.dialog_word_info, null);

        // 画面部品を取得
        TextView       txtInfoWord     = objView.findViewById(R.id.txtInfoWord);
        TextView       txtInfoType     = objView.findViewById(R.id.txtInfoType);
        TextView       txtInfoMean     = objView.findViewById(R.id.txtInfoMean);
        final EditText edtInfoMemo01   = objView.findViewById(R.id.edtInfoMemo01);
        TableLayout    tblHintHistory  = objView.findViewById(R.id.tblHintHistory);
        TableLayout    tblNomalHistory = objView.findViewById(R.id.tblNomalHistory);

        // 読み上げボタンを設定する
        Button   btnInfoSpeech = objView.findViewById(R.id.btnInfoSpeech);
        if(BlnTts) {
            btnInfoSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String string = WordRow.getWordDictionary().getAnswer();
                    if (0 < string.length()) {
                        if (Tts.isSpeaking()) {
                            // 読み上げ中なら止める
                            Tts.stop();
                        }

                        // 読み上げ開始
                        Tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });

        } else {
            btnInfoSpeech.setEnabled(false);
        }

        // 単語情報ダイアログをモーダルで生成する
        AlertDialog.Builder objDialog = new AlertDialog.Builder(this).setCancelable(false);
        objDialog.setView(objView);
        objDialog.setPositiveButton("Ｏ　Ｋ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 単語情報オブジェクトに入力内容を設定
                WordRow.getWordExtend().setMemo_01(edtInfoMemo01.getText().toString());

                // テ単語情報拡張テーブルの当該単語の行を削除する（行がなくても行う）
                int intID = DB.delete(WordExtendInfo.CON_TBL_NAME, WordExtendInfo.CON_COL_01+ " = ?", new String[]{WordRow.getWordDictionary().getWord_id()});

                // 編集内容を単語情報拡張テーブルに保存する
                ContentValues objValues = new ContentValues();
                objValues.put(WordExtendInfo.CON_COL_01, WordRow.getWordDictionary().getWord_id());  // 単語コード
                objValues.put(WordExtendInfo.CON_COL_02, WordRow.getWordExtend().getResult_0()); // テスト結果履歴（ノーマル）
                objValues.put(WordExtendInfo.CON_COL_03, WordRow.getWordExtend().getResult_1()); // テスト結果履歴（ヒント）
                objValues.put(WordExtendInfo.CON_COL_04, WordRow.getWordExtend().getMemo_01());  // メモ１行目
                objValues.put(WordExtendInfo.CON_COL_05, WordRow.getWordExtend().getMemo_02());  // メモ２行目
                objValues.put(WordExtendInfo.CON_COL_06, WordRow.getWordExtend().getMemo_03());  // メモ３行目
                objValues.put(WordExtendInfo.CON_COL_07, WordRow.getWordExtend().getPersent());  // 正解率
                long lngID = DB.insert(WordExtendInfo.CON_TBL_NAME, null, objValues);
            }
        });
//        objDialog.setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {} // ダイアログを閉じるだけなので処理なし
//        });

        // 単語情報を設定する
        txtInfoWord.setText(WordRow.getWordDictionary().getWord());
        txtInfoType.setText(WordRow.getWordDictionary().getAll_type());
        txtInfoMean.setText(WordRow.getWordDictionary().getMean());
        txtInfoMean.setMovementMethod(ScrollingMovementMethod.getInstance());
        edtInfoMemo01.setText(WordRow.getWordExtend().getMemo_01());

        // テスト結果履歴を取得し設定する
        Cursor   objCursor = null;
        TableRow objRow    = null;
        TextView objText   = null;
        String   strIndex  = "";
        try{
            // テスト結果履歴（ヒント）の結果を生成する
            objCursor = DB.query(
                    false,
                    ExamHistoryInfo.CON_TBL_NAME,
                    new String[]{
                            "date(" + ExamHistoryInfo.CON_COL_06 + ")",
                            ExamHistoryInfo.CON_COL_03,
                            ExamHistoryInfo.CON_COL_05
                    }, // 取得する項目名の配列
                    ExamHistoryInfo.CON_COL_02 + " = ? AND " + ExamHistoryInfo.CON_COL_04 + " = 1",
                    new String[]{WordRow.getWordDictionary().getWord_id()}, // selectionのパラメータ「?」に置き換わる値の配列
                    null,
                    null,
                    ExamHistoryInfo.CON_COL_06,
                    null
            );
            while(objCursor.moveToNext()){
                objRow = new TableRow(this);
                tblHintHistory.addView(objRow);

                // 行番号
                strIndex = "" + ((objCursor.getPosition() + 1));
                objText = new TextView(this);
                objText.setText(strIndex);
                objRow.addView(objText);

                // テスト時間
                objText = new TextView(this);
                objText.setText(objCursor.getString((int)0));
                objRow.addView(objText);

                // テスト結果
                objText = new TextView(this);
                objText.setText(objCursor.getString((int)1));
                objRow.addView(objText);

                // 入力した内容
                objText = new TextView(this);
                objText.setText(objCursor.getString((int)2));
                objRow.addView(objText);
            }

            // テスト結果履歴（ノーマル）の結果を生成する
            objCursor = DB.query(
                    false,
                    ExamHistoryInfo.CON_TBL_NAME,
                    new String[]{
                            "date(" + ExamHistoryInfo.CON_COL_06 + ")",
                            ExamHistoryInfo.CON_COL_03,
                            ExamHistoryInfo.CON_COL_05
                    }, // 取得する項目名の配列
                    ExamHistoryInfo.CON_COL_02 + " = ? AND " + ExamHistoryInfo.CON_COL_04 + " = 0",
                    new String[]{WordRow.getWordDictionary().getWord_id()}, // selectionのパラメータ「?」に置き換わる値の配列
                    null,
                    null,
                    ExamHistoryInfo.CON_COL_06,
                    null
            );
            while(objCursor.moveToNext()){
                objRow = new TableRow(this);
                tblNomalHistory.addView(objRow);

                // 行番号
                strIndex = "" + ((objCursor.getPosition() + 1));
                objText = new TextView(this);
                objText.setText(strIndex);
                objRow.addView(objText);

                // テスト時間
                objText = new TextView(this);
                objText.setText(objCursor.getString((int)0));
                objRow.addView(objText);

                // テスト結果
                objText = new TextView(this);
                objText.setText(objCursor.getString((int)1));
                objRow.addView(objText);

                // 入力した内容
                objText = new TextView(this);
                objText.setText(objCursor.getString((int)2));
                objRow.addView(objText);
            }

        } finally {
            if(objCursor != null) {
                objCursor.close();
            }
        }

        // ダイアログを表示する
        objDialog.create().show();
    }
}
