package net.cltech.enterprisent.domain.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Acta de desecho
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/05/2018
 * @see Creacion
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Acta Desecho",
        description = "Representa Actas de desecho"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisposalCertificate
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "description", description = "Descripción", required = true, order = 2)
    private String description;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "closed", description = "Indica que el acta se encuentra cerrada", required = true, order = 4)
    private boolean closed;
    @ApiObjectField(name = "creationDate", description = "Fecha de creación del acta", required = true, order = 5)
    private Date creationDate;
    @ApiObjectField(name = "creationUser", description = "Usuario que crea el acta", required = true, order = 6)
    private AuthorizedUser creationUser;
    @ApiObjectField(name = "disposalDate", description = "Fecha de desecho", required = true, order = 7)
    private Date disposalDate;
    @ApiObjectField(name = "disposalUser", description = "Usuario que desecha", required = true, order = 8)
    private AuthorizedUser disposalUser;
    @ApiObjectField(name = "type", description = "Tipo de acta 1 - Gradillas, 2 - Muestras", required = true, order = 9)
    private Integer type;
    @ApiObjectField(name = "racks", description = "Lista de gradillas para adicionar al acta", required = true, order = 10)
    private List<Integer> racks;
//    @ApiObjectField(name = "position", description = "Posicion para asignar posicion al acta de desecho", required = false, order = 11)
    private RackDetail position;
//    @ApiObjectField(name = "positions", description = "Lista el contenido del acta de desecho", required = false, order = 12)
    private List<RackDetail> positions;

    public DisposalCertificate()
    {
    }

    public DisposalCertificate(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isClosed()
    {
        return closed;
    }

    public void setClosed(boolean closed)
    {
        this.closed = closed;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public AuthorizedUser getCreationUser()
    {
        return creationUser;
    }

    public void setCreationUser(AuthorizedUser creationUser)
    {
        this.creationUser = creationUser;
    }

    public Date getDisposalDate()
    {
        return disposalDate;
    }

    public void setDisposalDate(Date disposalDate)
    {
        this.disposalDate = disposalDate;
    }

    public AuthorizedUser getDisposalUser()
    {
        return disposalUser;
    }

    public void setDisposalUser(AuthorizedUser disposalUser)
    {
        this.disposalUser = disposalUser;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public List<Integer> getRacks()
    {
        return racks;
    }

    public void setRacks(List<Integer> racks)
    {
        this.racks = racks;
    }

    public RackDetail getPosition()
    {
        return position;
    }

    public void setPosition(RackDetail position)
    {
        this.position = position;
    }

    public List<RackDetail> getPositions()
    {
        return positions;
    }

    public void setPositions(List<RackDetail> positions)
    {
        this.positions = positions;
    }

}
