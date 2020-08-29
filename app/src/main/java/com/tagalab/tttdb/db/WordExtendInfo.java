package com.tagalab.tttdb.db;

import com.tagalab.tttdb.TTTDB_Util;

public class WordExtendInfo {
    public static final String CON_TBL_NAME = "word_expansion";

    public static final String CON_COL_01   = "word_id";
    public static final String CON_COL_02   = "result";
    public static final String CON_COL_03   = "memo_01";
    public static final String CON_COL_04   = "memo_02";
    public static final String CON_COL_05   = "memo_03";
    public static final String CON_COL_06   = "count";
    public static final String CON_COL_07   = "correct";

    // 単語ID
    private String word_id = "";
    // 解答履歴
    private String result = "";
    // メモ１
    private String memo_01 = "";
    // メモ２（予約）
    private String memo_02 = "";
    // メモ３（予約）
    private String memo_03 = "";
    // 解答数
    private String count = "";
    // 正解数
    private String correct = "";

    public WordExtendInfo(String[] _strDatas) {
        // DBデータを保持する
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            word_id = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            result = _strDatas[1];
        }
        if(_strDatas.length > (int)2 && _strDatas[2] != null) {
            memo_01 = _strDatas[2];
        }
        if(_strDatas.length > (int)3 && _strDatas[3] != null) {
            memo_02 = _strDatas[3];
        }
        if(_strDatas.length > (int)4 && _strDatas[4] != null) {
            memo_03 = _strDatas[4];
        }
        if(_strDatas.length > (int)5 && _strDatas[5] != null) {
            count = _strDatas[5];
        }
        if(_strDatas.length > (int)6 && _strDatas[6] != null) {
            correct = _strDatas[6];
        }
    }

    public String getWord_id() {
        return word_id;
    }

    public String getResult() {
        return result;
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

    public String getCount() {
        if(0 == count.length()) return "0";
        return count;
    }

    public String getCorrect() {
        if(0 == correct.length()) return "0";
        return correct;
    }

    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }

    public void setResult(String result) {
        this.result = result;
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

    public void setCount(String count) {
        this.count = count;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    // 正解率を返す
    public String getPercent() {
        return TTTDB_Util.getPercent(getCorrect(), getCount());
    }
}
