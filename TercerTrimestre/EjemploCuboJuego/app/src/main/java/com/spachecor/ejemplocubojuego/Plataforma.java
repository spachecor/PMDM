package com.spachecor.ejemplocubojuego;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Clase Plataforma que representa una plataforma en la pantalla.
 * Se utiliza para la detección de colisiones y para dibujar la plataforma.
 */
public class Plataforma {

    private int x, y, width, height;

    /**
     * Constructor de la clase Plataforma.
     *
     * @param x      Posición X de la plataforma.
     * @param y      Posición Y de la plataforma.
     * @param width  Ancho de la plataforma.
     * @param height Altura de la plataforma.
     */
    public Plataforma(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Métodos getters para acceder a las propiedades de la plataforma
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /**
     * Retorna el rectángulo que ocupa la plataforma.
     * Útil para la detección de colisiones.
     *
     * @return Rectángulo de la plataforma.
     */
    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }

    /**
     * Dibuja la plataforma en el canvas.
     *
     * @param canvas Canvas sobre el cual se dibuja la plataforma.
     */
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    /**
     * Verifica si hay colisión entre el rectángulo del personaje y la plataforma.
     *
     * @param characterRect     Rectángulo actual del personaje.
     * @param prevCharacterRect Rectángulo del personaje en el frame anterior.
     * @return true si se produce una colisión (el personaje cae sobre la plataforma), false en caso contrario.
     */
    public boolean checkCollision(Rect characterRect, Rect prevCharacterRect) {
        // Si no hay intersección entre el rectángulo del personaje y la plataforma, no hay colisión.
        if (!Rect.intersects(characterRect, getRect())) {
            return false;
        }
        // Verifica que en el frame anterior el personaje estaba por encima de la plataforma.
        if (prevCharacterRect.bottom <= this.y) {
            return true;
        } else {
            // Si el personaje viene desde abajo o lateralmente, se permite pasar.
            return false;
        }
    }
}
