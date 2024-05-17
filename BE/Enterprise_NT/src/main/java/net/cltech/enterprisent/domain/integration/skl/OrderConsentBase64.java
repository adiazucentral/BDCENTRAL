package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa una orden con examenes con base64
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 09/06/2020
 * @see Creación
 */
@ApiObject(
        group = "SKL",
        name = "Orden Informado con base64",
        description = "Orden con consentimiento informado con base64"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderConsentBase64
{

    @ApiObjectField(name = "oder", description = "Id de la orden", required = true, order = 1)
    private long oder;
    @ApiObjectField(name = "testConsent", description = "Listado de pruebas", required = true, order = 2)
    private List<TestConsentBase64> testConsentBase64;
    @ApiObjectField(name = "history", description = "Historia", required = true, order = 3)
    private String history;
    @ApiObjectField(name = "idPatient", description = "idPatient", required = true, order = 4)
    private Integer idPatient;
    @ApiObjectField(name = "names", description = "Nombre", required = true, order = 4)
    private String names;
    @ApiObjectField(name = "subnames", description = "Sub nombre", required = true, order = 5)
    private String subnames;
    @ApiObjectField(name = "photo", description = "Foto del Paciente", required = true, order = 6)
    private String photo;

    public OrderConsentBase64()
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

    public List<TestConsentBase64> getTestConsentBase64()
    {
        return testConsentBase64;
    }

    public void setTestConsentBase64(List<TestConsentBase64> testConsentBase64)
    {
        this.testConsentBase64 = testConsentBase64;
    }
    
    public Integer getIdPatient()
    {
        return idPatient;
    }

    public void setIdPatient(Integer idPatient)
    {
        this.idPatient = idPatient;
    }


    public String getHistory()
    {
        return history;
    }

    public void setHistory(String history)
    {
        this.history = history;
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

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

}
