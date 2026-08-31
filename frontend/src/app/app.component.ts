import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import {
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet
} from '@angular/router';

/* =========================================================
   ROOT COMPONENT
   ========================================================= */

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './app.component.html'
})
export class AppComponent {

  menuOpen = signal(false);

  constructor(public router: Router) {
  }

  isAppPage(): boolean {
    return !this.router.url.startsWith('/register')
      && !this.router.url.startsWith('/login');
  }

  userInitials(): string {
    const customer = JSON.parse(
      localStorage.getItem('electra-customer') || '{}'
    );

    const name = String(
      customer?.fullName ||
      customer?.customerName ||
      'Customer'
    );

    const initials = name
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map(part => part[0]?.toUpperCase() || '')
      .join('');

    return initials || 'CU';
  }

  logout(): void {
    localStorage.removeItem('electra-customer');
    localStorage.removeItem('electra-bills');
    this.router.navigateByUrl('/login');
  }
}

/* =========================================================
   LOGIN COMPONENT
   ========================================================= */

@Component({
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  template: `
    <main class="auth-shell">
      <section class="brand-panel">
        <div class="brand-lockup">
          <span class="brand-mark">⚡</span>
          <span>ElectraCare</span>
        </div>

        <div class="brand-copy">
          <span class="eyebrow light">
            SMART CUSTOMER PORTAL
          </span>

          <h1>Welcome back.</h1>

          <p>
            Sign in with your customer account to review bills,
            manage payments, and check account activity.
          </p>
        </div>

        <div class="brand-stats">
          <div>
            <b>24/7</b>
            <span>Account access</span>
          </div>

          <div>
            <b>100%</b>
            <span>Secure access</span>
          </div>
        </div>
      </section>

      <section class="auth-panel">
        <div class="form-card">
          <span class="eyebrow">SIGN IN</span>

          <h2>Login to your account</h2>

          <p class="muted">
            Use your registered user ID and password.
          </p>

          <form
            [formGroup]="loginForm"
            (ngSubmit)="submit()"
          >
            <div class="form-grid single-column">
              <label>
                <span>User ID *</span>

                <input
                  formControlName="userId"
                  placeholder="Enter your user ID"
                >

                <small *ngIf="bad('userId')">
                  User ID is required.
                </small>
              </label>

              <label>
                <span>Password *</span>

                <input
                  formControlName="password"
                  type="password"
                  placeholder="Enter your password"
                >

                <small *ngIf="bad('password')">
                  Password is required.
                </small>
              </label>
            </div>

            <div
              class="notice error"
              *ngIf="error"
            >
              {{ error }}
            </div>

            <button
              class="primary wide"
              type="submit"
              [disabled]="loading"
            >
              {{ loading ? 'Signing in...' : 'Login' }}
              <span>→</span>
            </button>

            <p class="privacy">
              Your credentials will be securely verified by
              the customer portal.
            </p>

            <p class="switch-text">
              New customer?
              <a routerLink="/register">Create account</a>
            </p>
          </form>
        </div>
      </section>
    </main>
  `
})
export class LoginComponent {

  loginForm;
  error = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      userId: ['', Validators.required],
      password: ['', Validators.required]
    });

    if (localStorage.getItem('electra-customer')) {
      this.router.navigateByUrl('/home');
    }
  }

  bad(name: string): boolean {
    const control = this.loginForm.get(name);

    return !!(
      control?.invalid &&
      (control.dirty || control.touched)
    );
  }

  submit(): void {
    this.loginForm.markAllAsTouched();

    if (this.loginForm.invalid || this.loading) {
      return;
    }

    this.loading = true;
    this.error = '';

    const requestBody = {
      userId: String(
        this.loginForm.value.userId ?? ''
      ).trim(),

      password: String(
        this.loginForm.value.password ?? ''
      )
    };

    this.http.post<any>(
      'http://localhost:8080/api/auth/login',
      requestBody
    ).subscribe({
      next: response => {
        const customerSession = {
          customerId: response.customerId,
          consumerNumber: response.consumerNumber,
          userId: response.userId,
          username: response.username,
          fullName: response.fullName,
          customerName: response.fullName,
          role: response.role
        };

        localStorage.setItem(
          'electra-customer',
          JSON.stringify(customerSession)
        );

        localStorage.removeItem('electra-bills');

        this.loading = false;
        this.router.navigateByUrl('/home');
      },

      error: httpError => {
        this.loading = false;

        if (httpError.status === 0) {
          this.error =
            'Unable to connect to the backend. ' +
            'Make sure Spring Boot is running on port 8080.';
          return;
        }

        if (httpError.status === 401) {
          this.error = 'Invalid user ID or password.';
          return;
        }

        const validationErrors =
          httpError.error?.validationErrors;

        if (validationErrors) {
          const messages = Object.values(
            validationErrors
          );

          if (messages.length > 0) {
            this.error = messages.join(' ');
            return;
          }
        }

        this.error =
          httpError.error?.message ||
          'Login failed. Please try again.';
      }
    });
  }
}

