package net.cltech.enterprisent.domain.operation.tracking;


import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.jsondoc.core.pojo.ApiVisibility;

/**
 * Representa un examen que sera retomado
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 04/03/2020
 * @see Creacion
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Trazabilidad de la Muestra",
        description = "Muestra la Trazabilidad de la muestra que usa el API",
        visibility = ApiVisibility.PRIVATE
)
public class TestSampleTake
{

    @ApiObjectField(name = "order", description = "Orden", required = false, order = 1)
    private Long order;
    @ApiObjectField(name = "sample", description = "Muestra", required = false, order = 2)
    private String sample;
    @ApiObjectField(name = "testSampleTakeTracking", description = "testTracking", required = false, order = 3)
    private List<TestSampleTakeTracking> testSampleTakeTracking;

    public TestSampleTake(Long order, String sample, List<TestSampleTakeTracking> testSampleTakeTracking)
    {
        this.order = order;
        this.sample = sample;
        this.testSampleTakeTracking =  testSampleTakeTracking;
    }

    public TestSampleTake()
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

    public String getSample()
    {
        return sample;
    }

    public void setSample(String sample)
    {
        this.sample = sample;
    }

    public List<TestSampleTakeTracking> getTestSampleTakeTracking()
    {
        return testSampleTakeTracking;
    }

    public void setTestSampleTakeTracking(List<TestSampleTakeTracking> testSampleTakeTracking)
    {
        this.testSampleTakeTracking = testSampleTakeTracking;
    }

}
