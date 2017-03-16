package zh.co.item.bank.web.manage.service;


import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.web.manage.dao.ManageIndexDao;
import zh.co.item.bank.web.user.dao.UserDao;

@Named
public class ManageIndexService {

    @Inject
    private ManageIndexDao manageIndexDao;

    @Inject
    private UserDao userDao;


}
