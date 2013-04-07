/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.bean;

import cl.perfecttraining.sys.model.Cliente;
import cl.perfecttraining.sys.controller.ClienteJpaController;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Freddy Jimenez
 */
public class listarClientes implements Serializable {

    /**
     * Creates a new instance of listarClientes
     */
    @Resource
    private UserTransaction utx;
    @PersistenceUnit(unitName = "perfectPersistence")
    EntityManagerFactory emf;
    ClienteJpaController daoCliente;
    List<Cliente> clientes;
    List<Cliente> clientesFiltrados;
    Cliente clienteSelecionado;

    public listarClientes() {
        emf = javax.persistence.Persistence.createEntityManagerFactory("perfectPersistence");
        daoCliente = new ClienteJpaController(utx, emf);
        clientes = daoCliente.findClienteEntities();
        clientesFiltrados = daoCliente.findClienteEntities();
    }

    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(Cliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    public List<Cliente> getClientes() {
        clientes = daoCliente.findClienteEntities();
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Cliente> getClientesFiltrados() {
        return clientesFiltrados;
    }

    public void setClientesFiltrados(List<Cliente> clientesFiltrados) {
        this.clientesFiltrados = clientesFiltrados;
    }

    public void darDeBajaCliente(ActionEvent event) {
    }
}
