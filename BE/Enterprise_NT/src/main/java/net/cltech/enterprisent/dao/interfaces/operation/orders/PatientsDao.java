package net.cltech.enterprisent.dao.interfaces.operation.orders;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.PatientPhoto;
import net.cltech.enterprisent.domain.operation.reports.PatientReport;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.ListEnum;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Interfaz de acceso a datos para los pacientes
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creación
 */
public interface PatientsDao
{

    /**
     * Obtiene el dao de Comentarios
     *
     * @return Instancia de CommentDao
     */
    public CommentDao getCommentDao();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

    /**
     * Obtiene un paciente buscandolo por la historia y tipo de docuemnto
     *
     * @param encryptedPatientId Historia del paciente encriptada
     * @param documentTypeId Id del tipo del documento (Opcional)
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient get(String encryptedPatientId, Integer documentTypeId, final List<Demographic> demographics) throws Exception
    {
        try
        {
            String select = ""
                    + "SELECT   lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21.lab80c1 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c8 "
                    + " , lab21c9 "
                    + " , lab21c10 "
                    + " , lab21c11 "
                    + " , lab21c12 "
                    + " , lab21.lab04c1 "
                    + " , lab04c2 "
                    + " , lab04c3 "
                    + " , lab04c4 "
                    + " , lab21.lab08c1 "
                    + " , lab08c2 "
                    + " , lab08c5 "
                    + " , lab21.lab08c1 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c2 "
                    + " , lab54.lab54c3 "
                    + " , lab21c13 "
                    + " , lab21c16 "
                    + " , lab21c17 ";
            String from = ""
                    + " FROM     Lab21 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab21.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE    lab21c2 = ? ";
            List<Object> params = new ArrayList<>(0);
            params.add(encryptedPatientId);

            if (documentTypeId != 0)
            {
                where = where + " AND lab54.lab54c1 = ? ";
                params.add(documentTypeId);
            }

            return getJdbcTemplate().queryForObject(select + from + where, params.toArray(), (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
                patient.setPhone(rs.getString("lab21c16"));
                patient.setAddress(rs.getString("lab21c17"));
                patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                patient.setLastUpdateUser(user);

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setCode(rs.getString("lab08c5"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                if (rs.getString("lab54c1") != null)
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics)
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    patient.getDemographics().add(demoValue);
                }
                return patient;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente buscandolo por id de base de datos
     *
     * @param id Id base de datos
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient get(int id, final List<Demographic> demographics) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21.lab80c1 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c8 "
                    + " , lab21c9 "
                    + " , lab21c10 "
                    + " , lab21c11 "
                    + " , lab21c12 "
                    + " , lab21c14 "
                    + " , lab21.lab04c1 "
                    + " , lab04c2 "
                    + " , lab04c3 "
                    + " , lab04c4 "
                    + " , lab21.lab08c1 "
                    + " , lab08c2 "
                    + " , lab08c5 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c2 "
                    + " , lab54.lab54c3 "
                    + " , lab21c13 "
                    + " , lab21c16 "
                    + " , lab21c17 "
                    + " , lab21c19 ";
            String from = ""
                    + " FROM     Lab21 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         LEFT JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";
                    select += ", demo" + demographic.getId() + ".lab63c5 as demo" + demographic.getId() + "_description";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab21.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE    Lab21C1 = ? ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
                patient.setPhoto(rs.getString("lab21c14"));
                patient.setPhone(rs.getString("lab21c16"));
                patient.setAddress(rs.getString("lab21c17"));
                patient.setPasswordWebQuery(Tools.decrypt(rs.getString("lab21c19")));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                patient.setLastUpdateUser(user);

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setCode(rs.getString("lab08c5"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                if (rs.getString("lab54c1") != null)
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics)
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                            demoValue.setCodifiedDescription(rs.getString("demo" + demographic.getId() + "_description"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    patient.getDemographics().add(demoValue);
                }
                return patient;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente buscandolo por id de base de datos
     *
     * @param id Id base de datos
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient getEmail(int id, final List<Demographic> demographics) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab21.lab08c1 "
                    + " , lab08c6 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c6 ";
            String from = ""
                    + " FROM     Lab21 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";
                    select += ", demo" + demographic.getId() + ".lab63c5 as demo" + demographic.getId() + "_description";
                    select += ", demo" + demographic.getId() + ".lab63c7 as demo" + demographic.getId() + "_email";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab21.lab_demo_" + demographic.getId();
                }
            }
            String where = " WHERE    Lab21C1 = ? ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                DemographicValue demoValue = null;

                if (rs.getString("lab08c1") != null )
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(rs.getInt("lab08c1"));
                    demoValue.setDemographic("RACE");
                    demoValue.setEmail(rs.getString("lab08c6"));
                    patient.getDemographics().add(demoValue);
                }

                if (rs.getString("lab54c1") != null )
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(rs.getInt("lab54c1"));
                    demoValue.setDemographic("DOCUMENTYPE");
                    demoValue.setEmail(rs.getString("lab54c6"));
                    patient.getDemographics().add(demoValue);
                }

                for (Demographic demographic : demographics)
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                            demoValue.setCodifiedDescription(rs.getString("demo" + demographic.getId() + "_description"));
                            demoValue.setEmail(rs.getString("demo" + demographic.getId() + "_email"));
                            patient.getDemographics().add(demoValue);
                        }
                    }
                }
                return patient;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente buscandolo por la historia
     *
     * @param encryptedPatientId Historia del paciente encriptada
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient get(String encryptedPatientId, final List<Demographic> demographics) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21.lab80c1 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c8 "
                    + " , lab21c9 "
                    + " , lab21c10 "
                    + " , lab21c11 "
                    + " , lab21c12 "
                    + " , lab21.lab04c1 "
                    + " , lab04c2 "
                    + " , lab04c3 "
                    + " , lab04c4 "
                    + " , lab21.lab08c1 "
                    + " , lab08c2 "
                    + " , lab08c5 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c2 "
                    + " , lab54.lab54c3 "
                    + " , lab21c13 ";
            String from = ""
                    + " FROM     Lab21 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab21.lab_demo_" + demographic.getId();
                }
            }
            String where = ""
                    + " WHERE    Lab21C2 = ? ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
                patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                patient.setLastUpdateUser(user);

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setCode(rs.getString("lab08c5"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                if (rs.getString("lab54c1") != null)
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics)
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    patient.getDemographics().add(demoValue);
                }
                return patient;
            }, encryptedPatientId);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente buscandolo por la historia y tipo de docuemnto
     *
     * @param encryptedPatientId Historia del paciente encriptada
     * @param documentTypeId Id del tipo del documento (Opcional)
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     * @param id
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient get(String encryptedPatientId, Integer documentTypeId, final List<Demographic> demographics, int id) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21.lab80c1 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c8 "
                    + " , lab21c9 "
                    + " , lab21c10 "
                    + " , lab21c11 "
                    + " , lab21c12 "
                    + " , lab21.lab04c1 "
                    + " , lab04c2 "
                    + " , lab04c3 "
                    + " , lab04c4 "
                    + " , lab21.lab08c1 "
                    + " , lab08c2 "
                    + " , lab08c5 "
                    + " , lab21.lab08c1 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c2 "
                    + " , lab54.lab54c3 "
                    + " , lab21c13 "
                    + " , lab21c16 "
                    + " , lab21c17 "
                    + " , lab21c19 ";
            String from = ""
                    + " FROM     Lab21 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab21.lab_demo_" + demographic.getId();
                }
            }

            List<Object> params = new ArrayList<>(0);
            String where = " WHERE ";
            if (id != 0)
            {
                where = where + " lab21c1 = ? ";
                params.add(id);
            } else
            {
                where = where + "  lab21c2 = ? ";
                params.add(encryptedPatientId);

                if (documentTypeId != 0)
                {
                    where = where + " AND lab54.lab54c1 = ? ";
                    params.add(documentTypeId);
                }
            }

            return getJdbcTemplate().queryForObject(select + from + where, params.toArray(), (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
                patient.setPhone(rs.getString("lab21c16"));
                patient.setAddress(rs.getString("lab21c17"));
                patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                patient.setLastUpdateUser(user);

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setCode(rs.getString("lab08c5"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                if (rs.getString("lab54c1") != null)
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics)
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    patient.getDemographics().add(demoValue);
                }
                patient.setPasswordWebQuery(rs.getString("lab21c19"));
                return patient;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente buscandolo por demografico no codificado
     *
     * @param demographicId id del demografico no codificado
     * @param demographicValue valor del demografico a comparar
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient getPatienByDemographic(int demographicId, String demographicValue, final List<Demographic> demographics) throws Exception
    {
        try
        {
            int count = 0;
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21.lab80c1 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c8 "
                    + " , lab21c9 "
                    + " , lab21c10 "
                    + " , lab21c11 "
                    + " , lab21c12 "
                    + " , lab21.lab04c1 "
                    + " , lab04c2 "
                    + " , lab04c3 "
                    + " , lab04c4 "
                    + " , lab21.lab08c1 "
                    + " , lab08c2 "
                    + " , lab08c5 "
                    + " , lab21.lab08c1 "
                    + " , lab54.lab54c1 "
                    + " , lab54.lab54c2 "
                    + " , lab54.lab54c3 "
                    + " , lab21c13 "
                    + " , lab21c16 "
                    + " , lab21c17 ";
            String from = ""
                    + " FROM     Lab21 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            for (Demographic demographic : demographics)
            {
                if (demographic.isEncoded())
                {
                    select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                    select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                    select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                    from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                } else
                {
                    select += ", Lab21.lab_demo_" + demographic.getId();
                }

                if (demographicId == demographic.getId())
                {
                    count++;
                }
            }
            String where = "";
            if (count > 0)
            {
                where = " WHERE    upper(Lab21.lab_demo_" + demographicId + ") = '" + demographicValue.toUpperCase() + "'";
            } else
            {
                return null;
            }

            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
                patient.setPhone(rs.getString("lab21c16"));
                patient.setAddress(rs.getString("lab21c17"));
                patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                patient.setLastUpdateUser(user);

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setCode(rs.getString("lab08c5"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                if (rs.getString("lab54c1") != null)
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }

                DemographicValue demoValue = null;
                for (Demographic demographic : demographics)
                {
                    demoValue = new DemographicValue();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    patient.getDemographics().add(demoValue);
                }
                return patient;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Inserta un nuevo paciente con sus demograficos
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     *
     * @return Paciente actualizado
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en base de datos
     */
    default Patient insert(Patient patient) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab21")
                .usingGeneratedKeyColumns("lab21c1");
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("lab21c2", patient.getPatientId());
        parameters.put("lab21c3", patient.getName1());
        parameters.put("lab21c4", patient.getName2());
        parameters.put("lab21c5", patient.getLastName());
        parameters.put("lab21c6", patient.getSurName());
        parameters.put("lab80c1", patient.getSex().getId());
        parameters.put("lab21c7", new Timestamp(patient.getBirthday().getTime()));
        parameters.put("lab21c8", patient.getEmail());
        parameters.put("lab21c9", patient.getSize());
        parameters.put("lab21c10", patient.getWeight());
        parameters.put("lab21c11", patient.getDateOfDeath() != null ? new Timestamp(patient.getDateOfDeath().getTime()) : null);
        parameters.put("lab21c12", new Timestamp(patient.getLastUpdateDate().getTime()));
        parameters.put("lab21c13", patient.getDiagnostic());
        parameters.put("lab04c1", patient.getLastUpdateUser().getId());
        parameters.put("lab08c1", patient.getRace() == null ? null : patient.getRace().getId());
        parameters.put("lab54c1", patient.getDocumentType() == null ? null : patient.getDocumentType().getId());
        parameters.put("lab21c16", patient.getPhone());
        parameters.put("lab21c17", patient.getAddress());
        parameters.put("lab21c18", patient.getPatientId());
        parameters.put("lab21c20", new Timestamp(patient.getLastUpdateDate().getTime()));
        parameters.put("lab04c1_2", patient.getLastUpdateUser().getId());
        parameters.put("lab21c19", patient.getPasswordWebQuery());
        parameters.put("lab21c22", 0);
        parameters.put("lab21c24", 1);
        if (patient.getDemographics() != null && !patient.getDemographics().isEmpty())
        {
            patient.getDemographics().forEach((demographic) ->
            {
                parameters.put("lab_demo_" + demographic.getIdDemographic(), demographic.isEncoded() ? demographic.getCodifiedId() : demographic.getNotCodifiedValue());
            });
        }

