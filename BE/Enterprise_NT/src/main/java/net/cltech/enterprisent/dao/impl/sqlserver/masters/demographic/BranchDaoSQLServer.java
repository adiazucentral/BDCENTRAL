package net.cltech.enterprisent.dao.impl.sqlserver.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro Sedes
 * para SQLServer
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/05/2017
 * @see Creación
 */
@Repository
public class BranchDaoSQLServer implements BranchDao {

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Branch> list() throws Exception {
        try {
            return jdbc.query(""
                    + "SELECT lab05c1, lab05c2, lab05c4, lab05c5, lab05c6, lab05c7, lab05c8, lab05c9, lab05c10, lab05c11, lab05c12, lab05c13, lab05c16, lab05c17, lab05.lab07c1, lab05.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab05 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab05.lab04c1", (ResultSet rs, int i)
                    -> {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setAbbreviation(rs.getString("lab05c2"));
                branch.setName(rs.getString("lab05c4"));
                branch.setAddress(rs.getString("lab05c5"));
                branch.setPhone(rs.getString("lab05c6"));
                branch.setMinimum(rs.getInt("lab05c7"));
                branch.setMaximum(rs.getInt("lab05c8"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setResponsable(rs.getString("lab05c11"));
                branch.setEmail(rs.getString("lab05c12"));
                branch.setUrlConnection(rs.getString("lab05c13"));

                branch.setUrlFront(rs.getString("lab05c16"));
                branch.setUrlBackend(rs.getString("lab05c17"));
                branch.setLastTransaction(rs.getTimestamp("lab05c9"));
                /*Usuario*/
                branch.getUser().setId(rs.getInt("lab04c1"));
                branch.getUser().setName(rs.getString("lab04c2"));
                branch.getUser().setLastName(rs.getString("lab04c3"));
                branch.getUser().setUserName(rs.getString("lab04c4"));

                branch.setState(rs.getInt("lab07c1") == 1);

                return branch;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Branch create(Branch branch) throws Exception {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab05")
                .usingColumns("lab05c2", "lab05c4", "lab05c5", "lab05c6", "lab05c7", "lab05c8", "lab05c9", "lab05c10", "lab05c11", "lab05c12", "lab04c1", "lab07c1", "lab05c13", "lab05c16", "lab05c17", "lab05c14", "lab05c15")
                .usingGeneratedKeyColumns("lab05c1");

        HashMap parameters = new HashMap();
        parameters.put("lab05c2", branch.getAbbreviation().trim());
        parameters.put("lab05c4", branch.getName());
        parameters.put("lab05c5", branch.getAddress());
        parameters.put("lab05c6", branch.getPhone());
        parameters.put("lab05c7", branch.getMinimum());
        parameters.put("lab05c8", branch.getMaximum());
        parameters.put("lab05c9", timestamp);
        parameters.put("lab05c10", branch.getCode());
        parameters.put("lab05c11", branch.getResponsable());
        parameters.put("lab05c12", branch.getEmail());
        parameters.put("lab04c1", branch.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab05c13", branch.getUrlConnection());
        parameters.put("lab05c14", branch.getImageMinistry());
        parameters.put("lab05c15", branch.getNumberAppointments());
        parameters.put("lab05c16", branch.getUrlFront());
        parameters.put("lab05c17", branch.getUrlBackend());

        Number key = insert.executeAndReturnKey(parameters);
        branch.setId(key.intValue());
        branch.setLastTransaction(timestamp);

        return branch;
    }

    @Override
    public Branch get(Integer id, String name, String abbr, String code) throws Exception {
        try {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab05c1, lab05c2, lab05c4, lab05c5, lab05c6, lab05c7, lab05c8, lab05c9, lab05c10, lab05c11, lab05c12, lab05.lab07c1, lab05.lab04c1, lab04c2, lab04c3, lab04c4, lab05c13, lab05c16, lab05c17, lab05c14, lab05c15  "
                    + "FROM lab05 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab05.lab04c1 ";
            /*Where*/
            if (id != null) {
                query = query + "WHERE lab05c1 = ? ";
            }
            if (abbr != null) {
                query = query + "WHERE UPPER(lab05c2) = ? ";
            }
            if (name != null) {
                query = query + "WHERE UPPER(lab05c4) = ? ";
            }
            if (code != null) {
                query = query + "WHERE lab05c10 = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null) {
                object = id;
            }
            if (abbr != null) {
                object = abbr.toUpperCase();
            }
            if (name != null) {
                object = name.toUpperCase();
            }
            if (code != null) {
                object = code;
            }

            return jdbc.queryForObject(query,
                    new Object[]{
                        object
                    }, (ResultSet rs, int i)
                    -> {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setAbbreviation(rs.getString("lab05c2"));
                branch.setName(rs.getString("lab05c4"));
                branch.setAddress(rs.getString("lab05c5"));
                branch.setPhone(rs.getString("lab05c6"));
                branch.setMinimum(rs.getInt("lab05c7"));
                branch.setMaximum(rs.getInt("lab05c8"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setResponsable(rs.getString("lab05c11"));
                branch.setEmail(rs.getString("lab05c12"));
                branch.setUrlConnection(rs.getString("lab05c13"));
                branch.setImageMinistry(rs.getString("lab05c14"));
                branch.setNumberAppointments(rs.getInt("lab05c15"));
                branch.setUrlFront(rs.getString("lab05c16"));
                branch.setUrlBackend(rs.getString("lab05c17"));

                branch.setLastTransaction(rs.getTimestamp("lab05c9"));
                /*Usuario*/
                branch.getUser().setId(rs.getInt("lab04c1"));
                branch.getUser().setName(rs.getString("lab04c2"));
                branch.getUser().setLastName(rs.getString("lab04c3"));
                branch.getUser().setUserName(rs.getString("lab04c4"));

                branch.setState(rs.getInt("lab07c1") == 1);

                return branch;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Branch getBasic(Integer id, String name, String abbr, String code) throws Exception {
        try {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab05c1, lab05c2, lab05c7, lab05c8, lab05c15  "
                    + "FROM lab05 ";

            /*Where*/
            if (id != null) {
                query = query + "WHERE lab05c1 = ? ";
            }
            if (abbr != null) {
                query = query + "WHERE UPPER(lab05c2) = ? ";
            }
            if (name != null) {
                query = query + "WHERE UPPER(lab05c4) = ? ";
            }
            if (code != null) {
                query = query + "WHERE lab05c10 = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null) {
                object = id;
            }
            if (abbr != null) {
                object = abbr.toUpperCase();
            }
            if (name != null) {
                object = name.toUpperCase();
            }
            if (code != null) {
                object = code;
            }

            return jdbc.queryForObject(query,
                    new Object[]{
                        object
                    }, (ResultSet rs, int i)
                    -> {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setAbbreviation(rs.getString("lab05c2"));
                branch.setMinimum(rs.getInt("lab05c7"));
                branch.setMaximum(rs.getInt("lab05c8"));
                branch.setNumberAppointments(rs.getInt("lab05c15"));

                return branch;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Branch update(Branch branch) throws Exception {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab05 SET lab05c2 = ?, lab05c4 = ?, lab05c5 = ?, lab05c6 = ?, lab05c7 = ?, lab05c8 = ?, lab05c9 = ?, lab05c10 = ?, lab05c11 = ?, lab05c12 = ?, lab04c1 = ?, lab07c1 = ?, lab05c13 = ?, lab05c16 = ?, lab05c17 = ?, lab05c14 = ?, lab05c15 = ?  "
                + "WHERE lab05c1 = ?",
                branch.getAbbreviation(), branch.getName(), branch.getAddress(), branch.getPhone(), branch.getMinimum(), branch.getMaximum(), timestamp, branch.getCode(), branch.getResponsable(), branch.getEmail(), branch.getUser().getId(), branch.isState() ? 1 : 0, branch.getUrlConnection(), branch.getUrlFront(), branch.getUrlBackend(), branch.getImageMinistry(), branch.getNumberAppointments(), branch.getId());

        branch.setLastTransaction(timestamp);

        return branch;
    }

    @Override
    public void delete(Integer id) throws Exception {

    }

    @Override
    public List<Branch> listLogin() throws Exception {
        try {
            return jdbc.query("SELECT lab05c1,lab05c4,lab05c16,lab05c17  FROM lab05 WHERE lab07c1 = 1",
                    (ResultSet rs, int i)
                    -> {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setName(rs.getString("lab05c4"));
                branch.setUrlFront(rs.getString("lab05c16"));
                branch.setUrlBackend(rs.getString("lab05c17"));
                return branch;
            });

        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<Branch> filterByUsernameLogin(String username) throws Exception {
        try {
            return jdbc.query("SELECT lab05.lab05c1, lab05c4 "
                    + ",lab05c16, lab05c17 "
                    + "FROM lab93 "
                    + "INNER JOIN lab05 ON lab93.lab05c1 = lab05.lab05c1 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab93.lab04c1 "
                    + "WHERE LOWER(lab04c4) = ? and lab04.lab07c1 = 1 and lab05.lab07c1 = 1",
                    new Object[]{
                        username.trim().toLowerCase()
                    }, (ResultSet rs, int i)
                    -> {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setName(rs.getString("lab05c4"));
                branch.setUrlFront(rs.getString("lab05c16"));
                branch.setUrlBackend(rs.getString("lab05c17"));

                return branch;
            });

        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }

    }

    @Override
    public List<Branch> filterByUsername(String username) throws Exception {
        try {
            return jdbc.query("SELECT lab05.lab05c1, lab05c2, lab05c4 "
                    + ",lab05c5, lab05c6, lab05c7, lab05c8, lab05c9, lab05c10 "
                    + ",lab05c11, lab05c12 "
                    + "FROM lab93 "
                    + "INNER JOIN lab05 ON lab93.lab05c1 = lab05.lab05c1 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab93.lab04c1 "
                    + "WHERE LOWER(lab04c4) = ? and lab04.lab07c1 = 1 and lab05.lab07c1 = 1",
                    new Object[]{
                        username.trim().toLowerCase()
                    }, (ResultSet rs, int i)
                    -> {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setAbbreviation(rs.getString("lab05c2"));
                branch.setName(rs.getString("lab05c4"));
                branch.setAddress(rs.getString("lab05c5"));
                branch.setPhone(rs.getString("lab05c6"));
                branch.setMinimum(rs.getInt("lab05c7"));
                branch.setMaximum(rs.getInt("lab05c8"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setResponsable(rs.getString("lab05c11"));
                branch.setEmail(rs.getString("lab05c12"));

                branch.setLastTransaction(rs.getTimestamp("lab05c9"));

                return branch;
            });

        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }

    }

}
