package org.uacm.futbol.servicios;

import org.uacm.futbol.modelo.Partido;

import java.util.List;

public interface PartidoService {
    Partido crear(Partido equipo);
    Partido actualizar(Partido equipo);
    Partido buscarPorId(Long id);
    List<Partido> listarTodos();
    void eliminar(Long id);
}
