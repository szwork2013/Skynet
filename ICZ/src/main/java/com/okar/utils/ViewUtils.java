package com.okar.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Created by wangfengchen on 15/3/16.
 */
public class ViewUtils {

    public static void setEditable(EditText mEdit, int maxLength, boolean value) {
//        if (value) {
//            mEdit.setFilters(new InputFilter[] { new MyEditFilter(maxLength) });
//            mEdit.setCursorVisible(true);
//            mEdit.setFocusableInTouchMode(true);
//            mEdit.requestFocus();
//        } else {
//            mEdit.setFilters(new InputFilter[] { new InputFilter() {
//                @Override
//                public CharSequence filter(CharSequence source, int start,
//                                           int end, Spanned dest, int dstart, int dend) {
//                    return source.length() < 1 ? dest.subSequence(dstart, dend)
//                            : "";
//                }
//            } });
//            mEdit.setCursorVisible(false);
//            mEdit.setFocusableInTouchMode(false);
//            mEdit.clearFocus();
//        }
    }

}
