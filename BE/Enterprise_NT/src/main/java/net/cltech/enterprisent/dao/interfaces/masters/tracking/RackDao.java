package net.cltech.enterprisent.dao.interfaces.masters.tracking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.masters.tracking.Refrigerator;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.tracking.DisposalCertificate;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/05/2018
 * @see Creación
 */
public interface RackDao
{

    /**
     * Lista gradillas desde la base de datos.
     *
     * @return Lista de gradillas.
     * @throws Exception Error en la base de datos.
     */
    default List<Rack> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + " SELECT lab16c1, lab16c2,lab16c3,lab16c4,lab16c5,lab16c6,lab16c7, lab16.lab07c1"
                    + " , lab16.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + " , lab05.lab05c1, lab05c2, lab16c9, lab16c8, lab16c10 "
                    + " , lab31.lab31c1, lab31c2 "
                    + " FROM lab16 "
                    + " INNER JOIN lab05 ON lab05.lab05c1 = lab16.lab05c1 "
                    + " LEFT JOIN lab31 on lab31.lab31c1 = lab16.lab31c1  "
                    +  "LEFT JOIN lab04 ON lab04.lab04c1 = lab16.lab04c1", (ResultSet rs, int i) ->
            {
                Rack bean = new Rack();
                bean.setId(rs.getInt("lab16c1"));
                bean.setCode(rs.getString("lab16c2"));
                bean.setName(rs.getString("lab16c3"));
                bean.setType(rs.getInt("lab16c4"));
                bean.setRow(rs.getInt("lab16c5"));
                bean.setColumn(rs.getInt("lab16c6"));
                bean.setLastTransaction(rs.getTimestamp("lab16c7"));
                bean.setState(rs.getInt("lab07c1"));
                bean.setReusable((rs.getInt("lab16c9") == 1));
                bean.setFloor(rs.getString("lab16c8"));
                bean.setCloseDate(rs.getTimestamp("lab16c10"));

                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                bean.setBranch(new Branch(rs.getInt("lab05c1")));
                
                if (rs.getString("lab31c1") != null)
                {
                    bean.setRefrigerator(new Refrigerator());
                    bean.getRefrigerator().setId(rs.getInt("lab31c1"));
                    bean.getRefrigerator().setName(rs.getString("lab31c2"));
                }
                
                return bean;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra gradilla en la base de datos.
     *
     * @param create Instancia con los datos de gradilla.
     *
     * @return Instancia con los datos de gradilla.
     * @throws Exception Error en la base de datos.
     */
    default Rack create(Rack create) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab16")
                .usingGeneratedKeyColumns("lab16c1");

        HashMap parameters = new HashMap();
        parameters.put("lab16c3", create.getName().trim());
        parameters.put("lab16c4", create.getType());
        parameters.put("lab16c2", create.getCode());
        parameters.put("lab16c5", create.getRow());
        parameters.put("lab16c6", create.getColumn());
        parameters.put("lab16c7", timestamp);
        parameters.put("lab04c1", create.getUser().getId());
        parameters.put("lab07c1", 0);
        parameters.put("lab05c1", create.getBranch().getId());
        // Reusabilidad de las gradillas
        parameters.put("lab16c9", create.getReusable() == true ? 1 : 0);

        Number key = insert.executeAndReturnKey(parameters);
        create.setId(key.intValue());
        create.setLastTransaction(timestamp);
        create.setState(0);

        return create;
    }

