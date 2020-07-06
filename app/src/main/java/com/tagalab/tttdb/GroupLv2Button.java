package com.tagalab.tttdb;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.view.ContextThemeWrapper;

import com.tagalab.tttdb.db.GroupLv2Info;

class GroupLv2Button {
    // グループLv2情報
    private GroupLv2Info Lv2Info;
    // 自ボタン
    private Button Lv2Button;
    // 親ボタン
    private GroupLv1Button Lv1Button;

    GroupLv2Button(String[] _strDatas) {
        Lv2Info = new GroupLv2Info(_strDatas);
        Lv2Button = null;
        Lv1Button = null;
    }

    void createGroupLv2Button(Context _context, LinearLayout _linearLayout) {
        // XMLのスタイルを適用したLv2ボタンを作成し画面に追加する
        Lv2Button = new Button(new ContextThemeWrapper(_context, R.style.MenuButtonStyle));
        _linearLayout.addView(Lv2Button);

        // XMLのスタイルを適用していても、Javaではlayout_marginやdrawableは反映されないので直接設定する
        Lv2Button.setBackgroundResource(R.drawable.button_next);
        ViewGroup.MarginLayoutParams objMLP = (ViewGroup.MarginLayoutParams) Lv2Button.getLayoutParams();
        objMLP.setMargins(50, 10,  50, 0);
        Lv2Button.setLayoutParams(objMLP);
        Lv2Button.setText(Lv2Info.getLv2_name());
    }

    GroupLv2Info getInfo() {
        return Lv2Info;
    }

    Button getButton() {
        return Lv2Button;
    }

    GroupLv1Button getParent() {
        return Lv1Button;
    }

    void setParent(GroupLv1Button lv1Button) {
        Lv1Button = lv1Button;
    }
}
