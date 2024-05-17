package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la relacion entre Prueba, Sede y Laboratorio.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Prueba Por Laboratorio",
        description = "Clase que representa la relacion entre Prueba, Sede y Laboratorio"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestByLaboratory extends MasterAudit
{
    @ApiObjectField(name = "test", description = "Prueba", required = true, order = 1)
    private TestBasic test;
    @ApiObjectField(name = "idBranch", description = "Id de la sede", required = true, order = 2)
    private Integer idBranch;
    @ApiObjectField(name = "idLaboratory", description = "Id del laboratorio", required = true, order = 3)
    private Integer idLaboratory;
    @ApiObjectField(name = "codeLaboratory", description = "Codigo del laboratorio", required = true, order = 4)
    private Integer codeLaboratory;
    @ApiObjectField(name = "nameLaboratory", description = "Nombre del laboratorio", required = true, order = 5)
    private String nameLaboratory;
    @ApiObjectField(name = "groupType", description = "Grupo Tipo de Orden:  1 -> Urgencia, 2 -> Rutina", required = true, order = 6)
    private Short groupType;
    @ApiObjectField(name = "urgency", description = "Urgencia: 0 -> Esta activa en otro laboratorio, 1 -> Activo, 2 -> Inactivo", required = true, order = 7)
    private Short urgency;
    @ApiObjectField(name = "routine", description = "Rutina: 0 -> Esta activa en otro laboratorio, 1 -> Activo, 2 -> Inactivo", required = true, order = 8)
    private Short routine;
    @ApiObjectField(name = "nameBranch", description = "Nombre de la sede", required = true, order = 9)
    private String nameBranch;

    public TestByLaboratory()
    {
        test = new TestBasic();
    }

    public TestByLaboratory(TestBasic test)
    {
        this.test = test;
    }

    public TestBasic getTest()
    {
        return test;
    }

    public void setTest(TestBasic test)
    {
        this.test = test;
    }

    public Integer getIdBranch()
    {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch)
    {
        this.idBranch = idBranch;
    }

    public Integer getIdLaboratory()
    {
        return idLaboratory;
    }

    public void setIdLaboratory(Integer idLaboratory)
    {
        this.idLaboratory = idLaboratory;
    }

    public Integer getCodeLaboratory()
    {
        return codeLaboratory;
    }

    public void setCodeLaboratory(Integer codeLaboratory)
    {
        this.codeLaboratory = codeLaboratory;
    }

    public Short getGroupType()
    {
        return groupType;
    }

    public void setGroupType(Short groupType)
    {
        this.groupType = groupType;
    }

    public Short getUrgency()
    {
        return urgency;
    }

    public void setUrgency(Short urgency)
    {
        this.urgency = urgency;
    }

    public Short getRoutine()
    {
        return routine;
    }

    public void setRoutine(Short routine)
    {
        this.routine = routine;
    }

    public String getNameLaboratory()
    {
        return nameLaboratory;
    }

    public void setNameLaboratory(String nameLaboratory)
    {
        this.nameLaboratory = nameLaboratory;
    }

    public String getNameBranch()
    {
        return nameBranch;
    }

    public void setNameBranch(String nameBranch)
    {
        this.nameBranch = nameBranch;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.test);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TestByLaboratory other = (TestByLaboratory) obj;
        if (!Objects.equals(this.test, other.test))
        {
            return false;
        }
        return true;
    }

}
