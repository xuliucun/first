package com.fbtest.service;

import com.fbtest.model.TAuthUser;

public interface TAuthUserService {
    boolean addUser(TAuthUser user);
    TAuthUser findUserById(Integer id);
    boolean deleteUserById(Integer id);
    boolean modifyUser(TAuthUser user);
}
