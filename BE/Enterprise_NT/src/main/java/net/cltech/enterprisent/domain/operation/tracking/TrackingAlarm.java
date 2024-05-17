/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.tracking;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author adiaz
 */
public class TrackingAlarm {
    @ApiObjectField(name = "externalQuery", description = "si la orden pertenece a un servicio de consulta externa o no", required = false, order = 1)
    private boolean externalQuery;
    @ApiObjectField(name = "interview", description = "si la orden ya tiene una entrevista aplicada", required = false, order = 2)
    private boolean interview;
    
    public boolean getExternalQuery()
    {
        return externalQuery;
    }

    public void setExternalQuery(boolean externalQuery)
    {
        this.externalQuery = externalQuery;
    }
    
    public boolean getInterview()
    {
        return interview;
    }

    public void setInterview(boolean interview)
    {
        this.interview = interview;
    }
}
