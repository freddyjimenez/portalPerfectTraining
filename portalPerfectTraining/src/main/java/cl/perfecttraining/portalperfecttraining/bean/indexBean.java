/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.portalperfecttraining.bean;

import cl.perfecttraining.portalperfecttraining.modell.imageGaleriaIndex;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Freddy Jimenez
 */
@ManagedBean
@SessionScoped
public class indexBean implements Serializable {

    /**
     * Creates a new instance of indexBean
     */
    private List<imageGaleriaIndex> images;

    public indexBean() {
        images = new ArrayList<imageGaleriaIndex>();

        
            imageGaleriaIndex image1= new imageGaleriaIndex("imagen1.jpg", "descripcion", "titulo");
            images.add(image1);
            imageGaleriaIndex image2= new imageGaleriaIndex("imagen2.jpg", "descripcion", "titulo");
            images.add(image2);
            imageGaleriaIndex image3= new imageGaleriaIndex("imagen3.jpg", "descripcion", "titulo");
            images.add(image3);
            imageGaleriaIndex image4= new imageGaleriaIndex("imagen4.jpg", "descripcion", "titulo");
            images.add(image4);
            imageGaleriaIndex image5= new imageGaleriaIndex("imagen5.jpg", "descripcion", "titulo");
            images.add(image5);
        
    }

    public List<imageGaleriaIndex> getImages() {
        return images;
    }

    public void setImages(List<imageGaleriaIndex> images) {
        this.images = images;
    }

    public void login(ActionEvent actionEvent) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Proximente", "Proximamente Disponible");
        boolean loggedIn = false;

        // if(username != null  &&&& username.equals("admin") && password != null  && password.equals("admin")) {  
        //    loggedIn = true;  
        //   msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);  
        //  } else {  
        //      loggedIn = false;  
        //   msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Proximente", "Proximamente Disponible");
        //  }  

        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }
}
