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

        // Inicializa la lista de plataformas y añade una plataforma de ejemplo
        plataformas = new ArrayList<>();
        plataformas.add(new Plataforma(300, 500, 200, 20));

        // Crea el personaje pasándole la animación de "sentado" por defecto
        personaje = new Personaje(context, playerX, playerY, playerWidth, playerHeight, this.gordiSentada);
    }

    /**
     * Método llamado cuando el Surface está creado.
     * Se encarga de cargar los sprites y de iniciar el hilo del juego.
     *
     * @param holder SurfaceHolder asociado.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Carga los sprites para las animaciones
        this.cargarSprites();
        // Inicia el hilo del juego
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Método para cargar los sprites y configurar las animaciones del personaje.
     */
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
            // Se utiliza un ancho mayor para la animación de caminar (150) para diferenciarla
            this.animCaminandoDcha[i] = Bitmap.createScaledBitmap(bmp, 150, playerHeight, false);
        }

        // Cargar animación caminando hacia la izquierda
        this.animCaminandoIzq = new Bitmap[gordiCaminandoIzq.length];
        for (int i = 0; i < gordiCaminandoIzq.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), gordiCaminandoIzq[i]);
            this.animCaminandoIzq[i] = Bitmap.createScaledBitmap(bmp, 150, playerHeight, false);
        }
    }

    /**
     * Método llamado cuando la superficie cambia de tamaño o formato.
     *
     * @param surfaceHolder El SurfaceHolder.
     * @param i             Nuevo formato.
     * @param i1            Nuevo ancho.
     * @param i2            Nueva altura.
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // No se utiliza en este ejemplo.
    }

    /**
     * Método llamado cuando se destruye la superficie.
     * Se encarga de detener el hilo del juego de forma segura.
     *
     * @param holder SurfaceHolder asociado.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // Intenta detener el hilo del juego de forma segura
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

    /**
     * Método para dibujar en el Canvas.
     *
     * @param canvas Canvas en el que se dibuja.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            // Limpia el canvas con color blanco
            canvas.drawColor(Color.WHITE);

            // Dibuja cada una de las plataformas
            for (Plataforma plat : plataformas) {
                plat.draw(canvas);
            }

            // Dibuja al personaje en la pantalla
            personaje.draw(canvas);
        }
    }

    /**
     * Método que actualiza la lógica del juego en cada frame.
     * Se encarga de la gravedad, salto, colisiones y actualiza la posición del personaje.
     */
    public void update() {
        // Guarda la posición anterior del personaje para la detección de colisiones
        Rect prevRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);

        // Aplica la gravedad incrementando la velocidad vertical y actualizando la posición Y
        velocityY += GRAVITY;
        playerY += velocityY;

        // Actualiza la posición del personaje en la instancia de Personaje
        personaje.setPosition(playerX, playerY);
        // Actualiza la animación del personaje
        personaje.update();

        // Crea el rectángulo actual del personaje para la detección de colisiones
        Rect currentRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);

        // Verifica las colisiones del personaje con cada plataforma
        for (Plataforma plat : plataformas) {
            if (plat.checkCollision(currentRect, prevRect)) {
                // Si hay colisión, se ajusta la posición del personaje para que "caiga" sobre la plataforma
                playerY = plat.getY() - playerHeight;
                velocityY = 0;
                isJumping = false;
                personaje.setPosition(playerX, playerY);
                // Se sale del bucle tras detectar la colisión
                break;
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

    /**
     * Método para hacer que el personaje salte.
     * Sólo se permite saltar si el personaje no está ya en el aire.
     */
    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_FORCE;
            isJumping = true;
        }
    }

    /**
     * Maneja los eventos táctiles en la pantalla.
     * Se distinguen los toques simples (salto) y los toques prolongados (movimiento lateral).
     *
     * @param event Evento táctil.
     * @return true si se ha procesado el evento.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked(); // Maneja correctamente el multitouch

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Cuando se toca la pantalla inicialmente
                isHolding = false; // Reinicia la bandera de toque prolongado
                touchX = event.getX(); // Guarda la posición del toque

                // Configura un Runnable para manejar el movimiento continuo en caso de toque prolongado
                moveRunnable = () -> {
                    isHolding = true; // Marca que se mantiene presionado
                    if (touchX < getWidth() / 2) {
                        // Si el toque es en la mitad izquierda, se mueve a la izquierda
                        // Se establece la animación de caminar a la izquierda
                        personaje.setCurrentAnimation(animCaminandoIzq);
                        playerX -= 10; // Mueve el personaje a la izquierda
                    } else {
                        // Si el toque es en la mitad derecha, se mueve a la derecha
                        personaje.setCurrentAnimation(animCaminandoDcha);
                        playerX += 10; // Mueve el personaje a la derecha
                    }
                    // Programa la siguiente ejecución del Runnable tras 50 ms
                    handler.postDelayed(moveRunnable, 50);
                };

                // Se programa el Runnable tras el umbral para diferenciar entre salto y movimiento continuo
                handler.postDelayed(moveRunnable, HOLD_THRESHOLD);
                break;

            case MotionEvent.ACTION_UP:
                // Cuando se suelta el toque
                // Se vuelve a la animación de "sentado"
                personaje.setCurrentAnimation(animSentada);
                // Se detiene el Runnable para el movimiento continuo
                handler.removeCallbacks(moveRunnable);
                // Si no se ha considerado como toque prolongado, se realiza un salto
                if (!isHolding) {
                    jump();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // Si se mueve el dedo, se actualiza la posición X del toque
                touchX = event.getX();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // Si se detecta un segundo toque (multitáctil), se realiza un salto adicional
                jump();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // Opcional: se puede manejar la lógica cuando se levanta un dedo en multitáctil
                break;
        }

        return true;
    }
}
