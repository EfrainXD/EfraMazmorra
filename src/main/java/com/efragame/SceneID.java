package com.efragame;

/**
 * Enumeración que representa los identificadores únicos para las distintas escenas del juego.
 * 
 * Esta enumeración se utiliza para gestionar la navegación entre vistas (escenas) dentro de
 * la aplicación mediante el {@code SceneManager}.
 */
public enum SceneID {
    
    /** Escena principal del juego donde se desarrolla la jugabilidad. */
    JUEGO,
    
    /** Escena de portada o menú principal que se muestra al iniciar el juego. */
    PORTADA,
    
    /** Escena de personalización donde el jugador puede modificar su personaje. */
    PERSONALIZACION
}
