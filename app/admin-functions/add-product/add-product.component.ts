import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';  // Importa FormBuilder e FormGroup
import { PRODUCTS } from '../../support/Constants';
import { AuthService } from '../../servizi/auth.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit{
  addProdForm: FormGroup; 
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient, private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
    this.addProdForm = this.formBuilder.group({
      name: ['', Validators.required],
      url: ['', Validators.required],
      description: [''],
      price: ['', Validators.required],
      quantity: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const session = this.authService.getSession();
    if(!session.admin){
      alert('Unauthorized!');
      this.router.navigate(['/home']);
    } 
  }

  async register(): Promise<void> {
    const url = PRODUCTS + "/add";
    const session = this.authService.getSession();
    const token = session.jwt.accessToken;

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token
    });

    const body = {
      "name": this.addProdForm.value['name'],
      "url" : this.addProdForm.value['url'],
      "description": this.addProdForm.value['description'],
      "price": this.addProdForm.value['price'],
      "quantity": this.addProdForm.value['quantity']
    }
  
    try {
      const response = await this.http.post<any>(url, body, { headers }).toPromise();
      this.successMessage = response.message || 'Product added successfully!';
      this.addProdForm.reset();
    } catch (error: any) {
      if (error.status === 400) {
        if(error.error.message === 'Product already exists'){
           this.updateProduct(); //se il prodotto esiste, lo aggiorna
        }
        else {
          this.errorMessage = error.error.message || 'Failed to add product due to validation error';4
        }
      } else {
        this.errorMessage = 'Failed to add product due to an unexpected error';
      }
    }
  }

  async updateProduct(){
    const url = PRODUCTS + "/update";
    const session = this.authService.getSession();
    const token = session.jwt.accessToken;

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token
    });

    const body = {
      "name": this.addProdForm.value['name'],
      "url" : this.addProdForm.value['url'],
      "description": this.addProdForm.value['description'],
      "price": this.addProdForm.value['price'],
      "quantity": this.addProdForm.value['quantity']
    }
  
    try {
      const response = await this.http.put<any>(url, body, { headers }).toPromise();
      this.successMessage = response.message || 'Product added successfully!';
      this.addProdForm.reset();
    } catch (error: any) {
      if (error.status === 400) {
        
        this.errorMessage = error.error.message || 'Failed to add product due to validation error';
      } else {
        this.errorMessage = 'Failed to add product due to an unexpected error';
      }
    }
  }

}
