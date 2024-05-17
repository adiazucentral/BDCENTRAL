package net.cltech.enterprisent.domain.masters.test;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los laboratorios de una sede
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 29/01/2020
 * @see Creacion
 */
@ApiObject(
        name = "Laboratorios por sede",
        group = "Prueba",
        description = "Actualiza los laboratorios de una sede"
)
public class LaboratorysByBranch
{

    @ApiObjectField(name = "branch_id", description = "Identificador del la sede", required = true, order = 1)
    private Integer branch_id;
    @ApiObjectField(name = "laboratories", description = "laboratorios a cambiar", required = true, order = 2)
    Integer[] laboratories =
    {
    };

    public LaboratorysByBranch()
    {

    }

    public LaboratorysByBranch(Integer branch_id)
    {
        this.branch_id = branch_id;
    }

    public Integer getBranch_id()
    {
        return branch_id;
    }

    public void setBranch_id(Integer branch_id)
    {
        this.branch_id = branch_id;
    }

    public Integer[] getLaboratories()
    {
        return laboratories;
    }

    public void setLaboratories(Integer[] laboratories)
    {
        this.laboratories = laboratories;
    }

}
