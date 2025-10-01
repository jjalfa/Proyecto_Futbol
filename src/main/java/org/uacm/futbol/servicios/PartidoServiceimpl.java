package org.uacm.futbol.servicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.uacm.futbol.modelo.Gol;
import org.uacm.futbol.modelo.Partido;
import org.uacm.futbol.persistencia.PartidoDao;

import java.util.List;

public class PartidoServiceimpl implements PartidoService{
    private final PartidoDao dao;
    private final EntityManager em;

    public PartidoServiceimpl(EntityManager em) {this.dao = new PartidoDao(em);
    this.em = em;
    }

    @Override
    public Partido crear(Partido partido) { return dao.save(partido); }
    @Override
    public Partido actualizar(Partido partido) { return dao.save(partido); }
    @Override
    public Partido buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Partido> listarTodos() { return dao.findAll(); }
    /*@Override
    public void eliminar(Long id) {
        Partido e = dao.find(id);
        if (e != null) dao.delete(e);
    }*/
    public void eliminarCascade(Long partidoId) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Partido partido = em.find(Partido.class, partidoId);
            if (partido == null) { tx.commit(); return; }

            //  Borrar goles del partido
            List<Gol> goles = em.createQuery("SELECT g FROM Gol g WHERE g.partido.id = :id", Gol.class)
                    .setParameter("id", partidoId).getResultList();
            for (Gol gol : goles) {
                em.remove(em.contains(gol) ? gol : em.merge(gol));
            }

            // Eliminar el partido
            em.remove(em.contains(partido) ? partido : em.merge(partido));
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    @Override
    public void eliminar(Long id) { eliminarCascade(id); }
}