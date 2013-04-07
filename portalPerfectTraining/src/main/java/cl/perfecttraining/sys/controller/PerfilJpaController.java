/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.controller;

import cl.perfecttraining.sys.controller.exceptions.NonexistentEntityException;
import cl.perfecttraining.sys.controller.exceptions.PreexistingEntityException;
import cl.perfecttraining.sys.controller.exceptions.RollbackFailureException;
import cl.perfecttraining.sys.model.Perfil;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.perfecttraining.sys.model.Permiso;
import java.util.ArrayList;
import java.util.Collection;
import cl.perfecttraining.sys.model.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Freddy Jimenez
 */
public class PerfilJpaController implements Serializable {

    public PerfilJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perfil perfil) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (perfil.getPermisoCollection() == null) {
            perfil.setPermisoCollection(new ArrayList<Permiso>());
        }
        if (perfil.getUsuarioCollection() == null) {
            perfil.setUsuarioCollection(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Permiso> attachedPermisoCollection = new ArrayList<Permiso>();
            for (Permiso permisoCollectionPermisoToAttach : perfil.getPermisoCollection()) {
                permisoCollectionPermisoToAttach = em.getReference(permisoCollectionPermisoToAttach.getClass(), permisoCollectionPermisoToAttach.getNombre());
                attachedPermisoCollection.add(permisoCollectionPermisoToAttach);
            }
            perfil.setPermisoCollection(attachedPermisoCollection);
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : perfil.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getRut());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            perfil.setUsuarioCollection(attachedUsuarioCollection);
            em.persist(perfil);
            for (Permiso permisoCollectionPermiso : perfil.getPermisoCollection()) {
                permisoCollectionPermiso.getPerfilCollection().add(perfil);
                permisoCollectionPermiso = em.merge(permisoCollectionPermiso);
            }
            for (Usuario usuarioCollectionUsuario : perfil.getUsuarioCollection()) {
                Perfil oldPerfilOfUsuarioCollectionUsuario = usuarioCollectionUsuario.getPerfil();
                usuarioCollectionUsuario.setPerfil(perfil);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
                if (oldPerfilOfUsuarioCollectionUsuario != null) {
                    oldPerfilOfUsuarioCollectionUsuario.getUsuarioCollection().remove(usuarioCollectionUsuario);
                    oldPerfilOfUsuarioCollectionUsuario = em.merge(oldPerfilOfUsuarioCollectionUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPerfil(perfil.getNombre()) != null) {
                throw new PreexistingEntityException("Perfil " + perfil + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perfil perfil) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Perfil persistentPerfil = em.find(Perfil.class, perfil.getNombre());
            Collection<Permiso> permisoCollectionOld = persistentPerfil.getPermisoCollection();
            Collection<Permiso> permisoCollectionNew = perfil.getPermisoCollection();
            Collection<Usuario> usuarioCollectionOld = persistentPerfil.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = perfil.getUsuarioCollection();
            Collection<Permiso> attachedPermisoCollectionNew = new ArrayList<Permiso>();
            for (Permiso permisoCollectionNewPermisoToAttach : permisoCollectionNew) {
                permisoCollectionNewPermisoToAttach = em.getReference(permisoCollectionNewPermisoToAttach.getClass(), permisoCollectionNewPermisoToAttach.getNombre());
                attachedPermisoCollectionNew.add(permisoCollectionNewPermisoToAttach);
            }
            permisoCollectionNew = attachedPermisoCollectionNew;
            perfil.setPermisoCollection(permisoCollectionNew);
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getRut());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            perfil.setUsuarioCollection(usuarioCollectionNew);
            perfil = em.merge(perfil);
            for (Permiso permisoCollectionOldPermiso : permisoCollectionOld) {
                if (!permisoCollectionNew.contains(permisoCollectionOldPermiso)) {
                    permisoCollectionOldPermiso.getPerfilCollection().remove(perfil);
                    permisoCollectionOldPermiso = em.merge(permisoCollectionOldPermiso);
                }
            }
            for (Permiso permisoCollectionNewPermiso : permisoCollectionNew) {
                if (!permisoCollectionOld.contains(permisoCollectionNewPermiso)) {
                    permisoCollectionNewPermiso.getPerfilCollection().add(perfil);
                    permisoCollectionNewPermiso = em.merge(permisoCollectionNewPermiso);
                }
            }
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    usuarioCollectionOldUsuario.setPerfil(null);
                    usuarioCollectionOldUsuario = em.merge(usuarioCollectionOldUsuario);
                }
            }
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    Perfil oldPerfilOfUsuarioCollectionNewUsuario = usuarioCollectionNewUsuario.getPerfil();
                    usuarioCollectionNewUsuario.setPerfil(perfil);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                    if (oldPerfilOfUsuarioCollectionNewUsuario != null && !oldPerfilOfUsuarioCollectionNewUsuario.equals(perfil)) {
                        oldPerfilOfUsuarioCollectionNewUsuario.getUsuarioCollection().remove(usuarioCollectionNewUsuario);
                        oldPerfilOfUsuarioCollectionNewUsuario = em.merge(oldPerfilOfUsuarioCollectionNewUsuario);
                    }
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
                String id = perfil.getNombre();
                if (findPerfil(id) == null) {
                    throw new NonexistentEntityException("The perfil with id " + id + " no longer exists.");
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
            Perfil perfil;
            try {
                perfil = em.getReference(Perfil.class, id);
                perfil.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfil with id " + id + " no longer exists.", enfe);
            }
            Collection<Permiso> permisoCollection = perfil.getPermisoCollection();
            for (Permiso permisoCollectionPermiso : permisoCollection) {
                permisoCollectionPermiso.getPerfilCollection().remove(perfil);
                permisoCollectionPermiso = em.merge(permisoCollectionPermiso);
            }
            Collection<Usuario> usuarioCollection = perfil.getUsuarioCollection();
            for (Usuario usuarioCollectionUsuario : usuarioCollection) {
                usuarioCollectionUsuario.setPerfil(null);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            em.remove(perfil);
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

    public List<Perfil> findPerfilEntities() {
        return findPerfilEntities(true, -1, -1);
    }

    public List<Perfil> findPerfilEntities(int maxResults, int firstResult) {
        return findPerfilEntities(false, maxResults, firstResult);
    }

    private List<Perfil> findPerfilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perfil.class));
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

    public Perfil findPerfil(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perfil.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perfil> rt = cq.from(Perfil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
