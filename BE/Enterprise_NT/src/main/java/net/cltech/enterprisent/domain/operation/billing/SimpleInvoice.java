package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Detalle simple de una factura
*
* @version 1.0.0
* @author Julian
* @since 22/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Detalle Simple De La Factura",
        description = "Representa el detalle simple de la factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class SimpleInvoice 
{
    @ApiObjectField(name = "id", description = "Id de la factura", required = true, order = 1)
    private Long id;
    @ApiObjectField(name = "invoiceNumber", description = "Número de la factura", required = true, order = 2)
    private String invoiceNumber;
    @ApiObjectField(name = "userName", description = "Nombre de usuario", required = true, order = 3)
    private String userName;
    @ApiObjectField(name = "expirationDate", description = "Fecha de expiración de la factura", required = true, order = 4)
    private Timestamp expirationDate;
    @ApiObjectField(name = "particular", description = "tipo de factura 0 -> Cliente, 1-> particular", required = true, order = 5)
    private int particular;
    @ApiObjectField(name = "paymentDate", description = "Fecha de pago", required = true, order = 6)
    private Timestamp paymentDate;
    @ApiObjectField(name = "total", description = "Total de la factura", required = true, order = 7)
    private Double total;
    @ApiObjectField(name = "idUser", description = "Id usuario que creo la factura", required = true, order = 8)
    private int idUser;
    @ApiObjectField(name = "date", description = "Fecha de la factura", required = true, order = 9)
    private Timestamp date;
    @ApiObjectField(name = "paymentForm", description = "Forma de pago", required = true, order = 10)
    private int paymentForm;    
    @ApiObjectField(name = "status", description = "Estado de la factura", required = true, order = 11)
    private int status;
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 12)
    private Integer customerId;
    @ApiObjectField(name = "accountNit", description = "NIT del cliente", required = true, order = 13)
    private String accountNit;
    @ApiObjectField(name = "accountName", description = "Nombre del cliente", required = true, order = 14)
    private String accountName;
    @ApiObjectField(name = "accountPhone", description = "Telefono del cliente", required = true, order = 15)
    private String accountPhone;
    @ApiObjectField(name = "accountAddress", description = "Dirección del cliente", required = true, order = 16)
    private String accountAddress;
    @ApiObjectField(name = "accountCity", description = "Ciudad del cliente", required = true, order = 17)
    private String accountCity;
    @ApiObjectField(name = "accountCityName", description = "Nombre de la ciudad del cliente", required = true, order = 18)
    private String accountCityName;
    
}