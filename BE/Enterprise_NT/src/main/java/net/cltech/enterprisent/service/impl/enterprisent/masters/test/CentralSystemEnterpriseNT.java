package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.test.CentralSystemDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.StandardizationDemographic;
import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import net.cltech.enterprisent.domain.masters.test.HomologationCode;
import net.cltech.enterprisent.domain.masters.test.Standardization;
import net.cltech.enterprisent.domain.masters.user.StandardizationUser;
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
import net.cltech.enterprisent.service.interfaces.masters.test.CentralSystemService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Sistema Central para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 12/04/2017
 * @see Creaciòn
 */
@Service
public class CentralSystemEnterpriseNT implements CentralSystemService
{

    @Autowired
    private CentralSystemDao dao;
    @Autowired
    private CentralSystemService centralSystemService;
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
    public List<CentralSystem> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public CentralSystem create(CentralSystem centralSystem) throws Exception
    {
        List<String> errors = validateFields(false, centralSystem);
        if (errors.isEmpty())
        {
            CentralSystem created = dao.create(centralSystem);
            trackingService.registerConfigurationTracking(null, created, CentralSystem.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public CentralSystem get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public CentralSystem update(CentralSystem centralSystem) throws Exception
    {
        List<String> errors = validateFields(true, centralSystem);
        if (errors.isEmpty())
        {
            CentralSystem centralSystemC = dao.get(centralSystem.getId(), null);
            CentralSystem modifited = dao.update(centralSystem);
            trackingService.registerConfigurationTracking(centralSystemC, modifited, CentralSystem.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<CentralSystem> list(boolean state) throws Exception
    {
        return new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((CentralSystem) o).isState() == state));
    }

    @Override
    public List<Standardization> standardizationList(int centralSystem, boolean all) throws Exception
    {
        List<Standardization> groupedList = new LinkedList<>();
        if(centralSystem > 0){
           
            List<Standardization> standardizationList = dao.standardizationList(centralSystem).stream()
                    .filter(central -> (all || central.getTestType() == 0 || central.getTestType() == 1))
                    .collect(Collectors.toList());

            if (standardizationList != null && !standardizationList.isEmpty())
            {
                standardizationList.forEach((standardization) ->
                {
                    if (!groupedList.contains(standardization))
                    {
                        groupedList.add(standardization);
                    } else
                    {
                        groupedList.get(groupedList.indexOf(standardization)).getCodes().add(standardization.getCodes().get(0));
                    }
                });
            }
        }

        return groupedList;
    }
    
     @Override
    public List<HomologationCode> gethomologationcodes(int centralSystem) throws Exception
    {
        List<HomologationCode> groupedList = new LinkedList<>();
        if(centralSystem > 0){
           
            groupedList = dao.getHomologationCode(centralSystem);
        }

        return groupedList;
    }

    @Override
    public boolean standardizationCodeExists(int centralSystem, String code, int test) throws Exception
    {
        return standardizationList(centralSystem, false).stream()
                .filter(filter -> filter.getCodes().contains(code))
                .anyMatch(filter -> !filter.getId().equals(test));

    }

    @Override
    public Standardization addStandardizationTest(final Standardization standardization) throws Exception
    {
        List<String> errors = validateStandardizationField(standardization);
        if (errors.isEmpty())
        {
            Standardization previous = standardizationList(standardization.getCentralSystem().getId(), false)
                    .stream()
                    .filter((Standardization s) -> Objects.equals(s.getId(), standardization.getId()))
                    .findFirst()
                    .orElse(null);
            previous.setName("");
            Standardization current = dao.insertStandardization(standardization);
            trackingService.registerConfigurationTracking(previous, current, Standardization.class);
            return standardization;
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    @Override
    public List<StandardizationDemographic> demographicsItemList(int idCentralSystem, int idDemographic) throws Exception
    {
        List<StandardizationDemographic> demographicItems;
        List<StandardizationDemographic> demographics = dao.demographicsItemList(idCentralSystem, idDemographic);
        switch (idDemographic)
        {
            case Constants.ACCOUNT:
                demographicItems = accountService.list(true).stream()
                        .map(account -> new StandardizationDemographic(idCentralSystem, idDemographic, account.getId(), account.getName(), account.getEpsCode(), getCodes(demographics, account.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.PHYSICIAN:
                demographicItems = physicianService.filterByState(true).stream()
                        .map(physician -> new StandardizationDemographic(idCentralSystem, idDemographic, physician.getId(), physician.getLastName() + " " + physician.getName(), physician.getCode(), getCodes(demographics, physician.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.RATE:
                demographicItems = rateService.list(true).stream()
                        .map(rate -> new StandardizationDemographic(idCentralSystem, idDemographic, rate.getId(), rate.getName(), rate.getCode(), getCodes(demographics, rate.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.ORDERTYPE:
                demographicItems = orderTypeService.filterByState(true).stream()
                        .map(orderType -> new StandardizationDemographic(idCentralSystem, idDemographic, orderType.getId(), orderType.getName(), orderType.getCode(), getCodes(demographics, orderType.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.BRANCH:
                demographicItems = branchService.list(true).stream()
                        .map(branch -> new StandardizationDemographic(idCentralSystem, idDemographic, branch.getId(), branch.getName(), branch.getCode(), getCodes(demographics, branch.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.SERVICE:
                demographicItems = serviceService.filterByState(true).stream()
                        .map(service -> new StandardizationDemographic(idCentralSystem, idDemographic, service.getId(), service.getName(), service.getCode(), getCodes(demographics, service.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.RACE:
                demographicItems = raceService.filterByState(true).stream()
                        .map(race -> new StandardizationDemographic(idCentralSystem, idDemographic, race.getId(), race.getName(), race.getCode(), getCodes(demographics, race.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.DOCUMENT_TYPE:
                demographicItems = documentService.list(true).stream()
                        .map(documentType -> new StandardizationDemographic(idCentralSystem, idDemographic, documentType.getId(), documentType.getName(), documentType.getAbbr(), getCodes(demographics, documentType.getId())))
                        .collect(Collectors.toList());
                break;
            default:
                demographicItems = demographicItemService.get(null, null, null, idDemographic, null).stream()
                        .filter(filter -> filter.isState())
                        .map(demographicItem -> new StandardizationDemographic(idCentralSystem, idDemographic, demographicItem.getId(), demographicItem.getName(), demographicItem.getCode(), getCodes(demographics, demographicItem.getId())))
                        .collect(Collectors.toList());
                ;
                break;
        }

        return demographicItems;
    }

    @Override
    public StandardizationDemographic insertStandardizationDemographic(StandardizationDemographic demographic) throws Exception
    {
        List<String> errors = validateStandardizationDemographicsField(demographic);
        if (errors.isEmpty())
        {
            StandardizationDemographic previous = demographicsItemList(demographic.getId(), demographic.getDemographic().getId())
                    .stream()
                    .filter((StandardizationDemographic t) -> Objects.equals(demographic.getDemographicItem().getId(), t.getDemographicItem().getId())).findFirst().orElse(null);
            StandardizationDemographic current = dao.insertStandardizationDemographic(demographic);
            List<Demographic> list = demographicService.demographicsList();
            previous.getDemographic().setName(list.stream().filter((Demographic d) -> Objects.equals(d.getId(), previous.getDemographic().getId())).map((Demographic d) -> d.getName()).findFirst().orElse(null));
            current.getDemographic().setName(list.stream().filter((Demographic d) -> Objects.equals(d.getId(), current.getDemographic().getId())).map((Demographic d) -> d.getName()).findFirst().orElse(null));
            List<CentralSystem> listCentral = centralSystemService.list(true);
            previous.setCentralName(listCentral.stream().filter((CentralSystem d) -> Objects.equals(d.getId().toString(), previous.getCentralCode().isEmpty() ? "" : previous.getCentralCode().get(0))).map((CentralSystem d) -> d.getName()).findFirst().orElse(null));
            current.setCentralName(listCentral.stream().filter((CentralSystem d) -> Objects.equals(d.getId().toString(), current.getCentralCode().isEmpty() ? "" : current.getCentralCode().get(0))).map((CentralSystem d) -> d.getName()).findFirst().orElse(null));
            trackingService.registerConfigurationTracking(previous, current, StandardizationDemographic.class);
            return current;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Obtiene los codigos de homologación de demografico item
     *
     * @param demographics Lista de Demograficos items con codigo de
     * homologación
     * @param id Id Demografico Item
     *
     * @return lista de codigos de homologación
     */
    public List<String> getCodes(List<StandardizationDemographic> demographics, Integer id)
    {
        return demographics.stream()
                .filter(demo -> demo.getDemographicItem().getId().equals(id))
                .map(demo -> demo.getCentralCode().get(0).toUpperCase())
                .collect(Collectors.toList());
    }

    @Override
    public boolean standardizationCodeExists(int centralSystem, String code, int demographic, int demographicItem) throws Exception
    {
        return demographicsItemList(centralSystem, demographic).stream()
                .filter(filter -> filter.getCentralCode().contains(code))
                .anyMatch(filter -> !filter.getDemographicItem().getId().equals(demographicItem));
    }

    @Override
    public int insertStandardizationDemographicAll(List<StandardizationDemographic> demographics) throws Exception
    {
        long time_start, time_end;
        time_start = System.currentTimeMillis();

        List<StandardizationDemographic> validate = validateFieldsImport(demographics);

        time_end = System.currentTimeMillis();
        System.err.println("validando en " + (time_end - time_start) + " milliseconds");
        if (!validate.isEmpty())
        {
            time_start = System.currentTimeMillis();
            List<StandardizationDemographic> diagnosticsval = validate.stream()
                    .distinct()
                    .collect(Collectors.toList());

            int registers = dao.createAllStandardizationDemographics(diagnosticsval);
            time_end = System.currentTimeMillis();
            System.err.println("Insertando en " + (time_end - time_start) + " milliseconds");
            return registers;
        } else
        {
            return 0;
        }
    }

    @Override
    public List<StandardizationUser> usersList(int idCentralSystem) throws Exception
    {
        return dao.usersList(idCentralSystem);
    }

    @Override
    public StandardizationUser insertStandardizationUser(StandardizationUser user) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (user.getId() == null || user.getId() == 0)
        {
            errors.add("0|id");
        }

        if (user.getUserStandardization().getId() == null || user.getUserStandardization().getId() == 0)
        {
            errors.add("0|userStandardization");
        }

        if (errors.isEmpty())
        {
            trackingService.registerConfigurationTracking(null, user, StandardizationUser.class);
            return dao.insertStandardizationUser(user);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean standardizationUserCodeExists(int centralSystem, String code, int user) throws Exception
    {
        return dao.usersList(centralSystem).stream()
                .filter(filter -> filter.getCentralCode().toUpperCase().equals(code))
                .anyMatch(filter -> !filter.getUserStandardization().getId().equals(user));
    }

    /**
     * Valida campos obligatorios para la homologación
     *
     *
     *
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, CentralSystem centralSystem) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (centralSystem.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(centralSystem.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (centralSystem.getName() != null && !centralSystem.getName().isEmpty())
        {
            CentralSystem centralSystemC = dao.get(null, centralSystem.getName());
            if (centralSystemC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(centralSystem.getId(), centralSystemC.getId()))
                    {
                        errors.add("1|name");
                    }
                } else
                {
                    errors.add("1|name");
                }
            }
        } else
        {
            errors.add("0|name");
        }

        if (centralSystem.getUser().getId() == null || centralSystem.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    /**
     * Valida campos obligatorios para la homologación
     *
     * @param standardization informacion de homologación
     *
     * @return lista de errores encontrados 0 -> Datos vacios
     * @throws Exception Error en el
     */
    private List<String> validateStandardizationField(Standardization standardization) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (standardization.getCentralSystem().getId() == null)
        {
            errors.add("0|centralSystem");
        }
        if (standardization.getId() == null)
        {
            errors.add("0|test");
        }
        return errors;
    }

    /**
     * Valida campos obligatorios para la homologación
     *
     * @param demographic informacion de homologación
     *
     * @return lista de errores encontrados 0 -> Datos vacios
     * @throws Exception Error en el
     */
    private List<String> validateStandardizationDemographicsField(StandardizationDemographic demographic) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (demographic.getId() == null || demographic.getId() == 0)
        {
            errors.add("0|centralSystem");
        }

        if (demographic.getDemographic().getId() == null || demographic.getDemographic().getId().equals(0))
        {
            errors.add("0|demographic");
        }

        if (demographic.getDemographicItem().getId() == null || demographic.getDemographicItem().getId().equals(0))
        {
            errors.add("0|demographic item");
        }

        int i = 1;
        for (String code : demographic.getCentralCode())
        {
            if (standardizationCodeExists(demographic.getId(), code, demographic.getDemographic().getId(), demographic.getDemographicItem().getId()))
            {
                errors.add("1|code|" + i);
            }
            i++;
        }

        return errors;
    }

    private List<StandardizationDemographic> validateFieldsImport(List<StandardizationDemographic> demographics) throws Exception
    {
        List<String> errors = new ArrayList<>();
        List<StandardizationDemographic> valid = new ArrayList<>();
        List<StandardizationDemographic> saved = dao.demographicsItemList(demographics.get(0).getId(), demographics.get(0).getDemographic().getId());

        int i = 1;
        for (StandardizationDemographic demographicVal : demographics)
        {
            //validamos el codigo
            if (!standardizationCodeExists(demographicVal.getId(), demographicVal.getCentralCode().get(0), demographicVal.getDemographic().getId(), demographicVal.getDemographicItem().getId()) && !alreadyExistsCodeList(demographicVal, demographics))
            {
                if (!alreadyExistsCode(demographicVal, saved))
                {
                    if (demographicVal.getCentralCode() == null || demographicVal.getCentralCode().isEmpty())
                    {
                        errors.add("0|id|" + i);
                    }

                    if (demographicVal.getDemographic().getId() == null || demographicVal.getDemographic().getId().equals(0))
                    {
                        errors.add("0|demographic|" + i);
                    }

                    if (demographicVal.getDemographicItem().getId() == null || demographicVal.getDemographicItem().getId().equals(0))
                    {
                        errors.add("0|demographicItem|" + i);
                    }

                    valid.add(demographicVal);
                }
            } else
            {
                errors.add("3|centralCode|" + i);
            }
            i++;
        }

        if (!errors.isEmpty())
        {
            throw new EnterpriseNTException(errors);
        }

        return valid;
    }

    /**
     * Metodo para verificar si el codigo del diagnostico existe en una lista
     *
     * @param demographic
     * @param searchList lista de busqueda
     *
     * @return si existe en la lista
     */
    public boolean alreadyExistsCode(StandardizationDemographic demographic, List<StandardizationDemographic> searchList)
    {
        return searchList.stream()
                .anyMatch(m -> m.getCentralCode().get(0).trim().equalsIgnoreCase(demographic.getCentralCode().get(0).trim()));
    }

    /**
     * Metodo para verificar si el codigo del diagnostico existe en una lista
     *
     * @param demographic
     * @param searchList lista de busqueda
     *
     * @return si existe en la lista
     */
    public boolean alreadyExistsCodeList(StandardizationDemographic demographic, List<StandardizationDemographic> searchList)
    {
        return searchList.stream()
                .filter(m -> m.getCentralCode().get(0).trim().equalsIgnoreCase(demographic.getCentralCode().get(0).trim()))
                .anyMatch(m -> !m.getDemographicItem().getId().equals(demographic.getDemographicItem().getId()));
    }

}
