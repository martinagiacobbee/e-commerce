package org.example.rodeodrivediner_webapp.controllers;

import jakarta.validation.Valid;
import org.example.rodeodrivediner_webapp.entities.Cart;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.exceptions.*;
import org.example.rodeodrivediner_webapp.repositories.CustomerRepository;
import org.example.rodeodrivediner_webapp.security.Utils;
import org.example.rodeodrivediner_webapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/purchases")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerRepository customerRepository;




    @PostMapping("/add/in")
    @PreAuthorize("hasRole('User')" )
    public ResponseEntity<Cart> addIn(@RequestBody List<Product> prodotti) throws IllegalQuantityException, PriceChangedException {
        Customer utente=customerRepository.findByUsernameIgnoreCase(Utils.getUsername());
        return new ResponseEntity(cartService.setProductsInCarrello(prodotti, utente),HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Cart> getCart (@RequestParam int utenteId) throws UserNotFoundException {
        return cartService.getCarrello(utenteId);
    }

    @GetMapping("/all/{user}")
    public List<Cart> getPurchases(@Valid @PathVariable("user") String user) {
        try {
            return cartService.getCarrello(customerRepository.findByUsernameIgnoreCase(user).getCustId());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }
    @GetMapping("/all/products")
    public List<Product> getCartProducts(@RequestParam int cartId, @RequestParam int utenteId) throws UserNotFoundException {
        return cartService.getCarrelloProducts(cartId, utenteId);

    }

    @Transactional(readOnly = false)
    @PutMapping("/update")
    public ResponseEntity<Cart> updateCart(@RequestBody List<Product> prodotti, @RequestParam int carrelloId) throws IllegalQuantityException, PriceChangedException {
        return new ResponseEntity(cartService.updateCarrello(carrelloId, prodotti),HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Cart> deleteProdFromCart(@RequestParam int carrelloId, @RequestParam int prodId, @RequestParam int quantity) throws IllegalQuantityException, PriceChangedException, NoProductException {
        return new ResponseEntity(cartService.removeProdCart(carrelloId, prodId, quantity),HttpStatus.OK);
    }

   /* @GetMapping("/{user}/{startDate}/{endDate}")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity getPurchasesInPeriod(@Valid @PathVariable("user") Customer user, @PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date start,
                                               @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date end) {
        try {
            List<Cart> result = cartService.getPurchasesByUserInPeriod(user, start, end);
            if (result.isEmpty()) {
                return new ResponseEntity<>("No results!", HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found XXX!", e);
        } catch (DateWrongRangeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be previous end date XXX!", e);
        }
    }*/

}
