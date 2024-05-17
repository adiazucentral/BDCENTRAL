package net.cltech.enterprisent.domain.operation.list;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de los examenes verificados en una sede en un rango
 * de ordenes
 *
 * @version 1.0.0
 * @author bvalero
 * @since 27/05/2020
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Examenes verificados con sede de verifiacion por orden",
        description = "Representa la informacion de los examenes verificados en una sede en un rango de ordenes."
)
public class ResponsTestCheckByBranch
{

    @ApiObjectField(name = "order", description = "Numero de orden", order = 1)
    private Long order;

    @ApiObjectField(name = "testBranch", description = "Objeto de examenes verificados con sede de verificacion", order = 2)
    private List<TestBranchCheck> testBranch;

    public ResponsTestCheckByBranch(Long order, List<TestBranchCheck> testBranch)
    {
        this.order = order;
        this.testBranch = testBranch;
    }

    public ResponsTestCheckByBranch()
    {
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public List<TestBranchCheck> getTestBranch()
    {
        return testBranch;
    }

    public void setTestBranch(List<TestBranchCheck> testBranch)
    {
        this.testBranch = testBranch;
    }

    @Override
    public String toString()
    {
        return "ResponsTestCheckByBranch{" + "order=" + order + ", testBranch=" + testBranch + '}';
    }
}
