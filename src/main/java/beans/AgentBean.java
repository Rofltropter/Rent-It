/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import persistence.Agent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dcamp
 * Note: This file does not contain any getters/setters for the Agent object
 * since this project does not build either a login page for Agents, or use 
 * cases to create agent accounts. The objects listed are solely to accommodate 
 * the values required for the createAccount() use case implementation.
 * 
 */
@Named(value = "agentBean")
@SessionScoped
public class AgentBean implements Serializable {

    //Variables for new account to create here

    private String usrEmail;
    private String usrPassword;
    private String usrFirstName;
    private String usrLastName;
    private String usrUsername;
    private Boolean finalType;
    private String usrType; //0 = Owner, 1 = Customer
    private Integer custMaxRent;
    @PersistenceContext(unitName = "RentIt_PU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    
    /**
     * Creates a new instance of AgentBean
     */
    public AgentBean() {
    }

    /**
     * @return the emailId
     */
    public String getUsrEmail() {
        return usrEmail;
    }

    /**
     * @param usrEmail the usrEmail to set
     */
    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    /**
     * @return the usrUsername
     */
    public String getUsrUsername() {
        return usrUsername;
    }

    /**
     * @param usrUsername the username to set
     */
    public void setUsrUsername(String usrUsername) {
        this.usrUsername = usrUsername;
    }

    /**
     * @return the usrPassword
     */
    public String getUsrPassword() {
        return usrPassword;
    }

    /**
     * @param usrPassword the usrPassword to set
     */
    public void setUsrPassword(String usrPassword) {
        this.usrPassword = usrPassword;
    }

    /**
     * @return the usrFirstName
     */
    public String getUsrFirstName() {
        return usrFirstName;
    }

    /**
     * @param usrFirstName the usrFirstName to set
     */
    public void setUsrFirstName(String usrFirstName) {
        this.usrFirstName = usrFirstName;
    }

    /**
     * @return the lastName
     */
    public String getUsrLastName() {
        return usrLastName;
    }

    /**
     * @param usrLastName the lastName to set
     */
    public void setUsrLastName(String usrLastName) {
        this.usrLastName = usrLastName;
    }

    /**
     * @return the usrType
     */
    public String getUsrType() {
        return usrType;
    }

    /**
     * @param usrType the lastName to set
     */
    public void setUsrType(String usrType) {
        this.usrType = usrType;
    }

    /**
     * @return the maxRent
     */
    public Integer getCustMaxRent() {
        return custMaxRent;
    }

    /**
     * @param custMaxRent the maxRent to set
     */
    public void setCustMaxRent(Integer custMaxRent) {
        this.custMaxRent = custMaxRent;
    }

    public String createAccount(ActionEvent actionEvent) {
        if(usrType == "0"){finalType = false;}else{finalType=true;}
        //Email Matcher Algo from Stephen Connolly 
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(usrEmail);
        boolean emailverified = m.matches();
        if (emailverified == false) {
            String msg = "Error, User Email must be formatted properly (email@email.com) ";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getFlash().setKeepMessages(true);
            throw new IllegalArgumentException("Incorrect Email Format, must inclue proper characters to match format");
        }
        Pattern x = Pattern.compile("[a-z]");
        Matcher y = x.matcher(usrUsername);
        boolean usernameverified = y.matches();
        if (usernameverified == false) {
            String msg = "Error, User Username must only contain characters from A-Z ";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getFlash().setKeepMessages(true);
            throw new IllegalArgumentException("Error, User Username must only contain characters from A-Z");
        }

        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
        String creationDate = df.format(currentDate);        //Storing account creation date and time, in reference to server date and time
        if (finalType == false) { //Owner Account to create  
            persistence.Owner profile = new persistence.Owner(creationDate);  //creating the Owner account via reference to Owner in the persistence package  UNFINISHED
            try {
                persist(profile);                 //Attempts to persist account into DB after it's creation
                String msg = "Owner Account Created Successfully";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
            } catch (RuntimeException e) {
                String msg = "Error While Creating Owner Account";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().setKeepMessages(true);
            }
        } else {     //Customer Account to create
            persistence.Customer profile = new persistence.Customer(creationDate);  //creating the Customer account via reference to Customer in the persistence package  UNFINISHED   
            try {
                persist(profile); 
                String msg = "Customer Account Created Successfully";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
            } catch (RuntimeException e) {
                String msg = "Error While Creating Customer Account";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().setKeepMessages(true);
            }
        }
        return null;
    }

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}