    /**
     * Actualiza la información de gradilla en la base de datos.
     *
     * @param update Instancia con los datos de gradilla.
     *
     * @return Objeto de gradilla modificada.
     * @throws Exception Error en la base de datos.
     */
    default Rack update(Rack update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab16 SET "
                + "lab16c2 = ?, lab16c3 = ?, lab16c4 = ?, lab05c1 = ?, lab16c5 =?, lab16c6= ?, lab16c9 = ? "
                + "WHERE lab16c1 = ?",
                update.getCode(), update.getName().trim(), update.getType(), update.getBranch().getId(), update.getRow(), update.getColumn(), update.getReusable() == true ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);
        return update;
    }

    /**
     * Actualiza la información de gradilla para el cierre en la base de datos.
     *
     * @param update Instancia con los datos de gradilla.
     *
     * @return Objeto de gradilla modificada.
     * @throws Exception Error en la base de datos.
     */
    default int close(Rack update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return getConnection().update("UPDATE lab16 SET "
                + "lab16c8 = ?, lab04c1 = ?, lab07c1 = ?,lab31c1  = ?, lab16c10 = ? "
                + "WHERE lab16c1 = ?",
                update.getFloor(), update.getUpdateUser().getId(), 1, update.getRefrigerator().getId(), timestamp, update.getId());
    }

    /**
     * Marca las gradillas como desechadas
     *
     * @param racks lista de id´s de las gradillas
     * @param user id del usuario
     * @return registros afectados
     * @throws Exception
     */
    default int dispouse(List<Integer> racks, int user) throws Exception
    {
        return getConnection().update("UPDATE lab16 SET "
                + "lab04c1 = ?, lab07c1 = ?  "
                + "WHERE lab16c1 in ("
                + racks.stream().map(rack -> rack.toString()).collect(Collectors.joining(",")) + ")",
                user, 2);
    }

    /**
     * Lista los exámenes de una muestra
     *
     * @param order numero de orden
     * @param sample id de la muestra
     * @return
     * @throws Exception
     */
    default List<ResultTest> listSampleTests(long order, String sample) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            
            return getConnection().query(""
                    + "SELECT lab39.lab39c1, lab57c8,lab39c27, lab24.lab24c1, lab24c7, lab24c14 "
                    + "FROM " + lab57 + " as lab57 " 
                    + "LEFT JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "LEFT JOIN lab24 ON lab57.lab24c1 = lab24.lab24c1 "
                    + "WHERE lab22c1 = ? AND lab24c9 =  ? AND lab57c16 = ? and lab39c37 = 0",
                    (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setConfidential(rs.getInt("lab24c14") == 1);
                bean.setSampleId(rs.getInt("lab24c1"));
                bean.setStorageDays(rs.getInt("lab24c7"));
                return bean;
            }, order, sample, LISEnum.ResultSampleState.CHECKED.getValue());
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el detalle de una gradilla
     *
     * @param rack id gradilla
     * @return lista de gradillas
     * @throws Exception
     */
    default List<RackDetail> listRackDetail(int rack) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab11.lab11c1 AS lab11c1, ")
                    .append("lab11.lab16c1 AS lab16c1, ")
                    .append("lab11.lab22c1 AS lab22c1, ")
                    .append("lab11.lab11c2 AS lab11c2, ")
                    .append("lab11.lab11c3 AS lab11c3, ")
                    .append("lab11.lab11c4 AS lab11c4, ")
                    .append("lab11.lab11c5 AS lab11c5, ")
                    .append("lab11.lab04c1 AS lab04c1, ")
                    .append("lab11.lab04c1_1 AS lab04c1_1, ")
                    .append("lab24.lab24c1 AS lab24c1, ")
                    .append("lab24.lab24c9 AS lab24c9, ")
                    .append("a.lab04c2 as updateName, ")
                    .append("a.lab04c3 as updateLast, ")
                    .append("a.lab04c4 as updateUser, ")
                    .append(" lab183.lab27c1 ")
                    .append("FROM lab11 ")
                    .append("INNER JOIN lab24 on lab24.lab24c1 = lab11.lab24c1 ")
                    .append("LEFT JOIN lab04 a on a.lab04c1 = lab11.lab04c1_1 ")
                    .append("LEFT JOIN lab183 ON lab183.lab24c1 = lab11.lab24c1 AND lab183.lab22c1 = lab11.lab22c1 ")
                    .append("WHERE lab11.lab16c1 = ").append(rack);
            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                RackDetail bean = new RackDetail();
                Rack rackOne = new Rack();
                Sample sample = new Sample();
                AuthorizedUser authorizedUser = new AuthorizedUser();
                bean.setPosition(rs.getString("lab11c1"));
                rackOne.setId(rs.getInt("lab16c1"));
                bean.setRack(rackOne);
                sample.setId(rs.getInt("lab24c1"));
                sample.setCodesample(rs.getString("lab24c9"));
                bean.setSample(sample);
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setRegistDate(rs.getTimestamp("lab11c2"));
                bean.setUpdateDate(rs.getTimestamp("lab11c3"));
                bean.setValidStorageDate(rs.getTimestamp("lab11c5"));
                bean.setInsert(rs.getInt("lab11c4") == 1);
                authorizedUser.setId(rs.getInt("lab04c1"));
                authorizedUser.setUpdateUserId(rs.getInt("lab04c1_1"));
                authorizedUser.setName(rs.getString("updateName"));
                authorizedUser.setLastName(rs.getString("updateLast"));
                authorizedUser.setUserName(rs.getString("updateUser"));
                bean.setRegistUser(authorizedUser);
                bean.setUpdateUser(authorizedUser);
                bean.setDiscard(false);
                //bean.setDiscard(rs.getInt("lab27c1") > 0);
                return bean;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta detalle de la gradilla
     *
     * @param rackDetail
     * @return
     * @throws Exception
     */
    default RackDetail insertRackDetail(RackDetail rackDetail) throws Exception
    {
        Timestamp timestamp = new Timestamp(rackDetail.getRegistDate().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab11");

        HashMap parameters = new HashMap();
        parameters.put("lab11c1", rackDetail.getPosition());
        parameters.put("lab16c1", rackDetail.getRack().getId());
        parameters.put("lab24c1", rackDetail.getSample().getId());
        parameters.put("lab22c1", rackDetail.getOrder());
        parameters.put("lab11c2", timestamp);
        parameters.put("lab04c1", rackDetail.getRegistUser().getId());
        parameters.put("lab11c4", 1);//muestra almacenada
        parameters.put("lab11c5", rackDetail.getValidStorageDate());
        insert.execute(parameters);
        return rackDetail;
    }

    /**
     * Obtiene la ubicación de la muestra
     *
     * @param order
     * @param sample
     * @return lista de gradillas
     * @throws Exception
     */
    default List<RackDetail> findSample(long order, String sample) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab11c1, lab22c1,lab11c2,lab11c3,lab11c4,lab11.lab04c1_1 "
                    + ",lab16.lab16c1, lab16c3,lab16c4,lab16c5,lab16c6,lab16c7,lab16c8, lab16.lab07c1 "
                    + ",lab24.lab24c1, lab24c2, lab24c9 "
                    + ",lab11.lab04c1, lab04.lab04c4 "
                    + ",lab31.lab31c1, lab31c2 "
                    + ",lab05.lab05c1, lab05c2, lab05c10, lab05c4 "
                    + ",lab11.lab04c1_1, a.lab04c4 as updateUser "
                    + "FROM lab11 "
                    + "INNER JOIN lab24 on lab24.lab24c1 = lab11.lab24c1 "
                    + "INNER JOIN lab16 on lab16.lab16c1 = lab11.lab16c1 "
                    + "INNER JOIN lab05 on lab05.lab05c1 = lab16.lab05c1 "
                    + "LEFT JOIN lab31 on lab31.lab31c1 = lab16.lab31c1  "
                    + "LEFT JOIN lab04 on lab04.lab04c1 = lab11.lab04c1  "
                    + "LEFT JOIN lab04 a on a.lab04c1 = lab11.lab04c1_1 "
                    + "WHERE lab22c1 = ? AND lab24c9 = ? ",
                    (ResultSet rs, int i) ->
            {
                RackDetail bean = new RackDetail();
                bean.setSample(new Sample(rs.getInt("lab24c1")));
                bean.getSample().setName(rs.getString("lab24c2"));
                bean.getSample().setCodesample(rs.getString("lab24c9"));
                bean.setOrder(rs.getLong("lab22c1"));

                bean.setPosition(rs.getString("lab11c1"));
                bean.setRack(new Rack(rs.getInt("lab16c1")));
                bean.getRack().setName(rs.getString("lab16c3"));
                bean.getRack().setType(rs.getInt("lab16c4"));
                bean.getRack().setFloor(rs.getString("lab16c8"));
                bean.getRack().setRow(rs.getInt("lab16c5"));
                bean.getRack().setColumn(rs.getInt("lab16c6"));
                bean.getRack().setState(rs.getInt("lab07c1"));
                if (rs.getString("lab31c1") != null)
                {
                    bean.getRack().setRefrigerator(new Refrigerator());
                    bean.getRack().getRefrigerator().setId(rs.getInt("lab31c1"));
                    bean.getRack().getRefrigerator().setName(rs.getString("lab31c2"));
                }

                bean.setBranch(new Branch(rs.getInt("lab05c1")));
                bean.getBranch().setAbbreviation(rs.getString("lab05c2"));
                bean.getBranch().setName(rs.getString("lab05c4"));
                bean.getBranch().setCode(rs.getString("lab05c10"));
                bean.setRegistDate(rs.getTimestamp("lab11c2"));
                bean.setUpdateDate(rs.getTimestamp("lab11c3"));
                bean.setInsert(rs.getInt("lab11c4") == 1);
                bean.setRegistUser(new AuthorizedUser(rs.getInt("lab04c1")));
                bean.getRegistUser().setUserName(rs.getString("lab04c4"));

                bean.setUpdateUser(new AuthorizedUser(rs.getInt("lab04c1_1")));
//                bean.getUpdateUser().setName(rs.getString("updateName"));
//                bean.getUpdateUser().setLastName(rs.getString("updateLast"));
                bean.getUpdateUser().setUserName(rs.getString("updateUser"));
                return bean;
            }, order, sample);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene mustra de la gradilla y la posicion enviada
     *
     * @param rackId id gradilla
     * @param position posicion en la gradilla
     * @return
     * @throws Exception
     */
    default RackDetail getSampleDetail(int rackId, String position) throws Exception
    {
        try
        {
            return getConnection().queryForObject(""
                    + "SELECT lab11c1, lab22c1,lab11c2,lab11c3,lab11c4,lab11c5 "
                    + ",lab16.lab16c1, lab16c3,lab16c4,lab16c5,lab16c6,lab16c7,lab16c8, lab16.lab07c1 "
                    + ",lab24.lab24c1, lab24c2, lab24c9 "
                    + ",lab11.lab04c1, lab04.lab04c4 "
                    + ",lab31.lab31c1, lab31c2 "
                    + ",lab05.lab05c1, lab05c2, lab05c10, lab05c4 "
                    + ",lab11.lab04c1_1, a.lab04c4 as updateUser "
                    + "FROM lab11 "
                    + "INNER JOIN lab24 on lab24.lab24c1 = lab11.lab24c1 "
                    + "INNER JOIN lab16 on lab16.lab16c1 = lab11.lab16c1 "
                    + "INNER JOIN lab05 on lab05.lab05c1 = lab16.lab05c1 "
                    + "LEFT JOIN lab31 on lab31.lab31c1 = lab16.lab31c1  "
                    + "LEFT JOIN lab04 on lab04.lab04c1 = lab11.lab04c1  "
                    + "LEFT JOIN lab04 a on a.lab04c1 = lab11.lab04c1_1 "
                    + "WHERE lab16.lab16c1 = ? AND lab11.lab11c1 = ? ",
                    (ResultSet rs, int i) ->
            {
                RackDetail bean = new RackDetail();
                bean.setSample(new Sample(rs.getInt("lab24c1")));
                bean.getSample().setName(rs.getString("lab24c2"));
                bean.getSample().setCodesample(rs.getString("lab24c9"));
                bean.setOrder(rs.getLong("lab22c1"));

                bean.setPosition(rs.getString("lab11c1"));
                bean.setRack(new Rack(rs.getInt("lab16c1")));
                bean.getRack().setName(rs.getString("lab16c3"));
                bean.getRack().setType(rs.getInt("lab16c4"));
                bean.getRack().setFloor(rs.getString("lab16c8"));
                bean.getRack().setRow(rs.getInt("lab16c5"));
                bean.getRack().setColumn(rs.getInt("lab16c6"));
                bean.getRack().setState(rs.getInt("lab07c1"));
                if (rs.getString("lab31c1") != null)
                {
                    bean.getRack().setRefrigerator(new Refrigerator());
                    bean.getRack().getRefrigerator().setId(rs.getInt("lab31c1"));
                    bean.getRack().getRefrigerator().setName(rs.getString("lab31c2"));
                }

                bean.setBranch(new Branch(rs.getInt("lab05c1")));
                bean.getBranch().setAbbreviation(rs.getString("lab05c2"));
                bean.getBranch().setName(rs.getString("lab05c4"));
                bean.getBranch().setCode(rs.getString("lab05c10"));
                bean.setRegistDate(rs.getTimestamp("lab11c2"));
                bean.setUpdateDate(rs.getTimestamp("lab11c3"));
                bean.setInsert(rs.getInt("lab11c4") == 1);
                bean.setRegistUser(new AuthorizedUser(rs.getInt("lab04c1")));
                bean.getRegistUser().setUserName(rs.getString("lab04c4"));

                bean.setUpdateUser(new AuthorizedUser(rs.getInt("lab04c1_1")));
                bean.getUpdateUser().setUserName(rs.getString("updateUser"));

                bean.setValidStorageDate(rs.getTimestamp("lab11c5"));

                Date finalDate = bean.getCertificate() == null || bean.getCertificate().getDisposalDate() == null ? new Date() : bean.getCertificate().getDisposalDate();
                bean.setStoredDays(DateTools.getElapsedDays(bean.getRegistDate(), finalDate));
                return bean;
            }, rackId, position);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de gradilla para almacenar o remover una muestra
     * en la base de datos.
     *
     * @param rack id gradilla
     * @param position posicion en la gradilla
     * @param user id usuario actualiza
     * @param state estado, 1 - Almacenado, 2 - Retirado
     *
     * @return registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int changeState(int rack, String position, int user, int state) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return getConnection().update("UPDATE lab11 SET "
                + "lab11c4 = ?, lab04c1_1 = ?, lab11c3 = ? "
                + "WHERE lab11c1 = ? AND lab16c1 = ? ",
                state, user, timestamp, position, rack);

    }

    /**
     * Lista de las gradillas candidatas para ser desechadas
     *
     * @param currentDate fecha actual
     * @param branch id de la sede
     * @return
     * @throws Exception
     */
    default List<Rack> racksForDisposal(Date currentDate, Integer branch) throws Exception
    {
        try
        {
            List<Rack> racks = new ArrayList<>();
            getConnection().query(""
                    + "SELECT lab11c1, lab22c1,lab11c2,lab11c3,lab11c4,lab11c5,lab11.lab04c1_1 "
                    + ",lab16.lab16c1, lab16c3,lab16c4,lab16c5,lab16c6,lab16c7,lab16c8, lab16.lab07c1 "
                    + ",lab24.lab24c1, lab24c2, lab24c9 "
                    + ",lab11.lab04c1, lab04.lab04c4 "
                    + ",lab31.lab31c1, lab31c2 "
                    + ",lab05.lab05c1, lab05c2, lab05c10, lab05c4 "
                    + "FROM lab11 "
                    + "INNER JOIN lab24 on lab24.lab24c1 = lab11.lab24c1 "
                    + "INNER JOIN lab16 on lab16.lab16c1 = lab11.lab16c1 "
                    + "INNER JOIN lab05 on lab05.lab05c1 = lab16.lab05c1 "
                    + "INNER JOIN lab31 on lab31.lab31c1 = lab16.lab31c1 "
                    + "LEFT JOIN lab04 on lab04.lab04c1 = lab11.lab04c1  "
                    + "WHERE lab16.lab07c1 = 1 AND lab05.lab05c1 = ?", new RowCallbackHandler()
            {
                @Override
                public void processRow(ResultSet rs) throws SQLException
                {
                    Rack rack = new Rack(rs.getInt("lab16c1"));
                    if (!racks.contains(rack))
                    {
                        rack.setName(rs.getString("lab16c3"));
                        rack.setPositions(new ArrayList<>());
                        rack.setType(rs.getInt("lab16c4"));
                        rack.setFloor(rs.getString("lab16c8"));
                        rack.setRefrigerator(new Refrigerator());
                        rack.getRefrigerator().setId(rs.getInt("lab31c1"));
                        rack.getRefrigerator().setName(rs.getString("lab31c2"));
                        rack.setBranch(new Branch(rs.getInt("lab05c1")));
                        rack.getBranch().setCode(rs.getString("lab05c10"));
                        rack.getBranch().setName(rs.getString("lab05c4"));
                        racks.add(rack);
                    }
                    else
                    {
                        rack = racks.get(racks.indexOf(rack));
                    }
                    RackDetail detail = new RackDetail(rs.getString("lab11c1"));
                    detail.setRegistDate(rs.getTimestamp("lab11c2"));
                    detail.setValidStorageDate(rs.getTimestamp("lab11c5"));
                    rack.getPositions().add(detail);

                    if (rack.getStorageDate() == null || rack.getStorageDate().compareTo(detail.getRegistDate()) > 0)
                    {
                        rack.setStorageDate(detail.getRegistDate());
                        rack.setStorageDays(DateTools.getElapsedDays(detail.getRegistDate(), currentDate));
                    }

                }
            }, branch);
            return racks;
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Elimina registro de la tabla gradilla detalle
     *
     * @param rack id gradilla
     * @param position posicion
     * @return
     * @throws Exception
     */
    default int removeSample(int rack, String position) throws Exception
    {
        return getConnection().update("DELETE FROM lab11 "
                + "WHERE lab11c1 = ? AND lab16c1 = ? ",
                position, rack);
    }

    /**
     * Inserta la gradilla en la auditoria de la gradilla solo cuando esta sea
     * desechada
     *
     * @param rackDetail
     * @param reusable
     * @param disposalCertificate
     * @throws Exception
     */
    default void insertRackAudit(List<RackDetail> rackDetail, boolean reusable, DisposalCertificate disposalCertificate) throws Exception
    {
        try
        {
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab183");
            
            rackDetail.stream().map( (rack) 
                    -> {
                HashMap params = new HashMap();
                params.put("lab183c1", rack.getPosition());
                params.put("lab16c1", rack.getRack().getId());
                params.put("lab24c1", rack.getSample().getId());
                params.put("lab22c1", rack.getOrder());
                params.put("lab183c2", rack.getRegistDate());
                params.put("lab183c3", rack.getUpdateDate());
                params.put("lab183c4", rack.getInsert() == true ? 1 : 0);
                params.put("lab04c1", rack.getRegistUser().getId());
                params.put("lab04c1_1", rack.getUpdateUser().getUpdateUserId());
                params.put("lab183c5", rack.getValidStorageDate());
                params.put("lab27c1", disposalCertificate.getId());
                return params;
            }).forEachOrdered((parameters)
                    ->
            {
                batchArray.add(parameters);
            });
            
            insert.executeBatch(batchArray.toArray(new HashMap[rackDetail.size()]));
            
            if (reusable)
            {
                reopen(rackDetail.get(0).getRack().getId());
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

    /**
     * Retorna el Id de la gradilla por su codigo
     *
     * @param rackCode
     *
     * @return id de la gradilla
     * @throws Exception Error en la base de datos.
     */
    default Integer getIdRack(String rackCode) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab16c1 AS rackId ")
                    .append("FROM lab16 ")
                    .append("INNER JOIN lab05 ON lab05.lab05c1 = lab16.lab05c1 ")
                    .append("LEFT JOIN lab04 ON lab04.lab04c1 = lab16.lab04c1 ")
                    .append("WHERE lab16c2 = '").append(rackCode).append("'");
            ;

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("rackId");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     *
     * Lista los exámenes de una muestra que estan validados.
     *
     * @param order
     * @param sample
     * @return Listado de examenes validados
     * @throws Exception Error en la base de datos.
     */
    default List<ResultTest> listSampleTestsAll(long order, String sample) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39.lab39c1, lab57c8,lab39c27, lab24.lab24c1, lab24c7, lab24c14 ")
                    .append("FROM lab57 ")
                    .append("LEFT JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ")
                    .append("LEFT JOIN lab24 ON lab57.lab24c1 = lab24.lab24c1 ")
                    .append("WHERE lab22c1 = ").append(order)
                    .append(" AND lab24c9 = '").append(sample).append("'");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setConfidential(rs.getInt("lab24c14") == 1);
                bean.setSampleId(rs.getInt("lab24c1"));
                bean.setStorageDays(rs.getInt("lab24c7"));
                return bean;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>(0);
        }

    }
    
    /**
     * Elimina registros de la tabla gradilla detalle
     *
     * @param racks Lista de muestras de una gradilla a desechar
     * @return
     * @throws Exception
     */
    default int removeSamples(List<RackDetail> racks) throws Exception
    {
        String sql = "DELETE FROM lab11 " +
            "WHERE lab11c1 = ? AND lab16c1 = ? ";

        List<Object[]> parameters = new LinkedList<>();

        
        for (RackDetail rack : racks) {
            parameters.add(new Object[] {rack.getPosition(),
                rack.getRack().getId()}
            );
        }
   
        return getConnection().batchUpdate(sql, parameters).length;
    }
    
    /**
     * Lista de las gradillas candidatas para ser desechadas que no tienen muestras almacenadas pero fueron cerradas
     *
     * @param currentDate fecha actual
     * @param branch id de la sede
     * @return
     * @throws Exception
     */
    default List<Rack> racksWithoutSamplesForDisposal(Date currentDate, Integer branch) throws Exception
    {
        try
        {
            List<Rack> racks = new ArrayList<>();
            getConnection().query(""
                    + "SELECT lab16.lab16c1, lab16c3, lab16c4, lab16c8, lab16.lab31c1, lab31c2, lab16.lab05c1, lab05c10 "
                    + ", lab05c4, lab16c10 "
                    + "FROM lab16 "
                    + " LEFT JOIN lab11 ON lab16.lab16c1 = lab11.lab16c1"
                    + " INNER JOIN lab31 on lab31.lab31c1 = lab16.lab31c1 "
                    + " INNER JOIN lab05 on lab05.lab05c1 = lab16.lab05c1 "
                    + " WHERE lab16.lab07c1 = 1 AND lab11c1 IS NULL AND lab05.lab05c1 = ?", new RowCallbackHandler()
            {
                @Override
                public void processRow(ResultSet rs) throws SQLException
                {
                    Rack rack = new Rack(rs.getInt("lab16c1"));
                    
                    rack.setName(rs.getString("lab16c3"));
                    rack.setPositions(new ArrayList<>());
                    rack.setType(rs.getInt("lab16c4"));
                    rack.setFloor(rs.getString("lab16c8"));
                    rack.setRefrigerator(new Refrigerator());
                    rack.getRefrigerator().setId(rs.getInt("lab31c1"));
                    rack.getRefrigerator().setName(rs.getString("lab31c2"));
                    rack.setBranch(new Branch(rs.getInt("lab05c1")));
                    rack.getBranch().setCode(rs.getString("lab05c10"));
                    rack.getBranch().setName(rs.getString("lab05c4"));
                    
                    rack.setStorageDate(rs.getTimestamp("lab16c10"));
                    if( rack.getStorageDate() == null ) {
                        rack.setStorageDays(null);
                    } else {
                        rack.setStorageDays(DateTools.getElapsedDays(rack.getStorageDate(), currentDate));
                    }
 
                    racks.add(rack);
                }
            }, branch);
            return racks;
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Inserta la gradilla en la auditoria de la gradilla solo cuando esta sea
     * desechada
     *
     * @param racks Lista de gradillas a desechar
     * @param idCertificate Id del acta
     * @throws Exception
     */
    default void insertRackAuditToDisposal(List<Rack> racks, Integer idCertificate) throws Exception
    {
        try
        {
            List<HashMap> batchArray = new ArrayList<>();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab271");
            
            racks.stream().map( (rack) 
                    -> {
                HashMap params = new HashMap();
                params.put("lab16c1", rack.getId());
                params.put("lab271c1", rack.getFloor());
                params.put("lab31c1", rack.getRefrigerator() == null ? null : rack.getRefrigerator().getId());
                params.put("lab04c1", rack.getUser().getId());
                params.put("lab271c2", rack.getCloseDate());
                params.put("lab27c1", idCertificate);   
                return params;
            }).forEachOrdered((parameters)
                    ->
            {
                batchArray.add(parameters);
            });
            
            insert.executeBatch(batchArray.toArray(new HashMap[racks.size()]));
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }
    
    /**
     * Re abre la gradilla.
     *
     * @param IdRack Id de la gradilla.
     *
     * @return Objeto de gradilla modificada.
     * @throws Exception Error en la base de datos.
     */
    default int reopen(Integer IdRack) throws Exception
    {   
        // Cuando la gradilla es registrada en la auditoria y esta puede ser reutilizada
        // se cambia su estado automaticamente a 0 -> (Abierta), para volver a ser usada
        return getConnection().update("UPDATE lab16 SET lab07c1 = 0 WHERE lab16c1 = ?", IdRack);
    }

    public JdbcTemplate getConnection();
}
