package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.billing.ActivationValidator;
import net.cltech.enterprisent.domain.masters.billing.Contract;
import net.cltech.enterprisent.domain.masters.billing.CustomerContractFilter;
import net.cltech.enterprisent.domain.masters.billing.DiscountRate;
import net.cltech.enterprisent.domain.masters.billing.RatesByContract;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * contratos
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 30/06/2021
 * @see Creación
 */
public interface ContractDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Se obtiene el contrato por su id, nombre o codigo
     *
     * @param id
     * @param name
     * @param code
     *
     * @return Lista de recipientes
     * @throws Exception
     */
    default Contract getContract(Integer id, String name, String code) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            String where = "";

            query.append("SELECT lab990.lab990c1 as id")
                    .append(", lab990.lab990c2 as nombre")
                    .append(", lab990.lab07c1 as estado")
                    .append(", lab990.lab990c4 as montoMaximo")
                    .append(", lab990.lab990c5 as montoActual")
                    .append(", lab990.lab990c6 as montoAlarma")
                    .append(", lab990.lab990c7 as capitado")
                    .append(", lab990.lab990c8 as descuento")
                    .append(", lab990.lab990c9 as anticipo")
                    .append(", lab990.lab990c10 as diasPagoEfectivo")
                    .append(", lab990.lab990c11 as diasPagoCredito")
                    .append(", lab990.lab990c12 as plazoPago")
                    .append(", lab990.lab990c13 as cuotaModeradora")
                    .append(", lab990.lab990c14 as copago")
                    .append(", lab990.lab990c15 as regimen")
                    .append(", lab990.lab990c16 as nombreVendedor")
                    .append(", lab990.lab990c17 as identificacionVendedor")
                    .append(", lab990.lab14c1 as idcliente")
                    .append(", lab990.lab990c21 as code")
                    .append(", lab990.lab990c22 as city")
                    .append(", lab990.lab990c23 as department")
                    .append(", lab990.lab990c24 as capitatedContract")
                    .append(", lab990.lab990c25 as monthlyValue")
                    .append(" FROM lab990 ");

            if (id != null && !id.equals(0))
            {
                where += where.contains("WHERE") ? " AND lab990c1 = " + id : "WHERE lab990c1 = " + id;
            }
            if (name != null && !name.isEmpty())
            {
                where += where.contains("WHERE") ? " AND lab990c2 = '" + name + "'" : "WHERE lab990c2 = '" + name + "'";
            }
            if (code != null && !code.isEmpty())
            {
                where += where.contains("WHERE") ? " AND lab990c21 = '" + code + "'" : "WHERE lab990c21 = '" + code + "'";
            }

            query.append(where);
            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                Contract cont = new Contract();
                cont.setId(rs.getInt("id"));
                cont.setName(rs.getString("nombre"));
                cont.setState(rs.getInt("estado") == 1);
                cont.setMaxAmount(rs.getDouble("montoMaximo"));
                cont.setCurrentAmount(rs.getDouble("montoActual"));
                cont.setAlertAmount(rs.getFloat("montoAlarma"));
                cont.setCapitated(rs.getInt("capitado"));
                cont.setDiscount(rs.getFloat("descuento"));
                cont.setAdvancePercentage(rs.getInt("anticipo"));
                cont.setCashPaymentDays(rs.getInt("diasPagoEfectivo"));
                cont.setCreditPayDays(rs.getInt("diasPagoCredito"));
                cont.setPaymentPeriod(rs.getInt("plazoPago"));
                cont.setModeratingFee(rs.getInt("cuotaModeradora") == 1);
                cont.setCopayment(rs.getInt("copago") == 1);
                cont.setRegimen(rs.getString("regimen"));
                cont.setVendorName(rs.getString("nombreVendedor"));
                cont.setVendorIdentifier(rs.getString("identificacionVendedor"));
                cont.setIdclient(rs.getInt("idcliente"));
                cont.setCode(rs.getString("code"));
                cont.setCity(rs.getInt("city"));
                cont.setDepartment(rs.getInt("department"));
                cont.setCapitatedContract(rs.getBoolean("capitatedContract"));
                cont.setMonthlyValue(rs.getDouble("monthlyValue"));

                return cont;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Registra nuevo contrato.
     *
     * @param contract Instancia con los datos del contrato.
     *
     * @return Instancia con los datos del contrato.
     * @throws Exception Error en la base de datos.
     */
    default Contract create(Contract contract) throws Exception
    {
        try
        {

            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab990")
                    .usingGeneratedKeyColumns("lab990c1");

            HashMap parameters = new HashMap();

            parameters.put("lab990c2", contract.getName());
            parameters.put("lab07c1", contract.isState() ? 1 : 0);
            parameters.put("lab990c4", contract.getMaxAmount());
            parameters.put("lab990c5", contract.getCurrentAmount());
            parameters.put("lab990c6", contract.getAlertAmount());
            parameters.put("lab990c7", contract.getCapitated());
            parameters.put("lab990c8", contract.getDiscount());
            parameters.put("lab990c9", contract.getAdvancePercentage());
            parameters.put("lab990c10", contract.getCashPaymentDays());
            parameters.put("lab990c11", contract.getCreditPayDays());
            parameters.put("lab990c12", contract.getPaymentPeriod());
            parameters.put("lab990c13", contract.isModeratingFee() == true ? 1 : 0);
            parameters.put("lab990c14", contract.isCopayment() == true ? 1 : 0);
            parameters.put("lab990c15", contract.getRegimen());
            parameters.put("lab990c16", contract.getVendorName());
            parameters.put("lab990c17", contract.getVendorIdentifier());
            parameters.put("lab990c19", timestamp);
            parameters.put("lab990c20", timestamp);
            parameters.put("lab990c21", contract.getCode());
            parameters.put("lab14c1", contract.getIdclient());
            parameters.put("lab990c22", contract.getCity());
            parameters.put("lab990c23", contract.getDepartment());
            parameters.put("lab990c24", contract.isCapitatedContract() ? 1 : 0);
            parameters.put("lab990c25", contract.getMonthlyValue());
            Number key = insert.executeAndReturnKey(parameters);
            contract.setId(key.intValue());

            return contract;

        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un contrato.
     *
     * @param contract
     * @return Instancia modificada del contrato.
     * @throws Exception Error en la base de datos.
     */
    default Contract update(Contract contract) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            getJdbcTemplate().update("UPDATE lab990 SET lab990c2=?"
                    + ", lab07c1=?"
                    + ", lab990c4=?"
                    + ", lab990c5=?"
                    + ", lab990c6=?"
                    + ", lab990c7=?"
                    + ", lab990c8=?"
                    + ", lab990c9=?"
                    + ", lab990c10=?"
                    + ", lab990c11=?"
                    + ", lab990c12=?"
                    + ", lab990c13=?"
                    + ", lab990c14=?"
                    + ", lab990c15=?"
                    + ", lab990c16=?"
                    + ", lab990c17=?"
                    + ", lab990c20=?"
                    + ", lab14c1=?"
                    + ", lab990c21=?"
                    + ", lab990c22=?"
                    + ", lab990c23=?"
                    + ", lab990c24=?"
                    + ", lab990c25=?"
                    + " WHERE lab990c1=?",
                    contract.getName(),
                    contract.isState() ? 1 : 0,
                    contract.getMaxAmount(),
                    contract.getCurrentAmount(),
                    contract.getAlertAmount(),
                    contract.getCapitated(),
                    contract.getDiscount(),
                    contract.getAdvancePercentage(),
                    contract.getCashPaymentDays(),
                    contract.getCreditPayDays(),
                    contract.getPaymentPeriod(),
                    contract.isModeratingFee() == true ? 1 : 0,
                    contract.isCopayment() == true ? 1 : 0,
                    contract.getRegimen(),
                    contract.getVendorName(),
                    contract.getVendorIdentifier(),
                    timestamp,
                    contract.getIdclient(),
                    contract.getCode(),
                    contract.getCity(),
                    contract.getDepartment(),
                    contract.isCapitatedContract() ? 1 : 0,
                    contract.getMonthlyValue(),
                    contract.getId());

            return contract;
        }
        catch (DataAccessException e)
        {
            return null;
        }

    }

    /**
     *
     * Inserta las tarifas asociadas al cliente
     *
     * @param ratesOfAccounts lista de tarifas del cliente.
     * @param id
     * @return la cantidad de registros insertados.
     *
     * @throws Exception Error en base de datos.
     */
    default int insertRatesWhitId(List<RatesByContract> ratesOfAccounts, int id) throws Exception
    {
        getJdbcTemplate().execute("DELETE FROM lab993 WHERE lab990c1 = " + id);
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab993");
        ratesOfAccounts.stream().map((rate) ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab990c1", id);
            parameters.put("lab904c1", rate.getRateId());
            return parameters;
        }).forEachOrdered((parameters) ->
        {
            batchArray.add(parameters);
        });

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[ratesOfAccounts.size()]));

        return inserted.length;
    }


    /**
     * Inserta los impuestos asociadas al cliente con el id resibido
     *
     * @param taxes lista de impuestos a ser asociados
     * @param id
     *
     * @return La lista de impuestos asociados
     * @throws Exception Error en base de datos.
     */
    default int addTaxesPerContract(List<DiscountRate> taxes, int id) throws Exception
    {
        // Eliminamos la lista de impuestos asociados previamente
        getJdbcTemplate().execute("DELETE FROM lab992 WHERE lab990c1 = " + id);

        try
        {
            // Cargamos los impuestos asociados a este cliente
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab992");
            taxes.stream().map((tax) ->
            {
                HashMap parameters = new HashMap();
                parameters.put("lab990c1", id);
                parameters.put("lab910c1", tax.getId());
                return parameters;
            }).forEachOrdered((parameters) ->
            {
                batchArray.add(parameters);
            });

            int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[taxes.size()]));

            return inserted.length;
        }
        catch (Exception e)
        {
            return 0;
        }

    }

    /**
     * Obtiene los impuestos asignados a un cliente
     *
     * @param id
     * @return
     * @throws Exception Error en base de datos.
     */
    default List<DiscountRate> getTheContractTaxes(int id) throws Exception
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
                    .append(", lab910.lab910c6 AS modifiedDate ")
                    .append("FROM lab992 ")
                    .append("JOIN lab910 ON lab910.lab910c1 = lab992.lab910c1 ")
                    .append("WHERE lab992.lab990c1 = ").append(id);

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
        }
        catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta la lista de contratos por el id del cliente
     *
     * @param idCustomer
     * @param idRate 
     * @return Lista de contratos
     * @throws Exception
     */
    default List<Contract> getContractByCustomerId(int idCustomer, int idRate) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab990.lab990c1 as id")
                    .append(", lab990.lab990c2 as nombre")
                    .append(", lab990.lab07c1 as estado")
                    .append(", lab990.lab990c4 as montoMaximo")
                    .append(", lab990.lab990c5 as montoActual")
                    .append(", lab990.lab990c6 as montoAlarma")
                    .append(", lab990.lab990c7 as capitado")
                    .append(", lab990.lab990c8 as descuento")
                    .append(", lab990.lab990c9 as anticipo")
                    .append(", lab990.lab990c10 as diasPagoEfectivo")
                    .append(", lab990.lab990c11 as diasPagoCredito")
                    .append(", lab990.lab990c12 as plazoPago")
                    .append(", lab990.lab990c13 as cuotaModeradora")
                    .append(", lab990.lab990c14 as copago")
                    .append(", lab990.lab990c15 as regimen")
                    .append(", lab990.lab990c16 as nombreVendedor")
                    .append(", lab990.lab990c17 as identificacionVendedor")
                    .append(", lab990.lab14c1 as idcliente")
                    .append(", lab990.lab990c21 as code")
                    .append(", lab990.lab990c22 as city")
                    .append(", lab990.lab990c23 as department")
                    .append(", lab990.lab990c24 as capitatedContract")
                    .append(", lab990.lab990c25 as monthlyValue")
                    .append(" FROM lab990 ");
           
            if(idRate > 0) {
                query.append(" INNER JOIN lab993 ON la993.lab990c1 = lab990.lab990c1 ").append(idCustomer); 
            }
            
            if(idCustomer > 0) {
               query.append("WHERE lab14c1 = ").append(idCustomer); 
            }
            
            if(idRate > 0) {
                query.append(" WHERE lab904c1 = ").append(idRate); 
            }

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                Contract cont = new Contract();
                cont.setId(rs.getInt("id"));
                cont.setName(rs.getString("nombre"));
                cont.setState(rs.getBoolean("estado"));
                cont.setMaxAmount(rs.getDouble("montoMaximo"));
                cont.setCurrentAmount(rs.getDouble("montoActual"));
                cont.setAlertAmount(rs.getFloat("montoAlarma"));
                cont.setCapitated(rs.getInt("capitado"));
                cont.setDiscount(rs.getFloat("descuento"));
                cont.setAdvancePercentage(rs.getInt("anticipo"));
                cont.setCashPaymentDays(rs.getInt("diasPagoEfectivo"));
                cont.setCreditPayDays(rs.getInt("diasPagoCredito"));
                cont.setPaymentPeriod(rs.getInt("plazoPago"));
                cont.setModeratingFee(rs.getInt("cuotaModeradora") == 1);
                cont.setCopayment(rs.getInt("copago") == 1);
                cont.setRegimen(rs.getString("regimen"));
                cont.setVendorName(rs.getString("nombreVendedor"));
                cont.setVendorIdentifier(rs.getString("identificacionVendedor"));
                cont.setIdclient(rs.getInt("idcliente"));
                cont.setCode(rs.getString("code"));
                cont.setCity(rs.getInt("city"));
                cont.setDepartment(rs.getInt("department"));
                cont.setCapitatedContract(rs.getBoolean("capitatedContract"));
                cont.setMonthlyValue(rs.getDouble("monthlyValue"));

                return cont;
            });
        }
        catch (DataAccessException e)
        {
            OrderCreationLog.error("Error contratos " + e);
            return null;
        }
    }

    /**
     * Consulta las tarifas asociadas a todos los contratos de un cliente por el
     * id de este
     *
     * @param customerId
     * @return Lista de tarifas de todos los contratos
     * @throws Exception
     */
    default List<RatesOfAccount> ratesOfACustomersContracts(int customerId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab904.lab904c1 AS id")
                    .append(", lab904.lab904c2 AS code")
                    .append(", lab904.lab904c3 AS name")
                    .append(", lab904.lab904c15 AS typeOfPayer")
                    .append(" FROM lab990 ")
                    .append("JOIN lab993 ON lab993.lab990c1 = lab990.lab990c1 ")
                    .append("JOIN lab904 ON lab993.lab904c1 = lab904.lab904c1 ")
                    .append("WHERE lab990.lab14c1 = ").append(customerId)
                    .append(" AND lab990.lab07c1 = 1");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                //TARIFA
                RatesOfAccount ratesOfAccount = new RatesOfAccount();
                ratesOfAccount.getRate().setId(rs.getInt("id"));
                ratesOfAccount.getRate().setCode(rs.getString("code"));
                ratesOfAccount.getRate().setName(rs.getString("name"));
                ratesOfAccount.getRate().setTypePayer(rs.getInt("typeOfPayer") == 4 ? 1 : 0);
                return ratesOfAccount;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
    


    /**
     * Consulta las tarifas asociadas a un cliente y todos sus contratos por el
     * id de este
     *
     * @param customerId
     * @param contractId
     * @param state
     *
     * @return Lista de tarifas de los clientes y de todos sus contratos
     * @throws Exception
     */
    default List<RatesByContract> allRatesPerCustomerAndContract(int customerId, int contractId, Boolean state) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab904.lab904c1 ")
                    .append(", lab904.lab904c2 ")
                    .append(", lab904.lab904c3 ")
                    .append(", lab990.lab14c1 ")
                    .append(", lab993.lab990c1 ")
                    .append(", lab990.lab990c2 ")
                    .append(", lab990.lab07c1 ")
                    .append(" FROM lab904 ")
                    .append(" INNER JOIN lab993 ON lab993.lab904c1 = lab904.lab904c1 ")
                    .append(" INNER JOIN lab990 ON lab990.lab990c1 = lab993.lab990c1 ");
            
            query.append(" WHERE lab993.lab990c1 = ").append(contractId);
            
            if(customerId > 0) {
                query.append(" AND lab990.lab14c1 = ").append(customerId);
            }
            
            if(state != null) {
                if(state) {
                query.append(" AND lab904.lab07c1 = 1");
                } else {
                    query.append(" AND lab904.lab07c1 = 0");
                }
            }
            
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                
                    RatesByContract rate = new RatesByContract();
                    rate.setRateId(rs.getInt("lab904c1"));
                    rate.setRateCode(rs.getString("lab904c2"));
                    rate.setNameRate(rs.getString("lab904c3"));
                    rate.setClientId(rs.getInt("lab14c1") == customerId ? rs.getInt("lab14c1") : 0);
                    rate.setContractId(rs.getInt("lab14c1") == customerId ? rs.getInt("lab990c1"): 0);
                    rate.setContractName(rs.getString("lab990c2"));
                    rate.setStatusType(rs.getInt("lab990c1") == contractId ? (short) 1 : (short)0 );
                    rate.setContractState(rs.getInt("lab07c1"));
                    
                    return rate;
                
                
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    
    /**
     * consulta el estado de un contrato
     *
     * @param idContract
     *
     * @return La lista de impuestos asociados
     * @throws Exception Error en base de datos.
     */
    default int getStateContract(int idContract) throws Exception
    {
        
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab07c1 FROM lab990 ")
                 .append("WHERE lab990c1 = ").append(idContract);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab07c1");
            });
        }
        catch (Exception e)
        {
            return 0;
        }

    }
    
    /**
     * consulta el cliente al que pertenece un contrato
     *
     * @param idContract
     *
     * @return La lista de impuestos asociados
     * @throws Exception Error en base de datos.
     */
    default int getAccountContract(int idContract) throws Exception
    {
        
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab14c1 FROM lab990 ")
                 .append("WHERE lab990c1 = ").append(idContract);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab14c1");
            });
        }
        catch (Exception e)
        {
            return 0;
        }

    }
    
    
    /**
     * Consulta la lista de contratos que participan a la hora de facturar a un
     * cliente dependiendo de las tarifas asociadas al mismo
     *
     * @param filter Filtros para la consulta de contratos
     * @return Lista de contratos por cliente
     * @throws Exception
     */
    default List<Contract> getContractsForTheAssociatedRates(CustomerContractFilter filter) throws Exception
    {
        try
        {
            List<Contract> contracts = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(filter.getStartDate()), Integer.toString(filter.getEndDate()));
            int currentYear = DateTools.dateToNumberYear(new Date());
            String lab22;
            String lab900;
            
            for (Integer year : years)
            {
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                
                StringBuilder query = new StringBuilder();
                query.append("SELECT DISTINCT lab990.lab990c1 AS id")
                        .append(", lab990.lab990c2 AS contractName")
                        .append(", lab990.lab990c21 AS code")
                        .append(", lab990.lab990c4 AS maximumAmount")
                        .append(", lab990.lab990c5 AS currentAmount")
                        .append(", lab990.lab990c6 AS alarmAmount")
                        .append(", lab990.lab990c7 AS capitado")
                        .append(" FROM  ").append(lab900).append(" as lab900 ")
                        .append("JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab900.lab22c1 ")
                        .append("JOIN lab993 ON lab993.lab904c1 = lab900.lab904c1 ")
                        .append("JOIN lab990 ON lab990.lab990c1 = lab993.lab990c1 AND lab990.lab14c1 = lab900.lab14c1 ")
                        .append(" WHERE lab900.lab14c1 = ").append(filter.getCustomerId())
                        .append(" AND lab22c2 BETWEEN ").append(filter.getStartDate()).append(" AND ").append(filter.getEndDate())
                        .append(" AND lab990.lab07c1 = 1 AND lab901c1I is null")
                        .append(" ");
                
                
                if( filter.getBranchId() != -1 ) {
                    query.append("AND lab900.lab05c1 = ").append(filter.getBranchId())
                        .append(" ");
                } 

                getJdbcTemplate().query(query.toString(),
                        (ResultSet rs, int i)
                        ->
                {
                    Contract contract = new Contract();
                    contract.setId(rs.getInt("id"));
                    contract.setName(rs.getString("contractName"));
                    contract.setCode(rs.getString("code"));
                    contract.setMaxAmount(rs.getDouble("maximumAmount"));
                    contract.setCurrentAmount(rs.getDouble("currentAmount"));
                    contract.setAlertAmount(rs.getFloat("alarmAmount"));
                    contract.setCapitated(rs.getInt("capitado"));
                    contracts.add(contract);
                    return contract;
                });
                
            }
            return contracts;
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
    
    default List<RatesByContract> validatesContractActivation(ActivationValidator validator) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab990.lab990c2 AS contractName")
                    .append(", lab990.lab990c21 AS contractCode")
                    .append(", lab904.lab904c2 AS ratecode")
                    .append(", lab904.lab904c3 AS ratename")
                    .append(" FROM lab990 ")
                    .append("JOIN lab993 ON lab993.lab990c1 = lab990.lab990c1 ")
                    .append("JOIN lab904 ON lab993.lab904c1 = lab904.lab904c1 ")
                    .append("WHERE lab990.lab07c1 = 1")
                    .append(" AND lab990.lab14c1 = ").append(validator.getCustomerId())
                    .append(" AND lab990.lab990c1 != ").append(validator.getContractId())
                    .append(" AND lab993.lab904c1 IN (").append(validator.getRates()).append(")");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                RatesByContract contract = new RatesByContract();
                contract.setContractCode(rs.getString("contractCode"));
                contract.setContractName(rs.getString("contractName"));
                contract.setRateCode(rs.getString("ratecode"));
                contract.setNameRate(rs.getString("ratename"));
                        
                return contract;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
    
    /**
     * Consulta la lista de contratos por el id del cliente
     *
     * @param customers
     * @param rates 
     * @return Lista de contratos
     * @throws Exception
     */
    default List<Contract> getContractByCustomers(List<Integer> customers, List<Integer> rates) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab990.lab990c1 as id")
                .append(", lab990.lab990c2 as nombre")
                .append(", lab990.lab07c1 as estado")
                .append(", lab990.lab990c4 as montoMaximo")
                .append(", lab990.lab990c5 as montoActual")
                .append(", lab990.lab990c6 as montoAlarma")
                .append(", lab990.lab990c7 as capitado")
                .append(", lab990.lab990c8 as descuento")
                .append(", lab990.lab990c9 as anticipo")
                .append(", lab990.lab990c10 as diasPagoEfectivo")
                .append(", lab990.lab990c11 as diasPagoCredito")
                .append(", lab990.lab990c12 as plazoPago")
                .append(", lab990.lab990c13 as cuotaModeradora")
                .append(", lab990.lab990c14 as copago")
                .append(", lab990.lab990c15 as regimen")
                .append(", lab990.lab990c16 as nombreVendedor")
                .append(", lab990.lab990c17 as identificacionVendedor")
                .append(", lab990.lab14c1 as idcliente")
                .append(", lab990.lab990c21 as code")
                .append(", lab990.lab990c22 as city")
                .append(", lab990.lab990c23 as department")
                .append(", lab990.lab990c24 as capitatedContract")
                .append(", lab990.lab990c25 as monthlyValue")
                .append(", lab904.lab904c1 ")
                .append(", lab904.lab904c2 ")
                .append(", lab904.lab904c3 ")
                .append(", lab990.lab14c1 ")
                .append(", lab993.lab990c1 ")
                .append(", lab990.lab990c2 ")
                .append(", lab990.lab07c1 ")
                .append(" FROM lab993 ")
                .append(" INNER JOIN lab990 ON lab990.lab990c1 = lab993.lab990c1 ")
                .append(" INNER JOIN lab904 ON lab904.lab904c1 = lab993.lab904c1 ");    
            
            query.append(" WHERE lab14c1 IN(").append(customers.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");

            if(rates.size() > 0) {
                query.append("  AND lab993.lab904c1 IN(").append(rates.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");
            }
            
            query.append(" AND lab990.lab07c1 = 1 ");
            
                        
            List<Contract> contracts = new ArrayList<>();
            RowMapper mapper = (RowMapper<Contract>) (ResultSet rs, int i) ->
            {
                Contract contract = new Contract();
                contract.setId(rs.getInt("id"));
                
                if(contracts.contains(contract)) {
                    RatesByContract rate = new RatesByContract();
                    rate.setRateId(rs.getInt("lab904c1"));
                    rate.setRateCode(rs.getString("lab904c2"));
                    rate.setNameRate(rs.getString("lab904c3"));
                    rate.setClientId(rs.getInt("lab14c1"));
                    rate.setContractId(rs.getInt("lab990c1"));
                    rate.setContractName(rs.getString("lab990c2"));
                    rate.setStatusType(rs.getShort("lab990c1"));
                    rate.setContractState(rs.getInt("lab07c1"));
                    
                    contracts.get(contracts.indexOf(contract)).getRates().add(rate);
                    
                } else {
                    contract.setName(rs.getString("nombre"));
                    contract.setState(rs.getBoolean("estado"));
                    contract.setMaxAmount(rs.getDouble("montoMaximo"));
                    contract.setCurrentAmount(rs.getDouble("montoActual"));
                    contract.setAlertAmount(rs.getFloat("montoAlarma"));
                    contract.setCapitated(rs.getInt("capitado"));
                    contract.setDiscount(rs.getFloat("descuento"));
                    contract.setAdvancePercentage(rs.getInt("anticipo"));
                    contract.setCashPaymentDays(rs.getInt("diasPagoEfectivo"));
                    contract.setCreditPayDays(rs.getInt("diasPagoCredito"));
                    contract.setPaymentPeriod(rs.getInt("plazoPago"));
                    contract.setModeratingFee(rs.getInt("cuotaModeradora") == 1);
                    contract.setCopayment(rs.getInt("copago") == 1);
                    contract.setRegimen(rs.getString("regimen"));
                    contract.setVendorName(rs.getString("nombreVendedor"));
                    contract.setVendorIdentifier(rs.getString("identificacionVendedor"));
                    contract.setIdclient(rs.getInt("idcliente"));
                    contract.setCode(rs.getString("code"));
                    contract.setCity(rs.getInt("city"));
                    contract.setDepartment(rs.getInt("department"));
                    contract.setCapitatedContract(rs.getBoolean("capitatedContract"));
                    contract.setMonthlyValue(rs.getDouble("monthlyValue"));
                    
                    if(rs.getString("lab904c1") != null) {
                        RatesByContract rate = new RatesByContract();
                        rate.setRateId(rs.getInt("lab904c1"));
                        rate.setRateCode(rs.getString("lab904c2"));
                        rate.setNameRate(rs.getString("lab904c3"));
                        rate.setClientId(rs.getInt("lab14c1"));
                        rate.setContractId(rs.getInt("lab990c1"));
                        rate.setContractName(rs.getString("lab990c2"));
                        rate.setStatusType(rs.getShort("lab990c1"));
                        rate.setContractState(rs.getInt("lab07c1"));
                        contract.getRates().add(rate);
                    }
                    contracts.add(contract);
                }
                return contract;
            };

            getJdbcTemplate().query(query.toString(), mapper);

            return contracts;
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

}
