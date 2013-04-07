/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.portalperfecttraining.modell;

/**
 *
 * @author Freddy Jimenez
 */
public class imageGaleriaIndex {
    String url;
    String descripcion;
    String titulo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public imageGaleriaIndex(String url, String descripcion, String titulo) {
        this.url = url;
        this.descripcion = descripcion;
        this.titulo = titulo;
    }
    
}
