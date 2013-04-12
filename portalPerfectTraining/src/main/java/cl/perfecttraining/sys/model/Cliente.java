/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Freddy Jimenez
 */
@Entity
@Table(name = "cliente", catalog = "perfectdb", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByRut", query = "SELECT c FROM Cliente c WHERE c.rut = :rut"),
    @NamedQuery(name = "Cliente.findByOcupacion", query = "SELECT c FROM Cliente c WHERE c.ocupacion = :ocupacion"),
    @NamedQuery(name = "Cliente.findByLesionesOperaciones", query = "SELECT c FROM Cliente c WHERE c.lesionesOperaciones = :lesionesOperaciones"),
    @NamedQuery(name = "Cliente.findByEnfermedadesCardiacas", query = "SELECT c FROM Cliente c WHERE c.enfermedadesCardiacas = :enfermedadesCardiacas"),
    @NamedQuery(name = "Cliente.findByEnfermedadesPrevias", query = "SELECT c FROM Cliente c WHERE c.enfermedadesPrevias = :enfermedadesPrevias"),
    @NamedQuery(name = "Cliente.findByProblemasSensoriales", query = "SELECT c FROM Cliente c WHERE c.problemasSensoriales = :problemasSensoriales"),
    @NamedQuery(name = "Cliente.findByEnfermedadesAlergicas", query = "SELECT c FROM Cliente c WHERE c.enfermedadesAlergicas = :enfermedadesAlergicas"),
    @NamedQuery(name = "Cliente.findByRealizaActividad5Anos", query = "SELECT c FROM Cliente c WHERE c.realizaActividad5Anos = :realizaActividad5Anos"),
    @NamedQuery(name = "Cliente.findByRealizaActividad", query = "SELECT c FROM Cliente c WHERE c.realizaActividad = :realizaActividad"),
    @NamedQuery(name = "Cliente.findByTipoActividadFisica", query = "SELECT c FROM Cliente c WHERE c.tipoActividadFisica = :tipoActividadFisica"),
    @NamedQuery(name = "Cliente.findByFrecuenciaSemanalActividadFisica", query = "SELECT c FROM Cliente c WHERE c.frecuenciaSemanalActividadFisica = :frecuenciaSemanalActividadFisica"),
    @NamedQuery(name = "Cliente.findByDuracionActividadFisica", query = "SELECT c FROM Cliente c WHERE c.duracionActividadFisica = :duracionActividadFisica"),
    @NamedQuery(name = "Cliente.findByHorasDormir", query = "SELECT c FROM Cliente c WHERE c.horasDormir = :horasDormir"),
    @NamedQuery(name = "Cliente.findByFuma", query = "SELECT c FROM Cliente c WHERE c.fuma = :fuma"),
    @NamedQuery(name = "Cliente.findByCuantoFuma", query = "SELECT c FROM Cliente c WHERE c.cuantoFuma = :cuantoFuma"),
    @NamedQuery(name = "Cliente.findByBebeAlcohol", query = "SELECT c FROM Cliente c WHERE c.bebeAlcohol = :bebeAlcohol"),
    @NamedQuery(name = "Cliente.findByFrecuenciaBeberAlcohol", query = "SELECT c FROM Cliente c WHERE c.frecuenciaBeberAlcohol = :frecuenciaBeberAlcohol"),
    @NamedQuery(name = "Cliente.findByTipoAlcohol", query = "SELECT c FROM Cliente c WHERE c.tipoAlcohol = :tipoAlcohol"),
    @NamedQuery(name = "Cliente.findByConsumeCafe", query = "SELECT c FROM Cliente c WHERE c.consumeCafe = :consumeCafe"),
    @NamedQuery(name = "Cliente.findByTipoCafe", query = "SELECT c FROM Cliente c WHERE c.tipoCafe = :tipoCafe")})
