import { Routes } from '@angular/router';
import { roleGuard } from '../core/guards/role.guard';
import { SellerDashboardComponent } from './seller-dashboard.component';
import { SellerProductFormComponent } from './seller-product-form.component';
import { SellerProductsComponent } from './seller-products.component';
import { SellerOrdersComponent } from './seller-orders.component';

export const SELLER_ROUTES: Routes = [
  {
    path: '',
    canActivate: [roleGuard(['ROLE_SELLER'])],
    children: [
      { path: '', component: SellerDashboardComponent },
      { path: 'products', component: SellerProductsComponent },
      { path: 'products/new', component: SellerProductFormComponent },
      { path: 'orders', component: SellerOrdersComponent },
      { path: 'products/edit/:id', component: SellerProductFormComponent }
    ]
  }
];
