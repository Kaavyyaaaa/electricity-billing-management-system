import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Bill {
  billId: number;
  customerId: number;
  billNumber: string;
  billingPeriod: string;
  billDate: string;
  dueDate: string;
  disconnectionDate?: string;
  billAmount: number;
  lateFee: number;
  unitsConsumed?: number;
  status: string;
  paymentDate?: string;
  paymentMode?: string;
}

export interface Customer {
  customerId: number;
  consumerNumber: string;
  fullName: string;
  address: string;
  email: string;
  mobileNumber: string;
}

export interface Payment {
  paymentId: number;
  transactionId: string;
  receiptNumber: string;
  paymentDate: string;
  paymentMethod: string;
  transactionType: string;
  totalAmount: number;
  paymentStatus: string;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api';

  getCustomerByConsumerNumber(consumerNumber: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/customers/consumer/${encodeURIComponent(consumerNumber)}`);
  }

  getCustomerBills(customerId: number): Observable<Bill[]> {
    return this.http.get<Bill[]>(`${this.baseUrl}/bills/customer/${customerId}`);
  }

  getBillHistory(customerId: number, startDate?: string, endDate?: string, status?: string, sortBy?: string): Observable<Bill[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    if (status && status !== 'All') params = params.set('status', status);
    if (sortBy) params = params.set('sortBy', sortBy);
    return this.http.get<Bill[]>(`${this.baseUrl}/bills/history/${customerId}`, { params });
  }

  addBill(bill: Partial<Bill>): Observable<Bill> {
    return this.http.post<Bill>(`${this.baseUrl}/bills`, bill);
  }

  payBill(request: {
    billId: number;
    cardNumber: string;
    expiryDate: string;
    cvv: string;
    cardholderName: string;
    paymentMethod: string;
    amount: number;
  }): Observable<Payment> {
    return this.http.post<Payment>(`${this.baseUrl}/bills/pay`, request);
  }

  getInvoice(transactionId: string): Observable<Record<string, any>> {
    return this.http.get<Record<string, any>>(`${this.baseUrl}/invoices/transaction/${encodeURIComponent(transactionId)}`);
  }

  getAdminBillsByConsumer(consumerNumber: string): Observable<Bill[]> {
    return this.http.get<Bill[]>(`${this.baseUrl}/admin/bills/consumer/${encodeURIComponent(consumerNumber)}`);
  }

  searchAdminBills(customerId: number, startDate?: string, endDate?: string, status?: string): Observable<Bill[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    if (status && status !== 'All') params = params.set('status', status);
    return this.http.get<Bill[]>(`${this.baseUrl}/admin/bills/search/${customerId}`, { params });
  }

  exportAdminBills(customerId: number, startDate?: string, endDate?: string, status?: string): Observable<Blob> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    if (status && status !== 'All') params = params.set('status', status);
    return this.http.get(`${this.baseUrl}/admin/bills/export/${customerId}`, { params, responseType: 'blob' });
  }
}
