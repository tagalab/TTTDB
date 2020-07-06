package com.tagalab.tttdb;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.view.ContextThemeWrapper;

import java.util.ArrayList;
import java.util.List;

import com.tagalab.tttdb.db.GroupLv1Info;

class GroupLv1Button {
    // グループLv1情報
    private GroupLv1Info Lv1Info;
    // 自ボタン
    private Button Lv1Button;
    // 子ボタン
    private List<GroupLv2Button> Lv2Buttons;
    // 自エリア
    private LinearLayout Lv1Area;
    // エリア開閉
    private int AreaOpen;

    GroupLv1Button(String[] _strDatas) {
        Lv1Info    = new GroupLv1Info(_strDatas);
        Lv1Button  = null;
        Lv2Buttons = new ArrayList<>();
        Lv1Area    = null;
        AreaOpen   = LinearLayout.GONE;
    }

    void createButton(Context _context, LinearLayout _linearLayout) {
        // XMLのスタイルを適用したLv1ボタンを作成し画面に追加する
        Lv1Button = new Button(new ContextThemeWrapper(_context, R.style.MenuButtonStyle));
        _linearLayout.addView(Lv1Button);

        // XMLのスタイルを適用していても、Javaではlayout_marginやdrawableは反映されないので直接設定する
        Lv1Button.setBackgroundResource(R.drawable.button_next);
        ViewGroup.MarginLayoutParams objMLP = (ViewGroup.MarginLayoutParams) Lv1Button.getLayoutParams();
        objMLP.setMargins(0, 50,  0, 0);
        Lv1Button.setLayoutParams(objMLP);
        Lv1Button.setText(Lv1Info.getLv1_name());

        // 配下のエリアを作成する
        Lv1Area = new LinearLayout(new ContextThemeWrapper(_context, R.style.Lv1AreaStyle));
        _linearLayout.addView(Lv1Area);
        Lv1Area.setVisibility(AreaOpen);
    }

    GroupLv1Info getInfo() {
        return Lv1Info;
    }

    Button getButton() {
        return Lv1Button;
    }

    List<GroupLv2Button> getChilds() {
        return Lv2Buttons;
    }

    LinearLayout getLv1Area() {
        return Lv1Area;
    }

    void addChild(GroupLv2Button _child) {
        _child.setParent(this);
        Lv2Buttons.add(_child);
    }

    void pushButton() {
        if (LinearLayout.GONE == AreaOpen) {
            // 自エリアを開く
            AreaOpen = LinearLayout.VISIBLE;

        } else {
            // 自エリアを閉じる
            AreaOpen = LinearLayout.GONE;
        }

        Lv1Area.setVisibility(AreaOpen);
    }
}
