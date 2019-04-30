package com.monkeybean.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.DirContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MonkeyBean on 2019/4/25.
 */
@RestController
@RequestMapping("test")
public class TestController {
    private final LdapTemplate ldapTemplate;

    private final PersonRepository personRepository;

    @Autowired
    public TestController(LdapTemplate ldapTemplate, PersonRepository personRepository) {
        this.ldapTemplate = ldapTemplate;
        this.personRepository = personRepository;
    }

    @GetMapping("account/login")
    public String accountLogin(@RequestParam String dn, @RequestParam String pwd) {
        DirContext ctx = ldapTemplate.getContextSource().getContext(dn, pwd);
//        InvalidNameException

        //如果验证成功根据sAMAccountName属性查询用户名和用户所属的组
        return "success";
    }


    @GetMapping("account/list")
    public List<Person> accountList() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
        return list;
    }
}
