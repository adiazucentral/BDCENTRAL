package net.cltech.enterprisent.service.impl.enterprisent.operation.list;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.list.FilterRejectSample;
import net.cltech.enterprisent.domain.operation.list.RemissionLaboratory;
import net.cltech.enterprisent.domain.operation.list.TestBranchCheck;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.TestList;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeOrder;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeRequest;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeSample;
import net.cltech.enterprisent.domain.operation.remission.RemissionOrderCentral;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.list.OrderListService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.StreamFilters;
import net.cltech.enterprisent.tools.barcode.BarcodeTools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.hl7.HL7List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de listados para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/09/2017
 * @see Creacion
 */
@Service
public class OrderListServiceEnterpriseNT implements OrderListService
{

    @Autowired
    private OrderListDao dao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private ResultDao resultDao;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public List<OrderList> listFilters(Filter search) throws Exception
    {
        getCurrentStates(search);
        int sistemaCentralListados = 0;

        boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");
        boolean consultarSistemaCentral = daoConfig.get("ConsultarSistemaCentral").getValue().equalsIgnoreCase("true");
        if (consultarSistemaCentral)
        {
            sistemaCentralListados = Integer.parseInt(daoConfig.get("SistemaCentralListados").getValue());
        }

        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        
        List<OrderList> list = dao.listBasic(search, serviceDemographic.list(true), account, physician, rate, service, race, documenttype, consultarSistemaCentral, sistemaCentralListados, false, laboratorys, idbranch );
        List<OrderList> filters = list.stream()
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographicList(filter, search.getDemographics()))
                .map((OrderList t) -> t.setTests(filteranaliteprofile(t.getTests())))
                .map((OrderList t) -> t.setTests(filterExcludetestbyprofilelist(t.getTests())))
                .map((OrderList t) -> t.setTests(filterviewquery(t.getOrderNumber(), t.getTests())))
                .filter(filter -> search.isBasic() || !filter.getTests().isEmpty())
                .collect(Collectors.toList());

