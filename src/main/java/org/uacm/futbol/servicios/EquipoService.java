package org.uacm.futbol.servicios;

import org.uacm.futbol.modelo.Equipo;

import java.util.List;

public interface EquipoService {
    Equipo crear(Equipo equipo);
    Equipo actualizar(Equipo equipo);
    Equipo buscarPorId(Long id);
    List<Equipo> listarTodos();
    void eliminar(Long id);
    long contarJugadores(Long equipoId);
    long contarPartidos(Long equipoId);
}
