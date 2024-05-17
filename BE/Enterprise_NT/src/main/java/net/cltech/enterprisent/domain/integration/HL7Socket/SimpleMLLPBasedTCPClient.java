package net.cltech.enterprisent.domain.integration.HL7Socket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoDao;
import net.cltech.enterprisent.domain.integration.ingreso.RequestStateTest;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTestStatus;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsSonControl;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author bvalero
 */

public class SimpleMLLPBasedTCPClient
{
    private static final char END_OF_BLOCK = 28;//'\u001c';
    private static final char START_OF_BLOCK = 11;//'\u000b';
    private static final char CARRIAGE_RETURN = 13;//\ufe0c
    private final String RUTA_LOG;
    LoggerInterface log = new LoggerInterface();

    @Autowired
    private HttpServletRequest request;
    @Autowired
    public  IntegrationIngresoDao integrationIngresoDao;
    
    public SimpleMLLPBasedTCPClient(String ruta) throws Exception
    {
        Calendar c = Calendar.getInstance();
        String anio = Integer.toString(c.get(Calendar.YEAR));
        String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        mes = Integer.parseInt(mes) < 10 ? 0 + mes : mes;
        String day = Integer.toString(c.get(Calendar.DATE));
        day = Integer.parseInt(day) < 10 ? 0 + day : day;
        this.RUTA_LOG = ruta + day + mes + anio + ".txt";
    }

    
    public void writeMessaggeLog(String messagge) throws IOException
    {
        IntegrationHisLog.info(messagge);
    }
}
