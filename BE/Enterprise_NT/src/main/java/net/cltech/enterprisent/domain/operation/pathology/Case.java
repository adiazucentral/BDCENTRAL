/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un caso de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 23/02/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Caso",
        description = "Representa la informacion de un caso de patologia"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Case extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos del caso", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "numberCase", description = "Identificador del caso", required = false, order = 2)
    private Long numberCase;
    @ApiObjectField(name = "studyType", description = "Tipo de Estudio", required = true, order = 3)
    private StudyType studyType = new StudyType();
    @ApiObjectField(name = "createdDateShort", description = "Fecha Creación en Formato yyyymmdd", required = true, order = 4)
    private Integer createdDateShort;
    @ApiObjectField(name = "order", description = "Orden", required = true, order = 5)
    private OrderPathology order = new OrderPathology();
    @ApiObjectField(name = "status", description = "Estado del caso", required = false, order = 6)
    private Integer status;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 7)
    private Branch branch = new Branch();
    @ApiObjectField(name = "specimens", description = "Especimenes", required = false, order = 8)
    private List<Specimen> specimens = new ArrayList<>();
    @ApiObjectField(name = "pathologist", description = "Patólogo", required = true, order = 9)
    private User pathologist = new User();
    @ApiObjectField(name = "quantity", description = "Cantidad de envases", required = false, order = 10)
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNumberCase() {
        return numberCase;
    }

    public void setNumberCase(Long numberCase) {
        this.numberCase = numberCase;
    }

    public StudyType getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyType studyType) {
        this.studyType = studyType;
    }

    public Integer getCreatedDateShort() {
        return createdDateShort;
    }

    public void setCreatedDateShort(Integer createdDateShort) {
        this.createdDateShort = createdDateShort;
    }

    public OrderPathology getOrder() {
        return order;
    }

    public void setOrder(OrderPathology order) {
        this.order = order;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public List<Specimen> getSpecimens() {
        return specimens;
    }

    public void setSpecimens(List<Specimen> specimens) {
        this.specimens = specimens;
    }

    public User getPathologist() {
        return pathologist;
    }

    public void setPathologist(User pathologist) {
        this.pathologist = pathologist;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
