package net.cltech.enterprisent.domain.masters.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Gradillas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/05/2018
 * @see Creacion
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Gradilla",
        description = "Representa gradillas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rack extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "state", description = "Estado de la gradilla 0 - Abierta, 1 - Cerrada, 2 - Desechada", required = true, order = 4)
    private Integer state;
    @ApiObjectField(name = "branch", description = "Sede a la que pertenece la gradillas", required = true, order = 5)
    private Branch branch;
    @ApiObjectField(name = "row", description = "Numero de filas", required = true, order = 6)
    private Integer row;
    @ApiObjectField(name = "column", description = "Numero de columnas", required = true, order = 7)
    private Integer column;
    @ApiObjectField(name = "type", description = "Tipo de gradilla (1-General, 2-Pendiente,3-Confidencial)", required = true, order = 8)
    private Integer type;
    @ApiObjectField(name = "refrigerator", description = "Nevera donde se ubica la gradilla", required = true, order = 9)
    private Refrigerator refrigerator;
    @ApiObjectField(name = "floor", description = "Piso de la nevera", required = true, order = 10)
    private String floor;
    @ApiObjectField(name = "updateUser", description = "Usuario que realiza el cierre", required = true, order = 11)
    private AuthorizedUser updateUser;
    @ApiObjectField(name = "positions", description = "Usuario que realiza el cierre", required = true, order = 12)
    private List<RackDetail> positions;
    @ApiObjectField(name = "storageDate", description = "Fecha de almacenamiento de la primera muestra", required = true, order = 13)
    private Date storageDate;
    @ApiObjectField(name = "storageDays", description = "Tiempo transcurido de almacenamiento (días)", required = true, order = 14)
    private Long storageDays;
    @ApiObjectField(name = "reusable", description = "La gradilla es reutilizable (0 - No, 1 - Si)", required = true, order = 15)
    private boolean reusable;
    @ApiObjectField(name = "closeDate", description = "Fecha de cierre", required = true, order = 16)
    private Date closeDate;

    public Rack()
    {
    }

    public Rack(Integer id)
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Integer getRow()
    {
        return row;
    }

    public void setRow(Integer row)
    {
        this.row = row;
    }

    public Integer getColumn()
    {
        return column;
    }

    public void setColumn(Integer column)
    {
        this.column = column;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Refrigerator getRefrigerator()
    {
        return refrigerator;
    }

    public void setRefrigerator(Refrigerator refrigerator)
    {
        this.refrigerator = refrigerator;
    }

    public String getFloor()
    {
        return floor;
    }

    public void setFloor(String floor)
    {
        this.floor = floor;
    }

    public AuthorizedUser getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(AuthorizedUser updateUser)
    {
        this.updateUser = updateUser;
    }

    public List<RackDetail> getPositions()
    {
        return positions;
    }

    public void setPositions(List<RackDetail> positions)
    {
        this.positions = positions;
    }

    public Date getStorageDate()
    {
        return storageDate;
    }

    public void setStorageDate(Date storageDate)
    {
        this.storageDate = storageDate;
    }

    public Long getStorageDays()
    {
        return storageDays;
    }

    public void setStorageDays(Long storageDays)
    {
        this.storageDays = storageDays;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }
    
    public boolean getReusable()
    {
        return reusable;
    }

    public void setReusable(boolean reusable)
    {
        this.reusable = reusable;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Rack other = (Rack) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
