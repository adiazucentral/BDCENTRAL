package net.cltech.enterprisent.service.impl.enterprisent.operation.statistic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.AgileStatisticDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.StatisticDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.filters.AgileStatisticFilter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.statistic.AgileStatisticTest;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.domain.operation.statistic.StatisticResult;
import net.cltech.enterprisent.service.interfaces.operation.statistics.AgileStatisticService;
import static net.cltech.enterprisent.start.StartApp.logger;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de estadisticas rapidas para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/04/2018
 * @see Creacion
 */
@Service
public class AgileStatisticServiceEnterpriseNT implements AgileStatisticService
{

    @Autowired
    private AgileStatisticDao dao;
    @Autowired
    private TestDao testDao;
    @Autowired
    private StatisticDao statisticDao;
    @Autowired
    private OrderListDao listDao;

    @Override
    public List<String> getYearsAgileStatistic() throws Exception
    {
        return dao.getExistingYears().stream().map(y -> y.substring(y.length() - 4, y.length())).collect(Collectors.toList());
    }

    @Override
    public AgileStatisticTest getStatisticsTestBranch(int date, int idBranch, int idTest) throws Exception
    {
        return dao.getStatisticsTestBranch(date, idBranch, idTest);
    }

    @Override
    public List<AgileStatisticTest> listStatisticsTestDate(AgileStatisticFilter filter) throws Exception
    {
        List<String> years = new ArrayList<>();
        for (Integer i = Integer.valueOf(filter.getInit().toString().substring(0, 4)); i <= Integer.valueOf(filter.getEnd().toString().substring(0, 4)); i++)
        {
            if (getYearsAgileStatistic().contains(i.toString()))
            {
                years.add(i.toString());
            }
        }
        return dao.listStatisticsTest(years).stream().filter(record -> record.getDate() >= filter.getInit() && record.getDate() <= filter.getEnd()).collect(Collectors.toList());
    }

    @Override
    public List<AgileStatisticTest> listStatisticsTestYears(AgileStatisticFilter filter) throws Exception
    {
        List<String> years = getYearsAgileStatistic();
        return dao.listStatisticsTest(filter.getInit(), filter.getEnd(), filter.getYears().stream().filter(year -> years.contains(year)).collect(Collectors.toList()));
    }

    @Override
    public List<AgileStatisticTest> listStatisticsBranchDate(AgileStatisticFilter filter) throws Exception
    {
        List<String> years = new ArrayList<>();
        for (Integer i = Integer.valueOf(filter.getInit().toString().substring(0, 4)); i <= Integer.valueOf(filter.getEnd().toString().substring(0, 4)); i++)
        {
            if (getYearsAgileStatistic().contains(i.toString()))
            {
                years.add(i.toString());
            }
        }
        return dao.listStatisticsBranch(years).stream().filter(record -> record.getDate() >= filter.getInit() && record.getDate() <= filter.getEnd()).collect(Collectors.toList());
    }

    @Override
    public List<AgileStatisticTest> listStatisticsBranchYears(AgileStatisticFilter filter) throws Exception
    {
        List<String> years = getYearsAgileStatistic();
        return dao.listStatisticsBranch(filter.getInit(), filter.getEnd(), filter.getYears().stream().filter(year -> years.contains(year)).collect(Collectors.toList()));
    }

    @Override
    public void updateTestBranch(long idOrder, int idTest, boolean add, Integer type) throws Exception
    {
        StatisticOrder order = getStatisticOrder(idOrder);
        if (!dao.validateStatisticsTestBranch(order.getDate(), order.getBranch(), idTest))
        {
            TestBasic test = testDao.list().stream().filter(t -> t.getId() == idTest).findFirst().orElse(null);
            if (test != null)
            {
                dao.createTestBranch(order.getDate(), order.getBranch(), idTest, order.getBranchCode(), order.getBranchName(), test.getCode(), test.getName());
            } else
            {
                throw new EnterpriseNTException(Arrays.asList("0|test"));
            }
        }

        if (!dao.validateStatisticsBranch(order.getDate(), order.getBranch()))
        {
            dao.createBranch(order.getDate(), order.getBranch(), order.getBranchCode(), order.getBranchName());
        }

        if (add)
        {
            dao.updateTestBranch(order.getDate(), order.getBranch(), idTest, true, type);
            dao.updateBranch(order.getDate(), order.getBranch(), true, type);
        } else
        {
            statisticDao.listOrderTest(idOrder, idTest).stream().findFirst().ifPresent(result ->
            {
                updateTestBranchRemove(order.getDate(), order.getBranch(), idTest, result);
            });
        }
    }

    @Override
    public void updateOrderBranch(long idOrder, boolean add) throws Exception
    {
        Order order = getOrderBranch(idOrder);
        if (!dao.validateStatisticsBranch(order.getCreatedDateShort(), order.getBranch().getId()))
        {
            dao.createBranch(order.getCreatedDateShort(), order.getBranch().getId(), order.getBranch().getCode(), order.getBranch().getName());
        }

        dao.updateBranch(order.getCreatedDateShort(), order.getBranch().getId(), add, Constants.STATISTICS_ORDER_ENTRY);
    }

