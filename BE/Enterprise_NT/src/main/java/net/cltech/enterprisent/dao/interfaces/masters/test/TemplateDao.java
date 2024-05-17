package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.ResultTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de Plantilla.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 31/07/2017
 * @see Creación
 */
public interface TemplateDao
{

    /**
     * Lista plantilla de un examen desde la base de datos.
     *
     * @param id id del examen
     *
     * @return Lista plantillas.
     * @throws Exception Error en la base de datos.
     */
    default List<OptionTemplate> list(int id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab51.lab51c1, "
                    + "lab51c2, "
                    + "lab51c3, "
                    + "lab51c4, "
                    + "lab51.lab39c1, "
                    + "lab146c1, "
                    + "lab146c2, "
                    + "lab146c3, "
                    + "lab146c4, "
                    + "lab39.lab39c4 "
                    + "FROM lab51 "
                    + "INNER JOIN lab146 ON lab146.lab51c1 = lab51.lab51c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab51.lab39c1 "
                    + "WHERE lab51.lab39c1 = ?", (ResultSet rs, int i) ->
            {
                OptionTemplate create = new OptionTemplate();
                create.setId(rs.getInt("lab51c1"));
                create.setOption(rs.getString("lab51c2"));
                create.setComment(rs.getString("lab51c3"));
                create.setSort(rs.getInt("lab51c4"));
                create.getResults().add(new ResultTemplate());
                create.getResults().get(create.getResults().size() - 1).setResult(rs.getString("lab146c1"));
                create.getResults().get(create.getResults().size() - 1).setSort(rs.getInt("lab146c2"));
                create.getResults().get(create.getResults().size() - 1).setReference(rs.getInt("lab146c3") == 1);
                create.getResults().get(create.getResults().size() - 1).setComment(rs.getString("lab146c4"));
                create.setIdTest(id);
                create.setTestName(rs.getString("lab39c4"));
                return create;
            }, id);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza microorganismo en la base de datos.
     *
     *
     * @param template plantilla con resultados
     *
     * @return Objeto microorganismo.
     * @throws Exception Error en la base de datos.
     */
    default int createAllItems(OptionTemplate template) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab146");
        int sort = 1;
        for (ResultTemplate result : template.getResults())
        {
            HashMap parameters = new HashMap();
            parameters.put("lab146c1", result.getResult());
            parameters.put("lab146c2", sort);
            parameters.put("lab146c3", result.isReference() ? 1 : 0);
            parameters.put("lab51c1", template.getId());
            parameters.put("lab146c4", result.getComment());
            batchArray.add(parameters);
            sort++;
        }
        return insert.executeBatch(batchArray.toArray(new HashMap[template.getResults().size()])).length;
    }

    /**
     * Inserta plantillas
     *
     * @param template bean con información a insertar
     *
     * @return numero de registros insertados
     * @throws Exception Error en base de datos
     */
    default int createTemplate(OptionTemplate template) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab51")
                .usingGeneratedKeyColumns("lab51c1");

        HashMap parameters = new HashMap();
        parameters.put("lab51c2", template.getOption().trim());
        parameters.put("lab51c3", template.getComment());
        parameters.put("lab51c4", template.getSort());
        parameters.put("lab39c1", template.getIdTest());

        Number key = insert.executeAndReturnKey(parameters);

        return key.intValue();
    }

    /**
     * Elimina los resultados de un examen
     *
     * @param id id del examen
     *
     * @return numero de registros
     * @throws Exception Error
     */
    default int deleteTemplateItems(int id) throws Exception
    {
        String deleteSql = "DELETE FROM lab146 WHERE lab51c1 IN ("
                + " SELECT lab51c1 FROM lab51 WHERE lab39c1 = ? "
                + ")";

        return getJdbcTemplate().update(deleteSql, id);

    }

    /**
     * Elimina las plantillas de un examen
     *
     * @param id id del examen
     *
     * @return numero de registros eliminados
     * @throws Exception Error
     */
    default int deleteTemplate(int id) throws Exception
    {
        String deleteSql = "DELETE FROM lab51 WHERE lab39c1 = ? ";

        return getJdbcTemplate().update(deleteSql, id);

    }

    /**
     * Consulta el comentario del examen para las plantillas
     *
     * @param id id del examen
     *
     * @return Comentario
     * @throws Exception Error
     */
    default String getsTestCommentForTemplates(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c54")
                    .append(" FROM lab39 ")
                    .append("WHERE lab39c1 = ").append(id);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getString("lab39c54");
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Actualiza el comentario del examen para las plantillas
     *
     * @param commentTemplates comentario del examen para las plantillas
     * @param id id del examen
     *
     * @throws Exception Error
     */
    default void updateTestCommentForTemplates(String commentTemplates, int id) throws Exception
    {
        getJdbcTemplate().update("UPDATE lab39 "
                + "SET lab39c54 = ? "
                + "WHERE lab39c1 = ?",
                commentTemplates,
                id);
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
