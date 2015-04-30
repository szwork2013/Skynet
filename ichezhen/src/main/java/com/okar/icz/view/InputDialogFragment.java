package com.okar.icz.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okar.icz.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangfengchen on 15/4/30.
 */
public class InputDialogFragment extends DialogFragment implements View.OnClickListener {

    private int mBackStackId;
    private TextView mTitleTV;
    private LinearLayout mContentLayout;
    private Button mSubmitBtn;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private OnInputDialogClickListener mOnInputDialogClickListener;

    public void setOnInputDialogClickListener(OnInputDialogClickListener listener) {
        mOnInputDialogClickListener = listener;
    }

    List<EditText> editTextList = new ArrayList<EditText>();

    public static InputDialogFragment newInstance(String title, ArrayList<String> list) {
        InputDialogFragment adf = new InputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dialog_title", title);
        bundle.putStringArrayList("dialog_list", list);
        adf.setArguments(bundle);
        return adf;
    }

    private String getTitle(){
        return getArguments().getString("dialog_title");
    }

    private ArrayList<String> getList(){
        return getArguments().getStringArrayList("dialog_list");
    }

    @Override //在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style,theme);
    }

    @Override //通过重写Fragment的onCreateView()实现dialog的UI
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //1、通过inflate，根据layout XML定义，创建view
        View v = inflater.inflate(R.layout.dialog_input, container,false);
        mTitleTV = (TextView) v.findViewById(R.id.dialog_input_title);
        mContentLayout = (LinearLayout) v.findViewById(R.id.dialog_input_content);
        mSubmitBtn = (Button) v.findViewById(R.id.dialog_input_submit);
        mSubmitBtn.setOnClickListener(this);
        mTitleTV.setText(getTitle());
        initInputContentLayout();
        return v;
    }

    private View createListItem(String label) {
        View view = getLayoutInflater(null).inflate(R.layout.dialog_input_item, null);
        TextView labelTV = (TextView) view.findViewById(R.id.dialog_input_item_label);
        EditText inputET = (EditText) view.findViewById(R.id.dialog_input_item_input);
        labelTV.setText(label);
        editTextList.add(inputET);
        return view;
    }

    private void initInputContentLayout() {
        List<String> labels = getList();
        if(labels!=null&&!labels.isEmpty()) {
            for (int i=0;i<labels.size();i++) {
                View view = createListItem(labels.get(i));
                mContentLayout.addView(view, i);
            }
        }
    }

    public int show(FragmentTransaction transaction, String tag) {
        transaction.add(this, tag);
        mBackStackId = transaction.commit();
        return mBackStackId;
    }

    @Override
    public void onClick(View v) {
        System.out.println("hah");
        List<String> results = new ArrayList<String>();
        for(EditText editText : editTextList) {
            results.add(editText.getText().toString());
        }
        mOnInputDialogClickListener.onDialogDone(true, type, results);
        dismiss();
    }

    public interface OnInputDialogClickListener {
        public void onDialogDone(boolean cancelled, int type, List<String> messages);
    }
}
