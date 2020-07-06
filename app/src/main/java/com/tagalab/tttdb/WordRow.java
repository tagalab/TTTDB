package com.tagalab.tttdb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.tagalab.tttdb.db.GroupLv1Info;
import com.tagalab.tttdb.db.GroupLv2Info;
import com.tagalab.tttdb.db.GroupLv3Info;
import com.tagalab.tttdb.db.WordDictionaryInfo;
import com.tagalab.tttdb.db.WordExtendInfo;

class WordRow {
    // 単語行レイアウト
    private LinearLayout row_layout;

    // 単語テキスト
    private TextView word_text;
    private View.OnClickListener word_text_Listener;

    // 単語ヒントレイアウト
    private LinearLayout hint_layout;
    // 単語ヒントボタン
    private Button hint_button;
    private View.OnClickListener hint_button_Listener;
    // 単語ヒントテスト履歴
    private TextView hint_history_text;

    // 単語ノーマルレイアウト
    private LinearLayout nomal_layout;
    // 単語ノーマルボタン
    private Button nomal_button;
    private View.OnClickListener nomal_button_Listener;
    // 単語ノーマルテスト履歴
    private TextView nomal_history_text;

    // グループLv3情報
    private GroupLv3Info GroupLv3;
    // 単語情報
    private WordDictionaryInfo WordDictionary;
    // 単語情報拡張
    private WordExtendInfo WordExtend;

    WordRow(String[] _strLv3, String[] _strWord, String[] _strWordExt) {
        GroupLv3       = new GroupLv3Info(_strLv3);
        WordDictionary = new WordDictionaryInfo(_strWord);
        WordExtend     = new WordExtendInfo(_strWordExt);

        row_layout            = null;
        word_text             = null;
        word_text_Listener    = null;
        hint_layout           = null;
        hint_button           = null;
        hint_button_Listener  = null;
        hint_history_text     = null;
        nomal_layout          = null;
        nomal_button          = null;
        nomal_button_Listener = null;
        nomal_history_text    = null;
    }

    void createWordRow(Context _context, LinearLayout _linearLayout) {
        // 単語行レイアウトを生成し画面に追加する
        row_layout = new LinearLayout(new ContextThemeWrapper(_context, R.style.WordRowLinearLayoutStyle));
        row_layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ViewGroup.MarginLayoutParams objMLP = (ViewGroup.MarginLayoutParams) row_layout.getLayoutParams();
        objMLP.setMargins(0, 10,  0, 0);
        _linearLayout.addView(row_layout);

        // 単語名を画面に追加する
        word_text = new TextView(new ContextThemeWrapper(_context, R.style.WordNameTextViewStyle));
        word_text.setOnClickListener(word_text_Listener);
        word_text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1));
        word_text.setText(WordDictionary.getMean());
        row_layout.addView(word_text);

        // 単語ヒント部を画面に追加する
        hint_layout = new LinearLayout(new ContextThemeWrapper(_context, R.style.WordColLinearLayoutStyle));
        hint_layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        objMLP = (ViewGroup.MarginLayoutParams) hint_layout.getLayoutParams();
        objMLP.setMargins(0, 0,  5, 0);
        row_layout.addView(hint_layout);

        hint_button = new Button(new ContextThemeWrapper(_context, R.style.WordButtonStyle));
        hint_button.setOnClickListener(hint_button_Listener);
        hint_button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        hint_button.setText("ヒントあり");
        hint_button.setBackgroundResource(R.drawable.button_next);
        hint_layout.addView(hint_button);

        hint_history_text = new TextView(new ContextThemeWrapper(_context, R.style.WordHistoryStyle));
        hint_history_text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        if(WordExtend.getResult_1().length() <= (int)5) {
            hint_history_text.setText(WordExtend.getResult_1());
        } else {
            hint_history_text.setText(WordExtend.getResult_1().substring(WordExtend.getResult_1().length() - (int)5));
        }
        hint_layout.addView(hint_history_text);

        // 単語ノーマル部を画面に追加する
        nomal_layout = new LinearLayout(new ContextThemeWrapper(_context, R.style.WordColLinearLayoutStyle));
        nomal_layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        objMLP = (ViewGroup.MarginLayoutParams) nomal_layout.getLayoutParams();
        objMLP.setMargins(0, 0,  5, 0);
        row_layout.addView(nomal_layout);

        nomal_button = new Button(new ContextThemeWrapper(_context, R.style.WordButtonStyle));
        nomal_button.setOnClickListener(nomal_button_Listener);
        nomal_button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        nomal_button.setText("ヒントなし");
        nomal_button.setBackgroundResource(R.drawable.button_next);
        nomal_layout.addView(nomal_button);

        nomal_history_text = new TextView(new ContextThemeWrapper(_context, R.style.WordHistoryStyle));
        nomal_history_text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        if(WordExtend.getResult_0().length() <= (int)5) {
            nomal_history_text.setText(WordExtend.getResult_0());
        } else {
            nomal_history_text.setText(WordExtend.getResult_0().substring(WordExtend.getResult_0().length() - (int)5));
        }
        nomal_layout.addView(nomal_history_text);
    }

    WordDictionaryInfo getWordDictionary() {
        return WordDictionary;
    }

    WordExtendInfo getWordExtend() {
        return WordExtend;
    }

    void setRow_layout(LinearLayout row_layout) {
        this.row_layout = row_layout;
    }

    void setWord_text_Listener(View.OnClickListener word_text_Listener) {
        this.word_text_Listener = word_text_Listener;
    }

    void setHint_button_Listener(View.OnClickListener hint_button_Listener) {
        this.hint_button_Listener = hint_button_Listener;
    }

    void setNomal_button_Listener(View.OnClickListener nomal_button_Listener) {
        this.nomal_button_Listener = nomal_button_Listener;
    }
}
