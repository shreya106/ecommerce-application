import { Routes } from '@angular/router';
import { roleGuard } from '../core/guards/role.guard';
import { AdminDashboardComponent } from './admin-dashboard.component';
import { AdminProductsComponent } from './admin-products.component';
import { AdminUsersComponent } from './admin-users.component';
import { AdminOrdersComponent } from './admin-orders.component';
export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    canActivate: [roleGuard(['ROLE_ADMIN'])],
    children: [
      { path: '', component: AdminDashboardComponent },
      { path: 'products', component: AdminProductsComponent },
      { path: 'users', component: AdminUsersComponent },
      { path: 'orders', component: AdminOrdersComponent }
    ]
  }
];