public class Cliente implements Serializable {
    @Size(max = 1000)
    @Column(name = "alergico_a")
    private String alergicoA;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "rut")
    private String rut;
    @Size(max = 30)
    @Column(name = "ocupacion")
    private String ocupacion;
    @Size(max = 1000)
    @Column(name = "lesiones_operaciones")
    private String lesionesOperaciones;
    @Size(max = 1000)
    @Column(name = "enfermedades_cardiacas")
    private String enfermedadesCardiacas;
    @Size(max = 1000)
    @Column(name = "enfermedades_previas")
    private String enfermedadesPrevias;
    @Size(max = 1000)
    @Column(name = "problemas_sensoriales")
    private String problemasSensoriales;
    @Size(max = 1000)
    @Column(name = "enfermedades_alergicas")
    private String enfermedadesAlergicas;
    @Column(name = "realiza_actividad_5_anos")
    private Boolean realizaActividad5Anos;
    @Column(name = "realiza_actividad")
    private Boolean realizaActividad;
    @Size(max = 100)
    @Column(name = "tipo_actividad_fisica")
    private String tipoActividadFisica;
    @Column(name = "frecuencia_semanal_actividad_fisica")
    private Integer frecuenciaSemanalActividadFisica;
    @Column(name = "duracion_actividad_fisica")
    private Integer duracionActividadFisica;
    @Column(name = "horas_dormir")
    private Integer horasDormir;
    @Column(name = "fuma")
    private Boolean fuma;
    @Column(name = "cuanto_fuma")
    private Integer cuantoFuma;
    @Column(name = "bebe_alcohol")
    private Boolean bebeAlcohol;
    @Column(name = "frecuencia_beber_alcohol")
    private Integer frecuenciaBeberAlcohol;
    @Size(max = 100)
    @Column(name = "tipo_alcohol")
    private String tipoAlcohol;
    @Column(name = "consume_cafe")
    private Boolean consumeCafe;
    @Size(max = 100)
    @Column(name = "tipo_cafe")
    private String tipoCafe;
    @JoinColumn(name = "rut", referencedColumnName = "rut", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;

    public Cliente() {
    }

    public Cliente(String rut) {
        this.rut = rut;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getLesionesOperaciones() {
        return lesionesOperaciones;
    }

    public void setLesionesOperaciones(String lesionesOperaciones) {
        this.lesionesOperaciones = lesionesOperaciones;
    }

    public String getEnfermedadesCardiacas() {
        return enfermedadesCardiacas;
    }

    public void setEnfermedadesCardiacas(String enfermedadesCardiacas) {
        this.enfermedadesCardiacas = enfermedadesCardiacas;
    }

    public String getEnfermedadesPrevias() {
        return enfermedadesPrevias;
    }

    public void setEnfermedadesPrevias(String enfermedadesPrevias) {
        this.enfermedadesPrevias = enfermedadesPrevias;
    }

    public String getProblemasSensoriales() {
        return problemasSensoriales;
    }

    public void setProblemasSensoriales(String problemasSensoriales) {
        this.problemasSensoriales = problemasSensoriales;
    }

    public String getEnfermedadesAlergicas() {
        return enfermedadesAlergicas;
    }

    public void setEnfermedadesAlergicas(String enfermedadesAlergicas) {
        this.enfermedadesAlergicas = enfermedadesAlergicas;
    }

    public Boolean getRealizaActividad5Anos() {
        return realizaActividad5Anos;
    }

    public void setRealizaActividad5Anos(Boolean realizaActividad5Anos) {
        this.realizaActividad5Anos = realizaActividad5Anos;
    }

    public Boolean getRealizaActividad() {
        return realizaActividad;
    }

    public void setRealizaActividad(Boolean realizaActividad) {
        this.realizaActividad = realizaActividad;
    }

    public String getTipoActividadFisica() {
        return tipoActividadFisica;
    }

    public void setTipoActividadFisica(String tipoActividadFisica) {
        this.tipoActividadFisica = tipoActividadFisica;
    }

    public Integer getFrecuenciaSemanalActividadFisica() {
        return frecuenciaSemanalActividadFisica;
    }

    public void setFrecuenciaSemanalActividadFisica(Integer frecuenciaSemanalActividadFisica) {
        this.frecuenciaSemanalActividadFisica = frecuenciaSemanalActividadFisica;
    }

    public Integer getDuracionActividadFisica() {
        return duracionActividadFisica;
    }

    public void setDuracionActividadFisica(Integer duracionActividadFisica) {
        this.duracionActividadFisica = duracionActividadFisica;
    }

    public Integer getHorasDormir() {
        return horasDormir;
    }

    public void setHorasDormir(Integer horasDormir) {
        this.horasDormir = horasDormir;
    }

    public Boolean getFuma() {
        return fuma;
    }

    public void setFuma(Boolean fuma) {
        this.fuma = fuma;
    }

    public Integer getCuantoFuma() {
        return cuantoFuma;
    }

    public void setCuantoFuma(Integer cuantoFuma) {
        this.cuantoFuma = cuantoFuma;
    }

    public Boolean getBebeAlcohol() {
        return bebeAlcohol;
    }

    public void setBebeAlcohol(Boolean bebeAlcohol) {
        this.bebeAlcohol = bebeAlcohol;
    }

    public Integer getFrecuenciaBeberAlcohol() {
        return frecuenciaBeberAlcohol;
    }

    public void setFrecuenciaBeberAlcohol(Integer frecuenciaBeberAlcohol) {
        this.frecuenciaBeberAlcohol = frecuenciaBeberAlcohol;
    }

    public String getTipoAlcohol() {
        return tipoAlcohol;
    }

    public void setTipoAlcohol(String tipoAlcohol) {
        this.tipoAlcohol = tipoAlcohol;
    }

    public Boolean getConsumeCafe() {
        return consumeCafe;
    }

    public void setConsumeCafe(Boolean consumeCafe) {
        this.consumeCafe = consumeCafe;
    }

    public String getTipoCafe() {
        return tipoCafe;
    }

    public void setTipoCafe(String tipoCafe) {
        this.tipoCafe = tipoCafe;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rut != null ? rut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.rut == null && other.rut != null) || (this.rut != null && !this.rut.equals(other.rut))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.perfecttraining.model.Cliente[ rut=" + rut + " ]";
    }

    public String getAlergicoA() {
        return alergicoA;
    }

    public void setAlergicoA(String alergicoA) {
        this.alergicoA = alergicoA;
    }

}
