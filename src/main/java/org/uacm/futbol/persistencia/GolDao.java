package org.uacm.futbol.persistencia;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Gol;

public class GolDao extends GenericJpaDao<Gol>{
    public GolDao(EntityManager em){
        super(em,Gol.class);
    }
}
