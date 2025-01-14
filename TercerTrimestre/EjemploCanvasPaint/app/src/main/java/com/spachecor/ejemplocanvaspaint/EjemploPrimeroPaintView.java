package com.spachecor.ejemplocanvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class EjemploPrimeroPaintView extends View {

    public EjemploPrimeroPaintView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint pain = new Paint();
        pain.setColor(Color.MAGENTA);
        pain.setStrokeWidth(10);
        pain.setStyle(Paint.Style.FILL);
        canvas.drawCircle(175, 175, 100, pain);
    }
}
