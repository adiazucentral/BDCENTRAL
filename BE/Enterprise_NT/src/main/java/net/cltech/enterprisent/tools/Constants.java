package net.cltech.enterprisent.tools;

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
    public final static int DESTINATION = 4;
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
    public final static String RESULT_ATTACHMENT = "VER RESULTADO ANEXO";
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
    public final static int MICROBIOLOGY = 4;

    //Actualizar laboratorio
    public final static int ORIGINLABORATORY = 1;
    public final static int ORIGININTEGRATION = 2;

    //Genero
    public final static int MALE_GENDER = 7;
    public final static int FEMALE_GENDER = 8;
    public final static int UNDEFINED_GENDER = 9;

    //Secuencias
    public final static String SEQUENCE_GENERAL = "enterprise_nt_seq_general";
    public final static String SEQUENCE = "enterprise_nt_seq_";
    public final static String SEQUENCE_RECALLED = "enterprise_nt_seq_recalled";
    public final static String SEQUENCE_APPOINTMENT = "enterprise_nt_seq_appointment";

    //Secuencias de facturacion
    public final static String RESOLUTION_SEQUENCE = "enterprise_nt_resolution_secuence_";

    //Agrupacion de ordenes
    public static final String ORDER_GROUPING_TYPE = "1";
    public static final String ORDER_GROUPING_SERVICE = "2";

    //Tiempo de expiracion del token
    public static final String TOKEN_EXPIRATION_TIME = "TokenExpirationTime";

    //Manejo de  seguridad
    public static final String SEGURITY_MANAGER = "SecurityPolitics";

    //Manejo de SinSeleccionSede
    public static final String BRANCH_IN = "SinSeleccionSede";

    //Recuperar contraseña
    public static final String PASSWORDRECOVERY = "RecuperarContraseña";

    //Tipos de token para el app movil de JWTTokenPatient
    public final static int TOKEN_AUTH_USER = 1;
    public final static int TOKEN_RESET_USER = 2;

    //SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
    public static String ISOLATION_READ_UNCOMMITTED = "";

    //Secuencias de patologia
    public final static String SEQUENCE_PATHOLOGY = "enterprise_nt_pat_seq_";

    //Constantes de patologia
    public final static int RECEPTION = 1;
    public final static int DESCRIPTION = 2;
    public final static int TRANSCRIPTION = 3;
    public final static int TISSUEPROCESSOR = 4;

    public final static int TISSUEPROCESSOR_PENDING = 1;
    public final static int TISSUEPROCESSOR_PROCESS = 2;
    public final static int TISSUEPROCESSOR_DONE = 3;

    //Formas de pago:
    public final static int FORM_OF_PAYMENT_CASH = 1;
    public final static int FORM_OF_PAYMENT_CREDIT = 2;
    public final static int FORM_OF_PAYMENT_NOT_APPLICABLE = 0;

    // Identificadores de demográficos Y Sus codigos de homologación:
    // Codigos de homologación
    public static final String ADRESS = "D";
    public static final String CITY = "C";
    public static final String DEPARTMENT = "DE";
    public static final String DOCUMENT = "TD";
    public static final String PHONE = "T";
    public static final String PHONE2 = "T2";
    public static final String REPORTER = "H";
    public static final String SYMPTOM_DATE = "FS";
    public static final String LOCALITY = "L";
    public static final String SOURCE = "S";
    public static final String SAMPLE_TYPE = "ST";
    public static final String TEST_CODE = "CT";
    public static final String PASSPORT = "PS";

    // Ids de los demográficos Paciente interface de ingreso
    public static final Integer CITY_ID = 3;
    public static final Integer DEPARTMENT_ID = 1;
    public static final Integer PHONE2_ID = 20;
    public static final Integer REPORTER_ID = 19;
    public static final Integer SYMPTOM_DATE_ID = 18;
    public static final Integer TOMA_DATE_ID = 10;
    public static final Integer RESULT_DATE_ID = 13;
    public static final Integer LOCALITY_ID = 21;
    public static final Integer SOURCE_ID = 9;
    public static final Integer SAMPLE_TYPE_ID = 15;
    public static final Integer TEST_CODE_ID = 12;

    //IDS DEMOGRAFICOS PANAMA 
    public static final Integer REGION_ID = 1;//DEPARTAMENTO
    public static final Integer DISTRITO_ID = 2;//CIUDAD
    public static final Integer CORREGIMENTO_ID = 3;//LOCALIDAD
    public static final Integer NUMERO_INTERNO_ORDER = 8;//LOCALIDAD
    
    
    //ESTADOS INTEGRACION KBITS
    public static final Integer NONE = 0;//NO ENVIAR
    public static final Integer SEND = 1;//_ENVIAR
    public static final Integer UNBILLED = 2;//COMBOS NO FACTURADAS
    public static final Integer BILLED = 3;//COMBOS FACTURADAS
    public static final Integer SENT = 4; //ENVIADO A KBITS
    
        //Tipos de usuario
    public final static int AUTHENTICATION_PHYSICIAN = 1;
    public final static int AUTHENTICATION_PATIENT = 2;
    public final static int AUTHENTICATION_ACCOUNT = 3;
    public final static int AUTHENTICATION_USERLIS = 4;
    public final static int AUTHENTICATION_DEMOGRAPHIC = 5;
    
    
}
