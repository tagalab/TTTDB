package com.tagalab.tttdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

import com.tagalab.tttdb.TTTDB_Util;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "DBOpenHelper";
    private static final String DB_NAME = "TTTDB.sqlite";
    private static final int    DB_VER  = 3;

    private static final String ENTER_BEFORE = "<br>";
    private static final String ENTER_AFTER  = "\n";

    private Resources OBJ_RESOURCES;

    // コンストラクタ
    public DBOpenHelper(@Nullable Context context, Resources _objResources) {
        super(context, DB_NAME,null, DB_VER);
        OBJ_RESOURCES = _objResources;

        Log.d(LOG_TAG, "END DBOpenHelper");
    }

    /*
     * DBがオープン時、DBが存在しない場合に自動で呼び出されるメソッド。
     * DB生成、テーブル生成、マスタテーブルのデータ登録を行う。
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "START onCreate");

        // assets配下のファイルを開く
        List<String[]> lstCsvLv1  = TTTDB_Util.readCsvFile(OBJ_RESOURCES, "group_lv1.csv");
        List<String[]> lstCsvLv2  = TTTDB_Util.readCsvFile(OBJ_RESOURCES, "group_lv2.csv");
        List<String[]> lstCsvLv3  = TTTDB_Util.readCsvFile(OBJ_RESOURCES, "group_lv3.csv");
        List<String[]> lstCsvWord = TTTDB_Util.readCsvFile(OBJ_RESOURCES, "word_dictionary.csv");

        // Lv1マスタテーブル生成
        db.execSQL(
                "CREATE TABLE " + GroupLv1Info.CON_TBL_NAME + "("
                + GroupLv1Info.CON_COL_01 + " TEXT PRIMARY KEY,"
                + GroupLv1Info.CON_COL_02 + " TEXT)"
        );
        for (String[] strDatas : lstCsvLv1) {
            // カラム名と値のセットを生成しテーブルに挿入する。
            ContentValues objValues = new ContentValues();
            objValues.put(GroupLv1Info.CON_COL_01, strDatas[0]); // Lv1コード(Key)
            objValues.put(GroupLv1Info.CON_COL_02, strDatas[1]); // Lv1名称

            Long lngID = db.insert(GroupLv1Info.CON_TBL_NAME, null, objValues);
        }

        // Lv2マスタテーブル生成
        db.execSQL(
                "CREATE TABLE " + GroupLv2Info.CON_TBL_NAME + "("
                + GroupLv2Info.CON_COL_01 + " INTEGER, "
                + GroupLv2Info.CON_COL_02 + " TEXT, "
                + GroupLv2Info.CON_COL_03 + " TEXT)"
        );
        for (String[] strDatas : lstCsvLv2) {
            // カラム名と値のセットを生成しテーブルに挿入する。
            ContentValues objValues = new ContentValues();
            objValues.put(GroupLv2Info.CON_COL_01, strDatas[0]); // Lv2順序
            objValues.put(GroupLv2Info.CON_COL_02, strDatas[1]); // Lv2名称
            objValues.put(GroupLv2Info.CON_COL_03, strDatas[2]); // Lv2略称

            Long lngID = db.insert(GroupLv2Info.CON_TBL_NAME, null, objValues);
        }

        // Lv3マスタテーブル生成
        db.execSQL(
                "CREATE TABLE " + GroupLv3Info.CON_TBL_NAME + "("
                + GroupLv3Info.CON_COL_01 + " TEXT PRIMARY KEY,"
                + GroupLv3Info.CON_COL_02 + " INTEGER,"
                + GroupLv3Info.CON_COL_03 + " TEXT)"
        );
        for (String[] strDatas : lstCsvLv3) {
            // カラム名と値のセットを生成しテーブルに挿入する。
            ContentValues objValues = new ContentValues();
            objValues.put(GroupLv3Info.CON_COL_01, strDatas[0]); // Lv3コード(Key)
            objValues.put(GroupLv3Info.CON_COL_02, strDatas[1]); // Lv3順序
            objValues.put(GroupLv3Info.CON_COL_03, strDatas[2]); // Lv3名称

            Long lngID = db.insert(GroupLv3Info.CON_TBL_NAME, null, objValues);
        }

        // 単語マスタテーブル生成
        db.execSQL(
                "CREATE TABLE " + WordDictionaryInfo.CON_TBL_NAME + "("
                + WordDictionaryInfo.CON_COL_01 + " TEXT PRIMARY KEY,"
                + WordDictionaryInfo.CON_COL_02 + " TEXT,"
                + WordDictionaryInfo.CON_COL_03 + " TEXT,"
                + WordDictionaryInfo.CON_COL_04 + " INTEGER,"
                + WordDictionaryInfo.CON_COL_05 + " TEXT,"
                + WordDictionaryInfo.CON_COL_06 + " TEXT,"
                + WordDictionaryInfo.CON_COL_07 + " TEXT,"
                + WordDictionaryInfo.CON_COL_08 + " TEXT,"
                + WordDictionaryInfo.CON_COL_09 + " TEXT,"
                + WordDictionaryInfo.CON_COL_10 + " TEXT)"
        );
        for (String[] strDatas : lstCsvWord) {
            // カラム名と値のセットを生成しテーブルに挿入する。
            ContentValues objValues = new ContentValues();
            objValues.put(WordDictionaryInfo.CON_COL_01, strDatas[0]); // Wordコード(Key)
            objValues.put(WordDictionaryInfo.CON_COL_02, strDatas[1]); // Lv1コード
            objValues.put(WordDictionaryInfo.CON_COL_03, strDatas[2]); // Lv3コード
            objValues.put(WordDictionaryInfo.CON_COL_04, strDatas[3]); // Lv3順序
            objValues.put(WordDictionaryInfo.CON_COL_05, strDatas[4]); // 全品詞
            objValues.put(WordDictionaryInfo.CON_COL_06, strDatas[5]); // 単語
            objValues.put(WordDictionaryInfo.CON_COL_07, strDatas[6].replaceAll(ENTER_BEFORE, ENTER_AFTER));  // 意味 改行変換（<br>⇒\n）
            objValues.put(WordDictionaryInfo.CON_COL_08, strDatas[7]); // ヒント
            objValues.put(WordDictionaryInfo.CON_COL_09, strDatas[8]); // 発音
            objValues.put(WordDictionaryInfo.CON_COL_10, strDatas[9].replaceAll(ENTER_BEFORE, ENTER_AFTER)); // 用例 改行変換（<br>⇒\n）

            Long lngID = db.insert(WordDictionaryInfo.CON_TBL_NAME, null, objValues);
        }

        // 単語データテーブル生成
        db.execSQL(
                "CREATE TABLE " + WordExtendInfo.CON_TBL_NAME + "("
                        + WordExtendInfo.CON_COL_01 + " TEXT　PRIMARY KEY,"
                        + WordExtendInfo.CON_COL_02 + " TEXT,"
                        + WordExtendInfo.CON_COL_03 + " TEXT,"
                        + WordExtendInfo.CON_COL_04 + " TEXT,"
                        + WordExtendInfo.CON_COL_05 + " TEXT,"
                        + WordExtendInfo.CON_COL_06 + " TEXT,"
                        + WordExtendInfo.CON_COL_07 + " TEXT)"
        );

        // テスト結果履歴詳細テーブル生成
        db.execSQL(
                "CREATE TABLE " + ExamHistoryInfo.CON_TBL_NAME +"("
                + ExamHistoryInfo.CON_COL_01 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ExamHistoryInfo.CON_COL_02 + " TEXT,"
                + ExamHistoryInfo.CON_COL_03 + " TEXT,"
                + ExamHistoryInfo.CON_COL_04 + " TEXT,"
                + ExamHistoryInfo.CON_COL_05 + " TEXT,"
                + ExamHistoryInfo.CON_COL_06 + " DEFAULT CURRENT_TIMESTAMP)"
        );

        // 設定テーブル生成
        db.execSQL(
                "CREATE TABLE " + SettingInfo.CON_TBL_NAME +"("
                        + SettingInfo.CON_COL_01 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + SettingInfo.CON_COL_02 + " TEXT,"
                        + SettingInfo.CON_COL_03 + " TEXT,"
                        + SettingInfo.CON_COL_04 + " TEXT,"
                        + SettingInfo.CON_COL_05 + " TEXT,"
                        + SettingInfo.CON_COL_06 + " TEXT,"
                        + SettingInfo.CON_COL_07 + " TEXT,"
                        + SettingInfo.CON_COL_08 + " TEXT,"
                        + SettingInfo.CON_COL_09 + " TEXT,"
                        + SettingInfo.CON_COL_10 + " TEXT)"
        );

        // カラム名と値のセットを生成しテーブルに挿入する。
        ContentValues objValues = new ContentValues();
        objValues.put(SettingInfo.CON_COL_01, "0");  // 設定１にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_02, "0");  // 設定２にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_03, "0");  // 設定３にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_04, "0");  // 設定４にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_05, "0");  // 設定５にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_06, "0");  // 設定６にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_07, "0");  // 設定７にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_08, "0");  // 設定８にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_09, "0");  // 設定９にデフォルト値として0（off）を設定する
        objValues.put(SettingInfo.CON_COL_10, "0");  // 設定１０にデフォルト値として0（off）を設定する
        Long lngID = db.insert(SettingInfo.CON_TBL_NAME, null, objValues);

        Log.d(LOG_TAG, "END onCreate");
    }

    /*
     * DBが端末に存在し、そのDBのバージョンがDB_VERより古い場合に自動で呼び出されるメソッド。
     * テーブルの変更（ALTER）や、マスタテーブルのデータ変更などを行う。
     *
     * 例１）端末のDBのバージョンが３でDB_VER ＝ ４なら呼び出され、４用の更新を行う。
     * 例２）端末のDBのバージョンが１でDB_VER ＝ ４なら呼び出され、２用，３用，４用の更新を行う。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "START onUpgrade");

        // 旧バージョン以降のDB更新を全て行う必要があるのでbreak句は記載しない。
        switch (oldVersion) {
            // バージョンＵＰ（１->２）の処理
            case (int)1:
                Log.d(LOG_TAG, "バージョンＵＰ（１->２）");

                // 単語データテーブル再生成
                db.execSQL("DROP TABLE " + WordExtendInfo.CON_TBL_NAME);
                db.execSQL("CREATE TABLE " + WordExtendInfo.CON_TBL_NAME + "("
                          + WordExtendInfo.CON_COL_01 + " TEXT　PRIMARY KEY,"
                          + WordExtendInfo.CON_COL_02 + " TEXT,"
                          + WordExtendInfo.CON_COL_03 + " TEXT,"
                          + WordExtendInfo.CON_COL_04 + " TEXT,"
                          + WordExtendInfo.CON_COL_05 + " TEXT,"
                          + WordExtendInfo.CON_COL_06 + " TEXT,"
                          + WordExtendInfo.CON_COL_07 + " TEXT)"
                );

                // テスト結果履歴詳細テーブル再生成
                db.execSQL("DROP TABLE " + ExamHistoryInfo.CON_TBL_NAME);
                db.execSQL("CREATE TABLE " + ExamHistoryInfo.CON_TBL_NAME +"("
                          + ExamHistoryInfo.CON_COL_01 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                          + ExamHistoryInfo.CON_COL_02 + " TEXT,"
                          + ExamHistoryInfo.CON_COL_03 + " TEXT,"
                          + ExamHistoryInfo.CON_COL_04 + " TEXT,"
                          + ExamHistoryInfo.CON_COL_05 + " TEXT,"
                          + ExamHistoryInfo.CON_COL_06 + " DEFAULT CURRENT_TIMESTAMP)"
                );

            // バージョンＵＰ（２->３）の処理
            case (int)2:
                // Lv2テーブルを空にする。
                db.execSQL("DELETE FROM " + GroupLv2Info.CON_TBL_NAME);

                // Lv2データを再登録する。
                List<String[]> lstCsvLv2  = TTTDB_Util.readCsvFile(OBJ_RESOURCES, "group_lv2.csv");
                for (String[] strDatas : lstCsvLv2) {
                    // カラム名と値のセットを生成しテーブルに挿入する。
                    ContentValues objValues = new ContentValues();
                    objValues.put(GroupLv2Info.CON_COL_01, strDatas[0]); // Lv2順序
                    objValues.put(GroupLv2Info.CON_COL_02, strDatas[1]); // Lv2名称
                    objValues.put(GroupLv2Info.CON_COL_03, strDatas[2]); // Lv2略称

                    Long lngID = db.insert(GroupLv2Info.CON_TBL_NAME, null, objValues);
                }

//            // バージョンＵＰ（３->４）の処理
//            case (int)3:
//
//            // バージョンＵＰ（4->5）の処理
//            case (int)4:
//
//            // バージョンＵＰ（5->6）の処理
//            case (int)5:
//
        }

        Log.d(LOG_TAG, "END onUpgrade");
    }
}
