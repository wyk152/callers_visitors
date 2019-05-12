package com.easymicro.rest.modular.business.controller;

import com.easymicro.persistence.modular.model.system.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class TestController {

    @RequestMapping(value = "send")
    public Object send() {
        User user = new User();
        user.setUpdatetime(new Date());
        user.setAccount("admin");
        user.setBirthday(new Date());
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user);

        list.add(user);
        list.add(user);
        list.add(user);
        return list;
    }
}
