package com.tagalab.tttdb;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TTTDB_Util {
    public static List<String[]> readCsvFile(Resources _objResources, String _strFileName) {
        List lstRet = new ArrayList<String[]>();

        try {
            // assets配下のファイルを開く
            InputStream inputStream = _objResources.getAssets().open(_strFileName);

            // 1行ずつ読込む
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String   strLine  = "";
            String[] strDatas = null;

            // 1行をデータとして使用する
            while ((strLine = bufferReader.readLine()) != null) {
                // csv形式データを1カラムずつに分割する
                strDatas = strLine.split(",", -1); // 末尾の空値を無視しないように-1を設定する。

                // 1行ずつListに格納する
                lstRet.add(strDatas);
            }

            //  ファイルを閉じる
            bufferReader.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return lstRet;
    }
}
