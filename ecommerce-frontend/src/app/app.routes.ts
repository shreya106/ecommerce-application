  import { Routes } from '@angular/router';
  import { PaymentSuccessComponent } from './buyer/payment-success.component';  
  export const routes: Routes = [
    {
      path: 'auth',
      loadChildren: () =>
        import('./auth/auth.routes').then(m => m.AUTH_ROUTES)
    },
    {
      path: 'buyer',
      loadChildren: () =>
        import('./buyer/buyer.routes').then(m => m.BUYER_ROUTES)
    },
    {
      path: '',
      redirectTo: 'auth/login',
      pathMatch: 'full'
    },
    {
      path: 'admin',
      loadChildren: () =>
        import('./admin/admin.routes').then(m => m.ADMIN_ROUTES)
    },
    {
      path: 'seller',
      loadChildren: () =>
        import('./seller/seller.routes').then(m => m.SELLER_ROUTES)
    },
    { path: 'payment/success', component: PaymentSuccessComponent },
    { path: 'payment/cancel', component: PaymentSuccessComponent }
  ];
