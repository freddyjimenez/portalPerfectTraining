/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.portalperfecttraining.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.map.OverlaySelectEvent;  
import org.primefaces.model.map.DefaultMapModel;  
import org.primefaces.model.map.LatLng;  
import org.primefaces.model.map.MapModel;  
import org.primefaces.model.map.Marker; 

/**
 *
 * @author Freddy Jimenez
 */
@ManagedBean
@RequestScoped
public class mapBean {

    /**
     * Creates a new instance of mapBean
     */
     private MapModel advancedModel;  
  
    private Marker marker;  
  
    public mapBean() {  
        advancedModel = new DefaultMapModel();  
          
        //Shared coordinates  
        LatLng coord1 = new LatLng(-32.788452, -71.526487);  
  
          
        //Icons and Data  
        advancedModel.addOverlay(new Marker(coord1, "Konyaalti", "logobottom.png", "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));  

    }  
  
    public MapModel getAdvancedModel() {  
        return advancedModel;  
    }  
      
    public void onMarkerSelect(OverlaySelectEvent event) {  
        marker = (Marker) event.getOverlay();  
    }  
      
    public Marker getMarker() {  
        return marker;  
    }  
}
