package com.monkeybean.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MonkeyBean on 2019/4/25.
 */
@RestController
@RequestMapping("test")
public class TestController {
    /**
     * LdapTemplate可对Ldap执行crud操作
     */
    private final LdapTemplate ldapTemplate;

    private final PersonRepository personRepository;

    @Value("${spring.ldap.username}")
    private String adminUserName;

    @Value("${spring.ldap.password}")
    private String adminPwd;

    @Autowired
    public TestController(LdapTemplate ldapTemplate, PersonRepository personRepository) {
        this.ldapTemplate = ldapTemplate;
        this.personRepository = personRepository;
    }

    /**
     * 测试登录
     *
     * @param dn  用户
     * @param pwd 密码
     */
    @GetMapping("account/login")
    public String accountLogin(@RequestParam String dn, @RequestParam String pwd) throws NamingException {
        // DirContext ctx = ldapTemplate.getContextSource().getContext(dn, pwd);
        DirContext ctx = ldapTemplate.getContextSource().getContext(adminUserName, adminPwd);

        //相关操作

        //关闭ldap连接
        LdapUtils.closeContext(ctx);
        return "success";
    }


    /**
     * 列出所有账户, 高权限, 仅测试使用
     */
    @GetMapping("account/list")
    public List<Person> accountList() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
        return list;
    }
}
