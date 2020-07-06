package com.tagalab.tttdb.db;

/*
 * 単語のグループを管理する
 */
public class GroupLv3Info {
    public static final String CON_TBL_NAME = "group_lv3";

    public static final String CON_COL_01   = "lv3_cd";
    public static final String CON_COL_02   = "lv3_order";
    public static final String CON_COL_03   = "lv3_name";

    private String lv3_cd = "";
    private String lv3_order = "";
    private String lv3_name = "";

    public GroupLv3Info(String[] _strDatas) {
        // DBデータを保持する
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            lv3_cd = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            lv3_order = _strDatas[1];
        }
        if(_strDatas.length > (int)2 && _strDatas[2] != null) {
            lv3_name = _strDatas[2];
        }
    }

    public String getLv3_cd() {
        return lv3_cd;
    }

    public String getLv3_order() {
        return lv3_order;
    }

    public String getLv3_name() {
        return lv3_name;
    }
}
