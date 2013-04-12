/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.bean;

import cl.perfecttraining.portal.bean.contactoBean;
import cl.perfecttraining.sys.controller.ClienteJpaController;
import cl.perfecttraining.sys.controller.UsuarioJpaController;
import cl.perfecttraining.sys.controller.exceptions.IllegalOrphanException;
import cl.perfecttraining.sys.controller.exceptions.PreexistingEntityException;
import cl.perfecttraining.sys.controller.exceptions.RollbackFailureException;
import cl.perfecttraining.sys.model.Cliente;
import cl.perfecttraining.sys.model.Perfil;
import cl.perfecttraining.sys.model.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;
import org.primefaces.context.RequestContext;

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
    UsuarioJpaController daoUsuario;
    Cliente nuevoCliente = new Cliente();
    Usuario nuevoUsuario = new Usuario();
    Perfil perfil = new Perfil("Cliente");

    public nuevoClienteBean() throws NamingException {
        emf = javax.persistence.Persistence.createEntityManagerFactory("perfectPersistence");
        Context initCtx = new InitialContext();
        utx = (UserTransaction) initCtx.lookup("java:comp/UserTransaction");
        daoCliente = new ClienteJpaController(utx, emf);
        daoUsuario = new UsuarioJpaController(utx, emf);
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

    public void guardarNuevoCliente() throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException {
        try {
            String clave = nuevoUsuario.getClave();
            nuevoUsuario.setPerfil(perfil);
            nuevoUsuario.setFechaIngreso(new Date());
            nuevoUsuario.setEstado(Boolean.TRUE);
            nuevoUsuario.setClave(DigestUtils.md5Hex(nuevoUsuario.getClave()));
            nuevoUsuario.setFechaCambioEstado(new Date());
            daoUsuario.create(nuevoUsuario);
            nuevoCliente.setUsuario(nuevoUsuario);
            nuevoCliente.setRut(nuevoUsuario.getRut());
            daoCliente.create(nuevoCliente);

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Agregado Correctamente", "El Cliente con Rut " + nuevoCliente.getRut() + " Ha Sido Agregado");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            enviarCorreo(nuevoUsuario.getEmail(), nuevoUsuario.getPrimerNombre() + " " + nuevoUsuario.getApellidoPaterno(), nuevoUsuario.getRut(), clave);
            // RequestContext.getCurrentInstance().execute("wi.hide()");
        } catch (RollbackFailureException ex) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error Interno", "Error Interno al Agregar Cliente por favor contactese con el administrador");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            Logger.getLogger(nuevoClienteBean.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error Interno", "Error Interno al Agregar Cliente por favor contactese con el administrador");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            Logger.getLogger(nuevoClienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarCorreo(String email, String nombre, String rut, String clave) {
        try {
            String mensaje = "<h3>Hola alguien</h3>\n"
                    + "<p>Le damos la bienvenida y agradecemos por preferir a Perfect Training.</p>\n"
                    + "\n"
                    + "<p>A continuación, te detallamos tus datos para poder acceder al sistema.</p>\n"
                    + "<h4>Rut:" + rut + "</h4>\n"
                    + "<h4>Contraseña:" + clave + "</h4>\n"
                    + "\n"
                    + "<p>Si tiene alguna duda, por favor contactarse a webmaster@perfecttraining.cl</p>\n"
                    + "<br/>\n"
                    + "<img src=\"http://localhost:8080/sys/img/logofooter.png\" alt=\"Logo Perfecttraining\">\n"
                    + "<h5>Perfect Training 2013 - Luis Uribe #2216 - Quintero - (032) 2930810 </h5>";
            Properties propsSSL = new Properties();
            propsSSL.put("mail.transport.protocol", "smtps");
            propsSSL.put("mail.smtps.host", "smtp.gmail.com");
            propsSSL.put("mail.smtps.auth", "true");

            Session sessionSSL = Session.getInstance(propsSSL);
            sessionSSL.setDebug(true);

            Message messageSSL = new MimeMessage(sessionSSL);
            messageSSL.setFrom(new InternetAddress("webmaster@perfecttraining.cl", "Sistema Perfect Training"));
            messageSSL.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // real recipient
            messageSSL.setSubject("Bienvenido a Perfect Training");
            messageSSL.setContent(mensaje, "text/html");

            Transport transportSSL = sessionSSL.getTransport();
            transportSSL.connect("smtp.gmail.com", 465, "webmaster@perfecttraining.cl", "itlmnhnoc@1"); // account used
            transportSSL.sendMessage(messageSSL, messageSSL.getAllRecipients());
            transportSSL.close();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Email ha Sido Enviado Correctamente", "Se ha Enviado un Email al Clientes con sus Datos de Acceso");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error Interno", "Error Interno al Enviar el Email al Cliente");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            Logger.getLogger(contactoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
