import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { User } from '../../modello/User';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { ADDRESS_STORE_SERVER, CLIENT_ID, REQUEST_ALL_USERS } from '../../support/Constants';
import { Router } from '@angular/router';
import { AuthService } from '../../servizi/auth.service';

import { HistoryService } from '../../servizi/history.service';
import { Cart } from '../../modello/Cart';
import { Product } from '../../modello/Product';



@Component({
  selector: 'app-view-users',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './view-users.component.html',
  styleUrls: ['./view-users.component.css']
})
export class ViewUsersComponent implements OnInit {
  nameForm: FormGroup;
  users: User[] = [];
  orders: { cartId: number, products: Product[], purchaseTime: string }[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private fb: FormBuilder,
    private auth: AuthService,
    private historyService: HistoryService
  ) {
    this.nameForm = this.fb.group({
      name: ['']
    });
    
  }


  ngOnInit(): void {
    const session = this.auth.getSession();
    if(!session.admin){
      alert('Unauthorized!');
      this.router.navigate(['/home']);
    } else this.getAllUsers();
  }

  async getAllUsers(): Promise<void> {
    try {
      const session = this.auth.getSession();
      const token = session.jwt.accessToken;
      const apiUrl = `${ADDRESS_STORE_SERVER}/users`; 

      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      });

      const response = await this.http.get<User[]>(apiUrl, { headers }).toPromise();
      this.users = response.map(user => User.fromJson(user));
    } catch (error) {
      console.error('Error fetching users:', error);
      
    }
  }

  onSearch(): void {
    const name = this.nameForm.get('name')?.value;
    if (name) {
      this.router.navigate(['/view-users-name', name]);
    }
  }

  viewUserCart(username: string): void {
    try {
      this.historyService.getUserIdByUser(username).then(userId => {
        if (userId !== -1) {
          this.historyService.getCartsByUser(userId).then(carts => {
            this.historyService.getAllProductsFromCartsByUser(carts, userId).then(data => {
              this.orders = data.map((item, index) => ({
                cartId: item.cartId,
                products: item.products,
                purchaseTime: carts[index].purchaseTime
              }));
            }).catch(error => {
              console.error('Error fetching products from carts:', error);
              
            });
          }).catch(error => {
            console.error('Error fetching carts:', error);
           
          });
        } else {
          console.log('User not found or invalid user ID.');
          alert('User not found or invalid user ID.');
        }
      }).catch(error => {
        console.error('Error fetching user ID:', error);
        
      });
    } catch (error) {
      console.error('Error occurred:', error);
      alert('Error while retrieving past orders. Try to log in again');
    }
  }
}
