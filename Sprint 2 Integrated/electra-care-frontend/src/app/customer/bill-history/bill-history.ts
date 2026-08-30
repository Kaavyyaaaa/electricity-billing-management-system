import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, Bill } from '../../services/api.service';

@Component({
  selector: 'app-bill-history',
  imports: [FormsModule],
  templateUrl: './bill-history.html',
  styleUrl: './bill-history.css'
})
export class BillHistory {
  private api = inject(ApiService);
  consumerNumber = '1234567890123';
  customerId: number | null = null;
  fromDate = '';
  toDate = '';
  paymentStatus = 'All';
  sortBy = 'billDate';
  bills: Bill[] = [];
  noDataMessage = '';
  loading = false;

  loadHistory() {
    this.noDataMessage = ''; this.loading = true;
    this.api.getCustomerByConsumerNumber(this.consumerNumber.trim()).subscribe({
      next: c => {
        this.customerId = c.customerId;
        this.api.getBillHistory(c.customerId, this.fromDate, this.toDate, this.paymentStatus, this.sortBy).subscribe({
          next: b => { this.bills=b; this.loading=false; },
          error: e => { this.bills=[]; this.noDataMessage=this.msg(e,'No bill history found for the selected period.'); this.loading=false; }
        });
      },
      error: e => { this.bills=[]; this.noDataMessage=this.msg(e,'Customer not found.'); this.loading=false; }
    });
  }
  applyFilters() { if (this.customerId !== null) this.loadHistory(); else this.loadHistory(); }
  clearFilters() { this.fromDate=''; this.toDate=''; this.paymentStatus='All'; this.sortBy='billDate'; this.loadHistory(); }
  viewBill(b: Bill) { alert(`Bill ${b.billNumber}\nBilling Period: ${b.billingPeriod}\nBill Date: ${b.billDate}\nDue Date: ${b.dueDate}\nAmount: ₹${b.billAmount}\nStatus: ${b.status}`); }
  downloadBill(b: Bill) {
    const content=`ELECTRA-CARE BILL\nBill ID: ${b.billId}\nBill Number: ${b.billNumber}\nConsumer Number: ${this.consumerNumber}\nBilling Period: ${b.billingPeriod}\nBill Date: ${b.billDate}\nDue Date: ${b.dueDate}\nBill Amount: ₹${b.billAmount}\nLate Fee: ₹${b.lateFee ?? 0}\nStatus: ${b.status}`;
    const url=URL.createObjectURL(new Blob([content],{type:'text/plain'})); const a=document.createElement('a'); a.href=url; a.download=`${b.billNumber}.txt`; a.click(); URL.revokeObjectURL(url);
  }
  private msg(e:any,f:string){return typeof e?.error==='string'?e.error:(e?.error?.message??f);}
}
