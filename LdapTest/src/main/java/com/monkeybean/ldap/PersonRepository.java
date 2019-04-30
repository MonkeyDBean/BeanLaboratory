package com.monkeybean.ldap;

import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

/**
 * Created by MonkeyBean on 2019/4/25.
 */
public interface PersonRepository extends CrudRepository<Person, Name> {
}
