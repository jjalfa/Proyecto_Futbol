package org.uacm.futbol.persistencia;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Gol;

import java.util.List;

public class GolDao extends GenericJpaDao<Gol>{
    public GolDao(EntityManager em){
        super(em,Gol.class);
    }
    public List<Gol> findByJugadorId(Long jugadorId) {
        return em.createQuery("SELECT g FROM Gol g WHERE g.jugador.id = :id", Gol.class)
                .setParameter("id", jugadorId)
                .getResultList();
    }
    public List<Gol> findByPartidoId(Long partidoId) {
        return em.createQuery("SELECT g FROM Gol g WHERE g.partido.id = :id", Gol.class)
                .setParameter("id", partidoId)
                .getResultList();
    }
}
