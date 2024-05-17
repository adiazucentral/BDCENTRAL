package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationBandDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.band.BandSample;
import net.cltech.enterprisent.domain.integration.band.BandSampleCheck;
import net.cltech.enterprisent.domain.integration.band.BandVerifiedSample;
import net.cltech.enterprisent.domain.integration.middleware.CheckDestination;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReason;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReasonUser;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationBandService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de los servicios para la banda transportadora y su integración
 * con NT
 *
 * @version 1.0.0
 * @author Julian
 * @since 22/05/2020
 * @see Creación
 */
@Service
public class IntegrationBandServiceEnterpriseNT implements IntegrationBandService
{

    @Autowired
    private IntegrationBandDao integrationBandDao;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private SampleService sampleService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;


    /**
     * Verificacion de la muestra desde banda transportadora
     *
     * @param band
     * @return 1 (Se verifico la muestra), 0 (No se verifico la muestra)
     * @throws java.lang.Exception Error al realizar la verificacion de la
     * muestra
     */
    @Override
    public Integer bandSampleCheck(BandSampleCheck band) throws Exception
    {
        try
        {
            String sampleSeparator = configurationServices.get("SeparadorMuestra").getValue();
            Integer digits = 2;//configurationServices.getIntValue("DigitoAño");
            Integer digitsOrder = configurationServices.getIntValue("DigitosOrden");
            
            String[] orderSampleSeparate = new String[2];
            String orderNumber = "20" + band.getOrderNumber().toString();
          
            StringProperty sample = new SimpleStringProperty();
            if (sampleSeparator.equals("."))
            {
                orderSampleSeparate = orderNumber.split("\\.");
                sample.set(orderSampleSeparate[1]);
            }
            else if (sampleSeparator.equals("-"))
            {
                orderSampleSeparate = orderNumber.split(sampleSeparator);
                sample.set(orderSampleSeparate[1]);
            }
            else {
               orderSampleSeparate[0]  = orderNumber.substring(0, digits + 4 + digitsOrder + 2).trim();
               sample.set(orderNumber.substring(digits + 4 + digitsOrder + 2).trim());
            }
            
           
            Order order = sampleTrackingService.getOrder(Long.valueOf(orderSampleSeparate[0]), true);
            Sample testSample = order.getSamples().stream().filter(s -> (s.getCodesample() == null ? sample.getValue() == null : s.getCodesample().equals(sample.getValue()))).findFirst().orElse(null);
            int verified = sampleTrackingService.sampleTracking(order.getOrderNumber(), testSample.getCodesample(), LISEnum.ResultSampleState.CHECKED.getValue(), null, false, false) == true ? 1 : 0;
            return verified;
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Verificacion de la muestra en el destino desde banda transportadora
     *
     * @param band
     * @return 1 (Se verifico la muestra), 0 (No se verifico la muestra)
     * @throws java.lang.Exception Error al realizar la verificacion de la
     * muestra
     */
    @Override
    public Integer bandVerifyDestination(BandSampleCheck band) throws Exception
    {
        try
        {
            CheckDestination checkDestination = new CheckDestination();
            AuthorizedUser authorizedUser = JWT.decode(request);
            User user = userService.get(authorizedUser.getId(), null, null, null);
            String sampleSeparator = configurationServices.get("SeparadorMuestra").getValue();
            Integer digits = 2; //configurationServices.getIntValue("DigitoAño");
            Integer digitsOrder = configurationServices.getIntValue("DigitosOrden");
            
            String[] orderSampleSeparate = new String[2];
            String orderNumber = "20" + band.getOrderNumber();
            
            StringProperty sample = new SimpleStringProperty();
            if (sampleSeparator.equals("."))
            {
                orderSampleSeparate = orderNumber.split("\\.");
                sample.set(orderSampleSeparate[1]);
            }
            else if (sampleSeparator.equals("-"))
            {
                orderSampleSeparate = orderNumber.split(sampleSeparator);
                sample.set(orderSampleSeparate[1]);
            }
            else {
               orderSampleSeparate[0]  = orderNumber.substring(0, digits + 4 + digitsOrder + 2).trim();
               sample.set(orderNumber.substring(digits + 4 + digitsOrder + 2).trim());
            }
            
            
            Sample sampleDetail = sampleService.get(null, null, sample.getValue(), null, null).get(0);

            boolean verific = sampleTrackingService.isSampleCheckSimple(Long.parseLong(orderSampleSeparate[0]), sampleDetail.getId());
            if (verific)
            {
                boolean sampleTracking = sampleTrackingService.sampleTracking(Long.parseLong(orderSampleSeparate[0]), sampleDetail.getCodesample(), LISEnum.ResultSampleState.CHECKED.getValue(), authorizedUser);
            }

            AssignmentDestination assigment = sampleTrackingService.getDestinationRouteAnalyzer(Long.parseLong(orderSampleSeparate[0]), sampleDetail.getCodesample(), authorizedUser.getBranch(), user);
            if (assigment == null)
            {
                return 0;
            }

            boolean assingDestination = false;
            int idDestination = 0;
            for (int i = 0; i < assigment.getDestinationRoutes().size(); i++)
            {
                if (assigment.getDestinationRoutes().get(i).getDestination().getId() == null ? band.getDestinationId() == null : assigment.getDestinationRoutes().get(i).getDestination().getId().equals(band.getDestinationId()))
                {
                    assingDestination = true;
                    idDestination = assigment.getDestinationRoutes().get(i).getId();
                    break;
                }
            }

            if (assingDestination)
            {
                VerifyDestination verify = new VerifyDestination();
                verify.setApproved(true);
                verify.setBranch(authorizedUser.getBranch());
                verify.setDestination(idDestination);
                verify.setOrder(Long.parseLong(orderSampleSeparate[0]));
                verify.setSample(sampleDetail.getCodesample());
                boolean verifyTwo = sampleTrackingService.verifyDestination(verify);
                if (!verifyTwo)
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            }
            else
            {
                return 0;
            }

        }
        catch (Exception e)
        {
            return 0;
        }

    }

    /**
     * Obtiene un listado con las muestras verificadas el dia actual (HOY)
     *
     * @param idDestination
     * @param idBranch
     * @return
     * @throws Exception Error al obtener las muestras verificadas
     */
    @Override
    public List<BandVerifiedSample> listVerifiedSamples(int idDestination, int idBranch) throws Exception
    {
        try
        {
            String sampleSeparator = configurationServices.get("SeparadorMuestra").getValue();
            //Se obtienen las ordenes creadas el dia de hoy
            List<Long> ordersCreatedToday = integrationBandDao.ordersOfTheDay();
            List<Order> ordersWithSamples = new ArrayList<>();
            List<BandVerifiedSample> verifiedSamples = new ArrayList<>();
            for (Long idOrder : ordersCreatedToday)
            {
                // Se obtienen todas las muestras de esa orden
                Order orderWhitSamples = sampleTrackingService.getOrder(idOrder, true);
                if (orderWhitSamples != null)
                {
                    // Se cargan la orden cargada con sus muestras en una lista de ordenes
                    ordersWithSamples.add(orderWhitSamples);
                }
            }

            for (Order order : ordersWithSamples)
            {
                for (Sample sample : order.getSamples())
                {
                    // Se obtiene el la muestra no verificada para ese destino en esa orden
                    BandVerifiedSample verified = integrationBandDao.verifiedSampleInADestination(order.getOrderNumber(), sample.getId(), idBranch, idDestination);
                    // Si el objeto es nulo es que esa muestra no pertenece a ese destino
                    if (verified != null && verified.getOrder() != null)
                    {
                        verified.setOrder(order.getOrderNumber().toString().concat(sampleSeparator).concat(sample.getCodesample()));
                        verifiedSamples.add(verified);
                    }
                }
            }

            return verifiedSamples;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene un listado con las muestras no verificadas el dia actual (HOY)
     *
     * @param idDestination
     * @param idBranch
     * @return
     * @throws Exception Error al obtener las muestras verificadas
     */
    @Override
    public List<BandVerifiedSample> listUnverifiedSamples(int idDestination, int idBranch) throws Exception
    {
        try
        {
            String sampleSeparator = configurationServices.get("SeparadorMuestra").getValue();
            //Se obtienen las ordenes creadas el dia de hoy
            List<Long> ordersCreatedToday = integrationBandDao.ordersOfTheDay();
            List<Order> ordersWithSamples = new ArrayList<>();
            List<BandVerifiedSample> unverifiedSamples = new ArrayList<>();
            for (Long idOrder : ordersCreatedToday)
            {
                // Se obtienen todas las muestras de esa orden
                Order orderWhitSamples = sampleTrackingService.getOrder(idOrder, true);
                if (orderWhitSamples != null)
                {
                    // Se cargan la orden cargada con sus muestras en una lista de ordenes
                    ordersWithSamples.add(orderWhitSamples);
                }
            }

            for (Order order : ordersWithSamples)
            {
                for (Sample sample : order.getSamples())
                {
                    // Obtenemos el id de la asignación de destino
                    Integer destinationAssignmentId = integrationBandDao.getsTheDestinationAssignmentId(order.getType().getId(), sample.getId(), idBranch, idDestination);
                    // Si al buscar el id de la asignación de destinos con el id del tipo de orden es cero
                    // Entonces debemos enviar a la consulta el id del tipo de documento en cero - Indicando que este no aplica
                    if (destinationAssignmentId == null)
                    {
                        destinationAssignmentId = integrationBandDao.getsTheDestinationAssignmentId(0, sample.getId(), idBranch, idDestination);
                    }

                    if (destinationAssignmentId != null &&  destinationAssignmentId != 0)
                    {
                        // Validamos que esa muestra pertenezca a ese destino
                        Boolean sampleBelongsToDestination = integrationBandDao.theSampleBelongsToThisDestination(destinationAssignmentId, idDestination);
                        if (sampleBelongsToDestination != null && sampleBelongsToDestination)
                        {
                            // Se obtiene el la muestra no verificada para ese destino en esa orden
                            BandVerifiedSample unverified = integrationBandDao.verifiedSampleInADestination(order.getOrderNumber(), sample.getId(), idBranch, idDestination);
                            // Si el objeto es nulo significa que esa muestra no a sido verificada para ese destino
                            if (unverified == null)
                            {
                                unverified = integrationBandDao.getTheOrderWithUnverifiedSample(order.getOrderNumber());
                                unverified.setOrder(order.getOrderNumber().toString().concat(sampleSeparator).concat(sample.getCodesample()));
                                unverifiedSamples.add(unverified);
                            }
                        }
                    }
                }
            }

            return unverifiedSamples;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene el listado de muestras
     *
     * @return Listado de muestras
     * @throws Exception Error al obtener la lista de muestras
     */
    @Override
    public List<BandSample> listSamples() throws Exception
    {
        return integrationBandDao.listSamples();
    }
    
    
    /**
     * crea los datos de Estado de la Banda y Motivo
     *
     * @return statusBandReason
     * @throws Exception Error al crear los datos de la banda y el motivo
     */
    
    @Override
    public StatusBandReason createStatusBandReason(StatusBandReason statusBandReason) throws Exception
    {
        
        StatusBandReason created = integrationBandDao.createStatusBandReason(statusBandReason);
        return created;
    }
    
    
    /**
     * Obtiene el listado de muestras
     *
     * @return Listado de muestras
     * @throws Exception Error al obtener la lista 
     */
    @Override
    public List<StatusBandReasonUser> listReason() throws Exception
    {
        return integrationBandDao.listReason();
    }
    
    
    
    /**
     * Obtiene la lista de Motivo de la banda
     *
     * @return Lista de Motivo de la banda
     * @throws Exception Error al obtener la lista 
     */
    @Override
    public List<Motive> listReasonBand() throws Exception
    {
        return integrationBandDao.listReasonBand();
    }
}
