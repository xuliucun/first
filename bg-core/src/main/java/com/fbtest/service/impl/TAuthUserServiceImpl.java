package com.fbtest.service.impl;

import com.fbtest.mapper.autogen.TAuthUserMapper;
import com.fbtest.model.TAuthUser;
import com.fbtest.service.TAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TAuthUserServiceImpl implements TAuthUserService {
    @Resource
    private TAuthUserMapper tAuthUserMapper;

    public boolean addUser(TAuthUser user) {
        //tAuthUserMapper.insert(user);
        return tAuthUserMapper.insert(user)>0;
    }

    public TAuthUser findUserById(Integer id) {
        return tAuthUserMapper.selectByPrimaryKey(id);
    }

    public boolean deleteUserById(Integer id) {
        return tAuthUserMapper.deleteByPrimaryKey(id)>0;
    }

    public boolean modifyUser(TAuthUser user) {
        return tAuthUserMapper.updateByPrimaryKey(user)>0;
    }
}
