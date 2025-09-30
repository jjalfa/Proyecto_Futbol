package org.uacm.futbol.servicios;

import org.uacm.futbol.modelo.Gol;

import java.util.List;

public interface GolService {
    Gol crear(Gol equipo);
    Gol actualizar(Gol equipo);
    Gol buscarPorId(Long id);
    List<Gol> listarTodos();
    void eliminar(Long id);
}
