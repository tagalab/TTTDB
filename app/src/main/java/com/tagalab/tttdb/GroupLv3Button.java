package com.tagalab.tttdb;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import java.util.ArrayList;
import java.util.List;

import com.tagalab.tttdb.db.GroupLv3Info;

class GroupLv3Button {
    // グループLv1情報
    private GroupLv3Info Lv3Info;
    // 自ボタン
    private Button Lv3Button;
    // 子要素
    private List<WordRow> WordRows;
    // 自エリア
    private LinearLayout Lv3Area;
    // エリア開閉
    private int AreaOpen;
    // コンテキスト
    private Context mainActivityContext;

    GroupLv3Button(String[] _strDatas) {
        Lv3Info   = new GroupLv3Info(_strDatas);
        Lv3Button = null;
        WordRows  = new ArrayList<>();
        Lv3Area   = null;
        AreaOpen  = LinearLayout.GONE;
        mainActivityContext = null;
    }

    void createButton(Context _context, LinearLayout _linearLayout) {
        mainActivityContext = _context;

        // XMLのスタイルを適用したLv1ボタンを作成し画面に追加する
        Lv3Button = new Button(new ContextThemeWrapper(_context, R.style.MenuButtonStyle));
        _linearLayout.addView(Lv3Button);

        // XMLのスタイルを適用していても、Javaではlayout_marginやdrawableは反映されないので直接設定する
        Lv3Button.setBackgroundResource(R.drawable.button_next);
        ViewGroup.MarginLayoutParams objMLP = (ViewGroup.MarginLayoutParams) Lv3Button.getLayoutParams();
        objMLP.setMargins(0, 20,  0, 0);
        Lv3Button.setLayoutParams(objMLP);
        Lv3Button.setText(Lv3Info.getLv3_name());
        Lv3Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pushButton();
            }
        });

        // 配下のエリアを作成する
        Lv3Area = new LinearLayout(new ContextThemeWrapper(_context, R.style.Lv3AreaStyle));
        _linearLayout.addView(Lv3Area);
        createArea();
    }

    GroupLv3Info getInfo() {
        return Lv3Info;
    }

    Button getButton() {
        return Lv3Button;
    }

    List<WordRow> getChilds() {
        return WordRows;
    }

    LinearLayout getLv3Area() {
        return Lv3Area;
    }

    int getAreaOpen() {
        return AreaOpen;
    }

    void addChild(WordRow _child) {
        WordRows.add(_child);
    }

    void pushButton() {
        if (LinearLayout.GONE == AreaOpen) {
            // 自エリアを開く
            AreaOpen = LinearLayout.VISIBLE;

        } else {
            // 自エリアを閉じる
            AreaOpen = LinearLayout.GONE;
        }

        createArea();
    }

    void createArea() {
        if (LinearLayout.VISIBLE == AreaOpen) {
            Lv3Area.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));

            // タイトル行を作成し画面に追加する
            LinearLayout objRow = new LinearLayout(new ContextThemeWrapper(mainActivityContext, R.style.WordRowLayoutStyle));
            Lv3Area.addView(objRow);

            TextView objTitle1 = new TextView(new ContextThemeWrapper(mainActivityContext, R.style.WordRowTitleStyle));
            objRow.addView(objTitle1);
            objTitle1.setText("テストボタン");
            objTitle1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            objTitle1.setWidth((int)(100 * MainActivity.THIS_SCALE + 0.5f));

            TextView objTitle2 = new TextView(new ContextThemeWrapper(mainActivityContext, R.style.WordRowTitleStyle));
            objRow.addView(objTitle2);
            objTitle2.setText("単語詳細ボタン");
            objTitle2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // 単語行を作成し画面に追加する
            for(final WordRow objWorkWord : WordRows) {
                objWorkWord.createWordRow(mainActivityContext, Lv3Area);
            }

        } else {
            // 単語列を削除する
            Lv3Area.removeAllViews();
        }

        Lv3Area.setVisibility(AreaOpen);
    }
}
