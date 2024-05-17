package net.cltech.enterprisent.domain.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa con información para el almacenamiento de la muestra
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/06/2018
 * @see Creación
 */
//@ApiObject(
//        group = "Trazabilidad",
//        name = "Detalle Gradilla",
//        description = "Detalle del almacenamiento de gradillas"
//)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RackDetail
{

    @ApiObjectField(name = "position", description = "Posicion de la gradilla", required = false, order = 1)
    private String position;
    @ApiObjectField(name = "sample", description = "Muestra", required = false, order = 2)
    private Sample sample;
    @ApiObjectField(name = "rack", description = "Id de la gradilla", required = false, order = 3)
    private Rack rack;
    @ApiObjectField(name = "order", description = "Numero de orden", required = false, order = 4)
    private Long order;
    @ApiObjectField(name = "registDate", description = "Fecha de insercion", required = false, order = 5)
    private Date registDate;
    @ApiObjectField(name = "updateDate", description = "Fecha de modificacion", required = false, order = 6)
    private Date updateDate;
    @ApiObjectField(name = "insert", description = "Si el tubo esta almacenado, false si fue retirado", required = false, order = 7)
    private boolean insert;
    @ApiObjectField(name = "registUser", description = "Usuario que registra", required = false, order = 8)
    private AuthorizedUser registUser;
    @ApiObjectField(name = "updateUser", description = "Usuario que actualiza", required = false, order = 9)
    private AuthorizedUser updateUser;
    @ApiObjectField(name = "full", description = "Al insertar identifica si la gradilla se encuentra llena", required = false, order = 7)
    private boolean full;
    @ApiObjectField(name = "branch", description = "Sede en la que esta la gradilla", required = false, order = 11)
    private Branch branch;
    @ApiObjectField(name = "validStorageDate", description = "Fecha en la que finaliza el almacenamiento de la muestra", required = false, order = 6)
    private Date validStorageDate;
    @ApiObjectField(name = "storedDays", description = "Tiempo almacenado de la muestra (dias)", required = false, order = 13)
    private Long storedDays;
    @ApiObjectField(name = "patient", description = "Informacion del paciente", required = false, order = 11)
    private Patient patient;
//    @ApiObjectField(name = "certificate", description = "Acta de desecho", required = false, order = 12)
    private DisposalCertificate certificate;
    @ApiObjectField(name = "discard", description = "Indica si la muestra ya fue desechada", required = false, order = 12)
    private boolean discard;

    public RackDetail(String position)
    {
        this.position = position;
    }

    public RackDetail()
    {
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public Rack getRack()
    {
        return rack;
    }
    
    public void setRack(Rack rack)
    {
        this.rack = rack;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Date getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(Date registDate)
    {
        this.registDate = registDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    public boolean isInsert()
    {
        return insert;
    }
    
    public boolean getInsert()
    {
        return insert;
    }
    
    public void setInsert(boolean insert)
    {
        this.insert = insert;
    }
    
    public AuthorizedUser getRegistUser()
    {
        return registUser;
    }

    public void setRegistUser(AuthorizedUser registUser)
    {
        this.registUser = registUser;
    }

    public AuthorizedUser getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(AuthorizedUser updateUser)
    {
        this.updateUser = updateUser;
    }

    public boolean isFull()
    {
        return full;
    }

    public void setFull(boolean full)
    {
        this.full = full;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public Date getValidStorageDate()
    {
        return validStorageDate;
    }

    public void setValidStorageDate(Date validStorageDate)
    {
        this.validStorageDate = validStorageDate;
    }

    public Long getStoredDays()
    {
        return storedDays;
    }

    public void setStoredDays(Long storedDays)
    {
        this.storedDays = storedDays;
    }

    public Patient getPatient()
    {
        return patient;
    }
    
    public void setPatient(Patient patient)
    {
        this.patient = patient;
    }

    public DisposalCertificate getCertificate()
    {
        return certificate;
    }

    public void setCertificate(DisposalCertificate certificate)
    {
        this.certificate = certificate;
    }

    public boolean isDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }
 
    public void auditClean()
    {
        this.getSample().setUser(null);
        this.getSample().setContainer(null);
        this.getSample().setSubSamples(null);
        this.getSample().setTests(null);
        this.getSample().setSampleTrackings(null);
        this.getRack().setUser(null);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        //hash = 71 * hash + this.position;
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
        final RackDetail other = (RackDetail) obj;
        if (this.position != other.position)
        {
            return false;
        }
        return true;
    }

}
