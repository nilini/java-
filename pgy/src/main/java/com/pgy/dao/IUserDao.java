package com.pgy.dao;

import com.pgy.model.User;

public interface IUserDao {
    User selectUser(long id);
}
