/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.service.impl.securitynt.masters.demographic;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.cltech.securitynt.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.securitynt.domain.masters.demographic.DemographicWebQuery;
import net.cltech.securitynt.service.interfaces.integration.IntegrationService;
import net.cltech.securitynt.service.interfaces.masters.demographic.DemographicWebQueryService;
import net.cltech.securitynt.tools.DateTools;
import net.cltech.securitynt.tools.Tools;
import net.cltech.securitynt.tools.log.jobs.SchedulerLog;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adiaz
 */
public class DemographicWebQueySegurityNT implements DemographicWebQueryService{
    
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private IntegrationService integrationService;
    
    @Override
    public void deactivateUsersDemographicwebquery(List<DemographicWebQuery> users, String token) throws Exception {
        final Date currentDate = DateTools.getDateWithoutTime(new Date());
        final int daysValidationInactivity = Integer.parseInt(configurationDao.get("DaysValidationInactivity").getValue());
        final String url = configurationDao.get("UrlLIS").getValue() + "/api/demographicwebquery/deactivate";
        users.stream()
                .filter(u -> u.getDateOfLastEntry() != null)
                .forEach(u
                        -> {
                    if (DateTools.getDateWithoutTime(DateTools.changeDate(u.getDateOfLastEntry(), Calendar.DAY_OF_YEAR, daysValidationInactivity)).before(currentDate)) {
                        try {
                            integrationService.putVoid(Tools.jsonObject(u), url, token);
                        } catch (JsonProcessingException ex) {
                            SchedulerLog.error("No se pudo desactivar usaurio");
                            SchedulerLog.error(ex);
                        } catch (Exception ex) {
                            SchedulerLog.error("No se pudo desactivar usaurio");
                            SchedulerLog.error(ex);
                        }
                    }
                });
    }
    
    
}
