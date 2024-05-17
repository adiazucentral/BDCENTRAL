package net.cltech.enterprisent.dao.impl.sqlserver.masters.billing;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.PriceDao;
import net.cltech.enterprisent.domain.masters.billing.PriceAssignmentBatch;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * Realiza la implementación del acceso a datos de información del maestro
 * para SQLServer
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/08/2017
 * @see Creación
 */
@Repository
public class PriceDaoSQLServer implements PriceDao
{

    private JdbcTemplate jdbc;

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
    public int priceBatchCreate(List<PriceAssignmentBatch> list) throws Exception
    {
        
        int x = list.size();
        int y = 1;
        if(x < 1000) {
            y = x-1;
        } else {
            y = 900;
        }
        
        Connection con = getJdbcTemplate().getDataSource().getConnection();
        
        StringBuilder insertSql = new StringBuilder();
        
        insertSql.append("INSERT INTO lab120 ([lab39c1], [lab904c1], [lab116c1], [lab120c1], [lab120c2], [lab04c1]) VALUES");
        
        try {
            
            for( int i = 0; i < x; i++ ) {
                
                insertSql.append("(")
                    .append(list.get(i).getIdTest())
                    .append(",")
                    .append(list.get(i).getIdRate())
                    .append(",")
                    .append(list.get(i).getIdValid())
                    .append(",")
                    .append(list.get(i).getPrice())
                    .append(",")
                    .append(list.get(i).getPatientPercentage() == null ? 0.0 : list.get(i).getPatientPercentage())
                    .append(",")
                    .append(list.get(i).getUser().getId())
                    .append(")");
                
                if( (i > 0 && i % y == 0) || i == (x-1) ) {
                    try {
                        insertSql.append(";");
                        Statement st = con.createStatement();
                        st.executeUpdate(insertSql.toString());
                        insertSql = new StringBuilder();
                        insertSql.append("INSERT INTO lab120 ([lab39c1], [lab904c1], [lab116c1], [lab120c1], [lab120c2], [lab04c1]) VALUES");
                        
                    } catch(Exception e) {
                        OrderCreationLog.error(e.getMessage());
                    }
                } else {
                   insertSql.append(","); 
                }
            }
        } catch(Exception e){
            OrderCreationLog.error(e.getMessage());
        }
        return 1;
    }
}
