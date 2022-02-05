package com.hc.mixthebluetooth.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.hc.mixthebluetooth.R;

public class LateralMoveBar extends View {
  /**
   * out line paint
   */
  private Paint outlinePaint = new Paint();
  /**
   * finger paint
   */
  private Paint fingerPaint = new Paint();

  private RectF outLineLeftCircle;
  private RectF outLineRightCircle;
  private Path outlinePath = new Path();

  private float radius = 40;
  private float outlineLen = 600.f;
  private float currentX = radius + outlineLen * 0.5f;
  private ValueAnimator positionAnimator;
  private SlideCallback slideCallback;
  private boolean backToCenter = false;

  public LateralMoveBar(Context context) {
    super(context);
  }

  public LateralMoveBar(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int width, height;
    int resultWSpec = widthMeasureSpec;
    int resultHSpec = heightMeasureSpec;

    switch (widthMode) {
      case MeasureSpec.AT_MOST:
        width = (int) (2 * radius + outlineLen);
        resultWSpec = MeasureSpec.makeMeasureSpec(width, widthMode);
        break;
    }
    switch (heightMode) {
      case MeasureSpec.AT_MOST:
        height = (int) (2 * radius);
        resultHSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
        break;
    }
    super.onMeasure(resultWSpec, resultHSpec);
  }

  private void calcMode(int spec) {
    int mode = MeasureSpec.getMode(spec);
    switch (mode) {
      case MeasureSpec.AT_MOST:
    }
  }

  private void init() {
    outLineLeftCircle = new RectF(0, 0, 2 * radius, 2 * radius);
    outLineRightCircle = new RectF(outlineLen, 0, 2 * radius + outlineLen, 2 * radius);
    outlinePath.moveTo(radius, 0);
    outlinePath.addArc(outLineRightCircle, 90, -180);
    outlinePath.lineTo(radius, 2 * radius);
    outlinePath.addArc(outLineLeftCircle, 270, -180);
    outlinePath.lineTo(radius + outlineLen, 0);

    outlinePaint.setColor(Color.parseColor("#d1d2d6"));
    outlinePaint.setAntiAlias(true);

    fingerPaint.setColor(Color.WHITE);
    fingerPaint.setAntiAlias(true);

    positionAnimator = ValueAnimator.ofFloat(1);
    positionAnimator.addUpdateListener(animation -> {
      Float currentProgress = (Float) animation.getAnimatedValue();
      chageFingerPosition(
          (currentX - (radius + 0.5f * outlineLen)) * currentProgress + radius + outlineLen * 0.5f);
    });
    positionAnimator.setDuration(1000);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_MOVE:
        fingerPaint.setColor(Color.parseColor("#ededed"));
        if (event.getX() < radius) {
          currentX = radius;
        } else if (event.getX() > (radius + outlineLen)) {
          currentX = radius + outlineLen;
        } else {
          currentX = event.getX();
        }
        chageFingerPosition(currentX);
        positionAnimator.cancel();
        break;
      case MotionEvent.ACTION_UP:
        if (backToCenter) {
          positionAnimator.start();
        }
        fingerPaint.setColor(Color.WHITE);
        break;
    }
    return true;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawPath(outlinePath, outlinePaint);
    canvas.drawCircle(currentX, radius, radius, fingerPaint);
  }

  private void chageFingerPosition(float x) {
    Log.d("ololeeTAG", "chageFingerPosition: " + x);
    if (slideCallback != null) {
      slideCallback.slide((2.0f * (x - radius) - outlineLen) / (outlineLen));
    }
    currentX = x;
    invalidate();
  }

  public interface SlideCallback {
    void slide(float x);
  }

  public void setSlideCallback(SlideCallback slideCallback) {
    this.slideCallback = slideCallback;
  }

  public void setBackToCenter(boolean backToCenter) {
    this.backToCenter = backToCenter;
    if (currentX != (radius + 0.5 * outlineLen)) {
      currentX = (float) (radius + 0.5 * outlineLen);
      invalidate();
    }
  }
}
