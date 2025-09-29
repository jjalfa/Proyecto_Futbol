package org.uacm.futbol.servicios;

import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Posicion;

import java.util.List;

public interface PosicionService {
    Posicion crear(Posicion Posicion);
    Posicion actualizar(Posicion posicion);
    Posicion buscarPorId(Long id);
    List<Posicion> listarTodos();
    void eliminar(Long id);
}
