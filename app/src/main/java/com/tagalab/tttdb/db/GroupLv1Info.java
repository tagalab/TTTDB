package com.tagalab.tttdb.db;

/*
　* 単語の学習範囲を管理する
 */
public class GroupLv1Info {
    public static final String CON_TBL_NAME = "group_lv1";

    public static final String CON_COL_01   = "lv1_cd";
    public static final String CON_COL_02   = "lv1_name";

    private String lv1_cd = "";
    private String lv1_name = "";

    public GroupLv1Info(String[] _strDatas) {
        // DBデータを保持する
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            lv1_cd = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            lv1_name = _strDatas[1];
        }
    }

    public String getLv1_cd() {
        return lv1_cd;
    }

    public String getLv1_name() {
        return lv1_name;
    }
}
