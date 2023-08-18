package com.rushional.cities.security;

import com.rushional.cities.models.CustomerEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomerDetails implements UserDetails {

  private final CustomerEntity customerEntity;

  public CustomerDetails(CustomerEntity customerEntity) {
    this.customerEntity = customerEntity;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return customerEntity.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return customerEntity.getPassword();
  }

  @Override
  public String getUsername() {
    return customerEntity.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public CustomerEntity getCustomerEntity() {
    return customerEntity;
  }
}
