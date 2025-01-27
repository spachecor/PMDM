package com.spachecor.ejemplocubojuego;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;

    // Variables del personaje
    private int playerX = 200; // Posición inicial X del personaje
    private int playerY = 600; // Posición inicial Y del personaje
    private int playerWidth = 100; // Ancho del personaje
    private int playerHeight = 100; // Altura del personaje
    private int velocityY = 0; // Velocidad vertical del personaje
    private final int GRAVITY = 2; // Gravedad constante
    private final int JUMP_FORCE = -30; // Fuerza del salto
    private boolean isJumping = false;

    // Plataforma (posición y tamaño de prueba)
    private int platformX = 0;
    private int platformY = 700;
    private int platformWidth = 600;
    private int platformHeight = 50;

    //Necesario para el movimiento
    private boolean isHolding = false; // Bandera para detectar si se está manteniendo presionado
    private Handler handler = new Handler();
    private Runnable moveRunnable;
    private float touchX; // Para almacenar la posición del toque
    private static final int HOLD_THRESHOLD = 200; // Tiempo para considerar un toque como prolongado (en ms)

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        platformY = getHeight() - platformHeight; // Establece el valor aquí
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Dibujar elementos en pantalla
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            // Fondo negro
            canvas.drawColor(Color.BLACK);

            // Pintar al personaje
            Paint paint = new Paint();
            paint.setColor(Color.RED); // Color del personaje
            canvas.drawRect(playerX, playerY, playerX + playerWidth, playerY + playerHeight, paint);

            // Dibujar la plataforma
            paint.setColor(Color.BLUE); // Color de la plataforma
            canvas.drawRect(platformX, platformY, platformX + platformWidth, platformY + platformHeight, paint);
        }
    }

    // Lógica del juego: actualizar la posición del personaje y la física
    public void update() {
        // Aplicar gravedad
        velocityY += GRAVITY;

        // Actualizar posición del personaje
        playerY += velocityY;

        // Detectar colisión con la plataforma
        if (playerY + playerHeight >= platformY && playerX + playerWidth > platformX && playerX < platformX + platformWidth) {
            playerY = platformY - playerHeight; // Alinear con la parte superior de la plataforma
            velocityY = 0; // Detener el movimiento hacia abajo
            isJumping = false; // Permitir saltar de nuevo
        }

        // Evitar que caiga fuera de la pantalla
        if (playerY + playerHeight > getHeight()) {
            playerY = getHeight() - playerHeight;
            velocityY = 0;
            isJumping = false;
        }
    }

    // Método para hacer que el personaje salte
    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_FORCE;
            isJumping = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked(); // Usa getActionMasked para manejar multitouch correctamente

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Toca la pantalla (primero toca)
                isHolding = false; // Reinicia la bandera
                touchX = event.getX(); // Guarda la posición del toque

                // Configura un Runnable para manejar el movimiento continuo
                moveRunnable = () -> {
                    isHolding = true; // Marca como toque prolongado
                    if (touchX < getWidth() / 2) {
                        playerX -= 10; // Mover continuamente hacia la izquierda
                    } else {
                        playerX += 10; // Mover continuamente hacia la derecha
                    }
                    handler.postDelayed(moveRunnable, 50); // Velocidad del movimiento continuo
                };

                // Retrasa el Runnable para iniciar el movimiento continuo
                handler.postDelayed(moveRunnable, HOLD_THRESHOLD); // Tiempo para diferenciar entre salto y hold
                break;

            case MotionEvent.ACTION_UP:
                // Suelta la pantalla
                handler.removeCallbacks(moveRunnable); // Detén el movimiento continuo
                if (!isHolding) {
                    jump(); // Realiza el salto si no se está manteniendo presionado
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // Permite ajustar la dirección mientras se mueve (en caso de que se deslice el dedo)
                touchX = event.getX(); // Actualiza la posición del toque
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // Detecta un segundo toque (multitáctil) mientras ya se mantiene uno
                jump(); // Realiza un salto si se detecta un toque adicional
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // Opcional: manejar si un dedo se levanta en un evento multitáctil
                break;
        }

        return true;
    }
}