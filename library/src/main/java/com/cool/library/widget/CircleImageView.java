package com.cool.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.cool.library.R;

/**
 * Created by cool on 2017/7/3.
 */

public class CircleImageView extends AppCompatImageView {

    private Paint mPaint;
    private Paint mBordPaint;
    private int width;
    private Drawable drawable;
    private float borderWidth;
    private int borderColor;
    private int defaultWidth;
    private int defalutHight;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        borderWidth = ta.getDimension(R.styleable.CircleImageView_borderWidth, -1);
        borderColor = ta.getColor(R.styleable.CircleImageView_borderColor, Color.WHITE);
        ta.recycle();
        init();
    }

    private void init() {
        defaultWidth = defalutHight = dp2px(100);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mBordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBordPaint.setColor(borderColor);
        mBordPaint.setStrokeWidth(borderWidth);
        mBordPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawable == null) {
            return;
        }
        canvas.drawCircle(width / 2, width / 2, width / 2, mPaint);
        if(borderWidth != -1) {
            RectF rectF = new RectF();
            rectF.left = borderWidth / 2;
            rectF.top = borderWidth / 2;
            rectF.right = width - borderWidth / 2;
            rectF.bottom = width - borderWidth / 2;
            canvas.drawArc(rectF, 0, 360, false, mBordPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = makeMeasure(widthMeasureSpec);
        int hight = makeMeasure(heightMeasureSpec);
        int realitySize = Math.min(width, hight);
        setMeasuredDimension(realitySize, realitySize);
    }

    private int makeMeasure(int measureSpec) {
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        int expectSize;
        if (mode == MeasureSpec.EXACTLY) {
            expectSize = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            expectSize = Math.min(defaultWidth, size);
        } else {
            expectSize = defaultWidth;
        }
        return expectSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        int bitmapWidth = drawable.getIntrinsicWidth();
        float scale = width * 1.0f / bitmapWidth;
        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(bitmapShader);
    }

    public int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        int px = (int) (density * dp + 0.5f);
        return px;
    }
}
