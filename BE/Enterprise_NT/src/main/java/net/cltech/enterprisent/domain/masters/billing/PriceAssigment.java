package net.cltech.enterprisent.domain.masters.billing;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la configuracion precios de examenes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/08/217
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Assignación Precios",
        description = "Objeto con los precios de los examenes"
)
public class PriceAssigment extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la tarifa", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "idValid", description = "Id de la vigencía", required = true, order = 2)
    private Integer idValid;
    @ApiObjectField(name = "test", description = "Examen para actualizar", required = true, order = 3)
    private TestPrice test = new TestPrice();
    @ApiObjectField(name = "importTests", description = "Lista de examenes para importar", required = true, order = 4)
    private List<TestPrice> importTests = new ArrayList<>();
    @ApiObjectField(name = "centralSystem", description = "Id sistema central de homologación <br> 0 - Código LIS ", required = true, order = 5)
    private Integer centralSystem;
    @ApiObjectField(name = "nameValid", description = "Nombre de la vigencia", required = false, order = 6)
    private String nameValid;
    @ApiObjectField(name = "nameRate", description = "Nombre de la tarifa", required = false, order = 7)
    private String nameRate;
    @ApiObjectField(name = "patientPercentage", description = "Porcentaje del paciente", required = false, order = 8)
    private Double patientPercentage;

    public PriceAssigment()
    {
    }

    public PriceAssigment(Integer id, Integer idValid)
    {
        this.id = id;
        this.idValid = idValid;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getIdValid()
    {
        return idValid;
    }

    public void setIdValid(Integer idValid)
    {
        this.idValid = idValid;
    }

    public TestPrice getTest()
    {
        return test;
    }

    public void setTest(TestPrice test)
    {
        this.test = test;
    }

    public List<TestPrice> getImportTests()
    {
        return importTests;
    }

    public void setImportTests(List<TestPrice> importTests)
    {
        this.importTests = importTests;
    }

    public Integer getCentralSystem()
    {
        return centralSystem;
    }

    public void setCentralSystem(Integer centralSystem)
    {
        this.centralSystem = centralSystem;
    }

    public String getNameValid()
    {
        return nameValid;
    }

    public void setNameValid(String nameValid)
    {
        this.nameValid = nameValid;
    }

    public String getNameRate()
    {
        return nameRate;
    }

    public void setNameRate(String nameRate)
    {
        this.nameRate = nameRate;
    }

    public Double getPatientPercentage()
    {
        return patientPercentage;
    }

    public void setPatientPercentage(Double patientPercentage)
    {
        this.patientPercentage = patientPercentage;
    }
}
