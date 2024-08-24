package com.klu.controller;

import com.klu.entity.Cart;
import com.klu.entity.Customer;
import com.klu.service.CartService;
import com.klu.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shop/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Customer>> getCustomersByName(@RequestParam String name) {
        List<Customer> customers = customerService.getCustomersByName(name);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/findByCity")
    public ResponseEntity<List<Customer>> getCustomersByCity(@RequestParam("city") String city) {
        List<Customer> customers = customerService.getCustomersByCity(city);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/findByPincode")
    public ResponseEntity<List<Customer>> getCustomersByPincode(@RequestParam("pincode") String pincode) {
        List<Customer> customers = customerService.getCustomersByPincode(pincode);
        return ResponseEntity.ok(customers);
    }



    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        try {
            // Fetch the cart associated with the customer
            Cart cart = cartService.findByCustomer_Id(id);
            if (cart != null) {
                // Optionally clear the cart or delete it
                cartService.removeCart(id);
            }

            // Now delete the customer
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Log the exception and return a server error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer updatedCustomer) {
        Optional<Customer> existingCustomerOptional = customerService.getCustomerById(id);
        
        if (!existingCustomerOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if customer does not exist
        }

        Customer existingCustomer = existingCustomerOptional.get();
        
        // Update the existing customer's details with the new data
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setCity(updatedCustomer.getCity());
        existingCustomer.setPincode(updatedCustomer.getPincode());
        
        // Save the updated customer
        Customer savedCustomer = customerService.saveCustomer(existingCustomer);
        
        return ResponseEntity.ok(savedCustomer); // Return 200 OK with the updated customer details
    }
}
