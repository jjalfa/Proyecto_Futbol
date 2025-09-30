package org.uacm.futbol.servicios;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Gol;
import org.uacm.futbol.modelo.Partido;
import org.uacm.futbol.persistencia.PartidoDao;

import java.util.List;

public class PartidoServiceimpl implements PartidoService{
    private final PartidoDao dao;

    public PartidoServiceimpl(EntityManager em) {this.dao = new PartidoDao(em);}

    @Override
    public Partido crear(Partido partido) { return dao.save(partido); }
    @Override
    public Partido actualizar(Partido partido) { return dao.save(partido); }
    @Override
    public Partido buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Partido> listarTodos() { return dao.findAll(); }
    @Override
    public void eliminar(Long id) {
        Partido e = dao.find(id);
        if (e != null) dao.delete(e);
    }
}