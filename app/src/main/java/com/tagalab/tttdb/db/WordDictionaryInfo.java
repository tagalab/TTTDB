package com.tagalab.tttdb.db;

public class WordDictionaryInfo {
    public static final String CON_TBL_NAME = "word_dictionary";

    public static final String CON_COL_01 = "word_id";
    public static final String CON_COL_02 = "lv1_cd";
    public static final String CON_COL_03 = "lv3_cd";
    public static final String CON_COL_04 = "word_order";
    public static final String CON_COL_05 = "all_type";
    public static final String CON_COL_06 = "word";
    public static final String CON_COL_07 = "mean";
    public static final String CON_COL_08 = "hint";
    public static final String CON_COL_09 = "phonetic";
    public static final String CON_COL_10 = "example";

    // 単語コード
    private String word_id = "";
    // Lv1コード
    private String lv1_cd = "";
    // Lv3コード
    private String lv3_cd = "";
    // 順序
    private String word_order = "";
    // 全品詞
    private String all_type = "";
    // 単語
    private String word = "";
    // 意味
    private String mean = "";
    // ヒント
    private String hint = "";
    // 発音記号
    private String phonetic = "";
    // 用例
    private String example = "";

    public WordDictionaryInfo(String[] _strDatas) {
        // DBデータを保持する
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            word_id = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            lv1_cd = _strDatas[1];
        }
        if(_strDatas.length > (int)2 && _strDatas[2] != null) {
            lv3_cd = _strDatas[2];
        }
        if(_strDatas.length > (int)3 && _strDatas[3] != null) {
            word_order = _strDatas[3];
        }
        if(_strDatas.length > (int)4 && _strDatas[4] != null) {
            all_type = _strDatas[4];
        }
        if(_strDatas.length > (int)5 && _strDatas[5] != null) {
            word = _strDatas[5];
        }
        if(_strDatas.length > (int)6 && _strDatas[6] != null) {
            mean = _strDatas[6];
        }
        if(_strDatas.length > (int)7 && _strDatas[7] != null) {
            hint = _strDatas[7];
        }
        if(_strDatas.length > (int)8 && _strDatas[8] != null) {
            phonetic = _strDatas[8];
        }
        if(_strDatas.length > (int)9 && _strDatas[9] != null) {
            example = _strDatas[9];
        }
    }

    public String getWord_id() {
        return word_id;
    }

    public String getLv1_cd() {
        return lv1_cd;
    }

    public String getLv3_cd() {
        return lv3_cd;
    }

    public String getWord_order() {
        return word_order;
    }

    public String getAll_type() {
        return all_type;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }

    public String getHint() {
        return hint;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public String getExample() {
        return example;
    }
}
