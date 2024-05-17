/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.widgets;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.StatisticDao;
import net.cltech.enterprisent.dao.interfaces.operation.widgets.WidgetDao;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import net.cltech.enterprisent.domain.operation.widgets.TurnWidgetInfo;
import net.cltech.enterprisent.domain.operation.widgets.WidgetEntry;
import net.cltech.enterprisent.domain.operation.widgets.WidgetOrderEntry;
import net.cltech.enterprisent.domain.operation.widgets.WidgetSample;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSigaService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.widgets.WidgetService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de los widgets para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/12/2017
 * @see Creacion
 */
@Service
public class WidgetServiceEnterpriseNT implements WidgetService
{

    @Autowired
    private WidgetDao dao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationSigaService integrationSigaService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StatisticDao statisticDao;

    @Override
    public WidgetSample getWidgetSample(int idBranch) throws Exception
    {
        return dao.getWidgetValueToday(DateTools.dateToNumber(new Date()), idBranch);
    }

    @Override
    public void addOrderedWidget(int idBranch) throws Exception
    {
        int dateNumber = DateTools.dateToNumber(new Date());
        boolean isCreatedWidgetToday = dao.validateWidgetValueToday(dateNumber, idBranch);
        if (isCreatedWidgetToday)
        {
            dao.addOrderedWidget(dateNumber, idBranch);
        } else
        {
            dao.createWidgetValueToday(dateNumber, idBranch);
            dao.addOrderedWidget(dateNumber, idBranch);
        }
    }

    @Override
    public void addVerifiedWidget(int idBranch) throws Exception
    {
        int dateNumber = DateTools.dateToNumber(new Date());
        boolean isCreatedWidgetToday = dao.validateWidgetValueToday(dateNumber, idBranch);
        if (isCreatedWidgetToday)
        {
            dao.addVerifiedWidget(dateNumber, idBranch);
        } else
        {
            dao.createWidgetValueToday(dateNumber, idBranch);
            dao.addVerifiedWidget(dateNumber, idBranch);
        }
    }

    @Override
    public void addRejectedWidget(int idBranch) throws Exception
    {
        int dateNumber = DateTools.dateToNumber(new Date());
        boolean isCreatedWidgetToday = dao.validateWidgetValueToday(dateNumber, idBranch);
        if (isCreatedWidgetToday)
        {
            dao.addRejectedWidget(dateNumber, idBranch);
        } else
        {
            dao.createWidgetValueToday(dateNumber, idBranch);
            dao.addRejectedWidget(dateNumber, idBranch);
        }
    }

    @Override
    public void addRetakedWidget(int idBranch) throws Exception
    {
        int dateNumber = DateTools.dateToNumber(new Date());
        boolean isCreatedWidgetToday = dao.validateWidgetValueToday(dateNumber, idBranch);
        if (isCreatedWidgetToday)
        {
            dao.addRetakedWidget(dateNumber, idBranch);
        } else
        {
            dao.createWidgetValueToday(dateNumber, idBranch);
            dao.addRetakedWidget(dateNumber, idBranch);
        }
    }

    @Override
    public void delayedWidget(int idBranch) throws Exception
    {

    }

