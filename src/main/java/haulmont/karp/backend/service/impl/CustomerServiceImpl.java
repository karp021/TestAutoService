package haulmont.karp.backend.service.impl;

import haulmont.karp.backend.models.Customer;
import haulmont.karp.backend.repos.CustomerRepository;
import haulmont.karp.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerOne(Long id) {
        return customerRepository.findOne(id);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }


    @Override
    public void deleteCustomer(Long id) {
        customerRepository.delete(id);
    }

    @Override
    public void deleteCustomer(Customer customer) throws DataIntegrityViolationException {
        customerRepository.delete(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        return customerRepository.findByLastNameStartsWithIgnoreCase(lastName);
    }

    @Override
    public boolean existCustomer(Long id) {
            return customerRepository.exists(id);
    }

    @Override
    public Customer getByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

}
