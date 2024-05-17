package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationResultadosDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultDao;
import net.cltech.enterprisent.domain.DTO.integration.resultados.ResponseOrderDetailResultHIS;
import net.cltech.enterprisent.domain.integration.resultados.RequestOrdersResultados;
import net.cltech.enterprisent.domain.integration.resultados.RequestUpdateSendResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseDetailMicroorganisms;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderDetailResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderResult;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationResultadosService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.interview.InterviewService;
import net.cltech.enterprisent.service.interfaces.masters.test.CentralSystemService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de Integración de resultados para Enterprise NT
 *
 * @version 1.0.0
 * @author javila
 * @since 04/03/2020
 * @see Creación
 */
@Service
public class IntegrationResultadosServiceEnterpriseNT implements IntegrationResultadosService
{

    @Autowired
    private IntegrationResultadosDao integrationResultadosDao;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private TestService testService;
    @Autowired
    private DemographicService demographicService;

    @Autowired
    private ResultDao resultDao;
    @Autowired
    private CentralSystemService centralSystemService;
    @Autowired
    private InterviewService interviewService;

    /**
     * Obtendra una lista de ordenes dentro del rango de una orden inicial hasta
     * una orden final Si envia ID HIS filtra las ordenes asociadas a ese HIS
     *
     * @param requestOrdersResultados
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    @Override
    public List<ResponseOrderResult> ordersRange(RequestOrdersResultados requestOrdersResultados) throws Exception
    {
        try
        {
            List<ResponseOrderResult> objFinal = new ArrayList<>();
            List<ResponseOrderResult> objAux;
            List<List<Long>> listOrdersRange;
            requestOrdersResultados.setType(requestOrdersResultados.getType() == null ? 1 : requestOrdersResultados.getType());

            if (!Objects.equals(requestOrdersResultados.getCentralSystem(), null) && requestOrdersResultados.getCentralSystem() > 0)
            {
                listOrdersRange = integrationResultadosDao.ordersRange(requestOrdersResultados.getFromOrder(), requestOrdersResultados.getUntilOrder(), requestOrdersResultados.getType(), requestOrdersResultados.getCentralSystem());
            } else
            {
                listOrdersRange = integrationResultadosDao.ordersRange(requestOrdersResultados.getFromOrder(), requestOrdersResultados.getUntilOrder(), requestOrdersResultados.getType(), null);
            }

            int countOrders = 0;
            for (List<Long> list : listOrdersRange)
            {
                countOrders++;
                if (countOrders <= 100)
                {
                    objAux = integrationResultadosDao.allDemographicsItemList(requestOrdersResultados, list.get(0), list.get(1).intValue());
                    if (objAux != null && !objAux.isEmpty())
                    {
                        objFinal.add(objAux.get(0));
                    }
                } else
                {
                    break;
                }
            }
            return objFinal;
        } catch (Exception e)
        {
            ResultsLog.error(e);
            return null;
        }
    }

    @Override
    public List<ResponseOrderDetailResult> resultsByOrderByCentralCodes(long order, int centralSystem, boolean funtionProfileId) throws Exception
    {
        try
        {
            List<ResponseOrderDetailResult> list = integrationResultadosDao.resultsByOrderByCentralCodes(order, centralSystem, funtionProfileId);
            list = list.stream().filter(filter -> filter.getInterfaceTestID() != null).collect(Collectors.toList());
            return list;
        } catch (Exception e)
        {
            ResultsLog.error(e);
            return null;
        }
    }

    /**
     * Filtra informacion por item demografico y SystemCentral
     *
     * @param resultsQueryFilter
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    /*
    @Override
    public List<DTOResultHis> resultsByCentralSystemByDemographicItems(ResultsQueryFilterGlobal resultsQueryFilter) throws Exception
    {
        try
        {
            long orderI = 0;
            long orderE = 0;
            Timestamp dateI = null;
            Timestamp dateF = null;
            switch (resultsQueryFilter.getCentralSystem().toLowerCase())
            {
                case "ciklos":
                    switch (Integer.parseInt(String.valueOf(resultsQueryFilter.getType())))
                    {
                        case 1:
                            orderI = resultsQueryFilter.getOrderI();
                            orderE = resultsQueryFilter.getOrderF();
                            dateI = null;
                            dateF = null;
                            break;
                        case 2:
                            dateI = new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(resultsQueryFilter.getDateI()).getTime());
                            dateF = new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(resultsQueryFilter.getDateF()).getTime());
                            orderI = 0;
                            orderE = 0;
                            break;
                    }
                    break;
                case "breakpoint":
                case "saludtotal":
                    orderI = resultsQueryFilter.getOrderI();
                    orderE = resultsQueryFilter.getOrderF();
                    if (orderI <= 0)
                    {
                        orderI = Tools.createInitialOrder(4, resultsQueryFilter.getDays());
                    }
                    if (orderE <= 0)
                    {
                        orderE = Tools.createFinalOrder(4, 0);
                    }
                    break;
                case "GHIPS":
                    orderI = resultsQueryFilter.getOrderI();
                    orderE = resultsQueryFilter.getOrderF();
                    if (orderI <= 0)
                    {
                        orderI = Tools.createInitialOrder(4, resultsQueryFilter.getDays());
                    }
                    if (orderE <= 0)
                    {
                        orderE = Tools.createFinalOrder(4, 0);
                    }
                    break;
            }
            ResultHISMapper resultHIS = new ResultHISMapper(resultsQueryFilter.getCentralSystem());
            List<ResponseOrderDetailResult> listResponse = integrationResultadosDao.resultsByCentralSystemByDemographicItems(orderI, orderE, dateI, dateF, resultsQueryFilter.getCentralSystemId(), true);
            List<DTOResultHis> records = listResponse.stream()
                    .map(resultHIS::toDTO)
                    .collect(toList());
            return records;
        } catch (Exception e)
        {
            return null;
        }
    }
     */
    /**
     * Consulta las ordenes con sus demograficos para ser enviadas con examenes
     * validados para ser enviados al HIS.
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<OrderList> orderspendingsendhis() throws Exception
    {
        try
        {
            List<OrderList> orders = integrationResultadosDao.orderspendingsendhis(demographicService.list(true));

            String listorders = orders.stream().map(o -> o.getOrderNumber().toString()).collect(Collectors.joining(","));

            List<Question> question = interviewService.getInterviewOrders(listorders);

            for (int i = 0; i < orders.size(); i++)
            {
                orders.get(i).setListQuestion(filterquestionorder(orders.get(i).getOrderNumber(), question));
            }

            return orders;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<Question> filterquestionorder(Long order, List<Question> questions)
    {
        try
        {
            return questions.stream().filter(t -> Objects.equals(t.getOrderNumber(), order)).collect(Collectors.toList());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Resultados de microorganismos, antibioticos y antibiogramas para la
     * interfaz de resultados por id orden y id test
     *
     * @param idOrder
     * @param idTest
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<ResponseDetailMicroorganisms> listDetailMicroorganisms(long idOrder, int idTest) throws Exception
    {
        try
        {
            List<ResponseDetailMicroorganisms> listResponseDetailMicro = integrationResultadosDao.listDetailMicroorganisms(idOrder, idTest);
            return listResponseDetailMicro;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Retorna la cantidad de sistemas centrales a interpriseNT
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    public int amountOfSystemCetral() throws Exception
    {
        List<CentralSystem> listSystem = centralSystemService.list(true);
        int amountOfSystemCetral = listSystem.size();
        return amountOfSystemCetral;
    }

    @Override
    public int updateResultStateAndDateUpdate(RequestUpdateSendResult requestUpdateSendResult) throws Exception
    {
        try
        {
            int amountOfSystemCetral = amountOfSystemCetral();
            int update = 0;
            Timestamp tmsNow = new Timestamp(new Date().getTime());
            if (amountOfSystemCetral > 1)
            {
                update = resultDao.insertOrdersToTheExternalCentralSystem(requestUpdateSendResult, tmsNow);

            } else
            {
                update = integrationResultadosDao.updateResultStateAndDateUpdate(requestUpdateSendResult, tmsNow);
            }
            return update;
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Verifica la muestra para una orden y un examen especifico
     *
     * @param idOrder
     * @param idTest
     * @return Boolean
     * @throws Exception Error en la base de datos.
     */
    @Override
    public boolean verifySampleLih(long idOrder, int idTest) throws Exception
    {
        try
        {
            OrderCreationLog.info("entro en verifySampleLih ");
            boolean verify = false;
            Test test = integrationResultadosDao.getTest(idTest, null, null, null);
            if (test.getTestType() == 0)
            {
                verify = sampleTrackingService.sampleTracking(idOrder, test.getSample().getCodesample(), LISEnum.ResultSampleState.CHECKED.getValue(), null, false, false);
            } else if (test.getTestType() == 1)
            {
                List<Profile> listProfiles = testService.getProfiles().stream().filter(profile -> Objects.equals(test.getId(), profile.getProfileId())).collect(Collectors.toList());
                for (Profile profile : listProfiles)
                {
                    Test testTwo = integrationResultadosDao.getTest(profile.getTestId(), null, null, null);
                    verify = sampleTrackingService.sampleTracking(idOrder, testTwo.getSample().getCodesample(), LISEnum.ResultSampleState.CHECKED.getValue(), null, false, false);
                }
            }

            return verify;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Obtendra una lista de ordenes dentro del rango de una orden inicial hasta
     * una orden final
     *
     * @param requestOrdersResultados
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    @Override
    public List<ResponseOrderResult> getOrdersForOthers(RequestOrdersResultados requestOrdersResultados) throws Exception
    {
        try
        {
            List<ResponseOrderResult> objFinal = new ArrayList<>();
            List<ResponseOrderResult> objAux;
            List<List<Long>> listOrdersRange;
            listOrdersRange = integrationResultadosDao.ordersRange(requestOrdersResultados.getFromOrder(), requestOrdersResultados.getUntilOrder(), requestOrdersResultados.getType(), null);
            int countOrders = 0;
            for (List<Long> list : listOrdersRange)
            {
                countOrders++;
                if (countOrders <= 100)
                {
                    objAux = integrationResultadosDao.allDemographicsItemListForOther(requestOrdersResultados, list.get(0), list.get(1).intValue());
                    if (objAux != null && !objAux.isEmpty())
                    {
                        objFinal.add(objAux.get(0));
                    }
                } else
                {
                    break;
                }
            }
            return objFinal;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ResponseOrderDetailResult> getMyMResults(long order, int centralSystem, boolean funtionProfileId) throws Exception
    {
        try
        {
            return integrationResultadosDao.getMyMResults(order, centralSystem, funtionProfileId);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public int updateSentCentralSystem(long idOrder, String idTest, Integer idCentralSystem) throws Exception
    {
        return resultDao.updateSentCentralSystem(idOrder, idTest, null);
    }

//    @Override
//    public DTOResultHis resultsByCentralSystemByOrderAndIdtest(OrderToSearch orderToSearch) throws Exception
//    {
//        return new ResultGHIPS(integrationResultadosDao.resultsByCentralSystem(orderToSearch));
//    }
    @Override
    public List<ResponseOrderDetailResultHIS> resultsByOrderByCentralCodes(long order, int centralSystem) throws Exception
    {
        try
        {
            List<ResponseOrderDetailResult> list = integrationResultadosDao.resultsByOrderByCentralCodes(order, centralSystem, true);
            List<ResponseOrderDetailResultHIS> detailResultHISs = new ArrayList<>();
            list.stream().filter(filter -> filter.getInterfaceTestID() != null)
                    .forEach(result ->
                    {
                        ResponseOrderDetailResultHIS resultHIS = new ResponseOrderDetailResultHIS(result);
                        if (result.getAntibiogram() == 1)
                        {
                            try
                            {
                                resultHIS.setMicroorganisms(integrationResultadosDao.listDetailMicroorganisms(order, result.getTestId()));
                            } catch (Exception ex)
                            {
                                resultHIS.setMicroorganisms(new ArrayList<>());
                                ResultsLog.error("ERROR EN ANTIBIOGRAMA " + ex);
                            }

                        }

                        detailResultHISs.add(resultHIS);

                    });

            return detailResultHISs;
        } catch (Exception e)
        {
            ResultsLog.error(e);
            return null;
        }
    }
}
