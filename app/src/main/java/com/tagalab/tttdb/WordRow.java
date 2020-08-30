package com.tagalab.tttdb;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.tagalab.tttdb.db.WordDictionaryInfo;
import com.tagalab.tttdb.db.WordExtendInfo;

class WordRow {
    // 単語行レイアウト
    private LinearLayout row_layout;

    // 単語テキスト
    private TextView word_text;

    // 単語情報ボタン
    private TextView info_text;
    private View.OnClickListener info_Listener;

    // 単語テストボタン
    private TextView exam_text;
    private View.OnClickListener exam_Listener;

    // 単語情報
    private WordDictionaryInfo WordDictionary;
    // 単語情報拡張
    private WordExtendInfo WordExtend;

    WordRow(String[] _strWord, String[] _strWordExt) {
        WordDictionary = new WordDictionaryInfo(_strWord);
        WordExtend     = new WordExtendInfo(_strWordExt);

        row_layout    = null;
        word_text     = null;
        info_text     = null;
        info_Listener = null;
        exam_text     = null;
        exam_Listener = null;
    }

    void createWordRow(Context _context, LinearLayout _linearLayout) {
        // 単語行レイアウトを生成し画面に追加する
        row_layout = new LinearLayout(new ContextThemeWrapper(_context, R.style.WordRowLayoutStyle));
        _linearLayout.addView(row_layout);
        LinearLayout.LayoutParams objParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        objParams1.setMargins(0, (int)(3 * MainActivity.THIS_SCALE + 0.5f),  0, 0);
        row_layout.setLayoutParams(objParams1);

        // 単語名を画面に追加する
        word_text = new TextView(new ContextThemeWrapper(_context, R.style.WordRowWordStyle));
        row_layout.addView(word_text);
        word_text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1));
        word_text.setText(WordDictionary.getMean());

        // 単語情報ボタンを画面に追加する
        info_text = new TextView(new ContextThemeWrapper(_context, R.style.WordRowInfoStyle));
        row_layout.addView(info_text);
        info_text.setOnClickListener(info_Listener);
        info_text.setBackgroundResource(R.drawable.button_next);
        info_text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        info_text.setText("情　報\n" + WordExtend.getPercent() + "％");

        // 単語テストボタンを画面に追加する
        exam_text = new TextView(new ContextThemeWrapper(_context, R.style.WordRowExamStyle));
        row_layout.addView(exam_text);
        exam_text.setOnClickListener(exam_Listener);
        exam_text.setBackgroundResource(R.drawable.button_next);
        LinearLayout.LayoutParams objParams3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        objParams3.setMargins((int)(2 * MainActivity.THIS_SCALE + 0.5f), 0,  0, 0);
        exam_text.setLayoutParams(objParams3);
        String strText;
        if(WordExtend.getResult().length() <= (int)5) {
            strText = "テストする\n" + WordExtend.getResult();
        } else {
            strText = "テストする\n" + WordExtend.getResult().substring(WordExtend.getResult().length() - (int)5);
        }
        exam_text.setText(strText);
    }

    WordDictionaryInfo getWordDictionary() {
        return WordDictionary;
    }

    WordExtendInfo getWordExtend() {
        return WordExtend;
    }

    void setInfo_Listener(View.OnClickListener info_Listener) {
        this.info_Listener = info_Listener;
    }

    void setExam_Listener(View.OnClickListener exam_Listener) {
        this.exam_Listener = exam_Listener;
    }
}
