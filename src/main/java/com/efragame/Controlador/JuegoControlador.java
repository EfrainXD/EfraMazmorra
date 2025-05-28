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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.IOException;

import com.efragame.Modelo.*;
import com.efragame.SceneID;
import com.efragame.SceneManager;

public class JuegoControlador {
    @FXML private BorderPane borderPane;
    @FXML private ScrollPane scrollPanelStats;
    @FXML private VBox panelStats;
    @FXML private GridPane gridMapa;
    @FXML private Text vidaProta;
    @FXML private TextArea infoCombate;
    @FXML private StackPane stackCentro;

    private Juego juego;
    
    private boolean juegoTerminado = false;
    private String estadoJuego = "JUGANDO"; // JUGANDO, MUERTO, VICTORIA
    private StackPane pantallaSuperpuesta;
    
    // Variables para tamaño responsive
    private double tamañoCelda = 40; // Tamaño base
    private final double TAMAÑO_MINIMO_CELDA = 20;
    private final double TAMAÑO_MAXIMO_CELDA = 40;

    @FXML
    public void initialize() {
        try {
            juego = new Juego(
                "src/main/resources/com/efragame/files/mapa.csv",
                "src/main/resources/com/efragame/files/enemigos.csv");
            
            // Configurar listeners para redimensionamiento
            configurarRedimensionamiento();
            
            // Pantalla superpuesta para mensajes de fin de juego
            pantallaSuperpuesta();
            stackCentro.getChildren().add(pantallaSuperpuesta);
            
            dibujarMapa();
            actualizarEstadisticas();

            gridMapa.setFocusTraversable(true);
            gridMapa.setOnKeyPressed(this::manejarTecla);
            gridMapa.requestFocus();
            gridMapa.setFocusTraversable(true);
            gridMapa.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Configurar listeners para redimensionamiento automático
    private void configurarRedimensionamiento() {
        // Listener para cuando cambie el tamaño del StackPane
        stackCentro.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
                ajustarTamañoCeldas();
            }
        });
        
