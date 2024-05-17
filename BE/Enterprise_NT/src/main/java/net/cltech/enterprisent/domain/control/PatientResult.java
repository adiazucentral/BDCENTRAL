package net.cltech.enterprisent.domain.control;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 *
 * @version 1.0.0
 * @author nmolina
 * @since 11/08/2020
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Resultados de paciente",
        description = "Representa los registro de resultados de examenes de pacientes"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientResult
{

    @ApiObjectField(name = "idOrden", description = "id Orden de resultados", required = true, order = 1)
    private Long idOrden;
    @ApiObjectField(name = "documentType", description = "Tipo de documento", required = true, order = 2)
    private int documentType;
    @ApiObjectField(name = "PatientRecord", description = "Historia del paciente", required = true, order = 3)
    private String patientRecord;
    @ApiObjectField(name = "FirstName", description = "Primer nombre del paciente", required = true, order = 4)
    private String firstName;
    @ApiObjectField(name = "SecondName", description = "Segundo nombre del paciente", required = true, order = 5)
    private String secondName;
    @ApiObjectField(name = "FirstLastName", description = "Primer apellido del paciente", required = true, order = 6)
    private String FirstLastName;
    @ApiObjectField(name = "SecondLastName", description = "Segundo apellido del paciente", required = true, order = 7)
    private String SecondLastName;
    @ApiObjectField(name = "idTest", description = "Id del examen del paciente", required = true, order = 8)
    private int idTest;
    @ApiObjectField(name = "results", description = "resultados del paciente", required = true, order = 9)
    private String results;

    public PatientResult()
    {
    }

    public Long getIdOrden()
    {
        return idOrden;
    }

    public void setIdOrden(Long idOrden)
    {
        this.idOrden = idOrden;
    }

    public int getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(int documentType)
    {
        this.documentType = documentType;
    }

    public String getPatientRecord()
    {
        return patientRecord;
    }

    public void setPatientRecord(String patientRecord)
    {
        this.patientRecord = patientRecord;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSecondName()
    {
        return secondName;
    }

    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }

    public String getFirstLastName()
    {
        return FirstLastName;
    }

    public void setFirstLastName(String FirstLastName)
    {
        this.FirstLastName = FirstLastName;
    }

    public String getSecondLastName()
    {
        return SecondLastName;
    }

    public void setSecondLastName(String SecondLastName)
    {
        this.SecondLastName = SecondLastName;
    }

    public int getIdTest()
    {
        return idTest;
    }

    public void setIdTest(int idTest)
    {
        this.idTest = idTest;
    }

    public String getResults()
    {
        return results;
    }

    public void setResults(String results)
    {
        this.results = results;
    }

}
