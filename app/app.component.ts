import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { CommonModule } from '@angular/common';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './home/home/home.component';
import { SearchService } from './servizi/search.service';
import { HttpClientModule } from '@angular/common/http';
import { ProductService } from './servizi/product.service';
import { AuthService } from './servizi/auth.service';
import { LoginComponent } from './login/login/login.component';
import { RegistrationComponent } from './registration/registration/registration.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { CartComponent } from './cart/cart/cart.component';
import { PaymentComponent } from './payment/payment/payment.component';
import { firstValueFrom } from 'rxjs';
import { Session } from './modello/Session';
import { CartService } from './servizi/cart.service';
import { PaymentService } from './servizi/payment.service';
import { CookieService } from 'ngx-cookie-service';
import { HistoryService } from './servizi/history.service';
import { HistoryComponent } from './history/history/history.component';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { HomeAdminComponent } from './home/home-admin/home-admin.component';
import { Router } from '@angular/router';
import { AddProductComponent } from './admin-functions/add-product/add-product.component';
import { DeleteProductComponent } from './admin-functions/delete-product/delete-product.component';
import { ViewUsersComponent } from './admin-functions/view-users/view-users.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, 
            CommonModule, 
            ReactiveFormsModule, 
            FormsModule, 
            HomeComponent,
            HttpClientModule,
            LoginComponent, 
            RegistrationComponent,
            KeycloakAngularModule,
            CartComponent,
            HistoryComponent,
            PaymentComponent,
            HomeAdminComponent,
            AddProductComponent,
            DeleteProductComponent,
            ViewUsersComponent
          ],
  providers: [ProductService,
              HistoryService,
              AuthService,
              CartService,
              PaymentService,
              CookieService,
              
              {
                provide: KeycloakService,
                useClass: KeycloakService
              }],
              
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  
  title = 'Rodeo Drive Diner';
  searchTerm: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  verifyUser() {
    const session = this.authService.getSession();
    if(!session || !session.admin) this.router.navigate(['/home']);
    else this.router.navigate(['/home-admin']);

  }

  async logout_user(){
    try{
      const session: Session = this.authService.getSession();
      if(session.logged){
        const response = await this.authService.logout();
        console.log('response ',response);
        alert('Logged Out Correctly');
      }else alert('No one is Logged In');

    }catch(error){
      console.log('error',error);
    }
  }
  
  }
