package net.cltech.enterprisent.service.impl.enterprisent.masters.tracking;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.tracking.RackDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.RackFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import net.cltech.enterprisent.domain.operation.tracking.SampleStore;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.RackService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.audit.AuditService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.barcode.BarcodeTools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.LISEnum.RackType;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
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
public class RackServiceEnterpriseNT implements RackService
{

    @Autowired
    private RackDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationService configService;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public Rack create(Rack create) throws Exception
    {
        create.setUser(trackingService.getRequestUser());
        create.setBranch(new Branch(create.getUser().getBranch()));
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Rack newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Rack.class);
            return newBean;
        }
        else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Rack> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Rack filterById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public Rack filterByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.trim().equalsIgnoreCase(a.getName()));
    }

    @Override
    public Rack update(Rack update) throws Exception
    {
        update.setUser(trackingService.getRequestUser());
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Rack old = filterById(update.getId());
            Rack updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Rack.class);
            updated.setState(old.getState());
            return updated;
        }
        else
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
    private List<String> validateFields(Rack validate, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();
        List<Rack> all = dao.list();
        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            }
            else
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
        }
        else
        {
            Rack found = findBy(all, a -> a.getName().equalsIgnoreCase(validate.getName()));

            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }

            Rack foundCode = null;
            foundCode = validateCode(all, validate.getCode());

            if (foundCode != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(foundCode.getId())))
                {
                    errors.add("1|code");//duplicado
                }
            }
        }

        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }

        if (validate.getBranch() == null)
        {
            errors.add("0|branch");
        }
        if (validate.getType() == null)
        {
            errors.add("0|type");
        }

        return errors;

    }

    @Override
    public List<Rack> filterBy(RackFilter options) throws Exception
    {
        if (options.getBranch() == -1)
        {
            options.setBranch(trackingService.getRequestUser().getBranch());
        }
        List<Rack> filter = dao.list()
                .stream()
                .filter(bean -> options.getType() == 0 || bean.getType() == options.getType())
                .filter(bean -> options.getState() == -1 || bean.getState() == options.getState())
                .filter(bean -> options.getBranch() == 0 || Objects.equals(bean.getBranch().getId(), options.getBranch()))
                .collect(Collectors.toList());
        return filter;
    }

    /**
     * Busqueda por una expresion
     *
     * @param racks listado en el que se realiza la busqueda
     * @param predicate criterio de busqueda
     *
     * @return Objeto encontrado, null si no se encuentra
     */
    public Rack findBy(List<Rack> racks, Predicate<Rack> predicate)
    {
        return racks.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

    public Rack validateCode(List<Rack> all, String validateCode)
    {
        Rack foundCode = null;
        for (Rack rack : all)
        {
            if (rack.getCode() != null)
            {
                if (rack.getCode().equals(validateCode))
                {
                    foundCode = rack;
                    break;
                }
            }
        }
        return foundCode;
    }

    @Override
    public List<Rack> listByBranch(Integer branchId) throws Exception
    {
        return dao.list().stream()
                .filter(bean -> Objects.equals(bean.getBranch().getId(), branchId))
                .collect(Collectors.toList());

    }

    @Override
    public RackDetail store(SampleStore store) throws Exception
    {

        String lastPosition = null;

        RackDetail detail;
        AuthorizedUser user = trackingService.getRequestUser();
        //consulta configuracion de gradillas
        boolean hasPending = configService.getValue(Configuration.KEY_RACK_PENDING).equalsIgnoreCase("true");
        boolean hasConfidential = configService.getValue(Configuration.KEY_RACK_CONFIDENTIAL).equalsIgnoreCase("true");
        int storeDestination = configService.getIntValue(Configuration.KEY_STORE_DESTINATION);
        //lista examenes de la muestra
        List<ResultTest> results = dao.listSampleTests(store.getOrder(), store.getSample());
        //obtiene gradillas de la sede y que correspondan a las enviadas
        List<Rack> racks = listByBranch(user.getBranch())
                .stream()
                .filter(rack -> store.getRacks().contains(rack.getId()))
                .collect(Collectors.toList());
        List<String> errors = validateStore(store, results, racks, hasPending, hasConfidential);
        //Obtiene  gradilla segun sus examenes
        RackType rackType = getSampleRackType(results, hasPending, hasConfidential);
        Rack sampleRack = racks.stream()
                .filter(rack -> Objects.equals(rack.getType(), rackType.getValue()))
                .findFirst()
                .orElse(new Rack());
        //valida y obtiene la siguiente posicion libre en la gradilla
        if (sampleRack.getId() != null && store.getPosition() == null)
        {
            lastPosition = getPositionAlfanumeric(sampleRack.getId(), sampleRack.getColumn(), sampleRack.getRow());
            if (lastPosition == null)
            {
                errors.add("2|" + sampleRack.getId() + "|rack is full");
            }
        }
        else if (sampleRack.getId() != null && store.getPosition() != null)
        {
            List<RackDetail> rackSamples = dao.listRackDetail(sampleRack.getId());
            List<RackDetail> racksFilterPosition = rackSamples.stream()
                    .filter(rack -> store.getPosition().contains(rack.getPosition()))
                    .collect(Collectors.toList());

            if (!racksFilterPosition.isEmpty())
            {
                errors.add("5|" + store.getPosition() + "|full position");
            }
            else
            {
                lastPosition = store.getPosition();
            }
        }
        else
        {
            errors.add("3|rack not found for sample");
        }
        if (errors.isEmpty())
        {
            int sampleId = results.get(0).getSampleId();
            Long storageDays = Long.parseLong(results.get(0).getStorageDays().toString());

            detail = new RackDetail();
            detail.setInsert(true);
            detail.setOrder(store.getOrder());
            detail.setSample(new Sample(sampleId));
            detail.setPosition(lastPosition);
            detail.setRack(new Rack(sampleRack.getId()));
            detail.setRegistDate(new Date());
            detail.setRegistUser(user);
            detail.setFull(lastPosition == null);
            detail.setValidStorageDate(DateTools.asDate(DateTools.localDate(detail.getRegistDate()).plusDays(storageDays)));
            if (storeDestination != -1)
            {
                checkStoreDestination(store.getOrder(), store.getSample(), storeDestination);
            }
        

            //Trazabilidad
            dao.insertRackDetail(detail);
            auditService.auditSampleStorage(detail.getRack().getId(), detail.getPosition(), AuditOperation.ACTION_INSERT);
            return detail;

        }
        else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    public String getPositionAlfanumeric(int rackId, int row, int column) throws Exception
    {
        String[] alphabelist =
        {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"
        };
        int[] numberslist =
        {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
        };

        alphabelist = Arrays.copyOfRange(alphabelist, 0, column);
        numberslist = Arrays.copyOfRange(numberslist, 0, row);

        String lastPosition = null;
        List<RackDetail> rackSamples = dao.listRackDetail(rackId);
        for (int i = 0; i < alphabelist.length; i++)
        {
            for (int j = 0; j < numberslist.length; j++)
            {
                String position = alphabelist[i] + numberslist[j];
                List<RackDetail> racks = rackSamples.stream()
                        .filter(rack -> rack.getPosition().equalsIgnoreCase(position))
                        .collect(Collectors.toList());

                if (racks.isEmpty())
                {
                    lastPosition = alphabelist[i] + numberslist[j];
                    break;
                }
            }
            if (lastPosition != null)
            {
                break;
            }
        }

        return lastPosition;
    }

    public String getPositionNumeric(String position)
    {

        Integer row = Integer.valueOf(position.substring(0, 2));
        Integer column = Integer.valueOf(position.substring(3, 4));

        String[] alphabelist =
        {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"
        };
        int[] numberslist =
        {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
        };

        return alphabelist[row.intValue()] + numberslist[column.intValue()];
    }

    /**
     * Validacion de campos enviados para almacenar muestra
     *
     * @param store informacion de almacenamientoi
     * @param results lista de exámenes de la muestra
     * @param racks lista de gradillas
     * @param isPendingConfigured Gradilla pendientes activa
     * @param isConfidentialConfigured Gradilla confidenciales activa
     * @return lista de errores encontrados
     * @throws Exception Error en la validacion
     */
    private List<String> validateStore(SampleStore store, List<ResultTest> results, List<Rack> racks, boolean isPendingConfigured, boolean isConfidentialConfigured) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (store.getOrder() == null || store.getOrder() == 0)
        {
            errors.add("0|order");
        }
        if (store.getSample() == null || store.getSample().trim().isEmpty())
        {
            errors.add("0|sample");
        }
        if (racks.isEmpty())
        {
            errors.add("0|racks");
        }
        else
        {

            if (!racks.stream().anyMatch(rack -> Objects.equals(rack.getType(), LISEnum.RackType.GENERAL.getValue())))
            {
                errors.add("1|not general rack");
            }
            if (isPendingConfigured && !racks.stream().anyMatch(rack -> Objects.equals(rack.getType(), LISEnum.RackType.PENDING.getValue())))
            {
                errors.add("1|not pending rack");
            }
            if (isConfidentialConfigured && !racks.stream().anyMatch(rack -> Objects.equals(rack.getType(), LISEnum.RackType.CONFIDENTIAL.getValue())))
            {
                errors.add("1|not confidential rack");
            }
        }
        if (results.isEmpty())
        {
            errors.add("2|no verified test");
        }
        /*else
        {
            if (results.get(0).getStorageDays() <= 0)
            {
                errors.add("3|sample storage time not set");
            }
        }*/
        return errors;

    }

    /**
     * Validacion de campos enviados para almacenar muestra
     *
     * @param store informacion de almacenamientoi
     * @param results lista de exámenes de la muestra
     * @param racks lista de gradillas
     * @param isPendingConfigured Gradilla pendientes activa
     * @param isConfidentialConfigured Gradilla confidenciales activa
     * @return lista de errores encontrados
     * @throws Exception Error en la validacion
     */
    private List<String> validateClose(Rack rack) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (rack.getFloor() == null || rack.getFloor().isEmpty())
        {
            errors.add("0|floor");
        }
        if (rack.getRefrigerator() == null || rack.getRefrigerator().getId() == null)
        {
            errors.add("0|refrigerator");
        }
        if (rack.getId() == null)
        {
            errors.add("0|rack");
        }
        return errors;

    }

    /**
     * Validacion de campos para retirar la muestra de una gradilla
     *
     * @param remove
     * @return Lista de errores encontrados
     * @throws Exception
     */
    private List<String> validateRemove(SampleStore remove) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (remove.getPosition() == null)
        {
            errors.add("0|position");
        }
        if (remove.getRackId() == null || remove.getRackId() == 0)
        {
            errors.add("0|rack");
        }
        return errors;

    }

    /**
     * Obtiene el tipo de gradilla segun los exámenes de la orden
     *
     * @param results lista de exámenes de la orden
     * @return tipo de gradilla
     */
    private LISEnum.RackType getSampleRackType(List<ResultTest> results, boolean pending, boolean confidential)
    {
        RackType rackType = LISEnum.RackType.GENERAL;
        boolean hasPendingTests = results.stream().anyMatch(result -> result.getState() < LISEnum.ResultTestState.VALIDATED.getValue());
        boolean hasConfidentialTests = results.stream().anyMatch(result -> result.isConfidential());
        if (pending && hasPendingTests)
        {
            rackType = LISEnum.RackType.PENDING;
        }
        else if (confidential && hasConfidentialTests)
        {
            rackType = LISEnum.RackType.CONFIDENTIAL;
        }

        return rackType;
    }

    @Override
    public List<RackDetail> listRackDetail(int rack) throws Exception
    {
        return dao.listRackDetail(rack);
    }

    /**
     * Verifica la muestra en el destino de almacenamiento
     *
     * @param order numero de orden
     * @param code codigo de la muestra
     * @param storeDestination id destino de almacenamiento
     */
    private void checkStoreDestination(long order, String code, int storeDestination)
    {
        try
        {
            AssignmentDestination assigmentDestination = sampleTrackingService.getDestinationRoute(order, code);
            if (assigmentDestination != null)
            {
                DestinationRoute microbiologyDestination = assigmentDestination.getDestinationRoutes().stream().filter(destination -> destination.getDestination().getId().equals(storeDestination)).findFirst().orElse(null);
                if (microbiologyDestination != null)
                {
                    sampleTrackingService.verifyDestination(new VerifyDestination(order, code, microbiologyDestination.getId()));
                }
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(RackServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<RackDetail> findSample(long order, String sampleCode) throws Exception
    {
        return dao.findSample(order, sampleCode);

    }

    @Override
    public void close(Rack rack) throws Exception
    {
        rack.setUpdateUser(trackingService.getRequestUser());
        List<String> errors = validateClose(rack);
        if (errors.isEmpty())
        {
            dao.close(rack);

        }
        else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public int removeSample(SampleStore store) throws Exception
    {
        List<String> errors = validateRemove(store);
        int affected = 0;
        if (errors.isEmpty())
        {
            Rack rack = findBy(list(), r -> Objects.equals(r.getId(), store.getRackId()));
            if (Objects.equals(rack.getType(), LISEnum.RackType.PENDING.getValue()))
            {
                auditService.auditSampleStorage(store.getRackId(), store.getPosition(), AuditOperation.ACTION_DELETE);
                affected = dao.removeSample(store.getRackId(), store.getPosition());

            }
            else
            {
                affected = dao.changeState(store.getRackId(), store.getPosition(), trackingService.getRequestUser().getId(), SampleStore.STATE_REMOVED);
                auditService.auditSampleStorage(store.getRackId(), store.getPosition(), AuditOperation.ACTION_UPDATE);
            }
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
        return affected;

    }

    @Override
    public String rackBarcode(Rack rack) throws Exception
    {
        String file = System.getProperty("user.dir") + File.separator + "barcode" + File.separator + "PrinterService-1.0.0.jar";
        System.out.println("File Printer:" + file);
        return BarcodeTools.rackBarcode(file, rack.getCode(), rack.getName());

    }

    @Override
    public RackDetail storeOld(SampleStore store) throws Exception
    {
        final RackDetail updated;
        List<String> errors = validateRemove(store);
        if (errors.isEmpty())
        {
            updated = findSample(store.getOrder(), store.getSample())
                    .stream()
                    .filter(rack -> rack.getPosition().equals(store.getPosition()))
                    .filter(rack -> Objects.equals(rack.getRack().getId(), store.getRackId()))
                    .findFirst()
                    .orElseThrow(() -> new EnterpriseNTException("1|Sample not found"));

            dao.changeState(store.getRackId(), store.getPosition(), trackingService.getRequestUser().getId(), SampleStore.STATE_STORED);
            updated.setInsert(true);
            auditService.auditSampleStorage(updated.getRack().getId(), updated.getPosition(), AuditOperation.ACTION_UPDATE);
            return updated;
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Rack> listRacksToDiscard() throws Exception
    {
        Date currentDate = new Date();
        
        List<Rack> list = dao.racksForDisposal(currentDate, trackingService.getRequestUser().getBranch())
                .stream()
                .filter(rack -> canDisposeRack(rack.getPositions(), currentDate))
                .collect(Collectors.toList());
        list.addAll(dao.racksWithoutSamplesForDisposal(currentDate, trackingService.getRequestUser().getBranch()));
        
        return list;
    }

    /**
     * Determina si se pueden descartar la gradillas, validando que todas sus
     * muestras hallan cumplido su periodo de almacenamiento con respecto a la
     * fecha enviada
     *
     * @param positions lista de posiciones de la gradilla
     * @param currentDate fecha actual
     * @return
     */
    private boolean canDisposeRack(List<RackDetail> positions, Date currentDate)
    {
        return !positions.stream()
                .anyMatch(position -> position.getValidStorageDate().compareTo(currentDate) == 1);
    }

    @Override
    public RackDetail getSampleDetail(int rackId, String position) throws Exception
    {
        return dao.getSampleDetail(rackId, position);
    }

    @Override
    public void dispouse(List<Integer> racks) throws Exception
    {
        dao.dispouse(racks, trackingService.getRequestUser().getId());
    }

    @Override
    public RackDetail storecode(SampleStore store) throws Exception
    {
        String rackCode = store.getRackCode();
        Integer rackId = dao.getIdRack(rackCode);
        
        int ORDERDIGITS = Integer.parseInt(configurationService.getValue("DigitoAño"));
        long order = Long.parseLong(Tools.getOrderNumberYear(store.getOrderPartial(), ORDERDIGITS));

        List<String> errors = new ArrayList<>();

        if (rackId == 0)
        {
            errors.add("6| Code don't exist in database");
        }

        Rack rackSingle = filterById(rackId);
        String lastPosition = null;
        RackDetail detail;
        AuthorizedUser user = trackingService.getRequestUser();
        //consulta configuracion de gradillas

        int storeDestination = configService.getIntValue(Configuration.KEY_STORE_DESTINATION);
     
        //lista examenes de la muestra
        List<ResultTest> results = dao.listSampleTests(order, store.getSample());

        //lista examenes en general
        List<ResultTest> resultsAll = dao.listSampleTestsAll(order, store.getSample());
        //lista examenes que va a filtrar la lista general y va a a dejar solo los validados
        List<ResultTest> resultsValidated = new ArrayList<>();

        for (int i = 0; i < resultsAll.size(); i++)
        {
            if (resultsAll.get(i).getState() == LISEnum.ResultTestState.VALIDATED.getValue())
            {
                resultsValidated.add(resultsAll.get(i));
            }
        }

        if (resultsAll.size() == resultsValidated.size())
        {
            if (rackId != null && store.getPosition() == null)
            {
                lastPosition = getPositionAlfanumeric(rackSingle.getId(), rackSingle.getColumn(), rackSingle.getRow());
                if (lastPosition == null)
                {
                    errors.add("2|" + rackSingle.getId() + "|rack is full");
                }
            }
            else if (rackSingle.getId() != null && store.getPosition() != null)
            {
                //store.setPosition(getPositionNumeric(store.getPosition()));// getPositionAlfanumeric(rackSingle.getId(), rackSingle.getColumn(), rackSingle.getRow());
                List<RackDetail> rackSamples = dao.listRackDetail(rackSingle.getId());
                List<RackDetail> racksFilterPosition = rackSamples.stream()
                        .filter(rack -> store.getPosition().contains(rack.getPosition()))
                        .collect(Collectors.toList());

                if (!racksFilterPosition.isEmpty())
                {
                    errors.add("5|" + store.getPosition() + "|full position");
                }
                else
                {
                    lastPosition = store.getPosition();
                }
            }
            else
            {
                errors.add("3|rack not found for sample");
            }

            if (errors.isEmpty())
            {
                int sampleId = results.get(0).getSampleId();
                Long storageDays = Long.parseLong(results.get(0).getStorageDays().toString());

                detail = new RackDetail();
                detail.setInsert(true);
                
                // Verificación de la muestra para el destino que tiene asociado ese usuario analizado
                
                detail.setOrder(order );
                detail.setSample(new Sample(sampleId));
                detail.setPosition(lastPosition);
                detail.setRack(new Rack(rackSingle.getId()));
                detail.setRegistDate(new Date());
                detail.setRegistUser(user);
                detail.setFull(lastPosition == null);
                detail.setValidStorageDate(DateTools.asDate(DateTools.localDate(detail.getRegistDate()).plusDays(storageDays)));
                if (storeDestination != -1)
                {
                    checkStoreDestination(order, store.getSample(), storeDestination);
                }

                //Trazabilidad
                dao.insertRackDetail(detail);
                auditService.auditSampleStorage(detail.getRack().getId(), detail.getPosition(), AuditOperation.ACTION_INSERT);
                return detail;

            }
            else
            {
                throw new EnterpriseNTException(errors);
            }
        }
        else
        {
            errors.add("1|Test validate incomplete");
            throw new EnterpriseNTException(errors);
        }

        //List<String> errors = validateStore(store, results, racks, hasPending, hasConfidential);
        //Obtiene  gradilla segun sus examenes
        //RackType rackType = getSampleRackType(results, hasPending, hasConfidential);
/*
                .filter(rack -> Objects.equals(rack.getType(), rackType.getValue()))
                .findFirst()
                .orElse(new Rack());*/
        //valida y obtiene la siguiente posicion libre en la gradilla
    }

}
