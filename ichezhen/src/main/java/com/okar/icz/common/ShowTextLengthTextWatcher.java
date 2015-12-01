package com.okar.icz.common;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/*
 * 监听输入内容是否超出最大长度，并设置光标位置
 * */
public class ShowTextLengthTextWatcher implements TextWatcher {

    private int maxLen = 0;
    private EditText editText = null;
    private TextView showTextLengthTV = null;


    public ShowTextLengthTextWatcher(int maxLen, EditText editText, TextView stv) {
        this.maxLen = maxLen;
        this.editText = editText;
        showTextLengthTV = stv;
    }

    public void afterTextChanged(Editable arg0) {
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        Editable editable = editText.getText();
        int len = editable.length();
        int textLength = len;
        if (len > maxLen) {
            textLength = maxLen;
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0, maxLen);
            editText.setText(newStr);
            editable = editText.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);

        }
        showTextLengthTV.setText(String.format("%d/%d", textLength, maxLen));
    }

}
