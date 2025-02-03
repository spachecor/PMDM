package com.spachecor.ejemplocubojuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Clase Personaje que representa al jugador.
 * Gestiona la animación, posición y el dibujo del personaje en pantalla.
 */
public class Personaje {
    private Bitmap[] frames; // Arreglo de imágenes para la animación
    private int currentFrame = 0; // Fotograma actual de la animación
    private long lastFrameChangeTime = 0; // Último tiempo en que se cambió el fotograma
    private int frameLengthInMilliseconds = 100; // Duración de cada fotograma en ms
    private Context context;

    private int x, y; // Posición (X, Y) del personaje
    private int width, height; // Tamaño (ancho y alto) del personaje

    /**
     * Constructor de la clase Personaje.
     *
     * @param context     Contexto de la aplicación.
     * @param x           Posición inicial X.
     * @param y           Posición inicial Y.
     * @param width       Ancho del personaje.
     * @param height      Altura del personaje.
     * @param resourceIds Arreglo de recursos (ID de drawable) para la animación.
     */
    public Personaje(Context context, int x, int y, int width, int height, int[] resourceIds) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.context = context;

        // Crea y escala los Bitmaps para cada fotograma de la animación
        frames = new Bitmap[resourceIds.length];
        for (int i = 0; i < resourceIds.length; i++) {
            frames[i] = BitmapFactory.decodeResource(context.getResources(), resourceIds[i]);
            frames[i] = Bitmap.createScaledBitmap(frames[i], width, height, false);
        }
    }

    /**
     * Actualiza la animación del personaje.
     * Cambia de fotograma según el tiempo transcurrido.
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        synchronized (this) {
            if (currentTime > lastFrameChangeTime + frameLengthInMilliseconds && frames != null && frames.length > 0) {
                currentFrame = (currentFrame + 1) % frames.length;
                lastFrameChangeTime = currentTime;
            }
        }
    }

    /**
     * Dibuja el personaje en el canvas.
     *
     * @param canvas Canvas sobre el cual dibujar el personaje.
     */
    public void draw(Canvas canvas) {
        synchronized (this) {
            if (frames != null && frames[currentFrame] != null) {
                canvas.drawBitmap(frames[currentFrame], x, y, null);
            }
        }
    }

    /**
     * Permite cambiar la animación actual del personaje.
     * Por ejemplo, al cambiar de estado (sentado, caminando, etc.).
     *
     * @param newFrames Arreglo de Bitmaps que representan la nueva animación.
     */
    public synchronized void setCurrentAnimation(Bitmap[] newFrames) {
        // Opcional: reiniciar el índice para comenzar la animación desde el inicio.
        // this.currentFrame = 0;
        this.frames = newFrames;
    }

    /**
     * Actualiza la posición del personaje.
     *
     * @param x Nueva posición X.
     * @param y Nueva posición Y.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Permite cambiar los recursos (fotogramas) de la animación.
     *
     * @param resourceIds Arreglo de ID de recursos para la nueva animación.
     */
    public void setFrames(int[] resourceIds) {
        this.frames = new Bitmap[resourceIds.length];
        for (int i = 0; i < resourceIds.length; i++) {
            frames[i] = BitmapFactory.decodeResource(this.context.getResources(), resourceIds[i]);
            frames[i] = Bitmap.createScaledBitmap(frames[i], width, height, false);
        }
    }

    // Métodos getters para obtener la posición y dimensiones del personaje
    public int getX() { return x; }
    public int getY() { return y; }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
