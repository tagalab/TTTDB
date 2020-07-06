package com.tagalab.tttdb.db;

public class WordExtendInfo {
    public static final String CON_TBL_NAME = "word_expansion";

    public static final String CON_COL_01   = "word_id";
    public static final String CON_COL_02   = "result_0";
    public static final String CON_COL_03   = "result_1";
    public static final String CON_COL_04   = "memo_01";
    public static final String CON_COL_05   = "memo_02";
    public static final String CON_COL_06   = "memo_03";
    public static final String CON_COL_07   = "persent";

    private String word_id = "";
    private String result_0 = "";
    private String result_1 = "";
    private String memo_01 = "";
    private String memo_02 = "";
    private String memo_03 = "";
    private String persent = "";

    public WordExtendInfo(String[] _strDatas) {
        // DBデータを保持する
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            word_id = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            result_0 = _strDatas[1];
        }
        if(_strDatas.length > (int)2 && _strDatas[2] != null) {
            result_1 = _strDatas[2];
        }
        if(_strDatas.length > (int)3 && _strDatas[3] != null) {
            memo_01 = _strDatas[3];
        }
        if(_strDatas.length > (int)4 && _strDatas[4] != null) {
            memo_02 = _strDatas[4];
        }
        if(_strDatas.length > (int)5 && _strDatas[5] != null) {
            memo_03 = _strDatas[5];
        }
        if(_strDatas.length > (int)6 && _strDatas[6] != null) {
            persent = _strDatas[6];
        }
    }

    public String getWord_id() {
        return word_id;
    }

    public String getResult_0() {
        return result_0;
    }

    public String getResult_1() {
        return result_1;
    }

    public String getMemo_01() {
        return memo_01;
    }

    public String getMemo_02() {
        return memo_02;
    }

    public String getMemo_03() {
        return memo_03;
    }

    public String getPersent() {
        return persent;
    }

    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }

    public void setResult_0(String result_0) {
        this.result_0 = result_0;
    }

    public void setResult_1(String result_1) {
        this.result_1 = result_1;
    }

    public void setMemo_01(String memo_01) {
        this.memo_01 = memo_01;
    }

    public void setMemo_02(String memo_02) {
        this.memo_02 = memo_02;
    }

    public void setMemo_03(String memo_03) {
        this.memo_03 = memo_03;
    }

    public void setPersent(String persent) {
        this.persent = persent;
    }
}
