package net.cltech.enterprisent.service.impl.enterprisent.common;

import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.service.interfaces.common.ChatService;
import net.cltech.enterprisent.websocket.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios chat para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 03/10/2017
 * @see Creaciòn
 */
@Service
public class ChatServiceEnterpriseNT implements ChatService
{

    @Autowired
    private UserDao userDao;
    @Autowired
    private ChatHandler chatHandler;

    @Override
    public List<AuthorizedUser> list() throws Exception
    {
        List<User> users = userDao.list();
        return chatHandler.getRegisteredUsers()
                .stream()
                .filter(ws -> ws.isOpen() == true)
                .map(session -> session.getAttributes().get("username").toString())
                .map(username -> users.stream().filter(user -> user.getUserName().equalsIgnoreCase(username)).findAny())
                .map(user -> user.orElse(new User()))
                .filter(user -> user.getId() != null)
                .map(user ->
                {
                    AuthorizedUser authorizedUser = new AuthorizedUser();
                    authorizedUser.setId(user.getId());
                    authorizedUser.setUserName(user.getUserName());
                    authorizedUser.setPhoto(user.getPhoto());
                    authorizedUser.setName(user.getName());
                    return authorizedUser;
                })
                .distinct()
                .collect(Collectors.toList());

    }

}
