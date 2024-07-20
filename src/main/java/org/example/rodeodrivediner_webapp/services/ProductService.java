package org.example.rodeodrivediner_webapp.services;

import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.exceptions.IllegalQuantityException;
import org.example.rodeodrivediner_webapp.exceptions.NoProductException;
import org.example.rodeodrivediner_webapp.exceptions.ProductAlreadyExistsException;
import org.example.rodeodrivediner_webapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> showProductByName(int page, int limit, String name){
        Pageable pageable = PageRequest.of(page,limit);
        Page<Product> pagedResult = productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name, pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }
    @Transactional(readOnly = true)
    public List<Product> orderProductsByPrice(int page, int limit, Sort.Direction sortType){
        Sort sort = Sort.by(sortType, "price");
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> pagedResult = productRepository.findAll(pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Product getProdotto(int id) throws NoProductException {
        Optional<Product> p = productRepository.findById(id);
        if (p.isPresent())
            return p.get();
        else
            throw new NoProductException("prodotto inesistente");
    }

    @Transactional(readOnly = true)
    public List<Product> getProductFilters(int page, int limit, String name, Sort.Direction sortType){
        if( name== null && sortType == null){
            return showAllProducts(page,limit);
        }
        else if (name !=null && sortType == null){
            return showProductByName(page, limit, name);
        }
        else if (name ==null && sortType!=null){
            return orderProductsByPrice(page,limit,sortType);
        }
        else if (name !=null && sortType !=null){ //tutte e due contemporaneamente no
            return null;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, int limit) {
        Pageable pageable = PageRequest.of(pageNumber,limit);
        Page<Product> pagedResult = productRepository.findAll(pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = false)
    public Product addProduct(Product prodotto) throws ProductAlreadyExistsException, IllegalQuantityException {
        if (Pattern.matches("[a-zA-Z0-9 ,:'\"!\\-.?]+", prodotto.getName())
                && !productRepository.existsByName(prodotto.getName())
                //&& Pattern.matches("[a-zA-Z0-9 ,:'\"!\\-.?]+", prodotto.getDescription())
                && prodotto.getPrice()==(int)prodotto.getPrice() && prodotto.getPrice()>0 &&
                prodotto.getQuantity()==(int)prodotto.getQuantity() && prodotto.getQuantity()>0
        ){
            return productRepository.save(prodotto);
        } else {
            if(productRepository.existsByName(prodotto.getName())){
                throw new ProductAlreadyExistsException();
            }
            if(prodotto.getQuantity()<=0 || prodotto.getPrice()<=0) {
                throw new IllegalQuantityException();
            }
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = false)
    public Product updateProduct(Product updatedProduct) throws NoProductException, IllegalQuantityException {
        Optional<Product> existingProductOptional = productRepository.findByProdId(productRepository.getProdIdByName(updatedProduct.getName()));
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            if(updatedProduct.getQuantity()<=0 || updatedProduct.getPrice()<=0){
                throw new IllegalQuantityException();
            }
            // Aggiorna l'entità esistente con i valori ricevuti dall'aggiornamento
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setQuantity(updatedProduct.getQuantity());
            // Salva l'entità aggiornata nel database
            return productRepository.save(existingProduct);
        } else {
            throw new NoProductException("Prodotto non trovato");
        }
    }
    @Transactional(readOnly = false)
    public void removeProduct(Product prodotto) throws NoProductException {
        if (productRepository.existsById(prodotto.getProdId())) {
            Optional<Product> p = productRepository.findById(prodotto.getProdId());
            if (p.isPresent()) productRepository.delete(p.get());
            else throw new NoProductException("Prodotto non trovato");
        }
        else throw new NoProductException("Prodotto non trovato");
    }

    //restituisce tutti i prodotti ordinati per prezzo
    public Page<Product> getProductsOrderByPrice(int page, int limit, Sort.Direction sortType) {
        Sort sort=Sort.by(sortType,"price");
        Pageable pageable= PageRequest.of(page,limit,sort);
        return productRepository.findAll(pageable);
    }



}
