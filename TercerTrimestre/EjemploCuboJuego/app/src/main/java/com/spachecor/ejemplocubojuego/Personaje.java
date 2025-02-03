package com.spachecor.ejemplocubojuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * La clase {@code Personaje} representa al jugador en el juego.
 * <p>
 * Esta clase se encarga de gestionar la animación, la posición y el dibujo del personaje en pantalla.
 * Proporciona métodos para actualizar la animación, cambiar el estado visual (animación), y actualizar la posición.
 * </p>
 */
public class Personaje {
    // Arreglo de imágenes (frames) que conforman la animación del personaje.
    private Bitmap[] frames;
    // Índice del frame actual que se muestra.
    private int currentFrame = 0;
    // Último tiempo (en milisegundos) en que se cambió el fotograma.
    private long lastFrameChangeTime = 0;
    // Duración en milisegundos que se muestra cada fotograma.
    private int frameLengthInMilliseconds = 100;
    // Contexto de la aplicación (necesario para acceder a recursos, por ejemplo).
    private Context context;

    // Posición actual del personaje (coordenadas X e Y).
    private int x, y;
    // Dimensiones del hitbox del personaje (ancho y alto).
    private int width, height;

    /**
     * Constructor de la clase {@code Personaje}.
     *
     * @param context     Contexto de la aplicación.
     * @param x           Posición inicial en el eje X.
     * @param y           Posición inicial en el eje Y.
     * @param width       Ancho del personaje (usado para el hitbox y escalado de imágenes).
     * @param height      Altura del personaje (usado para el hitbox y escalado de imágenes).
     * @param resourceIds Arreglo de IDs de recursos (drawables) que se utilizarán para la animación.
     */
    public Personaje(Context context, int x, int y, int width, int height, int[] resourceIds) {
        // Inicializa la posición y dimensiones.
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // Guarda el contexto para poder acceder a los recursos.
        this.context = context;

        // Crea el arreglo de Bitmaps según la cantidad de recursos recibidos.
        frames = new Bitmap[resourceIds.length];
        // Recorre cada recurso para decodificar y escalar la imagen.
        for (int i = 0; i < resourceIds.length; i++) {
            // Decodifica el recurso en un Bitmap.
            frames[i] = BitmapFactory.decodeResource(context.getResources(), resourceIds[i]);
            // Escala el Bitmap al tamaño especificado (width x height).
            frames[i] = Bitmap.createScaledBitmap(frames[i], width, height, false);
        }
    }

    /**
     * Actualiza la animación del personaje.
     * <p>
     * Cambia el fotograma actual en función del tiempo transcurrido desde el último cambio.
     * </p>
     */
    public void update() {
        // Obtiene el tiempo actual en milisegundos.
        long currentTime = System.currentTimeMillis();
        // Se sincroniza el bloque para evitar conflictos en entornos multihilo.
        synchronized (this) {
            // Si ha pasado suficiente tiempo desde el último cambio de fotograma y existen frames válidos...
            if (currentTime > lastFrameChangeTime + frameLengthInMilliseconds && frames != null && frames.length > 0) {
                // Se actualiza el índice del frame actual de forma cíclica.
                currentFrame = (currentFrame + 1) % frames.length;
                // Se actualiza el tiempo del último cambio de fotograma.
                lastFrameChangeTime = currentTime;
            }
        }
    }

    /**
     * Dibuja el personaje en el {@link Canvas} proporcionado.
     * <p>
     * Se centra la imagen (sprite) sobre el hitbox del personaje.
     * Esto permite que, aunque el sprite tenga un ancho mayor (por ejemplo, 150 cuando camina),
     * se dibuje centrado respecto al hitbox definido (por ejemplo, 100).
     * </p>
     *
     * @param canvas Objeto {@code Canvas} sobre el cual se dibuja el personaje.
     */
    public void draw(Canvas canvas) {
        // Se sincroniza el bloque para evitar conflictos en entornos multihilo.
        synchronized (this) {
            // Si el arreglo de frames y el frame actual son válidos...
            if (frames != null && frames[currentFrame] != null) {
                // Se obtiene el ancho real del frame actual (puede ser mayor que el ancho del hitbox).
                int spriteWidth = frames[currentFrame].getWidth();
                // Se calcula el offset horizontal para centrar el sprite sobre el hitbox.
                // Por ejemplo, si el sprite mide 150 y el hitbox 100, offsetX será 25.
                int offsetX = (spriteWidth - width) / 2;
                // Se dibuja el bitmap desplazado (x - offsetX) para centrarlo horizontalmente.
                canvas.drawBitmap(frames[currentFrame], x - offsetX, y, null);
            }
        }
    }

    /**
     * Permite cambiar la animación actual del personaje.
     * <p>
     * Este método se utiliza, por ejemplo, para cambiar entre estados como "sentado" o "caminando".
     * </p>
     *
     * @param newFrames Arreglo de {@link Bitmap} que representan la nueva animación.
     */
    public synchronized void setCurrentAnimation(Bitmap[] newFrames) {
        // Opcional: se puede reiniciar el índice para comenzar la animación desde el inicio.
        // this.currentFrame = 0;
        this.frames = newFrames;
    }

    /**
     * Actualiza la posición del personaje.
     *
     * @param x Nueva posición en el eje X.
     * @param y Nueva posición en el eje Y.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Métodos getters para obtener la posición y dimensiones del personaje

    /**
     * Obtiene la posición X actual del personaje.
     *
     * @return La coordenada X.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la posición Y actual del personaje.
     *
     * @return La coordenada Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Establece un nuevo ancho para el personaje.
     *
     * @param width Nuevo ancho.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Obtiene el ancho actual del personaje.
     *
     * @return El ancho.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Obtiene la altura actual del personaje.
     *
     * @return La altura.
     */
    public int getHeight() {
        return height;
    }
}
