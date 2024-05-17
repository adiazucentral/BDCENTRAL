package net.cltech.enterprisent.service.impl.enterprisent.masters.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.common.ChatMessage;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.configuration.GeneratePrintConfiguration;
import net.cltech.enterprisent.domain.masters.configuration.InitialConfiguration;
import net.cltech.enterprisent.domain.masters.configuration.PrintConfiguration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.domain.masters.user.BranchByUser;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
import net.cltech.enterprisent.service.interfaces.masters.pathology.StudyTypeService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.reports.ReportService;
import net.cltech.enterprisent.service.interfaces.start.jobs.SchedulerService;
import net.cltech.enterprisent.tools.ConfigurationConstants;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.websocket.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa los metodos de acceso a configuracion general para Enterprise NT
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creación
 */
@Service
public class ConfigurationServiceEnterpriseNT implements ConfigurationService
{

    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatHandler chatHandler;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private StudyTypeService studyTypeService;

    @Override
    public List<Configuration> get() throws Exception
    {
        return configurationDao.get();
    }

    @Override
    public Configuration get(String key) throws Exception
    {
        Configuration configKey;
        if (key.equalsIgnoreCase("documentTypes"))
        {
            configKey = configurationDao.get("ManejoTipoDocumento");
        }
        else if (key.equalsIgnoreCase("clients"))
        {
            configKey = configurationDao.get("ManejoCliente");
        }
        else if (key.equalsIgnoreCase("physicians"))
        {
            configKey = configurationDao.get("ManejoMedico");
        }
        else if (key.equalsIgnoreCase("rates"))
        {
            configKey = configurationDao.get("ManejoTarifa");
        }
        else if (key.equalsIgnoreCase("formatDate"))
        {
            configKey = configurationDao.get("FormatoFecha");
        }
        else if (key.equalsIgnoreCase("TrabajoPorSede"))
        {
            Configuration auxKey = new Configuration();
            auxKey.setKey("TrabajoPorSede");
            auxKey.setValue("True");
            configKey = auxKey;
        }
        else
        {
            configKey = configurationDao.get(key);
        }
        return configKey;
    }

    @Override
    public String getValue(String key) throws Exception
    {
        
        return configurationDao.get(key) == null ? null : configurationDao.get(key).getValue();
    }

