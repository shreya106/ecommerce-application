import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  FormGroup
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SellerService } from './seller.service';

@Component({
  standalone: true,
  selector: 'app-seller-product-form',
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="page-container">
      <div class="card">

        <!-- 🔝 TOP BAR -->
        <div class="top-bar">
          <h3 class="title">
            {{ isEdit ? 'Edit Product' : 'Add Product' }}
          </h3>

          <div class="nav-actions">
            <button class="nav-btn" (click)="goDashboard()">Dashboard</button>
            <button class="nav-btn" (click)="goProducts()">My Products</button>
            <button class="nav-btn" (click)="goOrders()">Orders</button>
          </div>
        </div>

        <p class="subtitle">
          {{ isEdit ? 'Update product details' : 'Create a new product for your store' }}
        </p>

        <form *ngIf="form" [formGroup]="form" (ngSubmit)="submit()" class="form">

          <div class="field">
            <label>Name</label>
            <input formControlName="name" placeholder="Product name" />
          </div>

          <div class="grid">
            <div class="field">
              <label>Price</label>
              <input type="number" formControlName="price" placeholder="Price" />
            </div>

            <div class="field">
              <label>Stock</label>
              <input type="number" formControlName="stock" placeholder="Stock" />
            </div>
          </div>

          <div class="field">
            <label>Category</label>
            <input formControlName="categories" placeholder="Category" />
          </div>

          <div class="field">
            <label>Description</label>
            <textarea
              formControlName="description"
              placeholder="Product description"
              rows="4">
            </textarea>
          </div>

          <!-- IMAGE -->
          <div class="field">
            <label>Product Image</label>
            <input type="file" accept="image/*" (change)="onFileSelect($event)" />

            <img *ngIf="previewUrl" [src]="previewUrl" class="preview-img" />
          </div>

          <button type="submit" [disabled]="form.invalid">
            {{ isEdit ? 'Update Product' : 'Create Product' }}
          </button>

        </form>

      </div>
    </div>
  `,
  styles: [`
    .page-container {
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background: #f5f6fa;
      padding: 20px;
    }

    .card {
      width: 100%;
      max-width: 650px;
      background: #ffffff;
      padding: 32px;
      border-radius: 14px;
      box-shadow: 0 12px 28px rgba(0,0,0,0.08);
    }

    /* TOP BAR */
    .top-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 12px;
      flex-wrap: wrap;
      margin-bottom: 10px;
    }

    .title {
      font-size: 24px;
      font-weight: 600;
      margin: 0;
    }

    .nav-actions {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }

    .nav-btn {
      padding: 7px 14px;
      border-radius: 10px;
      border: none;
      font-size: 13px;
      font-weight: 600;
      background: #ede9fe;
      color: #4c1d95;
      cursor: pointer;
    }

    .nav-btn:hover {
      background: #ddd6fe;
    }

    .subtitle {
      text-align: center;
      font-size: 14px;
      color: #6b7280;
      margin-bottom: 28px;
    }

    .form {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }

    .field {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    label {
      font-size: 13px;
      font-weight: 500;
      color: #374151;
    }

    input, textarea {
      padding: 10px 12px;
      border-radius: 8px;
      border: 1px solid #d1d5db;
      font-size: 14px;
    }

    input:focus, textarea:focus {
      outline: none;
      border-color: #4f46e5;
      box-shadow: 0 0 0 1px #4f46e5;
    }

    .grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 14px;
    }

    .preview-img {
      margin-top: 8px;
      width: 150px;
      border-radius: 10px;
      border: 1px solid #e5e7eb;
    }

    button[type="submit"] {
      margin-top: 10px;
      padding: 12px;
      border-radius: 10px;
      border: none;
      font-size: 15px;
      font-weight: 600;
      background: #4f46e5;
      color: #ffffff;
      cursor: pointer;
    }

    button:disabled {
      background: #c7c9f4;
      cursor: not-allowed;
    }

    @media (max-width: 600px) {
      .grid {
        grid-template-columns: 1fr;
      }

      .nav-btn {
        width: 100%;
      }
    }
  `]
})
export class SellerProductFormComponent implements OnInit {

  form!: FormGroup;
  isEdit = false;
  productId!: number;

  selectedFile: File | null = null;
  previewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private sellerService: SellerService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(1)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      categories: ['', Validators.required],
      description: ['']
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.productId = Number(id);

      this.sellerService.getProductById(this.productId).subscribe(product => {
        this.form.patchValue(product);
        if (product.imageUrl) this.previewUrl = product.imageUrl;
      });
    }
  }

  onFileSelect(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.selectedFile = file;
    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result as string;
    reader.readAsDataURL(file);
  }

  submit(): void {
    if (this.form.invalid) return;

    const formData = new FormData();
    Object.entries(this.form.value).forEach(([k, v]) =>
      formData.append(k, String(v))
    );

    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    const req$ = this.isEdit
      ? this.sellerService.updateProductWithImage(this.productId, formData)
      : this.sellerService.createProduct(formData);

    req$.subscribe(() => this.router.navigate(['/seller/products']));
  }

  /* NAVIGATION */
  goDashboard() {
    this.router.navigate(['/seller']);
  }

  goProducts() {
    this.router.navigate(['/seller/products']);
  }

  goOrders() {
    this.router.navigate(['/seller/orders']);
  }
}