/* =========================================================
   REGISTRATION COMPONENT
   ========================================================= */

@Component({
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  template: `
    <main class="auth-shell">
      <section class="brand-panel">
        <div class="brand-lockup">
          <span class="brand-mark">⚡</span>
          <span>ElectraCare</span>
        </div>

        <div class="brand-copy">
          <span class="eyebrow light">
            SMART CUSTOMER PORTAL
          </span>

          <h1>Powering a simpler customer experience.</h1>

          <p>
            Register, review bills, and manage electricity
            services from one secure, easy-to-use workspace.
          </p>
        </div>

        <div class="brand-stats">
          <div>
            <b>24/7</b>
            <span>Account access</span>
          </div>

          <div>
            <b>100%</b>
            <span>Secure registration</span>
          </div>
        </div>
      </section>

      <section class="auth-panel">
        <div class="form-card">
          <span class="eyebrow">
            CREATE YOUR ACCOUNT
          </span>

          <h2>Customer registration</h2>

          <p class="muted">
            Enter your electricity account details to get started.
          </p>

          <form
            [formGroup]="form"
            (ngSubmit)="submit()"
          >
            <div class="form-grid">
              <label>
                <span>Consumer number *</span>

                <input
                  formControlName="consumerNumber"
                  placeholder="Maximum 13 characters"
                  maxlength="13"
                >

                <small *ngIf="bad('consumerNumber')">
                  Consumer number is required and must not
                  exceed 13 characters.
                </small>
              </label>

              <label>
                <span>Customer name *</span>

                <input
                  formControlName="customerName"
                  placeholder="Full name"
                >

                <small *ngIf="bad('customerName')">
                  Enter at least 3 characters.
                </small>
              </label>

              <label class="full">
                <span>Address *</span>

                <input
                  formControlName="address"
                  placeholder="House, street, city"
                >

                <small *ngIf="bad('address')">
                  Address is required.
                </small>
              </label>

              <label>
                <span>Email address *</span>

                <input
                  formControlName="email"
                  type="email"
                  placeholder="name@example.com"
                >

                <small *ngIf="bad('email')">
                  Enter a valid email address.
                </small>
              </label>

              <label>
                <span>Mobile number *</span>

                <input
                  formControlName="mobileNumber"
                  placeholder="10-digit number"
                  maxlength="10"
                >

                <small *ngIf="bad('mobileNumber')">
                  Enter a valid 10-digit mobile number.
                </small>
              </label>

              <label>
                <span>Customer type *</span>

                <select formControlName="customerType">
                  <option value="">Select type</option>
                  <option>Residential</option>
                  <option>Commercial</option>
                </select>

                <small *ngIf="bad('customerType')">
                  Select a customer type.
                </small>
              </label>

              <label>
                <span>Electrical section *</span>

                <select formControlName="electricalSection">
                  <option value="">Select section</option>
                  <option>North Division</option>
                  <option>South Division</option>
                  <option>East Division</option>
                  <option>West Division</option>
                </select>

                <small *ngIf="bad('electricalSection')">
                  Select an electrical section.
                </small>
              </label>

              <label>
                <span>User ID *</span>

                <input
                  formControlName="userId"
                  placeholder="Choose a user ID"
                >

                <small *ngIf="bad('userId')">
                  Use at least 5 characters.
                </small>
              </label>

              <label>
                <span>Password *</span>

                <input
                  formControlName="password"
                  type="password"
                  placeholder="Minimum 8 characters"
                >

                <small *ngIf="bad('password')">
                  Password must contain at least 8 characters.
                </small>
              </label>

              <label class="full">
                <span>Confirm password *</span>

                <input
                  formControlName="confirmPassword"
                  type="password"
                  placeholder="Re-enter password"
                >

                <small
                  *ngIf="
                    form.get('confirmPassword')?.touched &&
                    form.hasError('passwordMismatch')
                  "
                >
                  Passwords do not match.
                </small>
              </label>
            </div>

            <div
              class="notice success"
              *ngIf="message"
            >
              ✓ {{ message }}
            </div>

            <div
              class="notice error"
              *ngIf="error"
            >
              {{ error }}
            </div>

            <button
              class="primary wide"
              type="submit"
              [disabled]="loading"
            >
              {{
                loading
                  ? 'Creating account...'
                  : 'Create account'
              }}
              <span>→</span>
            </button>

            <p class="privacy">
              Your registration details will be submitted
              securely to the customer portal.
            </p>

            <p class="switch-text">
              Already have an account?
              <a routerLink="/login">Login</a>
            </p>
          </form>
        </div>
      </section>
    </main>
  `
})
export class RegisterComponent {

