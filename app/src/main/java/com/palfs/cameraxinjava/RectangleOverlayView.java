package com.palfs.cameraxinjava;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class RectangleOverlayView extends View {

    private Paint paint;
    private int left, top, right, bottom;

    public RectangleOverlayView(Context context) {
        super(context);
        init();
    }

    public RectangleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectangleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(25); // Adjust stroke width as needed
    }

    // Method to update the position of the rectangle
    public void updateRectPosition(float leftPercentile, float topPercentile, float rightPercentile, float bottomPercentile) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();

        // Calculate coordinates based on percentiles and screen size
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels - 500;

        int paddingLR = (int) (0.25 * screenWidth); // 25% of the screen width for left and right padding
        int paddingTB = screenHeight / 15; // One-sixth of the screen height for top and bottom padding

        // Calculate coordinates for the rectangle
        left = paddingLR;
        top = (int) (screenHeight * topPercentile) + paddingTB;
        right = screenWidth - paddingLR;
        bottom = (int) (screenHeight * bottomPercentile) - paddingTB;

        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw a green rectangle on the canvas
        canvas.drawRect(left, top, right, bottom, paint);
//        canvas.drawRect(200, 200, 200, 200, paint);
    }
}
