package net.cltech.enterprisent.domain.masters.test;

import java.util.List;
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
        description = "Representa los laboratorios de una sede"
)
public class LaboratoryByBranch
{

    @ApiObjectField(name = "branch_id", description = "Identificador del la sede", required = true, order = 1)
    private Integer branch_id;
    @ApiObjectField(name = "name", description = "Nombre del laboratorio", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "code", description = "Codigo  del laboratorio", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "select", description = "Si pertence a la sede", required = true, order = 4)
    private boolean select;
    @ApiObjectField(name = "laboratory_id", description = "Identificador del laboratorio", required = true, order = 5)
    private Integer laboratory_id;
    @ApiObjectField(name = "type", description = "Tipo de laboratorio:<br>1-Interno<br>2-Externo", required = true, order = 8)
    private short type;

    public short getType()
    {
        return type;
    }

    public void setType(short type)
    {
        this.type = type;
    }

    public LaboratoryByBranch()
    {

    }

    public LaboratoryByBranch(Integer branch_id)
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isSelect()
    {
        return select;
    }

    public void setSelect(boolean select)
    {
        this.select = select;
    }

    public Integer getLaboratory_id()
    {
        return laboratory_id;
    }

    public void setLaboratory_id(Integer laboratory_id)
    {
        this.laboratory_id = laboratory_id;
    }

}
