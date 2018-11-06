package com.homework.demo.controller;

import com.homework.demo.model.Customer;
import com.homework.demo.service.CustomerRepoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepoService customerRepoService;

    // =========================================== Get All Customers ==========================================

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> getAll() {
        LOG.info("getting all Customers");
        List<Customer> customers = customerRepoService.getAllCustomers();

        if (customers == null || customers.isEmpty()){
            LOG.info("no customers found");
            return new ResponseEntity<List<Customer>>(NO_CONTENT);
        }

        return new ResponseEntity<List<Customer>>(customers, OK);
    }


    // =========================================== Get Customer By ID =========================================

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> get(@PathVariable("id") Long id){
        LOG.info("getting customer with id: {}", id);
        Optional<Customer> customer = customerRepoService.findById(id);

        if (customer == null){
            LOG.info("customer with id {} not found", id);
            return new ResponseEntity<Customer>(NOT_FOUND);
        }

        return new ResponseEntity<Customer>(customer.get(),OK);
    }

    // =========================================== Create New Customer ========================================

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody Customer customer, UriComponentsBuilder ucBuilder){
        LOG.info("creating new customer: {}", customer);

        if (customerRepoService.existById(customer.getId())){
            LOG.info("a customer  with name " + customer.getFirstName() +" "+customer.getLastName()+" already exists");
            return new ResponseEntity<Void>(CONFLICT);
        }

        customerRepoService.addUpdateCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/customer/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Void>(headers, CREATED);
    }

    // =========================================== Update Existing Customer ===================================

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customer){
        LOG.info("updating customer: {}", customer);
        Optional<Customer> currentCustomer = customerRepoService.findById(id);

        if (currentCustomer == null){
            LOG.info("Customer with id {} not found", id);
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }

        Customer savedcCustomer = customerRepoService.addUpdateCustomer(customer);
        return new ResponseEntity<Customer>(savedcCustomer, HttpStatus.OK);
    }

   // =========================================== Delete Customer ============================================

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        LOG.info("deleting customer with id: {}", id);
        Optional<Customer> currentCustomer = customerRepoService.findById(id);

        if (currentCustomer == null){
            LOG.info("Unable to delete. Customer with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        customerRepoService.deleteCustomer(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }



}
