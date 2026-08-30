import { Routes } from '@angular/router';

import { PayBill } from './customer/pay-bill/pay-bill';
import { GenerateInvoice } from './customer/generate-invoice/generate-invoice';
import { BillHistory } from './customer/bill-history/bill-history';
import { AddBill } from './admin/add-bill/add-bill';
import { ViewBill } from './admin/view-bill/view-bill';

export const routes: Routes = [

  // Home page
  {
    path: '',
    redirectTo: 'pay-bill',
    pathMatch: 'full'
  },

  // US005 - Pay Bill
  {
    path: 'pay-bill',
    component: PayBill
  },

  // US006 - Generate Invoice
  {
    path: 'generate-invoice',
    component: GenerateInvoice
  },

  // US007 - Bill History
  {
    path: 'bill-history',
    component: BillHistory
  },

  // US015 - Admin Add Bill
  {
    path: 'add-bill',
    component: AddBill
  },

  // US016 - Admin View Bill
  {
    path: 'view-bill',
    component: ViewBill
  },

  // Unknown URL
  {
    path: '**',
    redirectTo: 'pay-bill'
  }
];