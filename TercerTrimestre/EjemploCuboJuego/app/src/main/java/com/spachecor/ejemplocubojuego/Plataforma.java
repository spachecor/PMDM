package com.spachecor.ejemplocubojuego;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Plataforma {

    // Tipos de plataformas
    public enum PlatformType {
        SOLID,    // Colisión en todos los lados.
        ONE_WAY   // Solo bloquea desde arriba.
    }

    // Enum para identificar el lado de la colisión
    public enum CollisionSide {
        NONE, TOP, BOTTOM, LEFT, RIGHT
    }

    private int x, y, width, height;
    private PlatformType type;

    // Constructor
    public Plataforma(int x, int y, int width, int height, PlatformType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public PlatformType getType() { return type; }

    // Retorna el rectángulo que ocupa la plataforma (útil para la detección de colisiones)
    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }

    // Método para dibujar la plataforma (se usa un color distinto según el tipo)
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        if (type == PlatformType.ONE_WAY) {
            paint.setColor(Color.GREEN);
        } else {
            paint.setColor(Color.BLUE);
        }
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    /**
     * Determina el lado de colisión entre el rectángulo del personaje y la plataforma.
     *
     * Para plataformas ONE_WAY solo se considera la colisión si el personaje se aproxima desde arriba.
     * Para plataformas SOLID se determina el lado en función del menor solapamiento entre los rectángulos.
     *
     * @param characterRect El rectángulo actual del personaje.
     * @param prevCharacterRect El rectángulo del personaje en el frame anterior.
     * @return El lado de colisión (TOP, BOTTOM, LEFT, RIGHT) o NONE si no hay colisión.
     */
    public CollisionSide getCollisionSide(Rect characterRect, Rect prevCharacterRect) {
        // Si no hay intersección, no hay colisión.
        if (!Rect.intersects(characterRect, getRect())) {
            return CollisionSide.NONE;
        }

        // Calcular el área de intersección
        int intersectLeft   = Math.max(characterRect.left, x);
        int intersectTop    = Math.max(characterRect.top, y);
        int intersectRight  = Math.min(characterRect.right, x + width);
        int intersectBottom = Math.min(characterRect.bottom, y + height);
        int intersectWidth  = intersectRight - intersectLeft;
        int intersectHeight = intersectBottom - intersectTop;

        // Para plataformas ONE_WAY: solo consideramos la colisión si el personaje venía desde arriba.
        if (type == PlatformType.ONE_WAY) {
            if (prevCharacterRect.bottom <= y) {
                return CollisionSide.TOP;
            } else {
                return CollisionSide.NONE;
            }
        }

        // Para plataformas SOLID: determinamos el lado según el menor solapamiento.
        if (intersectWidth < intersectHeight) {
            // Colisión horizontal
            if (prevCharacterRect.right <= x) {
                return CollisionSide.LEFT;
            } else if (prevCharacterRect.left >= x + width) {
                return CollisionSide.RIGHT;
            }
        } else {
            // Colisión vertical
            if (prevCharacterRect.bottom <= y) {
                return CollisionSide.TOP;
            } else if (prevCharacterRect.top >= y + height) {
                return CollisionSide.BOTTOM;
            }
        }

        return CollisionSide.NONE;
    }
}
