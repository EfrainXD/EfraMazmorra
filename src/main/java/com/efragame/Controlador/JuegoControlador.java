package com.efragame.Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;

import com.efragame.SceneID;
import com.efragame.SceneManager;
import com.efragame.Modelo.*;

/**
 * Controlador principal del juego, encargado de gestionar la interfaz
 * gráfica, entrada de usuario, y la lógica de interacción con el modelo Juego.
 */
public class JuegoControlador {

    /** Contenedor principal BorderPane de la interfaz. */
    @FXML private BorderPane borderPane;

    /** Panel de desplazamiento para estadísticas del juego. */
    @FXML private ScrollPane scrollPanelStats;

    /** Panel vertical que contiene estadísticas del personaje. */
    @FXML private VBox panelStats;

    /** GridPane que muestra el mapa del juego. */
    @FXML private GridPane gridMapa;

    /** Texto que muestra la vida actual del protagonista. */
    @FXML private Text vidaProta;

    /** Área de texto para mostrar información del combate. */
    @FXML private TextArea infoCombate;

    /** Contenedor StackPane central que muestra el mapa y pantallas superpuestas. */
    @FXML private StackPane stackCentro;

    /** Instancia del modelo del juego. */
    private Juego juego;

    /** Indica si el juego ha terminado. */
    private boolean juegoTerminado = false;

    /** Estado actual del juego: "JUGANDO", "MUERTO", "VICTORIA". */
    private String estadoJuego = "JUGANDO";

    /** Panel superpuesto para mensajes de fin de juego. */
    private StackPane pantallaSuperpuesta;

    /** Tamaño base de cada celda en píxeles. */
    private double tamañoCelda = 40;

    /** Tamaño mínimo permitido para las celdas. */
    private final double TAMAÑO_MINIMO_CELDA = 20;

    /** Tamaño máximo permitido para las celdas. */
    private final double TAMAÑO_MAXIMO_CELDA = 40;