    @Override
    public void expiredWidget() throws Exception
    {
        long init = System.currentTimeMillis();

        int dateNumber = DateTools.dateToNumber(new Date());
        List<SampleTracking> sampleTaken = dao.getSampleTakeToday();
        List<Integer> idBranches = sampleTaken.stream().map(sample -> sample.getBranch().getId()).distinct().collect(Collectors.toList());

        for (Integer idBranch : idBranches)
        {
            int quantity = 0;
            for (SampleTracking sampleTake : sampleTaken)
            {
                if (sampleTake.getBranch().getId().equals(idBranch))
                {
                    sampleTake.setResultTests(sampleTake.getResultTests().stream()
                            .filter(result -> result.getSampleState() != LISEnum.ResultSampleState.REJECTED.getValue() && result.getSampleState() != LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                            .collect(Collectors.toList()));

                    int quantityResult = sampleTake.getResultTests().stream()
                            .filter(result -> result.getState() != LISEnum.ResultTestState.ORDERED.getValue() && result.getState() != LISEnum.ResultTestState.RERUN.getValue())
                            .collect(Collectors.toList()).size();

                    if (sampleTake.getResultTests().size() > 0)
                    {
                        if (quantityResult < sampleTake.getResultTests().size())
                        {
                            LocalDateTime end = LocalDateTime.now();
                            LocalDateTime start = sampleTake.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            long elapsedMinutes = start == null ? 0 : Duration.between(start, end).toMinutes();
                            Long validMinutes = sampleTake.getQualityTime() == null ? 0 : sampleTake.getQualityTime();

                            if (sampleTake.getQualityTime() != null && elapsedMinutes >= validMinutes)
                            {
                                quantity++;
                            }
                        } else
                        {
                            sampleTake.getResultTests().sort((r1, r2) -> r1.getResultDate().compareTo(r2.getResultDate()));
                            LocalDateTime end = sampleTake.getResultTests().get(sampleTake.getResultTests().size() - 1).getResultDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            LocalDateTime start = sampleTake.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            long elapsedMinutes = start == null ? 0 : Duration.between(start, end).toMinutes();
                            Long validMinutes = sampleTake.getQualityTime() == null ? 0 : sampleTake.getQualityTime();

                            if (sampleTake.getQualityTime() != null && elapsedMinutes >= validMinutes)
                            {
                                quantity++;
                            }
                        }
                    }
                }
            }
            boolean isCreatedWidgetToday = dao.validateWidgetValueToday(dateNumber, idBranch);
            if (isCreatedWidgetToday)
            {
                dao.updateExpiredWidget(quantity, dateNumber, idBranch);
            } else
            {
                dao.createWidgetValueToday(dateNumber, idBranch);
                dao.updateExpiredWidget(quantity, dateNumber, idBranch);
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("tiempo transcurrido" + (end - init));
    }

    @Override
    public WidgetOrderEntry widgetOrderEntry(int date, int idBranch, int idUser) throws Exception
    {
        WidgetOrderEntry widget = new WidgetOrderEntry();
        if (!configurationService.getValue("UrlSIGA").equals(""))
        {
            TurnWidgetInfo turnWidgetInfo = integrationSigaService.informationTurns(date, idBranch, idUser);
            widget.setTurns(turnWidgetInfo.getTotalTurns());
            widget.setShiftsWorked(turnWidgetInfo.getTotalTurnsByUser());
            widget.setWaitingTurns(turnWidgetInfo.getTotalWaitingTurns());
            widget.setQualifications(integrationSigaService.turnsQualification(date, idBranch));
        }
        widget.setNumberOrders(orderService.numberOrders(date, idUser, idBranch));
        widget.setNumberOrdersByBranch(orderService.numberOrders(date, null, idBranch));
        widget.setBranch(idBranch);
        widget.setUser(new User());
        widget.getUser().setId(idUser);
        try
        {
            widget.setService(Integer.parseInt(configurationService.get("OrdenesSIGA").getValue()));
        } catch (Exception e)
        {
            widget.setService(0);
        }
        return widget;
    }

    @Override
    public WidgetEntry widgetEntry(int date, int idBranch) throws Exception
    {
        WidgetEntry widget = new WidgetEntry();
        List<Timestamp> dates = Tools.rangeDates(date, date);
        widget.setBranch(idBranch);
        List<Integer> stateList = new ArrayList<>();
        stateList.add(LISEnum.ResultSampleState.ORDERED.getValue());
        stateList.add(LISEnum.ResultSampleState.CHECKED.getValue());
        statisticDao.countSampleByStateAll(stateList, dates.get(0), dates.get(1), widget);
        List<Integer> stateList2 = new ArrayList<>();
        stateList2.add(LISEnum.ResultTestCommonState.ORDERED.getValue());
        stateList2.add(LISEnum.ResultTestCommonState.VALIDATED.getValue());
        stateList2.add(LISEnum.ResultTestCommonState.PRINTED.getValue());
        statisticDao.countResultsByStateAll(stateList2, date, widget);

        //widget.setSampleEntry(statisticDao.countSampleByState(LISEnum.ResultSampleState.ORDERED.getValue(), dates.get(0), dates.get(1)));
        //widget.setSampleVerified(statisticDao.countSampleByState(LISEnum.ResultSampleState.CHECKED.getValue(), dates.get(0), dates.get(1)));
        //widget.setSampleByTestEntry(statisticDao.countResultsByState(LISEnum.ResultTestCommonState.ORDERED.getValue(), date));
        //widget.setSampleByTestValidated(statisticDao.countResultsByState(LISEnum.ResultTestCommonState.VALIDATED.getValue(), date));
        //widget.setSampleByTestPrinted(statisticDao.countResultsByState(LISEnum.ResultTestCommonState.PRINTED.getValue(), date));
        Date dateFormat = new Date();
        HashMap<Integer, Integer> mapOrdersByWeeks = new HashMap<>();
        HashMap<Integer, Integer> mapOrdersByDays = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            mapOrdersByWeeks.put(DateTools.dateToNumber(DateTools.changeDate(dateFormat, Calendar.DAY_OF_YEAR, (-1) * 7 * (i + 1))), 0);
        }
        for (int i = 0; i < 7; i++)
        {
            mapOrdersByDays.put(DateTools.dateToNumber(DateTools.changeDate(dateFormat, Calendar.DAY_OF_YEAR, (-1) * (i + 1))), 0);
        }
        statisticDao.countOrdersByDate(mapOrdersByWeeks);
        widget.setOrdersByWeeks(mapOrdersByWeeks.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByKey())
                .map(map -> map.getValue())
                .collect(Collectors.toList()));
        statisticDao.countOrdersByDate(mapOrdersByDays);
        widget.setOrdersByDays(mapOrdersByDays.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByKey())
                .map(map -> map.getValue())
                .collect(Collectors.toList()));
        widget.setDate(date);
        return widget;
    }

}
