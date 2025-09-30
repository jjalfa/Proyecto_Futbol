package org.uacm.futbol.servicios;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Gol;
import org.uacm.futbol.persistencia.GolDao;

import java.util.List;

public class GolServiceimpl implements GolService{
    private final GolDao dao;

    public GolServiceimpl(EntityManager em) {this.dao = new GolDao(em);}

    @Override
    public Gol crear(Gol gol) { return dao.save(gol); }
    @Override
    public Gol actualizar(Gol gol) { return dao.save(gol); }
    @Override
    public Gol buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Gol> listarTodos() { return dao.findAll(); }
    @Override
    public void eliminar(Long id) {
        Gol e = dao.find(id);
        if (e != null) dao.delete(e);
    }
}