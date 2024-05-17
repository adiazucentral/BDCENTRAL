/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.microbiology.CommentMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;

import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de microbiologia para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/02/2018
 * @see Creacion
 */
@Service
public class CommentServiceEnterpriseNT implements CommentService
{

    @Autowired
    private CommentDao dao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public List<CommentOrder> listCommentOrder(Long idOrder, Integer idPatient) throws Exception
    {
        return dao.listCommentOrder(idOrder, idPatient);
    }

    @Override
    public List<CommentMicrobiology> listCommentMicrobiology(long idOrder, Integer idTest, Integer idSample) throws Exception
    {
        return dao.listCommentMicrobiology(idOrder, idTest, idSample);
    }

    @Override
    public int commentOrder(List<CommentOrder> commentsOrder) throws Exception
    {
        int quantity = 0;
        List<AuditOperation> audit = new ArrayList<>();
        AuthorizedUser session = JWT.decode(request);
        if (commentsOrder != null && !commentsOrder.isEmpty())
        {
            
            List<CommentOrder> commentsInsert = commentsOrder.stream().filter(comment -> comment.getState() == 1).collect(Collectors.toList());
            List<CommentOrder> commentsUpdate = commentsOrder.stream().filter(comment -> comment.getState() == 2).collect(Collectors.toList());
            List<CommentOrder> commentsDelete = commentsOrder.stream().filter(comment -> comment.getState() == 3).collect(Collectors.toList());

            if(commentsInsert.size() > 0) {
                quantity = quantity + dao.insertCommentOrder(commentsInsert);
            }
            Long orderNumer = null;
            
            if(quantity > 0){
                for (CommentOrder comment : commentsInsert)
                {
                    if(commentsOrder.get(0).getType() == 1){
                        audit.add(new AuditOperation(commentsOrder.get(0).getIdRecord(), null, 0, AuditOperation.ACTION_INSERT, commentsOrder.get(0).getType() == 1 ? AuditOperation.TYPE_ORDERCOMMENT : AuditOperation. TYPE_PATIENTDIAGNOSTIC, Tools.jsonObject(comment), null, null, null, null));
                        orderNumer = commentsOrder.get(0).getIdRecord();
                    }
                    else {
                        audit.add(new AuditOperation(0L, commentsOrder.get(0).getIdRecord().intValue() , 0, AuditOperation.ACTION_INSERT, commentsOrder.get(0).getType() == 1 ? AuditOperation.TYPE_ORDERCOMMENT : AuditOperation. TYPE_PATIENTDIAGNOSTIC, Tools.jsonObject(comment), null, null, null, null));
                    }
                    
                }
                
                if ("True".equals(configurationServices.get("TransmitirComentario").getValue()) && orderNumer != null)
                {
                    integrationMiddlewareService.sendOrderASTM(orderNumer, null, null, Constants.ENTRY, null, null, null, session.getBranch() , false);
                }
            }
            if(commentsUpdate.size() > 0 ) {
                quantity = quantity + dao.updateCommentOrder(commentsUpdate);
            }

            if(quantity > 0){
                for (CommentOrder comment : commentsUpdate)
                {
                    if(commentsOrder.get(0).getType() == 1){
                        audit.add(new AuditOperation(commentsOrder.get(0).getIdRecord(), null, 0, AuditOperation.ACTION_INSERT, commentsOrder.get(0).getType() == 1 ? AuditOperation.TYPE_ORDERCOMMENT : AuditOperation. TYPE_PATIENTDIAGNOSTIC, Tools.jsonObject(comment), null, null, null, null));
                    }
                    else {
                        audit.add(new AuditOperation(0L, commentsOrder.get(0).getIdRecord().intValue() , 0, AuditOperation.ACTION_INSERT, commentsOrder.get(0).getType() == 1 ? AuditOperation.TYPE_ORDERCOMMENT : AuditOperation. TYPE_PATIENTDIAGNOSTIC, Tools.jsonObject(comment), null, null, null, null));
                    }
                }
            }
            
            if(commentsDelete.size() > 0) {
                quantity = quantity + dao.deleteCommentOrder(commentsDelete);
            }

            if(quantity > 0){
                for (CommentOrder comment : commentsDelete)
                {
                    if(commentsOrder.get(0).getType() == 1){
                        audit.add(new AuditOperation(commentsOrder.get(0).getIdRecord(), null, 0, AuditOperation.ACTION_INSERT, commentsOrder.get(0).getType() == 1 ? AuditOperation.TYPE_ORDERCOMMENT : AuditOperation. TYPE_PATIENTDIAGNOSTIC, Tools.jsonObject(comment), null, null, null, null));
                    }
                    else {
                        audit.add(new AuditOperation(0L, commentsOrder.get(0).getIdRecord().intValue() , 0, AuditOperation.ACTION_INSERT, commentsOrder.get(0).getType() == 1 ? AuditOperation.TYPE_ORDERCOMMENT : AuditOperation. TYPE_PATIENTDIAGNOSTIC, Tools.jsonObject(comment), null, null, null, null));
                    }
                }
            }
            
            trackingService.registerOperationTracking(audit, commentsOrder.get(0).getUser().getId());
        }

        return quantity;
    }

    @Override
    public int commentMicrobiology(List<CommentMicrobiology> commentsMicrobiology) throws Exception
    {
        int quantity = 0;
        if (commentsMicrobiology != null && !commentsMicrobiology.isEmpty())
        {
            List<CommentMicrobiology> commentsInsert = commentsMicrobiology.stream().filter(comment -> comment.getState() == 1).collect(Collectors.toList());
            List<CommentMicrobiology> commentsUpdate = commentsMicrobiology.stream().filter(comment -> comment.getState() == 2).collect(Collectors.toList());
            List<CommentMicrobiology> commentsDelete = commentsMicrobiology.stream().filter(comment -> comment.getState() == 3).collect(Collectors.toList());

            quantity = quantity + dao.insertCommentMicrobiology(commentsInsert);
            quantity = quantity + dao.updateCommentMicrobiology(commentsUpdate);
            quantity = quantity + dao.deleteCommentMicrobiology(commentsDelete);

            List<CommentMicrobiology> commentsAudit = commentsMicrobiology.stream().filter(comment -> comment.getState() != 0 && comment.getState() != 1).collect(Collectors.toList());
            commentsAudit.addAll(commentsInsert);
            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(commentsMicrobiology.get(0).getOrder(), commentsMicrobiology.get(0).getIdTest(),null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICROCOMMENT, Tools.jsonObject(commentsAudit), null, null,null, null));
            trackingService.registerOperationTracking(audit);
        }

        return quantity;
    }

    @Override
    public List<CommentMicrobiology> listCommentMicrobiologyTracking(long idOrder, int idTest) throws Exception
    {
        List<AuditEvent> audit = dao.listTrackingMicrobiologyComment(idOrder, idTest);

        List<CommentMicrobiology> comments = new ArrayList<>();
        for (AuditEvent aud : audit)
        {
            comments.addAll(Tools.jsonList(aud.getCurrent(), CommentMicrobiology.class));
        }

        return comments;
    }

    @Override
    public List<CommentOrder> listCommentOrderTracking(long idRecord, int type) throws Exception
    {
        List<AuditEvent> audit = dao.listTrackingOrderComment(idRecord, type);

        List<CommentOrder> comments = new ArrayList<>();
        for (AuditEvent aud : audit)
        {
            comments.addAll(Tools.jsonList(aud.getCurrent(), CommentOrder.class));
            
        }

        return comments;
    }

}
