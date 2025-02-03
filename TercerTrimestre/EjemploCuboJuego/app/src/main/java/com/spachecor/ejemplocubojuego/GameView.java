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
 * <p>
 * Esta clase se encarga de gestionar el bucle principal del juego, dibujar en pantalla
 * y manejar las interacciones del usuario (toques). Además, se ocupa de la carga de sprites,
 * la actualización del estado del juego (por ejemplo, la posición y animación del personaje)
 * y la detección y resolución de colisiones con las plataformas.
 * </p>
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    // Arreglo de recursos para la animación del personaje "sentado".
    // Estos IDs de drawable se utilizarán para crear la animación cuando el personaje esté inactivo.
    int[] gordiSentada = {R.drawable.gordisentada1, R.drawable.gordisentada1, R.drawable.gordisentada1, R.drawable.gordisentada2, R.drawable.gordisentada2, R.drawable.gordisentada2};

    // Arreglos de Bitmaps para las animaciones
    Bitmap[] animSentada;          // Animación "sentado" (inactivo)
    Bitmap[] animCaminandoDcha;    // Animación al caminar hacia la derecha
    Bitmap[] animCaminandoIzq;     // Animación al caminar hacia la izquierda

    private Context context;     // Contexto de la aplicación
    private GameThread thread;   // Hilo principal del juego que se encarga del bucle de actualización y dibujo
    private Personaje personaje; // Objeto que representa al personaje (jugador)

    // Variables para el control del personaje
    private int playerX = 200;       // Posición X inicial del personaje
    private int playerY = 600;       // Posición Y inicial del personaje
    private int playerWidth = 100;   // Ancho del hitbox del personaje (usado para la detección de colisiones)
    private int playerHeight = 100;  // Altura del hitbox del personaje
    private int velocityY = 0;       // Velocidad vertical del personaje
    private final int GRAVITY = 2;   // Constante de gravedad que se aplica en cada actualización
    private final int JUMP_FORCE = -30; // Fuerza del salto (valor negativo para desplazar hacia arriba)
    private boolean isJumping = false;  // Bandera para indicar si el personaje está en estado de salto

    // Variables para el manejo del toque prolongado (para movimiento lateral)
    private boolean isHolding = false;  // Bandera que indica si el usuario mantiene presionado el toque
    private Handler handler = new Handler(); // Handler para programar tareas con retardo
    private Runnable moveRunnable;       // Runnable que se ejecuta periódicamente para mover al personaje
    private float touchX;                // Posición X del toque detectado
    private static final int HOLD_THRESHOLD = 200; // Tiempo (en ms) para considerar un toque como prolongado

    // Lista de plataformas presentes en la pantalla
    private ArrayList<Plataforma> plataformas;

    /**
     * Constructor de la clase GameView.
     *
     * @param context Contexto de la aplicación.
     */
    public GameView(Context context) {
        super(context); // Llama al constructor de SurfaceView
        // Añade el callback para gestionar los cambios en el SurfaceHolder (creación, cambios y destrucción de la superficie)
        getHolder().addCallback(this);
        // Crea el hilo del juego y lo asocia a este SurfaceView
        thread = new GameThread(getHolder(), this);
        // Permite que esta vista reciba eventos de toque
        setFocusable(true);
        this.context = context;

        // Inicializa la lista de plataformas y añade algunas de ejemplo:
        plataformas = new ArrayList<>();
        // Se añade una plataforma sólida ubicada en (300, 600) con ancho 200 y alto 20.
        plataformas.add(new Plataforma(300, 600, 200, 20, Plataforma.PlatformType.SOLID));
        // Se añade otra plataforma sólida ubicada en (1000, 500) con ancho 200 y alto 20.
        plataformas.add(new Plataforma(1000, 500, 200, 20, Plataforma.PlatformType.SOLID));

        // Crea el personaje, asignándole la animación "sentado" por defecto y las posiciones/tamaños iniciales.
        personaje = new Personaje(context, playerX, playerY, playerWidth, playerHeight, this.gordiSentada);
    }

    /**
     * Método invocado cuando se crea la superficie (Surface).
     * Se cargan los sprites y se inicia el hilo del juego.
     *
     * @param holder El SurfaceHolder asociado a esta vista.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Carga los sprites para las diferentes animaciones del personaje.
        this.cargarSprites();
        // Configura el hilo del juego para que se ejecute.
        thread.setRunning(true);
        // Inicia el hilo del juego.
        thread.start();
    }

    /**
     * Carga los sprites para las animaciones del personaje (sentado, caminando a la derecha e izquierda).
     * Se crean arreglos de Bitmaps redimensionados al tamaño deseado.
     */
    private void cargarSprites() {
        // Arreglos de recursos (IDs de drawable) para las animaciones al caminar.
        int[] gordiCaminandoIzq = {R.drawable.gordicaminandoizq1, R.drawable.gordicaminandoizq2, R.drawable.gordicaminandoizq3, R.drawable.gordicaminandoizq1, R.drawable.gordicaminandoizq2, R.drawable.gordicaminandoizq3};
        int[] gordiCaminandoDcha = {R.drawable.gordicaminandodcha1, R.drawable.gordicaminandodcha2, R.drawable.gordicaminandodcha3, R.drawable.gordicaminandodcha1, R.drawable.gordicaminandodcha2, R.drawable.gordicaminandodcha3};

        // Cargar animación "sentado":
        // Se crean los Bitmaps y se escalan al tamaño del hitbox (playerWidth x playerHeight).
        this.animSentada = new Bitmap[gordiSentada.length];
        for (int i = 0; i < gordiSentada.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), this.gordiSentada[i]);
            this.animSentada[i] = Bitmap.createScaledBitmap(bmp, playerWidth, playerHeight, false);
        }

        // Cargar animación caminando hacia la derecha:
        // Los sprites se escalan a 150 de ancho y se mantienen con la altura del hitbox.
        this.animCaminandoDcha = new Bitmap[gordiCaminandoDcha.length];
        for (int i = 0; i < gordiCaminandoDcha.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), gordiCaminandoDcha[i]);
            this.animCaminandoDcha[i] = Bitmap.createScaledBitmap(bmp, 150, playerHeight, false);
        }

        // Cargar animación caminando hacia la izquierda:
        // Se realiza el mismo procedimiento que para la animación de la derecha.
        this.animCaminandoIzq = new Bitmap[gordiCaminandoIzq.length];
        for (int i = 0; i < gordiCaminandoIzq.length; i++) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), gordiCaminandoIzq[i]);
            this.animCaminandoIzq[i] = Bitmap.createScaledBitmap(bmp, 150, playerHeight, false);
        }
    }

    /**
     * Método invocado cuando la superficie cambia de tamaño o formato.
     * En este ejemplo no se utiliza.
     *
     * @param surfaceHolder El SurfaceHolder asociado a esta vista.
     * @param i             Nuevo ancho de la superficie.
     * @param i1            Nuevo alto de la superficie.
     * @param i2            Nuevo formato de la superficie.
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // No se implementa ninguna acción en este ejemplo.
    }

    /**
     * Método invocado cuando la superficie es destruida.
     * Se detiene el hilo del juego de forma segura.
     *
     * @param holder El SurfaceHolder asociado a esta vista.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // Se intenta detener el hilo del juego hasta que se detenga correctamente.
        while (retry) {
            try {
                // Se indica al hilo que deje de correr.
                thread.setRunning(false);
                // Se espera a que el hilo termine su ejecución.
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dibuja en el canvas todos los elementos del juego: fondo, plataformas y personaje.
     *
     * @param canvas Objeto Canvas sobre el cual se realiza el dibujo.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            // Se pinta el fondo con color blanco.
            canvas.drawColor(Color.WHITE);

            // Se recorren todas las plataformas y se dibujan.
            for (Plataforma plat : plataformas) {
                plat.draw(canvas);
            }

            // Se dibuja el personaje en su posición actual.
            personaje.draw(canvas);
        }
    }

    /**
     * Actualiza el estado del juego en cada ciclo del bucle principal.
     * Se actualizan la posición del personaje, la aplicación de la gravedad y se gestionan colisiones.
     */
    public void update() {
        // Se guarda la posición anterior del personaje (hitbox) para detectar colisiones.
        Rect prevRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);

        // Se aplica la gravedad incrementando la velocidad vertical y actualizando la posición Y.
        velocityY += GRAVITY;
        playerY += velocityY;

        // Se actualiza la posición y la animación del personaje.
        personaje.setPosition(playerX, playerY);
        personaje.update();

        // Se crea el rectángulo que representa la posición actual del personaje después de aplicar la gravedad.
        Rect currentRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);

        // Se recorren todas las plataformas para detectar colisiones y resolverlas.
        for (Plataforma plat : plataformas) {
            // Se obtiene el rectángulo de la plataforma.
            Rect platRect = plat.getRect();
            // Si el rectángulo del personaje interseca al de la plataforma...
            if (Rect.intersects(currentRect, platRect)) {
                if (plat.getType() == Plataforma.PlatformType.ONE_WAY) {
                    // Para plataformas ONE_WAY solo se bloquea la colisión si el personaje viene desde arriba.
                    if (prevRect.bottom <= plat.getY()) {
                        // Se posiciona al personaje justo encima de la plataforma.
                        playerY = plat.getY() - playerHeight;
                        velocityY = 0;
                        isJumping = false;
                        personaje.setPosition(playerX, playerY);
                    }
                } else if (plat.getType() == Plataforma.PlatformType.SOLID) {
                    // Para plataformas SOLID se calcula el solapamiento en cada dirección:
                    int overlapLeft   = currentRect.right - platRect.left;
                    int overlapRight  = platRect.right - currentRect.left;
                    int overlapTop    = currentRect.bottom - platRect.top;
                    int overlapBottom = platRect.bottom - currentRect.top;

                    // Se determina el menor solapamiento para identificar el lado de colisión.
                    int minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                            Math.min(overlapTop, overlapBottom));

                    if (minOverlap == overlapTop && prevRect.bottom <= platRect.top) {
                        // Si el menor solapamiento es por la parte superior y el personaje venía desde arriba,
                        // se posiciona justo sobre la plataforma.
                        playerY = plat.getY() - playerHeight;
                        velocityY = 0;
                        isJumping = false;
                        personaje.setPosition(playerX, playerY);
                    } else if (minOverlap == overlapBottom && prevRect.top >= platRect.bottom) {
                        // Si el menor solapamiento es por la parte inferior y el personaje venía desde abajo,
                        // se posiciona justo debajo de la plataforma.
                        playerY = plat.getY() + plat.getHeight();
                        if (velocityY < 0) {
                            velocityY = 0;
                        }
                        personaje.setPosition(playerX, playerY);
                    } else if (minOverlap == overlapLeft || minOverlap == overlapRight) {
                        // Para colisiones laterales, se evita que el personaje atraviese la plataforma.
                        // Se utiliza la posición central para determinar desde qué lado se aproxima el personaje.
                        int playerCenterX = playerX + playerWidth / 2;
                        int platCenterX = plat.getX() + plat.getWidth() / 2;
                        if (playerCenterX < platCenterX) {
                            // Si el personaje viene de la izquierda, se coloca a la izquierda de la plataforma.
                            playerX = plat.getX() - playerWidth;
                        } else {
                            // Si viene de la derecha, se coloca a la derecha de la plataforma.
                            playerX = plat.getX() + plat.getWidth();
                        }
                        personaje.setPosition(playerX, playerY);
                    }
                }
                // Se actualiza el rectángulo del personaje tras cualquier ajuste de posición.
                currentRect = new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight);
            }
        }

        // Verifica que el personaje no se salga por la parte inferior de la pantalla.
        if (playerY + playerHeight > getHeight()) {
            playerY = getHeight() - playerHeight;
            velocityY = 0;
            isJumping = false;
            personaje.setPosition(playerX, playerY);
        }
    }

    /**
     * Inicia el salto del personaje.
     * Si el personaje no está saltando, se aplica la fuerza de salto (JUMP_FORCE)
     * y se marca como en estado de salto.
     */
    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_FORCE;
            isJumping = true;
        }
    }

    /**
     * Maneja los eventos de toque en la pantalla.
     * <p>
     * Se detectan toques simples (que desencadenan un salto) y toques prolongados (que mueven al personaje lateralmente).
     * Si el toque se realiza en la mitad izquierda de la pantalla, se activa la animación y movimiento hacia la izquierda;
     * si es en la mitad derecha, se hace lo contrario.
     * </p>
     *
     * @param event Evento de toque recibido.
     * @return true si el evento ha sido consumido.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Se obtiene la acción del evento de toque, manejando multitouch correctamente.
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // El usuario inicia un toque.
                isHolding = false;
                touchX = event.getX();

                // Se crea un Runnable que se ejecuta tras un retardo (HOLD_THRESHOLD) para detectar un toque prolongado.
                moveRunnable = () -> {
                    isHolding = true;
                    // Si el toque ocurre en la mitad izquierda de la pantalla...
                    if (touchX < getWidth() / 2) {
                        // Se cambia la animación a la de caminar a la izquierda y se mueve el personaje hacia la izquierda.
                        personaje.setCurrentAnimation(animCaminandoIzq);
                        playerX -= 10;
                    } else {
                        // Si el toque es en la mitad derecha, se cambia la animación a la de caminar a la derecha y se mueve el personaje.
                        personaje.setCurrentAnimation(animCaminandoDcha);
                        playerX += 10;
                    }
                    // Se vuelve a programar la ejecución del Runnable cada 50 ms para mantener el movimiento.
                    handler.postDelayed(moveRunnable, 50);
                };

                // Se programa el Runnable para que se ejecute tras HOLD_THRESHOLD milisegundos.
                handler.postDelayed(moveRunnable, HOLD_THRESHOLD);
                break;

            case MotionEvent.ACTION_UP:
                // Al levantar el dedo, se restaura la animación de "sentado".
                personaje.setCurrentAnimation(animSentada);
                // Se cancela el Runnable que mueve al personaje.
                handler.removeCallbacks(moveRunnable);
                // Si el toque no fue prolongado (toque rápido), se ejecuta un salto.
                if (!isHolding) {
                    jump();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // Si el usuario mueve el dedo, se actualiza la posición X del toque.
                touchX = event.getX();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // Si se detecta un segundo dedo, se ejecuta un salto.
                jump();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // No se realiza ninguna acción al levantar un dedo secundario.
                break;
        }

        // Se indica que el evento ha sido consumido.
        return true;
    }
}
