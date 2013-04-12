/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.portal.bean;

import cl.perfecttraining.sys.bean.nuevoClienteBean;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Freddy Jimenez
 */
@ManagedBean
@RequestScoped
public class contactoBean {

    String nombre;
    String email;
    String mensaje;

    /**
     * Creates a new instance of contactoBean
     */
    public contactoBean() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void enviarCorreo() {
        try {
            Properties propsSSL = new Properties();
            propsSSL.put("mail.transport.protocol", "smtps");
            propsSSL.put("mail.smtps.host", "smtp.gmail.com");
            propsSSL.put("mail.smtps.auth", "true");

            Session sessionSSL = Session.getInstance(propsSSL);
            sessionSSL.setDebug(true);

            Message messageSSL = new MimeMessage(sessionSSL);
            messageSSL.setFrom(new InternetAddress(email, nombre));
            messageSSL.setRecipients(Message.RecipientType.TO, InternetAddress.parse("contacto@perfecttraining.cl")); // real recipient
            messageSSL.setSubject("Mensaje de Contacto desde el PortalWeb Perfect Training");
            messageSSL.setText(mensaje+" Correo Electronico del Contacto:"+email);

            Transport transportSSL = sessionSSL.getTransport();
            transportSSL.connect("smtp.gmail.com", 465, "webmaster@perfecttraining.cl", "itlmnhnoc@1"); // account used
            transportSSL.sendMessage(messageSSL, messageSSL.getAllRecipients());
            transportSSL.close();
            setMensaje("");
            setEmail("");
            setNombre("");
             FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Su Mensaje ha Sido Enviado Correctamente", "Prontamente nos Pondremos en Contacto con Usted");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } catch (Exception ex) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error Interno", "Error Interno al Enviar el Mensaje");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            Logger.getLogger(contactoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
