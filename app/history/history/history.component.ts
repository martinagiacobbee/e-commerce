

import { Component, OnInit } from '@angular/core';
import { HistoryService } from '../../servizi/history.service';
import { CommonModule } from '@angular/common';
import { ToastrModule } from 'ngx-toastr';
import { Cart } from '../../modello/Cart';
import { Product } from '../../modello/Product';
import { AuthService } from '../../servizi/auth.service';

@Component({
  selector: 'app-my-orders',
  templateUrl: './history.component.html',
  imports: [CommonModule, ToastrModule],
  standalone: true,
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  orders: { cartId: number, products: Product[], purchaseTime: string }[] = [];

  constructor(private historyService: HistoryService, private authService: AuthService) { }

  async ngOnInit(): Promise<void> {
    await this.getCarts();
  }

  async getCarts(): Promise<void> {
    const session = this.authService.getSession();

    try {
      const carts = await this.historyService.getCarts();
      
      if (carts.length === 0 && !session.logged) {
        alert('Error while retrieving past orders. Try to log in again');
        return;
      }

      const data = await this.historyService.getAllProductsFromCarts(carts);

      this.orders = data.map((item, index) => ({
        cartId: item.cartId,
        products: item.products,
        purchaseTime: carts[index].purchaseTime
      }));
    } catch (error) {
      console.error('Error retrieving carts and products:', error);
      alert('Error while retrieving past orders. Try to log in again');
    }
  }
}

