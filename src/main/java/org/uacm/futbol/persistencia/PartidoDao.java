package org.uacm.futbol.persistencia;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Partido;

import java.util.List;

public class PartidoDao extends GenericJpaDao<Partido> {
    public PartidoDao(EntityManager em){
        super(em, Partido.class);
    }
    public List<Partido> findByEquipoId(Long equipoId) {
        return em.createQuery("SELECT p FROM Partido p WHERE p.equipo_local.id = :id OR p.equipo_visitante.id = :id", Partido.class)
                .setParameter("id", equipoId)
                .getResultList();
    }
}
