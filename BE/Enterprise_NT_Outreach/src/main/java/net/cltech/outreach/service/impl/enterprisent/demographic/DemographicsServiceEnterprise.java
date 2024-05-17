package net.cltech.outreach.service.impl.enterprisent.demographic;

import net.cltech.outreach.dao.interfaces.demographic.DemographicsDao;
import net.cltech.outreach.domain.demographic.Demographic;
import net.cltech.outreach.domain.demographic.QueryDemographic;
import net.cltech.outreach.domain.masters.configuration.Configuration;
import net.cltech.outreach.service.interfaces.demographic.DemographicsService;
import net.cltech.outreach.service.interfaces.masters.configuration.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de integración para Enterprise NT.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 29/01/2020
 * @see Creacion
 */
@Service
public class DemographicsServiceEnterprise implements DemographicsService
{

    @Autowired
    private DemographicsDao dao;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public Demographic queryDemographic() throws Exception
    {

        Configuration config = configurationService.get("DemograficoConsultaWeb");

        String valueID = config.getValue();
        int number = 0;
        if (valueID.isEmpty())
        {
            number = 0;
        } else
        {
            number = Integer.parseInt(valueID);
        }
        return dao.getDemographic(number);

    }
    
    @Override
    public QueryDemographic getQueryDemographic(Integer id) throws Exception
    {
        return dao.getQueryDemographic(id);
    }

}
