package org.uacm.futbol.servicios;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Posicion;
import org.uacm.futbol.persistencia.JugadorDao;
import org.uacm.futbol.persistencia.PosicionDao;

import java.util.List;

public class PosicionServiceimpl implements PosicionService{
    private final PosicionDao dao;

    public PosicionServiceimpl(EntityManager em) {
        this.dao = new PosicionDao(em);
    }

    @Override
    public Posicion crear(Posicion posicion) { return dao.save(posicion); }
    @Override
    public Posicion actualizar(Posicion posicion) { return dao.save(posicion); }
    @Override
    public Posicion buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Posicion> listarTodos() { return dao.findAll(); }
    @Override
    public void eliminar(Long id) {
        Posicion e = dao.find(id);
        if (e != null) dao.delete(e);
    }
}
