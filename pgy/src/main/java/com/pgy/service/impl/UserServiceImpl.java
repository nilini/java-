package com.pgy.service.impl;

import com.pgy.dao.IUserDao;
import com.pgy.model.User;
import com.pgy.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserDao userDao;
    @Override
    public User selectUser(long userId) {
        return this.userDao.selectUser(userId);
    }
}
