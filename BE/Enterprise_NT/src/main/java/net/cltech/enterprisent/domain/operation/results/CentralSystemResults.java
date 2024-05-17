package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase con la que obtendremos el listado de ordenes
 * que no se han enviado al Sistema Central
 * 
 * @version
 * @author Julian
 * @since 23/07/2020
 * @see Creación
 */

@ApiObject(
        group = "Operación - Resultados",
        name = "Resultados No Enviados Al Sistema Central",
        description = "Representa el filtro de los resultados que no se han enviado al sistema central"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CentralSystemResults
{
    @ApiObjectField(name = "startDate", description = "Fecha inicial", required = false, order = 1)
    private String startDate;
    @ApiObjectField(name = "endDate", description = "Fecha final", required = false, order = 2)
    private String endDate;
    @ApiObjectField(name = "includeTests", description = "Ids de los examenes a incluir de la busqueda", required = false, order = 3)
    private String includeTests;
    @ApiObjectField(name = "centralSystem", description = "Sistema central con el que verificaremos el envio de los resultados", required = false, order = 4)
    private int centralSystem;
    @ApiObjectField(name = "idTest", description = "Id del examen", required = false, order = 5)
    private int idTest;
    @ApiObjectField(name = "codeProfile", description = "Codigo del perfil al que pertenece ese examen", required = false, order = 6)
    private String codeProfile;
    @ApiObjectField(name = "idOrder", description = "Id de la orden", required = false, order = 7)
    private long idOrder;
    @ApiObjectField(name = "indicatore", description = "Indicador, 0 -> Orden no enviada, 1 -> Orden enviada", required = false, order = 8)
    private int indicatore;
    @ApiObjectField(name = "dateOfDispatch", description = "Fecha de envió de la orden al sistema central externo", required = false, order = 9)
    private Timestamp dateOfDispatch;

    public CentralSystemResults()
    {
    }
    
    public String getIncludeTests()
    {
        return includeTests;
    }

    public void setIncludeTests(String includeTests)
    {
        this.includeTests = includeTests;
    }
    
    public int getCentralSystem()
    {
        return centralSystem;
    }

    public void setCentralSystem(int centralSystem)
    {
        this.centralSystem = centralSystem;
    }

    public int getIdTest()
    {
        return idTest;
    }

    public void setIdTest(int idTest)
    {
        this.idTest = idTest;
    }

    public String getCodeProfile()
    {
        return codeProfile;
    }

    public void setCodeProfile(String codeProfile)
    {
        this.codeProfile = codeProfile;
    }

    public long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(long idOrder)
    {
        this.idOrder = idOrder;
    }

    public int getIndicatore()
    {
        return indicatore;
    }

    public void setIndicatore(int indicatore)
    {
        this.indicatore = indicatore;
    }

    public Timestamp getDateOfDispatch()
    {
        return dateOfDispatch;
    }

    public void setDateOfDispatch(Timestamp dateOfDispatch)
    {
        this.dateOfDispatch = dateOfDispatch;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
}
