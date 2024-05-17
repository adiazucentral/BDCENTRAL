package net.cltech.enterprisent.domain.integration.resultados;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa las ordenes de respuesta del HIS para enviarselas
 * al LIS
 * 
 * @version 1.0.0
 * @author javila
 * @since 28/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Respuesta de resultados de las ordenes",
        description = "Representa las ordenes de respuesta del HIS para enviarselas al LIS"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class ResponseOrderResult
{
    @ApiObjectField(name = "idOrder", description = "Id de la orden", required = true, order = 1)
    private long idOrder;
    @ApiObjectField(name = "interfaceKey", description = "Llave de la interfaz", required = true, order = 2)
    private String interfaceKey;
    @ApiObjectField(name = "record", description = "Historia", required = true, order = 3)
    private String record;
    @ApiObjectField(name = "name", description = "Nombre del paciente", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "lastName", description = "Apellidos del paciente", required = true, order = 3)
    private String lastName;
    @ApiObjectField(name = "gender", description = "Genero", required = true, order = 3)
    private int gender;
    @ApiObjectField(name = "birthDate", description = "Fecha de nacimiento del paciente", required = true, order = 3)
    private Date birthDate;
    @ApiObjectField(name = "serCode", description = "Codigo del servicio", required = true, order = 3)
    private String serCode;
    @ApiObjectField(name = "serName", description = "Nombre del servicio", required = true, order = 4)
    private String serName;
    @ApiObjectField(name = "sedeCode", description = "Codigo de la sede", required = true, order = 5)
    private String sedeCode;
    @ApiObjectField(name = "ambitoCode", description = "Codigo del ambito", required = true, order = 6)
    private String ambitoCode;
    @ApiObjectField(name = "priority", description = "Prioridad", required = true, order = 6)
    private String priority;
    @ApiObjectField(name = "idType", description = "Tipo de documento", required = true, order = 6)
    private String idType;
    @ApiObjectField(name = "alreadySent", description = "Pruebas enviadas al HIS", required = true, order = 6)
    private int alreadySent;
    @ApiObjectField(name = "numHistoria", description = "Número de la historia", required = true, order = 6)
    private String numHistoria;
    @ApiObjectField(name = "cuenta", description = "Cuenta key", required = true, order = 6)
    private String cuenta;
    @ApiObjectField(name = "correo", description = "Correo paciente", required = true, order = 6)
    private String correo;
    @ApiObjectField(name = "telefono", description = "Telefono del paciente", required = true, order = 6)
    private String telefono;

    public ResponseOrderResult(long idOrder, String interfaceKey, String record, String name, String lastName, int gender, Date birthDate, String serCode, String serName, String sedeCode, String ambitoCode, String priority, String idType, int alreadySent, String numHistoria, String cuenta, String correo, String telefono)
    {
        this.idOrder = idOrder;
        this.interfaceKey = interfaceKey;
        this.record = record;
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.serCode = serCode;
        this.serName = serName;
        this.sedeCode = sedeCode;
        this.ambitoCode = ambitoCode;
        this.priority = priority;
        this.idType = idType;
        this.alreadySent = alreadySent;
        this.numHistoria = numHistoria;
        this.cuenta = cuenta;
        this.correo = correo;
        this.telefono = telefono;
    }
}
