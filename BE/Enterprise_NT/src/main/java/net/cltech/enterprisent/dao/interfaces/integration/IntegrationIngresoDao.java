package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.domain.integration.ingreso.RequestArea;
import net.cltech.enterprisent.domain.integration.ingreso.RequestItemCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestSample;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTest;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTestStatus;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsItemSample;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsSonControl;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a la base de datos para la informacion de
 * los demograficos y examenes
 *
 * @version 1.0.0
 * @author javila
 * @since 05/02/2020
 * @see Creación
 */
public interface IntegrationIngresoDao
{

    /**
     * Este metodo se encargara de consultar con id del examen que tipo de
     * prueba es:
     *
     * 0 -> Examen 1 -> Perfiles 2 -> Paquetes
     *
     * @param idTest
     * @return
     * @throws Exception Error en la base de datos
     */
    default int identifyTestType(int idTest) throws Exception
    {
        try
        {
            String query = "SELECT lab39c37 FROM lab39 WHERE lab39c1 = ?";
            return getJdbcTemplate().queryForObject(query,
                    ((rs, i)
                    ->
            {
                return rs.getInt("lab39c37");
            }), idTest);
        } catch (DataAccessException e)
        {
            OrderCreationLog.error(e);
            // Retorno 3 con el fin de saber que la consulta fallo
            return 3;
        }
    }

    /**
     * Actualizar codigo central
     *
     * @param item
     * @param order
     * @param typeTest
     * @return True si se actualizo satisfactoriamente, false si por el
     * contrario esta actualizacion no se hizo
     * @throws java.lang.Exception
     */
    default int updateCentralCode(RequestItemCentralCode item, long order, int typeTest) throws Exception
    {
        try
        {
            int idTest = item.getIdTest();
            String centralCodes = item.getCentralCode();

            StringBuilder update = new StringBuilder();
            update.append("UPDATE lab57 SET lab57c49 = ?").append(" WHERE lab22c1 = ").append(order).append(" AND lab39c1 = ").append(idTest);
            return getJdbcTemplate().update(update.toString(), centralCodes);
        } catch (DataAccessException e)
        {
            OrderCreationLog.error(e);
            return 0;
        }
    }
    /**
     * Actualiza el codigo cup de un examen
     *
     * @param order
     * @return True si se actualizo satisfactoriamente, false si por el
     * contrario esta actualizacion no se hizo
     * @throws java.lang.Exception
     */
    default int updateTestCups(long order) throws Exception
    {
        try
        {
            
            StringBuilder update = new StringBuilder();
            update.append("UPDATE lab57 SET lab57c49 = lab61.lab61c1 ")
                    .append("FROM lab61 ")
                    .append("WHERE lab61.lab39c1 = lab57.lab39c1 ")
                    .append("AND lab57c49 IS NULL ");
            return getJdbcTemplate().update(update.toString());
        } catch (DataAccessException e)
        {
            OrderCreationLog.error(e);
            return 0;
        }
    }

