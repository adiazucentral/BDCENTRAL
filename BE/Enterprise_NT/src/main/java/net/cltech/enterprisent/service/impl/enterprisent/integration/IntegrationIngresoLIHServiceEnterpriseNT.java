package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoLIHDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.CentralSystemDao;
import net.cltech.enterprisent.domain.integration.ingresoLIH.Items;
import net.cltech.enterprisent.domain.integration.ingresoLIH.OrdenLaboratorio;
import net.cltech.enterprisent.domain.masters.demographic.StandardizationDemographic;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoLIHService;
import net.cltech.enterprisent.service.interfaces.masters.billing.RateService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AccountService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicItemService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DocumentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.OrderTypeService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.PhysicianService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.RaceService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de Integracion de ingreso para Enterprise NT
 *
 * @version 1.0.0
 * @author BValero
 * @since 23/04/2020
 * @see Creacion
 */
@Service
public class IntegrationIngresoLIHServiceEnterpriseNT implements IntegrationIngresoLIHService
{

    @Autowired
    private IntegrationIngresoLIHDao daoLIH;
    @Autowired
    private CentralSystemDao dao;
    @Autowired
    private DemographicItemService demographicItemService;
    /*Demograficos fijos*/
    @Autowired
    private AccountService accountService;
    @Autowired
    private PhysicianService physicianService;
    @Autowired
    private RateService rateService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private RaceService raceService;
    @Autowired
    private DocumentTypeService documentService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private DemographicService demographicService;

    @Override
    public List<OrdenLaboratorio> getDataByCentralSystem(int CentralSystem, long orderInitial, long orderFinal) throws Exception
    {
        List<OrdenLaboratorio> objRespons;

        objRespons = daoLIH.getDataByCentralSystem(CentralSystem, orderInitial, orderFinal).stream()
                .distinct().collect(Collectors.toList());

        for (OrdenLaboratorio order : objRespons)
        {
            //Se obtiene el comentario quitando el objeto donde esta almacenado y los tags
            if (order.getDatosGenerales().getObserv() != null && !order.getDatosGenerales().getObserv().isEmpty())
            {
                String str1 = "<p>";
                String str2 = "</p>";
                int intIndex;
                int intIndex2;
                String comment = "";
                if (order.getDatosGenerales().getObserv().contains(str2))
                {
                    intIndex = order.getDatosGenerales().getObserv().indexOf(str1) + 3;
                    intIndex2 = order.getDatosGenerales().getObserv().indexOf(str2);
                    comment = order.getDatosGenerales().getObserv().substring(intIndex, intIndex2);
                }
                else
                {
                    comment = order.getDatosGenerales().getObserv();
                }
                
                order.getDatosGenerales().setObserv(comment);
            }
            //Se cambia el genero
            if (order.getPaciente().getGenero().equalsIgnoreCase("Masculino"))
            {
                order.getPaciente().setGenero("M");
            }
            else if (order.getPaciente().getGenero().equalsIgnoreCase("Femenino"))
            {
                order.getPaciente().setGenero("F");
            }
            //Se cambia Urgencia
            String urgente = order.getPaciente().getUrgente().equalsIgnoreCase("R") ? "N" : "S";
            order.getPaciente().setUrgente(urgente);
            //Se homologa el tipo de documento y se obtiene su codigo de homologación
            // demographicsItemCentralCode(CentralSystem, Constants.DOCUMENT_TYPE, Integer.parseInt(order.getPaciente().getTipoIdentificacion()));
            
            // Se obtiene la abreviatura del tipo de documento
            String abbrDocumentType = documentService.list(true)
                    .stream()
                    .map(documentType -> documentType.getId().equals(Integer.parseInt(order.getPaciente().getTipoIdentificacion())) ? documentType.getAbbr() : "")
                    .findAny().orElse(null);
            
            order.getPaciente().setTipoIdentificacion(abbrDocumentType);
            //Se obtienen los examenes de cada orden
            List<Items> listTest = daoLIH.getDataTest(CentralSystem, Long.parseLong(order.getDatosGenerales().getNumeroOrden()));
            //Se agregan los examenes al objeto de la orden
            order.setItems(listTest);
            // Se formatea la fecha de nacimiento para enviarse con el formato requerido por LIH
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate;
            try
            {
                startDate = df.parse(order.getPaciente().getFechaNacimiento());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String fechaNacimientoFormat = formatter.format(startDate);
                order.getPaciente().setFechaNacimiento(fechaNacimientoFormat);
            }
            catch (ParseException e)
            {
                e.getCause().getMessage();
            }
        }

        return objRespons;
    }

