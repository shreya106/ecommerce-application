import { Routes } from '@angular/router';
import { BuyerProductsComponent } from './buyer-products.component';
import { BuyerCartComponent } from './buyer-cart.component';
import { authGuard } from '../core/guards/auth.guard';
import { roleGuard } from '../core/guards/role.guard';
import { BuyerOrdersComponent } from './buyer-orders.component';
import { BuyerAddressComponent } from './buyer-address.component';
import { BuyerOrderDetailsComponent } from './buyer-order-details.component';

export const BUYER_ROUTES: Routes = [
  {
    path: '',
    canActivate: [
        authGuard,
        roleGuard(['ROLE_BUYER'])
      ],
    children: [
      { path: 'products', component: BuyerProductsComponent },
      { path: 'cart', component: BuyerCartComponent },
      { path: 'checkout/address', component: BuyerAddressComponent },
      { path: 'orders', component: BuyerOrdersComponent },
      { path: 'orders/:id', component: BuyerOrderDetailsComponent },
      { path: '', redirectTo: 'products', pathMatch: 'full' }
    ]
  }
];
