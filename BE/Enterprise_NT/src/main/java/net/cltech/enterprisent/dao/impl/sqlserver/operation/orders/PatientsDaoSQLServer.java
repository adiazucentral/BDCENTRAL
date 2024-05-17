/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.orders;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.PatientsDao;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.reports.PatientReport;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos para pacientes en SQLServer
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creacion
 */
@Repository
public class PatientsDaoSQLServer implements PatientsDao
{

    private JdbcTemplate jdbc;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ToolsDao toolsDao;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

    @Override
    public CommentDao getCommentDao()
    {
        return commentDao;
    }

    @Override
    public List<Patient> listPatientsByPag(PatientReport patientReport) throws Exception
    {
        final StringBuilder sql = new StringBuilder();
        sql.append(ISOLATION_READ_UNCOMMITTED);
        sql.append("SELECT lab21c1 ");
        sql.append(" , lab21c2 ");
        sql.append(" , lab21c3 ");
        sql.append(" , lab21c4 ");
        sql.append(" , lab21c5 ");
        sql.append(" , lab21c6 ");
        sql.append(" , lab21.lab80c1 ");
        sql.append(" , lab80c3 ");
        sql.append(" , lab80c4 ");
        sql.append(" , lab80c5 ");
        sql.append(" , lab21c7 ");
        sql.append(" , lab21c12 ");
        sql.append(" , lab21.lab04c1 ");
        sql.append(" , Lab04.lab04c2 ");
        sql.append(" , Lab04.lab04c3 ");
        sql.append(" , Lab04.lab04c4 ");
        sql.append(" , lab4.lab04c2 AS nameUser ");
        sql.append(" , lab4.lab04c3 AS lastUser ");
        sql.append(" , lab4.lab04c4 AS nickNameUser ");
        sql.append(" , lab54.lab54c1 ");
        sql.append(" , lab54.lab54c2 ");
        sql.append(" , lab54.lab54c3 ");
        sql.append(" , lab21c13 ");
        sql.append(" , lab21c16 ");
        sql.append(" , lab21c20 ");
        sql.append(" , lab04c1_2 ");
        final StringBuilder from = new StringBuilder();
        from.append(" FROM Lab21 ");
        from.append(" INNER JOIN  Lab80 ON Lab21.Lab80C1 = Lab80.Lab80C1 ");
        from.append(" INNER JOIN  Lab04 ON Lab21.Lab04C1 = Lab04.Lab04C1 ");
        from.append(" INNER JOIN  Lab04 AS lab4 ON Lab21.Lab04c1_2 = lab4.Lab04C1 ");
        from.append(" LEFT JOIN  Lab54 ON Lab21.Lab54C1 = Lab54.Lab54C1 ");
        if (patientReport.isEmail())
        {
            sql.append(" , lab21c8 ");
        }
        if (patientReport.isSize())
        {
            sql.append(" , lab21c9 ");
        }
        if (patientReport.isWeight())
        {
            sql.append(" , lab21c10 ");
        }
        if (patientReport.isDateOfDeath())
        {
            sql.append(" , lab21c11 ");
        }
        if (patientReport.isPhoto())
        {
            sql.append(" , lab21c14 ");
        }
        if (patientReport.isAddress())
        {
            sql.append(" , lab21c17 ");
        }
        if (patientReport.isRace())
        {
            sql.append(" , lab21.lab08c1 ");
            sql.append(" , lab08c2 ");
            sql.append(" , lab08c5 ");
            from.append(" LEFT JOIN  Lab08 ON Lab21.Lab08C1 = Lab08.Lab08C1 ");
        }

        if (!patientReport.getDemographicsQuery().isEmpty())
        {
            patientReport.getDemographicsQuery().stream().forEach((Demographic d) ->
            {
                if (d.isEncoded())
                {
                    sql.append(", demo").append(d.getId()).append(".lab63c1 as demo").append(d.getId()).append("_id");
                    sql.append(", demo").append(d.getId()).append(".lab63c2 as demo").append(d.getId()).append("_code");
                    sql.append(", demo").append(d.getId()).append(".lab63c3 as demo").append(d.getId()).append("_name");

                    from.append(" LEFT JOIN Lab63 demo").append(d.getId()).append(" ON Lab21.lab_demo_").append(d.getId()).append(" = demo").append(+d.getId()).append(".lab63c1 ");
                } else
                {
                    sql.append(", Lab21.lab_demo_").append(d.getId()).append(" ");
                }
            });
        }

        StringBuilder where = new StringBuilder();
        where.append("WHERE 1=1  ");
        List parameters = new ArrayList(0);
        if (!patientReport.getDemographics().isEmpty())
        {
            //--------Filtro por demograficos
            if (patientReport.getDemographics() != null)
            {
                patientReport.getDemographics().forEach((demographic) ->
                {
                    where.append(SQLTools.buildSQLDemographicFilter(demographic, parameters));
                });
            }

        }

        where.append(" ORDER BY lab21c1 ");

        // parameters.add(patientReport.getSizePage());
        //parameters.add(((patientReport.getPage() - 1) < 0 ? 1 : (patientReport.getPage() - 1)) * patientReport.getSizePage()); // Valor desde el cual se comienza pagina anterior * tamño de la pagina = registro siguiente
        return getJdbcTemplate().query(sql.toString() + from.toString() + where.toString(), parameters.toArray(), (ResultSet rs, int i) ->
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
            patient.setEmail(!patientReport.isEmail() ? null : rs.getString("lab21c8"));
            patient.setSize(!patientReport.isSize() ? null : rs.getBigDecimal("lab21c9"));
            patient.setWeight(!patientReport.isWeight() ? null : rs.getBigDecimal("lab21c10"));
            //  patient.setDiagnostic(getCommentDao().listCommentOrder(null, patient.getId()));
            patient.setDateOfDeath(!patientReport.isDateOfDeath() ? null : rs.getTimestamp("lab21c11"));
            patient.setLastUpdateDate(rs.getTimestamp("lab21c12"));
            patient.setFirstDate(rs.getTimestamp("lab21c20"));
            patient.setPhoto(!patientReport.isPhoto() ? null : rs.getString("lab21c14"));
            patient.setPhone(rs.getString("lab21c16"));
            patient.setAddress(!patientReport.isAddress() ? null : rs.getString("lab21c17"));

            User user = new User();
            user.setId(rs.getInt("lab04c1"));
            user.setName(rs.getString("lab04c2"));
            user.setLastName(rs.getString("lab04c3"));
            user.setUserName(rs.getString("lab04c4"));
            patient.setLastUpdateUser(user);

            user = new User();
            user.setId(rs.getInt("lab04c1_2"));
            user.setName(rs.getString("nameUser"));
            user.setLastName(rs.getString("lastUser"));
            user.setUserName(rs.getString("nickNameUser"));
            patient.setFirstUser(user);

            if (patientReport.isRace() == true && rs.getString("lab08c1") != null)
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

            for (Demographic demographic : patientReport.getDemographicsQuery())
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
    }

    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }

}
