package com.rushional.cities.security;

import com.rushional.cities.models.Customer;
import com.rushional.cities.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerDetailsService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Customer> customerOptional = customerRepository.findByUsername(username);
    UserDetails userDetails = new CustomerDetails(customerOptional.orElseThrow(
            () -> new UsernameNotFoundException("User with username " + username + " not found")
    ));

    log.info("In CustomerDetailsService: customer with username {} successfully loaded", username);
    return userDetails;
  }
}
