package net.cltech.enterprisent.service.impl.enterprisent.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.migration.InconsistencyDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.migration.Inconsistency;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.migration.InconsistencyService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de las inconsistencias para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/11/2017
 * @see Creacion
 */
@Service
public class InconsistencyServiceEnterpriseNT implements InconsistencyService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private InconsistencyDao dao;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private DemographicService demographicService;

    @Override
    public Order create(Order order, int user) throws Exception
    {
        Order orderResponse = null;
        boolean documentType = daoConfig.get("ManejoTipoDocumento").getValue().toLowerCase().equals("true");
        int demographic = Integer.valueOf(daoConfig.get("DemograficoInconsistensias").getValue());
        String inconsistencies = "";

        //Valida que la orden tenga asociado paciente
        if (order.getPatient() == null || order.getPatient().getPatientId() == null || order.getPatient().getPatientId().equals("") || order.getPatient().getPatientId().equals("0"))
        {
            List<String> errors = new ArrayList<>(0);
            errors.add("1|Order not valid must contains patient");
            throw new EnterpriseNTException(errors);
        }

        Patient patient = null;
        if (documentType)
        {
            patient = patientService.get(order.getPatient().getPatientId(), order.getPatient().getDocumentType().getId(),0);
        } else
        {
            patient = patientService.get(order.getPatient().getPatientId());
        }

        if (patient != null)
        {
            if (!patient.getName1().toLowerCase().equals(order.getPatient().getName1().toLowerCase()))
            {
                inconsistencies += "name1,";
            }
            if (!patient.getName2().toLowerCase().equals(order.getPatient().getName2().toLowerCase()))
            {
                inconsistencies += "name2,";
            }
            if (!patient.getLastName().toLowerCase().equals(order.getPatient().getLastName().toLowerCase()))
            {
                inconsistencies += "lastName,";
            }
            if (!patient.getSurName().toLowerCase().equals(order.getPatient().getSurName().toLowerCase()))
            {
                inconsistencies += "surName,";
            }
            if (!patient.getSex().getId().equals(order.getPatient().getSex().getId()))
            {
                inconsistencies += "sex,";
            }
            if (!DateTools.getInitialDate(patient.getBirthday()).equals(DateTools.getInitialDate(order.getPatient().getBirthday())))
            {
                inconsistencies += "birthday,";
            }

            if (demographic != 0)
            {
                Demographic demo = demographicService.get(demographic, null, null);
                if (demo != null)
                {
                    DemographicValue demoPatientHIS = order.getPatient().getDemographics().stream().filter(d -> d.getIdDemographic() == demo.getId()).findAny().orElse(null);
                    DemographicValue demoPatientLIS = patient.getDemographics().stream().filter(d -> d.getIdDemographic() == demo.getId()).findAny().orElse(null);
                    if (demo.isEncoded())
                    {
                        if (!demoPatientHIS.getCodifiedId().equals(demoPatientLIS.getCodifiedId()))
                        {
                            inconsistencies += demo.getName().toLowerCase() + ",";
                        }
                    } else
                    {
                        if (!demoPatientHIS.getNotCodifiedValue().toLowerCase().equals(demoPatientLIS.getNotCodifiedValue().toLowerCase()))
                        {
                            inconsistencies += demo.getName().toLowerCase() + ",";
                        }
                    }
                } else
                {
                    throw new EnterpriseNTException(Arrays.asList("0|demographicInconsistencies"));
                }
            }

            if (!inconsistencies.isEmpty())
            {
                AuthorizedUser session = JWT.decode(request);

                Inconsistency inconsistency = new Inconsistency();
                inconsistency.setPatientHIS(order.getPatient());
                inconsistency.getPatientHIS().setPatientId(Tools.encrypt(inconsistency.getPatientHIS().getPatientId()));
                inconsistency.getPatientHIS().setName1(Tools.encrypt(inconsistency.getPatientHIS().getName1()));
                inconsistency.getPatientHIS().setName2(Tools.encrypt(inconsistency.getPatientHIS().getName2()));
                inconsistency.getPatientHIS().setLastName(Tools.encrypt(inconsistency.getPatientHIS().getLastName()));
                inconsistency.getPatientHIS().setSurName(Tools.encrypt(inconsistency.getPatientHIS().getSurName()));

                inconsistency.setPatientLIS(patient);
                inconsistency.setInconsistencies(inconsistencies);
                inconsistency.setUser(session);

                order.setPatient(patient);
                orderResponse = orderService.create(order, user, session.getBranch());
                inconsistency.setOrderNumber(orderResponse.getOrderNumber());
                dao.create(inconsistency);
            } else
            {
                AuthorizedUser session = JWT.decode(request);
                orderResponse = orderService.create(order, user,session.getBranch() );
            }
        } else
        {
            AuthorizedUser session = JWT.decode(request);
            orderResponse = orderService.create(order, user, session.getBranch());
        }

        return orderResponse;
    }

    @Override
    public List<Inconsistency> list(Date init, Date end)
    {
        return dao.list(DateTools.getInitialDate(init), DateTools.getFinalDate(end));
    }

    @Override
    public Inconsistency getByOrderId(Long idOrder)
    {
        return dao.getInconsistency(idOrder);
    }

    @Override
    public boolean resolveInconsistency(long idOrder, boolean resolveLIS) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        int demographic = Integer.valueOf(daoConfig.get("DemograficoInconsistensias").getValue());

        Inconsistency inconsistency = dao.getInconsistency(idOrder);
        if (inconsistency == null)
        {
            throw new EnterpriseNTException(Arrays.asList("0|inconsistency"));
        }

        //Paciente HIS
        inconsistency.getPatientHIS().setPatientId(Tools.encrypt(inconsistency.getPatientHIS().getPatientId()));
        inconsistency.getPatientHIS().setName1(Tools.encrypt(inconsistency.getPatientHIS().getName1()));
        inconsistency.getPatientHIS().setName2(Tools.encrypt(inconsistency.getPatientHIS().getName2()));
        inconsistency.getPatientHIS().setLastName(Tools.encrypt(inconsistency.getPatientHIS().getLastName()));
        inconsistency.getPatientHIS().setSurName(Tools.encrypt(inconsistency.getPatientHIS().getSurName()));
        //Paciente LIS
        inconsistency.getPatientLIS().setPatientId(Tools.encrypt(inconsistency.getPatientLIS().getPatientId()));
        inconsistency.getPatientLIS().setName1(Tools.encrypt(inconsistency.getPatientLIS().getName1()));
        inconsistency.getPatientLIS().setName2(Tools.encrypt(inconsistency.getPatientLIS().getName2()));
        inconsistency.getPatientLIS().setLastName(Tools.encrypt(inconsistency.getPatientLIS().getLastName()));
        inconsistency.getPatientLIS().setSurName(Tools.encrypt(inconsistency.getPatientLIS().getSurName()));

        if (!resolveLIS)
        {
            if (demographic != 0)
            {
                Demographic demo = demographicService.get(demographic, null, null);
                DemographicValue demoPatientHIS = null;
                if (demo != null)
                {
                    demoPatientHIS = inconsistency.getPatientHIS().getDemographics().stream().filter(d -> d.getIdDemographic() == demo.getId()).findAny().orElse(null);
                    demoPatientHIS.setEncoded(demo.isEncoded());
                }

                inconsistency.getPatientHIS().setId(inconsistency.getPatientLIS().getId());
                dao.updatePatient(inconsistency.getPatientHIS(), session, demoPatientHIS);
            }
        }

        dao.resolveInconsistency(idOrder, inconsistency.getPatientLIS().getId());

        Order order = orderService.getAudit(idOrder).clean();

        List<AuditOperation> audit = new ArrayList<>();
        audit.add(new AuditOperation(idOrder, null,null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(order), Constants.MOTIVE_INCONSISTENCY, Tools.jsonObject(inconsistency), null, null));

        trackingService.registerOperationTracking(audit);

        return true;
    }
}
