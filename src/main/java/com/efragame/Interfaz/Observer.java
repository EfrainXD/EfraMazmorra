package com.efragame.Interfaz;

/**
 * Interfaz para los observadores que desean recibir notificaciones de cambios.
 */
public interface Observer {
    /**
     * Método que se llama para notificar al observador de un cambio o actualización.
     */
    void actualizar();
}
