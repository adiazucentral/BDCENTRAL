package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFatherSons;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicItemDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.BranchDemographic;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFather;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DemographicRequired;
import net.cltech.enterprisent.domain.masters.demographic.ItemDemographicSon;
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
 * Implementa los servicios de informacion del maestro de Demografico Item para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 09/05/2017
 * @see Creaciòn
 */
@Service
public class DemographicItemServiceEnterpriseNT implements DemographicItemService
{

    @Autowired
    private DemographicItemDao demographicItemDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private DemographicDao demographicDao;

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
    private DemographicService demographicService;
    @Autowired
    private DemographicItemService demographicItemService;

    @Override
    public List<DemographicItem> list() throws Exception
    {
        return demographicItemDao.list();
    }

    @Override
    public DemographicItem create(DemographicItem demographicItem) throws Exception
    {
        List<String> errors = validateFields(false, demographicItem);
        if (errors.isEmpty())
        {
            demographicItem = demographicItemDao.create(demographicItem);
            trackingService.registerConfigurationTracking(null, demographicItem, DemographicItem.class);
            if (demographicItem.getDefaultItem() == true)
            {
                Demographic demographic = demographicDao.get(demographicItem.getDemographic(), null, null);
                demographic.setDemographicItem(demographicItem.getId());
                demographicDao.update(demographic);
            }
            return demographicItem;
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    @Override
    public List<DemographicItem> get(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception
    {
        return demographicItemDao.get(id, code, name, demographic, state);
    }

     @Override
    public List<DemographicItem> getwebquery(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception
    {
        return demographicItemDao.getwebquery(id, code, name, demographic, state);
    }

    
    @Override
    public DemographicItem update(DemographicItem demographicItem) throws Exception
    {
        List<String> errors = validateFields(true, demographicItem);
        if (errors.isEmpty())
        {
            DemographicItem demographicItemval = demographicItemDao.get(demographicItem.getId(), null, null, null, null).get(0);
            trackingService.registerConfigurationTracking(demographicItemval, demographicItem, DemographicItem.class);
            if (demographicItem.getDefaultItem() == true)
            {
                Demographic demographic = demographicDao.get(demographicItem.getDemographic(), null, null);
                demographic.setDemographicItem(demographicItem.getId());
                demographicDao.update(demographic);
            }
            return demographicItemDao.update(demographicItem);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validateFields(boolean isEdit, DemographicItem demographicItem) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();

        //si Esta editando
        if (isEdit)
        {
            if (demographicItem.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (demographicItemDao.get(demographicItem.getId(), null, null, null, null).isEmpty())
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        //Validamos que el Demografico padre que no se null
        if (demographicItem.getDemographic() == null || demographicItem.getDemographic() == 0)
        {
            errors.add("0|Id Demographic");

        } else
        {
            //Validamos que exista el Demografico padre 
            Demographic demographic = demographicDao.get(demographicItem.getDemographic(), null, null);

            if (demographic == null)
            {
                errors.add("2|Id Demographic");
            } else
            {

                if (demographicItem.getName() != null && !demographicItem.getName().isEmpty())
                {
                    //Buscamos el nombre por demografico padre
                    List<DemographicItem> list = demographicItemDao.get(null, null, null, demographicItem.getDemographic(), null);
                    DemographicItem demographicItemrival = null;

                    if (!list.isEmpty())
                    {
                        for (DemographicItem val : list)
                        {
                            if (val.getName().equals(demographicItem.getName()))
                            {
                                demographicItemrival = val;
                                break;
                            }
                        }

                        if (demographicItemrival != null)
                        {
                            if (isEdit)
                            {
                                //comparamos que corresponda al mismo ID
                                if (!Objects.equals(demographicItem.getId(), demographicItemrival.getId()))
                                {
                                    errors.add("1|id");
                                }
                            } else
                            {
                                //guardar
                                errors.add("1|Name");
                            }
                        }
                    }
                } else
                {
                    errors.add("0|Name");
                }

                if (demographicItem.getCode() != null && !demographicItem.getCode().isEmpty())
                {
                    //Buscamos el codigo por demografico padre
                    List<DemographicItem> list = demographicItemDao.get(null, null, null, demographicItem.getDemographic(), null);
                    DemographicItem demographicItemrival = null;

                    if (!list.isEmpty())
                    {
                        for (DemographicItem val : list)
                        {
                            if (val.getCode().equals(demographicItem.getCode()))
                            {
                                demographicItemrival = val;
                                break;
                            }
                        }
                        if (demographicItemrival != null)
                        {
                            if (isEdit)
                            {
                                //comparamos que corresponda al mismo ID
                                if (!Objects.equals(demographicItem.getId(), demographicItemrival.getId()))
                                {
                                    errors.add("1|Code");
                                }
                            } else
                            {
                                //guardar
                                errors.add("1|Code");
                            }
                        }
                    }

                } else
                {
                    errors.add("0|Code");
                }
                if (demographicItem.getDefaultItem() == null)
                {
                    errors.add("0|DefaultItem");
                }

            }
        }
        return errors;
    }

    @Override
    public List<BranchDemographic> demographicsItemList(int idBranch, int idDemographic) throws Exception
    {
        List<BranchDemographic> demographicItems;
        List<BranchDemographic> demographics = demographicItemDao.demographicsItemList(idBranch, idDemographic);
        switch (idDemographic)
        {
            case Constants.ACCOUNT:
                demographicItems = accountService.list(true).stream()
                        .map(account -> new BranchDemographic(idBranch, idDemographic, account.getId(), account.getName(), account.getEpsCode(), getBranchByDemographics(idBranch, idDemographic, account.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.PHYSICIAN:
                demographicItems = physicianService.filterByState(true).stream()
                        .map(physician -> new BranchDemographic(idBranch, idDemographic, physician.getId(), physician.getLastName() + " " + physician.getName(), physician.getCode(), getBranchByDemographics(idBranch, idDemographic, physician.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.RATE:
                demographicItems = rateService.list(true).stream()
                        .map(rate -> new BranchDemographic(idBranch, idDemographic, rate.getId(), rate.getName(), rate.getCode(), getBranchByDemographics(idBranch, idDemographic, rate.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.ORDERTYPE:
                demographicItems = orderTypeService.filterByState(true).stream()
                        .map(orderType -> new BranchDemographic(idBranch, idDemographic, orderType.getId(), orderType.getName(), orderType.getCode(), getBranchByDemographics(idBranch, idDemographic, orderType.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.BRANCH:
                demographicItems = branchService.list(true).stream()
                        .map(branch -> new BranchDemographic(idBranch, idDemographic, branch.getId(), branch.getName(), branch.getCode(), getBranchByDemographics(idBranch, idDemographic, branch.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.SERVICE:
                demographicItems = serviceService.filterByState(true).stream()
                        .map(service -> new BranchDemographic(idBranch, idDemographic, service.getId(), service.getName(), service.getCode(), getBranchByDemographics(idBranch, idDemographic, service.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.RACE:
                demographicItems = raceService.filterByState(true).stream()
                        .map(race -> new BranchDemographic(idBranch, idDemographic, race.getId(), race.getName(), race.getCode(), getBranchByDemographics(idBranch, idDemographic, race.getId())))
                        .collect(Collectors.toList());
                break;
            case Constants.DOCUMENT_TYPE:
                demographicItems = documentService.list(true).stream()
                        .map(documentType -> new BranchDemographic(idBranch, idDemographic, documentType.getId(), documentType.getName(), documentType.getAbbr(), getBranchByDemographics(idBranch, idDemographic, documentType.getId())))
                        .collect(Collectors.toList());
                break;
            default:
                demographicItems = demographicItemService.get(null, null, null, idDemographic, null).stream()
                        .filter(filter -> filter.isState())
                        .map(demographicItem -> new BranchDemographic(idBranch, idDemographic, demographicItem.getId(), demographicItem.getName(), demographicItem.getCode(), getBranchByDemographics(idBranch, idDemographic, demographicItem.getId())))
                        .collect(Collectors.toList());
                ;
                break;
        }

        return demographicItems;
    }

    /**
     * Obtiene los codigos de homologación de demografico item
     *
     *
     * @param branch
     * @param idDemographics
     * @param idDemographicItem
     * @return
     */
    public boolean getBranchByDemographics(int branch, int idDemographics, int idDemographicItem)
    {
        try
        {
            return demographicItemDao.validateBranchDemographic(branch, idDemographics, idDemographicItem);

        } catch (Exception ex)
        {
            return false;
        }
        //return false;
    }

    @Override
    public void saveDemographicBranch(List<BranchDemographic> demographics) throws Exception
    {

        // Obtengo la lista de maestros antigua
        List<BranchDemographic> oldMastersList = demographicItemDao.getDemographicsBranch(demographics);
        // Elimino todos los registros de los demograficos que vayan llegando en la lista de la tabla
        demographicItemDao.deleteDemographicsBranch(demographics);
        // Se realiza una inserción en batch de la lista en la tabla
        demographicItemDao.createDemographicsBranch(demographics);
        // Inserto la trazabilidad:
        trackingService.registerConfigurationTracking(oldMastersList, demographics, BranchDemographic.class);

    }

    @Override
    public List<DemographicBranch> demographics(int idBranch) throws Exception
    {
        List<DemographicBranch> demographics = demographicItemDao.demographicsBranch(idBranch);
        return demographics;
    }

    @Override
    public void demographicBranchSave(List<DemographicBranch> demographics) throws Exception
    {
        // Obtengo la lista de maestros antigua
        List<DemographicBranch> oldMastersList = demographicItemDao.branchgetDemographics(demographics);
        // Elimino todos los registros de los demograficos que vayan llegando en la lista de la tabla
        demographicItemDao.deleteBranchDemographics(demographics.get(0).getIdBranch());
        // Se realiza una inserción en batch de la lista en la tabla
        demographicItemDao.createBranchDemographics(demographics);
        // Inserto la trazabilidad:
        trackingService.registerConfigurationTracking(oldMastersList, demographics, DemographicBranch.class);
    }

    @Override
    public void demographicValueRequired(List<DemographicRequired> demographics) throws Exception
    {

        int value = demographicItemDao.demographicValueRequired(demographics);
        if (value != 1)
        {
            throw new EnterpriseNTException("Dont register updates");

        }

    }

    @Override
    public List<ItemDemographicSon> listSons(int father, int idFatherItem, int idSon) throws Exception
    {
        try
        {
            List<ItemDemographicSon> listItemsSons = new ArrayList<>();

            listItemsSons = demographicItemDao.getlisDemographicItemstSons(father, idFatherItem, idSon);

            return listItemsSons;

        } catch (Exception ex)
        {
            throw new EnterpriseNTException("Dont Item Demographic Son");
        }

    }

    @Override
    public DemographicFather updateDemographicFather(DemographicFather demographicFather) throws Exception
    {
        try
        {

            demographicItemDao.deleteDemographicFather(demographicFather);

            int idFather = demographicFather.getIdDemographicFather();
            int idFatheriItem = demographicFather.getIdDemographicFatherItem();
            int idSon = demographicFather.getIdDemographicSon();
            int idUser = demographicFather.getUser().getId();
            int idItemSon;

            for (int i = 0; i < demographicFather.getDemographicSonItems().size(); i++)
            {

                idItemSon = demographicFather.getDemographicSonItems().get(i).getId();

                demographicItemDao.createDemographicFather(idFather, idFatheriItem, idSon, idItemSon, idUser);

            }

            return demographicFather;

        } catch (Exception ex)
        {
            throw new EnterpriseNTException("Dont working the insert");
        }
    }

    @Override
    public DemographicFatherSons listSon(int father, int idFatheriItem) throws Exception
    {
        try
        {

            DemographicFatherSons bean = new DemographicFatherSons();
            int valueId;

            valueId = demographicItemDao.getValueIdDemographicSon(father, idFatheriItem);

            List<Integer> idITems = new ArrayList<>();

            idITems = demographicItemDao.getidItems(father, idFatheriItem, valueId);

            bean.setIdDemographicSon(valueId);
            bean.setDemographicSonItems(idITems);

            return bean;

        } catch (Exception ex)
        {
            throw new EnterpriseNTException("Dont exit register");
        }

    }

    @Override
    public Integer getIdDemographicSon(int father) throws Exception
    {

        try
        {
            Integer idDemographicSon;

            idDemographicSon = demographicItemDao.getIdDemographicSon(father);

            return idDemographicSon;

        } catch (Exception ex)
        {
            throw new EnterpriseNTException("Error");
        }

    }

}
