package com.madhat.myzhihudaily.ui.weight;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by Avast on 2016/8/1.
 */
public class MyTextView extends TextView{
    public MyTextView(Context context) {
        super(context);
    }

    private int getAvailableWidth()
    {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
    public boolean isOverFlowed()
    {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > getAvailableWidth()) return true;
        return false;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        isOverFlowed();
    }
}
