package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.LaboratoryByBranch;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 30/01/2020
 * @see Creación
 */
public interface LaboratorysByBranchesDao
{

    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista laboratorios pertenceientes a una sede
     *
     * @param branch id de la sede
     * @return Lista de laboratorios
     * @throws Exception Error en base de datos
     */
    default List<LaboratoryByBranch> listLaboratorysByBranches(int branch) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab182.lab05c1"
                    + ", lab182.lab40c1"
                    + ", lab40.lab40c3"
                    + ", lab40.lab40c2"
                    + ", lab40.lab40c7"
                    + ", lab40.lab40c1 AS lab40c1_1"
                    + " FROM lab40 "
                    + "LEFT JOIN lab182 ON lab40.lab40c1 = lab182.lab40c1 and lab182.lab05c1 = ?",
                    (ResultSet rs, int numRow) ->
            {
                LaboratoryByBranch laboratorysByBranches = new LaboratoryByBranch();
                laboratorysByBranches.setBranch_id(rs.getInt("lab05c1"));
                laboratorysByBranches.setName(rs.getString("lab40c3"));
                laboratorysByBranches.setCode(rs.getString("lab40c2"));
                laboratorysByBranches.setType(rs.getShort("lab40c7"));
                laboratorysByBranches.setSelect(rs.getString("lab40c1") != null);
                laboratorysByBranches.setLaboratory_id(rs.getInt("lab40c1_1"));
                return laboratorysByBranches;
            }, branch);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     *
     * Retorna listado de id item de un id demografico hijo.
     *
     * @param branch
     * @return el estado de la muestra
     * @throws Exception Error en la base de datos.
     */
    default List<Integer> getidLaboratorybyBranch(int branch) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab40c1 ")
                    .append("FROM lab182  ")
                    .append("WHERE lab182.lab05c1 = ").append(branch);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab40c1");
            });
        } catch (Exception e)
        {
            return new ArrayList<>(0);
        }

    }

    /**
     * Elimina los laboratorios de una sede
     *
     * @param idBranch
     * @return numero de registros eliminados
     * @throws Exception
     */
    default int deleteLaboratoriesByBranch(Integer idBranch) throws Exception
    {
        String deleteSql = "DELETE FROM lab182 WHERE lab05c1 = ?";
        Object[] params =
        {
            idBranch
        };

        return getJdbcTemplate().update(deleteSql, params);

    }

    /**
     * Inserta/Asigna Examenes a un diagnostico
     *
     * @param idBranch id del diagnostico
     * @param laboratories lista de examenes
     *
     * @return numero de registros insertados
     * @throws Exception
     */
    default int insertLaboratoriesByBranch(Integer laboratories[], int idBranch) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab182");
            
            for (Integer i=0;i<laboratories.length;i++)
            {
                HashMap parameters = new HashMap();
                parameters.put("lab05c1", idBranch);
                parameters.put("lab40c1", laboratories[i]);
                batchArray.add(parameters);
            }

        return insert.executeBatch(batchArray.toArray(new HashMap[laboratories.length])).length;
    }

}
