package com.tagalab.tttdb.db;

public class SettingInfo {
    public static final String CON_TBL_NAME = "setting";

    public static final String CON_COL_01   = "set_01";
    public static final String CON_COL_02   = "set_02";
    public static final String CON_COL_03   = "set_03";
    public static final String CON_COL_04   = "set_04";
    public static final String CON_COL_05   = "set_05";
    public static final String CON_COL_06   = "set_06";
    public static final String CON_COL_07   = "set_07";
    public static final String CON_COL_08   = "set_08";
    public static final String CON_COL_09   = "set_09";
    public static final String CON_COL_10   = "set_10";

    private String set_01 = "";
    private String set_02 = "";
    private String set_03 = "";
    private String set_04 = "";
    private String set_05 = "";
    private String set_06 = "";
    private String set_07 = "";
    private String set_08 = "";
    private String set_09 = "";
    private String set_10 = "";

    public SettingInfo(String[] _strDatas) {
        if(_strDatas.length > (int)0 && _strDatas[0] != null) {
            set_01 = _strDatas[0];
        }
        if(_strDatas.length > (int)1 && _strDatas[1] != null) {
            set_02 = _strDatas[1];
        }
        if(_strDatas.length > (int)2 && _strDatas[2] != null) {
            set_03 = _strDatas[2];
        }
        if(_strDatas.length > (int)3 && _strDatas[3] != null) {
            set_04 = _strDatas[3];
        }
        if(_strDatas.length > (int)4 && _strDatas[4] != null) {
            set_05 = _strDatas[4];
        }
        if(_strDatas.length > (int)5 && _strDatas[5] != null) {
            set_06 = _strDatas[5];
        }
        if(_strDatas.length > (int)6 && _strDatas[6] != null) {
            set_07 = _strDatas[6];
        }
        if(_strDatas.length > (int)7 && _strDatas[7] != null) {
            set_08 = _strDatas[7];
        }
        if(_strDatas.length > (int)8 && _strDatas[8] != null) {
            set_09 = _strDatas[8];
        }
        if(_strDatas.length > (int)9 && _strDatas[9] != null) {
            set_10 = _strDatas[9];
        }
    }

    public String getSet_01() {
        return set_01;
    }

    public void setSet_01(String set_01) {
        this.set_01 = set_01;
    }

    public String getSet_02() {
        return set_02;
    }

    public void setSet_02(String set_02) {
        this.set_02 = set_02;
    }

    public String getSet_03() {
        return set_03;
    }

    public void setSet_03(String set_03) {
        this.set_03 = set_03;
    }

    public String getSet_04() {
        return set_04;
    }

    public void setSet_04(String set_04) {
        this.set_04 = set_04;
    }

    public String getSet_05() {
        return set_05;
    }

    public void setSet_05(String set_05) {
        this.set_05 = set_05;
    }

    public String getSet_06() {
        return set_06;
    }

    public void setSet_06(String set_06) {
        this.set_06 = set_06;
    }

    public String getSet_07() {
        return set_07;
    }

    public void setSet_07(String set_07) {
        this.set_07 = set_07;
    }

    public String getSet_08() {
        return set_08;
    }

    public void setSet_08(String set_08) {
        this.set_08 = set_08;
    }

    public String getSet_09() {
        return set_09;
    }

    public void setSet_09(String set_09) {
        this.set_09 = set_09;
    }

    public String getSet_10() {
        return set_10;
    }

    public void setSet_10(String set_10) {
        this.set_10 = set_10;
    }
}