  message = '';
  error = '';
  loading = false;
  form;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient
  ) {
    this.form = this.fb.group(
      {
        consumerNumber: [
          '',
          [
            Validators.required,
            Validators.maxLength(13)
          ]
        ],

        customerName: [
          '',
          [
            Validators.required,
            Validators.minLength(3)
          ]
        ],

        address: ['', Validators.required],

        email: [
          '',
          [
            Validators.required,
            Validators.email
          ]
        ],

        mobileNumber: [
          '',
          [
            Validators.required,
            Validators.pattern(/^\d{10}$/)
          ]
        ],

        customerType: ['', Validators.required],

        electricalSection: ['', Validators.required],

        userId: [
          '',
          [
            Validators.required,
            Validators.minLength(5)
          ]
        ],

        password: [
          '',
          [
            Validators.required,
            Validators.minLength(8)
          ]
        ],

        confirmPassword: ['', Validators.required]
      },
      {
        validators: control =>
          control.value.password ===
          control.value.confirmPassword
            ? null
            : { passwordMismatch: true }
      }
    );
  }

  bad(name: string): boolean {
    const control = this.form.get(name);

    return !!(
      control?.invalid &&
      (control.dirty || control.touched)
    );
  }

  submit(): void {
    this.form.markAllAsTouched();

    if (this.form.invalid || this.loading) {
      return;
    }

    this.loading = true;
    this.message = '';
    this.error = '';

    const requestBody = {
      consumerNumber: String(
        this.form.value.consumerNumber ?? ''
      ).trim(),

      customerName: String(
        this.form.value.customerName ?? ''
      ).trim(),

      address: String(
        this.form.value.address ?? ''
      ).trim(),

      email: String(
        this.form.value.email ?? ''
      ).trim(),

      mobileNumber: String(
        this.form.value.mobileNumber ?? ''
      ).trim(),

      customerType: String(
        this.form.value.customerType ?? ''
      ).trim(),

      electricalSection: String(
        this.form.value.electricalSection ?? ''
      ).trim(),

      userId: String(
        this.form.value.userId ?? ''
      ).trim(),

      password: String(
        this.form.value.password ?? ''
      ),

      confirmPassword: String(
        this.form.value.confirmPassword ?? ''
      )
    };

    this.http.post<any>(
      'http://localhost:8080/api/customers/register',
      requestBody
    ).subscribe({
      next: response => {
        const customerSession = {
          customerId: response.customerId,
          consumerNumber: response.consumerNumber,
          userId: response.userId,
          username: response.username,
          customerName: requestBody.customerName,
          fullName: requestBody.customerName
        };

        localStorage.setItem(
          'electra-customer',
          JSON.stringify(customerSession)
        );

        localStorage.removeItem('electra-bills');

        this.message =
          response.message ||
          'Customer registered successfully.';

        this.loading = false;

        setTimeout(() => {
          this.router.navigateByUrl('/home');
        }, 700);
      },

      error: httpError => {
        this.loading = false;

        if (httpError.status === 0) {
          this.error =
            'Unable to connect to the backend. ' +
            'Make sure Spring Boot is running on port 8080.';
          return;
        }

        const validationErrors =
          httpError.error?.validationErrors;

        if (validationErrors) {
          const messages = Object.values(
            validationErrors
          );

          if (messages.length > 0) {
            this.error = messages.join(' ');
            return;
          }
        }

        this.error =
          httpError.error?.message ||
          'Registration failed. Please try again.';
      }
    });
  }
}

/* =========================================================
   HOME COMPONENT
   ========================================================= */

