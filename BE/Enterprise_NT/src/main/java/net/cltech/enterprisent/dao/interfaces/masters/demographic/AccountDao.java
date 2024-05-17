package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.domain.masters.billing.DiscountRate;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Clientes.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/05/2017
 * @see Creación
 */
public interface AccountDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista los clientes desde la base de datos.
     *
     * @return Lista de clientes.
     * @throws Exception Error en la base de datos.
     */
    default List<Account> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab14c1"
                    + ", lab14c2"
                    + ", lab14c3"
                    + ", lab14c4"
                    + ", lab14c5"
                    + ", lab14c6"
                    + ", lab14c11"
                    + ", lab14c12"
                    + ", lab14c13"
                    + ", lab14c14"
                    + ", lab14c15"
                    + ", lab14c16"
                    + ", lab14c17"
                    + ", lab14c18"
                    + ", lab14c19"
                    + ", lab14c20"
                    + ", lab14c21"
                    + ", lab14c22"
                    + ", lab14c23"
                    + ", lab14c24"
                    + ", lab14c26"
                    + ", lab14.lab07c1"
                    + ", lab14.lab04c1"
                    + ", lab04c2"
                    + ", lab04c3"
                    + ", lab04c4"
                    + ", lab14c28"
                    + ", lab14c29"
                    + ", lab14c30"
                    + ", lab14c31"
                    + ", lab14c32"
                    + ", lab118c1 "
                    + ", lab14c33"
                    + ", lab14c34"
                    + ", lab14c35"
                    + ", lab14c36"
                    + " FROM lab14 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab14.lab04c1", (ResultSet rs, int i) ->
            {
                Account account = new Account();
                account.setId(rs.getInt("lab14c1"));
                account.setNit(rs.getString("lab14c2"));
                account.setName(rs.getString("lab14c3"));
                account.setPhone(rs.getString("lab14c4"));
                account.setFax(rs.getString("lab14c5"));
                account.setResponsable(rs.getString("lab14c6"));
                account.setObservation(rs.getString("lab14c11"));
                account.setInstitutional(rs.getInt("lab14c12") == 1);
                account.setEpsCode(rs.getString("lab14c13"));
                account.setAddress(rs.getString("lab14c14"));
                account.setAdditionalAddress(rs.getString("lab14c15"));
                account.setPostalCode(rs.getString("lab14c16"));
                account.setCity(rs.getInt("lab14c17"));
                account.setFaxSend(rs.getInt("lab14c18") == 1);
                account.setPrint(rs.getInt("lab14c19") == 1);
                account.setConnectivityEMR(rs.getInt("lab14c20") == 1);
                account.setEmail(rs.getString("lab14c21"));
                account.setAutomaticEmail(rs.getInt("lab14c22") == 1);
                account.setSelfPay(rs.getInt("lab14c23") == 1);
                account.setUsername(rs.getString("lab14c24"));
                //account.setDepartment(rs.getString("lab14c28") == null ? 0 : rs.getInt("lab14c28"));

                String department = rs.getString("lab14c28");
                if (department != null && !department.equals(""))
                {
                    account.setColony(rs.getInt("lab14c28"));
                }

                String colony = rs.getString("lab14c29");
                if (colony != null && !colony.equals(""))
                {
                    account.setColony(rs.getInt("lab14c29"));
                }

                account.setNamePrint(rs.getString("lab14c30"));
                account.setSendEnd(rs.getInt("lab14c31") == 1);

                account.setLastTransaction(rs.getTimestamp("lab14c26"));

                /*Usuario*/
                account.getUser().setId(rs.getInt("lab04c1"));
                account.getUser().setName(rs.getString("lab04c2"));
                account.getUser().setLastName(rs.getString("lab04c3"));
                account.getUser().setUserName(rs.getString("lab04c4"));

                account.setState(rs.getInt("lab07c1") == 1);
                account.setEncryptionReportResult(rs.getBoolean("lab14c32"));
                account.setCentralSystem(rs.getInt("lab118c1"));

                try
                {
                    account.setTaxes(getTheCustomersTaxes(account.getId()));
                } catch (Exception ex)
                {
                    Logger.getLogger(AccountDao.class.getName()).log(Level.SEVERE, null, ex);
                }

                account.setInvoice(rs.getBoolean("lab14c33"));
                account.setAgreement(rs.getBoolean("lab14c34"));
                account.setUsoCfdi(rs.getString("lab14c35"));
                account.setUsoRegimenFiscal(rs.getString("lab14c36"));

                return account;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo cliente en la base de datos.
     *
     * @param account Instancia con los datos del cliente.
     *
     * @return Instancia con los datos del cliente.
     * @throws Exception Error en la base de datos.
     */
    default Account create(Account account) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab14")
                .usingGeneratedKeyColumns("lab14c1");

        HashMap parameters = new HashMap();
        parameters.put("lab14c2", account.getNit().trim());
        parameters.put("lab14c3", account.getName().trim());
        parameters.put("lab14c4", account.getPhone());
        parameters.put("lab14c5", account.getFax());
        parameters.put("lab14c6", account.getResponsable());
        parameters.put("lab14c11", account.getObservation());
        parameters.put("lab14c12", account.isInstitutional() ? 1 : 0);
        parameters.put("lab14c13", account.getEpsCode());
        parameters.put("lab14c14", account.getAddress());
        parameters.put("lab14c15", account.getAdditionalAddress());
        parameters.put("lab14c16", account.getPostalCode());
        parameters.put("lab14c17", account.getCity());
        parameters.put("lab14c18", account.isFaxSend() ? 1 : 0);
        parameters.put("lab14c19", account.isPrint() ? 1 : 0);
        parameters.put("lab14c20", account.isConnectivityEMR() ? 1 : 0);
        parameters.put("lab14c21", account.getEmail());
        parameters.put("lab14c22", account.isAutomaticEmail() ? 1 : 0);
        parameters.put("lab14c23", account.isSelfPay() ? 1 : 0);
        parameters.put("lab14c24", account.getEmail()); // username
        parameters.put("lab14c25", account.getPassword() == null ? null : Tools.encrypt(account.getPassword()));
        parameters.put("lab14c26", timestamp);
        parameters.put("lab14c28", account.getDepartment());
        parameters.put("lab14c29", account.getColony());
        parameters.put("lab14c30", account.getNamePrint());
        parameters.put("lab14c31", account.isSendEnd() ? 1 : 0);
        parameters.put("lab04c1", account.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab14c32", account.isEncryptionReportResult() ? 1 : 0);
        parameters.put("lab118c1", account.getCentralSystem());

        parameters.put("lab14c33", account.isInvoice() ? 1 : 0);
        parameters.put("lab14c34", account.isAgreement() ? 1 : 0);
        parameters.put("lab14c35", account.getUsoCfdi());
        parameters.put("lab14c36", account.getUsoRegimenFiscal());

        Number key = insert.executeAndReturnKey(parameters);
        account.setId(key.intValue());
        account.setLastTransaction(timestamp);

        // Adicion de un listado de impuestos a un cliente:
        if (account.getTaxes() != null && !account.getTaxes().isEmpty())
        {
            account.getTaxes().forEach(tax ->
            {
                tax.setCustomerId(account.getId());
            });
            addTaxesPerCustomer(account.getTaxes(), account.getId());
        }

        return account;
    }

    /**
     * Obtener información de un cliente por un campo especifico.
     *
     * @param id ID del cliente a ser consultado.
     * @param name Nombre del cliente a ser consultado.
     * @param nit Nit del cliente a ser consultado.
     * @param codeEps Codigo EPS del cliente a ser consultado.
     * @param username Usuario del cliente a ser consultado.
     *
     * @return Instancia con los datos del cliente.
     * @throws Exception Error en la base de datos.
     */
    default Account get(Integer id, String name, String nit, String codeEps, String username) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab14c1"
                    + ", lab14c2"
                    + ", lab14c3"
                    + ", lab14c4"
                    + ", lab14c5"
                    + ", lab14c6"
                    + ", lab14c11"
                    + ", lab14c12"
                    + ", lab14c13"
                    + ", lab14c14"
                    + ", lab14c15"
                    + ", lab14c16"
                    + ", lab14c17"
                    + ", lab14c18"
                    + ", lab14c19"
                    + ", lab14c20"
                    + ", lab14c21"
                    + ", lab14c22"
                    + ", lab14c23"
                    + ", lab14c24"
                    + ", lab14c26"
                    + ", lab14.lab07c1"
                    + ", lab14.lab04c1"
                    + ", lab04c2"
                    + ", lab04c3"
                    + ", lab04c4"
                    + ", lab14c28"
                    + ", lab14c29"
                    + ", lab14c30"
                    + ", lab14c31"
                    + ", lab14c32"
                    + ", lab118c1 "
                    + ", lab14c33"
                    + ", lab14c34"
                    + ", lab14c35"
                    + ", lab14c36"
                    + " FROM lab14 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab14.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab14c1 = ? ";
            }
            if (nit != null)
            {
                query = query + "WHERE UPPER(lab14c2) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab14c3) = ? ";
            }
            if (codeEps != null)
            {
                query = query + "WHERE UPPER(lab14c13) = ? ";
            }
            if (username != null)
            {
                query = query + "WHERE UPPER(lab14c24) = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (nit != null)
            {
                object = nit;
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }
            if (codeEps != null)
            {
                object = codeEps.toUpperCase();
            }
            if (username != null)
            {
                object = username.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Account account = new Account();
                account.setId(rs.getInt("lab14c1"));
                account.setNit(rs.getString("lab14c2"));
                account.setName(rs.getString("lab14c3"));
                account.setPhone(rs.getString("lab14c4"));
                account.setFax(rs.getString("lab14c5"));
                account.setResponsable(rs.getString("lab14c6"));
                account.setObservation(rs.getString("lab14c11"));
                account.setInstitutional(rs.getInt("lab14c12") == 1);
                account.setEpsCode(rs.getString("lab14c13"));
                account.setAddress(rs.getString("lab14c14"));
                account.setAdditionalAddress(rs.getString("lab14c15"));
                account.setPostalCode(rs.getString("lab14c16"));
                account.setCity(rs.getInt("lab14c17"));
                account.setFaxSend(rs.getInt("lab14c18") == 1);
                account.setPrint(rs.getInt("lab14c19") == 1);
                account.setConnectivityEMR(rs.getInt("lab14c20") == 1);
                account.setEmail(rs.getString("lab14c21"));
                account.setAutomaticEmail(rs.getInt("lab14c22") == 1);
                account.setSelfPay(rs.getInt("lab14c23") == 1);
                account.setUsername(rs.getString("lab14c24"));
                account.setDepartment(rs.getInt("lab14c28"));
                account.setColony(rs.getInt("lab14c29"));
                account.setNamePrint(rs.getString("lab14c30"));
                account.setSendEnd(rs.getInt("lab14c31") == 1);

                account.setLastTransaction(rs.getTimestamp("lab14c26"));

                /*Usuario*/
                account.getUser().setId(rs.getInt("lab04c1"));
                account.getUser().setName(rs.getString("lab04c2"));
                account.getUser().setLastName(rs.getString("lab04c3"));
                account.getUser().setUserName(rs.getString("lab04c4"));

                account.setState(rs.getBoolean("lab07c1"));
                account.setEncryptionReportResult(rs.getBoolean("lab14c32"));
                account.setCentralSystem(rs.getInt("lab118c1"));

                try
                {
                    account.setTaxes(getTheCustomersTaxes(account.getId()));
                } catch (Exception ex)
                {
                    Logger.getLogger(AccountDao.class.getName()).log(Level.SEVERE, null, ex);
                }

                account.setInvoice(rs.getBoolean("lab14c33"));
                account.setAgreement(rs.getBoolean("lab14c34"));
                account.setUsoCfdi(rs.getString("lab14c35"));
                account.setUsoRegimenFiscal(rs.getString("lab14c36"));

                return account;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un cliente en la base de datos.
     *
     * @param account Instancia con los datos del cliente.
     *
     * @return Objeto del account modificada.
     * @throws Exception Error en la base de datos.
     */
    default Account update(Account account) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        if (account.getPassword() != null)
        {
            getJdbcTemplate().update("UPDATE lab14 SET lab14c2 = ? "
                    + ", lab14c3 = ?"
                    + ", lab14c4 = ?"
                    + ", lab14c5 = ?"
                    + ", lab14c6 = ?"
                    + ", lab14c11 = ?"
                    + ", lab14c12 = ?"
                    + ", lab14c13 = ?"
                    + ", lab14c14 = ?"
                    + ", lab14c15 = ?"
                    + ", lab14c16 = ?"
                    + ", lab14c17 = ?"
                    + ", lab14c18 = ?"
                    + ", lab14c19 = ?"
                    + ", lab14c20 = ?"
                    + ", lab14c21 = ?"
                    + ", lab14c22 = ?"
                    + ", lab14c23 = ?"
                    + ", lab14c24 = ?"
                    + ", lab14c25 = ?"
                    + ", lab14c26 = ?"
                    + ", lab04c1 = ?"
                    + ", lab07c1 = ?"
                    + ", lab14c28 = ?"
                    + ", lab14c29 = ?"
                    + ", lab14c30 = ?"
                    + ", lab14c31 = ?"
                    + ", lab14c32 = ?"
                    + ", lab118c1 = ?"
                    + ", lab14c33 = ?"
                    + ", lab14c34 = ?"
                    + ", lab14c35 = ?"
                    + ", lab14c36 = ?"
                    + " WHERE lab14c1 = ?",
                    account.getNit(),
                    account.getName(),
                    account.getPhone(),
                    account.getFax(),
                    account.getResponsable(),
                    account.getObservation(),
                    account.isInstitutional() ? 1 : 0,
                    account.getEpsCode(),
                    account.getAddress(),
                    account.getAdditionalAddress(),
                    account.getPostalCode(),
                    account.getCity(),
                    account.isFaxSend() ? 1 : 0,
                    account.isPrint() ? 1 : 0,
                    account.isConnectivityEMR() ? 1 : 0,
                    account.getEmail(),
                    account.isAutomaticEmail() ? 1 : 0,
                    account.isSelfPay() ? 1 : 0,
                    account.getEmail(),
                    Tools.encrypt(account.getPassword()),
                    timestamp,
                    account.getUser().getId(),
                    account.isState() ? 1 : 0,
                    account.getDepartment(),
                    account.getColony(),
                    account.getNamePrint(),
                    account.isSendEnd() ? 1 : 0,
                    account.isEncryptionReportResult() ? 1 : 0,
                    account.getCentralSystem(),
                    account.isInvoice() ? 1 : 0,
                    account.isAgreement() ? 1 : 0,
                    account.getUsoCfdi(),
                    account.getUsoRegimenFiscal(),
                    account.getId());
        } else
        {
            getJdbcTemplate().update("UPDATE lab14 SET lab14c2 = ?"
                    + ", lab14c3 = ?"
                    + ", lab14c4 = ?"
                    + ", lab14c5 = ?"
                    + ", lab14c6 = ?"
                    + ", lab14c11 = ?"
                    + ", lab14c12 = ?"
                    + ", lab14c13 = ?"
                    + ", lab14c14 = ?"
                    + ", lab14c15 = ?"
                    + ", lab14c16 = ?"
                    + ", lab14c17 = ?"
                    + ", lab14c18 = ?"
                    + ", lab14c19 = ?"
                    + ", lab14c20 = ?"
                    + ", lab14c21 = ?"
                    + ", lab14c22 = ?"
                    + ", lab14c23 = ?"
                    + ", lab14c24 = ?"
                    + ", lab14c26 = ?"
                    + ", lab04c1 = ?"
                    + ", lab07c1 = ?"
                    + ", lab14c28 = ?"
                    + ", lab14c29 = ?"
                    + ", lab14c30 = ?"
                    + ", lab14c31 = ?"
                    + ", lab14c32 = ?"
                    + ", lab118c1 = ?"
                    + ", lab14c33 = ?"
                    + ", lab14c34 = ?"
                    + ", lab14c35 = ?"
                    + ", lab14c36 = ?"
                    + " WHERE lab14c1 = ?",
                    account.getNit(),
                    account.getName(),
                    account.getPhone(),
                    account.getFax(),
                    account.getResponsable(),
                    account.getObservation(),
                    account.isInstitutional() ? 1 : 0,
                    account.getEpsCode(),
                    account.getAddress(),
                    account.getAdditionalAddress(),
                    account.getPostalCode(),
                    account.getCity(),
                    account.isFaxSend() ? 1 : 0,
                    account.isPrint() ? 1 : 0,
                    account.isConnectivityEMR() ? 1 : 0,
                    account.getEmail(),
                    account.isAutomaticEmail() ? 1 : 0,
                    account.isSelfPay() ? 1 : 0,
                    account.getEmail(),
                    timestamp,
                    account.getUser().getId(),
                    account.isState() ? 1 : 0,
                    account.getDepartment(),
                    account.getColony(),
                    account.getNamePrint(),
                    account.isSendEnd() ? 1 : 0,
                    account.isEncryptionReportResult() ? 1 : 0,
                    account.getCentralSystem(),
                    account.isInvoice() ? 1 : 0,
                    account.isAgreement() ? 1 : 0,
                    account.getUsoCfdi(),
                    account.getUsoRegimenFiscal(),
                    account.getId());
        }

        account.setLastTransaction(timestamp);
        // Adicion de un listado de impuestos a un cliente:
        if (account.getTaxes() != null && !account.getTaxes().isEmpty())
        {
            account.getTaxes().forEach(tax ->
            {
                tax.setCustomerId(account.getId());
            });
            addTaxesPerCustomer(account.getTaxes(), account.getId());
        }

        return account;
    }

    /**
     *
     * Lista las tarifas asociadas al cliente
     *
     * @param id ID del cliente.
     * @return lista de tarifas del cliente.
     *
     * @throws Exception Error en base de datos.
     */
    default List<RatesOfAccount> getRates(Integer id) throws Exception
    {
        try
        {
            String query = ""
                    + " SELECT "
                    + " lab904.lab904c1"
                    + " ,lab904.lab904c2"
                    + " ,lab14.lab14c3"
                    + " ,lab904.lab904c3"
                    + " ,lab904.lab904c15"
                    + " ,lab15.lab904c1 AS apply"
                    + " ,lab904.lab904c37"
                    + " FROM lab904"
                    + " LEFT JOIN lab15 ON lab15.lab904c1=lab904.lab904c1 AND lab15.lab14c1 = ? "
                    + " LEFT JOIN lab14 ON lab14.lab14c1 = lab15.lab14c1 ";

            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                RatesOfAccount ratesOfAccount = new RatesOfAccount();
                //TARIFA
                ratesOfAccount.getRate().setId(rs.getInt("lab904c1"));
                ratesOfAccount.getRate().setCode(rs.getString("lab904c2"));
                ratesOfAccount.getRate().setName(rs.getString("lab904c3"));
                ratesOfAccount.getRate().setTypePayer(rs.getInt("lab904c15") == 4 ? 1 : 0);
                ratesOfAccount.setApply(rs.getString("apply") != null);
                ratesOfAccount.getRate().setLastTransaction(rs.getTimestamp("lab904c37"));
                //cliente
                ratesOfAccount.getAccount().setId(id);
                ratesOfAccount.getAccount().setName(rs.getString("lab14c3"));
                return ratesOfAccount;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     *
     * Lista las tarifas asociadas al cliente
     *
     * @param id ID del cliente.
     * @return lista de tarifas del cliente.
     *
     * @throws Exception Error en base de datos.
     */
    default List<RatesOfAccount> getRatesByAccount(Integer id) throws Exception
    {
        try
        {
            String query = ""
                    + " SELECT "
                    + " lab904.lab904c1"
                    + " ,lab904.lab904c2"
                    + " ,lab14.lab14c3"
                    + " ,lab904.lab904c3"
                    + " ,lab904.lab904c15"
                    + " ,lab15.lab904c1 AS apply"
                    + " FROM lab904"
                    + " INNER JOIN lab15 ON lab15.lab904c1=lab904.lab904c1 AND lab15.lab14c1 = ? "
                    + " INNER JOIN lab14 ON lab14.lab14c1 = lab15.lab14c1 ";

            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                RatesOfAccount ratesOfAccount = new RatesOfAccount();
                //TARIFA
                ratesOfAccount.getRate().setId(rs.getInt("lab904c1"));
                ratesOfAccount.getRate().setCode(rs.getString("lab904c2"));
                ratesOfAccount.getRate().setName(rs.getString("lab904c3"));
                ratesOfAccount.getRate().setTypePayer(rs.getInt("lab904c15") == 4 ? 1 : 0);
                ratesOfAccount.setApply(rs.getString("apply") != null);
                //cliente
                ratesOfAccount.getAccount().setId(id);
                ratesOfAccount.getAccount().setName(rs.getString("lab14c3"));
                return ratesOfAccount;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     *
     * Inserta las tarifas asociadas al cliente
     *
     * @param ratesOfAccounts lista de tarifas del cliente.
     * @return la cantidad de registros insertados.
     *
     * @throws Exception Error en base de datos.
     */
    default int insertRates(List<RatesOfAccount> ratesOfAccounts) throws Exception
    {
        getJdbcTemplate().execute("DELETE FROM lab15 WHERE lab14c1 = " + ratesOfAccounts.get(0).getAccount().getId());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab15");
        for (RatesOfAccount ratesOfAccount : ratesOfAccounts)
        {
            HashMap parameters = new HashMap();

            parameters.put("lab14c1", ratesOfAccount.getAccount().getId());
            parameters.put("lab904c1", ratesOfAccount.getRate().getId());
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[ratesOfAccounts.size()]));

        return inserted.length;
    }

    /**
     *
     * Elimina las tarifas asociadas al cliente
     *
     * @param idAccount Id del cliente.
     * @return numero de registro elinador.
     *
     * @throws Exception Error en base de datos.
     */
    default int deleteRates(Integer idAccount) throws Exception
    {
        return getJdbcTemplate().update("DELETE FROM lab15 WHERE lab14c1 = " + idAccount);

    }

    /**
     * Obtener información de un cliente por correo.
     *
     *
     * @param email
     * @return Instancia con los datos del cliente.
     * @throws Exception Error en la base de datos.
     */
    default Account getByEmail(String email) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab14c1"
                    + ", lab14c2"
                    + ", lab14c3"
                    + ", lab14c4"
                    + ", lab14c5"
                    + ", lab14c6"
                    + ", lab14c11"
                    + ", lab14c12"
                    + ", lab14c13"
                    + ", lab14c14"
                    + ", lab14c15"
                    + ", lab14c16"
                    + ", lab14c17"
                    + ", lab14c18"
                    + ", lab14c19"
                    + ", lab14c20"
                    + ", lab14c21"
                    + ", lab14c22"
                    + ", lab14c23"
                    + ", lab14c24"
                    + ", lab14c26"
                    + ", lab14.lab07c1"
                    + ", lab14.lab04c1"
                    + ", lab04c2"
                    + ", lab04c3"
                    + ", lab04c4"
                    + ", lab14c28"
                    + ", lab14c29"
                    + ", lab14c30"
                    + ", lab14c31"
                    + ", lab14c32"
                    + ", lab118c1 "
                    + ", lab14c33"
                    + ", lab14c34"
                    + ", lab14c35"
                    + " FROM lab14 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab14.lab04c1 "
                    + "WHERE lab14c21 = ? ";

            Object object = email;
            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Account account = new Account();
                account.setId(rs.getInt("lab14c1"));
                account.setNit(rs.getString("lab14c2"));
                account.setName(rs.getString("lab14c3"));
                account.setPhone(rs.getString("lab14c4"));
                account.setFax(rs.getString("lab14c5"));
                account.setResponsable(rs.getString("lab14c6"));
                account.setObservation(rs.getString("lab14c11"));
                account.setInstitutional(rs.getInt("lab14c12") == 1);
                account.setEpsCode(rs.getString("lab14c13"));
                account.setAddress(rs.getString("lab14c14"));
                account.setAdditionalAddress(rs.getString("lab14c15"));
                account.setPostalCode(rs.getString("lab14c16"));
                account.setCity(rs.getInt("lab14c17"));
                account.setFaxSend(rs.getInt("lab14c18") == 1);
                account.setPrint(rs.getInt("lab14c19") == 1);
                account.setConnectivityEMR(rs.getInt("lab14c20") == 1);
                account.setEmail(rs.getString("lab14c21"));
                account.setAutomaticEmail(rs.getInt("lab14c22") == 1);
                account.setSelfPay(rs.getInt("lab14c23") == 1);
                account.setUsername(rs.getString("lab14c24"));
                account.setDepartment(rs.getInt("lab14c28"));
                account.setColony(rs.getInt("lab14c29"));
                account.setNamePrint(rs.getString("lab14c30"));
                account.setSendEnd(rs.getInt("lab14c31") == 1);
                account.setCentralSystem(rs.getInt("lab118c1"));
                account.setLastTransaction(rs.getTimestamp("lab14c26"));
                /*Usuario*/
                account.getUser().setId(rs.getInt("lab04c1"));
                account.getUser().setName(rs.getString("lab04c2"));
                account.getUser().setLastName(rs.getString("lab04c3"));
                account.getUser().setUserName(rs.getString("lab04c4"));

                account.setState(rs.getInt("lab07c1") == 1);
                account.setEncryptionReportResult(rs.getBoolean("lab14c32"));

                try
                {
                    account.setTaxes(getTheCustomersTaxes(account.getId()));
                } catch (Exception ex)
                {
                    Logger.getLogger(AccountDao.class.getName()).log(Level.SEVERE, null, ex);
                }

                account.setInvoice(rs.getBoolean("lab14c33"));
                account.setAgreement(rs.getBoolean("lab14c34"));
                account.setUsoCfdi(rs.getString("lab14c35"));

                return account;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Inserta los impuestos asociadas al cliente
     *
     * @param taxes lista de impuestos a ser asociados
     * @param accountId Id del cliente
     *
     * @return La lista de impuestos asociados
     * @throws Exception Error en base de datos.
     */
    default int addTaxesPerCustomer(List<DiscountRate> taxes, int accountId) throws Exception
    {
        // Eliminamos la lista de impuestos asociados previamente
        deleteTaxList(accountId);
        // Cargamos los impuestos asociados a este cliente
        Timestamp creationDate = new Timestamp(new Date().getTime());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab911");
        taxes.stream().map((tax) ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab14c1", tax.getCustomerId());
            parameters.put("lab910c1", tax.getId());
            parameters.put("lab911c1", creationDate);
            return parameters;
        }).forEachOrdered((parameters) ->
        {
            batchArray.add(parameters);
        });

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[taxes.size()]));

        return inserted.length;
    }

    /**
     * Elimina los impuestos asociados a un cliente previamente
     *
     * @param accountId Id del cliente
     *
     * @throws Exception Error en base de datos.
     */
    default void deleteTaxList(int accountId) throws Exception
    {
        getJdbcTemplate().execute("DELETE FROM lab911 WHERE lab14c1 = " + accountId);
    }

    /**
     * Obtiene los impuestos asignados a un cliente
     *
     * @param accountId Id del cliente
     * @return
     *
     * @throws Exception Error en base de datos.
     */
    default List<DiscountRate> getTheCustomersTaxes(int accountId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab910.lab910c1 AS taxId")
                    .append(", lab910.lab910c2 AS taxCode")
                    .append(", lab910.lab910c3 AS taxName")
                    .append(", lab910.lab910c4 AS taxPercentage")
                    .append(", lab910.lab07c1 AS taxState")
                    .append(", lab910.lab04c1 AS creationUser")
                    .append(", lab910.lab910c5 AS creationDate")
                    .append(", lab910.lab04c1_2 AS modifiedUser")
                    .append(", lab910.lab910c6 AS modifiedDate")
                    .append(" FROM lab911 ")
                    .append("JOIN lab910 ON lab910.lab910c1 = lab911.lab910c1 ")
                    .append("WHERE lab911.lab14c1 = ").append(accountId);

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                DiscountRate discountRate = new DiscountRate();
                discountRate.setId(rs.getInt("taxId"));
                discountRate.setCode(rs.getString("taxCode"));
                discountRate.setName(rs.getString("taxName"));
                discountRate.setPercentage(rs.getDouble("taxPercentage"));
                discountRate.setState(rs.getBoolean("taxState"));
                discountRate.setIdUserCreating(rs.getInt("creationUser"));
                discountRate.setDateCreation(rs.getTimestamp("creationDate"));
                discountRate.setModifyingUserId(rs.getInt("modifiedUser"));
                discountRate.setDateOfModification(rs.getTimestamp("modifiedDate"));
                return discountRate;
            });
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtener el nombre de un cliente por su id
     *
     * @param id ID del cliente a ser consultada.
     *
     * @return Nombre del cliente
     * @throws Exception Error en la base de datos.
     */
    default String getName(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab14c3")
                    .append(" FROM lab14 ")
                    .append("WHERE lab14c1 = ").append(id);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getString("lab14c3");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
}
