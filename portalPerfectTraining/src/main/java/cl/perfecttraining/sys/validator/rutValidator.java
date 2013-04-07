/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.sys.validator;

import cl.perfecttraining.sys.controller.ClienteJpaController;
import cl.perfecttraining.sys.controller.UsuarioJpaController;
import cl.perfecttraining.sys.model.Cliente;
import cl.perfecttraining.sys.model.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Freddy Jimenez
 */
public class rutValidator implements Validator {

    @Resource
    private UserTransaction utx;
    @PersistenceUnit(unitName = "perfectPersistence")
    EntityManagerFactory emf;
    UsuarioJpaController usuarioDao;

    public rutValidator() {
        emf = javax.persistence.Persistence.createEntityManagerFactory("perfectPersistence");
        usuarioDao = new UsuarioJpaController(utx, emf);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Si el valor es null, lo transformamos en un valor vacío
        String valor = StringUtils.defaultString((String) value);
        if (valor.length() < 9 || valor.length() > 10) {
            throw new ValidatorException(new FacesMessage("El Rut Ingresado es Invalido"));
        } else if (validar(valor) == false) {
            throw new ValidatorException(new FacesMessage("El Rut Ingresado es Invalido"));
        } else {
            Usuario usuarioTem = usuarioDao.findUsuario(valor);
            if (usuarioTem != null) {
                throw new ValidatorException(new FacesMessage("El Rut Ingresado ya Existe"));

            }
        }
    }

    public boolean validar(String rut) {
        int suma = 0;
        String dvR, dvT;
        int[] serie = {2, 3, 4, 5, 6, 7};
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");

        dvR = rut.substring(rut.length() - 1);

        for (int i = rut.length() - 2; i >= 0; i--) {
            suma += Integer.valueOf(rut.substring(i, i + 1))
                    * serie[(rut.length() - 2 - i) % 6];
        }
        dvT = String.valueOf(11 - suma % 11);
        if (dvT.compareToIgnoreCase("10") == 0) {
            dvT = "K";
        }
        if (dvT.compareToIgnoreCase(dvR) == 0) {
            return true;
        } else {
            return false;
        }
    }
}