    /**
     * Obtener muestras por id de la orden y por el id del examen
     *
     * @param idOrder
     * @param idSample
     * @return
     * @throws Exception Error en la base de datos
     */
    default List<ResponsItemSample> getSampleByIdOrderAndIdTest(long idOrder, int idSample, int idProfile) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
//            query.append("SELECT ")
//                    .append(" M.lab24c1 AS lab24c1,")
//                    .append(" M.lab24c2 AS lab24c2,")
//                    .append(" M.lab24c3 AS lab24c3,")
//                    .append(" M.lab24c9 AS lab24c9,")
//                    .append(" E.lab39c3 AS lab39c3,")
//                    .append(" T.lab56c2 AS lab56c2")
//                    .append(" FROM lab57 R")
//                    .append(" INNER JOIN lab39 E ON (R.lab39c1 = E.lab39c1) ")
//                    .append("INNER JOIN lab24 M ON (E.lab24c1 = M.lab24c1) ")
//                    .append("INNER JOIN lab56 T ON (M.lab56c1 = T.lab56c1) ")
//                    .append("WHERE R.lab22c1 = ? AND M.Lab24C1 = ? AND M.lab24c3 <> 0");
            query.append("SELECT ")
                    .append(" M.lab24c1 AS lab24c1,")
                    .append(" M.lab24c2 AS lab24c2,")
                    .append(" M.lab24c3 AS lab24c3,")
                    .append(" M.lab24c9 AS lab24c9,")
                    .append(" E.lab39c3 AS lab39c3,")
                    .append(" T.lab56c2 AS lab56c2")
                    .append(" FROM lab57 R")
                    .append(" INNER JOIN lab39 E ON (R.lab39c1 = E.lab39c1) ")
                    .append("INNER JOIN lab24 M ON (").append(idSample).append(" = M.lab24c1) ")
                    .append("INNER JOIN lab56 T ON (M.lab56c1 = T.lab56c1) ")
                    .append("WHERE R.lab22c1 = ").append(idOrder)
                    .append(" AND M.Lab24C1 = ").append(idSample)
                    .append(" AND M.lab24c3 <> 0")
                    .append(" AND E.lab39c1 = ").append(idProfile);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                ResponsItemSample itemSample = new ResponsItemSample();
                itemSample.setIdSample(rs.getInt("lab24c1"));
                itemSample.setLab24c2(rs.getString("lab24c2"));
                itemSample.setLab24c3(rs.getInt("lab24c3"));
                itemSample.setLab24c9(rs.getString("lab24c9"));
                itemSample.setLab56c2(rs.getString("lab56c2"));
                itemSample.setAbreviaturaExamen(rs.getString("lab39c3"));
                return itemSample;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtener Abreciatura del examen
     *
     * @param idTest
     * @return
     * @throws Exception Error en la base de datos
     */
    default String getAbbreviationByIdTest(int idTest) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c3")
                    .append(" FROM lab39")
                    .append(" WHERE lab39c1 = ?");

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getString("lab39c3");
            }, idTest);
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtendra la informacion de la tabla de control por id orden
     *
     * @param idOrder
     * @return
     * @throws Exception Error al retornar los datos de la tabla de control
     */
    default ResponsSonControl ordersSonControl(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Orden, ")
                    .append("Cuenta, ")
                    .append("Mensaje, ")
                    .append("Estado, ")
                    .append("Fecha, ")
                    .append("Perfiles ")
                    .append(" FROM SON_Control ")
                    .append(" WHERE  Orden = ").append(idOrder);

            return getConnectionCont().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                ResponsSonControl objControl = new ResponsSonControl();
                objControl.setIdOrder(rs.getLong("Orden"));
                objControl.setCuenta(rs.getString("Cuenta"));
                objControl.setMensaje(rs.getString("Mensaje"));
                objControl.setEstado(rs.getInt("Estado"));
                objControl.setFecha(DateTime.parse(rs.getDate("Fecha").toString()));
                objControl.setPerfiles(rs.getString("Perfiles") == null ? "" : rs.getString("Perfiles"));
                return objControl;
            });

        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtendra los codigos de homologación de todos los examenes relacionados
     * con una muestra
     *
     * @param idSample
     * @param centralSystem
     *
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    default List<String> homologationCodesForSample(int idSample, int centralSystem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(" SELECT DISTINCT HT.lab61c1 AS lab61c1  FROM Lab39 T ")
                    .append(" LEFT JOIN lab46 Con ON (Con.lab46c1 =  T.lab39c1) ")
                    .append(" LEFT JOIN Lab39 P ON(P.lab39c37 = 1 AND P.lab39c1 = Con.lab39c1) ")
                    .append(" JOIN lab24 Samp ON (T.lab24c1 = Samp.lab24c1) ")
                    .append(" JOIN lab61 HT ON ((T.lab39c1 = HT.lab39c1 OR P.lab39c1 = HT.lab39c1) AND HT.lab118c1 = ").append(centralSystem).append(")")
                    .append(" WHERE HT.lab61c1 IS NOT NULL AND Samp.lab24c1 = ").append(idSample);
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getString("lab61c1"); //To change body of generated lambdas, choose Tools | Templates.
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtendra los codigos de homologación de todos los examenes por su id
     *
     * @param idTest
     * @param centralSystem
     *
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    default List<String> homologationCodesForIdTest(String idTest, int centralSystem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT HT.lab61c1 AS lab61c1 FROM lab39 Test")
                    .append(" JOIN lab61 HT ON (Test.lab39c1 = HT.lab39c1 AND HT.lab118c1 = ").append(centralSystem).append(")")
                    .append(" WHERE HT.lab61c1 IS NOT NULL AND Test.Lab39C1 IN(").append(idTest).append(")");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getString("lab61c1"); //To change body of generated lambdas, choose Tools | Templates.
            });
        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            return null;
        }
    }

    /**
     * Actualizar estado de la orden de la tabla de control chek-in
     *
     * @param order
     * @param state
     * @return True si se actualizo satisfactoriamente, false si por el
     * contrario esta actualizacion no se hizo
     * @throws java.lang.Exception
     */
    default int updateStateOrder(long order, int state) throws Exception
    {
        try
        {
            StringBuilder update = new StringBuilder();
            update.append("UPDATE son_control SET Estado = ").append(state);
            update.append(" WHERE Orden = ").append(order);
            return getConnectionCont().update(update.toString());
        } catch (DataAccessException e)
        {
            return 0;
        }
    }

    /**
     * Obtenemos el codigo de homologación del examen y la cantidad de este
     *
     * @param idTest
     * @param idOrder
     *
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    default String getHomologationCodeAndQuantity(int idTest, long idOrder) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57c49 ")
                    .append("FROM  ").append(lab57).append(" as lab57 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab39c1 = ").append(idTest);
            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getString("lab57c49");
            });
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtenemos los codigos de homologacion de los demograficos
     *
     * @param idCentralSystem
     *
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    default List<ResponsDemographicIngreso> getDemographicCentralSystem(int idCentralSystem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab117c1, lab117c2, lab117c3 ")
                    .append("FROM lab117 ")
                    .append("WHERE lab118c1 = ").append(idCentralSystem);
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                ResponsDemographicIngreso objControl = new ResponsDemographicIngreso();
                objControl.setIdDemographic(rs.getInt("lab117c1"));
                objControl.setIdItemDemographicLis(rs.getInt("lab117c2"));
                objControl.setIdItemDemographicHis(rs.getString("lab117c3"));

                return objControl;
            });
        } catch (Exception e)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtenemos la cantidad de estudios que se han enviado al HIS para esa
     * orden
     *
     * @param idOrden
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    default int getStudiesSent(long idOrden) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Estudios ")
                    .append("FROM son_control ")
                    .append("WHERE Orden = ").append(idOrden);
            return getConnectionCont().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("Estudios");
            });
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Actualizamos la cantidad de estudios que se han enviado al HIS para esa
     * orden
     *
     * @param idOrden
     * @param shippedQuantity
     * @throws Exception error al cargar algn componente del objeto
     */
    default void updateStudiesSent(long idOrden, int shippedQuantity) throws Exception
    {
        getConnectionCont().update("UPDATE son_control "
                + "SET Estudios = ? "
                + "WHERE Orden = ?",
                shippedQuantity,
                idOrden);
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param order Numero de la orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<Test> getListTest(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57.lab22c1")
                    .append(", lab57.lab39c1") //examen
                    .append(", lab57.lab57c39") //Fecha Toma
                    .append(", lab39.lab39c2") //código
                    .append(", lab39.lab39c4") //nombre
                    .append(", lab39.lab39c37 AS testType") //Tipo de examen
                    .append(", lab40.lab40c1") //id del laboratorio
                    .append(", lab40.lab40c10") //url del laboratorio
                    .append(", lab40.lab40c11") //Si se valida al ingreso
                    .append(", lab40.lab40c12") //Si se valida en verificacion
                    .append(", lt.lab24c1 AS ltlab24c1") //muestra id
                    .append(", lt.lab24c9 AS ltlab24c9") //muestra codigo
                    .append(", lt.lab24c2 AS ltlab24c2") //muestra nombre
                    .append(", lt.lab24c10") //tipo laboratorio
                    .append(", lab46.lab39c1 AS profileID ") //Id del perfil
                    .append(", lab16.lab16c3, lab11.lab11c1 ")
                    .append(" FROM lab57") //Resultados
                    .append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1") //Examen
                    .append(" LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1") // Laboratorio
                    .append(" LEFT JOIN lab46 ON lab46.lab46c1 = lab39.lab39c1 ") // Perfiles
                    .append(" INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1") // Muestra                    
                    .append(" LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ")
                    .append(" WHERE lab57.lab22c1 = ? ");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setProfile(rs.getInt("profileID"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                test.setTestType(rs.getShort("testType"));
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setUrl(rs.getString("lab40c10"));
                laboratory.setEntry(rs.getBoolean("lab40c11"));
                laboratory.setCheck(rs.getBoolean("lab40c12"));
                test.setLaboratory(laboratory);
                Sample sample = new Sample();
                sample.setId(rs.getInt("ltlab24c1"));
                sample.setCodesample(rs.getString("ltlab24c9"));
                sample.setName(rs.getString("ltlab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTakeDate(rs.getTimestamp("lab57c39"));
                test.setSample(sample);
                return test;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default String obtainNameApprovedReferringPhysician(Integer idDemographic, String homologationCode) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab63.lab63c3 FROM lab117 ")
                    .append("JOIN lab63 ON lab63.lab63c1 = lab117.lab117c2 ")
                    .append("WHERE lab117.lab118c1 = ").append(1)
                    .append(" AND lab117.lab117c1 = ").append(idDemographic)
                    .append(" AND lab117c3 = '").append(homologationCode).append("'");

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getString("lab117c3");
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return "";
        }
    }

    default DemographicItem obtainReferringPhysician(String name) throws Exception
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
                    + ", lab63.lab62c1"
                    + ", lab62.lab62c2"
                    + ", lab63.lab04c1"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab62.lab63c1 AS item"
                    + "  FROM lab63"
                    + "  INNER JOIN lab62 ON lab62.lab62c1=lab63.lab62c1"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1 = lab63.lab04c1";
            //where
            if (name != null)
            {
                query += !query.contains("WHERE") ? " WHERE UPPER(lab63.lab63c3) = ? " : " AND UPPER(lab63.lab63c3) = ? ";
            }

            List object = new ArrayList(0);
            if (name != null)
            {
                object.add(name.toUpperCase());
            }

            return getJdbcTemplate().queryForObject(query,
                    object.toArray(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItem demographicItem = new DemographicItem();

                demographicItem.setId(rs.getInt("lab63c1"));
                demographicItem.setCode(rs.getString("lab63c2"));
                demographicItem.setName(rs.getString("lab63c3"));
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
            return null;
        }
    }

    default List<DemographicItem> obtainNamesApprovedReferringPhysician(int centralSystem, int idDemographic) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab63.lab63c1, lab63.lab63c3, lab63.lab62c1, lab63.lab63c2 FROM lab117 ")
                    .append("JOIN lab63 ON lab63.lab63c1 = lab117.lab117c2 ")
                    .append("WHERE lab117.lab118c1 = ").append(centralSystem)
                    .append(" AND lab117.lab117c1 = ").append(idDemographic);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                DemographicItem demo = new DemographicItem();
                demo.setId(rs.getInt("lab63.lab63c1"));
                demo.setName(rs.getString("lab63.lab63c3"));
                demo.setDemographic(rs.getInt("lab63.lab62c1"));
                demo.setCode(rs.getString("lab63.lab63c2"));
                return demo;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Retorna el objeto RequestTest
     *
     * @param idTest
     * @param idBranch
     * @param orderType
     *
     * @return Examen
     * @throws Exception Error en la base de datos.
     */
    default RequestTest getTestsByOrderHIS(int idTest, int idBranch, int orderType) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39.lab39c1 AS idTest, lab39c2 as cod, lab39c4 as nametest,  lab39.lab39c37 as tipoexamen, lab39.lab39c3 as abbrexamen, ")
                    .append("lab39.lab39c20 as price, lab39.lab39c25 as lab39c25, lab43.lab43c1 as idarea, lab43.lab43c2 as ordenamiento, ")
                    .append("lab43.lab43c3 as abreviatura, lab43.lab43c4 as nombrearea, lab43.lab43c5 as color, ")
                    .append("lab43.lab07c1 as statearea, lab43.lab43c8 as partialvalidation, ")
                    .append("(SELECT DISTINCT lab145.lab40c1 FROM lab145 WHERE lab145.lab39c1 = ").append(idTest).append(" AND lab145.lab05c1 = ").append(idBranch).append(" AND lab145.lab145c1 = ").append(orderType).append(") AS idLaboratory, ")
                    .append("lab24.lab24c1 as idsample, lab24.lab24c9 as codsample ")
                    .append("FROM lab39 ")
                    .append("INNER JOIN lab43 ON lab39.lab43c1 = lab43.lab43c1 ")
                    .append("LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 ")
                    .append("WHERE lab39.lab39c1 = ").append(idTest);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {

                RequestTest request = new RequestTest();
                request.setId(rs.getInt("idTest"));
                request.setCode(rs.getString("cod"));
                request.setName(rs.getString("nametest"));
                request.setTestType(rs.getInt("tipoexamen"));
                request.setIdLaboratory(rs.getInt("idLaboratory"));

                // Tarifa
                //RequestRate rate = new RequestRate();
                //rate.setId(0);
                //request.setRate(rate);
                // Area
                RequestArea area = new RequestArea();
                area.setId(rs.getInt("idarea"));
                area.setOrdering(rs.getInt("ordenamiento"));
                area.setAbbreviation(rs.getString("abreviatura"));
                area.setName(rs.getString("nombrearea"));
                area.setColor(rs.getString("color"));
                area.setState(rs.getBoolean("statearea"));
                area.setPartialValidation(rs.getBoolean("partialvalidation"));
                request.setArea(area);
                request.setPrice(rs.getInt("price"));
                request.setPrint(rs.getInt("lab39c25"));
                request.setAbbr(rs.getString("abbrexamen"));

                // Muestra
                RequestSample sample = new RequestSample();
                sample.setId(rs.getInt("idsample"));
                sample.setCodesample(rs.getString("codsample"));
                request.setSample(sample);
                request.setProfile(rs.getInt("tipoexamen"));
                return request;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Actualizamos el campo de Perfiles de la tabla de control con todos Y cada
     * uno de los perfiles que se han enviado al HIS con sus respectivos estados
     *
     * @param idOrden
     * @param profiles
     * @throws Exception de base de datos
     */
    default void updateOrderProfiles(long idOrden, String profiles) throws Exception
    {
        getConnectionCont().update("UPDATE son_control "
                + "SET Perfiles = ? "
                + "WHERE Orden = ?",
                profiles,
                idOrden);
    }
    
    /**
     * Se consulta si ya existe el estado para el examen
     * 
     * @param orderId id de la orden
     * @param code Codigo de homologacion
     * 
     * @return Booleano -> Existencia del estado
     * @throws java.lang.Exception
     */
    default List<RequestTestStatus> listStatusByTest(Long orderId, String code) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            
            query.append(" SELECT cont03c1")
                    .append(", cont03c2")
                    .append(", cont03c4")
                    .append(", cont03c5")
                    .append(", cont03c6")
                    .append(", cont03c7")
                    .append(", cont03c8 ");
            
            StringBuilder from = new StringBuilder();
            from.append(" FROM cont03 ");
            
            from.append(" WHERE cont03c1 = ").append(orderId);
            from.append(" AND cont03c6 = ").append(code);
            
            List<RequestTestStatus> list = new LinkedList<>();
            
            RowCallbackHandler handler = (ResultSet rs)
                    ->
            {
                RequestTestStatus requestTestStatus = new RequestTestStatus();
                requestTestStatus.setOrder(rs.getLong("cont03c1"));
                requestTestStatus.setAccountNumber(rs.getString("cont03c2"));
                requestTestStatus.setStatus(rs.getString("cont03c4"));
                requestTestStatus.setSendingDate(rs.getTimestamp("cont03c5"));
                requestTestStatus.setHomologationCode(rs.getString("cont03c6"));
                requestTestStatus.setNumberSending(rs.getInt("cont03c7"));
                requestTestStatus.setSendWithError(rs.getInt("cont03c8"));
                list.add(requestTestStatus);
            };
            getConnectionCont().query(query.toString() + from.toString(), handler);
            return list;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Se controla el estado enviado para un examen
     * 
     * @param requestTestStatus 
     * 
     * @return 
     * @throws java.lang.Exception
     */
    default boolean createStatusByTest(RequestTestStatus requestTestStatus)throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnectionCont())
                    .withTableName("cont03")
                    .usingColumns("cont03c1", "cont03c2", "cont03c4", "cont03c5", "cont03c6", "cont03c7", "cont03c8");

            HashMap parameters = new HashMap();

            parameters.put("cont03c1", requestTestStatus.getOrder());
            parameters.put("cont03c2", requestTestStatus.getAccountNumber());
            parameters.put("cont03c4", requestTestStatus.getStatus());
            parameters.put("cont03c5", timestamp);
            parameters.put("cont03c6", requestTestStatus.getHomologationCode());
            parameters.put("cont03c7", requestTestStatus.getNumberSending());
            parameters.put("cont03c8", requestTestStatus.getSendWithError());

            return insert.execute(parameters) == 1;
        }
        catch (Exception e)
        {
            IntegrationHisLog.info("ERROR insertando" + e);
            return false;
        }
        
    }
    
     /**
     * Actualizamos el campo de Perfiles de la tabla de control con todos Y cada
     * uno de los perfiles que se han enviado al HIS con sus respectivos estados
     *
     * @param idOrden
     * @param test
     * @param numersending
     * @param errorsend
     * @throws Exception de base de datos
     */
    default void updateStateTest(long idOrden, String test, int numersending, int errorsend) throws Exception
    {
        try{
            getConnectionCont().update("UPDATE cont03 "
                + "SET cont03c7 = ? "
                + "SET cont03c8 = ? "
                + "WHERE cont03c1 = ? AND cont03c6 = ?  " ,
                numersending,errorsend,idOrden,test);
        }
        catch(DataAccessException e){
            IntegrationHisLog.info("ERROR ACTUALIZANDO" + e);
        }
    }

    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene la conexión a la base de datos de Control
     *
     * @return jdbcCont
     */
    public JdbcTemplate getConnectionCont();
}