@Component({
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  template: `
    <section class="page">
      <div
        class="notice"
        *ngIf="loading"
      >
        Loading customer information...
      </div>

      <div
        class="notice error"
        *ngIf="error"
      >
        {{ error }}
      </div>

      <div class="hero-card">
        <div>
          <span class="eyebrow light">
            GOOD MORNING
          </span>

          <h1>Welcome back, {{ name }}</h1>

          <p>
            View your electricity account information and
            manage your billing activity.
          </p>

          <a
            routerLink="/bills"
            class="light-button"
          >
            View current bills →
          </a>
        </div>

        <div class="hero-orb">⚡</div>
      </div>

      <div class="stats-grid">
        <article class="stat">
          <span class="icon blue">₹</span>

          <div>
            <small>Billing information</small>
            <strong>View bills</strong>
            <em>Review current and past bills</em>
          </div>
        </article>

        <article class="stat">
          <span class="icon green">✓</span>

          <div>
            <small>Connection status</small>
            <strong>
              {{ customer.connectionStatus || 'ACTIVE' }}
            </strong>
            <em>
              {{ customer.customerType || 'Customer' }}
              connection
            </em>
          </div>
        </article>

        <article class="stat">
          <span class="icon purple">◉</span>

          <div>
            <small>Electrical section</small>
            <strong>
              {{ customer.electricalSection || 'Not available' }}
            </strong>
            <em>Registered account section</em>
          </div>
        </article>
      </div>

      <div class="content-grid">
        <article class="panel">
          <div class="panel-head">
            <div>
              <span class="eyebrow">QUICK ACTIONS</span>
              <h2>What would you like to do?</h2>
            </div>
          </div>

          <div class="action-grid">
            <a routerLink="/bills">
              <span>▤</span>
              <b>View bills</b>
              <small>Review and select bills</small>
            </a>

            <a routerLink="/summary">
              <span>₹</span>
              <b>Bill summary</b>
              <small>See selected bill total</small>
            </a>

            <a>
              <span>↗</span>
              <b>Make payment</b>
              <small>Outside current scope</small>
            </a>

            <a>
              <span>⌁</span>
              <b>Raise complaint</b>
              <small>Outside current scope</small>
            </a>
          </div>
        </article>

        <article class="panel account">
          <span class="eyebrow">ACCOUNT DETAILS</span>
          <h2>Your information</h2>

          <dl>
            <div>
              <dt>Consumer number</dt>
              <dd>
                {{ customer.consumerNumber || 'Not available' }}
              </dd>
            </div>

            <div>
              <dt>Email</dt>
              <dd>
                {{ customer.email || 'Not available' }}
              </dd>
            </div>

            <div>
              <dt>Mobile</dt>
              <dd>
                {{ customer.mobileNumber || 'Not available' }}
              </dd>
            </div>

            <div>
              <dt>Electrical section</dt>
              <dd>
                {{
                  customer.electricalSection ||
                  'Not available'
                }}
              </dd>
            </div>
          </dl>
        </article>
      </div>
    </section>
  `
})
export class HomeComponent {

  customer: any = {};
  name = 'Customer';
  loading = true;
  error = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadCustomer();
  }

  loadCustomer(): void {
    const savedSession = JSON.parse(
      localStorage.getItem('electra-customer') || '{}'
    );

    const consumerNumber = savedSession.consumerNumber;

    if (!consumerNumber) {
      this.router.navigateByUrl('/login');
      return;
    }

    this.loading = true;
    this.error = '';

    this.http.get<any>(
      `http://localhost:8080/api/customers/${consumerNumber}/home`
    ).subscribe({
      next: response => {
        this.customer = response;
        this.name = response.fullName || 'Customer';
        this.loading = false;

        const updatedSession = {
          ...savedSession,
          ...response,
          customerName: response.fullName
        };

        localStorage.setItem(
          'electra-customer',
          JSON.stringify(updatedSession)
        );
      },

      error: httpError => {
        this.loading = false;

        if (httpError.status === 0) {
          this.error =
            'Unable to connect to the backend. ' +
            'Make sure Spring Boot is running on port 8080.';
          return;
        }

        if (httpError.status === 404) {
          this.error =
            'Customer information was not found. ' +
            'Please register again.';
          return;
        }

        this.error =
          httpError.error?.message ||
          'Unable to load customer information.';
      }
    });
  }
}

