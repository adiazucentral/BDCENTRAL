/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Orden de migration",
        description = "Representa una orden de laboratorio en migration"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNT
{

    @ApiObjectField(name = "order", description = "Numero de Orden", required = false, order = 1)
    private String order;
    @ApiObjectField(name = "order", description = "Numero de Orden creado en el HIS", required = false, order = 1)
    private String ordenHis;
    @ApiObjectField(name = "type", description = "Tipo de la orden", required = true, order = 2)
    private String type;
    @ApiObjectField(name = "user", description = "Usuario que crea la orden", required = false, order = 3)
    private String user;
    @ApiObjectField(name = "comment", description = "Comentario de la trazabilidad de la orden", required = false, order = 4)
    private String comment;
    @ApiObjectField(name = "centralSystem", description = "Nombre Sistema Cntral", required = false, order = 5)
    private String centralSystem;
    @ApiObjectField(name = "centralSytemId", description = "id centralSytemId", required = false, order = 6)
    private Integer centralSytemId;
    @ApiObjectField(name = "patient", description = "Paciente de la orden", required = true, order = 7)
    private PatientNT patient;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 8)
    private List<DemographicNT> demographics;
    @ApiObjectField(name = "tests", description = "examenes", required = false, order = 9)
    private List<TestNT> tests;

    public OrderNT(Order order)
    {

        this.patient = MigrationMapper.toDtoPatientNT(order.getPatient());
        this.type = order.getType().getCode();
        this.user = order.getCreateUser().getName();
        this.comment = order.getCommentary();

    }

}
