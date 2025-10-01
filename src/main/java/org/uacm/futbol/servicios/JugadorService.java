package org.uacm.futbol.servicios;

import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Jugador;

import java.util.List;

public interface JugadorService {
    Jugador crear(Jugador jugador);
    Jugador actualizar(Jugador jugador);
    Jugador buscarPorId(Long id);
    List<Jugador> listarTodos();
    void eliminar(Long id);
    void eliminarCascade(Long id);
    long contarGoles(Long jugadorId);
}
