/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.perfecttraining.portal.bean;

import java.io.IOException;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Freddy Jimenez
 */
public class loginBean {

    @ManagedProperty("#{authenticationManager}")
    private AuthenticationManager authenticationManager;
    /**
     * Creates a new instance of loginBean
     */
    String username;
    String password;

    public loginBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*  public String login() throws ServletException, IOException{
     ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();  
     HttpServletRequest request = ((HttpServletRequest)context.getRequest());
            
     ServletResponse resposnse = ((ServletResponse)context.getResponse());
     RequestDispatcher dispatcher = request.getRequestDispatcher("/perfectTraining/j_spring_security_check");
     dispatcher.forward(request, resposnse);
     FacesContext.getCurrentInstance().responseComplete();
     return null;
     }*/
    public void login() throws IOException {
        try {
            Authentication result = null;
            // Authentication request = new UsernamePasswordAuthenticationToken(this.getUsername(), this.getPassword());
            //   authenticationManager
            authenticationManager=(AuthenticationManager) getSpringBean("authenticationManager");
            result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(this.getUsername(), this.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
         FacesContext.getCurrentInstance().getExternalContext().redirect("/perfectTraining/"); 

    }
    private Object getSpringBean(String name){
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
                (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext());
        return ctx.getBean(name);
    }
}
