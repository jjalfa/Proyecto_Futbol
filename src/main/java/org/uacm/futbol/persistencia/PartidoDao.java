package org.uacm.futbol.persistencia;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Partido;

public class PartidoDao extends GenericJpaDao<Partido> {
    public PartidoDao(EntityManager em){
        super(em, Partido.class);
    }
}
