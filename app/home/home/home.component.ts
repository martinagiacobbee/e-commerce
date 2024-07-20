import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Product } from '../../modello/Product';
import { IMAGES_PATH } from '../../support/Constants';
import { ProductService } from '../../servizi/product.service';
import { SearchService } from '../../servizi/search.service';

import { KeycloakService } from 'keycloak-angular';
import { AuthService } from '../../servizi/auth.service';
import { CartService } from '../../servizi/cart.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  products!: Product[];
  productsInCart!:Product[];
  images_path: string = IMAGES_PATH;
  loggedIn: boolean = false;

  filteredProducts: Product[] = [];
  @Input() searchTerm: string = '';
  

  constructor(private productService: ProductService, 
    private searchService: SearchService, 
    private keycloakService: KeycloakService,
    private authService: AuthService,
    private cartService: CartService) { }

  ngOnInit(): void {
  

    this.productService.getProducts()
      .subscribe((products: Product[]) => {
        this.products = products;
        this.filteredProducts = products; 

        //Copia profonda per la gestione dell'input dell'utente
        this.productsInCart = products.map(product => new Product(
          product.prodId,
          product.name,
          product.price,
          1, // Initialize quantity to 1
          product.description,
          product.url,
          
        ));
      });
  }
  
  
increaseQuantity(index: number) {
  
  this.productsInCart[index].quantity++;
  const newQta = this.productsInCart[index].quantity;
  if(this.products[index].quantity-newQta<0) alert('Quantity unavailable');
}

decreaseQuantity(index: number) {
  if (this.products[index].quantity > 1) {
    this.productsInCart[index].quantity--;
  }
}

getProductsByName(): void {
  if(this.searchTerm == "") 
    this.productService.getProducts().subscribe((products: Product[]) => {
    this.products = products;
    this.filteredProducts = products; 
  });
  
  this.productService.getProductByName('asc', this.searchTerm).subscribe(
    (products: Product[]) => {
      this.filteredProducts = products;
    },
    (error) => {
      console.error('Error retrieving products:', error);
    }
  );
}

amILogged(){
  //METODO DI PROVA
  if(this.keycloakService.isLoggedIn()){
    // Ottieni l'oggetto utente corrente
    const userProfile =this.keycloakService.isLoggedIn;
    console.log('Is Logged In:', userProfile);
  };

  const session = this.authService.getSession();
  console.log('session: ', session);

  
}


addToCart(product: any, index: number) {
  const session = this.authService.getSession();
  const prodotto = this.productsInCart[index];
  if (!session.logged) {
    alert('You need to log in first!');
    prodotto.quantity = 1;
  } else {
    const name = product.name;
    const quantity = prodotto.quantity;
    console.log(`${product.name} added to cart with quantity ${prodotto.quantity}`);
    try {
      this.cartService.addToCart(prodotto, product);
      alert(name + ' added to cart with quantity ' + quantity);
    } catch (error) {
      alert((error as Error).message);
      prodotto.quantity = 1;
    }
  }
}

}
