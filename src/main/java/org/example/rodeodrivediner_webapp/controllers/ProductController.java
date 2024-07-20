package org.example.rodeodrivediner_webapp.controllers;

import jakarta.validation.Valid;
import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.exceptions.IllegalQuantityException;
import org.example.rodeodrivediner_webapp.exceptions.NoProductException;
import org.example.rodeodrivediner_webapp.exceptions.ProductAlreadyExistsException;
import org.example.rodeodrivediner_webapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;


    @PostMapping("/add")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity create(@RequestBody @Valid Product product) throws ProductAlreadyExistsException {
        try {
            productService.addProduct(product);

            Map<String, String> response = new HashMap<>();
            response.put("message", "added successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(ProductAlreadyExistsException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch(IllegalArgumentException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (IllegalQuantityException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('Admin')")
    public void deleteProduct(@RequestBody Map<String, Object> requestBody) throws NoProductException {
        int idProd = Integer.parseInt(requestBody.get("id").toString());

        Product prod = this.getProductByID(idProd);
        productService.removeProduct(prod);
    }

    @GetMapping("/id")
    public Product getProductByID(@RequestParam int id) throws NoProductException {
        return productService.getProdotto(id);
    }

    @GetMapping("/all")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "25") int limit,
                                 @RequestParam(required = false)  String productName,
                                 @RequestParam(required = false) Sort.Direction sortType) {
        List<Product> result = productService.getProductFilters(page,limit, productName, sortType);
        if ( result.size() <= 0 ) {
            return new ResponseEntity<>("No results!", HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_name")
    public ResponseEntity getByName(@RequestParam(value = "name") String name) {
        List<Product> result = productService.showProductByName(0,5, name);
        if ( result.size() <= 0 ) {
            return new ResponseEntity<>("No results!", HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public String getProducts(Model model){
        model.addAttribute("products", productService.showAllProducts(0,5));
        //aggiunge al modello products la lista di prodotti
        return "products";
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity updateProduct(@RequestBody Product prodotto) throws NoProductException {
        try {
            productService.updateProduct(prodotto);

            Map<String, String> response = new HashMap<>();
            response.put("message", "added successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(IllegalArgumentException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (IllegalQuantityException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }


}
