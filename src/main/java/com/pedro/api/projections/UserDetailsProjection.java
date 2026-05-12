package com.pedro.api.projections;

public interface UserDetailsProjection {
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}