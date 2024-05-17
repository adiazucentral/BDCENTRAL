package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los tiempos entre estados de los resultados
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/02/2018
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Histograma Datos",
        description = "Representa datos para el histograma "
)
@JsonInclude(Include.NON_NULL)
public class HistogramData
{

    @ApiObjectField(name = "orderNumber", description = "Numero de la orden", required = false, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "id", description = "Id examen", required = false, order = 2)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo examen", required = false, order = 3)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre examen", required = false, order = 4)
    private String name;
    @ApiObjectField(name = "verifyDate", description = "Fecha verificación", required = false, order = 5)
    private Date verifyDate;
    @ApiObjectField(name = "verifyUser", description = "Usuario verifica", required = false, order = 6)
    private Integer verifyUser;
    @ApiObjectField(name = "validDate", description = "Fecha validado", required = false, order = 7)
    private Date validDate;
    @ApiObjectField(name = "validUser", description = "Usuario valida", required = false, order = 8)
    private Integer validUser;
    @ApiObjectField(name = "totalTime", description = "Tiempo transcurrido desde el ingreso hasta la validación", required = false, order = 9)
    private Long totalTime;
    @ApiObjectField(name = "codeService", description = "Codigo servicio", required = false, order = 3)
    private String codeService;
    @ApiObjectField(name = "nameService", description = "Nombre servicio", required = false, order = 4)
    private String nameService;
    @ApiObjectField(name = "bind", description = "Id clase ", required = false, order = 4)
    private Bind bind;

    public HistogramData()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Date getVerifyDate()
    {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate)
    {
        this.verifyDate = verifyDate;
    }

    public Integer getVerifyUser()
    {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser)
    {
        this.verifyUser = verifyUser;
    }

    public Date getValidDate()
    {
        return validDate;
    }

    public void setValidDate(Date validDate)
    {
        this.validDate = validDate;
    }

    public Integer getValidUser()
    {
        return validUser;
    }

    public void setValidUser(Integer validUser)
    {
        this.validUser = validUser;
    }

    public Long getTotalTime()
    {
        return totalTime;
    }

    public void setTotalTime(Long totalTime)
    {
        this.totalTime = totalTime;
    }

    public String getCodeService()
    {
        return codeService;
    }

    public void setCodeService(String codeService)
    {
        this.codeService = codeService;
    }

    public String getNameService()
    {
        return nameService;
    }

    public void setNameService(String nameService)
    {
        this.nameService = nameService;
    }

    public Bind getBind()
    {
        return bind;
    }

    public void setBind(Bind bind)
    {
        this.bind = bind;
    }

}
