package org.example.rodeodrivediner_webapp.controllers;

import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.exceptions.InvalidCredentials;
import org.example.rodeodrivediner_webapp.exceptions.UserAlreadyExistsException;
import org.example.rodeodrivediner_webapp.exceptions.UserNotFoundException;
import org.example.rodeodrivediner_webapp.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    //@PreAuthorize("hasRole('Admin')")
    public List<Customer> getUsers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/register")
    public Customer addUser(@RequestBody @Valid Customer user) throws Exception, InvalidCredentials {
        return customerService.registerUser(user);
    }

    @GetMapping("/getUser")
    //@PreAuthorize(("hasRole='Admin'"))
    public Customer getUserU(@RequestParam String username) throws UserNotFoundException {
        return customerService.getUser(username);
    }

    @GetMapping("/getName")
    //@PreAuthorize("hasRole('Admin')")
    public List<Customer> getAllUsersWithName(@RequestParam String name){
        return customerService.getUsers(name);
    }

    @GetMapping("/getID")
    public long getUserId(@RequestParam String username) {
        return customerService.getUserId(username);
    }

    @DeleteMapping("/delete")
    //@PreAuthorize("hasRole('Admin')")
    public void deleteUser(@RequestBody Customer utente){
        customerService.removeUser(utente);
    }

    @PutMapping("/update")
   // @PreAuthorize("hasRole('Admin')")
    public void updateUser(@RequestBody Customer utente) throws Exception, InvalidCredentials {
        customerService.updateUser(utente);
    }




}
