package com.bolsadeideas.springboot.app.util;

import com.bolsadeideas.springboot.app.models.dao.IUserDao;
import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtil {

    @Autowired
    IUserDao iUserDao;

    public Optional<User> getCurrentUser(){

        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return iUserDao.findById(principal.getId());
    }
}
