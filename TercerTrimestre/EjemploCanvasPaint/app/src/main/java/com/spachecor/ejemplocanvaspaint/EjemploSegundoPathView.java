package com.spachecor.ejemplocanvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.NonNull;

public class EjemploSegundoPathView extends View {
    Path path = new Path();
    Paint paint = new Paint();

    public EjemploSegundoPathView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        path.addCircle(150, 150, 100, Path.Direction.CCW);
        paint.setColor(Color.MAGENTA);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(24);
        paint.setTypeface(Typeface.MONOSPACE);
        canvas.drawTextOnPath("Aqui tamos, en clase de moviles", path, 10, 40, paint);
    }
}
