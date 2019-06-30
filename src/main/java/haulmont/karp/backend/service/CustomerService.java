package haulmont.karp.backend.service;

import haulmont.karp.backend.models.Customer;


import java.util.List;


public interface CustomerService {

    Customer saveCustomer(Customer customer);

    Customer getCustomerOne(Long id);

    List<Customer> getAllCustomer();

    void deleteCustomer(Long id);

    void deleteCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    List<Customer> findByLastName(String lastName);

    boolean existCustomer(Long id);

    Customer getByLastName(String lastName);
}
