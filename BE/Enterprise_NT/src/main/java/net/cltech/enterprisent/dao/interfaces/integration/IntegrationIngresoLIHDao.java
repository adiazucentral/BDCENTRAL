package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.util.List;
import net.cltech.enterprisent.domain.integration.ingresoLIH.DatosGenerales;
import net.cltech.enterprisent.domain.integration.ingresoLIH.Items;
import net.cltech.enterprisent.domain.integration.ingresoLIH.OrdenLaboratorio;
import net.cltech.enterprisent.domain.integration.ingresoLIH.Paciente;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implementacion de Integracion de ingreso para Enterprise NT
 *
 * @version 1.0.0
 * @author BValero
 * @since 23/04/2020
 * @see Creacion
 */
public interface IntegrationIngresoLIHDao {
    
    /**
     * Obtener informacion de orden y pacientes por sistema central y rango de ordenes
     *
     * @param CentralSystem
     * @param orderInitial
     * @param orderFinal
     * @return
     * @throws Exception Error en la base de datos
     */
    default List<OrdenLaboratorio> getDataByCentralSystem(int CentralSystem, long orderInitial, long orderFinal) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT ")
                    .append("  A.Lab22C1 AS 'NumeroOrden', 'F001' AS CodCliente, ")
                    .append(" 'Fundacion Santafe' AS 'NombreCliente', '' AS 'CodMedico', CAST(B.Lab60C3 AS VARCHAR) AS 'observ', ")
                    .append(" D.Lab54C1 AS 'TipoIdentificacion', C.Lab21C2 AS 'NumeroIdentificacion', ")
                    .append(" C.Lab21C3 AS 'Nombre1', C.Lab21C4 AS 'Nombre2', C.Lab21C5 AS 'Apellido1', C.Lab21C6 AS 'Apellido2', ")
                    .append(" E.Lab80C4 AS 'Genero', C.Lab21C20 AS 'FechaNacimiento', F.Lab103C2 AS 'Urgente', ")
                    .append(" '' AS 'Enembarazo' ")
                    .append(" FROM Lab22 A ")
                    .append(" LEFT JOIN Lab60 B ON (A.Lab22C1=B.Lab60C2) ")
                    .append(" JOIN Lab21 C ON (A.Lab21C1=C.Lab21C1) JOIN Lab54 D ON (D.Lab54C1=C.Lab54C1) ")
                    .append(" JOIN Lab80 E ON (C.Lab80C1 = E.Lab80C1) JOIN Lab103 F ON (A.Lab103C1=F.Lab103C1) ")
                    .append(" JOIN Lab57 G ON (A.Lab22C1=G.Lab22C1) JOIN Lab61 H ON (G.Lab39C1=H.lab39c1) ")
                    .append("WHERE H.Lab118C1 = ? AND A.Lab22C1 >= ? AND A.Lab22C1 <= ? AND A.lab22c14 IS NULL AND ( A.lab22c19 = 0 or A.lab22c19 is null) ");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    -> 
            {
                OrdenLaboratorio order = new OrdenLaboratorio();
                //Datos Generales
                DatosGenerales dg = new DatosGenerales();
                dg.setNumeroOrden(rs.getString("NumeroOrden"));
                dg.setCodCliente(rs.getString("CodCliente"));
                dg.setNombreCliente(rs.getString("NombreCliente"));
                dg.setCodMedico(rs.getString("CodMedico"));
                dg.setObserv(rs.getString("observ"));
                //Paciente
                Paciente pac = new Paciente();
                pac.setTipoIdentificacion(rs.getString("TipoIdentificacion"));
                pac.setNumeroIdentificacion(Tools.decrypt(rs.getString("NumeroIdentificacion")));
                pac.setNombres(Tools.decrypt(rs.getString("Nombre1")) + " " + Tools.decrypt(rs.getString("Nombre2")));
                pac.setApellidos(Tools.decrypt(rs.getString("Apellido1")) + " " + Tools.decrypt(rs.getString("Apellido2")));
                pac.setGenero(rs.getString("Genero"));
                pac.setFechaNacimiento(rs.getString("FechaNacimiento"));
                pac.setUrgente(rs.getString("Urgente"));
                pac.setEnembarazo(rs.getString("Enembarazo"));
                //Se cargan los objetos
                order.setDatosGenerales(dg);
                order.setPaciente(pac);
                return order;
            }, CentralSystem, orderInitial, orderFinal);
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    /**
     * Obtener informacion de examenes por sistema central y numero de orden
     *
     * @param CentralSystem
     * @param order
     * @return
     * @throws Exception Error en la base de datos
     */
    default List<Items> getDataTest(int CentralSystem, long order) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT ")
                    .append("  D.Lab61C1 AS CUPS, C.Lab39C4 AS Descripcion, C.Lab39c1 AS idTest ")
                    .append(" FROM Lab22 A ")
                    .append(" JOIN Lab57 B ON (A.Lab22C1=B.Lab22C1) ")
                    .append(" JOIN Lab39 C ON (B.Lab39C1 = C.Lab39C1) ")
                    .append(" JOIN Lab61 D ON (D.Lab39C1 = C.Lab39C1) ")
                    .append(" WHERE A.Lab22C1 = ? AND D.Lab118C1 = ?");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                Items test = new Items();
                //Agregar Items
                test.setCups(rs.getString("CUPS"));
                test.setDescripcion(rs.getString("Descripcion"));
                test.setSecuencia(Integer.toString(i+1));
                test.setIdTest(rs.getInt("idTest"));
                return test;
            }, order, CentralSystem);
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    /**
     * Actualizar el estado de la orden enviada al LIH
     * @param order
     * @return true, false
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    default int updateOrderStateSendLIH(long order) throws Exception
    {
        try
        {
            return getJdbcTemplate().update("UPDATE lab22 SET lab22c14 = 1 WHERE lab22c1 = ?", order);
        } catch (DataAccessException e)
        {
            return 0;
        }
    }
    
     /**
     * Obtiene la conexión a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
