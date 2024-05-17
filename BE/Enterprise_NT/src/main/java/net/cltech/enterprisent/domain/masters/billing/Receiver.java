/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.billing;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Receptor
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Receptor",
        description = "Muestra informacion del maestro Receptor que usa el API"
)
public class Receiver extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del receptor", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del receptor", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "applicationReceiverCode", description = "Application Receiver Code", required = true, order = 3)
    private String applicationReceiverCode;
    @ApiObjectField(name = "receiverID", description = "receiver ID", required = true, order = 4)
    private String receiverID;
    @ApiObjectField(name = "interchangeReceiver", description = "Interchange Receiver", required = true, order = 5)
    private String interchangeReceiver;
    @ApiObjectField(name = "state", description = "Estado del receptor", required = true, order = 6)
    private boolean state;

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

    public String getApplicationReceiverCode()
    {
        return applicationReceiverCode;
    }

    public void setApplicationReceiverCode(String applicationReceiverCode)
    {
        this.applicationReceiverCode = applicationReceiverCode;
    }

    public String getReceiverID()
    {
        return receiverID;
    }

    public void setReceiverID(String receiverID)
    {
        this.receiverID = receiverID;
    }

    public String getInterchangeReceiver()
    {
        return interchangeReceiver;
    }

    public void setInterchangeReceiver(String interchangeReceiver)
    {
        this.interchangeReceiver = interchangeReceiver;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

}
