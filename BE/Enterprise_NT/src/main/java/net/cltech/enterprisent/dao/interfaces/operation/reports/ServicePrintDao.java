package net.cltech.enterprisent.dao.interfaces.operation.reports;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la impresion por
 * servicios
 *
 *
 * @version 1.0.0
 * @author equijano
 * @since 20/06/2019
 * @see Creación
 */
public interface ServicePrintDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Lista los seriales con sus servicios
     *
     * @return Lista de seriales.
     * @throws Exception Error en la base de datos.
     */
    default List<SerialPrint> list() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT ");
            query.append(" lab106.lab106c1");
            query.append(", lab05c1");
            query.append(", lab10c1");
            query.append(", lab106c2");
            query.append(", lab106c3");
            query.append(" FROM lab106");
            query.append(" INNER JOIN lab107 ON lab106.lab106c1 = lab107.lab106c1");

            return getConnection().query(query.toString(), (ResultSet rs, int i) ->
            {
                SerialPrint serialPrint = new SerialPrint();
                serialPrint.setSerial(rs.getString("lab106c1"));
                serialPrint.setIp(rs.getString("lab106c2"));
                serialPrint.setDate(rs.getDate("lab106c3"));
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                serialPrint.setBranch(branch);
                ServiceLaboratory service = new ServiceLaboratory();
                service.setId(rs.getInt("lab10c1"));
                serialPrint.setService(service);
                return serialPrint;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Buscar serial por sede y por servicio
     *
     * @return Serial.
     * @throws Exception Error en la base de datos.
     */
    default SerialPrint getByService(int idBranch, int idService) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT ");
            query.append(" lab106.lab106c1");
            query.append(", lab05c1");
            query.append(", lab10c1");
            query.append(", lab106c2");
            query.append(", lab106c3");
            query.append(" FROM lab106");
            query.append(" INNER JOIN lab107 ON lab106.lab106c1 = lab107.lab106c1");
            query.append(" WHERE lab107.lab05c1 = ?");
            query.append(" AND lab107.lab10c1 = ?");
            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                SerialPrint serialPrint = new SerialPrint();
                serialPrint.setSerial(rs.getString("lab106c1"));
                serialPrint.setIp(rs.getString("lab106c2"));
                serialPrint.setDate(rs.getDate("lab106c3"));
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                serialPrint.setBranch(branch);
                ServiceLaboratory service = new ServiceLaboratory();
                service.setId(rs.getInt("lab10c1"));
                serialPrint.setService(service);
                return serialPrint;
            }, idBranch, idService);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Registra un nuevo registro impresion por servicio.
     *
     * @param serialPrint informacion respectiva del serial con el servicio
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}
     * @throws Exception Error en la base de datos.
     */
    default void create(SerialPrint serialPrint) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab107");

        HashMap parameters = new HashMap();
        parameters.put("lab106c1", serialPrint.getSerial());
        parameters.put("lab05c1", serialPrint.getBranch().getId());
        parameters.put("lab10c1", serialPrint.getService().getId());

        insert.execute(parameters);
    }

    /**
     * Registra una lista de registros impresion por servicio.
     *
     * @param list lista de seriales por servicio
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}
     * @return
     * @throws Exception Error en la base de datos.
     */
    default int createAll(List<SerialPrint> list) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab107");
        list.stream().map((serialPrint) ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab106c1", serialPrint.getSerial());
            parameters.put("lab05c1", serialPrint.getBranch().getId());
            parameters.put("lab10c1", serialPrint.getService().getId());
            return parameters;
        }).forEachOrdered((parameters) ->
        {
            batchArray.add(parameters);
        });

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[list.size()]));

        return inserted.length;
    }

    /**
     * Eliminar un serial para impresion directa
     *
     * @param serialPrint informacion respectiva del serial con el servicio
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}
     * @throws Exception Error en la base de datos.
     */
    default void delete(SerialPrint serialPrint) throws Exception
    {
        getConnection().execute("DELETE FROM lab107 WHERE lab106c1 = '" + serialPrint.getSerial() + "' AND lab05c1 = " + serialPrint.getBranch().getId() + " AND lab10c1 = " + serialPrint.getService().getId());
    }

    /**
     * Eliminar los seriales
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteAll() throws Exception
    {
        getConnection().execute("DELETE FROM lab107 ");
    }

}