/* =========================================================
   BILL MODEL
   ========================================================= */

export interface Bill {
  billId: number;
  billNumber: string;
  billDate: string;
  billingPeriod: string;
  dueDate: string;
  billAmount: number;
  unitsConsumed: number;
  status: string;
  selected: boolean;
}

/* =========================================================
   VIEW BILLS COMPONENT
   ========================================================= */

@Component({
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  template: `
    <section class="page">
      <div
        class="notice"
        *ngIf="loading"
      >
        Loading bills...
      </div>

      <div
        class="notice error"
        *ngIf="error"
      >
        {{ error }}
      </div>

      <div class="page-title">
        <div>
          <span class="eyebrow">BILLING</span>
          <h1>My bills</h1>

          <p>
            Review your billing history and select unpaid
            bills to continue.
          </p>
        </div>

        <button
          class="secondary"
          type="button"
          (click)="refresh()"
          [disabled]="loading"
        >
          ↻ Refresh
        </button>
      </div>

      <div class="panel table-card">
        <div class="table-toolbar">
          <div>
            <h2>Billing history</h2>
            <p>{{ bills.length }} bills found</p>
          </div>

          <div class="legend">
            <span>
              <i class="dot unpaid"></i>
              Unpaid
            </span>

            <span>
              <i class="dot paid"></i>
              Paid
            </span>
          </div>
        </div>

        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Select</th>
                <th>Bill ID</th>
                <th>Billing period</th>
                <th>Due date</th>
                <th>Amount</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              <tr
                *ngFor="let bill of bills"
                [class.selected-row]="bill.selected"
              >
                <td>
                  <input
                    class="check"
                    type="checkbox"
                    [checked]="bill.selected"
                    [disabled]="bill.status === 'PAID'"
                    (change)="toggle(bill)"
                  >
                </td>

                <td>
                  <b>
                    {{ bill.billNumber || bill.billId }}
                  </b>
                </td>

                <td>{{ bill.billingPeriod }}</td>

                <td>
                  {{ bill.dueDate | date:'dd MMM yyyy' }}
                </td>

                <td class="money">
                  ₹{{ bill.billAmount | number:'1.2-2' }}
                </td>

                <td>
                  <span
                    class="badge"
                    [class.paid]="bill.status === 'PAID'"
                  >
                    {{ bill.status }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div
        class="panel empty"
        *ngIf="!loading && !error && bills.length === 0"
      >
        <span>▤</span>
        <h2>No bills found</h2>

        <p>
          No electricity bills are currently available for
          this customer.
        </p>
      </div>

      <div class="selection-bar">
        <div>
          <span>Selected total</span>

          <strong>
            ₹{{ total | number:'1.2-2' }}
          </strong>

          <small>
            {{ selectedCount }}
            bill{{ selectedCount === 1 ? '' : 's' }}
            selected
          </small>
        </div>

        <button
          class="primary"
          type="button"
          [disabled]="selectedCount === 0"
          (click)="summary()"
        >
          View summary →
        </button>
      </div>
    </section>
  `
})
export class BillsComponent {

  bills: Bill[] = [];
  loading = true;
  error = '';

  constructor(
    private router: Router,
    private http: HttpClient
  ) {
    this.loadBills();
  }

  get total(): number {
    return this.bills
      .filter(bill => bill.selected)
      .reduce(
        (sum, bill) =>
          sum + (bill.billAmount || 0),
        0
      );
  }

  get selectedCount(): number {
    return this.bills.filter(
      bill => bill.selected
    ).length;
  }

  loadBills(): void {
    const customer = JSON.parse(
      localStorage.getItem('electra-customer') || '{}'
    );

    const consumerNumber = customer.consumerNumber;

    if (!consumerNumber) {
      this.router.navigateByUrl('/login');
      return;
    }

    this.loading = true;
    this.error = '';

    this.http.get<any[]>(
      `http://localhost:8080/api/bills/${consumerNumber}`
    ).subscribe({
      next: response => {
        this.bills = response.map(bill => ({
          ...bill,
          selected: false
        }));

        localStorage.removeItem('electra-bills');
        this.loading = false;
      },

      error: httpError => {
        this.loading = false;
        this.bills = [];

        if (httpError.status === 0) {
          this.error =
            'Unable to connect to the backend. ' +
            'Make sure Spring Boot is running on port 8080.';
          return;
        }

        this.error =
          httpError.error?.message ||
          'Unable to load bills.';
      }
    });
  }

