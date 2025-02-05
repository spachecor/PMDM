package com.spachecor.ejemplocubojuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemigo {
    private Bitmap sprite;
    private int x, y;
    private int width, height;
    private int velocityX = 5; // Velocidad de movimiento horizontal

    /**
     * Constructor del enemigo.
     * @param context Contexto de la aplicación.
     * @param x Posición X inicial.
     * @param y Posición Y inicial.
     * @param width Ancho del enemigo.
     * @param height Altura del enemigo.
     * @param resourceId ID del recurso drawable del sprite del enemigo.
     */
    public Enemigo(Context context, int x, int y, int width, int height, int resourceId) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId);
        this.sprite = Bitmap.createScaledBitmap(bmp, width, height, false);
    }

    /**
     * Actualiza la posición del enemigo.
     * En este ejemplo, el enemigo se mueve horizontalmente y rebota en los bordes de la pantalla.
     * @param screenWidth Ancho de la pantalla para detectar los bordes.
     */
    public void update(int screenWidth) {
        x += velocityX;
        // Si llega al borde izquierdo o derecho, invierte la dirección
        if (x < 0 || x + width > screenWidth) {
            velocityX = -velocityX;
        }
    }

    /**
     * Dibuja el enemigo en el canvas.
     * @param canvas Objeto Canvas sobre el cual se dibuja.
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(sprite, x, y, null);
    }

    /**
     * Retorna el rectángulo (hitbox) del enemigo para detectar colisiones.
     * @return Un objeto Rect con la posición y dimensiones del enemigo.
     */
    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }
}
