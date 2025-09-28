package org.uacm.futbol.servicios;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.persistencia.JugadorDao;


import java.util.List;

public class JugadorServiceimpl implements JugadorService{
    private final JugadorDao dao;

    public JugadorServiceimpl(EntityManager em) {
        this.dao = new JugadorDao(em);
    }

    @Override
    public Jugador crear(Jugador jugador) { return dao.save(jugador); }
    @Override
    public Jugador actualizar(Jugador jugador) { return dao.save(jugador); }
    @Override
    public Jugador buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Jugador> listarTodos() { return dao.findAll(); }
    @Override
    public void eliminar(Long id) {
        Jugador e = dao.find(id);
        if (e != null) dao.delete(e);
    }
}
