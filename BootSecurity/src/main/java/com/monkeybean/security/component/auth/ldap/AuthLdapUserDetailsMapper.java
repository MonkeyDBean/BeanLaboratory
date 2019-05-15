package com.monkeybean.security.component.auth.ldap;

import com.monkeybean.security.component.auth.AuthUserDetails;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by MonkeyBean on 2019/4/20.
 */
@Component
public class AuthLdapUserDetailsMapper extends LdapUserDetailsMapper {

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority > authorities) {
        AuthUserDetails authUserDetails = new AuthUserDetails();
        UserDetails userDetails = super.mapUserFromContext(ctx, username, authorities);
        authUserDetails.setUserName(userDetails.getUsername());
        authUserDetails.setPassword(userDetails.getPassword());
        authUserDetails.setIsDatabase(false);
        authUserDetails.setEnabled(true);
        return authUserDetails;
    }
}

