package org.example.rodeodrivediner_webapp.services;

import org.example.rodeodrivediner_webapp.configurations.KeycloakConfig;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.exceptions.InvalidCredentials;
import org.example.rodeodrivediner_webapp.exceptions.UserAlreadyExistsException;
import org.example.rodeodrivediner_webapp.exceptions.UserNotFoundException;
import org.example.rodeodrivediner_webapp.repositories.CustomerRepository;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import java.util.*;
import java.util.regex.Pattern;
import jakarta.ws.rs.core.Response;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = false)
    public Customer registerUser(Customer utente) throws Exception, InvalidCredentials {
        if(customerRepository.existsByEmail(utente.getEmail()) || customerRepository.existsByUsername(utente.getUsername())) {
            throw new UserAlreadyExistsException("Email or username already exists");
        } else if ((Pattern.matches("[a-zA-Z ]+", utente.getFirstName()) &&
                Pattern.matches("[a-zA-Z ]+", utente.getLastName()) &&
                Pattern.matches("[a-zA-Z0-9]{1,15}+", utente.getUsername())&&
                Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$", utente.getPassword()) &&
                Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z]+.[a-zA-Z]{2,3}", utente.getEmail())
        )) {
            add(utente);
            customerRepository.save(utente);
        }
        else {
            throw new InvalidCredentials("Invalid credentials");
        }
        return utente;
    }

    @Transactional(readOnly = true)
    public Customer getUser(String username) throws UserNotFoundException {
        if (customerRepository.existsByUsernameIgnoreCase(username))
            return customerRepository.findByUsernameIgnoreCase(username);
        throw new UserNotFoundException();
    }

    @Transactional(readOnly = true)
    public long getUserId(String username) {
        if (customerRepository.existsByUsernameIgnoreCase(username))
            return customerRepository.findByUsernameIgnoreCase(username).getCustId();
        return -1;
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Customer> getUsers (String name){
        Set<Customer> users = new HashSet<>();
        users.addAll(customerRepository.findByUsernameContainingOrderByUsernameIgnoreCase(name));
        users.addAll(customerRepository.findByFirstNameContainingIgnoreCase(name));
        users.addAll(customerRepository.findByLastNameContainingIgnoreCase(name));
        return new ArrayList<>(users);

    }
    @Transactional(readOnly = false)
    public void removeUser(Customer utente) {
        if (customerRepository.existsById(utente.getCustId())) {
            Optional<Customer> ut = customerRepository.findById(utente.getCustId());
            if (ut.isPresent()) customerRepository.delete(ut.get());

        }
    }
    @Transactional(readOnly = false)
    public Customer updateUser(Customer utente) throws Exception, InvalidCredentials {
        for (Customer u: getAllCustomers()) {
            if (utente.getUsername().equals(u.getUsername()) || utente.getEmail().equals(u.getEmail())) {
                if ((Pattern.matches("[a-zA-Z ]+", utente.getFirstName()) &&
                        Pattern.matches("[a-zA-Z ]+", utente.getLastName()) &&
                        Pattern.matches("[a-zA-Z0-9]{1,15}+", utente.getUsername())&&
                        Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$", utente.getPassword()) &&
                        Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z]+.[a-zA-Z]{2,3}", utente.getEmail())
                )) {
                    customerRepository.save(utente);
                }
                else {
                    throw new InvalidCredentials("Invalid credentials");
                }
                return utente;
            }
        }
        throw new UserNotFoundException();
    }

    public void add(Customer utente) throws UserAlreadyExistsException {
        Keycloak keycloak = KeycloakConfig.getInstance();

        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(utente.getUsername());
        user.setFirstName(utente.getFirstName());
        user.setLastName(utente.getLastName());
        user.setEmail(utente.getEmail());
        user.setCredentials(Collections.singletonList(createPasswordCredentials(utente.getPassword())));
        user.setEmailVerified(true);


//       Get realm
        RealmResource realmResource = keycloak.realm(KeycloakConfig.realm);
        UsersResource usersResource = realmResource.users();


//      Set password
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(utente.getPassword());
        user.setCredentials(Collections.singletonList(credentialRepresentation));

//      Create user (requires manage-users role)
        Response response=usersResource.create(user);
        if (response.getStatus() == 201) {
            System.out.println("User created successfully.");
        } else {
            System.err.println("Failed to create user. HTTP error code: " + response.getStatus());
            System.err.println("Error message: " + response.getStatusInfo().getReasonPhrase());
            if (response.hasEntity())
                System.err.println("Error details: " + response.readEntity(String.class));

            response.close();
            throw new UserAlreadyExistsException("user not created");
        }
    }
    public static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}


