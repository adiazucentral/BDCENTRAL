package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.BranchDemographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFather;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DemographicRequired;
import net.cltech.enterprisent.domain.masters.demographic.ItemDemographicSon;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * demografico Item
 *
 * @version 1.0.0
 * @author enavas
 * @since 08/05/2017
 * @see Creación
 */
public interface DemographicItemDao
{
    
    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista los demograficos Item desde la base de datos
     *
     * @return Lista de Demograficos Item
     * @throws Exception Error en base de datos
     */
    public List<DemographicItem> list() throws Exception;

    /**
     * Registra un nuevo Demografico Item en la base de datos
     *
     * @param demographicItem Instancia con los datos del Demografico Item.
     * @return Instancia con los datos del Demografico Item.
     * @throws Exception Error en base de datos
     */
    public DemographicItem create(DemographicItem demographicItem) throws Exception;

    /**
     * Obtener informacion del demografico Items en la base de datos
     *
     * @param id Id del demografico Item.
     * @param code Codigo del demografico Item.
     * @param name Nombre del demografico Item.
     * @param demographic Id del demografico padre.
     * @param state Estado del demografico Item.
     * @return Instancia con los datos del demografico Items.
     * @throws Exception Error en base de datos
     */
    public List<DemographicItem> get(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception;
    
    /**
     * Obtener informacion del demografico Items en la base de datos
     *
     * @param id Id del demografico Item.
     * @param code Codigo del demografico Item.
     * @param name Nombre del demografico Item.
     * @param demographic Id del demografico padre.
     * @param state Estado del demografico Item.
     * @return Instancia con los datos del demografico Items.
     * @throws Exception Error en base de datos
     */
    public List<DemographicItem> getwebquery(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception;

    /**
     * Actualiza la informacion del Demografico Item en la base de datos
     *
     * @param demographicItem Instancia con los datos del Demografico Item.
     * @return
     * @throws Exception Error en base de datos
     */
    public DemographicItem update(DemographicItem demographicItem) throws Exception;

    /**
     * Consulta los demograficos item por serde central y demografico.
     *
     * @param branch
     * @param idDemographic
     * @return
     * @throws Exception Error en base de datos
     */
    public List<BranchDemographic> demographicsItemList(int branch, int idDemographic) throws Exception;

    /**
     * Consulta por sede, demografico y item Demografico.
     *
     * @param branch
     * @param idDemographics
     * @param idDemographicItem
     * @return
     * @throws Exception Error en base de datos
     */
    public boolean validateBranchDemographic(int branch, int idDemographics, int idDemographicItem) throws Exception;

    /**
     * Consulta por sede, demografico y item Demografico.
     *
     * @param demographics
     *
     * @return
     * @throws Exception Error en base de datos
     */
    public List<BranchDemographic> getDemographicsBranch(List<BranchDemographic> demographics) throws Exception;

    /**
     * Borra por sede, demografico.
     *
     * @param demographics
     * @return
     * @throws Exception Error en base de datos
     */
    public int deleteDemographicsBranch(List<BranchDemographic> demographics) throws Exception;

    /**
     * Crea por sede, demografico y item Demografico.
     *
     * @param demographics
     * @return
     * @throws Exception Error en base de datos
     */
    public int createDemographicsBranch(List<BranchDemographic> demographics) throws Exception;

    /**
     * Consulta los demograficos item por sede y demografico.
     *
     * @param branch
     * @param idDemographic
     * @return
     * @throws Exception Error en base de datos
     */
    public List<DemographicBranch> demographicsBranch(int branch) throws Exception;

    /**
     * Consulta por sede y demografico
     *
     * @param demographics
     *
     * @return
     * @throws Exception Error en base de datos
     */
    public List<DemographicBranch> branchgetDemographics(List<DemographicBranch> demographics) throws Exception;

    /**
     * Borra por sede, demografico.
     *
     * @param demographics
     * @return
     * @throws Exception Error en base de datos
     */
    public int deleteBranchDemographics(int idBranch) throws Exception;

    /**
     * Crea por sede, demografico.
     *
     * @param demographics
     * @return
     * @throws Exception Error en base de datos
     */
    public int createBranchDemographics(List<DemographicBranch> demographics) throws Exception;

    /**
     * Obtiene los demografico asignados para esa sede
     *
     * @param idBranch
     * @return
     * @throws Exception Error en base de datos
     */
    default List<BranchDemographic> getBranchDemographics(int idBranch) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab05c1, ")
                    .append("lab62c1 ")
                    .append("FROM lab109 ")
                    .append("WHERE lab05c1 = ").append(idBranch);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                BranchDemographic demoByBranch = new BranchDemographic();
                demoByBranch.setId(rs.getInt("lab05c1"));
                demoByBranch.getDemographic().setId(rs.getInt("lab62c1"));
                return demoByBranch;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene los Items del demografico asignados para esa sede
     *
     * @param idBranch
     * @param idDemographic
     * @return
     * @throws Exception Error en base de datos
     */
    default List<BranchDemographic> getBranchDemographicsItems(int idBranch, int idDemographic) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab05c1, ")
                    .append("lab62c1, ")
                    .append("lab63c1 ")
                    .append("FROM lab108 ")
                    .append("WHERE lab05c1 = ").append(idBranch)
                    .append(" AND lab62c1 = ").append(idDemographic);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                BranchDemographic demoByBranch = new BranchDemographic();
                demoByBranch.setId(rs.getInt("lab05c1"));
                demoByBranch.getDemographic().setId(rs.getInt("lab62c1"));
                demoByBranch.getDemographicItem().setId(rs.getInt("lab63c1"));
                return demoByBranch;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Actualiza demografico.
     *
     * @param demographics
     * @return
     * @throws Exception Error en base de datos
     */
    public int demographicValueRequired(List<DemographicRequired> demographics) throws Exception;

    /**
     * Obtiene los Items hijos
     *
     * @param father
     * @param idFatherItem
     * @param idSon
     * @return
     * @throws Exception Error en base de datos
     */
    default List<ItemDemographicSon> getlisDemographicItemstSons(int father, int idFatherItem, int idSon) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            
            switch (idSon)
            {
                case Constants.ACCOUNT:
                    query.append("SELECT lab14c1 AS id, lab14c2 AS code, lab14c3 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab14 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab14.lab14c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.PHYSICIAN:
                    query.append("SELECT lab19c1 AS id, lab19c22 AS code, lab19c2 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab19 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab19.lab19c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.RATE:
                    query.append("SELECT lab904c1 AS id, lab904c2 AS code, lab904c3 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab904 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab904.lab904c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.ORDERTYPE:
                    query.append("SELECT lab103c1 AS id, lab103c2 AS code, lab103c3 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab103 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab103.lab103c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.BRANCH:
                    query.append("SELECT lab05c1 AS id, lab05c10 AS code, lab05c4 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab05 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab05.lab05c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.SERVICE:
                    query.append("SELECT lab10c1 AS id, lab10c7 AS code, lab10c2 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab10 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab10.lab10c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.RACE:
                    query.append("SELECT lab08c1 AS id, lab08c5 AS code, lab08c2 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab08 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab08.lab08c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.DOCUMENT_TYPE:
                    query.append("SELECT lab54c1 AS id, lab54c2 AS code, lab54c3 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab54 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab54.lab54c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                case Constants.AGE_GROUP:
                    query.append("SELECT lab13c1 AS id, lab13c2 AS code, lab13c3 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab13 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab13.lab13c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ");
                    break;
                default:
                    query.append("SELECT lab63c1 AS id, lab63c2 AS code, lab63c3 AS name, lab190c4 AS selected ")
                            .append(", lab190.lab04c1, lab04c2, lab04c3, lab04c4, lab190c5 ")
                            .append("FROM lab63 ")
                            .append("LEFT JOIN lab190 ")
                            .append("ON lab63.lab63c1 = lab190.lab190c4 ")
                            .append("AND  lab190c1 = ").append(father)
                            .append(" AND lab190.lab190c2 = ").append(idFatherItem)
                            .append(" LEFT JOIN lab04 ON lab04.lab04c1 = lab190.lab04c1 ")
                            .append(" WHERE lab63.lab62c1 = ").append(idSon);
                    break;
            }

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                ItemDemographicSon itemDemographicSon = new ItemDemographicSon();
                itemDemographicSon.setId(rs.getInt("id"));
                itemDemographicSon.setCode(rs.getString("code"));
                itemDemographicSon.setName(rs.getString("name"));
                itemDemographicSon.setSelected(rs.getInt("selected") != 0);
                
                itemDemographicSon.setLastTransaction(rs.getTimestamp("lab190c5"));
                /*Usuario*/
                itemDemographicSon.getUser().setId(rs.getInt("lab04c1"));
                itemDemographicSon.getUser().setName(rs.getString("lab04c2"));
                itemDemographicSon.getUser().setLastName(rs.getString("lab04c3"));
                itemDemographicSon.getUser().setUserName(rs.getString("lab04c4"));
                
                return itemDemographicSon;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Elimina el registro de un demografico *
     *
     * @param demographicFather
     * @return 1 - si todos los registros se eliminaron satisfactoriamente, -1 -
     * si algun registro no pudo ser eliminado
     * @throws Exception Error en base de datos
     */
    default int deleteDemographicFather(DemographicFather demographicFather) throws Exception
    {
        try
        {

            getJdbcTemplate().update("DELETE FROM lab190 "
                    + " WHERE lab190c1 = ? "
                    + " AND lab190c2 = ? AND lab190c3 = ?",
                    demographicFather.getIdDemographicFather(),
                    demographicFather.getIdDemographicFatherItem(),
                    demographicFather.getIdDemographicSon());

            return 1;
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Registra enla tabla de dependencia Demografica Demograficos Padre en
     * relacion con sus hijos.
     *
     * @param idFather
     * @param idFatheriItem
     * @param idSon
     * @param idItemSon
     * @return
     * @throws Exception Error en la base de datos.
     */
    default int createDemographicFather(int idFather, int idFatheriItem, int idSon, int idItemSon, int idUser) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab190")
                    .usingColumns("lab190c1", "lab190c2", "lab190c3", "lab190c4", "lab04c1", "lab190c5");

            HashMap parameters = new HashMap();
            parameters.put("lab190c1", idFather);
            parameters.put("lab190c2", idFatheriItem);
            parameters.put("lab190c3", idSon);
            parameters.put("lab190c4", idItemSon);
            parameters.put("lab04c1", idUser);
            parameters.put("lab190c5", timestamp);

            insert.execute(parameters);

            return 1;

        } catch (Exception e)
        {
            e.getMessage();
            return -1;
        }
    }

    /**
     * Retorna el idDemografico Hijo
     *
     *
     * @param idFather
     * @param idFatheriItem
     * @return el estado de la muestra
     * @throws Exception Error en la base de datos.
     */
    default int getValueIdDemographicSon(int idFather, int idFatheriItem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab190c3 ")
                    .append("FROM lab190  ")
                    .append("WHERE lab190c1 = ").append(idFather);
         

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab190c3");
            });
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     *
     * Retorna listado de id item de un id demografico hijo.
     *
     * @param idFather
     * @param idFatheriItem
     * @param idDemographicSon
     * @return el estado de la muestra
     * @throws Exception Error en la base de datos.
     */
    default List<Integer> getidItems(int idFather, int idFatheriItem, int idDemographicSon) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab190c4 ")
                    .append("FROM lab190  ")
                    .append("WHERE lab190c1 = ").append(idFather)
                    .append(" AND lab190c2 = ").append(idFatheriItem)
                    .append(" AND lab190c3 = ").append(idDemographicSon);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab190c4");
            });
        } catch (Exception e)
        {
            return new ArrayList<>(0);
        }

    }

    /**
     * Retorna el idDemografico Hijo
     *
     *
     *
     * @param idFather
     *
     * @return el estado de la muestra
     * @throws Exception Error en la base de datos.
     */
    default Integer getIdDemographicSon(int idFather) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab190c3 ")
                    .append("FROM lab190  ")
                    .append("WHERE lab190c1 = ").append(idFather);
            ;

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab190c3");
            });
        } catch (Exception e)
        {
            return 0;
        }
    }

}
