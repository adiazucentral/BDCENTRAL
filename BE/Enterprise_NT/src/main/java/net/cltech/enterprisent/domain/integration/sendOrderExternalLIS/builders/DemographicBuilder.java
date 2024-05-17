/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.builders;

import java.util.List;
import net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.buildersGeneral.DemographicsBuilders;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;

/**
 *
 * @author hpoveda
 */
public class DemographicBuilder implements DemographicsBuilders
{

    private DemographicValue demographicValue;
    
    public DemographicBuilder()
    {
        this.demographicValue = new DemographicValue();
    }
    
    @Override
    public DemographicsBuilders setIdDemographic(int idDemographic)
    {
        this.demographicValue.setIdDemographic(idDemographic);
        return this;
    }
    
    @Override
    public DemographicsBuilders setDemographic(String demographic)
    {
        this.demographicValue.setDemographic(demographic);
        return this;
    }
    
    @Override
    public DemographicsBuilders setEncoded(boolean encoded)
    {
        this.demographicValue.setEncoded(encoded);
        return this;
    }
    
    @Override
    public DemographicsBuilders setNotCodifiedValue(String notCodifiedValue)
    {
        this.demographicValue.setNotCodifiedValue(notCodifiedValue);
        return this;
    }
    
    @Override
    public DemographicsBuilders setCodifiedId(Integer codifiedId)
    {
        this.demographicValue.setCodifiedId(codifiedId);
        return this;
    }
    
    @Override
    public DemographicsBuilders setCodifiedCode(String codifiedCode)
    {
        this.demographicValue.setCodifiedCode(codifiedCode);
        return this;
    }
    
    @Override
    public DemographicsBuilders setHomologationCodeCentralSystem(List<String> homologationCodeCentralSystem)
    {
        this.demographicValue.setHomologationCodeCentralSystem(homologationCodeCentralSystem);
        return this;
    }
    
}