    /**
     * Inicializa el controlador tras la carga del FXML.
     * Configura el juego, listeners para redimensionamiento y eventos de teclado.
     */
    @FXML
    public void initialize() {
        try {
            juego = new Juego(
                "src/main/resources/com/efragame/files/mapa.csv",
                "src/main/resources/com/efragame/files/enemigos.csv");

            configurarRedimensionamiento();
            pantallaSuperpuesta();
            stackCentro.getChildren().add(pantallaSuperpuesta);

            dibujarMapa();
            actualizarEstadisticas();

            gridMapa.setFocusTraversable(true);
            gridMapa.setOnKeyPressed(this::manejarTecla);
            gridMapa.requestFocus();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configura listeners para ajustar el tamaño de las celdas cuando
     * el contenedor central cambia de tamaño.
     */
    private void configurarRedimensionamiento() {
        stackCentro.widthProperty().addListener((obs, oldVal, newVal) -> ajustarTamañoCeldas());
        stackCentro.heightProperty().addListener((obs, oldVal, newVal) -> ajustarTamañoCeldas());
    }

    /**
     * Calcula y ajusta el tamaño de las celdas en función del espacio disponible,
     * respetando límites mínimo y máximo.
     */
    private void ajustarTamañoCeldas() {
        if (juego == null) return;

        char[][] mapa = juego.getMapa();
        if (mapa.length == 0) return;

        double anchoDisponible = stackCentro.getWidth() - 20;
        double altoDisponible = stackCentro.getHeight() - 20;

        if (anchoDisponible <= 0 || altoDisponible <= 0) return;

        double tamañoPorAncho = anchoDisponible / mapa[0].length;
        double tamañoPorAlto = altoDisponible / mapa.length;

        double nuevoTamaño = Math.min(tamañoPorAncho, tamañoPorAlto);

        nuevoTamaño = Math.max(TAMAÑO_MINIMO_CELDA, nuevoTamaño);
        nuevoTamaño = Math.min(TAMAÑO_MAXIMO_CELDA, nuevoTamaño);

        if (Math.abs(nuevoTamaño - tamañoCelda) > 2) {
            tamañoCelda = nuevoTamaño;
            dibujarMapa();
        }
    }

    /**
     * Inicializa el panel superpuesto usado para mostrar mensajes de fin de juego.
     * Inicialmente está invisible.
     */
    private void pantallaSuperpuesta() {
        pantallaSuperpuesta = new StackPane();
        pantallaSuperpuesta.setVisible(false);
    }

    /**
     * Verifica el estado actual del juego para detectar muerte o victoria.
     * Muestra pantallas superpuestas correspondientes si se detecta fin del juego.
     */
    private void verificarEstadoJuego() {
        if (!juego.getProta().estaVivo()) {
            estadoJuego = "MUERTO";
            juegoTerminado = true;
            mostrarPantallaMuerte();
            return;
        }

        if (todosEnemigosEliminados()) {
            estadoJuego = "VICTORIA";
            juegoTerminado = true;
            mostrarPantallaVictoria();
            return;
        }
    }

    /**
     * Comprueba si todos los enemigos del juego han sido derrotados.
     * @return true si ningún enemigo está vivo, false en caso contrario.
     */
    private boolean todosEnemigosEliminados() {
        for (Enemigo e : juego.getEnemigos()) {
            if (e.estaVivo()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Muestra la pantalla superpuesta de muerte con fondo rojo y texto instructivo.
     * El diseño es responsive al tamaño del contenedor.
     */
    private void mostrarPantallaMuerte() {
        pantallaSuperpuesta.getChildren().clear();

        Rectangle fondo = new Rectangle();
        fondo.widthProperty().bind(stackCentro.widthProperty());
        fondo.heightProperty().bind(stackCentro.heightProperty());
        fondo.setFill(Color.rgb(255, 0, 0, 0.6));

        Text titulo = new Text("DERROTA\nEstas Muerto");
        titulo.setFont(Font.font("Arial", Math.max(24, stackCentro.getWidth() / 20)));
        titulo.setFill(Color.WHITE);
        titulo.setTextAlignment(TextAlignment.CENTER);

        Text instrucciones = new Text("Presiona R para reiniciar\nPresiona ESC para volver al menú");
        instrucciones.setFont(Font.font("Arial", Math.max(14, stackCentro.getWidth() / 40)));
        instrucciones.setFill(Color.WHITE);
        instrucciones.setTranslateY(-30);
        instrucciones.setTextAlignment(TextAlignment.CENTER);

        pantallaSuperpuesta.getChildren().addAll(fondo, titulo, instrucciones);

        StackPane.setAlignment(titulo, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(instrucciones, javafx.geometry.Pos.BOTTOM_CENTER);

        pantallaSuperpuesta.setVisible(true);
    }

    /**
     * Muestra la pantalla superpuesta de victoria con fondo verde y texto instructivo.
     * El diseño es responsive al tamaño del contenedor.
     */
    private void mostrarPantallaVictoria() {
        pantallaSuperpuesta.getChildren().clear();

        Rectangle fondo = new Rectangle();
        fondo.widthProperty().bind(stackCentro.widthProperty());
        fondo.heightProperty().bind(stackCentro.heightProperty());
        fondo.setFill(Color.rgb(0, 255, 0, 0.8));

        Text titulo = new Text("VICTORIA");
        titulo.setFont(Font.font("Arial", Math.max(32, stackCentro.getWidth() / 15)));
        titulo.setFill(Color.WHITE);
        titulo.setTextAlignment(TextAlignment.CENTER);

        Text subtitulo = new Text("Todos los enemigos han sido derrotados");
        subtitulo.setFont(Font.font("Arial", Math.max(16, stackCentro.getWidth() / 30)));
        subtitulo.setFill(Color.WHITE);
        subtitulo.setTextAlignment(TextAlignment.CENTER);

        Text instrucciones = new Text("Presiona R para jugar de nuevo\nPresiona ESC para volver al menú");
        instrucciones.setFont(Font.font("Arial", Math.max(12, stackCentro.getWidth() / 50)));
        instrucciones.setFill(Color.WHITE);
        instrucciones.setTranslateY(-30);
        instrucciones.setTextAlignment(TextAlignment.CENTER);

        pantallaSuperpuesta.getChildren().addAll(fondo, titulo, subtitulo, instrucciones);

        StackPane.setAlignment(titulo, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setAlignment(subtitulo, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(instrucciones, javafx.geometry.Pos.BOTTOM_CENTER);

        pantallaSuperpuesta.setVisible(true);
    }

    /**
     * Reinicia el juego recreando la instancia de Juego, restableciendo el estado,
     * ocultando la pantalla superpuesta, redibujando el mapa, actualizando estadísticas
     * y limpiando la información de combate.
     */
    private void reiniciarJuego() {
        try {
            // Recrear el juego
            juego = new Juego(
                "src/main/resources/com/efragame/files/mapa.csv",
                "src/main/resources/com/efragame/files/enemigos.csv");
            
            // Restablecer estado
            juegoTerminado = false;
            estadoJuego = "JUGANDO";
            
            // Ocultar pantalla superpuesta
            pantallaSuperpuesta.setVisible(false);
            
            // Redibujar mapa y actualizar estadísticas
            dibujarMapa();
            actualizarEstadisticas();
            
            // Limpiar información de combate
            infoCombate.clear();

            gridMapa.requestFocus();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dibuja el mapa del juego en el GridPane, mostrando suelos, paredes,
     * protagonista y enemigos vivos con sus respectivas imágenes.
     */
    private void dibujarMapa() {
        gridMapa.getChildren().clear();
        char[][] mapa = juego.getMapa();
        Image imgSuelo = new Image(new File("src/main/resources/com/efragame/images/suelo1.png").toURI().toString());
        Image imgPared = new Image(new File("src/main/resources/com/efragame/images/pared1.png").toURI().toString());
        Image imgProta = new Image(new File("src/main/resources/com/efragame/images/protagonista.png").toURI().toString());
        Image imgEsbirro = new Image(new File("src/main/resources/com/efragame/images/esbirro.png").toURI().toString());
        Image imgEsqueleto = new Image(new File("src/main/resources/com/efragame/images/esqueleto.png").toURI().toString());
        Image imgZombie = new Image(new File("src/main/resources/com/efragame/images/zombie.png").toURI().toString());

        Image imgMaldicion = new Image(new File("src/main/resources/com/efragame/images/maldicion.png").toURI().toString());

        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                Image imgFondo;
                if (mapa[y][x] == '#') {
                    imgFondo = imgPared;
                } else if (mapa[y][x] == 'M') {
                    imgFondo = imgMaldicion;
                } else {
                    imgFondo = imgSuelo;
                }
                
                ImageView imgView = new ImageView(imgFondo);
                imgView.setFitWidth(tamañoCelda);
                imgView.setFitHeight(tamañoCelda);
                imgView.setPreserveRatio(false);
                gridMapa.add(imgView, x, y);
            }
        }

        // Solo dibujar protagonista si está vivo
        if (juego.getProta().estaVivo()) {
            ImageView protaView = new ImageView(imgProta);
            protaView.setFitWidth(tamañoCelda);
            protaView.setFitHeight(tamañoCelda);
            protaView.setPreserveRatio(false);
            gridMapa.add(protaView, juego.getProta().getX(), juego.getProta().getY());
        }

        // Enemigos
        for (Enemigo e : juego.getEnemigos()) {
            if (!e.estaVivo()) continue;
            Image img = null;
            EnemigoTipo tipo = e.getTipo();
            switch (tipo) {
                case ESBIRRO: 
                    img = imgEsbirro;
                    break;
                case ESQUELETO: 
                    img = imgEsqueleto;
                    break;
                case ZOMBIE:
                    img = imgZombie;
                    break;
                default:
                    System.out.println("Enemigo no encontrado");
                    break;
            }
            ImageView enemigoView = new ImageView(img);
            enemigoView.setFitWidth(tamañoCelda);
            enemigoView.setFitHeight(tamañoCelda);
            enemigoView.setPreserveRatio(false);
            gridMapa.add(enemigoView, e.getX(), e.getY());
        }
        
        gridMapa.requestFocus();
    }

    /**
     * Intenta mover al protagonista en la dirección indicada (dx, dy)
     * solo si el juego no ha terminado y el protagonista está vivo.
     * Maneja colisiones con muros y enemigos, inicia combate si hay un enemigo,
     * mueve enemigos si el protagonista sigue vivo y actualiza la vista y estadísticas.
     * 
     * @param dx desplazamiento horizontal (-1 izquierda, 1 derecha, 0 sin cambio)
     * @param dy desplazamiento vertical (-1 arriba, 1 abajo, 0 sin cambio)
     */
    private void moverProta(int dx, int dy) {
        if (juegoTerminado || !juego.getProta().estaVivo()) {
            return;
        }

        Prota p = juego.getProta();
        int nuevoX = p.getX() + dx;
        int nuevoY = p.getY() + dy;

        if (juego.esMuro(nuevoX, nuevoY)) {
            System.out.println("Hay un muro");
            return;
        }

        Enemigo enemigo = juego.obtenerEnemigoEn(nuevoX, nuevoY);
        if (enemigo != null) {
            System.out.println("Encontraste un enemigo");
            infoCombate.clear();
            combatir(p, enemigo);
            actualizarEstadisticas();
        } else {
            System.out.println("Moviendo de (" + p.getX() + "," + p.getY() + ") a (" + nuevoX + "," + nuevoY + ")");
            p.setPosicion(nuevoX, nuevoY);
            infoCombate.clear();
            
            // Verificar si pisó una casilla maldita
            if (juego.esMaldicion(nuevoX, nuevoY)) {
                aplicarMaldicionAleatoria();
            }
        }

        if (juego.esMaldicion(nuevoX, nuevoY)) {
            moverEnemigosConPercepcion();
        }

        dibujarMapa();
        actualizarEstadisticas();
        verificarEstadoJuego();
    }

    /**
     * Mueve los enemigos que están vivos y se encuentran en rango de percepción
     * hacia el protagonista. Los que no detectan al protagonista se mueven aleatoriamente.
     */
    private void moverEnemigosConPercepcion() {
        Prota p = juego.getProta();

        for (Enemigo e : juego.getEnemigos()) {
            if (!e.estaVivo()) continue;

            if (e.enRangoDe(p)) {
                // Enemigo detecta al prota y se mueve hacia él
                int dx = Integer.compare(p.getX(), e.getX());
                int dy = Integer.compare(p.getY(), e.getY());

                int nuevoX = e.getX() + dx;
                int nuevoY = e.getY() + dy;

                if (!juego.esMuro(nuevoX, nuevoY) && juego.obtenerEnemigoEn(nuevoX, nuevoY) == null
                    && !(nuevoX == p.getX() && nuevoY == p.getY())) {
                    e.setPosicion(nuevoX, nuevoY);
                }
            } else {
                // Movimiento aleatorio si no detecta al prota
                moverEnemigoAleatorio(e);
            }
        }
    }

    /**
     * Mueve un enemigo a una posición adyacente aleatoria válida (no muro, ni ocupado).
     * 
     * @param e enemigo que se va a mover aleatoriamente
     */
    private void moverEnemigoAleatorio(Enemigo e) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        int dir = (int)(Math.random() * 4);
        int nuevoX = e.getX() + dx[dir];
        int nuevoY = e.getY() + dy[dir];

        if (!juego.esMuro(nuevoX, nuevoY) && juego.obtenerEnemigoEn(nuevoX, nuevoY) == null) {
            e.setPosicion(nuevoX, nuevoY);
        }
    }

    /**
     * Realiza un combate entre el protagonista y un enemigo.
     * El protagonista ataca primero y luego el enemigo si sigue vivo.
     * Actualiza el área de texto de información de combate con el resultado.
     * 
     * @param p protagonista que ataca
     * @param e enemigo atacado
     */
    private void combatir(Prota p, Enemigo e) {
        StringBuilder mensaje = new StringBuilder();

        int danioAlEnemigo = Math.max(0, p.getEstadisticas().getFuerza() - 5);
        e.setSalud(e.getSalud() - danioAlEnemigo);
        mensaje.append("Atacaste al ").append(e.getTipo()).append(" e hiciste ").append(danioAlEnemigo).append(" de daño.\n");

        if (e.estaVivo()) {
            int danioAlProta = Math.max(0, e.getTipo().getAtaque() - p.getEstadisticas().getDefensa());
            p.getEstadisticas().setSalud(p.getEstadisticas().getSalud() - danioAlProta);
            mensaje.append("El ").append(e.getTipo()).append(" te atacó e hizo ").append(danioAlProta).append(" de daño.\n");
        } else {
            mensaje.append("¡Derrotaste al ").append(e.getTipo()).append("!\n");
        }

        infoCombate.appendText(mensaje.toString());
    }

    /**
     * Actualiza el texto que muestra la vida del protagonista
     * y refresca el panel de estadísticas para reflejar los valores actuales.
     */
    private void actualizarEstadisticas() {
        vidaProta.setText("Vida: " + juego.getProta().getEstadisticas().getSalud());
        actualizarPanelStats();
    }

    /**
     * Actualiza el panel de estadísticas mostrando la información del protagonista
     * y los enemigos vivos actualmente en el juego.
     * Limpia el panel y luego agrega los elementos actualizados.
     */
    private void actualizarPanelStats() {
        panelStats.getChildren().clear();
        
        // Información del protagonista
        if (juego.getProta().estaVivo()) {
            VBox protaBox = crearPanelPersonaje(
                "PROTAGONISTA",
                "src/main/resources/com/efragame/images/protagonista.png",
                juego.getProta().getEstadisticas().getSalud(),
                juego.getProta().getEstadisticas().getFuerza(),
                juego.getProta().getEstadisticas().getDefensa(),
                Color.LIGHTGREEN
            );
            panelStats.getChildren().add(protaBox);
        }
        
        // Información de enemigos vivos
        Text tituloEnemigos = new Text("ENEMIGOS:");
        tituloEnemigos.setFill(Color.RED);
        tituloEnemigos.setFont(Font.font("Arial", 14));
        panelStats.getChildren().add(tituloEnemigos);
        
        for (Enemigo e : juego.getEnemigos()) {
            if (e.estaVivo()) {
                String imagePath = obtenerImagenEnemigo(e.getTipo());
                VBox enemigoBox = crearPanelPersonaje(
                    e.getTipo().toString(),
                    imagePath,
                    e.getSalud(),
                    e.getTipo().getAtaque(),
                    0, // Los enemigos no tienen defensa visible
                    Color.LIGHTCORAL
                );
                panelStats.getChildren().add(enemigoBox);
            }
        }
    }

    /**
     * Obtiene la ruta de la imagen correspondiente a un tipo de enemigo.
     *
     * @param tipo El tipo de enemigo (ESBIRRO, ESQUELETO, ZOMBIE).
     * @return La ruta en formato String hacia la imagen asociada al tipo de enemigo.
     */
    private String obtenerImagenEnemigo(EnemigoTipo tipo) {
        switch (tipo) {
            case ESBIRRO: 
                return "src/main/resources/com/efragame/images/esbirro.png";
            case ESQUELETO: 
                return "src/main/resources/com/efragame/images/esqueleto.png";
            case ZOMBIE:
                return "src/main/resources/com/efragame/images/zombie.png";
            default:
                return "src/main/resources/com/efragame/images/esbirro.png";
        }
    }

    /**
     * Crea un panel visual (VBox) que muestra la imagen y estadísticas
     * de un personaje (protagonista o enemigo).
     *
     * @param nombre Nombre del personaje que se mostrará.
     * @param imagePath Ruta del archivo de imagen del personaje.
     * @param vida Valor actual de la vida del personaje.
     * @param ataque Valor del ataque del personaje.
     * @param defensa Valor de defensa del personaje; si es 0, no se muestra.
     * @param colorFondo Color de fondo para el panel.
     * @return Un VBox configurado con la imagen y las estadísticas del personaje.
     */
    private VBox crearPanelPersonaje(String nombre, String imagePath, int vida, int ataque, int defensa, Color colorFondo) {
        VBox panel = new VBox(3);
        panel.setStyle("-fx-background-color: rgba(" + 
            (int)(colorFondo.getRed() * 255) + "," + 
            (int)(colorFondo.getGreen() * 255) + "," + 
            (int)(colorFondo.getBlue() * 255) + ",0.2); " +
            "-fx-border-color: " + toHexString(colorFondo) + "; " +
            "-fx-border-width: 1; -fx-padding: 5;");
        
        HBox contenido = new HBox(8);
        contenido.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        try {
            Image img = new Image(new File(imagePath).toURI().toString());
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(40);
            imgView.setFitHeight(40);
            imgView.setPreserveRatio(true);
            contenido.getChildren().add(imgView);
        } catch (Exception e) {
            Rectangle rect = new Rectangle(40, 40, colorFondo);
            contenido.getChildren().add(rect);
        }
        
        VBox stats = new VBox(2);
        
        Text nombreText = new Text(nombre);
        nombreText.setFill(Color.WHITE);
        nombreText.setFont(Font.font("Arial", 12));
        
        Text vidaText = new Text("Vida: " + vida);
        vidaText.setFill(Color.LIGHTGREEN);
        vidaText.setFont(Font.font("Arial", 10));
        
        Text ataqueText = new Text("Ataque: " + ataque);
        ataqueText.setFill(Color.ORANGE);
        ataqueText.setFont(Font.font("Arial", 10));
        
        stats.getChildren().addAll(nombreText, vidaText, ataqueText);
        
        if (defensa > 0) {
            Text defensaText = new Text("Defensa: " + defensa);
            defensaText.setFill(Color.LIGHTBLUE);
            defensaText.setFont(Font.font("Arial", 10));
            stats.getChildren().add(defensaText);
        }
        
        contenido.getChildren().add(stats);
        panel.getChildren().add(contenido);
        
        return panel;
    }

    /**
     * Convierte un objeto Color de JavaFX a su representación hexadecimal en String.
     *
     * @param color El color a convertir.
     * @return La cadena en formato hexadecimal correspondiente al color (e.g. "#FF00FF").
     */
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }

    /**
     * Maneja la entrada de teclado para controlar el juego, incluyendo
     * movimiento del protagonista y acciones especiales cuando el juego termina.
     *
     * @param event Evento de tecla presionada (KeyEvent).
     */
    @FXML
    public void manejarTecla(KeyEvent event) {
        System.out.println("Tecla presionada: " + event.getCode());
        
        if (juegoTerminado) {
            switch (event.getCode()) {
                case R:
                    reiniciarJuego();
                    break;
                case ESCAPE:
                    SceneManager.getInstance().switchTo(SceneID.PORTADA);
                    break;
                default:
                    break;
            }
            event.consume();
            return;
        }
        
        switch (event.getCode()) {
            case UP:
            case W: 
                moverProta(0, -1);
                break;
            case LEFT:
            case A: 
                moverProta(-1, 0);
                break;
            case DOWN:
            case S: 
                moverProta(0, 1);
                break;
            case RIGHT:
            case D: 
                moverProta(1, 0);
                break;
            case ESCAPE:
                SceneManager.getInstance().switchTo(SceneID.PORTADA);
                break;
            default:
                break;  
        }
        event.consume();
    }

    private void aplicarMaldicionAleatoria() {
        juego.aplicarMaldicionAleatoria();
        infoCombate.appendText("MALDICION Un personaje ha sido maldecido y perdió el 25% de su vida máxima.\n");
    }
}
