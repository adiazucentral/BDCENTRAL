package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las Tarifas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 13/06/2017
 * @see Creación
 */
public interface RateDao
{

    /**
     * Lista las tarifas desde la base de datos.
     *
     * @return Lista de sitios anatomicos.
     * @throws Exception Error en la base de datos.
     */
    default List<Rate> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab904c1, lab904c2, lab904c3, lab904c4, lab904c5, lab904c6, lab904c7, lab904c8, lab904c9, lab904c10, lab904c11, lab904c12, lab904c13, lab904c15, lab904c16, lab904c17, lab904c18, lab904c19, lab904c20, lab904c21, lab904c22, lab904c23, lab904c24, lab904c25, lab904c26, lab904c27, lab904c28, lab904c29, lab904c30, lab904c31, lab904c32, lab904c33, lab904c34, lab904c35, lab904c36, lab904c37, lab904.lab04c1, lab04c2, lab04c3, lab04c4, lab904.lab07c1, lab905c1, lab904c38 "
                    + "FROM lab904 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab904.lab04c1", (ResultSet rs, int i) ->
            {
                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode(rs.getString("lab904c2"));
                rate.setName(rs.getString("lab904c3"));
                rate.setAddress(rs.getString("lab904c4"));
                rate.setAddAddress(rs.getString("lab904c5"));
                rate.setCity(rs.getString("lab904c6"));
                rate.setDepartment(rs.getString("lab904c7"));
                rate.setPhone(rs.getString("lab904c8"));
                rate.setEmail(rs.getString("lab904c9"));
                rate.setPostalCode(rs.getString("lab904c10"));
                rate.setWebPage(rs.getString("lab904c11"));
                rate.setShowPriceInEntry(rs.getInt("lab904c12") == 1);
                rate.setCheckPaid(rs.getInt("lab904c13") == 1);
                rate.setTypePayer(rs.getInt("lab904c15"));
                rate.setAssingAllAccounts(rs.getInt("lab904c16") == 1);
                rate.setApplyDiagnostics(rs.getInt("lab904c17") == 1);
                rate.setCheckCPTRelation(rs.getInt("lab904c18") == 1);
                rate.setHomebound(rs.getInt("lab904c19") == 1);
                rate.setVenipunture(rs.getInt("lab904c20") == 1);
                rate.setApplyTypePayer(rs.getInt("lab904c21") == 1);
                rate.setClaimCode(rs.getString("lab904c22"));
                rate.setEligibility(rs.getInt("lab904c23") == 1);
                rate.setInterchangeSender(rs.getString("lab904c24"));
                rate.setInterchangeQualifier(rs.getString("lab904c25"));
                rate.setApplicationSendCode(rs.getString("lab904c26"));
                rate.setLabSubmitter(rs.getString("lab904c27"));
                rate.setIdentificationPayer(rs.getString("lab904c28"));
                rate.setFormatMemberId(rs.getString("lab904c29"));
                rate.setReceiver(rs.getInt("lab905c1"));
                rate.setConsecutive(rs.getString("lab904c30"));
                rate.setOutputFileName(rs.getString("lab904c31"));
                rate.setClaimType(rs.getInt("lab904c32"));
                rate.setTransactionType(rs.getInt("lab904c33"));
                rate.setSupplierSignature(rs.getInt("lab904c34") == 1);
                rate.setAssingBenefits(rs.getInt("lab904c35") == 1);
                rate.setElectronicClaim(rs.getInt("lab904c36") == 1);
                rate.setObservation(rs.getString("lab904c38"));

                /*Usuario*/
                rate.getUser().setId(rs.getInt("lab04c1"));
                rate.getUser().setName(rs.getString("lab04c2"));
                rate.getUser().setLastName(rs.getString("lab04c3"));
                rate.getUser().setUserName(rs.getString("lab04c4"));

                rate.setLastTransaction(rs.getTimestamp("lab904c37"));
                rate.setState(rs.getInt("lab07c1") == 1);

                return rate;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva tarifa en la base de datos.
     *
     * @param rate Instancia con los datos de la tarifa.
     *
     * @return Instancia con los datos de la tarifa.
     * @throws Exception Error en la base de datos.
     */
    default Rate create(Rate rate) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab904")
                .usingGeneratedKeyColumns("lab904c1");

        HashMap parameters = new HashMap();
        parameters.put("lab904c2", rate.getCode());
        parameters.put("lab904c3", rate.getName());
        parameters.put("lab904c4", rate.getAddress());
        parameters.put("lab904c5", rate.getAddAddress());
        parameters.put("lab904c6", rate.getCity());
        parameters.put("lab904c7", rate.getDepartment());
        parameters.put("lab904c8", rate.getPhone());
        parameters.put("lab904c9", rate.getEmail());
        parameters.put("lab904c10", rate.getPostalCode());
        parameters.put("lab904c11", rate.getWebPage());
        parameters.put("lab904c12", rate.isShowPriceInEntry() ? 1 : 0);
        parameters.put("lab904c13", rate.isCheckPaid() ? 1 : 0);
        parameters.put("lab904c15", rate.getTypePayer());
        parameters.put("lab904c16", rate.isAssingAllAccounts() ? 1 : 0);
        parameters.put("lab904c17", rate.isApplyDiagnostics() ? 1 : 0);
        parameters.put("lab904c18", rate.isCheckCPTRelation() ? 1 : 0);
        parameters.put("lab904c19", rate.isHomebound() ? 1 : 0);
        parameters.put("lab904c20", rate.isVenipunture() ? 1 : 0);
        parameters.put("lab904c21", rate.isApplyTypePayer() ? 1 : 0);
        parameters.put("lab904c22", rate.getClaimCode());
        parameters.put("lab904c23", rate.isEligibility() ? 1 : 0);
        parameters.put("lab904c24", rate.getInterchangeSender());
        parameters.put("lab904c25", rate.getInterchangeQualifier());
        parameters.put("lab904c26", rate.getApplicationSendCode());
        parameters.put("lab904c27", rate.getLabSubmitter());
        parameters.put("lab904c28", rate.getIdentificationPayer());
        parameters.put("lab904c29", rate.getFormatMemberId());
        parameters.put("lab905c1", rate.getReceiver());
        parameters.put("lab904c30", rate.getConsecutive());
        parameters.put("lab904c31", rate.getOutputFileName());
        parameters.put("lab904c32", rate.getClaimType());
        parameters.put("lab904c33", rate.getTransactionType());
        parameters.put("lab904c34", rate.isSupplierSignature() ? 1 : 0);
        parameters.put("lab904c35", rate.isAssingBenefits() ? 1 : 0);
        parameters.put("lab904c36", rate.isElectronicClaim() ? 1 : 0);
        parameters.put("lab904c37", timestamp);
        parameters.put("lab904c38", rate.getObservation());
        parameters.put("lab04c1", rate.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        rate.setId(key.intValue());
        rate.setLastTransaction(timestamp);

        return rate;
    }

    /**
     * Obtener información de una tarifa por una campo especifico.
     *
     * @param id ID de la tarifa a ser consultada.
     * @param code Descripcion de la tarifa a ser consultada.
     * @param name Nombre de la tarifaa ser consultada.
     *
     * @return Instancia con los datos de la tarifa.
     * @throws Exception Error en la base de datos.
     */
    default Rate get(Integer id, String code, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab905c2, lab904c1, lab904c2, lab904c3, lab904c4, lab904c5, lab904c6, lab904c7, lab904c8, lab904c9, lab904c10, lab904c11, lab904c12, lab904c13, lab904c15, lab904c16, lab904c17, lab904c18, lab904c19, lab904c20, lab904c21, lab904c22, lab904c23, lab904c24, lab904c25, lab904c26, lab904c27, lab904c28, lab904c29, lab904c30, lab904c31, lab904c32, lab904c33, lab904c34, lab904c35, lab904c36, lab904c37, lab904.lab04c1, lab04c2, lab04c3, lab04c4, lab904.lab07c1, lab904.lab905c1, lab904c38 "
                    + "FROM lab904 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab904.lab04c1 "
                    + "LEFT JOIN lab905 ON lab905.lab905c1 = lab904.lab905c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab904c1 = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(lab904c2) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab904c3) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (code != null)
            {
                object = code.toUpperCase();
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode(rs.getString("lab904c2"));
                rate.setName(rs.getString("lab904c3"));
                rate.setAddress(rs.getString("lab904c4"));
                rate.setAddAddress(rs.getString("lab904c5"));
                rate.setCity(rs.getString("lab904c6"));
                rate.setDepartment(rs.getString("lab904c7"));
                rate.setPhone(rs.getString("lab904c8"));
                rate.setEmail(rs.getString("lab904c9"));
                rate.setPostalCode(rs.getString("lab904c10"));
                rate.setWebPage(rs.getString("lab904c11"));
                rate.setShowPriceInEntry(rs.getInt("lab904c12") == 1);
                rate.setCheckPaid(rs.getInt("lab904c13") == 1);
                rate.setTypePayer(rs.getInt("lab904c15"));
                rate.setAssingAllAccounts(rs.getInt("lab904c16") == 1);
                rate.setApplyDiagnostics(rs.getInt("lab904c17") == 1);
                rate.setCheckCPTRelation(rs.getInt("lab904c18") == 1);
                rate.setHomebound(rs.getInt("lab904c19") == 1);
                rate.setVenipunture(rs.getInt("lab904c20") == 1);
                rate.setApplyTypePayer(rs.getInt("lab904c21") == 1);
                rate.setClaimCode(rs.getString("lab904c22"));
                rate.setEligibility(rs.getInt("lab904c23") == 1);
                rate.setInterchangeSender(rs.getString("lab904c24"));
                rate.setInterchangeQualifier(rs.getString("lab904c25"));
                rate.setApplicationSendCode(rs.getString("lab904c26"));
                rate.setLabSubmitter(rs.getString("lab904c27"));
                rate.setIdentificationPayer(rs.getString("lab904c28"));
                rate.setFormatMemberId(rs.getString("lab904c29"));
                if (rs.getString("lab905c1") != null)
                {
                    rate.setReceiver(rs.getInt("lab905c1"));
                    rate.setNameReceiver(rs.getString("lab905c2"));
                }
                rate.setConsecutive(rs.getString("lab904c30"));
                rate.setOutputFileName(rs.getString("lab904c31"));
                rate.setClaimType(rs.getInt("lab904c32"));
                rate.setTransactionType(rs.getInt("lab904c33"));
                rate.setSupplierSignature(rs.getInt("lab904c34") == 1);
                rate.setAssingBenefits(rs.getInt("lab904c35") == 1);
                rate.setElectronicClaim(rs.getInt("lab904c36") == 1);
                rate.setObservation(rs.getString("lab904c38"));

                /*Usuario*/
                rate.getUser().setId(rs.getInt("lab04c1"));
                rate.getUser().setName(rs.getString("lab04c2"));
                rate.getUser().setLastName(rs.getString("lab04c3"));
                rate.getUser().setUserName(rs.getString("lab04c4"));

                rate.setLastTransaction(rs.getTimestamp("lab904c37"));
                rate.setState(rs.getInt("lab07c1") == 1);

                return rate;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una tarifa en la base de datos.
     *
     * @param rate Instancia con los datos de la tarifa.
     *
     * @return Objeto de la tarifa modificada.
     * @throws Exception Error en la base de datos.
     */
    default Rate update(Rate rate) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab904 SET lab904c2 = ?, lab904c3 = ?, lab904c4 = ?, lab904c5 = ?, lab904c6 = ?, lab904c7 = ?, lab904c8 = ?, lab904c9 = ?, lab904c10 = ?, lab904c11 = ?, lab904c12 = ?, lab904c13 = ?, lab904c15 = ?, lab904c16 = ?, lab904c17 = ?, lab904c18 = ?, lab904c19 = ?, lab904c20 = ?, lab904c21 = ?, lab904c22 = ?, lab904c23 = ?, lab904c24 = ?, lab904c25 = ?, lab904c26 = ?, lab904c27 = ?, lab904c28 = ?, lab904c29 = ?, lab905c1 = ?, lab904c30 = ?, lab904c31 = ?, lab904c32 = ?, lab904c33 = ?, lab904c34 = ?, lab904c35 = ?, lab904c36 = ?, lab904c37 = ?, lab04c1 = ?, lab07c1 = ?, lab904c38 = ? "
                + "WHERE lab904c1 = ?",
                rate.getCode(), rate.getName(), rate.getAddress(), rate.getAddAddress(), rate.getCity(), rate.getDepartment(), rate.getPhone(), rate.getEmail(), rate.getPostalCode(), rate.getWebPage(), rate.isShowPriceInEntry() ? 1 : 0, rate.isCheckPaid() ? 1 : 0, rate.getTypePayer(), rate.isAssingAllAccounts() ? 1 : 0, rate.isApplyDiagnostics() ? 1 : 0, rate.isCheckCPTRelation() ? 1 : 0, rate.isHomebound() ? 1 : 0, rate.isVenipunture() ? 1 : 0, rate.isApplyTypePayer() ? 1 : 0, rate.getClaimCode(), rate.isEligibility() ? 1 : 0, rate.getInterchangeSender(), rate.getInterchangeQualifier(), rate.getApplicationSendCode(), rate.getLabSubmitter(), rate.getIdentificationPayer(), rate.getFormatMemberId(), rate.getReceiver(), rate.getConsecutive(), rate.getOutputFileName(), rate.getClaimType(), rate.getTransactionType(), rate.isSupplierSignature() ? 1 : 0, rate.isAssingBenefits() ? 1 : 0, rate.isElectronicClaim() ? 1 : 0, timestamp, rate.getUser().getId(), rate.isState() ? 1 : 0, rate.getObservation(), rate.getId());

        rate.setLastTransaction(timestamp);

        return rate;
    }

    /**
     * Obtener el nombre de una tarifa por su id
     *
     * @param id ID de la tarifa a ser consultada.
     *
     * @return Nombre de la tarifa
     * @throws Exception Error en la base de datos.
     */
    default String getName(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab904c3")
                    .append(" FROM lab904 ")
                    .append("WHERE lab904c1 = ").append(id);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getString("lab904c3");
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