        stackCentro.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
                ajustarTamañoCeldas();
            }
        });
    }

    // Ajustar el tamaño de las celdas basado en el espacio disponible
    private void ajustarTamañoCeldas() {
        if (juego == null) return;
        
        char[][] mapa = juego.getMapa();
        if (mapa.length == 0) return;
        
        double anchoDisponible = stackCentro.getWidth() - 20; // Margen
        double altoDisponible = stackCentro.getHeight() - 20; // Margen
        
        if (anchoDisponible <= 0 || altoDisponible <= 0) return;
        
        // Calcular tamaño óptimo basado en el mapa
        double tamañoPorAncho = anchoDisponible / mapa[0].length;
        double tamañoPorAlto = altoDisponible / mapa.length;
        
        // Usar el menor para mantener proporciones
        double nuevoTamaño = Math.min(tamañoPorAncho, tamañoPorAlto);
        
        // Aplicar límites
        nuevoTamaño = Math.max(TAMAÑO_MINIMO_CELDA, nuevoTamaño);
        nuevoTamaño = Math.min(TAMAÑO_MAXIMO_CELDA, nuevoTamaño);
        
        // Solo redibujar si hay cambio significativo
        if (Math.abs(nuevoTamaño - tamañoCelda) > 2) {
            tamañoCelda = nuevoTamaño;
            dibujarMapa();
        }
    }

    // Crear pantalla superpuesta responsive
    private void pantallaSuperpuesta() {
        pantallaSuperpuesta = new StackPane();
        pantallaSuperpuesta.setVisible(false);
        // El tamaño se ajustará dinámicamente
    }

    // Verificar estado del juego
    private void verificarEstadoJuego() {
        // Verificar si el protagonista murió
        if (!juego.getProta().estaVivo()) {
            estadoJuego = "MUERTO";
            juegoTerminado = true;
            mostrarPantallaMuerte();
            return;
        }
        
        // Verificar si todos los enemigos han sido derrotados
        if (todosEnemigosEliminados()) {
            estadoJuego = "VICTORIA";
            juegoTerminado = true;
            mostrarPantallaVictoria();
            return;
        }
    }

    // Verificar si todos los enemigos están muertos
    private boolean todosEnemigosEliminados() {
        for (Enemigo e : juego.getEnemigos()) {
            if (e.estaVivo()) {
                return false;
            }
        }
        return true;
    }

    // Mostrar pantalla de muerte responsive
    private void mostrarPantallaMuerte() {
        pantallaSuperpuesta.getChildren().clear();
        
        // Fondo rojo semi-transparente que se ajusta al tamaño del contenedor
        Rectangle fondo = new Rectangle();
        fondo.widthProperty().bind(stackCentro.widthProperty());
        fondo.heightProperty().bind(stackCentro.heightProperty());
        fondo.setFill(Color.rgb(255, 0, 0, 0.6));
        
        // Crear texto principal responsive
        Text titulo = new Text("DERROTA\nEstas Muerto");
        titulo.setFont(Font.font("Arial", Math.max(24, stackCentro.getWidth() / 20)));
        titulo.setFill(Color.WHITE);
        titulo.setTextAlignment(TextAlignment.CENTER);
        
        // Crear texto de instrucciones responsive
        Text instrucciones = new Text("Presiona R para reiniciar\nPresiona ESC para volver al menú");
        instrucciones.setFont(Font.font("Arial", Math.max(14, stackCentro.getWidth() / 40)));
        instrucciones.setFill(Color.WHITE);
        instrucciones.setTranslateY(-30);
        instrucciones.setTextAlignment(TextAlignment.CENTER);
        
        // Agregar todo al StackPane
        pantallaSuperpuesta.getChildren().addAll(fondo, titulo, instrucciones);
        
        // Posicionar textos
        StackPane.setAlignment(titulo, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(instrucciones, javafx.geometry.Pos.BOTTOM_CENTER);
        
        pantallaSuperpuesta.setVisible(true);
    }

    // Mostrar pantalla de victoria responsive
    private void mostrarPantallaVictoria() {
        pantallaSuperpuesta.getChildren().clear();
        
        // Fondo verde semi-transparente responsive
        Rectangle fondo = new Rectangle();
        fondo.widthProperty().bind(stackCentro.widthProperty());
        fondo.heightProperty().bind(stackCentro.heightProperty());
        fondo.setFill(Color.rgb(0, 255, 0, 0.8));
        
        // Crear texto principal responsive
        Text titulo = new Text("VICTORIA");
        titulo.setFont(Font.font("Arial", Math.max(32, stackCentro.getWidth() / 15)));
        titulo.setFill(Color.WHITE);
        titulo.setTextAlignment(TextAlignment.CENTER);
        
        // Crear subtítulo responsive
        Text subtitulo = new Text("Todos los enemigos han sido derrotados");
        subtitulo.setFont(Font.font("Arial", Math.max(16, stackCentro.getWidth() / 30)));
        subtitulo.setFill(Color.WHITE);
        subtitulo.setTextAlignment(TextAlignment.CENTER);
        
        // Crear texto de instrucciones responsive
        Text instrucciones = new Text("Presiona R para jugar de nuevo\nPresiona ESC para volver al menú");
        instrucciones.setFont(Font.font("Arial", Math.max(12, stackCentro.getWidth() / 50)));
        instrucciones.setFill(Color.WHITE);
        instrucciones.setTranslateY(-30);
        instrucciones.setTextAlignment(TextAlignment.CENTER);
        
        // Agregar todo al StackPane
        pantallaSuperpuesta.getChildren().addAll(fondo, titulo, subtitulo, instrucciones);
        
        // Posicionar textos
        StackPane.setAlignment(titulo, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setAlignment(subtitulo, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(instrucciones, javafx.geometry.Pos.BOTTOM_CENTER);
        
        pantallaSuperpuesta.setVisible(true);
    }

    // Reiniciar juego
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

    private void dibujarMapa() {
        gridMapa.getChildren().clear();
        char[][] mapa = juego.getMapa();
        Image imgSuelo = new Image(new File("src/main/resources/com/efragame/images/suelo1.png").toURI().toString());
        Image imgPared = new Image(new File("src/main/resources/com/efragame/images/pared1.png").toURI().toString());
        Image imgProta = new Image(new File("src/main/resources/com/efragame/images/protagonista.png").toURI().toString());
        Image imgEsbirro = new Image(new File("src/main/resources/com/efragame/images/esbirro.png").toURI().toString());
        Image imgEsqueleto = new Image(new File("src/main/resources/com/efragame/images/esqueleto.png").toURI().toString());
        Image imgZombie = new Image(new File("src/main/resources/com/efragame/images/zombie.png").toURI().toString());

        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                ImageView imgView = new ImageView(mapa[y][x] == '#' ? imgPared : imgSuelo);
                imgView.setFitWidth(tamañoCelda);
                imgView.setFitHeight(tamañoCelda);
                imgView.setPreserveRatio(false); // Permitir deformación para llenar la celda
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

    // Solo permitir movimiento si el juego no ha terminado y el protagonista está vivo
    private void moverProta(int dx, int dy) {
        // No mover si el juego terminó o el protagonista está muerto
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
            System.out.println("¡Encontraste un enemigo!");
            infoCombate.clear();
            combatir(p, enemigo);
            actualizarEstadisticas();
        } else {
            System.out.println("Moviendo de (" + p.getX() + "," + p.getY() + ") a (" + nuevoX + "," + nuevoY + ")");
            p.setPosicion(nuevoX, nuevoY);
            infoCombate.clear();
        }

        // Solo mover enemigos si el protagonista sigue vivo
        if (juego.getProta().estaVivo()) {
            moverEnemigosConPercepcion();
        }

        dibujarMapa();
        actualizarEstadisticas();
        
        // Comprobar estado del juego después de cada movimiento
        verificarEstadoJuego();
    }

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

    private void actualizarEstadisticas() {
        vidaProta.setText("Vida: " + juego.getProta().getEstadisticas().getSalud());
        actualizarPanelStats(); // AÑADIR ESTA LÍNEA
    }

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

    private VBox crearPanelPersonaje(String nombre, String imagePath, int vida, int ataque, int defensa, Color colorFondo) {
        VBox panel = new VBox(3);
        panel.setStyle("-fx-background-color: rgba(" + 
            (int)(colorFondo.getRed() * 255) + "," + 
            (int)(colorFondo.getGreen() * 255) + "," + 
            (int)(colorFondo.getBlue() * 255) + ",0.2); " +
            "-fx-border-color: " + toHexString(colorFondo) + "; " +
            "-fx-border-width: 1; -fx-padding: 5;");
        
        // Crear HBox para imagen y stats
        HBox contenido = new HBox(8);
        contenido.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // Imagen del personaje
        try {
            Image img = new Image(new File(imagePath).toURI().toString());
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(40);
            imgView.setFitHeight(40);
            imgView.setPreserveRatio(true);
            contenido.getChildren().add(imgView);
        } catch (Exception e) {
            // Si no se puede cargar la imagen, crear un rectángulo de color
            Rectangle rect = new Rectangle(40, 40, colorFondo);
            contenido.getChildren().add(rect);
        }
        
        // Stats del personaje
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

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }

    // Agregar controles para reiniciar y salir
    @FXML
    public void manejarTecla(KeyEvent event) {
        System.out.println("Tecla presionada: " + event.getCode());
        
        // Si el juego terminó
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
        
        // Controles normales del juego (solo si no ha terminado)
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
            case ESCAPE: // Permitir salir durante el juego también
                SceneManager.getInstance().switchTo(SceneID.PORTADA);
                break;
            default:
                break;  
        }
        event.consume();
    }
}