        int id = insert.executeAndReturnKey(parameters).intValue();
        patient.setId(id);
        return patient;
    }

    /**
     * Actualiza la informacion de un paciente y sus demograficos
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     *
     * @return Paciente actualizado
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en base de datos
     */
    default Patient update(Patient patient) throws Exception
    {
        String update = ""
                + "UPDATE   lab21 "
                + "SET lab21c2 = ? "
                + " , lab21c3 = ? "
                + " , lab21c4 = ? "
                + " , lab21c5 = ? "
                + " , lab21c6 = ? "
                + " , lab80c1 = ? "
                + " , lab21c7 = ? "
                + " , lab21c8 = ? "
                + " , lab21c9 = ? "
                + " , lab21c10 = ? "
                + " , lab21c11 = ? "
                + " , lab21c12 = ? "
                + " , lab04c1 = ? "
                + " , lab08c1 = ? "
                + " , lab54c1 = ? "
                + " , lab21c16 = ? "
                + " , lab21c17 = ? "
                + " , lab21c18 = ? "
                + " , lab21c19 = ? "
                + " , lab21c24 = 1 "
                + " , lab21c25 = 0 ";
        Object[] args = new Object[20 + (patient.getDemographics() != null ? patient.getDemographics().size() : 0)];
        args[0] = patient.getPatientId();
        args[1] = patient.getName1();
        args[2] = patient.getName2();
        args[3] = patient.getLastName();
        args[4] = patient.getSurName();
        args[5] = patient.getSex().getId();
        args[6] = patient.getBirthday() != null ? new Timestamp(patient.getBirthday().getTime()) : null;
        args[7] = patient.getEmail();
        args[8] = patient.getSize();
        args[9] = patient.getWeight();
        args[10] = patient.getDateOfDeath() != null ? new Timestamp(patient.getDateOfDeath().getTime()) : null;
        args[11] = new Timestamp(patient.getLastUpdateDate().getTime());
        args[12] = patient.getLastUpdateUser().getId();
        args[13] = patient.getRace() == null ? null : patient.getRace().getId();
        args[14] = patient.getDocumentType() == null ? null : patient.getDocumentType().getId();
        args[15] = patient.getPhone();
        args[16] = patient.getAddress();
        args[17] = patient.getPatientId();
        args[18] = patient.getPasswordWebQuery();

        int index = 19;
        for (DemographicValue demographic : patient.getDemographics())
        {
            update += ", lab_demo_" + demographic.getIdDemographic() + " = ? ";
            args[index] = demographic.isEncoded() ? demographic.getCodifiedId() : demographic.getNotCodifiedValue();
            index++;
        }

        update += " WHERE lab21c1 = ? ";
        args[index] = patient.getId();
        int affectedRows = getJdbcTemplate().update(update, args);
        return affectedRows > 0 ? patient : null;
    }

    /**
     * Obtiene un paciente buscandolo por numero de orden
     *
     * @param order Numero de Orden
     * @param demographics Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * demograficos del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient get(long order, final List<Demographic> demographics) throws Exception
    {
        try
        {
            if (order != 0)
            {
                Integer year = Tools.YearOfOrder(String.valueOf(order));
                Integer currentYear = DateTools.dateToNumberYear(new Date());
                String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                if (tableExists)
                {
                    String select = "" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT   lab21.lab21c1 "
                            + " , lab21c2 "
                            + " , lab21c3 "
                            + " , lab21c4 "
                            + " , lab21c5 "
                            + " , lab21c6 "
                            + " , lab21.lab80c1 "
                            + " , lab80c3 "
                            + " , lab80c4 "
                            + " , lab80c5 "
                            + " , lab21c7 "
                            + " , lab21c8 "
                            + " , lab21c9 "
                            + " , lab21c10 "
                            + " , lab21c11 "
                            + " , lab21c12 "
                            + " , lab21.lab04c1 "
                            + " , lab04c2 "
                            + " , lab04c3 "
                            + " , lab04c4 "
                            + " , lab21.lab08c1 "
                            + " , lab08c2 "
                            + " , lab54.lab54c1 "
                            + " , lab54.lab54c2 "
                            + " , lab54.lab54c3 "
                            + " , lab21c13 "
                            + " , lab21c16 "
                            + " , lab21c17 ";
                    String from = ""
                            + " FROM      " + lab22 + " as lab22 "
                            + "         INNER JOIN  Lab21 ON Lab21.Lab21C1 = Lab22.Lab21C1 "
                            + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                            + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                            + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                            + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
                    for (Demographic demographic : demographics)
                    {
                        if (demographic.isEncoded())
                        {
                            select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                            select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                            select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                            from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                        } else
                        {
                            select += ", Lab21.lab_demo_" + demographic.getId();
                        }
                    }
                    String where = " WHERE    Lab22C1 = ? ";
                    return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
                    {
                        Patient patient = new Patient();
                        patient.setId(rs.getInt("lab21c1"));
                        patient.setPatientId(Tools.decryptPatient(rs.getString("lab21c2")));
                        patient.setName1(Tools.decryptPatient(rs.getString("lab21c3")));
                        patient.setName2(Tools.decryptPatient(rs.getString("lab21c4")));
                        patient.setLastName(Tools.decryptPatient(rs.getString("lab21c5")));
                        patient.setSurName(Tools.decryptPatient(rs.getString("lab21c6")));

                        Item item = new Item();
                        item.setId(rs.getInt("lab80c1"));
                        item.setCode(rs.getString("lab80c3"));
                        item.setEsCo(rs.getString("lab80c4"));
                        item.setEnUsa(rs.getString("lab80c5"));
                        patient.setSex(item);

                        patient.setPhone(rs.getString("lab21c16"));
                        patient.setAddress(rs.getString("lab21c17"));
                        patient.setBirthday(rs.getTimestamp("lab21c7"));
                        patient.setEmail(rs.getString("lab21c8"));
                        patient.setSize(rs.getBigDecimal("lab21c9"));
                        patient.setWeight(rs.getBigDecimal("lab21c10"));
                        patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                        patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
                        patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));

                        User user = new User();
                        user.setId(rs.getInt("lab04c1"));
                        user.setName(rs.getString("lab04c2"));
                        user.setLastName(rs.getString("lab04c3"));
                        user.setUserName(rs.getString("lab04c4"));
                        patient.setLastUpdateUser(user);

                        if (rs.getString("lab08c1") != null)
                        {
                            Race race = new Race();
                            race.setId(rs.getInt("lab08c1"));
                            race.setName(rs.getString("lab08c2"));
                            patient.setRace(race);
                        }

                        if (rs.getString("lab54c1") != null)
                        {
                            DocumentType documentType = new DocumentType();
                            documentType.setId(rs.getInt("lab54c1"));
                            documentType.setAbbr(rs.getString("lab54c2"));
                            documentType.setName(rs.getString("lab54c3"));
                            patient.setDocumentType(documentType);
                        }

                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics)
                        {
                            demoValue = new DemographicValue();
                            demoValue.setIdDemographic(demographic.getId());
                            demoValue.setDemographic(demographic.getName());
                            demoValue.setEncoded(demographic.isEncoded());
                            if (demographic.isEncoded())
                            {
                                if (rs.getString("demo" + demographic.getId() + "_id") != null)
                                {
                                    demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                    demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                    demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                                }
                            } else
                            {
                                demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                            }
                            patient.getDemographics().add(demoValue);
                        }
                        return patient;
                    }, order);
                } else
                {
                    return new Patient();
                }
            } else
            {
                return new Patient();
            }

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente buscandolo por numero de orden
     *
     * @param order Numero de Orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en base de datos
     */
    default Patient getPatienbyOrder(long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21.lab21c1 "
                    + " , lab21c2 "
                    + " , lab21c3 "
                    + " , lab21c4 "
                    + " , lab21c5 "
                    + " , lab21c6 "
                    + " , lab21.lab80c1 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab21c7 "
                    + " , lab21c8 "
                    + " , lab21c9 "
                    + " , lab21c10 "
                    + " , lab21c11 "
                    + " , lab21c12 "
                    + " , lab21.lab08c1 "
                    + " , lab08c2 "
                    + " , lab21c13 "
                    + " , lab21c16 "
                    + " , lab21c17 ";
            String from = ""
                    + " FROM      " + lab22 + " as lab22 "
                    + "         INNER JOIN  Lab21 ON Lab21.Lab21C1 = Lab22.Lab21C1 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 ";
            String where = " WHERE    Lab22C1 = ? ";
            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setPhone(rs.getString("lab21c16"));
                patient.setAddress(rs.getString("lab21c17"));
                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                return patient;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la foto de un paciente
     *
     * @param photo
     *
     * @throws Exception Error en base de datos
     */
    default void updatePhoto(PatientPhoto photo) throws Exception
    {
        getJdbcTemplate().update(""
                + "UPDATE   lab21 "
                + "SET      lab21c14 = ? , lab21c25 = 0 "
                + "WHERE    lab21c1 = ? ",
                photo.getPhotoInBase64(),
                photo.getId());
    }

    /**
     * Obtiene la foto de un paciente
     *
     * @param id Id DB Paciente
     *
     * @return Foto en base64, null en caso de tener foto
     * @throws Exception Error en base de datos
     */
    default PatientPhoto getPhoto(int id) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c14 "
                    + "FROM     lab21 "
                    + "WHERE    lab21c1 = ? ",
                    (ResultSet rs, int i) ->
            {
                PatientPhoto photo = new PatientPhoto();
                photo.setPhotoInBase64(rs.getString("lab21c14"));
                return photo;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default List<Patient> list(Integer gender, String name, String name1, String lastName, String surName, String encryptedRecord, Integer document, final List<Demographic> demographics) throws Exception
    {
        try
        {
            List<Object> params = new ArrayList<>();
            String select = ISOLATION_READ_UNCOMMITTED + "SELECT "
                    + "lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c12,lab21c13,"
                    + "lab21.lab80c1, lab80c3, lab80c4, lab80c5, "
                    + "lab21.lab04c1, lab04c2, lab04c3, lab04c4, lab21.lab08c1, lab08c2,"
                    + "lab54.lab54c1, lab54.lab54c2, lab54.lab54c3 ";
            String from = ""
                    + " FROM     Lab21 "
                    + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                    + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                    + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                    + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ";
            if (demographics != null)
            {
                for (Demographic demographic : demographics)
                {
                    if (demographic.isEncoded())
                    {
                        select += ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                        select += ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                        select += ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                        from += " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + +demographic.getId() + ".lab63c1";
                    } else
                    {
                        select += ", Lab21.lab_demo_" + demographic.getId();
                    }
                }
            }

            String where = "";

            if (gender != null && gender != ListEnum.Gender.BOTH.getValue())
            {
                where += "AND lab80.lab80c1 = ? ";
                params.add(gender);
            }
            if (name != null)
            {
                where += "AND lab21.lab21c3 = ? ";
                params.add(name);
            }
            
            if (name1 != null)
            {
                where += "AND lab21.lab21c4 = ? ";
                params.add(name1);
            }
            
            if (document != null)
            {
                where += "AND lab54c1 = ? ";
                params.add(document);
            }

            if (lastName != null)
            {
                where += "AND lab21.lab21c5 = ? ";
                params.add(lastName);
            }

            if (surName != null)
            {
                where += "AND lab21.lab21c6 = ? ";
                params.add(surName);
            }

            where = where.replaceFirst("AND", "WHERE");
            return getJdbcTemplate().query(select + from + where,
                    params.toArray(),
                    (ResultSet rs, int i) ->
            {
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                Item item = new Item();
                item.setId(rs.getInt("lab80c1"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                patient.setSex(item);

                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setEmail(rs.getString("lab21c8"));
                patient.setSize(rs.getBigDecimal("lab21c9"));
                patient.setWeight(rs.getBigDecimal("lab21c10"));
                patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));
                patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
                patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));

                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                patient.setLastUpdateUser(user);

                if (rs.getString("lab08c1") != null)
                {
                    Race race = new Race();
                    race.setId(rs.getInt("lab08c1"));
                    race.setName(rs.getString("lab08c2"));
                    patient.setRace(race);
                }

                if (rs.getString("lab54c1") != null)
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    patient.setDocumentType(documentType);
                }
                if (demographics != null)
                {
                    DemographicValue demoValue = null;
                    for (Demographic demographic : demographics)
                    {
                        demoValue = new DemographicValue();
                        demoValue.setIdDemographic(demographic.getId());
                        demoValue.setDemographic(demographic.getName());
                        demoValue.setEncoded(demographic.isEncoded());
                        if (demographic.isEncoded())
                        {
                            if (rs.getString("demo" + demographic.getId() + "_id") != null)
                            {
                                demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                            }
                        } else
                        {
                            demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                        }
                        patient.getDemographics().add(demoValue);
                    }
                }

                return patient;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Busca un paciente por nombres y fecha de nacimiento o sexo
     *
     * @param encryptedLastName Apellido
     * @param encryptedSurName Segundo Apellido
     * @param encryptedName1 Nombre 1
     * @param encryptedName2 Nombre 2
     * @param birthday Fecha de nacimiento
     * @param sex Sexo
     * @return Lista de pacientes encontrados
     * @throws Exception Error en base de datos
     */
    default List<Patient> get(String encryptedLastName, String encryptedSurName, String encryptedName1, String encryptedName2, Date birthday, Integer sex) throws Exception
    {
        String select = "" + ISOLATION_READ_UNCOMMITTED
                + "SELECT lab21c1 "
                + "     , lab21c2 "
                + "     , lab21c3 "
                + "     , lab21c4 "
                + "     , lab21c5 "
                + "     , lab21c6 "
                + "     , lab21c7 "
                + "     , lab21c8 "
                + "     , lab21c9 "
                + "     , lab21c10 "
                + "     , lab21c11 "
                + "     , lab21c12 "
                + "     , lab21c13 "
                + "     , lab21.lab80c1 "
                + "     , lab80c3 "
                + "     , lab80c4 "
                + "     , lab80c5 "
                + "     , lab21.lab04c1 "
                + "     , lab04c2 "
                + "     , lab04c3 "
                + "     , lab04c4 "
                + "     , lab21.lab08c1 "
                + "     , lab08c2 "
                + "     , lab54.lab54c1 "
                + "     , lab54.lab54c2 "
                + "     , lab54.lab54c3 "
                + "FROM     Lab21 "
                + "         INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 "
                + "         INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 "
                + "         LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 "
                + "         LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 "
                + "WHERE    1 = 1 "
                + (encryptedLastName != null ? " AND lab21.lab21c5 = ? " : "")
                + (encryptedSurName != null ? " AND lab21.lab21c6 = ? " : "")
                + (encryptedName1 != null ? " AND lab21.lab21c3 = ? " : "")
                + (encryptedName2 != null ? " AND lab21.lab21c4 = ? " : "")
                + (sex != null ? " AND lab21.lab80c1 = ? " : "")
                + (birthday != null ? " AND lab21.lab21c7 = ? " : "");
        List parameters = new ArrayList(0);
        if (encryptedLastName != null)
        {
            parameters.add(encryptedLastName);
        }
        if (encryptedSurName != null)
        {
            parameters.add(encryptedSurName);
        }
        if (encryptedName1 != null)
        {
            parameters.add(encryptedName1);
        }
        if (encryptedName2 != null)
        {
            parameters.add(encryptedName2);
        }
        if (sex != null)
        {
            parameters.add(sex);
        }
        if (birthday != null)
        {
            parameters.add(new Timestamp(birthday.getTime()));
        }
        return getJdbcTemplate().query(select,
                parameters.toArray(),
                (ResultSet rs, int i) ->
        {
            Patient patient = new Patient();
            patient.setId(rs.getInt("lab21c1"));
            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
            patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));

            Item item = new Item();
            item.setId(rs.getInt("lab80c1"));
            item.setCode(rs.getString("lab80c3"));
            item.setEsCo(rs.getString("lab80c4"));
            item.setEnUsa(rs.getString("lab80c5"));
            patient.setSex(item);

            patient.setBirthday(rs.getTimestamp("lab21c7"));
            patient.setEmail(rs.getString("lab21c8"));
            patient.setSize(rs.getBigDecimal("lab21c9"));
            patient.setWeight(rs.getBigDecimal("lab21c10"));
            patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));
            patient.setDateOfDeath(rs.getTimestamp("lab21c11"));
            patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));

            User user = new User();
            user.setId(rs.getInt("lab04c1"));
            user.setName(rs.getString("lab04c2"));
            user.setLastName(rs.getString("lab04c3"));
            user.setUserName(rs.getString("lab04c4"));
            patient.setLastUpdateUser(user);

            if (rs.getString("lab08c1") != null)
            {
                Race race = new Race();
                race.setId(rs.getInt("lab08c1"));
                race.setName(rs.getString("lab08c2"));
                patient.setRace(race);
            }

            if (rs.getString("lab54c1") != null)
            {
                DocumentType documentType = new DocumentType();
                documentType.setId(rs.getInt("lab54c1"));
                documentType.setAbbr(rs.getString("lab54c2"));
                documentType.setName(rs.getString("lab54c3"));
                patient.setDocumentType(documentType);
            }
            return patient;
        });
    }

    /**
     * Inserta un nuevo registro de historico del paciente
     *
     * @param historicalResult
     * @return Paciente actualizado
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en base de datos
     */
    default HistoricalResult createPatientHistory(HistoricalResult historicalResult) throws Exception
    {
        try
        {

            historicalResult.setLastResultDate(new Timestamp(new Date().getTime()));
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab17");
            HashMap<String, Object> parameters = new HashMap<>(0);
            parameters.put("lab21c1", historicalResult.getPatientId());
            parameters.put("lab39c1", historicalResult.getTestId());
            parameters.put("lab17c1", null);
            parameters.put("lab17c2", null);
            parameters.put("lab04c1_1", null);
            parameters.put("lab17c3", null);
            parameters.put("lab17c4", null);
            parameters.put("lab04c1_2", null);
            parameters.put("lab17c5", Tools.encrypt(historicalResult.getSecondLastResultTemp()));
            parameters.put("lab17c6", historicalResult.getSecondLastResultDateTemp());
            parameters.put("lab04c1_3", historicalResult.getSecondLastResultUserTemp().getId());
            insert.execute(parameters);
            return historicalResult;
        } catch (Exception e)
        {
            ResultsLog.info("ERROR HISTORICAL PATIENT: " + e);
            return null;
        }
    }

    /**
     * Actualizar un registro de historico del paciente
     *
     * @param historicalResult
     * @return Paciente actualizado
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en base de datos
     */
    default HistoricalResult updatePatientHistory(HistoricalResult historicalResult) throws Exception
    {
        try
        {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE lab17 SET");
            sql.append(" lab17c1 = ?");
            sql.append(", lab17c2 = ?");
            sql.append(", lab04c1_1 = ?");
            sql.append(", lab17c3 = ?");
            sql.append(", lab17c4 = ?");
            sql.append(", lab04c1_2 = ?");
            sql.append(", lab17c5 = ?");
            sql.append(", lab17c6 = ?");
            sql.append(", lab04c1_3 = ?");
            sql.append(" WHERE lab21c1 = ?");
            sql.append(" AND lab39c1 = ?");
            Object[] args = new Object[11];

            /*if (historicalResult.getLastResultDate() != null)
            {
                historicalResult.setLastResultDate(new Timestamp(new Date().getTime()));
            }*/
            args[0] = Tools.encrypt(historicalResult.getLastResult());
            args[1] = historicalResult.getLastResultDate();
            args[2] = historicalResult.getLastResultUser() == null ? null : historicalResult.getLastResultUser().getId();
            args[3] = Tools.encrypt(historicalResult.getSecondLastResult());
            args[4] = historicalResult.getSecondLastResultDate();
            args[5] = historicalResult.getSecondLastResultUser() == null ? null : historicalResult.getSecondLastResultUser().getId();
            args[6] = Tools.encrypt(historicalResult.getSecondLastResultTemp());
            args[7] = historicalResult.getSecondLastResultDateTemp();
            args[8] = historicalResult.getSecondLastResultUserTemp() == null ? null : historicalResult.getSecondLastResultUserTemp().getId();
            args[9] = historicalResult.getPatientId();
            args[10] = historicalResult.getTestId();
            getJdbcTemplate().update(sql.toString(), args);
            return historicalResult;
        } catch (DataAccessException e)
        {
            ResultsLog.error(e);
            return null;
        }
    }

    /**
     * Obtiene la cantidad total de pacientes
     *
     * @return Cantidad de pacientes
     * @throws Exception Error en el servicio
     */
    default Integer numberPatients() throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append(ISOLATION_READ_UNCOMMITTED);
        sql.append("SELECT COUNT(lab21c1) AS numb FROM lab21");
        return getJdbcTemplate().queryForObject(sql.toString(),
                (ResultSet rs, int i) ->
        {
            return rs.getInt("numb");
        });
    }

    /**
     * Obtiene la lista de pacientes por pagina
     *
     * @param patientReport
     * @return Lista de pacientes
     * @throws Exception Error en el servicio
     */
    public List<Patient> listPatientsByPag(PatientReport patientReport) throws Exception;

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param documentType
     * @param history
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    default Patient getBasicPatientInformation(int documentType, String history) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SET NOCOUNT ON ");
            query.append("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED ");
            query.append("SELECT lab21c2, ")
                    .append("lab21c3, ")
                    .append("lab21c4, ")
                    .append("lab21c5, ")
                    .append("lab21c6, ")
                    .append("lab21c7, ")
                    .append("lab21c22, ")
                    .append("lab54.lab54c1 AS documentId, ")
                    .append("lab54c2, ")
                    .append("lab54c3, ")
                    .append("lab54.lab07c1 AS documentState, ")
                    .append("lab80.lab80c1 AS genderId, ")
                    .append("lab80c2, ")
                    .append("lab80c3, ")
                    .append("lab80c4, ")
                    .append("lab80c5, ")
                    .append("lab80c6 ")
                    .append("FROM lab21 ")
                    .append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ")
                    .append("WHERE lab21.lab21c2 = '").append(Tools.encrypt(history)).append("'")
                    .append(" AND lab54.lab54c1 = ").append(documentType);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                Patient patient = new Patient();
                DocumentType document = new DocumentType();
                Item item = new Item();

                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                patient.setBirthday(rs.getTimestamp("lab21c7"));
                patient.setStatus(rs.getInt("lab21c22"));

                // Tipo de documento
                document.setId(rs.getInt("documentId"));
                document.setAbbr(rs.getString("lab54c2"));
                document.setName(rs.getString("lab54c3"));
                document.setState(rs.getBoolean("documentState"));
                patient.setDocumentType(document);
                // Genero del paciente
                item.setId(rs.getInt("genderId"));
                item.setIdParent(rs.getInt("lab80c2"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setAdditional(rs.getString("lab80c6") == null ? "" : rs.getString("lab80c6"));
                patient.setSex(item);
                return patient;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param initialDate
     * @param endDate
     * @param patientStatus
     * @param filterType
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    default List<Patient> getBasicPatientInformationByDate(Integer initialDate, Integer endDate, int patientStatus, int filterType) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            //BANNER - 2020-12-02, para validación de pacientes duplicados contra el tribunal.
            if (patientStatus == -200)
            {
                query.append("SELECT DISTINCT TOP 500 lab21.lab21c1 AS idPatient, ")
                        .append("lab21c2, ")
                        .append("lab21c3, ")
                        .append("lab21c4, ")
                        .append("lab21c5, ")
                        .append("lab21c6, ")
                        .append("lab21c7, ")
                        .append("lab21c22, ")
                        .append("lab54.lab54c1 AS documentId, ")
                        .append("lab54c2, ")
                        .append("lab54c3, ")
                        .append("lab54.lab07c1 AS documentState, ")
                        .append("lab80.lab80c1 AS genderId, ")
                        .append("lab80c2, ")
                        .append("lab80c3, ")
                        .append("lab80c4, ")
                        .append("lab80c5, ")
                        .append("lab80c6, ")
                        .append("0 AS idOrden ")
                        .append("FROM lab21 ")
                        .append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                        .append("JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ")
                        .append("WHERE lab21c22 = ").append(patientStatus);
            } else
            {
                query.append("SELECT DISTINCT TOP 500 lab21.lab21c1 AS idPatient, ")
                        .append("lab21c2, ")
                        .append("lab21c3, ")
                        .append("lab21c4, ")
                        .append("lab21c5, ")
                        .append("lab21c6, ")
                        .append("lab21c7, ")
                        .append("lab21c22, ")
                        .append("lab54.lab54c1 AS documentId, ")
                        .append("lab54c2, ")
                        .append("lab54c3, ")
                        .append("lab54.lab07c1 AS documentState, ")
                        .append("lab80.lab80c1 AS genderId, ")
                        .append("lab80c2, ")
                        .append("lab80c3, ")
                        .append("lab80c4, ")
                        .append("lab80c5, ")
                        .append("lab80c6, ")
                        .append("lab63c3, ")
                        .append("MAX(lab22.lab22c1) as idOrden ")
                        .append("FROM lab22 ")
                        .append("JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ")
                        .append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                        .append("JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1 ")
                        .append("LEFT JOIN lab63 ON lab63.lab63c1 = lab22.lab_demo_9 ");

                if (filterType == 0)
                {
                    query.append("WHERE lab22c2 BETWEEN ").append(initialDate)
                            .append(" AND ").append(endDate)
                            .append(" AND lab21c22 = ").append(patientStatus);
                } else
                {
                    String rangeOne = String.valueOf(initialDate) + " 00:00:00";
                    String rangeTwo = String.valueOf(endDate) + " 23:59:59";

                    query.append("WHERE lab21c23 BETWEEN '").append(rangeOne).append("'")
                            .append(" AND '").append(rangeTwo).append("'")
                            .append(" AND lab21c22 = ").append(patientStatus);
                }

                query.append(" group by lab21.lab21c1,lab21c2,lab21c3,  lab21c4, lab21c5, lab21c6, lab21c7, "
                        + "lab21c22, lab54.lab54c1 , lab54c2, lab54c3, lab54.lab07c1 , lab80.lab80c1, "
                        + "lab80c2, lab80c3, lab80c4, lab80c5, lab80c6, lab63c3 ");
            }

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                try
                {
                    Patient patient = new Patient();
                    DocumentType document = new DocumentType();
                    Item item = new Item();
                    Branch branch = new Branch();

                    patient.setId(rs.getInt("idPatient"));
                    patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                    patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                    patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    patient.setBirthday(rs.getTimestamp("lab21c7"));
                    patient.setStatus(rs.getInt("lab21c22"));
                    patient.setOrderNumber(rs.getLong("idOrden"));
                    patient.setDataTribunal(getCourtData(patient.getId()));
                    patient.setSampleprovenance(rs.getString("lab63c3"));

                    // Tipo de documento
                    document.setId(rs.getInt("documentId"));
                    document.setAbbr(rs.getString("lab54c2"));
                    document.setName(rs.getString("lab54c3"));
                    document.setState(rs.getBoolean("documentState"));
                    patient.setDocumentType(document);
                    // Genero del paciente
                    item.setId(rs.getInt("genderId"));
                    item.setIdParent(rs.getInt("lab80c2"));
                    item.setCode(rs.getString("lab80c3"));
                    item.setEsCo(rs.getString("lab80c4"));
                    item.setAdditional(rs.getString("lab80c6") == null ? "" : rs.getString("lab80c6"));
                    patient.setSex(item);
                    // Sede de donde fue registrada dicha orden
                    branch = getBranchByOrder(patient.getOrderNumber());
                    patient.setBranch(branch);
                    return patient;
                } catch (Exception ex)
                {
                    return null;
                }
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param patient
     * @return Paciente
     * @throws Exception Error en el servicio TE_VALIDATION_NONE = "0"
     * TE_VALIDATION_SUCCESS = "1" TE_VALIDATION_ERROR = "2";
     * TE_VALIDATION_NOREQUIRED = "3" TE_VALIDATION_PASSPORT_NOT_FOUND = "4"
     * TE_VALIDATION_DUPLICATE_PATIENT = "5" TE_VALIDATION_ALL_PATIENTS = "-200"
     */
    default int updateBasicPatientInformation(Patient patient) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        try
        {

            return getJdbcTemplate().update("UPDATE lab21 set lab21c3 = ?, "
                    + "lab21c4 = ?, "
                    + "lab21c5 = ?, "
                    + "lab21c6 = ?, "
                    + "lab21c7 = ?, "
                    + "lab21c22 = ?, "
                    + "lab21c23 = ?, "
                    + "lab54c1 = ?, "
                    + "lab80c1 = ?, "
                    + " lab21c25 = 0 "
                    + "WHERE lab21c1 = ? ",
                    patient.getName1(),
                    patient.getName2(),
                    patient.getLastName(),
                    patient.getSurName(),
                    patient.getBirthday(),
                    patient.getStatus(),
                    timestamp,
                    patient.getDocumentType().getId(),
                    patient.getSex().getId(),
                    patient.getId());
        } catch (DataAccessException ex)
        {
            //BANNER 2020-12-21 Se actualiza estado del paciente cuando se genera error en la actualización con los datos del tribunal.
            ex.printStackTrace();
            if (ex instanceof DuplicateKeyException)
            {
                try
                {
                    return getJdbcTemplate().update("UPDATE lab21 SET "
                            + "lab21c22 = ?, lab21c23 = ?, lab21c25 = 0 "
                            + "WHERE lab21c1 = ? ",
                            5,
                            timestamp,
                            patient.getId());
                } catch (Exception ex1)
                {
                    ex1.printStackTrace();
                    return -1;
                }
            } else
            {
                return -1;
            }
        }
    }

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param patient
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    default int updateStatePatient(Patient patient) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            return getJdbcTemplate().update("UPDATE lab21 set "
                    + "lab21c22 = ?, lab21c25 = 0 "
                    + "WHERE lab21c1 = ? ",
                    patient.getStatus(),
                    patient.getId());
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param patient
     * @return Registros insertados
     * @throws Exception Error en el servicio
     */
    default int insertCourtData(Patient patient) throws Exception
    {
        try
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab193");
            HashMap<String, Object> parameters = new HashMap<>(0);
            parameters.put("lab21c1", patient.getId());
            parameters.put("lab193c1", patient.getDataTribunal());
            return insert.execute(parameters);
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Obtiene la sede de una orden
     *
     * @param orderId
     * @return Sede asociada a esa orden
     * @throws Exception Error en el servicio
     */
    default Branch getBranchByOrder(long orderId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab05.lab05c1 AS lab05c1,")
                    .append("lab05c4, ")
                    .append("lab05c12 ")
                    .append("FROM lab22 ")
                    .append("JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                    .append("WHERE lab22.lab22c1 = ").append(orderId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setName(rs.getString("lab05c4"));
                branch.setEmail(rs.getString("lab05c12"));
                return branch;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Consulta si dicho paciente ya tiene datos del tribunal asociados
     *
     * @param idPatient
     * @return True - si ya exiten datos, False - si no es asi
     * @throws Exception Error en el servicio
     */
    default boolean verifyExistenceCourtData(int idPatient) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT 1 AS Exist ")
                    .append("FROM lab193 ")
                    .append("WHERE lab21c1 = ").append(idPatient);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                Integer exist = rs.getInt("Exist");
                return exist > 0;
            });
        } catch (EmptyResultDataAccessException e)
        {
            return false;
        }
    }
    
        /**
     * Consulta si dicho paciente ya tiene datos del tribunal asociados
     *
     * @param idPatient
     * @return True - si ya exiten datos, False - si no es asi
     * @throws Exception Error en el servicio
     */
    default int getIndicatorPasswordPatient(int idPatient) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab21c24 ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21c1 = ").append(idPatient);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab21c24");
            });
        } catch (EmptyResultDataAccessException e)
        {
            return 0;
        }
    }

    /**
     * Obtenemos los datos del tribunal de ese paciente
     *
     * @param patient
     * @return Registros afectados
     * @throws Exception Error en el servicio
     */
    default int updateCourtData(Patient patient) throws Exception
    {
        try
        {
            return getJdbcTemplate().update("UPDATE lab193 "
                    + "SET lab193c1 = ? "
                    + "WHERE lab21c1 = ?",
                    patient.getDataTribunal(),
                    patient.getId());
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Actualiza los datos del tribunal asociados a un paciente
     *
     * @param idPatient
     * @return Registros afectados
     * @throws Exception Error en el servicio
     */
    default String getCourtData(int idPatient) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab193c1 ")
                    .append("FROM lab193 ")
                    .append("WHERE lab21c1 = ").append(idPatient);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getString("lab193c1");
            });
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtiene el id de la ultima orden a la cual un paciente fue sometido
     *
     * @param idPatient
     * @return Id de la orden
     * @throws Exception Error en el servicio
     */
    default long lastOrderPatient(int idPatient) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT max(lab22)")
                    .append("FROM lab22 ")
                    .append("WHERE lab21.lab21c1 = ").append(idPatient)
                    .append(" AND (lab22c19 = 0 or lab22c19 is null) ");

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getLong("idOrden");
            });
        } catch (Exception e)
        {
            return 0;
        }
    }
}
