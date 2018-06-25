package com.zzh.findit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.zzh.findit.R;

/**
 * Created by 腾翔信息 on 2018/3/15.
 */

public class DashView extends View {
    private Paint mPaint;
    private Path mPath;

    public DashView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.main_bottom_sel_color));
        // 需要加上这句，否则画不出东西
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPath = new Path();
        mPath.addCircle(0, 0, 3, Path.Direction.CW);
        mPaint.setPathEffect(new PathDashPathEffect(mPath, 15, 0, PathDashPathEffect.Style.ROTATE));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerY = getHeight() / 2;
        mPath.reset();
        mPath.moveTo(0, centerY);
        mPath.lineTo(getWidth(), centerY);
        canvas.drawPath(mPath, mPaint);
    }

}
