package net.cltech.securitynt.tools;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Valores constantes para toda la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 21/04/2017
 * @see Creación
 */
public class Constants
{

    public final static int ACCOUNT = -1;
    public final static int PHYSICIAN = -2;
    public final static int RATE = -3;
    public final static int ORDERTYPE = -4;
    public final static int BRANCH = -5;
    public final static int SERVICE = -6;
    public final static int RACE = -7;
    public final static int WEIGHT = -8;
    public final static int SIZE = -9;
    public final static int DOCUMENT_TYPE = -10;
    public final static int AGE_GROUP = -11;
    //Datos Fijos de la Historia
    public final static int PATIENT_DB_ID = -99;
    public final static int PATIENT_ID = -100;
    public final static int PATIENT_LAST_NAME = -101;
    public final static int PATIENT_SURNAME = -102;
    public final static int PATIENT_NAME = -103;
    public final static int PATIENT_SEX = -104;
    public final static int PATIENT_BIRTHDAY = -105;
    public final static int PATIENT_EMAIL = -106;
    public final static int PATIENT_SECOND_NAME = -109;
    public final static int PATIENT_AGE = -110;
    public final static int PATIENT_PHONE = -111;
    public final static int PATIENT_ADDRESS = -112;
    public final static int PATIENT_COMMENT = -997;
    //Datos Fijos de la orden
    public final static int ORDER_ID = -107;
    public final static int ORDER_DATE = -108;
    public final static int ORDER_HIS = -111;
    public final static int ORDER_COMMENT = -996;
    //Aplican para la entrevista
    public final static int ORDER = 1;
    public final static int LABORATORY = 2;
    public final static int TEST = 3;
    //Estados del examen
    public final static int TEST_ENTRY = 1;
    public final static int TEST_TAKEN = 2;
    public final static int TEST_VERIFY = 3;
    public final static int TEST_WITH_RESULT = 4;
    public final static int TEST_REJECT_NEW_SAMPLE = 5;
    public final static int TEST_REJECT = 6;
    public final static int TEST_VALID = 7;
    public final static int TEST_PRINT = 8;
    //Motivos para uso del sistema
    public final static int MOTIVE_INCONSISTENCY = -1;
    public final static String RESULT_COMMENT = "MEMO";
    public final static String RESULT_ATTACHMENT = "ATTACHMENT";
    public final static String RESULT_ANTIBIOGRAM = "A/B";
    public static ExecutorService ex = Executors.newFixedThreadPool(1);
    //Tipo de errores
    public final static int ERROR_BACKEND = 0;
    public final static int ERROR_FRONTEND = 1;
    /**
     * Directorio en Glassfish donde se almacenarán los archivos de los
     * reportes.
     */
    public static final String REPORT_PATH = System.getProperty("user.dir")
            + File.separator + "barcode"
            + File.separator;
    //Tipos de trazabilidad de la muestra
    public final static int SAMPLE_NOTAPPLY = 1;
    public final static int SAMPLE_ENTRY = 2;
    public final static int SAMPLE_COMPLETEVERIFICATION = 3;
    public final static int SAMPLE_LIST = 4;

    public final static int STATISTICS_TEST_ENTRY = 1;
    public final static int STATISTICS_TEST_VALIDATE = 2;
    public final static int STATISTICS_TEST_PRINT = 3;
    public final static int STATISTICS_TEST_PATHOLOGY = 4;
    public final static int STATISTICS_ORDER_ENTRY = 5;

    //Listados 
    public final static int LIST_GENDER_ITEM = 6;
    public final static int LIST_DELIVERY_TYPE = 58;

    //Integracion con Middleware
    public final static int ENTRY = 1;
    public final static int CHECK = 2;
    public final static int ANY = 3;

    //Actualizar laboratorio
    public final static int ORIGINLABORATORY = 1;
    public final static int ORIGININTEGRATION = 2;

    //Secuencias
    public final static String SEQUENCE = "enterprise_nt_seq_";
    public final static String SEQUENCE_RECALLED = "enterprise_nt_seq_recalled";

    //Agrupacion de ordenes
    public static final String ORDER_GROUPING_TYPE = "1";
    public static final String ORDER_GROUPING_SERVICE = "2";

    //Tiempo de expiracion del token
    public static final String TOKEN_EXPIRATION_TIME = "TokenExpirationTime";
    
    //Tipos de token
    public final static int TOKEN_AUTH_USER = 1;
    public final static int TOKEN_RESET_USER = 2;
}
