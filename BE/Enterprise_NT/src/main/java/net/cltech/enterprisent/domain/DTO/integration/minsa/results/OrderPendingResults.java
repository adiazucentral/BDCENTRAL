/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.minsa.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "OrderPendingResults",
        description = "Muestra informacion del maestro Areas que usa el API"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPendingResults
{

    @ApiObjectField(name = "order", description = "Id del area", required = true, order = 1)
    private String order;
    @ApiObjectField(name = "registerDate", description = "Ordenamiento del area", required = true, order = 2)
    private Date registerDate;
    @ApiObjectField(name = "type", description = "Abreviatura del area", required = true, order = 3)
    private String orderTypeName;
    @ApiObjectField(name = "type", description = "Abreviatura del area", required = true, order = 3)
    private String orderTypeCode;
    @ApiObjectField(name = "comment", description = "Nombre del area", required = true, order = 4)
    private String comment;
    @ApiObjectField(name = "registSample", description = "Color del area", required = true, order = 5)
    private Date registSample;
    @ApiObjectField(name = "regist", description = "Tipo del area", required = true, order = 6)
    private Date regist;
    @ApiObjectField(name = "patient", description = "Tipo del area", required = true, order = 7)
    private PatientPendingResults patient;
    @ApiObjectField(name = "demographics", description = "Tipo del area", required = true, order = 8)
    private List<DemographicValue> demographics;
    //private List<DemographicPendingResults> demographics;
    @ApiObjectField(name = "result", description = "Tipo del area", required = true, order = 9)
    private List<ResultsTestPendingResults> result;
    @ApiObjectField(name = "interview", description = "Tipo del area", required = true, order = 10)
    private List<QuestionPendingResults> interview;
    @ApiObjectField(name = "idTest", description = "Tipo del area", required = true, order = 11)
    private int idTest;
    @ApiObjectField(name = "idBranch", description = "Tipo del area", required = true, order = 12)
    private int idBranch;
    @ApiObjectField(name = "nameBranch", description = "Tipo del area", required = true, order = 13)
    private String nameBranch;
    @ApiObjectField(name = "idPhysician", description = "Tipo del area", required = true, order = 14)
    private int idPhysician;
    @ApiObjectField(name = "namePhysician", description = "Tipo del area", required = true, order = 15)
    private String namePhysician;

    public OrderPendingResults(String order)
    {
        this.order = order;
        this.demographics = new ArrayList<>();
        this.result = new ArrayList<>();

        this.patient = new PatientPendingResults();
        this.interview = new ArrayList<>();
    }

    public OrderPendingResults()
    {

    }
}
