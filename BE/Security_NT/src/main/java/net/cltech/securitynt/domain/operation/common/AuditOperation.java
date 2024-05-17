/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.operation.common;

/**
 * Representa clase para realizar auditoria a la operación del sistema
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/10/2017
 * @see Creación
 */
public class AuditOperation
{

    public static String ACTION_UPDATE = "U";
    public static String ACTION_INSERT = "I";
    public static String ACTION_DELETE = "D";
    public static String ACTION_REPORT = "R";
    public static String TYPE_DEMOGRAPHIC = "D";
    public static String TYPE_ORDER = "O";
    public static String TYPE_TEST = "T";
    public static String TYPE_SAMPLE = "S";
    public static String TYPE_INTERVIEW = "I";
    public static String TYPE_RESULTTEMPLATE = "RT";
    public static String TYPE_INCONSISTENCY = "IN";
    public static String TYPE_MICRO = "M";
    public static String TYPE_MICROTASK = "MT";
    public static String TYPE_MICRODETECTION = "MD";
    public static String TYPE_MICROCOMMENT = "MC";
    public static String TYPE_ORDERCOMMENT = "OC";
    public static String TYPE_MASTER = "MA";
    public static String TYPE_DOCUMENT = "DO";
    public static String TYPE_BLOCKTEST = "BK";
    public static String TYPE_SAMPLE_STORAGE = "SS";

    private Long order;
    private Integer id;
    private String action;
    private String fieldType;
    private String information;
    private Integer motive;
    private String comment;
    private Integer user;

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

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getFieldType()
    {
        return fieldType;
    }

    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }

    public String getInformation()
    {
        return information;
    }

    public void setInformation(String information)
    {
        this.information = information;
    }

    public Integer getMotive()
    {
        return motive;
    }

    public void setMotive(Integer motive)
    {
        this.motive = motive;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Integer getUser()
    {
        return user;
    }

    public void setUser(Integer user)
    {
        this.user = user;
    }

}
