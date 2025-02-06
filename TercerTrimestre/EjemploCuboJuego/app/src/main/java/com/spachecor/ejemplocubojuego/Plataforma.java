package com.spachecor.ejemplocubojuego;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * La clase {@code Plataforma} representa una plataforma en el juego.
 * <p>
 * Una plataforma puede ser de dos tipos:
 * <ul>
 *     <li>{@link PlatformType#SOLID}: La plataforma bloquea el paso desde todos los lados.</li>
 *     <li>{@link PlatformType#ONE_WAY}: La plataforma bloquea solo desde arriba.</li>
 * </ul>
 * Además, se incluye un método para determinar el lado de colisión entre la plataforma y el personaje.
 * </p>
 */
public class Plataforma {

    // Tipos de plataformas disponibles.
    public enum PlatformType {
        SOLID,    // La plataforma bloquea colisiones desde todos los lados.
        ONE_WAY   // La plataforma bloquea solo si el personaje se acerca desde arriba.
    }

    // Enum para identificar el lado de la colisión entre la plataforma y el personaje.
    public enum CollisionSide {
        NONE,   // No hay colisión.
        TOP,    // Colisión por la parte superior.
        BOTTOM, // Colisión por la parte inferior.
        LEFT,   // Colisión por el lado izquierdo.
        RIGHT   // Colisión por el lado derecho.
    }

    // Coordenadas y dimensiones de la plataforma.
    private int x, y, width, height;
    // Tipo de la plataforma (SOLID o ONE_WAY).
    private PlatformType type;

    /**
     * Constructor de la clase {@code Plataforma}.
     *
     * @param x      Coordenada X (posición horizontal) de la plataforma.
     * @param y      Coordenada Y (posición vertical) de la plataforma.
     * @param width  Ancho de la plataforma.
     * @param height Alto de la plataforma.
     * @param type   Tipo de plataforma (SOLID o ONE_WAY).
     */
    public Plataforma(int x, int y, int width, int height, PlatformType type) {
        // Inicializa las variables de la plataforma.
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /**
     * Obtiene la coordenada X de la plataforma.
     *
     * @return La coordenada X.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la coordenada Y de la plataforma.
     *
     * @return La coordenada Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Obtiene el ancho de la plataforma.
     *
     * @return El ancho.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Obtiene la altura de la plataforma.
     *
     * @return La altura.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Obtiene el tipo de la plataforma (SOLID o ONE_WAY).
     *
     * @return El tipo de plataforma.
     */
    public PlatformType getType() {
        return type;
    }

    /**
     * Retorna un objeto {@link Rect} que representa el área ocupada por la plataforma.
     * <p>
     * Este rectángulo se utiliza para la detección de colisiones.
     * </p>
     *
     * @return Un objeto {@code Rect} que abarca la plataforma.
     */
    public Rect getRect() {
        // Crea un Rect a partir de las coordenadas (x, y) y las dimensiones (width, height).
        return new Rect(x, y, x + width, y + height);
    }

    /**
     * Dibuja la plataforma en el {@link Canvas} proporcionado.
     * <p>
     * Se utiliza un color diferente según el tipo de plataforma:
     * <ul>
     *     <li>Verde para plataformas ONE_WAY.</li>
     *     <li>Azul para plataformas SOLID.</li>
     * </ul>
     * </p>
     *
     * @param canvas El {@code Canvas} en el cual se dibuja la plataforma.
     */
    public void draw(Canvas canvas) {
        // Crea un objeto Paint para definir el color.
        Paint paint = new Paint();
        // Si la plataforma es ONE_WAY, se dibuja en verde.
        if (type == PlatformType.ONE_WAY) {
            paint.setColor(Color.GREEN);
        } else {
            // Si es SOLID, se dibuja en azul.
            paint.setColor(Color.BLUE);
        }
        // Dibuja el rectángulo que representa la plataforma.
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    /**
     * Determina el lado de colisión entre el rectángulo del personaje y la plataforma.
     * <p>
     * Para plataformas ONE_WAY se considera la colisión únicamente si el personaje se aproxima desde arriba.
     * Para plataformas SOLID se determina el lado en función del menor solapamiento entre los rectángulos.
     * </p>
     *
     * @param characterRect     El rectángulo actual que ocupa el personaje.
     * @param prevCharacterRect El rectángulo que ocupaba el personaje en el frame anterior.
     * @return El lado de colisión (TOP, BOTTOM, LEFT, RIGHT) o NONE si no se detecta colisión.
     */
    public CollisionSide getCollisionSide(Rect characterRect, Rect prevCharacterRect) {
        // Si no hay intersección entre el rectángulo del personaje y el de la plataforma, no hay colisión.
        if (!Rect.intersects(characterRect, getRect())) {
            return CollisionSide.NONE;
        }

        // Se calcula el área de intersección entre ambos rectángulos.
        int intersectLeft   = Math.max(characterRect.left, x);
        int intersectTop    = Math.max(characterRect.top, y);
        int intersectRight  = Math.min(characterRect.right, x + width);
        int intersectBottom = Math.min(characterRect.bottom, y + height);
        int intersectWidth  = intersectRight - intersectLeft;
        int intersectHeight = intersectBottom - intersectTop;

        // Para plataformas ONE_WAY: se considera colisión solo si el personaje venía desde arriba.
        if (type == PlatformType.ONE_WAY) {
            if (prevCharacterRect.bottom <= y) {
                return CollisionSide.TOP;
            } else {
                // Si el personaje se acerca desde otro lado, no se produce colisión.
                return CollisionSide.NONE;
            }
        }

        // Para plataformas SOLID: se determina el lado de colisión según el menor solapamiento.
        if (intersectWidth < intersectHeight) {
            // La colisión es horizontal.
            if (prevCharacterRect.right <= x) {
                // El personaje venía desde la izquierda.
                return CollisionSide.LEFT;
            } else if (prevCharacterRect.left >= x + width) {
                // El personaje venía desde la derecha.
                return CollisionSide.RIGHT;
            }
        } else {
            // La colisión es vertical.
            if (prevCharacterRect.bottom <= y) {
                // El personaje venía desde arriba.
                return CollisionSide.TOP;
            } else if (prevCharacterRect.top >= y + height) {
                // El personaje venía desde abajo.
                return CollisionSide.BOTTOM;
            }
        }

        // Si no se cumple ninguna de las condiciones anteriores, se retorna NONE.
        return CollisionSide.NONE;
    }
}