package com.tagalab.tttdb.db;

/*
 * 単語の検索キーワードを管理する
 */
public class GroupLv2Info {
    public static final String CON_TBL_NAME = "group_lv2";

    public static final String CON_COL_01   = "lv2_order";
    public static final String CON_COL_02   = "lv2_name";
    public static final String CON_COL_03   = "lv2_name_short";

    private String lv2_order = "";
    private String lv2_name = "";
    private String lv2_name_short = "";

    public GroupLv2Info(String[] _strDatas) {
        // DBデータを保持する
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            lv2_order = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            lv2_name = _strDatas[1];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            lv2_name_short = _strDatas[2];
        }
    }

    public String getLv2_order() {
        return lv2_order;
    }

    public String getLv2_name() {
        return lv2_name;
    }

    public String getLv2_name_short() {
        return lv2_name_short;
    }
}
