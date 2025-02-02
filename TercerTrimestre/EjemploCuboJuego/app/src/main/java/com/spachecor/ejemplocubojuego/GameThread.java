package com.spachecor.ejemplocubojuego;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Clase GameThread que extiende Thread.
 * Se encarga de ejecutar el bucle principal del juego, actualizando la lógica y redibujando la pantalla.
 */
public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;

    /**
     * Constructor de GameThread.
     *
     * @param surfaceHolder SurfaceHolder para bloquear y desbloquear el canvas.
     * @param gameView      Instancia de GameView que contiene la lógica y el dibujo del juego.
     */
    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    /**
     * Método para establecer si el hilo debe seguir ejecutándose.
     *
     * @param isRunning true para continuar la ejecución, false para detenerla.
     */
    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    /**
     * Método que se ejecuta en el hilo. Realiza el bucle principal del juego.
     */
    @Override
    public void run() {
        while (running) {
            canvas = null;
            try {
                // Bloquea el canvas para dibujar
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // Actualiza la lógica del juego
                    gameView.update();
                    // Dibuja la vista actualizada en el canvas
                    gameView.draw(canvas);
                }
            } finally {
                // Libera el canvas y muestra el contenido dibujado
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
