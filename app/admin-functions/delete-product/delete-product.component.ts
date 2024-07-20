import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { AuthService } from '../../servizi/auth.service';
import { CLIENT_ID, GRANT_TYPE, PRODUCTS } from '../../support/Constants';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-delete-product',
  templateUrl: './delete-product.component.html',
  standalone: true,
  imports:[CommonModule, ReactiveFormsModule],
  styleUrls: ['./delete-product.component.css']
})
export class DeleteProductComponent implements OnInit{
  deleteProdForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';

  private apiUrl = PRODUCTS +'/delete';

  constructor(private formBuilder: FormBuilder, private http: HttpClient, private authService: AuthService, private router: Router) {
    this.deleteProdForm = this.formBuilder.group({
      id: ['', [Validators.required, Validators.maxLength(4)]]
    });
  }

  ngOnInit(): void {
    const session = this.authService.getSession();
    if(!session.admin){
      alert('Unauthorized!');
      this.router.navigate(['/home']);
    } 
  }

  async delete(): Promise<void> {
    const id = this.deleteProdForm.value['id'];
    const session = this.authService.getSession();
    const token = session.jwt.accessToken;

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const body = { 
        id: id 
    };
        
    try {
      await this.http.delete<any>(this.apiUrl, {body, headers }).toPromise();
      this.successMessage = `Product with ID: ${id} has been deleted successfully!`;
      this.errorMessage = '';
      this.deleteProdForm.reset();
    } catch (error) {
      if (error instanceof HttpErrorResponse) {
        this.errorMessage = 'The product could either not be found or not be deleted';
      } else {
        this.errorMessage = 'An unexpected error occurred';
      }
      this.successMessage = '';
    }
  }
}
