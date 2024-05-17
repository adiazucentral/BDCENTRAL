/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.domain;

/**
 * Clase que represeta objeto a grudar en base de datos para log de envio de
 * correos
 *
 * @author hpoveda
 */
public class PrintLog
{

    private long order;
    private int branch;
    private String message;
    private String correos;
    private String branchName;

    /**
     * Constructor.
     *
     * @param order numero orden.
     * @param branch id sede.
     * @param comment commentario a guardar.
     * @param correos correo a enviar.
     */
    public PrintLog(long order, int branch, String comment, String correos, String branchName)
    {
        this.order = order;
        this.branch = branch;
        this.message = comment;
        this.correos = correos;
        this.branchName = branchName;
    }

    public PrintLog()
    {
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getBranch()
    {
        return branch;
    }

    public void setBranch(int branch)
    {
        this.branch = branch;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getCorreos()
    {
        return correos;
    }

    public void setCorreos(String correos)
    {
        this.correos = correos;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

}
