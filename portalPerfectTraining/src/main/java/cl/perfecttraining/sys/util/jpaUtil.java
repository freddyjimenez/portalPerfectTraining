/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Freddy Jimenez
 */
public class jpaUtil {
    @PersistenceUnit(unitName = "perfectPersistence")
    private static final EntityManagerFactory emf;
    static{
        emf=javax.persistence.Persistence.createEntityManagerFactory("perfectPersistence");
    }

    public static EntityManagerFactory getEmf() {
        return emf;
    }
    
}
