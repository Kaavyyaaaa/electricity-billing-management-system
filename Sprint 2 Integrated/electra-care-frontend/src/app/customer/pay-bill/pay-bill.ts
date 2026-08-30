import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService, Bill, Payment } from '../../services/api.service';

@Component({
  selector: 'app-pay-bill',
  imports: [FormsModule, RouterLink],
  templateUrl: './pay-bill.html',
  styleUrl: './pay-bill.css'
})
export class PayBill {
  private api = inject(ApiService);

  consumerNumber = '';
  bills: Bill[] = [];
  selectedBillId: number | null = null;
  selectedBill: Bill | null = null;

  cardholderName = '';
  cardNumber = '';
  expiryDate = '';
  cvv = '';
  paymentMethod = 'Card';

  cardholderNameError = '';
  cardNumberError = '';
  expiryDateError = '';
  cvvError = '';
  errorMessage = '';
  successMessage = '';
  loading = false;
  showConfirmation = false;
  payment: Payment | null = null;

  loadBills() {
    this.errorMessage = '';
    this.bills = [];
    this.selectedBill = null;
    if (!this.consumerNumber.trim()) {
      this.errorMessage = 'Consumer Number is required.';
      return;
    }
    this.loading = true;
    this.api.getCustomerByConsumerNumber(this.consumerNumber.trim()).subscribe({
      next: customer => {
        this.api.getCustomerBills(customer.customerId).subscribe({
          next: bills => {
            this.bills = bills.filter(b => !('PAID'.toLowerCase() === (b.status || '').toLowerCase()));
            if (!this.bills.length) this.errorMessage = 'No unpaid bills found for this customer.';
            this.loading = false;
          },
          error: err => { this.errorMessage = this.message(err, 'Unable to retrieve bills.'); this.loading = false; }
        });
      },
      error: err => { this.errorMessage = this.message(err, 'Customer not found.'); this.loading = false; }
    });
  }

  selectBill() {
    this.selectedBill = this.bills.find(b => b.billId === Number(this.selectedBillId)) ?? null;
  }

  submitPayment() {
    this.clearErrors();
    if (!this.selectedBill) { this.errorMessage = 'Please select a bill.'; return; }
    let valid = true;
    if (!this.cardholderName.trim()) { this.cardholderNameError = 'Cardholder name is required.'; valid = false; }
    else if (!/^[a-zA-Z ]+$/.test(this.cardholderName.trim())) { this.cardholderNameError = 'Cardholder name must contain only letters.'; valid = false; }
    if (!/^\d{16}$/.test(this.cardNumber)) { this.cardNumberError = 'Card number must contain exactly 16 digits.'; valid = false; }
    if (!this.isValidExpiryDate(this.expiryDate)) { this.expiryDateError = 'Enter a valid, non-expired date in MM/YY format.'; valid = false; }
    if (!/^\d{3,4}$/.test(this.cvv)) { this.cvvError = 'CVV must contain 3 or 4 digits.'; valid = false; }
    if (valid) this.showConfirmation = true;
  }

  confirmPayment() {
    if (!this.selectedBill) return;
    this.showConfirmation = false;
    this.loading = true;
    this.api.payBill({
      billId: this.selectedBill.billId,
      cardNumber: this.cardNumber,
      expiryDate: this.expiryDate,
      cvv: this.cvv,
      cardholderName: this.cardholderName,
      paymentMethod: this.paymentMethod,
      amount: this.selectedBill.billAmount
    }).subscribe({
      next: payment => {
        this.payment = payment;
        this.successMessage = 'Payment completed successfully.';
        this.loading = false;
      },
      error: err => {
        this.errorMessage = this.message(err, 'Payment failed.');
        this.loading = false;
      }
    });
  }

  cancelConfirmation() { this.showConfirmation = false; }

  downloadReceipt() {
    if (!this.payment || !this.selectedBill) return;
    const text = [
      'ELECTRA-CARE PAYMENT RECEIPT',
      `Payment ID: ${this.payment.paymentId}`,
      `Transaction ID: ${this.payment.transactionId}`,
      `Receipt Number: ${this.payment.receiptNumber}`,
      `Transaction Date: ${this.payment.paymentDate}`,
      `Transaction Type: ${this.payment.transactionType}`,
      `Bill Number: ${this.selectedBill.billNumber}`,
      `Transaction Amount: ₹${this.payment.totalAmount}`,
      `Transaction Status: ${this.payment.paymentStatus}`
    ].join('\n');
    this.downloadText(text, `Receipt-${this.payment.receiptNumber}.txt`);
  }

  reset() {
    this.payment = null; this.successMessage = ''; this.errorMessage = '';
    this.consumerNumber = ''; this.bills = []; this.selectedBill = null; this.selectedBillId = null;
    this.cardholderName = ''; this.cardNumber = ''; this.expiryDate = ''; this.cvv = '';
    this.clearErrors();
  }

  clearErrors() { this.cardholderNameError=''; this.cardNumberError=''; this.expiryDateError=''; this.cvvError=''; }

  isValidExpiryDate(value: string) {
    if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(value)) return false;
    const [m,y] = value.split('/').map(Number);
    const now = new Date();
    return y + 2000 > now.getFullYear() || (y + 2000 === now.getFullYear() && m >= now.getMonth()+1);
  }

  private message(err: any, fallback: string): string {
    return typeof err?.error === 'string' ? err.error : (err?.error?.message ?? fallback);
  }
  private downloadText(text: string, filename: string) {
    const url = URL.createObjectURL(new Blob([text], {type:'text/plain'}));
    const a = document.createElement('a'); a.href=url; a.download=filename; a.click(); URL.revokeObjectURL(url);
  }
}
