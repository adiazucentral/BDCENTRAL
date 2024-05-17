/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import java.util.Date;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa documentos del caso
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/03/2021
 * @see Creacion
 */
@ApiObject(
        group = "Patología",
        name = "Archivo",
        description = "Representa documentos del caso"
)
public class File extends PathologyAudit implements Cloneable
{
    @ApiObjectField(name = "id", description = "Id del archivo", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "idCase", description = "Id del caso", required = false, order = 2)
    private Integer idCase;
    @ApiObjectField(name = "file", description = "Archivo", required = true, order = 3)
    private String file;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "date", description = "Fecha", required = true, order = 5)
    private Date date;
    @ApiObjectField(name = "replace", description = "Reemplazar", required = true, order = 6)
    private boolean replace;
    @ApiObjectField(name = "delete", description = "Eliminar", required = true, order = 7)
    private boolean delete;
    @ApiObjectField(name = "fileType", description = "Tipo archivo", required = true, order = 8)
    private String fileType;
    @ApiObjectField(name = "extension", description = "Extensión", required = true, order = 9)
    private String extension;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public Integer getIdCase() {
        return idCase;
    }

    public void setIdCase(Integer idCase) {
        this.idCase = idCase;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
