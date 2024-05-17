package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representan los datos con los que se realizara el filtro para obtener el reporte de caja
*
* @version 1.0.0
* @author Julian
* @since 6/05/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Filtro De Reporte De Caja",
        description = "Representan los datos con los que se realizara el filtro para obtener el reporte de caja"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class CashReportFilter 
{
    @ApiObjectField(name = "branchId", description = "Id de la sede", required = true, order = 1)
    private Integer branchId;
    @ApiObjectField(name = "startDate", description = "Fecha de inicio formato", required = true, order = 2)
    private String startDate;
    @ApiObjectField(name = "endDate", description = "Fecha de final formato", required = true, order = 3)
    private String endDate;
    @ApiObjectField(name = "userId", description = "Id del usuario", required = true, order = 4)
    private Integer userId;
    @ApiObjectField(name = "typeBalance", description = "Tipo de saldo. 1-> General, 2-> Credito paciente, 3 -> Credito Cliente, 4-> Factura combo", required = true, order = 5)
    private Integer typeBalance;
    
    @ApiObjectField(name = "ruc", description = "Lista de ruc para filtrar", order = 6)
    private List<String> ruc = new ArrayList<>();    
    @ApiObjectField(name = "phoneBox", description = "Lista de correo de caja para filtrar", order = 7)
    private List<String> phoneBox = new ArrayList<>();    
    @ApiObjectField(name = "history", description = "Lista de historia de paciente para filtrar", order = 8)
    private List<String> history = new ArrayList<>();
    @ApiObjectField(name = "phone", description = "Lista de telefonos de paciente para filtrar", order = 9)
    private List<String> phone = new ArrayList<>();
    @ApiObjectField(name = "emailPatient", description = "Lista de emails de paciente para filtrar", order = 10)
    private List<String> emailPatient = new ArrayList<>();    
}