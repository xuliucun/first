package com.fbtest.controller;

import com.fbtest.model.TAuthUser;
import com.fbtest.service.TAuthUserService;
import com.fbtest.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * restful api
 * */
@RestController
@RequestMapping("/user")
public class TAuthUserController {
    @Autowired
    private TAuthUserService tAuthUserService;

    //
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    @ResponseBody
    public String queryUserById(@PathVariable Integer id){
        return JsonUtils.objectToJson(tAuthUserService.findUserById(id));
    }


    @RequestMapping(method = RequestMethod.POST)
    public String saveUser(@RequestBody TAuthUser user){

        if (tAuthUserService.addUser(user)){
            return "{\"code\":\"成功\"}";
        }else{
            return "{\"code\":\"fail\"}";
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateUser(@RequestBody TAuthUser user){
        tAuthUserService.modifyUser(user);
        return "";
    }


    //
    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUserById(@PathVariable Integer id){
        tAuthUserService.deleteUserById(id);
        return "";
    }








}
