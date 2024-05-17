package net.cltech.enterprisent.domain.masters.demographic;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la modificación de la contraseña del usuario demografico
 * consulta web
 *
 * @version 1.0.0
 * @author javila
 * @since 29/01/2020
 */
@ApiObject(
        group = "Demografico",
        name = "Cambio de contraseña",
        description = "Representa el objeto que realizara el cambio de contraseña"
)
public class UserPassword {

    @ApiObjectField(name = "idUser", description = "Id del usuario", required = false, order = 1)
    private int idUser;
    @ApiObjectField(name = "userName", description = "Nombre de usuario", required = true, order = 2)
    private String userName;
    @ApiObjectField(name = "passwordOld", description = "Penultima contraseña", required = true, order = 3)
    private String passwordOld;
    @ApiObjectField(name = "passwordNew", description = "Nueva contraseña", required = true, order = 4)
    private String passwordNew;
    @ApiObjectField(name = "passwordOldSecond", description = "Antepenultima contraseña", required = false, order = 5)
    private String passwordOldSecond;

    public UserPassword() {
    }
    
    public UserPassword(int idUser, String userName, String passwordOld, String passwordNew, String passwordOldSecond) {
        this.idUser = idUser;
        this.userName = userName;
        this.passwordOld = passwordOld;
        this.passwordNew = passwordNew;
        this.passwordOldSecond = passwordOldSecond;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordOldSecond() {
        return passwordOldSecond;
    }

    public void setPasswordOldSecond(String passwordOldSecond) {
        this.passwordOldSecond = passwordOldSecond;
    }
}
