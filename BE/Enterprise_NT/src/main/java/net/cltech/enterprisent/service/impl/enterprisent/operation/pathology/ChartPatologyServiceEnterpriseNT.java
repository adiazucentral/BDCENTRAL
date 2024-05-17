/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SampleCaseteDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.CaseteTrackingDao;
import net.cltech.enterprisent.domain.operation.pathology.ChartPathology;
import net.cltech.enterprisent.service.interfaces.operation.pathology.ChartPathologyService;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de las graficas utilizadas en patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 22/07/2021
 * @see Creaciòn
 */
@Service
public class ChartPatologyServiceEnterpriseNT implements ChartPathologyService
{
    @Autowired
    private SampleCaseteDao sampleCaseteDao;
    @Autowired
    private CaseteTrackingDao caseteTrackingDao;
    
    @Override
    public List<ChartPathology> getChartTissueProcessor() throws Exception
    {
        List<ChartPathology> list = new ArrayList<>();
        
        ChartPathology pending = new ChartPathology();
        pending.setKey("pending");
        pending.setValue(sampleCaseteDao.getCountCasetes(Constants.TISSUEPROCESSOR_PENDING));
        list.add(pending);
        
        ChartPathology process = new ChartPathology();
        process.setKey("process");
        process.setValue(sampleCaseteDao.getCountCasetes(Constants.TISSUEPROCESSOR_PROCESS));
        list.add(process);
        
        ChartPathology casetesByStudyType = new ChartPathology();
        casetesByStudyType.setKey("casetesByStudyType");
        casetesByStudyType.setData(sampleCaseteDao.getCountCasetesByStudyType(Constants.TISSUEPROCESSOR_PROCESS));
        list.add(casetesByStudyType);
        
        ChartPathology casetesByTime = new ChartPathology();
        casetesByTime.setKey("casetesByTime");
        casetesByTime.setData(sampleCaseteDao.getCountCasetesByTime(Constants.TISSUEPROCESSOR_PROCESS));
        list.add(casetesByTime);
        
        Calendar cinit = Calendar.getInstance();   
        cinit.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat finit = new SimpleDateFormat("yyyy-MM-dd");
        String  init = finit.format(cinit.getTime());
        
        Date today = new Date();  
        Calendar cend = Calendar.getInstance();  
        cend.setTime(today); 
        
        cend.add(Calendar.MONTH, 1);  
        cend.set(Calendar.DAY_OF_MONTH, 1);  
        cend.add(Calendar.DATE, -1);  

        Date lastDayOfMonth = cend.getTime();  

        DateFormat fend = new SimpleDateFormat("yyyy-MM-dd"); 
        String end = fend.format(lastDayOfMonth);
         
        ChartPathology monthly = new ChartPathology();
        monthly.setKey("monthly");
        monthly.setValue(caseteTrackingDao.getCount(Constants.TISSUEPROCESSOR_DONE, init, end));
        list.add(monthly);
        
        DateFormat ftoday = new SimpleDateFormat("yyyy-MM-dd");  
        String now = ftoday.format(today);
        
        ChartPathology daily = new ChartPathology();
        daily.setKey("daily");
        daily.setValue(caseteTrackingDao.getCount(Constants.TISSUEPROCESSOR_DONE, now, now));
        list.add(daily);
        
        return list;
    }
}
