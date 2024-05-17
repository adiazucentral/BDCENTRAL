package net.cltech.enterprisent.domain.masters.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
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
@Getter
@Setter
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
    @ApiObjectField(name = "laboratory", description = "Identificador de laboratorio", required = false, order = 34)
    private Integer laboratory;
    @ApiObjectField(name = "dashboard", description = "Si el usuario tiene permiso o no en el tablero", required = true, order = 35)
    private boolean dashboard;
    @ApiObjectField(name = "preValidationRequired", description = "Pre validación de examenes requerida: 0 -> No, 1 -> Si", required = true, order = 36)
    private boolean preValidationRequired;
    @ApiObjectField(name = "printReportpreliminary", description = "Ver el botón de peliminar en registro de resultados: 0 -> No, 1 -> Si", required = true, order = 37)
    private boolean printReportpreliminary;
    @ApiObjectField(name = "updatetestentry", description = "Permisos para modificar examanes en ingreso de ordenes: 0 -> No, 1 -> Si", required = true, order = 38)
    private boolean updatetestentry;
    @ApiObjectField(name = "editOrderCash", description = "Validar si tiene permisos para editar los examenes de una orden con caja", order = 22)
    private boolean editOrderCash;
    @ApiObjectField(name = "demographicQuery", description = "Demograficos para consultas", required = false, order = 39)
    private Integer demographicQuery;
    @ApiObjectField(name = "demographicItemQuery", description = "Item Demograficos para consultas", required = false, order = 40)
    private Integer demographicItemQuery;
    @ApiObjectField(name = "removeCashBox", description = "Validar si tiene permisos para eliminar caja", order = 40)
    private boolean removeCashBox;
    @ApiObjectField(name = "editObservation", description = "Si el usuario tiene permiso o no en las observaciones", required = true, order = 35)
    private boolean editObservation;
    @ApiObjectField(name = "editPayments", description = "Si el usuario tiene permisos para editar pagos", required = true, order = 35)
    private boolean editPayments;
    @ApiObjectField(name = "editTestResult", description = "Si el usuario tiene permiso o no en los examanes de los resultados", required = true, order = 35)
    private boolean editTestResult;

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

    @Override
    public String toString()
    {
        return "User{" + "id=" + id + ", name=" + name + ", lastName=" + lastName + ", userName=" + userName + ", password=" + password + ", state=" + state + ", activation=" + activation + ", expiration=" + expiration + ", passwordExpiration=" + passwordExpiration + ", identification=" + identification + ", email=" + email + ", signature=" + signature + ", signatureCode=" + signatureCode + ", maxDiscount=" + maxDiscount + ", type=" + type + ", photo=" + photo + ", confidential=" + confidential + ", printInReports=" + printInReports + ", addExams=" + addExams + ", secondValidation=" + secondValidation + ", editPatients=" + editPatients + ", quitValidation=" + quitValidation + ", creatingItems=" + creatingItems + ", printResults=" + printResults + ", areas=" + areas + ", branches=" + branches + ", roles=" + roles + '}';
    }

    public Object clone() throws CloneNotSupportedException
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