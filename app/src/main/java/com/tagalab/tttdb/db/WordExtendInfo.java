package com.tagalab.tttdb.db;

import com.tagalab.tttdb.TTTDB_Util;

public class WordExtendInfo {
    public static final String CON_TBL_NAME = "word_expansion";

    public static final String CON_COL_01   = "word_id";
    public static final String CON_COL_02   = "result_0";
    public static final String CON_COL_03   = "result_1";
    public static final String CON_COL_04   = "memo_01";
    public static final String CON_COL_05   = "memo_02";
    public static final String CON_COL_06   = "memo_03";
    public static final String CON_COL_07   = "count_0";
    public static final String CON_COL_08   = "count_1";
    public static final String CON_COL_09   = "correct_0";
    public static final String CON_COL_10   = "correct_1";

    // 単語ID
    private String word_id = "";
    // 解答履歴（ノーマル）
    private String result_0 = "";
    // 解答履歴（ヒント１）
    private String result_1 = "";
    // メモ１
    private String memo_01 = "";
    // メモ２（予約）
    private String memo_02 = "";
    // メモ３（予約）
    private String memo_03 = "";
    // 解答数（ノーマル）
    private String count_0 = "";
    // 解答数（ヒント１）
    private String count_1 = "";
    // 正解数（ノーマル）
    private String correct_0 = "";
    // 正解数（ヒント１）
    private String correct_1 = "";

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
            count_0 = _strDatas[6];
        }
        if(_strDatas.length > (int)7 && _strDatas[7] != null) {
            count_1 = _strDatas[7];
        }
        if(_strDatas.length > (int)8 && _strDatas[8] != null) {
            correct_0 = _strDatas[8];
        }
        if(_strDatas.length > (int)8 && _strDatas[8] != null) {
            correct_1 = _strDatas[8];
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

    public String getCount_0() {
        if(0 == count_0.length()) return "0";
        return count_0;
    }

    public String getCount_1() {
        if(0 == count_1.length()) return "0";
        return count_1;
    }

    // 解答数（総合）を返す
    public String getCount_all() {
        int intRet = Integer.parseInt(getCount_0()) + Integer.parseInt(getCount_1());
        return "" + intRet;
    }

    public String getCorrect_0() {
        if(0 == correct_0.length()) return "0";
        return correct_0;
    }

    public String getCorrect_1() {
        if(0 == correct_1.length()) return "0";
        return correct_1;
    }

    // 正解数（総合）を返す
    public String getCorrect_all() {
        int intRet = Integer.parseInt(getCorrect_0()) + Integer.parseInt(getCorrect_1());
        return "" + intRet;
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

    public void setCount_0(String count_0) {
        this.count_0 = count_0;
    }

    public void setCount_1(String count_1) {
        this.count_1 = count_1;
    }

    public void setCorrect_0(String correct_0) {
        this.correct_0 = correct_0;
    }

    public void setCorrect_1(String correct_1) {
        this.correct_1 = correct_1;
    }

    // 正解率（ノーマル）を返す
    public String getPercent_0() {
        return TTTDB_Util.getPercent(getCorrect_0(), getCount_0());
    }

    // 正解率（ヒント１）を返す
    public String getPercent_1() {
        return TTTDB_Util.getPercent(getCorrect_1(), getCount_1());
    }

    // 正解率（総合）を返す
    public String getPercent_all() {
        return TTTDB_Util.getPercent(getCorrect_all(), getCount_all());
    }

}
