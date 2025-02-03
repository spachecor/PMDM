package com.spachecor.ejemplocubojuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Clase GameView que extiende de SurfaceView e implementa SurfaceHolder.Callback.
 * Esta clase se encarga de gestionar el bucle del juego, dibujar en pantalla y
 * manejar las interacciones del usuario (toques).
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    // Arreglo de recursos para la animación del personaje "sentado"
    int[] gordiSentada = {R.drawable.gordisentada1, R.drawable.gordisentada1, R.drawable.gordisentada1, R.drawable.gordisentada2, R.drawable.gordisentada2, R.drawable.gordisentada2};

    // Arreglos de Bitmaps para las animaciones
    Bitmap[] animSentada;
    Bitmap[] animCaminandoDcha;
    Bitmap[] animCaminandoIzq;

    private Context context;
    private GameThread thread;
    private Personaje personaje;

    // Variables para el control del personaje
    private int playerX = 200; // Posición inicial X del personaje
    private int playerY = 600; // Posición inicial Y del personaje
    private int playerWidth = 100; // Ancho del personaje
    private int playerHeight = 100; // Altura del personaje
    private int velocityY = 0; // Velocidad vertical del personaje
    private final int GRAVITY = 2; // Gravedad constante aplicada al personaje
    private final int JUMP_FORCE = -30; // Fuerza del salto (valor negativo para ir hacia arriba)
    private boolean isJumping = false; // Indica si el personaje está saltando

    // Variables para el manejo del toque prolongado (para movimiento)
    private boolean isHolding = false; // Bandera para detectar si se mantiene presionado
    private Handler handler = new Handler();
    private Runnable moveRunnable;
    private float touchX; // Posición horizontal del toque
    private static final int HOLD_THRESHOLD = 200; // Tiempo (ms) para considerar un toque como prolongado

    // Lista de plataformas en la pantalla
    private ArrayList<Plataforma> plataformas;

    /**
     * Constructor de la clase GameView.
     *
     * @param context Contexto de la aplicación.
     */
    public GameView(Context context) {
        super(context);
        // Añade el callback para gestionar los cambios en el SurfaceHolder
        getHolder().addCallback(this);
        // Crea el hilo de juego
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        this.context = context;

        // Inicializa la lista de plataformas y añade algunas de ejemplo:
        plataformas = new ArrayList<>();
        // Plataforma "one way": solo bloquea si se viene desde arriba.
        plataformas.add(new Plataforma(300, 500, 200, 20, Plataforma.PlatformType.ONE_WAY));
        // Plataforma "solid": bloquea desde todos los lados.
        plataformas.add(new Plataforma(1000, 500, 200, 20, Plataforma.PlatformType.SOLID));

        // Crea el personaje pasándole la animación de "sentado" por defecto
        personaje = new Personaje(context, playerX, playerY, playerWidth, playerHeight, this.gordiSentada);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Carga los sprites para las animaciones
        this.cargarSprites();
        // Inicia el hilo del juego
        thread.setRunning(true);
        thread.start();
    }

    private void cargarSprites() {
        // Arreglos de recursos para las animaciones de caminar a la izquierda y derecha
        int[] gordiCaminandoIzq = {R.drawable.gordicaminandoizq1, R.drawable.gordicaminandoizq2, R.drawable.gordicaminandoizq3, R.drawable.gordicaminandoizq1, R.drawable.gordicaminandoizq2, R.drawable.gordicaminandoizq3};
        int[] gordiCaminandoDcha = {R.drawable.gordicaminandodcha1, R.drawable.gordicaminandodcha2, R.drawable.gordicaminandodcha3, R.drawable.gordicaminandodcha1, R.drawable.gordicaminandodcha2, R.drawable.gordicaminandodcha3};

        // Cargar animación "sentado"
        this.animSentada = new Bitmap[gordiSentada.length];
        for (int i = 0; i < gordiSentada.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), this.gordiSentada[i]);
            this.animSentada[i] = Bitmap.createScaledBitmap(bmp, playerWidth, playerHeight, false);
        }

        // Cargar animación caminando hacia la derecha
        this.animCaminandoDcha = new Bitmap[gordiCaminandoDcha.length];
        for (int i = 0; i < gordiCaminandoDcha.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), gordiCaminandoDcha[i]);
            this.animCaminandoDcha[i] = Bitmap.createScaledBitmap(bmp, 150, playerHeight, false);
        }

        // Cargar animación caminando hacia la izquierda
        this.animCaminandoIzq = new Bitmap[gordiCaminandoIzq.length];
        for (int i = 0; i < gordiCaminandoIzq.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), gordiCaminandoIzq[i]);
            this.animCaminandoIzq[i] = Bitmap.createScaledBitmap(bmp, 150, playerHeight, false);
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // No se utiliza en este ejemplo.
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

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);

            // Dibuja cada una de las plataformas
            for (Plataforma plat : plataformas) {
                plat.draw(canvas);
            }

            // Dibuja al personaje en la pantalla
            personaje.draw(canvas);
        }
    }

    public void update() {
        // Guarda la posición anterior del personaje para la detección de colisiones
        Rect prevRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);

        // Aplica la gravedad incrementando la velocidad vertical y actualizando la posición Y
        velocityY += GRAVITY;
        playerY += velocityY;

        // Actualiza la posición y animación del personaje
        personaje.setPosition(playerX, playerY);
        personaje.update();

        // Rectángulo actual del personaje (después de aplicar la gravedad)
        Rect currentRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);

        // Recorre cada plataforma para detectar y resolver colisiones
        for (Plataforma plat : plataformas) {
            Rect platRect = plat.getRect();
            if (Rect.intersects(currentRect, platRect)) {
                if (plat.getType() == Plataforma.PlatformType.ONE_WAY) {
                    // Para plataformas ONE_WAY solo se bloquea si se viene desde arriba
                    if (prevRect.bottom <= plat.getY()) {
                        playerY = plat.getY() - playerHeight;
                        velocityY = 0;
                        isJumping = false;
                        personaje.setPosition(playerX, playerY);
                    }
                } else if (plat.getType() == Plataforma.PlatformType.SOLID) {
                    // Para plataformas SOLID se calcula el solapamiento en cada dirección
                    int overlapLeft   = currentRect.right - platRect.left;
                    int overlapRight  = platRect.right - currentRect.left;
                    int overlapTop    = currentRect.bottom - platRect.top;
                    int overlapBottom = platRect.bottom - currentRect.top;

                    int minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                            Math.min(overlapTop, overlapBottom));

                    if (minOverlap == overlapTop && prevRect.bottom <= platRect.top) {
                        // Colisión por arriba: aterriza sobre la plataforma
                        playerY = plat.getY() - playerHeight;
                        velocityY = 0;
                        isJumping = false;
                        personaje.setPosition(playerX, playerY);
                    } else if (minOverlap == overlapBottom && prevRect.top >= platRect.bottom) {
                        // Colisión por abajo: choca con la parte inferior de la plataforma
                        playerY = plat.getY() + plat.getHeight();
                        if (velocityY < 0) {
                            velocityY = 0;
                        }
                        personaje.setPosition(playerX, playerY);
                    } else if (minOverlap == overlapLeft || minOverlap == overlapRight) {
                        // Colisión lateral: se evita que el personaje atraviese la plataforma
                        // Se usa la posición central para determinar desde qué lado se aproxima el personaje
                        int playerCenterX = playerX + playerWidth / 2;
                        int platCenterX = plat.getX() + plat.getWidth() / 2;
                        if (playerCenterX < platCenterX) {
                            // El personaje viene desde la izquierda, se posiciona a la izquierda de la plataforma
                            playerX = plat.getX() - playerWidth;
                        } else {
                            // El personaje viene desde la derecha, se posiciona a la derecha de la plataforma
                            playerX = plat.getX() + plat.getWidth();
                        }
                        personaje.setPosition(playerX, playerY);
                    }
                }
                // Actualiza el rectángulo actual tras los ajustes de posición
                currentRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);
            }
        }

        // Verifica colisión con el límite inferior de la pantalla
        if (playerY + playerHeight > getHeight()) {
            playerY = getHeight() - playerHeight;
            velocityY = 0;
            isJumping = false;
            personaje.setPosition(playerX, playerY);
        }
    }

    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_FORCE;
            isJumping = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked(); // Maneja correctamente el multitouch

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isHolding = false;
                touchX = event.getX();

                moveRunnable = () -> {
                    isHolding = true;
                    if (touchX < getWidth() / 2) {
                        personaje.setCurrentAnimation(animCaminandoIzq);
                        playerX -= 10;
                    } else {
                        personaje.setCurrentAnimation(animCaminandoDcha);
                        playerX += 10;
                    }
                    handler.postDelayed(moveRunnable, 50);
                };

                handler.postDelayed(moveRunnable, HOLD_THRESHOLD);
                break;

            case MotionEvent.ACTION_UP:
                personaje.setCurrentAnimation(animSentada);
                handler.removeCallbacks(moveRunnable);
                if (!isHolding) {
                    jump();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                jump();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;
        }

        return true;
    }
}
