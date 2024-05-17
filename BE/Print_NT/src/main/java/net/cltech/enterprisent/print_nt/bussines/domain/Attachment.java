/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.bussines.domain;

/**
 * Representa Documentos de la orden o del resultado.
 *
 * @version 1.0.0
 * @author equijano
 * @since 28/05/2019
 * @see Creacion
 */
public class Attachment implements Cloneable
{

    private Long idOrder;
    private Integer idTest;
    private String file;
    private String name;
    private boolean replace;
    private boolean delete;
    private String fileType;
    private String extension;

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public Long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(Long idOrder)
    {
        this.idOrder = idOrder;
    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public String getFile()
    {
        return file;
    }

    public void setFile(String file)
    {
        this.file = file;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isReplace()
    {
        return replace;
    }

    public void setReplace(boolean replace)
    {
        this.replace = replace;
    }

    public boolean isDelete()
    {
        return delete;
    }

    public void setDelete(boolean delete)
    {
        this.delete = delete;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

}
