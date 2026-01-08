import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { RouterModule } from '@angular/router';
@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm!: ReturnType<FormBuilder['group']>;
  authError = false;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
    localStorage.removeItem('token')

    this.loginForm.valueChanges.subscribe(() => {
      this.authError = false;
    });
  }
  goToRegister() {
    console.log('Register clicked');
    this.router.navigate(['/auth/register']);
  }
  loading = false;

  submit() {
    console.log('Login submit clicked'); 
  
    if (this.loginForm.invalid) {
      console.log('Form invalid', this.loginForm.value);
      this.loginForm.markAllAsTouched();
      return;
    }
  
    this.authError = false; 
    this.loading = true;
    console.log('Form valid, calling API');
  
    this.authService.login(this.loginForm.value).subscribe({
      next: res => {
        this.loading = false;
        console.log('Login API response:', res);
  
        this.authService.saveToken(res.token);
        const role = this.authService.getUserRole();
  
        if (role === 'ROLE_BUYER') {
          this.router.navigate(['/buyer']);
        } else if (role === 'ROLE_SELLER') {
          this.router.navigate(['/seller']);
        } else if (role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin']);
        }
      },
      error: err => {
        this.loading = false;
        console.error('Login error', err);
  
        if (err.status === 401 || err.status === 403) {
          this.authError = true;
        }
      }
    });
  }
  
}
