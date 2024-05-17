package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.siga.SigaBranch;
import net.cltech.enterprisent.domain.integration.siga.SigaFilterOrders;
import net.cltech.enterprisent.domain.integration.siga.SigaLogUser;
import net.cltech.enterprisent.domain.integration.siga.SigaMainLogUser;
import net.cltech.enterprisent.domain.integration.siga.SigaPointOfCare;
import net.cltech.enterprisent.domain.integration.siga.SigaReason;
import net.cltech.enterprisent.domain.integration.siga.SigaRequestLog;
import net.cltech.enterprisent.domain.integration.siga.SigaService;
import net.cltech.enterprisent.domain.integration.siga.SigaTransferByServices;
import net.cltech.enterprisent.domain.integration.siga.SigaTransferData;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnCall;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnGrid;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnMovement;
import net.cltech.enterprisent.domain.integration.siga.SigaUrlBranch;
import net.cltech.enterprisent.domain.operation.widgets.TurnWidgetInfo;
import net.cltech.enterprisent.domain.operation.widgets.TurnsRatingWidgetInfo;

/**
 * Interfaz de servicios a la informacion para integrar con SIGA.
 *
 * @version 1.0.0
 * @author Jrodriguez
 * @since 16/10/2018
 * @see Creación
 */
public interface IntegrationSigaService
{

    /**
     * Lista de sedes.
     *
     * @return Lista de sedes.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaBranch> listBranches() throws Exception;

    /**
     * Lista de servicios.
     *
     * @param idbranch Id de la sede
     * @return Lista de servicios.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaService> listService(int idbranch) throws Exception;

    /**
     * Lista de taquillas por sede y servicio.
     *
     * @param branch Id de la sede
     * @param service Id del servicio
     * @return Lista de taquillas.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaPointOfCare> getByBranchService(int branch, int service) throws Exception;

    /**
     * Guarda el estado de la ventanilla.
     *
     * @param requestLog
     * @return log de usuario
     * @throws Exception Error en la base de datos.
     */
    public SigaLogUser changeStateWork(SigaRequestLog requestLog) throws Exception;

    /**
     * Lista de sedes.
     *
     * @param url url del siga
     * @return Lista de sedes.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaBranch> test(String url) throws Exception;

    /**
     * Lista de sedes.
     *
     * @param url url del siga
     * @return Lista de servicios.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaService> listServices(SigaUrlBranch url) throws Exception;

    /**
     * Lista de turnos activos.
     *
     * @param branch id de la sede
     * @param service id del servicio
     * @return Lista de servicios.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaTurnGrid> listActiveTurns(int branch, int service, int point) throws Exception;

    /**
     * Seleccionar turno de forma automatica.
     *
     * @param branch id de la sede
     * @param service id del servicio
     * @param point id del tarifa
     * @return Lista de turnos.
     * @throws Exception Error en la base de datos.
     */
    public SigaTurnGrid turnAutomatic(int branch, int service, int point) throws Exception;

    /**
     * Seleccionar turno de forma manual.
     *
     * @param turn objeto que se representa el turno
     * @return Lista de turnos
     * @throws Exception Error en la base de datos.
     */
    public SigaTurnGrid turnmanual(SigaTurnCall turn) throws Exception;

    /**
     * Seleccionar turno de forma manual.
     *
     * @param turn id del turno
     * @param service id del servicio
     * @return Validacion si el turno esta disponible.
     * @throws Exception Error en la base de datos.
     */
    public Boolean turncall(int turn, int service) throws Exception;

    /**
     * Lista los motivos de aplazamiento del siga
     *
     * @return Lista de motivos de aplazamiento.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaReason> listReason() throws Exception;

    /**
     * Lista los motivos de descasos del siga
     *
     * @return Lista de motivos de descanso.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaReason> listReasonBreak() throws Exception;

    /**
     * Lista los motivos de cancelacion del siga
     *
     * @return Lista de motivos de cancelacion.
     * @throws Exception Error en la base de datos.
     */
    public List<SigaReason> listReasonCancel() throws Exception;

    /**
     * Cambiar estado del turno
     *
     * @param turnmovement movimiento del turno
     * @return el id del turno
     * @throws Exception Error en la base de datos.
     */
    public int changeStateTurn(SigaTurnMovement turnmovement) throws Exception;

    /**
     * Obtiene los servicios a los cuales se puede transferir un turno
     *
     * @param branch id de la sede
     * @param service id del servicio
     * @param turn id del turno
     * @return Lista de {@link net.cltech.enterprisent.domain.integration.siga}
     * @throws Exception Error en la base de datos
     */
    public List<SigaTransferByServices> transferServicies(int branch, int service, int turn) throws Exception;

    /**
     * Funcionalidad para transferir un turno
     *
     * @param data objeto que representa la informacion de la transferencia
     * @return int id de la transferencia
     * @throws Exception Error en la base de datos
     */
    public int transfers(SigaTransferData data) throws Exception;

    /**
     * Verifica si un servicio se valida o no
     *
     * @param data objeto que representa la informacion del movimiento del turno
     * @return int si es 1 se califica el servicio y si devuelve otro valor no
     * se califica
     * @throws Exception Error en la base de datos
     */
    public int serviceRating(SigaTurnMovement data) throws Exception;

    /**
     * Obtiene total de turnos de una sede, total de turnos atendidos por un
     * usuario y total de turnos en espera de ser atendidos.
     *
     * @param date
     * @param idBranch
     * @param idUser
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.widgets.TurnWidgetInfo}
     * @throws Exception Error en la base de datos.
     */
    public TurnWidgetInfo informationTurns(int date, int idBranch, int idUser) throws Exception;

    /**
     * Obtiene total de turnos calificados de una sede y un servicio.
     *
     * @param date
     * @param idBranch
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.widgets.TurnsRatingWidgetInfo}
     * @throws Exception Error en la base de datos.
     */
    public List<TurnsRatingWidgetInfo> turnsQualification(int date, int idBranch) throws Exception;

    /**
     * Obtiene el historial del usuario en taquilla y turno
     *
     * @param userName
     * @return Historial del usuario
     * @throws Exception Error en la base de datos.
     */
    public SigaMainLogUser getUserHistory(String userName) throws Exception;

    /**
     * Obtiene todos los puntos de atención
     *
     * @return Lista de puntos de atención
     * @throws Exception Error en la base de datos.
     */
    public List<SigaPointOfCare> getAllPointsOfCare() throws Exception;

    /**
     * Obtiene lista de ordenes por rango de ingreso
     *
     * @param filter Objeto que representa el filtro de las ordenes
     * @return Lista de ordenes
     * @throws Exception Error presentado en base de datos
     */
    public List<Long> getOrdersByDate(SigaFilterOrders filter) throws Exception;

    /**
     * Verifica si un servicio se valida o no
     *
     * @param data objeto que representa la informacion del movimiento del turno
     * @return int si es 1 se califica el servicio y si devuelve otro valor no
     * se califica
     * @throws Exception Error en la base de datos
     */
    public int serviceTurnOrder(SigaTurnMovement data) throws Exception;
}
