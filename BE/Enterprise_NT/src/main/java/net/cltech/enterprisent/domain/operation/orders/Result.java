package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el resultado de un examen
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 5/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultado",
        name = "Resultado",
        description = "Representa resultado de un exámen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Result extends SuperResult
{
    @ApiObjectField(name = "dateOrdered", description = "Fecha Ingreso", required = false, order = 4)
    private Date dateOrdered;
    @ApiObjectField(name = "dateTake", description = "Fecha Toma", required = false, order = 5)
    private Date dateTake;
    @ApiObjectField(name = "dateSample", description = "Fecha Verificación", required = false, order = 6)
    private Date dateSample;
    @ApiObjectField(name = "dateMicrobiologySample", description = "Fecha Verificación Microbiologia", required = false, order = 7)
    private Date dateMicrobiologySample;
    @ApiObjectField(name = "dateMicrobiologyGrowth", description = "Fecha Siembra Microbiologia", required = false, order = 8)
    private Date dateMicrobiologyGrowth;
    @ApiObjectField(name = "dateValidation", description = "Fecha Validación", required = false, order = 10)
    private Date dateValidation;
    @ApiObjectField(name = "datePreValidation", description = "Fecha PreValidación", required = false, order = 11)
    private Date datePreValidation;
    @ApiObjectField(name = "datePrinted", description = "Fecha Impresión", required = false, order = 12)
    private Date datePrinted;
    @ApiObjectField(name = "userOrdered", description = "Usuario Ingreso", required = false, order = 13)
    private AuthorizedUser userOrdered = new AuthorizedUser();
    @ApiObjectField(name = "userTake", description = "Usuario Toma", required = false, order = 14)
    private AuthorizedUser userTake = new AuthorizedUser();
    @ApiObjectField(name = "userSample", description = "Usuario Verificación", required = false, order = 15)
    private AuthorizedUser userSample = new AuthorizedUser();
    @ApiObjectField(name = "userMicrobiologySample", description = "Usuario Verificación Microbiologia", required = false, order = 16)
    private AuthorizedUser userMicrobiologySample = new AuthorizedUser();
    @ApiObjectField(name = "userMicrobiologyGrowth", description = "Usuario Siembra Microbiologia", required = false, order = 17)
    private AuthorizedUser userMicrobiologyGrowth = new AuthorizedUser();
    @ApiObjectField(name = "userResult", description = "Usuario Resultado", required = false, order = 18)
    private AuthorizedUser userResult = new AuthorizedUser();
    @ApiObjectField(name = "userValidation", description = "Usuario Validación", required = false, order = 19)
    private AuthorizedUser userValidation = new AuthorizedUser();
    @ApiObjectField(name = "userSecondValidation", description = "Usuario PreValidación", required = false, order = 20)
    private AuthorizedUser userPreValidation = new AuthorizedUser();
    @ApiObjectField(name = "userPrinted", description = "Usuario Impresión", required = false, order = 21)
    private AuthorizedUser userPrinted = new AuthorizedUser();
    @ApiObjectField(name = "pathology", description = "Patología del examen con respecto al valor de referencia", required = true, order = 22)
    private int pathology;
    @ApiObjectField(name = "dateRetakes", description = "Fecha de retoma", required = false, order = 23)
    private Date dateRetakes;
    @ApiObjectField(name = "userRetakes", description = "Usuario de retoma", required = false, order = 24)
    private AuthorizedUser userRetakes = new AuthorizedUser();
    @ApiObjectField(name = "dateRepetition", description = "Fecha de repeticion", required = false, order = 25)
    private Date dateRepetition;
    @ApiObjectField(name = "userRepetition", description = "Usuario de repeticion", required = false, order = 26)
    private AuthorizedUser userRepetition = new AuthorizedUser();
    @ApiObjectField(name = "deliveryTypes", description = "Tipos de entregas", required = false, order = 27)
    private List<Deliverytype> deliveryTypes = new ArrayList<>();
    @ApiObjectField(name = "antibiogram", description = "Indica si el examen tiene antibiograma", required = true, order = 27)
    private int antibiogram;
}
