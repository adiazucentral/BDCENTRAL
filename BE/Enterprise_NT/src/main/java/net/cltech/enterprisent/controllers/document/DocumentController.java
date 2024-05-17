/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.document;

import java.util.Base64;
import java.util.List;
import net.cltech.enterprisent.domain.document.Document;
import net.cltech.enterprisent.service.interfaces.document.DocumentService;
import net.cltech.enterprisent.tools.Tools;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de servicios Rest sobre ordenes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/09/2017
 * @see Creacion
 */
@Api(
        name = "Documento",
        group = "Documentos",
        description = "Servicios Rest sobre los documentos de la orden o resultados."
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/documents")
public class DocumentController
{

    @Autowired
    private DocumentService documentService;

    //------------ LISTA DOCUMENTOS DE LA ORDEN ----------------
    @ApiMethod(
            description = "Lista las documentos de una orden especifica",
            path = "/api/documents/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Document.class)
    @RequestMapping(value = "/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Document>> list(@ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") Long order) throws Exception
    {
        List<Document> list = documentService.list(order);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA DOCUMENTOS DE LOS RESULTADOS ----------------
    @ApiMethod(
            description = "Lista los documentos de una orden y una examen.",
            path = "/api/documents/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Document.class)
    @RequestMapping(value = "/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Document>> list(@ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") Long order, @ApiPathParam(name = "test", description = "Prueba") @PathVariable(name = "test") Integer test) throws Exception
    {
        List<Document> list = documentService.list(order, test);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Guardar documento de una orden o un resultado.",
            path = "/api/documents",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Document.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> save(@ApiBodyObject(clazz = Document.class) @RequestBody Document document) throws Exception
    {
        return new ResponseEntity<>(documentService.saveDocument(document), HttpStatus.OK);
    }

    //------------ ELIMINAR ----------------
    @ApiMethod(
            description = "Eliminar documento de una orden o un resultado.",
            path = "/api/documents",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Document.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> delete(@ApiBodyObject(clazz = Document.class) @RequestBody Document document) throws Exception
    {
        return new ResponseEntity<>(documentService.deleteDocument(document), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/documents/test",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Document.class)
    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> list() throws Exception
    {
        String base64File = Base64.getEncoder().encodeToString(Tools.loadFileAsBytesArray("C:\\reportes\\test_20180208.pdf"));
        Tools.createFile(base64File, "C:\\reportes\\testRsponse_20180208.pdf");
        return new ResponseEntity<>(base64File, HttpStatus.OK);

    }
}