    /**
     * Obtiene los codigos de homologación de demografico item
     *
     * @param demographics Lista de Demograficos items con codigo de homologación
     * @param id Id Demografico Item
     *
     * @return lista de codigos de homologación
     */
    public String getCodes(List<StandardizationDemographic> demographics, Integer id)
    {
        return demographics.stream()
                .filter(demo -> demo.getDemographicItem().getId().equals(id))
                .map(demo -> demo.getCentralCode().get(0).toUpperCase())
                .findAny().orElse(null);
    }

    public String demographicsItemCentralCode(int idCentralSystem, int idDemographic, int idItemDemographic) throws Exception
    {

        String CentralCodeItem = "";

        List<StandardizationDemographic> demographics = dao.demographicsItemList(idCentralSystem, idDemographic);
        switch (idDemographic)
        {
            case Constants.ACCOUNT:
                CentralCodeItem = accountService.list(true).stream()
                        .map(account -> account.getId().equals(idItemDemographic) ? getCodes(demographics, account.getId()) : "")
                        .findAny().orElse(null);
                break;
            case Constants.PHYSICIAN:
                CentralCodeItem = physicianService.filterByState(true).stream()
                        .map(physician -> physician.getId().equals(idItemDemographic) ? getCodes(demographics, physician.getId()) : "")
                        .findAny().orElse(null);
                break;
            case Constants.RATE:
                CentralCodeItem = rateService.list(true).stream()
                        .map(rate -> rate.getId().equals(idItemDemographic) ? getCodes(demographics, rate.getId()) : "")
                        .findAny().orElse(null);
                break;
            case Constants.ORDERTYPE:
                CentralCodeItem = orderTypeService.filterByState(true).stream()
                        .map(orderType -> orderType.getId().equals(idItemDemographic) ? getCodes(demographics, orderType.getId()) : "")
                        .findAny().orElse(null);
                break;
            case Constants.BRANCH:
                CentralCodeItem = branchService.list(true).stream()
                        .map(branch -> branch.getId().equals(idItemDemographic) ? getCodes(demographics, branch.getId()) : "")
                        .findAny().orElse(null);
                break;
            case Constants.SERVICE:
                CentralCodeItem = serviceService.filterByState(true).stream()
                        .map(service -> service.getId().equals(idItemDemographic) ? getCodes(demographics, service.getId()) : "")
                        .findAny().orElse(null);
                break;
            case Constants.RACE:
                CentralCodeItem = raceService.filterByState(true).stream()
                        .map(race -> race.getId().equals(idItemDemographic) ? getCodes(demographics, race.getId()) : "")
                        .findAny().orElse(null);
                break;
            case Constants.DOCUMENT_TYPE:
                CentralCodeItem = documentService.list(true).stream()
                        .map(documentType -> documentType.getId().equals(idItemDemographic) ? getCodes(demographics, documentType.getId()) : "")
                        .findAny().orElse(null);
                break;
            default:
                CentralCodeItem = demographicItemService.get(null, null, null, idDemographic, null).stream()
                        .filter(filter -> filter.isState())
                        .map(demographicItem -> demographicItem.getId().equals(idItemDemographic) ? getCodes(demographics, demographicItem.getId()) : "")
                        .findAny().orElse(null);
                ;
                break;
        }

        return CentralCodeItem;
    }

    @Override
    public int updateOrderStateSendLIH(OrdenLaboratorio order) throws Exception
    {
        int res = 0;
        long numberOrder = 0;

        numberOrder = Long.parseLong(order.getDatosGenerales().getNumeroOrden());

        res = daoLIH.updateOrderStateSendLIH(numberOrder);

        return res;
    }
}
