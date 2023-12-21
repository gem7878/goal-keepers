/*package com.goalkeepers.server.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    
    private String principal;
    private String credentials;

    public CustomAuthenticationToken(String principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public CustomAuthenticationToken() {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}*/
