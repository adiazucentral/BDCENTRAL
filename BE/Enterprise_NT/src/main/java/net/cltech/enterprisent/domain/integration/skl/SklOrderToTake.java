package net.cltech.enterprisent.domain.integration.skl;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de Respuesta requerido por SKL 
 * para su integracion con NT
 * 
 * @version 1.0.0
 * @author Julian
 * @since 28/05/20
 * @see Creación
 */

@ApiObject(
        group = "SKL",
        name = "Orden por toma de muestra",
        description = "Orden por toma de muestra que se le enviará a SKL según lo requiera para su integración con NT"
)
public class SklOrderToTake
{
    @ApiObjectField(name = "order", description = "Id de la orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "idPatient", description = "Identificador en la base de datos", required = true, order = 2)
    private int idPatient;
    @ApiObjectField(name = "patientId", description = "Historia del paciente", required = true, order = 3)
    private String patientId;
    @ApiObjectField(name = "patient", description = "Nombres y apellidos del paciente", required = true, order = 4)
    private String patient;
    @ApiObjectField(name = "sex", description = "Genero del paciente", required = true, order = 5)
    private String sex;
    @ApiObjectField(name = "location", description = "Ubicación hospitalaria", required = true, order = 6)
    private String location;

    public SklOrderToTake()
    {
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getIdPatient()
    {
        return idPatient;
    }

    public void setIdPatient(int idPatient)
    {
        this.idPatient = idPatient;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getPatient()
    {
        return patient;
    }

    public void setPatient(String patient)
    {
        this.patient = patient;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
}
