package com.homework.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.demo.filter.CORSFilter;
import com.homework.demo.model.Customer;
import com.homework.demo.model.ResidentialAddress;
import com.homework.demo.service.CustomerRepoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerRepoService customerRepoService;

    @InjectMocks
    private CustomerController customerController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .addFilters(new CORSFilter())
                .build();
    }

    // =========================================== Get All Customers ==========================================

    @Test
    public void test_get_all_success() throws Exception {
        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L,"Unit 18 43 Cremorne Street Melbourne 3000");
        ResidentialAddress residentialAddress2 = new ResidentialAddress(2L, "Unit 30 23 Collins Street Melbourne 3000");

        List<Customer> customers = Arrays.asList(
                new Customer(1L,"Mayur","Prabhudas", "Sawant", "MS", "MR", residentialAddress1, "Male", 90, "Y" ),
                new Customer(2L,"Jack","Jane", "Sparrow", "JS", "MR", residentialAddress2, "Male", 50, "Y" ));
        when(customerRepoService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Mayur")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jack")));
        verify(customerRepoService, times(1)).getAllCustomers();
        verifyNoMoreInteractions(customerRepoService);
    }

    // =========================================== Get Customer By ID =========================================

    @Test
    public void test_get_by_id_success() throws Exception {
        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L, "Unit 18 43 Cremorne Street Melbourne 3000");
        Customer customer = new Customer(1L,"Ashley","Jay", "Michael", "AM", "MS", residentialAddress1, "Female", 20, "N" );

        when(customerRepoService.findById(1L)).thenReturn(java.util.Optional.of(customer));

        mockMvc.perform(get("/customers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Ashley")));

        verify(customerRepoService, times(1)).findById(1L);
        verifyNoMoreInteractions(customerRepoService);
    }


    @Test
    public void test_get_by_id_fail_404_not_found() throws Exception {

        when(customerRepoService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/customers/{id}", 1))
                .andExpect(status().isNotFound());

        verify(customerRepoService, times(1)).findById(1L);
        verifyNoMoreInteractions(customerRepoService);
    }

    // =========================================== Create New Customer ========================================

    @Test
    public void test_create_customer_success() throws Exception {
        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L,"Unit 18 43 Cremorne Street Melbourne 3000");
        Customer customer = new Customer(10L,"Chan","Jay", "Kew", "CA", "Mr", residentialAddress1, "Male", 30, "N" );

        when(customerRepoService.existById(10L)).thenReturn(false);
        when(customerRepoService.addUpdateCustomer(customer)).thenReturn(customer);

        mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/customers/10")));

        verify(customerRepoService, times(1)).existById(10L);
        verify(customerRepoService, times(1)).addUpdateCustomer(customer);
        verifyNoMoreInteractions(customerRepoService);
    }

    @Test
    public void test_create_customer_fail_409_conflict() throws Exception {
        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L,"Unit 18 43 Cremorne Street Melbourne 3000");
        Customer customer = new Customer(10L,"Chan","Jay", "Kew", "CA", "Mr", residentialAddress1, "Male", 30, "N" );

        when(customerRepoService.existById(10L)).thenReturn(true);

        mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customer)))
                .andExpect(status().isConflict());

        verify(customerRepoService, times(1)).existById(10L);
        verifyNoMoreInteractions(customerRepoService);
    }

    // =========================================== Update Existing Customer ===================================

    @Test
    public void test_update_customer_success() throws Exception {

        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L,"Unit 18 43 Cremorne Street Melbourne 3000");
        Customer customer = new Customer(10L,"Chan","Jay", "Kew", "CA", "Mr", residentialAddress1, "Male", 30, "N" );

        when(customerRepoService.findById(10L)).thenReturn(java.util.Optional.of(customer));
        when(customerRepoService.addUpdateCustomer(customer)).thenReturn(customer);

        mockMvc.perform(
                put("/customers/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customer)))
                .andExpect(status().isOk());

        verify(customerRepoService, times(1)).findById(customer.getId());
        verify(customerRepoService, times(1)).addUpdateCustomer(customer);
        verifyNoMoreInteractions(customerRepoService);
    }

    @Test
    public void test_update_customer_fail_404_not_found() throws Exception {

        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L,"Unit 18 43 Cremorne Street Melbourne 3000");
        Customer customer = new Customer(20L,"No Customer","NA", "No Customer Test", "CA", "Mr", residentialAddress1, "Male", 30, "N" );

        when(customerRepoService.findById(customer.getId())).thenReturn(null);

        mockMvc.perform(
                put("/customers/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customer)))
                .andExpect(status().isNotFound());

        verify(customerRepoService, times(1)).findById(customer.getId());
        verifyNoMoreInteractions(customerRepoService);
    }

    // =========================================== Delete Customer ============================================

    @Test
    public void test_delete_customer_success() throws Exception {
        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L,"Unit 18 43 Cremorne Street Melbourne 3000");
        Customer customer = new Customer(1L,"Ashley","Jay", "Michael", "AM", "MS", residentialAddress1, "Female", 20, "N" );

        when(customerRepoService.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
        doNothing().when(customerRepoService).deleteCustomer(customer.getId());

        mockMvc.perform(
                delete("/customers/{id}", customer.getId()))
                .andExpect(status().isOk());

        verify(customerRepoService, times(1)).findById(customer.getId());
        verify(customerRepoService, times(1)).deleteCustomer(customer.getId());
        verifyNoMoreInteractions(customerRepoService);
    }

    @Test
    public void test_delete_customer_fail_404_not_found() throws Exception {
        ResidentialAddress residentialAddress1 = new ResidentialAddress(1L,"Unit 18 43 Cremorne Street Melbourne 3000");
        Customer customer = new Customer(20L,"No Customer","NA", "No Customer Test", "CA", "Mr", residentialAddress1, "Male", 30, "N" );

        when(customerRepoService.findById(customer.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/customers/{id}", customer.getId()))
                .andExpect(status().isNotFound());

        verify(customerRepoService, times(1)).findById(customer.getId());
        verifyNoMoreInteractions(customerRepoService);
    }

    // =========================================== CORS Headers ===========================================

    @Test
    public void test_cors_headers() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE"))
                .andExpect(header().string("Access-Control-Allow-Headers", "*"))
                .andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}