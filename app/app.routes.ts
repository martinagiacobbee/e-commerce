import { Routes } from '@angular/router';
import { HomeComponent } from './home/home/home.component';
import { LoginComponent } from './login/login/login.component';
import { RegistrationComponent } from './registration/registration/registration.component';
import { CartComponent } from './cart/cart/cart.component';
import { PaymentComponent } from './payment/payment/payment.component';
import { HistoryComponent } from './history/history/history.component';
import { HomeAdminComponent } from './home/home-admin/home-admin.component';
import { AddProductComponent } from './admin-functions/add-product/add-product.component';
import { DeleteProductComponent } from './admin-functions/delete-product/delete-product.component';
import { ViewUsersComponent } from './admin-functions/view-users/view-users.component';

export const routes: Routes = [{path:'', redirectTo: '/home', pathMatch: 'full'},
    {path: 'home', component: HomeComponent },
    {path: 'register', component: RegistrationComponent },
    {path: 'login', component: LoginComponent},
    {path: 'cart', component: CartComponent},
    {path: 'payment', component: PaymentComponent},
    {path: 'history', component: HistoryComponent},
    {path: 'home-admin', component: HomeAdminComponent},
    {path: 'addProduct', component: AddProductComponent},
    {path: 'deleteProduct', component: DeleteProductComponent},
    { path: 'viewUsers', component:ViewUsersComponent}
   ];
