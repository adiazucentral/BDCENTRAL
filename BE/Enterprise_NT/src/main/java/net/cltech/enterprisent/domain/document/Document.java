/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.document;

import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Documentos de la orden o del resultado.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/12/2017
 * @see Creacion
 */
@ApiObject(
        group = "Documentos",
        name = "Documento",
        description = "Representa documentos de la orden o el resultado"
)
public class Document implements Cloneable
{

    @ApiObjectField(name = "idOrder", description = "Id de la orden.", required = false, order = 1)
    private Long idOrder;
    @ApiObjectField(name = "idTest", description = "Id de la prueba.", required = false, order = 2)
    private Integer idTest;
    @ApiObjectField(name = "file", description = "Archivo", required = true, order = 3)
    private String file;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "date", description = "Fecha", required = true, order = 5)
    private Date date;
    @ApiObjectField(name = "user", description = "Usuario", required = true, order = 6)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "replace", description = "Reemplazar", required = true, order = 7)
    private boolean replace;
    @ApiObjectField(name = "delete", description = "Eliminar", required = true, order = 8)
    private boolean delete;
    @ApiObjectField(name = "fileType", description = "Tipo archivo", required = true, order = 9)
    private String fileType;
    @ApiObjectField(name = "extension", description = "Extensión", required = true, order = 10)
    private String extension;
    @ApiObjectField(name = "viewresul", description = "Ver en reporte de resultados", required = true, order = 11)
    private boolean viewresul;
    @ApiObjectField(name = "path", description = "Ruta del adjunto", required = true, order = 12)
    private String path;

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

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
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

    public boolean isViewresul()
    {
        return viewresul;
    }

    public void setViewresul(boolean viewresul)
    {
        this.viewresul = viewresul;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}