    @Override
    public void update(List<Configuration> configuration, boolean sendNotification) throws Exception
    {
        HashMap<String, HashMap<String, String>> constants = trackingService.getConstants();
        for (Configuration config : configuration)
        {
            Configuration oldConfiguration = configurationDao.get(config.getKey());
            if (oldConfiguration != null)
            {
                if (constants.get(oldConfiguration.getKey()) != null)
                {
                    oldConfiguration.setName(constants.get(oldConfiguration.getKey()).get(oldConfiguration.getValue()));
                }
                configurationDao.update(config);
                trackingService.registerConfigurationTracking(oldConfiguration, config, Configuration.class);
            }
        }
        if (sendNotification)
        {
            AuthorizedUser user = trackingService.getRequestUser();
            CompletableFuture.runAsync(() -> sendUpdateNotification(user));
        }
    }

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.READ_COMMITTED, rollbackFor =
    {
        NullPointerException.class, Exception.class
    })
    @Override
    public void initial(InitialConfiguration initial) throws Exception
    {
        List<String> errorFields = validateFields(initial);
        if (errorFields.isEmpty())
        {
            update(initial.getConfig(), true);
            Branch newBranch = branchService.create(initial.getBranch());
            initial.getUser().setBranches(new ArrayList<>());
            BranchByUser branch = new BranchByUser();
            branch.setBranch(newBranch);
            initial.getUser().getBranches().add(branch);
            userService.update(initial.getUser());
        }
        else
        {
            throw new EnterpriseNTException(errorFields);
        }

    }

    /**
     * Validacion de campos del servicio initial
     *
     * @param initial Objeto con la configuracion inicial
     * @return
     */
    private List<String> validateFields(InitialConfiguration initial) throws JsonProcessingException
    {
        List<String> errors = new ArrayList<>();
        if (initial.getBranch() == null)
        {
            errors.add("0|branch");
        }
        if (initial.getUser() == null)
        {
            errors.add("0|user");
        }
        if (initial.getConfig() == null)
        {
            errors.add("0|config");
        }
        return errors;
    }

    /**
     * Envia notificacion de actualizacion de la configuracion
     *
     * @param user
     */
    private void sendUpdateNotification(AuthorizedUser user)
    {
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setType(ChatHandler.TYPE_SYSTEM);
        systemMessage.setCode(ChatMessage.SYSTEM_CONFIGURATION_UPDATED);
        systemMessage.setMessage("configuration updated");
        systemMessage.setSender(user.getUserName());
        chatHandler.sendToAll(systemMessage);
    }

    @Override
    public int getIntValue(String key) throws Exception
    {
        try
        {
            return Integer.parseInt(configurationDao.get(key).getValue());
        }
        catch (Exception ex)
        {
            return -1;
        }
    }

    @Override
    public boolean restartSequenceManually() throws Exception
    {
        List<OrderSearch> orders = orderService.ordersByEntryDate(DateTools.dateToNumber(new Date()));
        boolean result = false;
        if (orders.isEmpty())
        {
            restartSequence();
            result = true;
        }
        return result;
    }

    @Override
    public void restartSequenceAutomatic(String hour) throws Exception
    {
        if (Integer.parseInt(hour) > -1)
        {
            List<Configuration> list = new ArrayList<>();
            Configuration conf = new Configuration();
            conf.setKey("HorarioReinicioOrdenes");
            conf.setValue(hour);
            list.add(conf);
            update(list, true);
            schedulerService.createThread(hour);
        }
        else
        {
            throw new EnterpriseNTException(Arrays.asList("0|hour"));
        }
    }

    @Override
    public void restartSequence() throws Exception
    {
        String orderTypeNumber = get(ConfigurationConstants.KEY_NUMERATION).getValue();
        if (orderTypeNumber.equals(ConfigurationConstants.NUMERATION_GENERAL))
        {
            toolsDao.resetSequence(Constants.SEQUENCE_GENERAL, 1);
        }
        else if (orderTypeNumber.equals(ConfigurationConstants.NUMERATION_BRANCH))
        {
            List<Branch> branches = branchService.list();
            for (Branch branche : branches)
            {
                if (branche != null && branche.getMinimum() != null && branche.getMaximum() != null)
                {
                    toolsDao.resetSequence(Constants.SEQUENCE + branche.getId(), branche.getMinimum(), branche.getMaximum());
                }
            }
        }
        else if (orderTypeNumber.equals(ConfigurationConstants.NUMERATION_SERVICE))
        {
            List<ServiceLaboratory> services = serviceService.list();
            for (ServiceLaboratory service : services)
            {
                toolsDao.resetSequence(Constants.SEQUENCE + service.getId(), 1);
            }
        }
    }

    @Override
    public PrintConfiguration configprint(GeneratePrintConfiguration serial) throws Exception
    {
        PrintConfiguration printConfiguration = new PrintConfiguration();
        printConfiguration.setUrlWS(serial.getUrlBackend().replace("http", "ws").replace("https", "ws") + "/print");
        if (serial.getSerial())
        {
            printConfiguration.setSerial(reportService.createSerial().getSerial());
        }
        printConfiguration.setUrlNode(get("UrlNodeJs").getValue());
        printConfiguration.setEmail(get("SmtpAuthUser").getValue());
        printConfiguration.setHost(get("SmtpHostName").getValue());
        printConfiguration.setPassword(get("SmtpPasswordUser").getValue());
        printConfiguration.setPort(get("SmtpPort").getValue());
        printConfiguration.setSsl(get("SmtpSSL").getValue());
        printConfiguration.setServerEmail(get("ServidorCorreo").getValue());

        return printConfiguration;
    }

    @Override
    public List<String> bodyEmail() throws Exception
    {
        List<String> list = new ArrayList<>(0);
        list.add(get("EmailBody").getValue());
        list.add(get("EmailSubjectPatient").getValue());
        list.add(get("EmailSubjectPhysician").getValue());
        return list;
    }  
   
    

    @Override
    public void restartSequenceRecalled() throws Exception
    {
        if (Integer.parseInt(get("RangoInicial").getValue().trim()) > 0 && Integer.parseInt(get("RangoFinal").getValue().trim()) > 0)
        {
            if (toolsDao.validateSequence(Constants.SEQUENCE_RECALLED))
            {
                toolsDao.resetSequence(Constants.SEQUENCE_RECALLED, Integer.parseInt(get("RangoInicial").getValue().trim()), Integer.parseInt(get("RangoFinal").getValue().trim()));
            }
            else
            {
                toolsDao.createSequence(Constants.SEQUENCE_RECALLED, Integer.parseInt(get("RangoInicial").getValue().trim()), 1, Integer.parseInt(get("RangoFinal").getValue().trim()));
            }
        }
    }

    @Override
    public void restartSequenceAppointment() throws Exception
    {
        if (Integer.parseInt(get("RangoInicialCitas").getValue().trim()) > 0 && Integer.parseInt(get("RangoFinalCitas").getValue().trim()) > 0)
        {
            if (toolsDao.validateSequence(Constants.SEQUENCE_APPOINTMENT))
            {
                toolsDao.resetSequence(Constants.SEQUENCE_APPOINTMENT, Integer.parseInt(get("RangoInicialCitas").getValue().trim()), Integer.parseInt(get("RangoFinalCitas").getValue().trim()));
            }
            else
            {
                toolsDao.createSequence(Constants.SEQUENCE_APPOINTMENT, Integer.parseInt(get("RangoInicialCitas").getValue().trim()), 1, Integer.parseInt(get("RangoFinalCitas").getValue().trim()));
            }
        }
    }

    @Override
    public void updateSecurity(List<Configuration> configuration) throws Exception
    {
        if (configuration.size() == 2 && configuration.contains(new Configuration("UrlSecurity")) && configuration.contains(new Configuration("UrlDischarge")))
        {
            update(configuration, false);
        }
        else
        {
            throw new EnterpriseNTException(Arrays.asList("0|keys incorrect"));
        }
    }

    /**
     * Limpiamos de registros la tabla que se envie como parametro
     *
     * @param tableToTruncate Tabla a truncar
     * @throws Exception Error en el servicio
     */
    @Override
    public void tableToTruncate(String tableToTruncate) throws Exception
    {
        configurationDao.tableToTruncate(tableToTruncate);
    }

    @Override
    public void renameOperationTablesByYear() throws Exception
    {
        Timestamp date = new Timestamp(new Date().getTime());
        int year = Integer.parseInt(date.toString().substring(0, 4));
        configurationDao.renameOperationTablesByYear(year);
    }

    @Override
    public void renameOperationTablesStat() throws Exception
    {
        Timestamp date = new Timestamp(new Date().getTime());
        int year = Integer.parseInt(date.toString().substring(0, 4));
        configurationDao.renameOperationTablesStat(year);
    }

    @Override
    public void restartSequencePathology() throws Exception
    {
        List<StudyType> studies = studyTypeService.list();
        studies.forEach(study ->
        {
            try
            {
                toolsDao.resetSequencePathology(Constants.SEQUENCE_PATHOLOGY, study.getId());
            }
            catch (Exception ex)
            {
                Logger.getLogger(ConfigurationServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @Override
    public String getBranchConfiguration() throws Exception
    {
        try
        {
            return configurationDao.get("TrabajoPorSede") == null ? null : configurationDao.get("TrabajoPorSede").getValue();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
