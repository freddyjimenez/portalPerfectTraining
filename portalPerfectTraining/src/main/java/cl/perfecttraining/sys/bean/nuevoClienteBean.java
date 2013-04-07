/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.bean;

import cl.perfecttraining.sys.controller.ClienteJpaController;
import cl.perfecttraining.sys.controller.exceptions.IllegalOrphanException;
import cl.perfecttraining.sys.controller.exceptions.PreexistingEntityException;
import cl.perfecttraining.sys.controller.exceptions.RollbackFailureException;
import cl.perfecttraining.sys.model.Cliente;
import cl.perfecttraining.sys.model.Usuario;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 *
 * @author Freddy Jimenez
 */
public class nuevoClienteBean implements Serializable {

    /**
     * Creates a new instance of nuevoClienteBean
     */
    @Resource
    private UserTransaction utx;
    @PersistenceUnit(unitName = "perfectPersistence")
    EntityManagerFactory emf;
    ClienteJpaController daoCliente;
    Cliente nuevoCliente= new Cliente();
    Usuario nuevoUsuario= new Usuario();
    public nuevoClienteBean() {
         emf = javax.persistence.Persistence.createEntityManagerFactory("perfectPersistence");
        daoCliente = new ClienteJpaController(utx, emf);
        
    }

    public Usuario getNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(Usuario nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    public Cliente getNuevoCliente() {
        return nuevoCliente;
    }

    public void setNuevoCliente(Cliente nuevoCliente) {
        this.nuevoCliente = nuevoCliente;
    }
    public void guardarNuevoCliente() throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException{
        try {
            daoCliente.create(nuevoCliente);
        } catch (Exception ex) {
            Logger.getLogger(nuevoClienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
