package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa una orden con consentimiento informado
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 05/06/2020
 * @see Creación
 */
@ApiObject(
        group = "SKL",
        name = "Orden Informado",
        description = "Orden con consentimiento informado"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderConsent
{

    @ApiObjectField(name = "oder", description = "Id de la orden", required = true, order = 1)
    private long oder;
    @ApiObjectField(name = "testConsent", description = "Listado de pruebas", required = true, order = 2)
    private List<TestConsent> testConsent;
    @ApiObjectField(name = "history", description = "Historia", required = true, order = 3)
    private String history;
    @ApiObjectField(name = "idPatient", description = "idPatient", required = true, order = 4)
    private Integer idPatient;
    @ApiObjectField(name = "names", description = "Nombre", required = true, order = 5)
    private String names;
    @ApiObjectField(name = "subnames", description = "Sub nombre", required = true, order = 6)
    private String subnames;

    public OrderConsent()
    {
    }

    public long getOder()
    {
        return oder;
    }

    public void setOder(long oder)
    {
        this.oder = oder;
    }

    public List<TestConsent> getTestConsent()
    {
        return testConsent;
    }

    public void setTestConsent(List<TestConsent> testConsent)
    {
        this.testConsent = testConsent;
    }

    public String getHistory()
    {
        return history;
    }

    public void setHistory(String history)
    {
        this.history = history;
    }
    
    public Integer getIdPatient()
    {
        return idPatient;
    }

    public void setIdPatient(Integer idPatient)
    {
        this.idPatient = idPatient;
    }

    public String getNames()
    {
        return names;
    }

    public void setNames(String names)
    {
        this.names = names;
    }

    public String getSubnames()
    {
        return subnames;
    }

    public void setSubnames(String subnames)
    {
        this.subnames = subnames;
    }

}
