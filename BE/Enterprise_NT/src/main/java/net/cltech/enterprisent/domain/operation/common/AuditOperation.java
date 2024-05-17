package net.cltech.enterprisent.domain.operation.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa clase para realizar auditoria a la operación del sistema
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/10/2017
 * @see Creación
 */
@Getter
@Setter
public class AuditOperation
{

    public static String ACTION_UPDATE = "U";
    public static String ACTION_INSERT = "I";
    public static String ACTION_DELETE = "D";
    public static String ACTION_REPORT = "R";
    public static String ACTION_UPDATE_RESULT = "UR";
    public static String TYPE_DEMOGRAPHIC = "D";
    public static String TYPE_ORDER = "O";
    public static String TYPE_TEST = "T";
    public static String TYPE_PATIENT = "P";
    public static String TYPE_SAMPLE = "S";
    public static String TYPE_SAMPLETEST = "ST";
    public static String TYPE_INTERVIEW = "I";
    public static String TYPE_RESULTTEMPLATE = "RT";
    public static String TYPE_INCONSISTENCY = "IN";
    public static String TYPE_MICRO = "M";
    public static String TYPE_MICROTASK = "MT";
    public static String TYPE_MICRODETECTION = "MD";
    public static String TYPE_MICRODETECTIONCOMMENT = "MDC";
    public static String TYPE_MICROCOMMENT = "MC";
    public static String TYPE_ORDERCOMMENT = "OC";
    public static String TYPE_PATIENTDIAGNOSTIC = "PD";
    public static String TYPE_RESULTCOMMENT = "RC";
    public static String TYPE_RESULTCOMMENT2 = "RC2";
    public static String TYPE_MASTER = "MA";
    public static String TYPE_DOCUMENT = "DO";
    public static String TYPE_BLOCKTEST = "BK";
    public static String TYPE_PRINTTEST = "PR";
    public static String TYPE_SAMPLE_STORAGE = "SS";
    public static String TYPE_COMMENTINTERNALRESULT = "IC";
    public static String TYPE_OBSERVATIONS = "OB";
    // Factura
    public static String EXECUTION_TYPE_INV = "INV";
    // Nota credito
    public static String EXECUTION_TYPE_CN = "CN";
    // Tipo de ejecución para el pago total de la factura: Pay invoice
    public static String EXECUTION_TYPE_PI = "PI";
    // Caja: CashBox
    public static String EXECUTION_TYPE_CB = "CB";
    //Tipo de ejecución para el tipo de pago
    public static String EXECUTION_TYPE_PT = "PT";
    // Cambio de precios: PC
    public static String EXECUTION_TYPE_PC = "PC";

    private Long order;
    private Integer id;
    private Integer idDestination;
    private String destination;
    private String action;
    private String fieldType;
    private String fieldDescription;
    private String information;
    private Integer motive;
    private String motiveName;
    private String comment;
    private Integer user;
    private String username;
    private String result;
    private Timestamp date;
    private Integer sampleTest;
    private Integer testType;
    private Timestamp dateOrder;
    private String register;
    private String patientId;
    private String lastName;
    private String surName;
    private String name1;
    private String name2;
    private Long invoiceId;
    private String invoiceNumber;
    private String executionType;
    private Integer delivery;
    private String deliveryEsCo;
    private String deliveryEnUSA;
    private String receivesPerson;

    private List<AuditOperation> cashAudit = new ArrayList<>();

    public AuditOperation()
    {

    }

    public AuditOperation(Long order, Integer id, String action, String fieldType, String information, Integer motive, String comment)
    {
        this.order = order;
        this.id = id;
        this.action = action;
        this.fieldType = fieldType;
        this.information = information;
        this.motive = motive;
        this.comment = comment;
    }

    public AuditOperation(Long order, Integer id, Integer idDestination, String action, String fieldType, String information, Integer motive, String comment, String result, Integer delivery)
    {
        this.order = order;
        this.id = id;
        this.idDestination = idDestination;
        this.action = action;
        this.fieldType = fieldType;
        this.information = information;
        this.motive = motive;
        this.comment = comment;
        this.result = result;
        this.delivery = delivery;
    }

    public AuditOperation(Long order, Integer id, Integer idDestination, String action, String fieldType, String information, Integer motive, String comment, String result, Integer delivery, String receivesPerson)
    {
        this.order = order;
        this.id = id;
        this.idDestination = idDestination;
        this.action = action;
        this.fieldType = fieldType;
        this.information = information;
        this.motive = motive;
        this.comment = comment;
        this.result = result;
        this.delivery = delivery;
        this.receivesPerson = receivesPerson;
    }

    public AuditOperation(String action, String information, Long invoiceId, String executionType)
    {
        this.action = action;
        this.information = information;
        this.invoiceId = invoiceId;
        this.executionType = executionType;
    }
}
