package net.cltech.enterprisent.dao.impl.postgresql.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicItemDao;
import net.cltech.enterprisent.domain.masters.demographic.BranchDemographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DemographicRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de los
 * demografico items para PostgreSQL
 *
 * @version 1.0.0
 * @author enavas
 * @since 08/05/2017
 * @see Creación
 */
@Repository
public class DemographicItemDaoPostgreSQL implements DemographicItemDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<DemographicItem> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab63.lab63c1"
                    + ", lab63.lab63c2"
                    + ", lab63.lab63c7"
                    + ", lab63.lab63c3"
                    + ", lab63.lab07c1"
                    + ", lab63.lab63c5"
                    + ", lab63.lab63c6"
                    + ", lab63.lab62c1"
                    + ", lab63.lab04c1"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab62.lab63c1 AS item"
                    + ", lab63.lab63c8"
                    + "  FROM lab63"
                    + "  INNER JOIN lab62 ON lab62.lab62c1=lab63.lab62c1"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab63.lab04c1", (ResultSet rs, int i)
                    ->
            {
                DemographicItem demographicItem = new DemographicItem();

                demographicItem.setId(rs.getInt("lab63c1"));
                demographicItem.setCode(rs.getString("lab63c2"));
                demographicItem.setEmail(rs.getString("lab63c7"));
                demographicItem.setName(rs.getString("lab63c3"));
                demographicItem.setNameEnglish(rs.getString("lab63c8"));
                demographicItem.setState(rs.getBoolean("lab07c1"));
                demographicItem.setDescription(rs.getString("lab63c5"));
                demographicItem.setLastTransaction(rs.getTimestamp("lab63c6"));
                demographicItem.setDemographic(rs.getInt("lab62c1"));
                /*Usuario*/
                demographicItem.getUser().setId(rs.getInt("lab04c1"));
                demographicItem.getUser().setName(rs.getString("lab04c2"));
                demographicItem.getUser().setLastName(rs.getString("lab04c3"));
                demographicItem.getUser().setUserName(rs.getString("lab04c4"));
                //validamos si es item seleccionado
                demographicItem.setDefaultItem(false);
                if (rs.getInt("item") == rs.getInt("lab63c1"))
                {
                    demographicItem.setDefaultItem(true);
                }

                return demographicItem;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public DemographicItem create(DemographicItem demographicItem) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab63")
                .usingColumns("lab63c2", "lab63c7", "lab63c3", "lab07c1", "lab63c5", "lab63c6", "lab62c1", "lab04c1", "lab63c8")
                .usingGeneratedKeyColumns("lab63c1");

        HashMap parameters = new HashMap();

        parameters.put("lab63c2", demographicItem.getCode());
        parameters.put("lab63c7", demographicItem.getEmail());
        parameters.put("lab63c3", demographicItem.getName());
        parameters.put("lab07c1", 1);
        parameters.put("lab63c5", demographicItem.getDescription());
        parameters.put("lab63c6", timestamp);
        parameters.put("lab62c1", demographicItem.getDemographic());
        parameters.put("lab04c1", demographicItem.getUser().getId());
        parameters.put("lab63c8", demographicItem.getNameEnglish());

        Number key = insert.executeAndReturnKey(parameters);

        demographicItem.setId(key.intValue());
        demographicItem.setLastTransaction(timestamp);
        return demographicItem;
    }

    @Override
    public List<DemographicItem> get(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception
    {
        try
        {
            String query = ""
                    + "  SELECT lab63.lab63c1"
                    + ", lab63.lab63c2"
                    + ", lab63.lab63c3"
                    + ", lab63.lab63c7"
                    + ", lab63.lab07c1"
                    + ", lab63.lab63c5"
                    + ", lab63.lab63c6"
                    + ", lab63.lab62c1"
                    + ", lab62.lab62c2"
                    + ", lab63.lab04c1"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab62.lab63c1 AS item"
                    + ", lab63.lab63c8"
                    + "  FROM lab63"
                    + "  INNER JOIN lab62 ON lab62.lab62c1=lab63.lab62c1"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab63.lab04c1";
            //where
            if (id != null)
            {
                query += !query.contains("WHERE") ? " WHERE lab63.lab63c1 = ? " : " AND lab63.lab63c1 = ? ";
            }

            if (code != null)
            {
                query += !query.contains("WHERE") ? " WHERE UPPER(lab63.lab63c2) = ? " : " AND UPPER(lab63.lab63c2) = ? ";
            }

            if (name != null)
            {
                query += !query.contains("WHERE") ? " WHERE UPPER(lab63.lab63c3) = ? " : " AND UPPER(lab63.lab63c3) = ? ";
            }

            if (demographic != null)
            {
                query += !query.contains("WHERE") ? " WHERE lab63.lab62c1 = ? " : " AND lab63.lab62c1 = ? ";
            }

            if (state != null)
            {
                query += !query.contains("WHERE") ? " WHERE lab63.lab07c1 = ? " : " AND lab63.lab07c1 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            List object = new ArrayList(0);
            if (id != null)
            {
                object.add(id);
            }
            if (code != null)
            {
                object.add(code.toUpperCase());
            }
            if (name != null)
            {
                object.add(name.toUpperCase());
            }

            if (demographic != null)
            {
                object.add(demographic);
            }

            if (state != null)
            {
                object.add(state ? 1 : 0);
            }

            return jdbc.query(query,
                    object.toArray(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItem demographicItem = new DemographicItem();

                demographicItem.setId(rs.getInt("lab63c1"));
                demographicItem.setCode(rs.getString("lab63c2"));
                demographicItem.setName(rs.getString("lab63c3"));
                demographicItem.setNameEnglish(rs.getString("lab63c8"));
                demographicItem.setEmail(rs.getString("lab63c7"));
                demographicItem.setState(rs.getBoolean("lab07c1"));
                demographicItem.setDescription(rs.getString("lab63c5"));
                demographicItem.setLastTransaction(rs.getTimestamp("lab63c6"));
                demographicItem.setDemographic(rs.getInt("lab62c1"));
                demographicItem.setDemographicName(rs.getString("lab62c2"));
                /*Usuario*/
                demographicItem.getUser().setId(rs.getInt("lab04c1"));
                demographicItem.getUser().setName(rs.getString("lab04c2"));
                demographicItem.getUser().setLastName(rs.getString("lab04c3"));
                demographicItem.getUser().setUserName(rs.getString("lab04c4"));
                //validamos si es item seleccionado
                demographicItem.setDefaultItem(false);
                if (rs.getInt("item") == rs.getInt("lab63c1"))
                {
                    demographicItem.setDefaultItem(true);
                }
                return demographicItem;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    

    public List<DemographicItem> getwebquery(Integer id, String code, String name, Integer demographic, Boolean state) throws Exception
    {
        try
        {
            String query = ""
                    + "  SELECT lab63.lab63c1"
                    + ", lab63.lab63c2"
                    + ", lab63.lab63c3"
                    + ", lab63.lab07c1"
                    + ", lab63.lab63c5"
                    + ", lab63.lab63c6"
                    + ", lab63.lab63c8"
                    + ", lab63.lab62c1"
                    + ", lab62.lab62c2"
                    + ", lab63.lab04c1"
                    + ", lab181.lab181c2"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"                    
                    + ", lab62.lab63c1 AS item"
                    + "  FROM lab63"
                    + "  INNER JOIN lab62 ON lab62.lab62c1=lab63.lab62c1"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab63.lab04c1"
                    + "  LEFT JOIN lab181 ON lab181.lab181c8=lab63.lab63c1";
            //where
            if (id != null)
            {
                query += !query.contains("WHERE") ? " WHERE lab63.lab63c1 = ? " : " AND lab63.lab63c1 = ? ";
            }

            if (code != null)
            {
                query += !query.contains("WHERE") ? " WHERE UPPER(lab63.lab63c2) = ? " : " AND UPPER(lab63.lab63c2) = ? ";
            }

            if (name != null)
            {
                query += !query.contains("WHERE") ? " WHERE UPPER(lab63.lab63c3) = ? " : " AND UPPER(lab63.lab63c3) = ? ";
            }

            if (demographic != null)
            {
                query += !query.contains("WHERE") ? " WHERE lab63.lab62c1 = ? " : " AND lab63.lab62c1 = ? ";
            }

            if (state != null)
            {
                query += !query.contains("WHERE") ? " WHERE lab63.lab07c1 = ? " : " AND lab63.lab07c1 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            List object = new ArrayList(0);
            if (id != null)
            {
                object.add(id);
            }
            if (code != null)
            {
                object.add(code.toUpperCase());
            }
            if (name != null)
            {
                object.add(name.toUpperCase());
            }

            if (demographic != null)
            {
                object.add(demographic);
            }

            if (state != null)
            {
                object.add(state ? 1 : 0);
            }

            return jdbc.query(query,
                    object.toArray(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItem demographicItem = new DemographicItem();

                demographicItem.setId(rs.getInt("lab63c1"));
                demographicItem.setCode(rs.getString("lab63c2"));
                demographicItem.setName(rs.getString("lab63c3"));
                demographicItem.setNameEnglish(rs.getString("lab63c8"));
                demographicItem.setState(rs.getBoolean("lab07c1"));
                demographicItem.setDescription(rs.getString("lab63c5"));
                demographicItem.setLastTransaction(rs.getTimestamp("lab63c6"));
                demographicItem.setDemographic(rs.getInt("lab62c1"));
                demographicItem.setDemographicName(rs.getString("lab62c2"));
                demographicItem.setUserNameWeQuery(rs.getString("lab181c2"));
                /*Usuario*/
                demographicItem.getUser().setId(rs.getInt("lab04c1"));
                demographicItem.getUser().setName(rs.getString("lab04c2"));
                demographicItem.getUser().setLastName(rs.getString("lab04c3"));
                demographicItem.getUser().setUserName(rs.getString("lab04c4"));
                //validamos si es item seleccionado
                demographicItem.setDefaultItem(false);
                if (rs.getInt("item") == rs.getInt("lab63c1"))
                {
                    demographicItem.setDefaultItem(true);
                }
                return demographicItem;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public DemographicItem update(DemographicItem demographicItem) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query;
        query = ""
                + " UPDATE lab63 SET"
                + "  lab63c2=?"
                + ", lab63c7=?"
                + ", lab63c3=?"
                + ", lab07c1=?"
                + ", lab63c5=?"
                + ", lab63c6=?"                
                + ", lab62c1=?"
                + ", lab04c1=?"
                + ", lab63c8=?"
                + "  WHERE lab63c1 = ?";
        jdbc.update(query, demographicItem.getCode(),
                demographicItem.getEmail(),
                demographicItem.getName(),
                demographicItem.isState() ? 1 : 0,
                demographicItem.getDescription(),
                timestamp,
                demographicItem.getDemographic(),
                demographicItem.getUser().getId(),
                demographicItem.getNameEnglish(),
                demographicItem.getId()
        );
        demographicItem.setLastTransaction(timestamp);
        return demographicItem;
    }

    /**
     * Consulta los demograficos item por sede y demografico.
     *
     * @param branch
     * @param idDemographic Id demografico
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<BranchDemographic> demographicsItemList(int branch, int idDemographic) throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab05c1, lab62c1, lab63c1 "
                    + "FROM lab108 "
                    + "WHERE lab05c1 = ? AND lab62c1 = ?",
                    new Object[]
                    {
                        branch, idDemographic
                    }, (ResultSet rs, int i)
                    ->
            {
                BranchDemographic branchDemographic = new BranchDemographic();

                branchDemographic.setId(rs.getInt("lab05c1"));
                branchDemographic.getDemographic().setId(rs.getInt("lab62c1"));
                branchDemographic.getDemographicItem().setId(rs.getInt("lab63c1"));

                return branchDemographic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Identificar si ya se encuentra creado el registro de valores.
     *
     * @param branch
     * @param idDemographics
     * @param idDemographicItem
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    @Override
    public boolean validateBranchDemographic(int branch, int idDemographics, int idDemographicItem) throws Exception
    {
        try
        {

            return jdbc.queryForObject(""
                    + "SELECT lab05c1, lab62c1, lab63c1 "
                    + "FROM lab108"
                    + " WHERE lab05c1 = ? AND lab62c1 = ? AND lab63c1 = ?",
                    new Object[]
                    {
                        branch, idDemographics, idDemographicItem
                    }, (ResultSet rs, int i) -> true);
        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Elimina los demograficos por id demografico y sede
     *
     * @param demographic Objeto Homologación de Demograficos
     *
     * @throws Exception Error en la base de datos.
     */
    public void deleteDemographicsBranch(BranchDemographic demographic) throws Exception
    {
        String query = " DELETE FROM lab108 WHERE lab05c1 = ? AND lab62c1 = ? AND lab63c1 = ? ";
        jdbc.update(query,
                demographic.getId(),
                demographic.getDemographic().getId(),
                demographic.getDemographicItem().getId());
    }

    /**
     * Obtiene registros de un demografico con su item correspondiente de la
     * tabla de relacion sede-demograficos
     *
     * @param demographics
     * @return lista de maestros
     * @throws Exception Error en base de datos
     */
    @Override
    public List<BranchDemographic> getDemographicsBranch(List<BranchDemographic> demographics) throws Exception
    {
        List<BranchDemographic> mastersList = new ArrayList<>();
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab05c1, lab62c1, lab63c1 ")
                    .append("FROM lab108 ")
                    .append("WHERE lab05c1 = ? AND lab62c1 = ? ");

            for (BranchDemographic demographic : demographics)
            {
                mastersList.addAll(jdbc.query(query.toString(),
                        (ResultSet rs, int i)
                        ->
                {
                    BranchDemographic obj = new BranchDemographic();
                    obj.setId(rs.getInt("lab05c1"));
                    obj.getDemographic().setId(rs.getInt("lab62c1"));
                    obj.getDemographicItem().setId(rs.getInt("lab63c1"));

                    return obj;
                }, demographic.getId(), demographic.getDemographic().getId()));

            }

            return mastersList;
        } catch (Exception e)
        {
            return mastersList;
        }
    }

    /**
     * Elimina el registro de un demografico con su item correspondiente *
     *
     * @param demographics
     * @return 1 - si todos los registros se eliminaron satisfactoriamente, -1 -
     * si algun registro no pudo ser eliminado
     * @throws Exception Error en base de datos
     */
    @Override
    public int deleteDemographicsBranch(List<BranchDemographic> demographics) throws Exception
    {
        try
        {
            demographics.forEach((demographic)
                    ->
            {
                jdbc.update("DELETE FROM lab108 "
                        + " WHERE lab05c1 = ? "
                        + " AND lab62c1 = ?",
                        demographic.getId(),
                        demographic.getDemographic().getId());
            });

            return 1;
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Inserta un demografico con su item correspondiente
     *
     * @param demographics
     * @return 1 - si todos los campos se registraron satisfactoriamente, -1 -
     * si algun campo no pudo ser registrado
     * @throws Exception Error en base de datos
     */
    @Override
    public int createDemographicsBranch(List<BranchDemographic> demographics) throws Exception
    {
        try
        {
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                    .withTableName("lab108");
            demographics.stream().map((demographic)
                    ->
            {
                HashMap parameters = new HashMap();
                parameters.put("lab05c1", demographic.getId());
                parameters.put("lab62c1", demographic.getDemographic().getId());
                parameters.put("lab63c1", demographic.getDemographicItem().getId());
                return parameters;
            }).forEachOrdered((parameters)
                    ->
            {
                batchArray.add(parameters);
            });

            insert.executeBatch(batchArray.toArray(new HashMap[demographics.size()]));
            return 1;
        } catch (Exception e)
        {
            e.getMessage();
            return -1;
        }
    }

    /**
     * Consulta los demograficos item por sede y demografico.
     *
     * @param branch
     * @param idDemographic Id demografico
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<DemographicBranch> demographicsBranch(int branch) throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab62.lab62C1 as lab62C1 , "
                    + "lab62C2,"
                    + "lab62c4,"
                    + "lab62c5,"
                    + "lab62c14,  "
                    + "lab109.lab05C1  "
                    + "FROM lab62 "
                    + "LEFT JOIN lab109 ON lab109.lab62C1 = lab62.lab62C1 AND lab05C1 = ? "
                    + "WHERE lab62.lab07c1 = 1",
                    new Object[]
                    {
                        branch
                    }, (ResultSet rs, int i)
                    ->
            {
                DemographicBranch demographicBranch = new DemographicBranch();

                demographicBranch.setId(rs.getInt("lab62c1"));
                demographicBranch.setNameDemographic(rs.getString("lab62C2"));
                demographicBranch.setEncoded(rs.getInt("lab62c4") != 0);
                demographicBranch.setSelected(rs.getString("lab05c1") != null);
                demographicBranch.setRequired(rs.getInt("lab62c5") == 1);
                demographicBranch.setDefaultValue(rs.getString("lab62c14"));

                return demographicBranch;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene registros de un demografico tabla de Demograficos de una sede
     *
     * @param demographics
     * @return lista de maestros
     * @throws Exception Error en base de datos
     */
    @Override
    public List<DemographicBranch> branchgetDemographics(List<DemographicBranch> demographics) throws Exception
    {
        List<DemographicBranch> mastersList = new ArrayList<>();
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab05c1, lab62c1 ")
                    .append("FROM lab109 ")
                    .append("WHERE lab05c1 = ? AND lab62c1 = ? ");

            for (DemographicBranch demographic : demographics)
            {

                mastersList.addAll(jdbc.query(query.toString(),
                        (ResultSet rs, int i)
                        ->
                {
                    DemographicBranch obj = new DemographicBranch();
                    obj.setIdBranch(rs.getInt("lab05c1"));
                    obj.setId(rs.getInt("lab62c1"));
                    return obj;
                }, demographic.getIdBranch(), demographic.getId()));

            }

            return mastersList;
        } catch (Exception e)
        {
            return mastersList;
        }
    }

    /**
     * Elimina el registro de un demografico con su sede *
     *
     * @param demographics
     * @return 1 - si todos los registros se eliminaron satisfactoriamente, -1 -
     * si algun registro no pudo ser eliminado
     * @throws Exception Error en base de datos
     */
    @Override
    public int deleteBranchDemographics(int idBranch) throws Exception
    {
        try
        {
             jdbc.update("DELETE FROM lab109 "
                        + " WHERE lab05c1 = ? ",
                        idBranch);

            return 1;
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Inserta un demografico correspondiente
     *
     * @param demographics
     * @return 1 - si todos los campos se registraron satisfactoriamente, -1 -
     * si algun campo no pudo ser registrado
     * @throws Exception Error en base de datos
     */
    @Override
    public int createBranchDemographics(List<DemographicBranch> demographics) throws Exception
    {
        try
        {
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                    .withTableName("lab109");
            demographics.stream().map((demographic)
                    ->
            {
                HashMap parameters = new HashMap();
                parameters.put("lab05c1", demographic.getIdBranch());
                parameters.put("lab62c1", demographic.getId());

                return parameters;
            }).forEachOrdered((parameters)
                    ->
            {
                batchArray.add(parameters);
            });

            insert.executeBatch(batchArray.toArray(new HashMap[demographics.size()]));
            return 1;
        } catch (Exception e)
        {
            e.getMessage();
            return -1;
        }
    }

    public int demographicValueRequired(List<DemographicRequired> demographics) throws Exception
    {

        try
        {
            demographics.forEach((demographicValue)
                    ->
            {
                jdbc.update("UPDATE lab62 SET lab62c14 = ?  "
                        + " WHERE lab62c1 = ? ",
                        demographicValue.getDefaultValueRequired(),
                        demographicValue.getIdDemographic());
            });

            return 1;
        } catch (Exception e)
        {
            return -1;
        }
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
}
