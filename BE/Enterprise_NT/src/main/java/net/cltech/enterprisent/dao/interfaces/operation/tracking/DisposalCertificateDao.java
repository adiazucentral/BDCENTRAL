package net.cltech.enterprisent.dao.interfaces.operation.tracking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.tracking.DisposalCertificate;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
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
 * @since 29/06/2018
 * @see Creación
 */
public interface DisposalCertificateDao
{

    /**
     * Lista actas desde la base de datos.
     *
     * @return Lista de actas.
     * @throws Exception Error en la base de datos.
     */
    default List<DisposalCertificate> list() throws Exception
    {
        try
        {
            return getConnection().query("" + ISOLATION_READ_UNCOMMITTED
                    + " SELECT lab27c1, lab27c2,lab27c3,lab27c4,lab27c5,lab27c5,lab27c6,lab27c7"
                    + " ,lab04.lab04c1, lab04.lab04c2, lab04.lab04c3, lab04.lab04c4 "
                    + "FROM lab27 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab27.lab04c1 ", (ResultSet rs, int i) ->
            {
                DisposalCertificate bean = new DisposalCertificate();
                bean.setId(rs.getInt("lab27c1"));
                bean.setName(rs.getString("lab27c7"));
                bean.setDescription(rs.getString("lab27c2"));
                bean.setClosed(rs.getInt("lab27c3") == 1);
                bean.setType(rs.getInt("lab27c5"));
                bean.setCreationDate(rs.getTimestamp("lab27c4"));
                /*Usuario acta*/
                bean.setCreationUser(new AuthorizedUser());
                bean.getCreationUser().setId(rs.getInt("lab04c1"));
                bean.getCreationUser().setName(rs.getString("lab04c2"));
                bean.getCreationUser().setLastName(rs.getString("lab04c3"));
                bean.getCreationUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra acta en la base de datos.
     *
     * @param create Instancia con los datos de gradilla.
     *
     * @return Instancia con los datos de gradilla.
     * @throws Exception Error en la base de datos.
     */
    default DisposalCertificate create(DisposalCertificate create) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab27")
                .usingGeneratedKeyColumns("lab27c1");

        HashMap parameters = new HashMap();
        parameters.put("lab27c2", create.getDescription());
        parameters.put("lab27c7", create.getName().trim());
        parameters.put("lab27c3", create.isClosed() ? 1 : 0);
        parameters.put("lab27c4", timestamp);
        parameters.put("lab27c5", create.getType());
        parameters.put("lab04c1", create.getCreationUser().getId());

        Number key = insert.executeAndReturnKey(parameters);
        create.setId(key.intValue());
        create.setCreationDate(timestamp);
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
    default DisposalCertificate update(DisposalCertificate update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab27 SET "
                + "lab27c7 = ?, lab27c2 = ?, lab27c3 = ?, lab27c5 = ? "
                + "WHERE lab27c1 = ?",
                update.getName().trim(), update.getDescription(), update.isClosed() ? 1 : 0, update.getType(), update.getId());

        update.setCreationDate(timestamp);

        return update;
    }

    /**
     * Actualiza el acta de desecho a las muestras de las gradillas enviadas
     *
     * @param user
     * @param racks lista de gradillas a asignar
     * @return registros afectados
     * @throws Exception Error en base de datos
     */
    default int assignCertificateByRacks(List<Integer> racks, int user) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return getConnection().update("UPDATE lab11 SET "
                + "lab04c1_1 = ?, lab11c3 = ? "
                + "WHERE lab16c1 in (" + racks.stream().map(rack -> rack.toString()).collect(Collectors.joining(",")) + ")",
                user, timestamp);
    }

    /**
     * Actualiza acta de desecho segun la gradilla y posicion enviada
     *
     * @param idRack id gradilla
     * @param user
     * @param position posicion
     * @return registros afectados
     * @throws Exception Error en base de datos
     */
    default int assignCertificateByPosition(int idRack, String  position, int user) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return getConnection().update("UPDATE lab11 SET "
                + "lab04c1_1 = ?, lab11c3 = ? "
                + "WHERE lab11c1 = ? AND lab16c1 = ?",
                user, timestamp, position, idRack);
    }

    /**
     * Cambio de estado para cerrar el acta
     *
     * @param idCertificate id acta
     * @param user id de usuario
     * @return registros afectados
     * @throws Exception
     */
    default int close(int idCertificate, int user) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return getConnection().update("UPDATE lab27 SET "
                + "lab27c3 = 1, lab04c1_1 = ?, lab27c6 = ? "
                + "WHERE lab27c1 = ? ",
                user, timestamp, idCertificate);
    }

