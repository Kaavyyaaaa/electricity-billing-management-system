import { Routes } from '@angular/router';
import { BillsComponent, HomeComponent, LoginComponent, RegisterComponent, SummaryComponent } from './app.component';
export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent },
  { path: 'bills', component: BillsComponent },
  { path: 'summary', component: SummaryComponent },
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: '**', redirectTo: 'login' }
];
