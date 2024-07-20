package org.example.rodeodrivediner_webapp.services;

import jakarta.persistence.EntityManager;
import org.example.rodeodrivediner_webapp.entities.Cart;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.entities.ProductInCart;
import org.example.rodeodrivediner_webapp.exceptions.*;
import org.example.rodeodrivediner_webapp.repositories.CartRepository;
import org.example.rodeodrivediner_webapp.repositories.CustomerRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductInCartRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Cart setProductsInCarrello(List<Product> prodottoList, Customer utente) throws IllegalQuantityException, PriceChangedException {
        Cart carrello = new Cart();
        carrello.setCust(utente);
        cartRepository.save(carrello);
        List<ProductInCart> lpic= new ArrayList<>();
        for (Product p: prodottoList){
            if (p.getPrice() != productRepository.findById(p.getProdId()).get().getPrice()){
                throw new PriceChangedException(p.getName() +"'s price changed to "+p.getPrice()+"  euro from " + productRepository.findById(p.getProdId()).get().getPrice() +" euro.");
            }
            Optional<Product> added= productRepository.findById(p.getProdId());
            if (added.isPresent()) {
                if (productInCartRepository.existsByCartAndProduct(carrello, added.get())) {
                    ProductInCart pic = productInCartRepository.findByCartAndProduct(carrello, added.get());
                    pic.setQuantity(pic.getQuantity() + 1);
                } else {
                    ProductInCart pic = new ProductInCart();
                    pic.setCart(carrello);
                    pic.setProduct(added.get());
                    pic.setQuantity(p.getQuantity());
                    productInCartRepository.save(pic);
                    lpic.add(pic); //aggiunto alla lista degli appena aggiunti
                    carrello.setProductsInCart(lpic);
                    cartRepository.save(carrello);
                }
            }
        }


        for(ProductInCart pic: carrello.getProductsInCart()){
            ProductInCart justAdded = productInCartRepository.save(pic);

            Product prodotto=justAdded.getProduct();
            int newQuantity= prodotto.getQuantity() - pic.getQuantity();
            if(newQuantity<0){
                throw new IllegalQuantityException();
            }
            prodotto.setQuantity(newQuantity);


        }
        return carrello;
    }


    @Transactional(readOnly = true)
    public List<Cart> getCarrello(int utenteId) throws  UserNotFoundException{
        if(!customerRepository.existsById(utenteId)) throw new UserNotFoundException();
        List<Cart> ret = cartRepository.findByCust(customerRepository.findById(utenteId).get());
        return ret;
    }


    @Transactional(readOnly = true)
    public List<Product> getCarrelloProducts(int carrelloId, int utenteId) throws UserNotFoundException
    {
        if(!customerRepository.existsById(utenteId)) throw new UserNotFoundException();
        Customer u = customerRepository.findByCustId(utenteId);
        Cart carrello = cartRepository.findByCartId(carrelloId);
        List<Product> ret= new ArrayList<>();
        if (cartRepository.existsByCust(u)) {
            for (ProductInCart pic : carrello.getProductsInCart()) {
                Product p = pic.getProduct();
                p.setQuantity(pic.getQuantity()); //altrimenti metterebbe la quantità del prodotto originale
                ret.add(p);
            }
        }

        return ret;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Cart updateCarrello(int carrelloId, List<Product> prodotti) throws IllegalQuantityException, PriceChangedException {
        Cart optionalCarrello = cartRepository.findByCartId(carrelloId);
        Cart carrello = optionalCarrello;
        Customer utente = carrello.getCust();
        // Controlla se ci sono prodotti nel carrello con prezzo cambiato
        for (Product p : prodotti) {
            if (p.getPrice() != productRepository.findById(p.getProdId()).orElseThrow().getPrice()) {
                throw new PriceChangedException(p.getName() + "'s price changed from " + p.getPrice() + " euro to " + productRepository.findById(p.getProdId()).orElseThrow().getPrice() + " euro.");
            }
        }
        // Aggiorna i prodotti nel carrello
        for (Product p : prodotti) {
            Optional<Product> optionalProdotto = productRepository.findById(p.getProdId());
            if (optionalProdotto.isPresent()) {
                Product prodotto = optionalProdotto.get();
                ProductInCart prodInCarr = productInCartRepository.findByCartAndProduct(carrello, prodotto);
                if (prodInCarr != null) {
                    // Aggiorna la quantità del prodotto nel carrello
                    if (prodotto.getQuantity() - p.getQuantity() < 0) {
                        throw new IllegalQuantityException();
                    }
                    prodInCarr.setQuantity(prodInCarr.getQuantity() + p.getQuantity());
                    productInCartRepository.save(prodInCarr);
                    // Aggiorna la quantità disponibile del prodotto
                    prodotto.setQuantity(prodotto.getQuantity() - p.getQuantity());
                    productRepository.save(prodotto);
                } else {
                    // Aggiungi il prodotto al carrello
                    if (p.getQuantity() > 0) {
                        int newQuantity = prodotto.getQuantity() - p.getQuantity();
                        if (newQuantity < 0) {
                            throw new IllegalQuantityException();
                        }
                        ProductInCart newProdInCarr = new ProductInCart();
                        newProdInCarr.setCart(carrello);
                        newProdInCarr.setProduct(prodotto);
                        newProdInCarr.setQuantity(p.getQuantity());
                        productInCartRepository.save(newProdInCarr);
                        // Aggiorna la quantità disponibile del prodotto
                        prodotto.setQuantity(newQuantity);
                        productRepository.save(prodotto);
                    }
                }
            }
        }
        return carrello;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Cart removeProdCart(int carrelloId, int prodId, int quantity) throws IllegalQuantityException, NoProductException {
        Cart optionalCarrello = cartRepository.findByCartId(carrelloId);
        Cart carrello = optionalCarrello;
        Optional<Product> optionalProdotto = productRepository.findById(prodId);
        if (optionalProdotto.isPresent()) {
            Product prodotto = optionalProdotto.get();
            ProductInCart prodInCarr = productInCartRepository.findByCartAndProduct(carrello, prodotto);
            if (prodInCarr != null) {
                // Aggiorna la quantità nel carrello
                int newQuantity = prodInCarr.getQuantity() - quantity;
                if (newQuantity < 0) {
                    throw new IllegalQuantityException();
                }
                if (newQuantity == 0) {
                    productInCartRepository.delete(prodInCarr);
                } else {
                    prodInCarr.setQuantity(newQuantity);
                    productInCartRepository.save(prodInCarr);
                }
                // Aggiorna la quantità disponibile del prodotto
                prodotto.setQuantity(prodotto.getQuantity() + quantity);
                productRepository.save(prodotto);
            } else {
                throw new NoProductException("Il prodotto non è presente nel carrello");
            }
        } else {
            throw new NoProductException("Prodotto non trovato con ID: " + prodId);
        }
        return carrello;
    }



}
