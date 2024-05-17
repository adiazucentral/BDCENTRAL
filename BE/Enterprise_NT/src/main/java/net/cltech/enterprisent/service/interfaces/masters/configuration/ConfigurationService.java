package net.cltech.enterprisent.service.interfaces.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.configuration.GeneratePrintConfiguration;
import net.cltech.enterprisent.domain.masters.configuration.InitialConfiguration;
import net.cltech.enterprisent.domain.masters.configuration.PrintConfiguration;

/**
 * Servicios de configuracion general
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creacion
 */
public interface ConfigurationService
{

    /**
     * Obtiene todas las llaves de configuracion
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.configuration.Configuration}
     * @throws Exception Error en el servicio
     */
    public List<Configuration> get() throws Exception;

    /**
     * Obtiene una llave de configuraciòn
     *
     * @param key Llave de configuraciòn
     *
     * @return
     * {@link net.cltech.enterprisent.domain.masters.configuration.Configuration},
     * null en caso de que no se encuentre la llave
     * @throws Exception Error en el servicio
     */
    public Configuration get(String key) throws Exception;

    /**
     * Obtiene el valor de configuración
     *
     * @param key Llave de configuraciòn
     *
     * @return LLave de configuración null en caso de que no se encuentre la
     * llave
     * @throws Exception Error en el servicio
     */
    public String getValue(String key) throws Exception;

    /**
     * Actualiza una llave de configuración si existe
     *
     * @param configuration Lista de
     * {@link net.cltech.enterprisent.domain.masters.configuration.Configuration}
     * @param sendNotificatio Enviar notificacion
     *
     * @throws Exception Error en el servicio
     */
    public void update(List<Configuration> configuration, boolean sendNotificatio) throws Exception;

    /**
     * Establecer valores iniciales de configuracion del sistema
     *
     * @param initial objeto con la informacion inicial
     * @throws Exception Error en el servicio
     */
    public void initial(InitialConfiguration initial) throws Exception;

    /**
     * Obtiene el valor de la configuración como numero, -1 de no poder
     * convertirlo
     *
     * @param key llave configuracion
     * @return valor numerico de la llave
     * @throws Exception Error en el servicio
     */
    public int getIntValue(String key) throws Exception;

    /**
     * Reiniciar secuencia de forma manual
     *
     * @return si es permitido reiniciar la secuencia
     * @throws Exception Error en el servicio
     */
    public boolean restartSequenceManually() throws Exception;

    /**
     * Asginar hora de reinicio de secuencia del numero de orden de forma
     * automatica
     *
     * @param hour
     * @throws Exception Error en el servicio
     */
    public void restartSequenceAutomatic(String hour) throws Exception;

    /**
     * Reinicia el contador del numero de orden
     *
     * @throws Exception Error en el servicio
     */
    public void restartSequence() throws Exception;

    /**
     * Obtener configuracion para aplicativo de impresion
     *
     * @param serial
     * @return
     * @throws Exception Error en el servicio
     */
    public PrintConfiguration configprint(GeneratePrintConfiguration serial) throws Exception;

    /**
     * Obtener configuracion para cuerpo y asunto del correo
     *
     * @return
     * @throws Exception Error en el servicio
     */
    public List<String> bodyEmail() throws Exception;

    /**
     * Reinicia el contador del numero de orden para rellamado
     *
     * @throws Exception Error en el servicio
     */
    public void restartSequenceRecalled() throws Exception;
    
    /**
     * Reinicia el contador del numero de orden para citas
     *
     * @throws Exception Error en el servicio
     */
    public void restartSequenceAppointment() throws Exception;

    /**
     * Actualiza valor de llaves de url de api seguridad y de api de
     * licenciamiento
     *
     * @param configuration Lista de
     * {@link net.cltech.enterprisent.domain.masters.configuration.Configuration}
     *
     * @throws Exception Error en el servicio
     */
    public void updateSecurity(List<Configuration> configuration) throws Exception;
    
    /**
     * Limpiamos de registros la tabla que se envie como parametro
     *
     * @param tableToTruncate Tabla a truncar
     * @throws Exception Error en el servicio
     */
    public void tableToTruncate(String tableToTruncate) throws Exception;
    
    /**
     * Renombramos las tablas de operación, sus llaves primarias, y sus constrains
     * Ejemplo: nombreTabla_año - lab22_2020
     *
     * @throws Exception Error en el servicio
     */
    public void renameOperationTablesByYear() throws Exception;
    
    /**
     * Renombramos las tablas de operación, sus llaves primarias, y sus constrains
     * Ejemplo: nombreTabla_año - lab22_2020
     * (BD Estadistica)
     * @throws Exception Error en el servicio
     */
    public void renameOperationTablesStat() throws Exception;
    
    /**
    * Reinicia el contador del numero de caso
    *
    * @throws Exception Error en el servicio
    */
    public void restartSequencePathology() throws Exception;
    
    /**
     * Obtiene el valor de la llave de configuracion que nos indica si el LIS usa sedes o no
     *
     * @return Valor de la llave TrabajoPorSede
     * @throws Exception Error en el servicio
     */
    public String getBranchConfiguration() throws Exception;
}
