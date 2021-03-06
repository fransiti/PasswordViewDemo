package com.bhavesh.passwordview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by bhavesh on 17/3/16.
 */
public class PasswordView extends EditText {
    private Drawable eye;
    private Drawable eyeWithStrike;
    private final static int VISIBILITY_ENABLED = (int) (255 * .54f); // 54%
    private final static int VISIBLITY_DISABLED = (int) (255 * .38f); // 38%
    private boolean visible = false;
    private boolean useStrikeThrough = false;
    private Typeface typeface;

    public PasswordView(Context context) {
        super(context);
        init(null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attr) {
        if (attr != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attr, R.styleable.PasswordView, 0, 0);
            try {
                useStrikeThrough = typedArray.getBoolean(R.styleable.PasswordView_useStrikeThrough, false);
            } finally {
                typedArray.recycle();
            }
        }


        // Make sure to mutate so that if there are multiple password fields, they can have
        // different visibilities.
        eye = ContextCompat.getDrawable(getContext(), R.drawable.eye).mutate();
        eyeWithStrike = ContextCompat.getDrawable(getContext(), R.drawable.eye_strike).mutate();
        eyeWithStrike.setAlpha(VISIBILITY_ENABLED);
        setup();

    }


    protected void setup() {
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        Drawable drawable = useStrikeThrough && !visible ? eyeWithStrike : eye;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
        eye.setAlpha(visible && !useStrikeThrough ? VISIBILITY_ENABLED : VISIBLITY_DISABLED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                && event.getX() >= (getRight() - getCompoundDrawables()[2].getBounds().width())) {
            visible = !visible;
            setup();
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void setInputType(int type) {
        this.typeface = getTypeface();
        super.setInputType(type);
        setTypeface(typeface);
    }

    public void setUseStrikeThrough(boolean useStrikeThrough) {
        this.useStrikeThrough = useStrikeThrough;
    }
}
