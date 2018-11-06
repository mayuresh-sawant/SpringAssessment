package com.homework.demo.service;

import com.homework.demo.dao.CustomerRepo;
import com.homework.demo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerRepoService {

    @Autowired
    private CustomerRepo customerRepo;


    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepo.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepo.findById(id);
    }


    public Customer addUpdateCustomer(Customer customer) {
         return customerRepo.save(customer);
    }

    public boolean existById(Long customerId) {
        return customerRepo.existsById(customerId);
    }



    public void deleteCustomer(Long customerId) {
          customerRepo.deleteById(customerId);
    }


}
