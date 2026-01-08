import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuard = (allowedRoles: string[]): CanActivateFn => {
    return () => {
      const authService = inject(AuthService);
      const router = inject(Router);
  
      console.log('🛡 Guard triggered');
      console.log('Token:', authService.getToken());
      console.log('isLoggedIn:', authService.isLoggedIn());
  
      const role = authService.getUserRole();
      console.log('User role:', role);
      console.log('Allowed roles:', allowedRoles);
  
      if (!authService.isLoggedIn()) {
        console.log('❌ Not logged in');
        router.navigate(['/auth/login']);
        return false;
      }
  
      if (role && allowedRoles.includes(role)) {
        console.log('✅ Role allowed');
        return true;
      }
  
      console.log('❌ Role blocked');
      router.navigate(['/auth/login']);
      return false;
    };
  };
  
