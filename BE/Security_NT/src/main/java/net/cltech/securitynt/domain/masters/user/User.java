package net.cltech.securitynt.domain.masters.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.print.attribute.standard.Destination;
import net.cltech.securitynt.domain.masters.common.Item;
import net.cltech.securitynt.domain.masters.common.MasterAudit;
import net.cltech.securitynt.domain.masters.demographic.OrderType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario del sistema
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/04/2017
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "Usuario",
        description = "Representa un usuario del sistema"
)
@JsonInclude(Include.NON_NULL)
public class User extends MasterAudit implements Cloneable
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombres del ususario", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "lastName", description = "Apellidos del usuario", required = true, order = 3)
    private String lastName;
    @ApiObjectField(name = "userName", description = "Nickname del usuario", required = true, order = 4)
    private String userName;
    @ApiObjectField(name = "password", description = "Ultima Contraseña de usuario", required = true, order = 5)
    private String password;
    @ApiObjectField(name = "state", description = "Si el usuario esta activo", required = true, order = 6)
    private boolean state;
    @ApiObjectField(name = "activation", description = "Fecha de activación", required = true, order = 7)
    private Date activation;
    @ApiObjectField(name = "expiration", description = "Fecha de expiración", required = true, order = 8)
    private Date expiration;
    @ApiObjectField(name = "passwordExpiration", description = "Fecha de expiración del password", required = true, order = 9)
    private Date passwordExpiration;
    @ApiObjectField(name = "identification", description = "Identificación del ususario", required = true, order = 10)
    private String identification;
    @ApiObjectField(name = "email", description = "Email del ususario", required = true, order = 11)
    private String email;
    @ApiObjectField(name = "signature", description = "Firma del usuario (Imagen)", order = 12)
    private String signature;
    @ApiObjectField(name = "signatureCode", description = "Codigo firma del usuario", order = 13)
    private String signatureCode;
    @ApiObjectField(name = "maxDiscount", description = "Descuento maximo permitido", order = 14)
    private Double maxDiscount;
    @ApiObjectField(name = "type", description = "Tipo de usuario", order = 15)
    private Item type;
    @ApiObjectField(name = "photo", description = "Foto del usuario (Imagen)", order = 16)
    private String photo;
    @ApiObjectField(name = "confidential", description = "Es confidencial", order = 17)
    private boolean confidential;
    @ApiObjectField(name = "printInReports", description = "Imprime en informes", order = 18)
    private boolean printInReports;
    @ApiObjectField(name = "addExams", description = "Agregar o quitar examenes", order = 19)
    private boolean addExams;
    @ApiObjectField(name = "secondValidation", description = "Segunda validacion", order = 20)
    private boolean secondValidation;
    @ApiObjectField(name = "editPatients", description = "Editar informacion de pacientes", order = 21)
    private boolean editPatients;
    @ApiObjectField(name = "quitValidation", description = "Desvalidar", order = 22)
    private boolean quitValidation;
    @ApiObjectField(name = "creatingItems", description = "Creación de items en ingreso", order = 23)
    private boolean creatingItems;
    @ApiObjectField(name = "printResults", description = "Imprimir resultados en ingreso de ordenes", order = 24)
    private boolean printResults;
    @ApiObjectField(name = "areas", description = "Areas asociadas al usuario", required = true, order = 25)
    private List<AreaByUser> areas;
    @ApiObjectField(name = "branches", description = "Sedes asociadas al usuario", required = true, order = 26)
    private List<BranchByUser> branches;
    @ApiObjectField(name = "roles", description = "Roles asociados al usuario", required = true, order = 27)
    private List<RoleByUser> roles;
    @ApiObjectField(name = "orderType", description = "Tipo de orden por defecto", order = 28)
    private OrderType orderType;
    @ApiObjectField(name = "photoBase64", description = "Foto del paciente en base64 para integración", required = true, order = 29)
    private String photoBase64;
    @ApiObjectField(name = "destination", description = "Destino que verifica para el usuario integración", required = false, order = 30)
    private Destination destination;
    @ApiObjectField(name = "dateLastLogin", description = "Fecha de ultimo ingreso de la sesion", required = false, order = 31)
    private Timestamp dateLastLogin;
    @ApiObjectField(name = "penultimatePassword", description = "Penultima Contraseña de usuario", required = true, order = 32)
    private String penultimatePassword;
    @ApiObjectField(name = "antepenultimatePassword", description = "Antepenultima Contraseña de usuario", required = true, order = 33)
    private String antepenultimatePassword;
    @ApiObjectField(name = "countFail", description = "Contador de intentos faliidos", required = true, order = 34)
    private int countFail;
    @ApiObjectField(name = "userTypeLogin", description = "Contador de intentos faliidos", required = true, order = 35)
    private int userTypeLogin;
    @ApiObjectField(name = "dateEntryLogin", description = "Fecha del ultimo ingreso", required = true, order = 36)
    private Timestamp dateEntryLogin;
    @ApiObjectField(name = "changePassword", description = "Cambio de cantraseña", required = true, order = 37)
    private boolean changePassword;
    @ApiObjectField(name = "validatedRecove", description = "Válida si se recupera la contraseña", order = 38)
    private boolean validatedRecove;
    @ApiObjectField(name = "editOrderCash", description = "Validar si tiene permisos para editar los examenes de una orden con caja", order = 39)
    private boolean editOrderCash;
    @ApiObjectField(name = "removeCashBox", description = "Validar si tiene permisos para eliminar la caja", order = 40)
    private boolean removeCashBox;

    public User()
    {
        areas = new ArrayList<>();
        branches = new ArrayList<>();
        roles = new ArrayList<>();
        type = new Item();
        orderType = new OrderType();
    }    

    public User(Integer id, String name, String lastName, String userName, String photoBase64)
    {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.photoBase64 = photoBase64;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Date getActivation()
    {
        return activation;
    }

    public void setActivation(Date activation)
    {
        this.activation = activation;
    }

    public Date getExpiration()
    {
        return expiration;
    }

    public void setExpiration(Date expiration)
    {
        this.expiration = expiration;
    }

    public Date getPasswordExpiration()
    {
        return passwordExpiration;
    }

    public void setPasswordExpiration(Date passwordExpiration)
    {
        this.passwordExpiration = passwordExpiration;
    }

    public String getIdentification()
    {
        return identification;
    }

    public void setIdentification(String identification)
    {
        this.identification = identification;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public String getSignatureCode()
    {
        return signatureCode;
    }

    public void setSignatureCode(String signatureCode)
    {
        this.signatureCode = signatureCode;
    }

    public Double getMaxDiscount()
    {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount)
    {
        this.maxDiscount = maxDiscount;
    }

    public Item getType()
    {
        return type;
    }

    public void setType(Item type)
    {
        this.type = type;
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

    public boolean isPrintInReports()
    {
        return printInReports;
    }

    public void setPrintInReports(boolean printInReports)
    {
        this.printInReports = printInReports;
    }

    public boolean isAddExams()
    {
        return addExams;
    }

    public void setAddExams(boolean addExams)
    {
        this.addExams = addExams;
    }

    public boolean isSecondValidation()
    {
        return secondValidation;
    }

    public void setSecondValidation(boolean secondValidation)
    {
        this.secondValidation = secondValidation;
    }

    public boolean isEditPatients()
    {
        return editPatients;
    }

    public void setEditPatients(boolean editPatients)
    {
        this.editPatients = editPatients;
    }

    public boolean isQuitValidation()
    {
        return quitValidation;
    }

    public void setQuitValidation(boolean quitValidation)
    {
        this.quitValidation = quitValidation;
    }

    public boolean isCreatingItems()
    {
        return creatingItems;
    }

    public void setCreatingItems(boolean creatingItems)
    {
        this.creatingItems = creatingItems;
    }

    public boolean isPrintResults()
    {
        return printResults;
    }

    public void setPrintResults(boolean printResults)
    {
        this.printResults = printResults;
    }

    public List<AreaByUser> getAreas()
    {
        return areas;
    }

    public void setAreas(List<AreaByUser> areas)
    {
        this.areas = areas;
    }

    public List<BranchByUser> getBranches()
    {
        return branches;
    }

    public void setBranches(List<BranchByUser> branches)
    {
        this.branches = branches;
    }

    public List<RoleByUser> getRoles()
    {
        return roles;
    }

    public void setRoles(List<RoleByUser> roles)
    {
        this.roles = roles;
    }

    public OrderType getOrderType()
    {
        return orderType;
    }

    public void setOrderType(OrderType orderType)
    {
        this.orderType = orderType;
    }

    public String getPhotoBase64()
    {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64)
    {
        this.photoBase64 = photoBase64;
    }

    public Timestamp getDateLastLogin()
    {
        return dateLastLogin;
    }

    public void setDateLastLogin(Timestamp dateLastLogin)
    {
        this.dateLastLogin = dateLastLogin;
    }

    public Destination getDestination()
    {
        return destination;
    }

    public void setDestination(Destination destination)
    {
        this.destination = destination;
    }

    public String getPenultimatePassword()
    {
        return penultimatePassword;
    }

    public void setPenultimatePassword(String penultimatePassword)
    {
        this.penultimatePassword = penultimatePassword;
    }

    public String getAntepenultimatePassword()
    {
        return antepenultimatePassword;
    }

    public void setAntepenultimatePassword(String antepenultimatePassword)
    {
        this.antepenultimatePassword = antepenultimatePassword;
    }

    public int getCountFail()
    {
        return countFail;
    }

    public void setCountFail(int countFail)
    {
        this.countFail = countFail;
    }

    public int getUserTypeLogin() {
        return userTypeLogin;
    }

    public void setUserTypeLogin(int userTypeLogin) {
        this.userTypeLogin = userTypeLogin;
    }

    public Timestamp getDateEntryLogin() {
        return dateEntryLogin;
    }

    public void setDateEntryLogin(Timestamp dateEntryLogin) {
        this.dateEntryLogin = dateEntryLogin;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }
    
    public boolean isValidatedRecove() {
        return validatedRecove;
    }

    public void setValidatedRecove(boolean validatedRecove) {
        this.validatedRecove = validatedRecove;
    }

    public boolean isEditOrderCash() {
        return editOrderCash;
    }

    public void setEditOrderCash(boolean editOrderCash) {
        this.editOrderCash = editOrderCash;
    }

    public boolean isRemoveCashBox() {
        return removeCashBox;
    }

    public void setRemoveCashBox(boolean removeCashBox) {
        this.removeCashBox = removeCashBox;
    }

    @Override
    public String toString()
    {
        return "User{" + "id=" + id + ", name=" + name + ", lastName=" + lastName + ", userName=" + userName + ", password=" + password + ", state=" + state + ", activation=" + activation + ", expiration=" + expiration + ", passwordExpiration=" + passwordExpiration + ", identification=" + identification + ", email=" + email + ", signature=" + signature + ", signatureCode=" + signatureCode + ", maxDiscount=" + maxDiscount + ", type=" + type + ", photo=" + photo + ", confidential=" + confidential + ", printInReports=" + printInReports + ", addExams=" + addExams + ", secondValidation=" + secondValidation + ", editPatients=" + editPatients + ", quitValidation=" + quitValidation + ", creatingItems=" + creatingItems + ", printResults=" + printResults + ", areas=" + areas + ", branches=" + branches + ", roles=" + roles + '}';
    }

    public Object clone()
    {
        Object obj = null;
        try
        {
            obj = super.clone();
        } catch (CloneNotSupportedException ex)
        {
            System.out.println("No se puede duplicar");
        }
        return obj;
    }
}
