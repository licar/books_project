/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validators;

import java.util.Calendar;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author user
 */
@FacesValidator("validators.YearValidator")
public class YearValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        ResourceBundle bundle = ResourceBundle.getBundle("locales.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesMessage message = new FacesMessage(bundle.getString("invalid_year"));
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if (Integer.parseInt(value.toString()) > year || Integer.parseInt(value.toString()) < 0 ){
                message.setSeverity(FacesMessage.SEVERITY_ERROR); 
                throw new ValidatorException(message); //To change body of generated methods, choose Tools | Templates.
            }
        } catch (NullPointerException ex){
            throw new ValidatorException(message);
        }
    }
    
}
