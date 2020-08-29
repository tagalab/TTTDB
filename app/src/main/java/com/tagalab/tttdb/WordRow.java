package com.tagalab.tttdb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.tagalab.tttdb.db.WordDictionaryInfo;
import com.tagalab.tttdb.db.WordExtendInfo;

class WordRow {
    // todo テーブルレイアウトの行として作成する

    // 単語行レイアウト
    private LinearLayout row_layout;

    // 単語テキスト
    private TextView word_text;
    private View.OnClickListener word_text_Listener;

    // 単語テストボタン
    private TextView exam_text;
    private View.OnClickListener exam_text_Listener;

    // 単語情報
    private WordDictionaryInfo WordDictionary;
    // 単語情報拡張
    private WordExtendInfo WordExtend;

    WordRow(String[] _strWord, String[] _strWordExt) {
        WordDictionary = new WordDictionaryInfo(_strWord);
        WordExtend     = new WordExtendInfo(_strWordExt);

        row_layout         = null;
        word_text          = null;
        word_text_Listener = null;
        exam_text          = null;
        exam_text_Listener = null;
    }

    void createWordRow(Context _context, LinearLayout _linearLayout) {
        int intMargin = (int)(1 * MainActivity.THIS_SCALE + 0.5f);

        // 単語行レイアウトを生成し画面に追加する
        row_layout = new LinearLayout(new ContextThemeWrapper(_context, R.style.WordRowLayoutStyle));
        _linearLayout.addView(row_layout);
        row_layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // 単語テスト部を画面に追加する
        exam_text = new TextView(new ContextThemeWrapper(_context, R.style.WordRowWordStyle));
        row_layout.addView(exam_text);
        exam_text.setOnClickListener(exam_text_Listener);
        exam_text.setBackgroundResource(R.drawable.button_fire);
        LinearLayout.LayoutParams objParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        objParams1.setMargins(intMargin, intMargin,  intMargin, intMargin);
        exam_text.setLayoutParams(objParams1);
        exam_text.setWidth((int)(100 * MainActivity.THIS_SCALE + 0.5f));
        exam_text.setHeight((int)(60 * MainActivity.THIS_SCALE + 0.5f));
        String strText;
        if(WordExtend.getResult().length() <= (int)5) {
            strText = WordExtend.getPercent() + "%\n" + WordExtend.getResult();
        } else {
            strText = WordExtend.getPercent() + "%\n" + WordExtend.getResult().substring(WordExtend.getResult().length() - (int)5);
        }
        exam_text.setText(strText);

        // 単語名を画面に追加する
        word_text = new TextView(new ContextThemeWrapper(_context, R.style.WordRowWordStyle));
        row_layout.addView(word_text);
        word_text.setOnClickListener(word_text_Listener);
        word_text.setBackgroundResource(R.drawable.button_fire);
        LinearLayout.LayoutParams objParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        objParams2.setMargins(intMargin, intMargin,  intMargin, intMargin);
        word_text.setLayoutParams(objParams2);
        word_text.setHeight((int)(60 * MainActivity.THIS_SCALE + 0.5f));
        word_text.setText(WordDictionary.getMean());
    }

    WordDictionaryInfo getWordDictionary() {
        return WordDictionary;
    }

    WordExtendInfo getWordExtend() {
        return WordExtend;
    }

    void setWord_text_Listener(View.OnClickListener word_text_Listener) {
        this.word_text_Listener = word_text_Listener;
    }

    void setExam_text_Listener(View.OnClickListener exam_text_Listener) {
        this.exam_text_Listener = exam_text_Listener;
    }
}
