import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private API = `${environment.apiBaseUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(data: any) {
    return this.http.post<{ token: string }>(`${this.API}/login`, data);
  }

  register(data: any) {
    return this.http.post(`${this.API}/register`, data);
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) return null;
  
    const payload = JSON.parse(atob(token.split('.')[1]));
    const role = payload.role;
  
    return role?.startsWith('ROLE_') ? role : `ROLE_${role}`;
  }
  
}