        if (search.getFilterType() != null)
        {
            filters = filters.stream().filter(filter -> filterType(filter, search)).collect(Collectors.toList());
        }
        return filters;
    }
    
    @Override
    public List<OrderList> listFiltersAppointment(Filter search) throws Exception
    {

        boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");
     
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        
        List<OrderList> list = dao.listBasicAppointment(search, serviceDemographic.list(true), account, physician, rate, service, race, documenttype, false, laboratorys, idbranch );
        List<OrderList> filters = list.stream()
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographicList(filter, search.getDemographics()))
                .map((OrderList t) -> t.setTests(filteranaliteprofile(t.getTests())))
                .map((OrderList t) -> t.setTests(filterExcludetestbyprofilelist(t.getTests())))
                .map((OrderList t) -> t.setTests(filterviewquery(t.getOrderNumber(), t.getTests())))
                .filter(filter -> search.isBasic() || !filter.getTests().isEmpty())
                .collect(Collectors.toList());

        if (search.getFilterType() != null)
        {
            filters = filters.stream().filter(filter -> filterType(filter, search)).collect(Collectors.toList());
        }
        
        return filters;
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<TestList> filterExcludetestbyprofilelist(List<TestList> filter)
    {
        try
        {
            Set<Integer> filteredProfile
                    = filter.stream().filter(t -> t.getTestType() != 0 && t.getExcluideTestProfile() == 1)
                            .map(TestList::getId).collect(Collectors.toSet());

            List<TestList> filteredTests = filter.stream()
                    .filter(c -> !filteredProfile.contains(c.getProfile()))
                    .collect(Collectors.toList());

            return filteredTests;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<TestList> filteranaliteprofile(List<TestList> filtertest)
    {
        try
        {
            //Set<Integer> filteredProfile = filtertest.stream().filter(t -> t.getTestType() != 0).map(TestList::getId).collect(Collectors.toSet());
            List<TestList> filteredProfile = filtertest.stream().filter(t -> t.getTestType() != 0).collect(Collectors.toList());

            if (filteredProfile.size() > 0)
            {
                filteredProfile.forEach(t ->
                {
                    List<TestList> filteredTests = filtertest.stream()
                            .filter(c -> java.util.Objects.equals(c.getProfile(), t.getId()))
                            .collect(Collectors.toList());

                    if (filteredTests.isEmpty())
                    {
                        filtertest.removeIf(test -> java.util.Objects.equals(test.getId(), t.getId()));
                    } else if (t.getSample().getId() == null || t.getSample().getId() <= 0)
                    {
                        String position = "";
                        String rack = "";
                        for (TestList filteredTest : filteredTests)
                        {
                            if (filteredTest.getPositionStore() != null && !position.contains(filteredTest.getPositionStore()))
                            {
                                position += filteredTest.getPositionStore() + ", ";
                            }
                            if (filteredTest.getRackStore() != null && !rack.contains(filteredTest.getRackStore()))
                            {
                                rack += filteredTest.getRackStore() + ", ";
                            }
                        }
                        if (!position.isEmpty())
                        {
                            position = position.substring(0, position.lastIndexOf(",")).trim();
                            t.setPositionStore(position);
                        }
                        if (!rack.isEmpty())
                        {
                            rack = rack.substring(0, rack.lastIndexOf(",")).trim();
                            t.setRackStore(rack);
                        }
                    }
                });

            }
            return filtertest;

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<TestList> filterviewquery(Long order, List<TestList> filtertest)
    {
        try
        {
            System.out.println(order);
            return filtertest.stream().filter(t -> t.getViewquery() == 1 || t.getTestType() > 0).collect(Collectors.toList());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<CommentOrder> getListCommentOrder(Long order)
    {
        return commentDao.getlistCommentOrder(order);
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<Test> filterExcludetestbyprofile(Order filter)
    {
        try
        {
            Set<Integer> filteredProfile
                    = filter.getTests().stream().filter(t -> t.getTestType() != 0 && t.getExcluideTestProfile() == 1)
                            .map(Test::getId).collect(Collectors.toSet());

            List<Test> filteredTests = filter.getTests().stream()
                    .filter(c -> !filteredProfile.contains(c.getProfile()))
                    .collect(Collectors.toList());

            return filteredTests;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param Order Orden a la cual se le realizara el filtro
     * @param filter lista exámenes a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<Test> filterRemmision(Order filter)
    {
        try
        {
            Set<Integer> filteredProfile
                    = filter.getTests().stream().filter(t -> t.getTestType() != 0 && t.getExcluideTestProfile() == 1 && t.getRemission() == 0)
                            .map(Test::getId).collect(Collectors.toSet());

            List<Test> filteredTests = filter.getTests().stream()
                    .filter(c -> !filteredProfile.contains(c.getProfile()) && c.getRemission() == 0 && c.getResult().getState() < 2)
                    .collect(Collectors.toList());

            return filteredTests;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Order> orderbyDateDelete(Filter search) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Order> list = dao.orderbyDateDelete(search.getInit(), search.getEnd(),idbranch);
        return list;
    }

    @Override
    public List<Order> orderState(Filter search) throws Exception
    {
        List<Order> list = dao.orderState(search.getOrders());
        return list;
    }

    @Override
    public List<Order> listFiltersRejectSample(FilterRejectSample search) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        
        List<Order> list = dao.list(search.getInit(), search.getEnd(), 0, serviceDemographic.list(true), null, null, 0, 0, laboratorys, idbranch);
        List<Order> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != 0)
                .map((Order t) -> t.setTests(filterOrderTestsReject(t, search)))
                .filter(filter -> search.getBranch() == null || search.getBranch() == 0 || filter.getBranch().getId().equals(search.getBranch()))
                .filter(filter -> !filter.getTests().isEmpty())
                .collect(Collectors.toList());
        //filters = setComments(filters);
        return filters;
    }

    @Override
    public List<Order> listFiltersLaboratory(Filter search) throws Exception
    {
        List<Order> list = ordersFilters(search);
        if (search.getRemission() == 1)
        {
            //remission(list, search.getLaboratory());
        }
        return list;
    }

    @Override
    public List<Order> savelaboratoryremmision(RemissionLaboratory orders) throws Exception
    {
        resultDao.remission(orders);
        return orders.getOrders();
    }

    @Override
    public List<Order> listFilterLaboratoryRemmision(Filter search) throws Exception
    {
        List<Order> list = ordersFiltersRemmision(search);
        return list;
    }

    @Override
    public String listFiltersLaboratoryHL7(Filter search) throws Exception
    {
         int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        
        List<Order> list = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), null, search.getTestFilterType() == 2 ? search.getTests() : null, search.getCheck(), 0, laboratorys, idbranch);
        List<Order> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != 0)
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographic(filter, search.getDemographics()))
                .map((Order t) -> t.setTests(filterOrderTestsLaboratory(t, search)))
                .filter(filter -> !filter.getTests().isEmpty())
                .collect(Collectors.toList());

        HL7List hl7 = new HL7List();
        String hl7S = hl7.hl7List(filters);
        return hl7S;
    }

    @Override
    public List<String> barcodeGeneration(Filter search) throws Exception
    {
        BarcodeRequest printRequest = new BarcodeRequest();

        String separator = daoConfig.get("SeparadorMuestra").getValue();
        String dateFormat = daoConfig.get("FormatoFecha").getValue();

        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        //Realiza filtros
        List<Order> listOld = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), null, search.getTestFilterType() == 2 ? search.getTests() : null, 0, 0, laboratorys, idbranch);
        List<Order> list = listOld.stream()
                .filter(filter -> filter.getPatient().getId() != 0)
                .map(order -> order.setTests(filterTestsSample(order, search)))
                .filter(filter -> !filter.getTests().isEmpty())
                .collect(Collectors.toList());
        List<BarcodeOrder> barcodes = new ArrayList<>();

        list.forEach(order ->
        {
            if (search.isPrintAddLabel())
            {
                Order addLabel = listOld.get(listOld.indexOf(order));
                BarcodeOrder barcode = new BarcodeOrder(addLabel.getOrderNumber().toString(), 0);
                if (!barcodes.contains(barcode))
                {
                    barcode.setConfiguration("1,2,3");
                    barcode.setSeparator(separator);
                    barcode.setDemos(BarcodeTools.orderToHashMap(addLabel, dateFormat));
                    barcode.setQuantity(1);
                    barcodes.add(barcode);

                }
                barcode = barcodes.get(barcodes.indexOf(barcode));
                barcode.setAreas(addLabel.getTests().stream().map(test -> test.getArea().getAbbreviation()).collect(Collectors.toList()));
                barcode.setTests(addLabel.getTests().stream().map(test -> test.getAbbr()).collect(Collectors.toList()));
            }
            order.getTests().forEach(test ->
            {
                BarcodeOrder barcode = new BarcodeOrder(order.getOrderNumber().toString(), test.getSample().getId());
                if (!barcodes.contains(barcode))
                {
                    barcode.setSampleName(test.getSample().getName());
                    barcode.setSampleCode(test.getSample().getCodesample());
                    barcode.setSampleRecipient(test.getSample().getContainer().getName());
                    barcode.setConfiguration(test.getSample().isTypebarcode() ? "1,2,3" : "1,2,4");
                    barcode.setSeparator(separator);
                    barcode.setDemos(BarcodeTools.orderToHashMap(order, dateFormat));
                    Integer quantity = search.getSamples().isEmpty() ? null : search.getSamples().get(search.getSamples().indexOf(new BarcodeSample(test.getSample().getId()))).getQuantity();
                    barcode.setQuantity(quantity == null ? test.getSample().getCanstiker() : quantity);
                    barcodes.add(barcode);
                }
                barcode = barcodes.get(barcodes.indexOf(barcode));
                if (!barcode.getAreas().contains(test.getArea().getAbbreviation()))
                {
                    barcode.getAreas().add(test.getArea().getAbbreviation());
                }
                if (!barcode.getTests().contains(test.getAbbr()))
                {
                    barcode.getTests().add(test.getAbbr());
                }
            });
        });
        String file = System.getProperty("user.dir") + File.separator + "barcode" + File.separator + "PrinterService-1.0.0.jar";
        System.out.println("File Printer:" + file);
        //Generación comando EPL
        if (!barcodes.isEmpty())
        {
            printRequest.setId(search.getPrinterId());
            for (BarcodeOrder barcode : barcodes)
            {
                String epl;
                if (barcode.getSampleId() != null && barcode.getSampleId() != 0)
                {
                    epl = barcode.getQuantity() == 0 ? "" : BarcodeTools.sampleBarcode(file, barcode);

                } else
                {
                    epl = BarcodeTools.sampleAddLabel(file, barcode);
                }

                for (int i = 0; i < barcode.getQuantity(); i++)
                {
                    printRequest.getBarcodeString().add(epl);
                }
            }
        }

        return printRequest.getBarcodeString();
    }

    @Override
    public List<Order> listFiltersNoPatient(Filter search) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        
        List<Order> list = dao.listNoPatient(search.getInit(), search.getEnd(), search.getRangeType(), idbranch)
                .stream()
                .filter(filter -> search.getOrderType() == 0 || filter.getType().getId() == search.getOrderType())
                .collect(Collectors.toList());
        //list = setComments(list);
        return list;
    }

    private List<Order> setComments(List<Order> orders) throws Exception
    {
        List<Order> ordersComment = new ArrayList<>();
        for (Order order : orders)
        {
            order.setComments(commentService.listCommentOrder(order.getOrderNumber(), null).stream().filter(comment -> comment.isPrint()).collect(Collectors.toList()));
            if (order.getPatient() != null && order.getPatient().getId() != null)
            {
                order.getPatient().setDiagnostic(commentService.listCommentOrder(null, order.getPatient().getId()));
            }
            ordersComment.add(order);
        }
        return ordersComment;
    }

    /**
     * Filtro de examenes por seccion y muestra
     *
     * @param filter orden a filtrar
     * @param search filtros deseados
     *
     * @return Lista de examenes encontrados
     */
    private List<Test> filterTestsSample(Order filter, Filter search)
    {
        try
        {
            List<Test> filteredTests = filter.getTests();
            filteredTests = filteredTests.stream()
                    .filter(test -> test.getSample().getId() != null)
                    .filter(test -> search.getSamples() == null || search.getSamples().isEmpty() || search.getSamples().stream().map(sample -> sample.getIdSample()).collect(Collectors.toList()).contains(test.getSample().getId()))
                    .filter(test -> search.getTests() == null || search.getTests().isEmpty() || search.getTests().contains(test.getArea().getId()))
                    .collect(Collectors.toList());

            return filteredTests;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<Test> filterOrderTestsLaboratory(Order filter, Filter search)
    {
        try
        {
            List<Test> filteredTests = filter.getTests();
            String laboratory = daoConfig.get("Entidad").getValue();

            filteredTests = filteredTests.stream()
                    .filter(test -> search.getLaboratories().isEmpty() || search.getLaboratories().contains(test.getLaboratory().getId()))
                    .map(test -> test.setLaboratoryOrigin(laboratory))
                    .collect(Collectors.toList());

            return filteredTests;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * *
     * Filtra examenes con muestras rechazadas
     *
     * @param filter orden a filtrar
     * @param search lista de examenes filtrados
     *
     * @return
     */
    private List<Test> filterOrderTestsReject(Order filter, FilterRejectSample search)
    {
        List<Test> filteredTests = filter.getTests();

        filteredTests = filteredTests.stream()
                .filter(test -> test.getResult().getSampleState() == (search.isRejectSample() ? LISEnum.ResultSampleState.REJECTED.getValue() : LISEnum.ResultSampleState.NEW_SAMPLE.getValue()))
                .filter(test -> search.getSamples() == null || search.getSamples().isEmpty() || search.getSamples().contains(test.getSample().getId()))
                .collect(Collectors.toList());
        return filteredTests;
    }

    /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param filter Orden con examenes a filtrar
     * @param search lista de examenes filtrados
     *
     * @return
     */
    private List<Test> filterOrderTests(Order filter, Filter search)
    {
        List<Test> filteredTests;
        switch (search.getTestFilterType())
        {
            case 1:
                filteredTests = filter.getTests().stream()
                        .filter(test -> search.getTests().contains(test.getArea().getId()))
                        .collect(Collectors.toList());
                break;
            case 2:
                filteredTests = filter.getTests().stream()
                        .filter(test -> search.getTests().contains(test.getId()))
                        .collect(Collectors.toList());
                break;
            case 3:
                filteredTests = filter.getTests().stream()
                        .filter(test -> search.getTests().contains(test.getId()))
                        .collect(Collectors.toList());
                break;

            case 4:
                filteredTests = filter.getTests().stream()
                        .filter(test -> test.getRemission() == 1)
                        .collect(Collectors.toList());
                break;

            case 5:
                filteredTests = filter.getTests().stream()
                        .filter(test -> test.getRemission() == 1)
                        .filter(test -> search.getTests().contains(test.getId()))
                        .collect(Collectors.toList());
                break;
            default:
                filteredTests = filter.getTests();
                break;
        }

        filteredTests = filteredTests.stream()
                .filter(test -> search.getCheck() == 0 || (search.getCheck() == 1 && test.getResult().getSampleState() == LISEnum.ResultSampleState.CHECKED.getValue()) || (search.getCheck() == 2 && test.getResult().getSampleState() != LISEnum.ResultSampleState.CHECKED.getValue()))
                .filter(test -> search.getTestFilterType() == 0 || (search.getTestFilterType() == 3 ? test.isConfidential() : !test.isConfidential()))
                .filter(test -> !search.isPackageDescription() || test.getPack().getId() != null)
                .filter(test -> search.getLaboratory() != 0 ? test.getLaboratory().getId() == search.getLaboratory() : test.getLaboratory().getId() != 0)
                .filter(test -> search.getSamples() == null || search.getSamples().isEmpty() || search.getSamples().stream().map(sample -> sample.getCodeSample()).collect(Collectors.toList()).contains(test.getSample().getCodesample()))
                .filter(test -> matchStates(test, search.getTestState()))
                .filter(test -> matchSampleStates(test, search.getSampleState()))
                .collect(Collectors.toList());
        return filteredTests;
    }

    @Override
    public Order findOrder(long order) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        return dao.list(order, order, 1, serviceDemographic.list(true), null, null, 0, 0, laboratorys, idbranch)
                .stream()
                .findAny()
                .orElse(null);
    }

    /**
     * Obtiene una lista ordenes con examenes pendientes dentro de un rango de
     * ordenes
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<Order> listPendingExams(Filter search) throws Exception
    {
        try
        {
            int idbranch = JWT.decode(request).getBranch();
            List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

            return dao.listPendingTest(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true),laboratorys, idbranch );
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<Order> getTestChekByBranch(Filter search) throws Exception
    {

        List<Order> orders = new ArrayList<>();
        orders = listFiltersLaboratory(search);
        for (int i = 0; i < orders.size(); i++)
        {
            List<Test> tests = new ArrayList<>();
            List<Test> testsSend = new ArrayList<>();

            tests = dao.getListTest(orders.get(i).getOrderNumber())
                    .stream().filter(test -> test.isShowInQuery()).collect(Collectors.toList());

            for (Test singleTest : tests)
            {

                List<TestBranchCheck> listTestCheckByBranch = new ArrayList<>();

                listTestCheckByBranch = dao.setTestCheckByBranch(orders.get(i).getOrderNumber(), singleTest.getSample().getId(), singleTest.getLaboratory().getId());

                singleTest.setTestCheckByBranch(listTestCheckByBranch);

                testsSend.add(singleTest);

            }

            orders.get(i).setTests(testsSend);

        }

        return orders;

    }

    public List<Order> ordersFilters(Filter search) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Order> list = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), null, search.getTestFilterType() == 2 ? search.getTests() : null, search.getCheck(), search.getRemission(), laboratorys, idbranch);
        List<Order> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != 0)
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographic(filter, search.getDemographics()))
                .map((Order t) -> t.setTests(filterOrderTestsLaboratory(t, search)))
                .map((Order te) -> te.setTests(filterExcludetestbyprofile(te)))
                .filter(filter -> !filter.getTests().isEmpty())
                .collect(Collectors.toList());

        //filters = setComments(filters);
        return filters;
    }

    public List<Order> ordersFiltersRemmision(Filter search) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Order> list = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), null, search.getTestFilterType() == 2 ? search.getTests() : null, search.getCheck(), search.getRemission(), laboratorys, idbranch);
        List<Order> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != 0)
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographic(filter, search.getDemographics()))
                .map((Order t) -> t.setTests(filterOrderTestsLaboratory(t, search)))
                .map((Order te) -> te.setTests(filterRemmision(te)))
                .filter(filter -> !filter.getTests().isEmpty())
                .collect(Collectors.toList());

        //filters = setComments(filters);
        return filters;
    }

    @Override
    public List<Order> getRemissionOrders(Filter search) throws Exception
    {
        getCurrentStates(search);
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        List<Order> list = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), search.getOrders(), search.getTestFilterType() == 2 ? search.getTests() : null, search.getCheck(), 0,laboratorys, idbranch );

        List<Order> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != 0)
                .filter(filter -> search.getOrderType() == 0 || filter.getType().getId() == search.getOrderType())
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographic(filter, search.getDemographics()))
                .map((Order t) -> t.setTests(filterExcludetestbyprofile(t)))
                .map((Order t) -> t.setTests(filterOrderTests(t, search)))
                .filter(filter -> search.isBasic() || !filter.getTests().isEmpty())
                .collect(Collectors.toList());

        //filters = setComments(filters);
        return filters;
    }

    /**
     * Obtiene el estado actual del examen
     *
     * @param filter Filtros
     *
     * @return Filtro con estados correspondientes al examen
     */
    private Filter getCurrentStates(Filter filter)
    {
        filter.setSampleState(new ArrayList<>());
        filter.setTestState(new ArrayList<>());
        filter.setFilterState(filter.getFilterState() == null ? new ArrayList<>() : filter.getFilterState());

        if (filter.getFilterState() != null && filter.getFilterState().size() > 0)
        {
            for (Integer state : filter.getFilterState())
            {
                switch (state)
                {
                    case 1:
                        filter.getSampleState().add(LISEnum.ResultSampleState.COLLECTED.getValue());
                        filter.getTestState().add(LISEnum.ResultTestState.ORDERED.getValue());
                        break;
                    case 2:
                        filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                        filter.getTestState().add(LISEnum.ResultTestState.ORDERED.getValue());
                        break;
                    case 3:
                        filter.getTestState().add(LISEnum.ResultTestState.REPORTED.getValue());
                        break;
                    case 4:
                        filter.getTestState().add(LISEnum.ResultTestState.VALIDATED.getValue());
                        break;
                    case 5:
                        filter.getTestState().add(LISEnum.ResultTestState.DELIVERED.getValue());
                        break;

                }
            }
        }
        return filter;
    }

    /**
     * Indica si el examen cumple con los estados enviados
     *
     * @param test Examen a evaluar
     * @param filterStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private boolean matchStates(Test test, List<Integer> filterStates)
    {
        filterStates = filterStates == null ? new LinkedList<>() : filterStates;
        return filterStates.isEmpty() || filterStates.contains(test.getResult().getState());
    }

    /**
     * Indica si el examen cumple con los estados de la muestra
     *
     * @param test Examen a evaluar
     * @param filterSampleStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private boolean matchSampleStates(Test test, List<Integer> filterSampleStates)
    {
        filterSampleStates = filterSampleStates == null ? new ArrayList<>() : filterSampleStates;
        return filterSampleStates.isEmpty() || filterSampleStates.contains(test.getSampleState());
    }

    /**
     * Filtra la consulta de acuerdo a las opciones enviadas
     *
     * @param order Orden a evaluar
     * @param search lista de examenes filtrados
     *
     * @return true si cumple con el tipo de consulta
     */
    private boolean filterType(OrderList order, Filter search)
    {
        search.setFilterType(search.getFilterType() == null ? new LinkedList<>() : search.getFilterType());

        for (Integer filter : search.getFilterType())
        {
            switch (filter)
            {

                //Edad
                case 1:

                    int year = order.getPatient().getBirthday().getYear();
                    int patientYear = year + 1900;
                    if (!(patientYear >= search.getInitAge() && patientYear <= search.getEndAge()))
                    {
                        return false;
                    }
                    break;
                //Sexo    
                case 2:
                    if (!order.getPatient().getSex().getId().equals(search.getSex()))
                    {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
       
    
    @Override
    public List<OrderBilling> getOrderBilling(String startDate, String endDate) throws Exception
    {
        List<Demographic> listdemos = new LinkedList<>();
        listdemos.add(serviceDemographic.get(17, null, null));     
        return dao.getOrdersBilling(startDate, endDate, listdemos);
    }
    
      /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param filter Orden con examenes a filtrar
     * @param search lista de examenes filtrados
     *
     * @return
     */
    private List<Test> filterOrderTestsRemission(Order filter, Filter search)
    {
        List<Test> filteredTests;
        switch (search.getTestFilterType())
        {
            case 1:
                filteredTests = filter.getTests().stream()
                        .filter(test -> search.getTests().contains(test.getArea().getId()))
                        .collect(Collectors.toList());
                break;
            case 2:
                filteredTests = filter.getTests().stream()
                        .filter(test -> search.getTests().contains(test.getId()))
                        .collect(Collectors.toList());
                break;
            case 3:
                filteredTests = filter.getTests().stream()
                        .filter(test -> search.getTests().contains(test.getId()))
                        .collect(Collectors.toList());
                break;

            case 4:
                filteredTests = filter.getTests().stream()
                        .filter(test -> test.getRemission() == 1)
                        .collect(Collectors.toList());
                break;

            case 5:
                filteredTests = filter.getTests().stream()
                        .filter(test -> test.getRemission() == 1)
                        .filter(test -> search.getTests().contains(test.getId()))
                        .collect(Collectors.toList());
                break;
            default:
                filteredTests = filter.getTests();
                break;
        }

        filteredTests = filteredTests.stream()
                .filter(test -> search.getCheck() == 0 || (search.getCheck() == 1 && test.getResult().getSampleState() == LISEnum.ResultSampleState.CHECKED.getValue()) || (search.getCheck() == 2 && test.getResult().getSampleState() != LISEnum.ResultSampleState.CHECKED.getValue()))
                .filter(test -> search.getTestFilterType() == 0 || (search.getTestFilterType() == 3 ? test.isConfidential() : !test.isConfidential()))
                .filter(test -> !search.isPackageDescription() || test.getPack().getId() != null)
                .collect(Collectors.toList());
        return filteredTests;
    }
    
    @Override
    public List<Order> getListRemissionOrders(Filter search) throws Exception
    {
        getCurrentStates(search);
        List<Order> list = dao.listRemissionOrders(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), search.getOrders(), search.getTestFilterType() == 2 ? search.getTests() : null, search.getCheck(), 0);

        List<Order> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != 0)
                .filter(filter -> search.getOrderType() == 0 || filter.getType().getId() == search.getOrderType())
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographic(filter, search.getDemographics()))
                .map((Order t) -> t.setTests(filterExcludetestbyprofile(t)))
                .map((Order t) -> t.setTests(filterOrderTestsRemission(t, search)))
                .filter(filter -> search.isBasic() || !filter.getTests().isEmpty())
                .collect(Collectors.toList());

        //filters = setComments(filters);
        return filters;
    }
    
    @Override
    public List<RemissionOrderCentral> getRemissionCentralOrders(long order) throws Exception
    {
        return dao.getRemissionCentralOrders(order);
    }
    
    @Override
    public int insertRemmisioncTest(List<RemissionOrderCentral> testList) throws Exception
    {
        return dao.insertRemmisioncTest(testList);
    }
    
    @Override
    public List<RemissionOrderCentral> listremissionCentralOrders(Filter filter) throws Exception
    {
        return dao.listremissionCentralOrders(filter);
    }

}