    /**
     * Obtiene informacion detallada del acta de desecho
     *
     * @param id id del acta de desecho
     * @return Objeto DisposalCertificate
     * @throws Exception Error en la base de datos
     */
    default DisposalCertificate getDisposalDetail(int id) throws Exception
    {

        final DisposalCertificate disposal = new DisposalCertificate();
        try
        {
            String sql = ISOLATION_READ_UNCOMMITTED + " SELECT "
                    + " lab183.lab22c1 "
                    + " ,lab27.lab27c1, lab27c2,lab27c3,lab27c4,lab27c5,lab27c6,lab27c7 "
                    + " ,lab16.lab16c1, lab16c2,lab16c3,lab16c4,lab16c8 "
                    + " ,lab24.lab24c1, lab24c2,lab24c9 "
                    + " ,lab183.lab183c1, lab183c2,lab183c5, lab183c3 "
                    + " ,lab04.lab04c1, lab04.lab04c2, lab04.lab04c3, lab04.lab04c4 "//usuario cierra acta
                    + " ,a.lab04c1 as dispouseUserId, a.lab04c4 as dispouseUser "//usuario desecha muestra
                    + " ,lab21.lab21c1, lab21.lab21c2, lab21.lab21c3, lab21.lab21c4,lab21.lab21c5,lab21.lab21c6 "//info paciente
                    + "FROM lab183 "
                    + "INNER JOIN lab22 ON lab183.lab22c1 = lab22.lab22c1 "
                    + "INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "INNER JOIN lab16 ON lab16.lab16c1 = lab183.lab16c1 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab183.lab24c1 "
                    + "INNER JOIN lab27 ON lab27.lab27c1 = lab183.lab27c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab27.lab04c1_1 "
                    + "LEFT JOIN lab04 a ON a.lab04c1 = lab183.lab04c1_1 "
                    + "WHERE lab27.lab27c1 = ?";
            getConnection().query(sql, (ResultSet rs) ->
            {
                if (disposal.getId() == null)
                {
                    disposal.setId(rs.getInt("lab27c1"));
                    disposal.setName(rs.getString("lab27c7"));
                    disposal.setDescription(rs.getString("lab27c2"));
                    disposal.setClosed(rs.getInt("lab27c3") == 1);
                    disposal.setType(rs.getInt("lab27c5"));
                    disposal.setCreationDate(rs.getTimestamp("lab27c4"));
                    disposal.setPositions(new ArrayList<>());
                    if (rs.getString("lab27c1") != null)
                    {
                        disposal.setDisposalDate(rs.getTimestamp("lab27c6"));
                        disposal.setDisposalUser(new AuthorizedUser(rs.getInt("lab04c1")));
                        disposal.getDisposalUser().setName(rs.getString("lab04c2"));
                        disposal.getDisposalUser().setLastName(rs.getString("lab04c3"));
                        disposal.getDisposalUser().setUserName(rs.getString("lab04c4"));
                    }
                    disposal.setPositions(new ArrayList<>());
                }
                RackDetail detail = new RackDetail(rs.getString("lab183c1"));
                //Gradilla
                detail.setRack(new Rack(rs.getInt("lab16c1")));
                detail.getRack().setCode(rs.getString("lab16c1"));
                detail.getRack().setName(rs.getString("lab16c3"));
                detail.getRack().setType(rs.getInt("lab16c4"));
                detail.getRack().setFloor(rs.getString("lab16c8"));
                //Muestra
                detail.setSample(new Sample(rs.getInt("lab24c1")));
                detail.getSample().setCodesample(rs.getString("lab24c9"));
                detail.getSample().setName(rs.getString("lab24c2"));

                detail.setOrder(rs.getLong("lab22c1"));
                detail.setRegistDate(rs.getTimestamp("lab183c2"));
                detail.setValidStorageDate(rs.getTimestamp("lab183c5"));

                detail.setUpdateUser(new AuthorizedUser(rs.getInt("dispouseUserId")));
                detail.getUpdateUser().setUserName(rs.getString("dispouseUser"));
                detail.setUpdateDate(rs.getTimestamp("lab183c3"));

                Date finalDate = disposal.getDisposalDate() == null ? new Date() : disposal.getDisposalDate();
                detail.setStoredDays(DateTools.getElapsedDays(detail.getRegistDate(), finalDate));

                //Paciente
                Patient patient = new Patient();
                patient.setId(rs.getInt("lab21c1"));
                patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                detail.setPatient(patient);

                disposal.getPositions().add(detail);

            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
        return disposal;
    }
    
    /**
     * Obtiene informacion detallada del acta de desecho
     *
     * @param id id del acta de desecho
     * @return Objeto DisposalCertificate
     * @throws Exception Error en la base de datos
     */
    default List<RackDetail> getDisposalDetailWithoutSamples(int id) throws Exception
    {
        List<RackDetail> list = new ArrayList<>();
        
        try
        {
            getConnection().query(ISOLATION_READ_UNCOMMITTED + " SELECT "
                    + " lab271.lab16c1, lab16c2, lab16c3, lab16c4, lab16c8 "
                    + " FROM lab271 "
                    + " INNER JOIN lab16 ON lab16.lab16c1 = lab271.lab16c1 "
                    + " LEFT JOIN lab183 ON lab183.lab16c1 = lab271.lab16c1 AND lab183.lab27c1 = " + id
                    + " LEFT JOIN lab27 ON lab27.lab27c1 = lab271.lab27c1 "
                    + " WHERE lab271.lab27c1 = ? AND lab183c1 IS NULL", (ResultSet rs) -> {
                        RackDetail detail = new RackDetail("");
                        //Gradilla
                        detail.setRack(new Rack(rs.getInt("lab16c1")));
                        detail.getRack().setCode(rs.getString("lab16c1"));
                        detail.getRack().setName(rs.getString("lab16c3"));
                        detail.getRack().setType(rs.getInt("lab16c4"));
                        detail.getRack().setFloor(rs.getString("lab16c8"));
                        list.add(detail);
            }, id);
            return list;

        } catch (EmptyResultDataAccessException ex)
        {
           return new ArrayList<>(0);
        }
    }

    public JdbcTemplate getConnection();

}
