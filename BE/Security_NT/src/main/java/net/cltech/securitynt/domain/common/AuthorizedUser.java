/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un usuario autorizado
 *
 * @version 1.0.0
 * @author dcortes
 * @since 31/03/2017
 * @see Creación
 */
@ApiObject(
        group = "Usuarios",
        name = "AuthorizedUser",
        description = "Representa un usuario del sistema",
        show = false
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizedUser
{

    @ApiObjectField(name = "id", description = "Id del usuario", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "userName", description = "Usuario", required = true, order = 2)
    private String userName;
    @ApiObjectField(name = "lastName", description = "Apellido del usuario", required = true, order = 3)
    private String lastName;
    @ApiObjectField(name = "name", description = "Nombre del usuario", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "passwordExpiration", description = "Fecha de expiracion del usuario", required = true, order = 5)
    private Date passwordExpiration;
    @ApiObjectField(name = "branch", description = "Id de la sede", required = true, order = 6)
    private Integer branch;
    @ApiObjectField(name = "photo", description = "Foto del usuario", required = true, order = 7)
    private String photo;
    @ApiObjectField(name = "confidential", description = "Tiene permisos a examenes confidenciales", required = true, order = 8)
    private boolean confidential;
    @ApiObjectField(name = "administrator", description = "Indica si tiene un rol administrados", required = false, order = 9)
    private boolean administrator;
    @ApiObjectField(name = "type", description = "Tipo de usuario", required = true, order = 10)
    private Integer type;
    @ApiObjectField(name = "maxDiscount", description = "Maximo descuento permitido", required = true, order = 11)
    private Double maxDiscount;
    @ApiObjectField(name = "orderType", description = "Tipo de orden por defecto", required = true, order = 12)
    private Integer orderType;
    @ApiObjectField(name = "licenses", description = "Licencias del producto", required = true, order = 13)
    private HashMap<String, Boolean> licenses;
    @ApiObjectField(name = "userTypeLogin", description = "Contador de intentos faliidos", required = true, order = 35)
    private int userTypeLogin;
    @ApiObjectField(name = "creatingItems", description = "Creación de items en ingreso", order = 23)
    private boolean creatingItems;
    @ApiObjectField(name = "changePassword", description = "Cambio de cantraseña", required = true, order = 37)
    private boolean changePassword;
    @ApiObjectField(name = "discount", description = "Descuento del usuario para homebound", required = true, order = 38)
    private String discount;
    @ApiObjectField(name = "editOrderCash", description = "Validar si tiene permisos para editar los examenes de una orden con caja", order = 39)
    private boolean editOrderCash;
    @ApiObjectField(name = "removeCashBox", description = "Validar si tiene permisos para eliminar la caja", order = 40)
    private boolean removeCashBox;
    @ApiObjectField(name = "dateEntryLogin", description = "Fecha del ultimo ingreso", required = true, order = 36)
    private Timestamp dateEntryLogin;

    public AuthorizedUser()
    {
    }

    public AuthorizedUser(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String user)
    {
        this.userName = user;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getPasswordExpiration()
    {
        return passwordExpiration;
    }

    public void setPasswordExpiration(Date passwordExpiration)
    {
        this.passwordExpiration = passwordExpiration;
    }

    public Integer getBranch()
    {
        return branch;
    }

    public void setBranch(Integer branch)
    {
        this.branch = branch;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public boolean isConfidential()
    {
        return confidential;
    }

    public void setConfidential(boolean confidential)
    {
        this.confidential = confidential;
    }

    public boolean isAdministrator()
    {
        return administrator;
    }

    public void setAdministrator(boolean administrator)
    {
        this.administrator = administrator;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final AuthorizedUser other = (AuthorizedUser) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public Double getMaxDiscount()
    {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount)
    {
        this.maxDiscount = maxDiscount;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }

    public HashMap<String, Boolean> getLicenses()
    {
        return licenses;
    }

    public void setLicenses(HashMap<String, Boolean> licenses)
    {
        this.licenses = licenses;
    }

    public int getUserTypeLogin()
    {
        return userTypeLogin;
    }

    public void setUserTypeLogin(int userTypeLogin)
    {
        this.userTypeLogin = userTypeLogin;
    }

    public boolean isCreatingItems()
    {
        return creatingItems;
    }

    public void setCreatingItems(boolean creatingItems)
    {
        this.creatingItems = creatingItems;
    }

    public boolean isChangePassword()
    {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword)
    {
        this.changePassword = changePassword;
    }

    public String getDiscount()
    {
        return discount;
    }

    public void setDiscount(String discount)
    {
        this.discount = discount;
    }

    public boolean isEditOrderCash()
    {
        return editOrderCash;
    }

    public void setEditOrderCash(boolean editOrderCash)
    {
        this.editOrderCash = editOrderCash;
    }

    public boolean isRemoveCashBox()
    {
        return removeCashBox;
    }

    public void setRemoveCashBox(boolean removeCashBox)
    {
        this.removeCashBox = removeCashBox;
    }

    public Timestamp getDateEntryLogin()
    {
        return dateEntryLogin;
    }

    public void setDateEntryLogin(Timestamp dateEntryLogin)
    {
        this.dateEntryLogin = dateEntryLogin;
    }
}
