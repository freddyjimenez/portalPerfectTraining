/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.controller;

import cl.perfecttraining.sys.controller.exceptions.NonexistentEntityException;
import cl.perfecttraining.sys.controller.exceptions.PreexistingEntityException;
import cl.perfecttraining.sys.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.perfecttraining.sys.model.Perfil;
import cl.perfecttraining.sys.model.Permiso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Freddy Jimenez
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (permiso.getPerfilCollection() == null) {
            permiso.setPerfilCollection(new ArrayList<Perfil>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Perfil> attachedPerfilCollection = new ArrayList<Perfil>();
            for (Perfil perfilCollectionPerfilToAttach : permiso.getPerfilCollection()) {
                perfilCollectionPerfilToAttach = em.getReference(perfilCollectionPerfilToAttach.getClass(), perfilCollectionPerfilToAttach.getNombre());
                attachedPerfilCollection.add(perfilCollectionPerfilToAttach);
            }
            permiso.setPerfilCollection(attachedPerfilCollection);
            em.persist(permiso);
            for (Perfil perfilCollectionPerfil : permiso.getPerfilCollection()) {
                perfilCollectionPerfil.getPermisoCollection().add(permiso);
                perfilCollectionPerfil = em.merge(perfilCollectionPerfil);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPermiso(permiso.getNombre()) != null) {
                throw new PreexistingEntityException("Permiso " + permiso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getNombre());
            Collection<Perfil> perfilCollectionOld = persistentPermiso.getPerfilCollection();
            Collection<Perfil> perfilCollectionNew = permiso.getPerfilCollection();
            Collection<Perfil> attachedPerfilCollectionNew = new ArrayList<Perfil>();
            for (Perfil perfilCollectionNewPerfilToAttach : perfilCollectionNew) {
                perfilCollectionNewPerfilToAttach = em.getReference(perfilCollectionNewPerfilToAttach.getClass(), perfilCollectionNewPerfilToAttach.getNombre());
                attachedPerfilCollectionNew.add(perfilCollectionNewPerfilToAttach);
            }
            perfilCollectionNew = attachedPerfilCollectionNew;
            permiso.setPerfilCollection(perfilCollectionNew);
            permiso = em.merge(permiso);
            for (Perfil perfilCollectionOldPerfil : perfilCollectionOld) {
                if (!perfilCollectionNew.contains(perfilCollectionOldPerfil)) {
                    perfilCollectionOldPerfil.getPermisoCollection().remove(permiso);
                    perfilCollectionOldPerfil = em.merge(perfilCollectionOldPerfil);
                }
            }
            for (Perfil perfilCollectionNewPerfil : perfilCollectionNew) {
                if (!perfilCollectionOld.contains(perfilCollectionNewPerfil)) {
                    perfilCollectionNewPerfil.getPermisoCollection().add(permiso);
                    perfilCollectionNewPerfil = em.merge(perfilCollectionNewPerfil);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = permiso.getNombre();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            Collection<Perfil> perfilCollection = permiso.getPerfilCollection();
            for (Perfil perfilCollectionPerfil : perfilCollection) {
                perfilCollectionPerfil.getPermisoCollection().remove(permiso);
                perfilCollectionPerfil = em.merge(perfilCollectionPerfil);
            }
            em.remove(permiso);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Permiso findPermiso(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
