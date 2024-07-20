import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../modello/Product';
import { CartService } from '../../servizi/cart.service';
import { CommonModule } from '@angular/common';
import { IMAGES_PATH } from '../../support/Constants';


@Component({
  selector: 'app-cart',
  standalone:true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: Product[] = [];
  total = 0;
  images_path: string = IMAGES_PATH;

  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit(): void {
    
    this.loadCart();
  }

  loadCart(): void {
    this.cart = this.cartService.getCart();
    this.total = this.cartService.getTotal();
    
  }

  removeFromCart(product: Product): void {
    this.cartService.removeFromCart(product);
    this.loadCart();
  }

  proceedToPayment(): void {
    if (this.cart.length > 0) {
      
      this.router.navigate(['/payment']);
    }else{
      alert('Empty Cart');
    }
  }
}
