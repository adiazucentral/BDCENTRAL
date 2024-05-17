package net.cltech.enterprisent.service.interfaces.operation.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.operation.tracking.DisposalCertificate;

/**
 * Interfaz de servicios para actas de desechos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 29/06/2018
 * @see Creación
 */
public interface DisposalCertificateService
{

    /**
     * Lista actas desde la base de datos.
     *
     * @return Lista de actas.
     * @throws Exception Error en la base de datos.
     */
    public List<DisposalCertificate> list() throws Exception;

    /**
     * Registra actas en la base de datos.
     *
     * @param create Instancia con los datos del actas.
     *
     * @return Instancia con los datos del actas.
     * @throws Exception Error en la base de datos.
     */
    public DisposalCertificate create(DisposalCertificate create) throws Exception;

    /**
     * Obtener información de actas por un campo especifico.
     *
     * @param id ID de actas a consultar.
     *
     * @return Instancia con los datos del actas.
     * @throws Exception Error en la base de datos.
     */
    public DisposalCertificate filterById(Integer id) throws Exception;

    /**
     * Obtener información de actas por un campo especifico.
     *
     * @param name name de actas a consultar.
     *
     * @return Instancia con los datos del actas.
     * @throws Exception Error en la base de datos.
     */
    public DisposalCertificate filterByName(String name) throws Exception;

    /**
     * Actualiza la información de un actas en la base de datos.
     *
     * @param update Instancia con los datos del actas.
     *
     * @return Objeto de actas modificada.
     * @throws Exception Error en la base de datos.
     */
    public DisposalCertificate update(DisposalCertificate update) throws Exception;

    /**
     * Adicion muestras al acta de desecho a partir de la gradilla
     *
     * @param add informacion del acta y gradillas que se van a agregar
     * @return registros afectados
     * @throws Exception error en el servicio
     */
    public int addRacks(DisposalCertificate add) throws Exception;

    /**
     * Adicion de muestra al acta de desecho
     *
     * @param add informacion del acta y gradillas que se van a agregar
     * @return registros afectados
     * @throws Exception error en el servicio
     */
    public int addSampleByPosition(DisposalCertificate add) throws Exception;

    /**
     * Cierra el acta
     *
     * @param close informacion del acta de desecho
     * @return registros afectados
     * @throws Exception Error en el servicio
     */
    public int close(DisposalCertificate close) throws Exception;

    /**
     * Obtiene el acta con el detalle de lo desechado
     *
     * @param id id del acta
     * @return acta de desecho
     * @throws Exception Error en el servicio
     */
    public DisposalCertificate listDetail(Integer id) throws Exception;

}
