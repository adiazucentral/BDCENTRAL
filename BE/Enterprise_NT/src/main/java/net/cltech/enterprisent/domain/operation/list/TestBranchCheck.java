package net.cltech.enterprisent.domain.operation.list;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de los examenes verificados en una sede
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 02/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Examenes verificados con sede de verifiacion",
        description = "Representa la informacion de los examenes verificados en una sede."
)
public class TestBranchCheck
{

    @ApiObjectField(name = "branchName", description = "Nombre de la sede", order = 1)
    private String branchName;
    @ApiObjectField(name = "branchId", description = "Id de la sede", order = 3)
    private Integer branchId;
    @ApiObjectField(name = "selected", description = "Seleccionado", order = 4)
    private boolean selected;

    public TestBranchCheck(String branchName, Integer branchId, boolean selected)
    {

        this.branchName = branchName;

        this.branchId = branchId;

        this.selected = selected;
    }

    public TestBranchCheck()
    {
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public Integer getBranchId()
    {
        return branchId;
    }

    public void setBranchId(Integer branchId)
    {
        this.branchId = branchId;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

}
