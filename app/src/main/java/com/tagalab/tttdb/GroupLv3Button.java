package com.tagalab.tttdb;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import java.util.ArrayList;
import java.util.List;

import com.tagalab.tttdb.db.GroupLv3Info;

class GroupLv3Button {
    // グループLv1情報
    private GroupLv3Info Lv3Info;
    // 自ボタン
    private Button Lv3Button;
    // 子要素
    private List<WordRow> WordRows;
    // 自エリア
    private LinearLayout Lv3Area;
    // エリア開閉
    private int AreaOpen;
    // コンテキスト
    private Context mainActivityContext;

    GroupLv3Button(String[] _strDatas) {
        Lv3Info   = new GroupLv3Info(_strDatas);
        Lv3Button = null;
        WordRows  = new ArrayList<>();
        Lv3Area   = null;
        AreaOpen  = LinearLayout.GONE;
        mainActivityContext = null;
    }

    void createButton(Context _context, LinearLayout _linearLayout) {
        mainActivityContext = _context;

        // XMLのスタイルを適用したLv1ボタンを作成し画面に追加する
        Lv3Button = new Button(new ContextThemeWrapper(_context, R.style.MainMenuButtonStyle));
        _linearLayout.addView(Lv3Button);

        // XMLのスタイルを適用していても、Javaではlayout_marginやdrawableは反映されないので直接設定する
        Lv3Button.setBackgroundResource(R.drawable.button_next);
        ViewGroup.MarginLayoutParams objMLP = (ViewGroup.MarginLayoutParams) Lv3Button.getLayoutParams();
        objMLP.setMargins(0, 20,  0, 0);
        Lv3Button.setLayoutParams(objMLP);
        Lv3Button.getLayoutParams().height = (int)(60 * MainActivity.THIS_SCALE);
        Lv3Button.setText(Lv3Info.getLv3_name());
        Lv3Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pushButton();
            }
        });

        // 配下のエリアを作成する
        Lv3Area = new LinearLayout(new ContextThemeWrapper(_context, R.style.Lv3AreaStyle));
        _linearLayout.addView(Lv3Area);
        createArea();
    }

    GroupLv3Info getInfo() {
        return Lv3Info;
    }

    Button getButton() {
        return Lv3Button;
    }

    List<WordRow> getChilds() {
        return WordRows;
    }

    LinearLayout getLv3Area() {
        return Lv3Area;
    }

    int getAreaOpen() {
        return AreaOpen;
    }

    void addChild(WordRow _child) {
        WordRows.add(_child);
    }

    void pushButton() {
        if (LinearLayout.GONE == AreaOpen) {
            // 自エリアを開く
            Lv3Button.getLayoutParams().height = (int)(50 * MainActivity.THIS_SCALE);
            AreaOpen = LinearLayout.VISIBLE;

        } else {
            // 自エリアを閉じる
            Lv3Button.getLayoutParams().height = (int)(60 * MainActivity.THIS_SCALE);
            AreaOpen = LinearLayout.GONE;
        }

        createArea();
    }

    void createArea() {
        if (LinearLayout.VISIBLE == AreaOpen) {
            // 単語行を作成し画面に追加する
            for(final WordRow objWorkWord : WordRows) {
                objWorkWord.createWordRow(mainActivityContext, Lv3Area);
            }

        } else {
            // 単語列を削除する
            Lv3Area.removeAllViews();
        }

        Lv3Area.setVisibility(AreaOpen);
    }
}