    /**
     * Identifica los estados del examen para descontar valores en Estadisticas
     * Rapidas: Sede - Prueba.
     *
     * @param date Fecha en formato yyyymmdd.
     * @param idBranch Id de la sede.
     * @param idTest Id del examen.
     * @param result Información del examen.
     */
    private void updateTestBranchRemove(int date, int idBranch, int idTest, StatisticResult result)
    {
        try
        {
            if (result.getOpportunityTimes().getEntryDate() != null)
            {
                dao.updateTestBranch(date, idBranch, idTest, false, Constants.STATISTICS_TEST_ENTRY);
                dao.updateBranch(date, idBranch, false, Constants.STATISTICS_TEST_ENTRY);
            }

            if (result.getOpportunityTimes().getValidDate() != null)
            {
                dao.updateTestBranch(date, idBranch, idTest, false, Constants.STATISTICS_TEST_VALIDATE);
                dao.updateBranch(date, idBranch, false, Constants.STATISTICS_TEST_VALIDATE);
            }

            if (result.getOpportunityTimes().getPrintDate() != null)
            {
                dao.updateTestBranch(date, idBranch, idTest, false, Constants.STATISTICS_TEST_PRINT);
                dao.updateBranch(date, idBranch, false, Constants.STATISTICS_TEST_PRINT);
            }

            if (result.getPathology() != LISEnum.ResultTestPathology.NORMAL.getValue())
            {
                dao.updateTestBranch(date, idBranch, idTest, false, Constants.STATISTICS_TEST_PATHOLOGY);
                dao.updateBranch(date, idBranch, false, Constants.STATISTICS_TEST_PATHOLOGY);
            }
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "[EnterpriseNT - App] : Error al insertar en estadisticas rapidas.", ex);
        }
    }

    @Override
    public void addOrderTests(long idOrder, List<Test> tests) throws Exception
    {
        for (Test test : tests.stream().filter(test -> test.getTestType() == 0).collect(Collectors.toList()))
        {
            updateTestBranch(idOrder, test.getId(), true, Constants.STATISTICS_TEST_ENTRY);
        }
        addAreaBranch(idOrder, tests);
    }

    /**
     * Obtiene la información de la orden para ingresar en estadisticas rapidas.
     *
     * @param idOrder Numero de la Orden.
     * @return Orden.
     * @throws java.lang.Exception
     */
    private Order getOrder(long idOrder) throws Exception
    {

        return listDao.listTestOrder(idOrder, idOrder, 1, new ArrayList<>(), null, null).stream().findAny().orElse(null);
    }

    /**
     * Obtiene la información de la orden para ingresar en estadisticas rapidas.
     *
     * @param idOrder Numero de la Orden.
     * @return Orden.
     * @throws java.lang.Exception
     */
    private Order getOrder(long idOrder, AuthorizedUser authorizedUser) throws Exception
    {
        AuthorizedUser user = authorizedUser;
        return listDao.list(idOrder, idOrder, 1, new ArrayList<>(), null, null, user.getId()).stream().findAny().orElse(null);
    }

    /**
     * Obtiene la información de la orden para ingresar en estadisticas rapidas.
     *
     * @param idOrder Numero de la Orden.
     * @return Orden.
     * @throws java.lang.Exception
     */
    private Order getOrderBranch(long idOrder) throws Exception
    {
        return listDao.orderBranch(idOrder);
    }

    /**
     * Obtiene la información de la orden para ingresar en estadisticas rapidas.
     *
     * @param idOrder Numero de la Orden.
     * @return Orden.
     */
    private StatisticOrder getStatisticOrder(long idOrder) throws Exception
    {
        return statisticDao.getOrder(idOrder, new ArrayList<>());
    }

    @Override
    public void addAreaBranch(long idOrder, List<Test> tests) throws Exception
    {
        try
        {
            Order order = getOrder(idOrder);
            if (order != null)
            {
                Map<Area, Long> areaGroup = order.getTests().stream()
                        .filter(test -> tests.contains(test))
                        .collect(Collectors.groupingBy(Test::getArea, Collectors.counting()));

                for (Map.Entry<Area, Long> entry : areaGroup.entrySet())
                {
                    Area area = entry.getKey();
                    Long value = entry.getValue();
                    if (area.getId() != null)
                    {
                        if (!dao.existsAreaByBranch(order.getCreatedDateShort(), order.getBranch().getId(), area.getId()))
                        {
                            dao.insertAreaBranch(order.getCreatedDateShort(), order.getBranch().getId(), area.getId(), order.getBranch().getCode(), order.getBranch().getName(), area.getAbbreviation(), area.getName(), value);
                        } else
                        {
                            dao.updateAreaBranch(order.getCreatedDateShort(), order.getBranch().getId(), area.getId(), true, Constants.STATISTICS_TEST_ENTRY, value);
                        }
                    }
                }
            }
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "[EnterpriseNT - App] : Error al descontar en estadisticas rapidas:[sede-area]", ex);
        }

    }

    @Override
    public void deleteAreaBranch(long idOrder, List<Test> tests) throws Exception
    {
        StatisticOrder order = getStatisticOrder(idOrder);
        Map<Area, Long> areaGroup = order.getResults().stream()
                .filter(result -> tests.contains(new Test(result.getId())))
                .map(result ->
                {
                    Test test = new Test(result.getId());
                    test.setArea(new Area(result.getSectionId()));
                    test.getArea().setAbbreviation(result.getSectionCode());
                    test.getArea().setName(result.getSectionName());
                    return test;
                })
                .collect(Collectors.groupingBy(Test::getArea, Collectors.counting()));

        for (Map.Entry<Area, Long> entry : areaGroup.entrySet())
        {
            Area area = entry.getKey();
            Long value = entry.getValue();
            dao.updateAreaBranch(order.getDate(), order.getBranch(), area.getId(), false, Constants.STATISTICS_TEST_ENTRY, value);
        }

        order.getResults().stream()
                .filter(result -> tests.contains(new Test(result.getId())))
                .forEachOrdered(test ->
                {
                    discountAreaBranchStates(order.getDate(), order.getBranch(), test.getSectionId(), test.getTestState(), test.getPathology() != LISEnum.ResultTestPathology.NORMAL.getValue());
                });
    }

    /**
     * Descuenta del conteo de examenes para impresos, validados y patologicos
     *
     * @param date fecha yyyyMMdd
     * @param idBranch id de la sede
     * @param idArea id del area
     * @param resultState estado del resultado
     * @param isPathologic indica si el resultado es patologico
     */
    private void discountAreaBranchStates(int date, int idBranch, int idArea, int resultState, boolean isPathologic)
    {
        try
        {
            if (resultState >= LISEnum.ResultTestState.DELIVERED.getValue())
            {
                dao.updateAreaBranch(date, idBranch, idArea, false, Constants.STATISTICS_TEST_PRINT, 1);
            }
            if (resultState >= LISEnum.ResultTestState.VALIDATED.getValue())
            {
                dao.updateAreaBranch(date, idBranch, idArea, false, Constants.STATISTICS_TEST_VALIDATE, 1);
            }
            if (isPathologic)
            {
                dao.updateAreaBranch(date, idBranch, idArea, false, Constants.STATISTICS_TEST_PATHOLOGY, 1);
            }

        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "[EnterpriseNT - App] : Error al descontar en estadisticas rapidas:[sede-area]", ex);
        }
    }

    @Override
    public void discountAreaBranch(long idOrder, int idTest)
    {
        try
        {
            StatisticOrder order = getStatisticOrder(idOrder);
            order.getResults().stream()
                    .filter(result -> idTest == result.getId())
                    .findFirst()
                    .ifPresent(test ->
                    {
                        discountAreaBranchStates(order.getDate(), order.getBranch(), test.getSectionId(), test.getTestState(), test.getPathology() != LISEnum.ResultTestPathology.NORMAL.getValue());
                    });
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "[EnterpriseNT - App] : Error al descontar en estadisticas rapidas:[sede-area]", ex);
        }
    }

    @Override
    public List<AgileStatisticTest> listBranchAreaDate(AgileStatisticFilter filter) throws Exception
    {
        List<String> years = new ArrayList<>();
        for (Integer i = Integer.valueOf(filter.getInit().toString().substring(0, 4)); i <= Integer.valueOf(filter.getEnd().toString().substring(0, 4)); i++)
        {
            if (getYearsAgileStatistic().contains(i.toString()))
            {
                years.add(i.toString());
            }
        }
        return dao.listBranchArea(years).stream().filter(record -> record.getDate() >= filter.getInit() && record.getDate() <= filter.getEnd()).collect(Collectors.toList());
    }

    @Override
    public List<AgileStatisticTest> listBranchAreaYears(AgileStatisticFilter filter) throws Exception
    {
        List<String> years = getYearsAgileStatistic();
        return dao.listBranchArea(filter.getInit(), filter.getEnd(), filter.getYears().stream().filter(year -> years.contains(year)).collect(Collectors.toList()));
    }

    @Override
    public void updateOrderBranch(long idOrder, boolean add, AuthorizedUser authorizedUser) throws Exception
    {
        Order order = getOrder(idOrder, authorizedUser);
        if (!dao.validateStatisticsBranch(order.getCreatedDateShort(), order.getBranch().getId()))
        {
            dao.createBranch(order.getCreatedDateShort(), order.getBranch().getId(), order.getBranch().getCode(), order.getBranch().getName());
        }

        dao.updateBranch(order.getCreatedDateShort(), order.getBranch().getId(), add, Constants.STATISTICS_ORDER_ENTRY);
    }

}
