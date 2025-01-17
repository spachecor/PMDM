package com.spachecor.ejemplocanvaspaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class EjemploTerceroDibujarDedoView extends View {
    private Path path = new Path();
    private Paint paint = new Paint();
    private Bitmap bitmap;
    private Canvas canvas;
    private boolean eliminar = false;

    public EjemploTerceroDibujarDedoView(Context context) {
        super(context);
        init();
    }

    public EjemploTerceroDibujarDedoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //crear un bitmap para el lienzo
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public void setEliminar() {
        this.eliminar = !this.eliminar;
        if (this.eliminar) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            paint.setStrokeWidth(50);
        } else {
            paint.setXfermode(null);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
        }
    }

    public boolean isEliminar() {
        return eliminar;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                if (eliminar) {
                    //si está en modo de borrado, dibuja directamente en el Bitmap
                    canvas.drawPath(path, paint);
                    path.reset();
                    path.moveTo(pointX, pointY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!eliminar) {
                    //si no está en modo de borrado, dibuja en el Bitmap al soltar
                    canvas.drawPath(path, paint);
                }
                path.reset();
                break;
            default:
                return false;
        }

        invalidate(); //solicita redibujar la vista
        return true;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //dibuja el contenido del bitmap
        canvas.drawBitmap(bitmap, 0, 0, null);
        //dibuja el trazo actual solo si no está en modo de borrado
        if (!eliminar) {
            canvas.drawPath(path, paint);
        }
    }
}
