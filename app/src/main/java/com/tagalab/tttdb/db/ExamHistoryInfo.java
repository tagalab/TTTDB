package com.tagalab.tttdb.db;

public class ExamHistoryInfo {
    public static final String CON_TBL_NAME = "exam_history";

    public static final String CON_COL_01   = "history_id";
    public static final String CON_COL_02   = "word_id";
    public static final String CON_COL_03   = "result";
    public static final String CON_COL_04   = "hint_cd";
    public static final String CON_COL_05   = "input_word";
    public static final String CON_COL_06   = "input_time";

    private String history_id = "";
    private String word_id = "";
    private String result = "";
    private String hint_cd = "";
    private String input_word = "";
    private String input_time = "";

    public ExamHistoryInfo(String[] _strDatas) {
        // DBデータを保持する
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            history_id = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            word_id = _strDatas[1];
        }
        if(_strDatas.length > (int)2 && _strDatas[2] != null) {
            result = _strDatas[2];
        }
        if(_strDatas.length > (int)3 && _strDatas[3] != null) {
            hint_cd = _strDatas[3];
        }
        if(_strDatas.length > (int)4 && _strDatas[4] != null) {
            input_word = _strDatas[4];
        }
        if(_strDatas.length > (int)5 && _strDatas[5] != null) {
            input_time = _strDatas[5];
        }
    }

    public String getHistory_id() {
        return history_id;
    }

    public String getWord_id() {
        return word_id;
    }

    public String getResult() {
        return result;
    }

    public String getHint_cd() {
        return hint_cd;
    }

    public String getInput_word() {
        return input_word;
    }

    public String getInput_time() {
        return input_time;
    }
}
