package com.eways.etutor.views;

/**
 * Created by ADMIN on 5/17/2018.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.eways.etutor.R;
import com.eways.etutor.Utils.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Yang on 2016/12/19 0019.
 * 验证码 view
 */

public class VerificationCodeView extends LinearLayout {

    public interface CodeCompleteListener{
        void complete(boolean complete);
    }

    private float mVCodeTextSize;

    private int mVCodeTextColor;

    private Drawable mVCodeBottomIcon;
    private Drawable mVCodeBottomErrorIcon;

    private int mVCodeItemCenterSpaceSize;

    private StringBuilder mCodeStringBuilder;

    private List<TextView> mCodeViewList;

    private CodeCompleteListener mCodeCompleteListener;

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VerificationCodeView, defStyle, 0);
        mVCodeTextSize = typedArray.getDimensionPixelSize(
                R.styleable.VerificationCodeView_vcodeTextSize
                , getResources().getDimensionPixelSize(R.dimen.vcode_text_size));
        mVCodeTextColor = typedArray.getColor(
                R.styleable.VerificationCodeView_vcodeTextColor,
                getResources().getColor(R.color.vcode_text_color));
        mVCodeBottomIcon = typedArray.getDrawable(R.styleable.VerificationCodeView_vcodeBottomIcon);
        if (mVCodeBottomIcon == null) {
            mVCodeBottomIcon = getResources().getDrawable(R.drawable.icon_vcode_bottom);
        }
        mVCodeBottomErrorIcon = typedArray.getDrawable(R.styleable.VerificationCodeView_vcodeBottomIcon);
        if (mVCodeBottomErrorIcon == null) {
            mVCodeBottomErrorIcon = getResources().getDrawable(R.drawable.icon_vcode_err_bottom);
        }
        int mVCodeViewCount = typedArray.getInt(R.styleable.VerificationCodeView_vcodeViewCount, 4);
        mVCodeItemCenterSpaceSize = typedArray.getDimensionPixelSize(
                R.styleable.VerificationCodeView_vcodeItemSpaceSize,
                getResources().getDimensionPixelSize(R.dimen.vcode_item_space_size));
        typedArray.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        setFocusableInTouchMode(true);

        if (mCodeStringBuilder == null) mCodeStringBuilder = new StringBuilder();
        mCodeViewList = new ArrayList<>();

        for (int i = 0; i < mVCodeViewCount; i++) {
            TextView underLineCodeView = getUnderLineCodeView();
            mCodeViewList.add(underLineCodeView);
            addView(underLineCodeView);
        }
    }

    public void setCodeCompleteListener(CodeCompleteListener listener) {
        mCodeCompleteListener = listener;
    }

    private TextView getUnderLineCodeView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(mVCodeTextSize);
        textView.setTextColor(mVCodeTextColor);
        textView.setGravity(Gravity.CENTER);
        int padding = mVCodeItemCenterSpaceSize / 2;
        textView.setPadding(
                padding
                , 0
                , padding
                , 0);
        int bottomIconWidth = (int) getResources().getDimension(R.dimen.vcode_bottom_icon_width);
        int bottomIconHeight = (int) getResources().getDimension(R.dimen.vcode_bottom_icon_height);
        TextViewUtils.addTextViewBottomIcon(textView, mVCodeBottomIcon, bottomIconWidth, bottomIconHeight);
        return textView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestFocusFromTouch();
        requestFocus();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
        }
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        BaseInputConnection fic = new BaseInputConnection(this, false);
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_NONE;
        return fic;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mCodeStringBuilder == null) mCodeStringBuilder = new StringBuilder();
        if (keyCode == 67 && mCodeStringBuilder.length() > 0) {
            mCodeStringBuilder.deleteCharAt(mCodeStringBuilder.length() - 1);
            resetCodeShowView();
        } else if (keyCode >= 7
                && keyCode <= 16
                && mCodeStringBuilder.length() < mCodeViewList.size()) {
            mCodeStringBuilder.append(keyCode - 7);
            resetCodeShowView();
        }
        if (mCodeStringBuilder.length() >= mCodeViewList.size() || keyCode == 66) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void resetCodeShowView() {
        if (mCodeStringBuilder == null || mCodeViewList == null || mCodeViewList.size() <= 0) {
            return;
        }

        for (int i = 0 ; i< mCodeViewList.size(); i++) {
            mCodeViewList.get(i).setText("");
            if (i < mCodeStringBuilder.length()) {
                mCodeViewList.get(i).setText(String.valueOf(mCodeStringBuilder.charAt(i)));
            }
        }

        if (mCodeCompleteListener != null) {
            if (mCodeStringBuilder.length() == mCodeViewList.size()) {
                mCodeCompleteListener.complete(true);
            } else {
                mCodeCompleteListener.complete(false);
            }
        }
    }

    public void setCodeItemLineDrawable(Drawable drawable) {
        for (TextView textView : mCodeViewList) {
            int bottomIconWidth = (int) getResources().getDimension(R.dimen.vcode_bottom_icon_width);
            int bottomIconHeight = (int) getResources().getDimension(R.dimen.vcode_bottom_icon_height);
            TextViewUtils.addTextViewBottomIcon(textView, drawable, bottomIconWidth, bottomIconHeight);
        }
    }

    public void resetCodeItemLineDrawable() {
        setCodeItemLineDrawable(mVCodeBottomIcon);
    }

    public void setCodeItemErrorLineDrawable() {
        for (TextView textView : mCodeViewList) {
            int bottomIconWidth = (int) getResources().getDimension(R.dimen.vcode_bottom_icon_width);
            int bottomIconHeight = (int) getResources().getDimension(R.dimen.vcode_bottom_icon_height);
            TextViewUtils.addTextViewBottomIcon(textView, mVCodeBottomErrorIcon, bottomIconWidth, bottomIconHeight);
        }
    }

    public String getTextString() {
        return mCodeStringBuilder == null ? "" : mCodeStringBuilder.toString();
    }
}