  toggle(bill: Bill): void {
    bill.selected = !bill.selected;
  }

  refresh(): void {
    this.loadBills();
  }

  summary(): void {
    const selectedBills = this.bills.filter(
      bill => bill.selected
    );

    localStorage.setItem(
      'electra-bills',
      JSON.stringify(selectedBills)
    );

    this.router.navigateByUrl('/summary');
  }
}

/* =========================================================
   BILL SUMMARY COMPONENT
   ========================================================= */

@Component({
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  template: `
    <section class="page narrow">
      <div
        class="notice"
        *ngIf="loading"
      >
        Verifying selected bills...
      </div>

      <div
        class="notice error"
        *ngIf="error"
      >
        {{ error }}
      </div>

      <div class="page-title">
        <div>
          <span class="eyebrow">REVIEW</span>
          <h1>Bill summary</h1>

          <p>
            Confirm the selected bill details before proceeding.
          </p>
        </div>
      </div>

      <div
        class="panel summary-card"
        *ngIf="selected.length > 0; else empty"
      >
        <div class="summary-top">
          <div>
            <span class="summary-icon">▤</span>

            <div>
              <h2>Selected bills</h2>

              <p>
                {{ selected.length }}
                bill{{ selected.length === 1 ? '' : 's' }}
                ready for payment
              </p>
            </div>
          </div>

          <span class="secure">
            ✓ Secure review
          </span>
        </div>

        <div class="summary-lines">
          <div *ngFor="let bill of selected">
            <div>
              <b>{{ bill.billingPeriod }}</b>

              <small>
                {{ bill.billNumber || bill.billId }}
                · Due
                {{ bill.dueDate | date:'dd MMM yyyy' }}
              </small>
            </div>

            <strong>
              ₹{{ bill.billAmount | number:'1.2-2' }}
            </strong>
          </div>
        </div>

        <div class="total-box">
          <span>Total payable</span>

          <strong>
            ₹{{ total | number:'1.2-2' }}
          </strong>

          <small>
            Amount verified by the Spring Boot backend
          </small>
        </div>

        <div class="summary-actions">
          <a
            routerLink="/bills"
            class="secondary button-link"
          >
            ← Back to bills
          </a>

          <button
            class="primary"
            type="button"
            (click)="proceed()"
          >
            Proceed to payment →
          </button>
        </div>

        <div
          class="notice success"
          *ngIf="done"
        >
          ✓ Bill summary verified successfully.
          Payment processing is outside the current scope.
        </div>
      </div>

      <ng-template #empty>
        <div
          class="panel empty"
          *ngIf="!loading"
        >
          <span>▤</span>
          <h2>No bills selected</h2>

          <p>
            Choose an unpaid bill before viewing the summary.
          </p>

          <a
            routerLink="/bills"
            class="primary button-link"
          >
            Go to bills
          </a>
        </div>
      </ng-template>
    </section>
  `
})
export class SummaryComponent {

  selected: Bill[] = [];
  total = 0;
  loading = false;
  error = '';
  done = false;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadSummary();
  }

  loadSummary(): void {
    const customer = JSON.parse(
      localStorage.getItem('electra-customer') || '{}'
    );

    const storedBills: Bill[] = JSON.parse(
      localStorage.getItem('electra-bills') || '[]'
    );

    if (!customer.consumerNumber) {
      this.router.navigateByUrl('/login');
      return;
    }

    if (storedBills.length === 0) {
      this.selected = [];
      return;
    }

    this.loading = true;
    this.error = '';

    const requestBody = {
      consumerNumber: customer.consumerNumber,

      billIds: storedBills.map(
        bill => bill.billId
      )
    };

    this.http.post<any>(
      'http://localhost:8080/api/bill-summary',
      requestBody
    ).subscribe({
      next: response => {
        this.selected = response.selectedBills.map(
          (bill: any) => ({
            ...bill,
            selected: true
          })
        );

        this.total = response.totalAmount;
        this.loading = false;
      },

      error: httpError => {
        this.loading = false;
        this.selected = [];
        this.total = 0;

        if (httpError.status === 0) {
          this.error =
            'Unable to connect to the backend. ' +
            'Make sure Spring Boot is running on port 8080.';
          return;
        }

        this.error =
          httpError.error?.message ||
          'Unable to verify the selected bills.';
      }
    });
  }

  proceed(): void {
    this.done = true;
  }
}