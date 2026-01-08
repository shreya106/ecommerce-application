import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm!: ReturnType<FormBuilder['group']>;
  

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: ['BUYER', Validators.required],
      country: ['', Validators.required],
      location: ['', Validators.required],
      address: ['', Validators.required],
      phoneNumber: ['', Validators.required]
    });
    

    localStorage.removeItem('token')
  }
  goToLogin() {
    console.log('Back to login clicked');
    this.router.navigate(['/auth/login']);
  }
  alreadyRegistered = false;
  successMessage = false;
  
  submit() {
    if (this.registerForm.invalid) return;
  
    this.successMessage = false;
    this.alreadyRegistered = false;
  
    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        // 1️⃣ Set flag
        this.successMessage = true;
  
        // 2️⃣ Force UI to render before navigating
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000); // 👈 give UI time (2s)
      },
      error: err => {
        if (err.status === 409) {
          this.alreadyRegistered = true;
        }
      }
    });
  }
  
}
