import { Routes } from '@angular/router';

import { PayBill } from './customer/pay-bill/pay-bill';
import { GenerateInvoice } from './customer/generate-invoice/generate-invoice';
import { BillHistory } from './customer/bill-history/bill-history';
import { AddBill } from './admin/add-bill/add-bill';
import { ViewBill } from './admin/view-bill/view-bill';

export const routes: Routes = [
  {
    path: 'pay-bill',
    component: PayBill
  },
  {
    path: 'generate-invoice',
    component: GenerateInvoice
  },
  {
    path: 'bill-history',
    component: BillHistory
  },
  {
    path: 'add-bill',
    component: AddBill
  },
  {
    path: 'view-bill',
    component: ViewBill
  }
];