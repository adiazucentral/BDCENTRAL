package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de una muestra desde Home Bound
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "LIS",
        name = "Muestra",
        description = "Datos de una muestra desde Home Bound"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleHomeBound
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "container", description = "Recipiente", required = true, order = 4)
    private ContainerHomeBound container;
    @ApiObjectField(name = "takenUser", description = "Usuario que toma la muestra", order = 5)
    private Integer takenUser;
    @ApiObjectField(name = "takenDate", description = "Fecha de toma de la muestra", order = 6)
    private Date takenDate;
    @ApiObjectField(name = "print", description = "Si se imprime", order = 7)
    private boolean print;
    @ApiObjectField(name = "count", description = "Cantidad de veces que se va a imprimir", order = 8)
    private Integer count;
    @ApiObjectField(name = "info", description = "Informacion de manejo", order = 9)
    private String info;
    @ApiObjectField(name = "date", description = "Fecha de la solicitud de la toma de muestra", required = false, order = 10)
    private Timestamp date;

    public SampleHomeBound()
    {
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ContainerHomeBound getContainer()
    {
        return container;
    }

    public void setContainer(ContainerHomeBound container)
    {
        this.container = container;
    }

    public Integer getTakenUser()
    {
        return takenUser;
    }

    public void setTakenUser(Integer takenUser)
    {
        this.takenUser = takenUser;
    }

    public Date getTakenDate()
    {
        return takenDate;
    }

    public void setTakenDate(Date takenDate)
    {
        this.takenDate = takenDate;
    }

    public boolean isPrint()
    {
        return print;
    }

    public void setPrint(boolean print)
    {
        this.print = print;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public Timestamp getDate()
    {
        return date;
    }

    public void setDate(Timestamp date)
    {
        this.date = date;
    }

}
