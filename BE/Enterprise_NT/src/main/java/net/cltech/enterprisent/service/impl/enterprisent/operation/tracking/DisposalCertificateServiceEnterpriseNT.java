package net.cltech.enterprisent.service.impl.enterprisent.operation.tracking;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.tracking.RackDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.DisposalCertificateDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.tracking.DisposalCertificate;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.impl.enterprisent.masters.tracking.RackServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.RackService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.audit.AuditService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.DisposalCertificateService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a nevera
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 28/05/2018
 * @see Creación
 */
@Service
public class DisposalCertificateServiceEnterpriseNT implements DisposalCertificateService
{

    @Autowired
    private TrackingService trackingService;
    @Autowired
    private DisposalCertificateDao dao;
    @Autowired
    private RackDao rackDao;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private ConfigurationService configService;
    @Autowired
    private RackService rackService;
    @Autowired
    private AuditService auditService;

    @Override
    public DisposalCertificate create(DisposalCertificate create) throws Exception
    {
        create.setCreationUser(trackingService.getRequestUser());
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            DisposalCertificate newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, DisposalCertificate.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<DisposalCertificate> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public DisposalCertificate filterById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public DisposalCertificate filterByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.trim().equalsIgnoreCase(a.getName()));
    }

    @Override
    public DisposalCertificate update(DisposalCertificate update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            DisposalCertificate old = filterById(update.getId());
            DisposalCertificate updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, DisposalCertificate.class);
            return updated;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(DisposalCertificate validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        List<DisposalCertificate> all = dao.list();
        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (filterById(validate.getId()) == null)
                {
                    errors.add("2|id");//No existe id
                }
            }
        }
        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|name");
        } else
        {
            DisposalCertificate found = findBy(all, a -> a.getName().equalsIgnoreCase(validate.getName()));

            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }
        }

        if (validate.getDescription() == null || validate.getDescription().trim().isEmpty())
        {
            errors.add("0|description");
        }
        if (validate.getType() == null || !Arrays.asList(1, 2).contains(validate.getType()))
        {
            errors.add("0|type");
        }

        return errors;

    }

    /**
     * Busqueda por una expresion
     *
     * @param alarmList listado en el que se realiza la busqueda
     * @param predicate criterio de busqueda
     *
     * @return Objeto encontrado, null si no se encuentra
     */
    public DisposalCertificate findBy(List<DisposalCertificate> alarmList, Predicate<DisposalCertificate> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

    @Override
    public int addRacks(DisposalCertificate add) throws Exception
    {
        int disposalDestination = configService.getIntValue(Configuration.KEY_DISPOSAL_DESTINATION);
        int affected = dao.assignCertificateByRacks(add.getRacks(), trackingService.getRequestUser().getId());
//        if (affected > 0)
//        {
            //marca gradillas como desechadas
            
            List<Integer> idsRacks = add.getRacks();
            
            rackService.dispouse(idsRacks);

            List<Rack> listToDisposal = rackService.list().stream().filter( rack -> idsRacks.contains(rack.getId())).collect(Collectors.toList());

            if( listToDisposal.size() > 0 ) {
                rackDao.insertRackAuditToDisposal(listToDisposal, add.getId());
            }

            idsRacks.forEach( r -> {
                try {
                    Rack rack = rackService.filterById(r);
                    // Se lleva a cabo una consulta para obtener la gradilla que sera ingresada en la auditoria de la gradilla:
                    List<RackDetail> rackDetail = rackService.listRackDetail(r)
                            .stream()
                            .filter(rackD -> Objects.equal(rackD.getRack().getId(), r))
                            .filter(rackD -> !rackD.isDiscard())
                            .collect(Collectors.toList());
                    
                    if (rackDetail.size() > 0)
                    {
                        rackDao.insertRackAudit(rackDetail, rack.getReusable(), add);
                        // Eliminar detalle y re escribirlo sobre auditoria.
                        rackDao.removeSamples(rackDetail);
                    } else {
                        if (rack.getReusable())
                        {
                            rackDao.reopen(rack.getId());
                        }
                    }
                    
                    DisposalCertificate disposal = listDetail(add.getId());
                    if(disposal != null) {
                        //verifica destino y envio a auditoria
                        disposal.getPositions().stream()
                                .forEach(detail ->
                                {
                                    if (disposalDestination != -1)
                                    {
                                        checkDestination(detail.getOrder(), detail.getSample().getCodesample(), disposalDestination);
                                    }
                                    
                                    auditService.auditSampleStorage(detail.getRack().getId(), detail.getPosition(), AuditOperation.ACTION_UPDATE);
                                });
                    }   } catch (Exception ex) {
                    Logger.getLogger(DisposalCertificateServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        //}

        return affected;
    }

    @Override
    public int addSampleByPosition(DisposalCertificate add) throws Exception
    {
        int disposalDestination = configService.getIntValue(Configuration.KEY_DISPOSAL_DESTINATION);
        int affected = dao.assignCertificateByPosition(add.getPosition().getRack().getId(), add.getPosition().getPosition(), trackingService.getRequestUser().getId());
        if (affected > 0)
        {

            RackDetail detail = rackService.getSampleDetail(add.getPosition().getRack().getId(), add.getPosition().getPosition());
            boolean markRackForDispose = rackService.listRackDetail(add.getPosition().getRack().getId())
                    .stream()
                    .noneMatch(position -> position.getCertificate() == null);
            if (markRackForDispose)
            {
                rackService.dispouse(Arrays.asList(add.getPosition().getRack().getId()));
            }
            if (detail != null)
            {
                if (disposalDestination != -1)
                {
                    
                    checkDestination(detail.getOrder(), detail.getSample().getCodesample(), disposalDestination);
                }
                
                List<RackDetail> racks = new LinkedList<>();
                racks.add(detail);
                
                rackDao.insertRackAudit(racks, false, add);
                // Eliminar detalle y re escribirlo sobre auditoria.
                rackDao.removeSample(add.getPosition().getRack().getId(), add.getPosition().getPosition());
                System.out.println("Audita muestra :" + detail.getRack().getId() + " posición:" + detail.getPosition());
                auditService.auditSampleStorage(detail, AuditOperation.ACTION_UPDATE);
            }

        }
        return affected;
    }

    @Override
    public int close(DisposalCertificate close) throws Exception
    {
        return dao.close(close.getId(), trackingService.getRequestUser().getId());
    }

    @Override
    public DisposalCertificate listDetail(Integer id) throws Exception
    {
        DisposalCertificate disposal = dao.getDisposalDetail(id);
        List<RackDetail> racksWithoutSamples = dao.getDisposalDetailWithoutSamples(id);
        if(racksWithoutSamples.size() > 0) {
            if(disposal.getPositions() != null && disposal.getPositions().size() > 0 ) {
                disposal.getPositions().addAll(racksWithoutSamples); 
            } else {
                disposal.setPositions(racksWithoutSamples);
            }
        }
        return disposal;
    }

    /**
     * Verifica la muestra en el destino de almacenamiento
     *
     * @param order numero de orden
     * @param code codigo de la muestra
     * @param storeDestination id destino de almacenamiento
     */
    private void checkDestination(long order, String code, int storeDestination)
    {
        try
        {
            AssignmentDestination assigmentDestination = sampleTrackingService.getDestinationRoute(order, code);
            DestinationRoute microbiologyDestination = assigmentDestination.getDestinationRoutes().stream().filter(destination -> destination.getDestination().getId().equals(storeDestination)).findFirst().orElse(null);

            if (microbiologyDestination != null)
            {
                sampleTrackingService.verifyDestination(new VerifyDestination(order, code, microbiologyDestination.getId()));
            }
        } catch (Exception ex)
        {
            Logger.getLogger(RackServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
