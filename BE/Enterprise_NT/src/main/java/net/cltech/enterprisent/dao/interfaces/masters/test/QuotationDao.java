/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.test.QuotationDetail;
import net.cltech.enterprisent.domain.masters.test.QuotationHeader;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author jrodriguez
 */
public interface QuotationDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    // GUARDA LAS CABECERAS DE LAS COTIZACIONES
    default int insertQuotations(QuotationHeader quotationHeader) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab101")
                .usingGeneratedKeyColumns("lab101c1");
        HashMap<String, Object> parameters = new HashMap<>(0);

        parameters.put("lab101c2", quotationHeader.getName().toLowerCase());
        parameters.put("lab904c1", quotationHeader.getRate().getId());
        parameters.put("lab101c3", quotationHeader.getDiscount());
        parameters.put("lab101c4", quotationHeader.getPriceTotal());
        parameters.put("lab04c1", quotationHeader.getUser());
        parameters.put("lab101c5", new Timestamp(quotationHeader.getDate().getTime()));
        parameters.put("lab101c6", quotationHeader.getTax());
        parameters.put("lab101c7", quotationHeader.getAddressCharge());
        return insert.executeAndReturnKey(parameters).intValue();

    }
    // GUARDA LOS DETALLES DE LAS COTIZACIONES

    default int insertQuotationDetail(QuotationHeader quotationHeader) throws Exception
    {

        List<HashMap> batchArray = new ArrayList<>();

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab102")
                .usingGeneratedKeyColumns("lab102c1");

        for (QuotationDetail quotationDetail1 : quotationHeader.getQuotationDetail())
        {
            quotationDetail1.setDate(new Date());
            HashMap parameters = new HashMap();
            parameters.put("lab101c1", quotationHeader.getId());
            parameters.put("lab39c1", quotationDetail1.getTest().getId());
            parameters.put("lab904c1", quotationDetail1.getTestRate().getId());
            parameters.put("lab102c3", quotationDetail1.getPrice());
            parameters.put("lab102c5", quotationDetail1.getCount());
            parameters.put("lab04c1", quotationHeader.getUser());
            parameters.put("lab102c4", new Timestamp(quotationDetail1.getDate().getTime()));
            batchArray.add(parameters);
        }
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[quotationHeader.getQuotationDetail().size()]));

        return inserted.length;
    }

    default List<QuotationHeader> getPatientBy(String name)
    {
        List<QuotationHeader> headers = new ArrayList<>();
        getJdbcTemplate().query(""
                + " SELECT lab101.lab101c1 "
                + " ,lab101.lab101c2"
                + " ,lab904.lab904c1"
                + " ,lab904.lab904c2"
                + " ,lab904.lab904c3"
                + " ,lab101.lab101c3"
                + " ,lab101.lab101c4"
                + " ,lab101.lab101c6"
                + " ,lab101.lab101c7"
                + " ,lab102.lab101c1"
                + " ,lab102.lab39c1"
                + " ,lab39c2"
                + " ,lab39c4"
                + " ,lab904.lab904c1"
                + " ,lab904.lab904c2"
                + " ,lab904.lab904c3 "
                + " ,lab102.lab102c3"
                + " ,lab102.lab102c4"
                + " ,lab102.lab04c1"
                + " FROM lab101 "
                + " INNER JOIN lab904 ON lab904.lab904c1 = lab101.lab904c1"
                + " INNER JOIN lab102 ON lab102.lab101c1 = lab101.lab101c1"
                + " INNER JOIN lab39 ON lab39.lab39c1 = lab102.lab39c1"
                + " WHERE lab101c2 LIKE  '%" + name.toLowerCase() + "%' ",
                (ResultSet rs) ->
        {
            //CABECERA DE LA COTIZACION
            QuotationHeader quotationHeader = new QuotationHeader();
            quotationHeader.setId(rs.getInt("lab101c1"));
            if (!headers.contains(quotationHeader))
            {
                quotationHeader.setName(rs.getString("lab101c2"));
                //Tarifa
                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode(rs.getString("lab904c2"));
                rate.setName(rs.getString("lab904c3"));
                quotationHeader.setRate(rate);

                quotationHeader.setDiscount(rs.getInt("lab101c3"));
                quotationHeader.setPriceTotal(rs.getInt("lab101c4"));
                quotationHeader.setTax(rs.getBigDecimal("lab101c6"));
                quotationHeader.setAddressCharge(rs.getBigDecimal("lab101c7"));
                quotationHeader.setQuotationDetail(new ArrayList<>());
                headers.add(quotationHeader);
            }
            quotationHeader = headers.get(headers.indexOf(quotationHeader));

            //DETALLE DE LA COTIZACION
            QuotationDetail quotationDetail = new QuotationDetail();
            quotationDetail.setIdHeader(rs.getInt("lab101c1"));

            //examen
            TestBasic test = new TestBasic();
            test.setId(rs.getInt("lab39c1"));
            test.setCode(rs.getString("lab39c2"));
            test.setName(rs.getString("lab39c4"));
            quotationDetail.setTest(test);

            //tarifa 
            Rate rateTest = new Rate();
            rateTest.setId(rs.getInt("lab904c1"));
            rateTest.setCode(rs.getString("lab904c2"));
            rateTest.setName(rs.getString("lab904c3"));
            quotationDetail.setTestRate(rateTest);

            quotationDetail.setPrice(rs.getBigDecimal("lab102c3"));
            quotationDetail.setDate(rs.getTimestamp("lab102c4"));
            //User
            User user = new User();
            user.setId(rs.getInt("lab04c1"));
            quotationDetail.setUser(rs.getInt("lab04c1"));

            quotationHeader.getQuotationDetail().add(quotationDetail);

        }
        );
        return headers;
    }

    default List<QuotationHeader> listQuotationHeader(Date init, Date end)
    {
        List<QuotationHeader> headers = new ArrayList<>();
        getJdbcTemplate().query(""
                + " SELECT lab101.lab101c1 "
                + " ,lab101.lab101c2"
                + " ,lab904.lab904c1"
                + " ,lab904.lab904c2"
                + " ,lab904.lab904c3"
                + " ,lab101.lab101c3"
                + " ,lab101.lab101c4"
                + " ,lab101.lab101c6"
                + " ,lab101.lab101c7"
                + " ,lab102.lab101c1"
                + " ,lab102.lab39c1"
                + " ,lab39c2"
                + " ,lab39c4"
                + " ,lab904.lab904c1"
                + " ,lab904.lab904c2"
                + " ,lab904.lab904c3 "
                + " ,lab102.lab102c3"
                + " ,lab102.lab102c4"
                + " ,lab102.lab102c5"
                + " ,lab102.lab04c1"
                + " FROM lab101 "
                + " INNER JOIN lab904 ON lab904.lab904c1 = lab101.lab904c1"
                + " INNER JOIN lab102 ON lab102.lab101c1 = lab101.lab101c1"
                + " INNER JOIN lab39 ON lab39.lab39c1 = lab102.lab39c1"
                + " WHERE lab102c4 BETWEEN ? AND ? ",
                (ResultSet rs) ->
        {
            //CABECERA DE LA COTIZACION
            QuotationHeader quotationHeader = new QuotationHeader();
            quotationHeader.setId(rs.getInt("lab101c1"));
            if (!headers.contains(quotationHeader))
            {
                quotationHeader.setName(rs.getString("lab101c2"));
                //Tarifa
                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode(rs.getString("lab904c2"));
                rate.setName(rs.getString("lab904c3"));
                quotationHeader.setRate(rate);

                quotationHeader.setDiscount(rs.getInt("lab101c3"));
                quotationHeader.setPriceTotal(rs.getInt("lab101c4"));
                quotationHeader.setQuotationDetail(new ArrayList<>());
                quotationHeader.setTax(rs.getBigDecimal("lab101c6"));
                quotationHeader.setAddressCharge(rs.getBigDecimal("lab101c7"));
                headers.add(quotationHeader);
            }
            quotationHeader = headers.get(headers.indexOf(quotationHeader));

            //DETALLE DE LA COTIZACION
            QuotationDetail quotationDetail = new QuotationDetail();
            quotationDetail.setIdHeader(rs.getInt("lab101c1"));

            //examen
            TestBasic test = new TestBasic();
            test.setId(rs.getInt("lab39c1"));
            test.setCode(rs.getString("lab39c2"));
            test.setName(rs.getString("lab39c4"));
            quotationDetail.setTest(test);

            //tarifa 
            Rate rateTest = new Rate();
            rateTest.setId(rs.getInt("lab904c1"));
            rateTest.setCode(rs.getString("lab904c2"));
            rateTest.setName(rs.getString("lab904c3"));
            quotationDetail.setTestRate(rateTest);

            quotationDetail.setPrice(rs.getBigDecimal("lab102c3"));
            quotationDetail.setCount(rs.getInt("lab102c5"));
            quotationDetail.setDate(rs.getTimestamp("lab102c4"));
            //User
            User user = new User();
            user.setId(rs.getInt("lab04c1"));
            quotationDetail.setUser(rs.getInt("lab04c1"));

            quotationHeader.getQuotationDetail().add(quotationDetail);

        }, init, end);
        return headers;
    }
}
