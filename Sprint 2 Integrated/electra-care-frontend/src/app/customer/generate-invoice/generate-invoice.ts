import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-generate-invoice',
  imports: [FormsModule, RouterLink],
  templateUrl: './generate-invoice.html',
  styleUrl: './generate-invoice.css'
})
export class GenerateInvoice {
  private api = inject(ApiService);
  private route = inject(ActivatedRoute);

  transactionId = '';
  invoice: Record<string, any> | null = null;
  errorMessage = '';
  loading = false;

  constructor() {
    this.route.queryParams.subscribe(p => {
      if (p['transactionId']) {
        this.transactionId = p['transactionId'];
        this.generateInvoice();
      }
    });
  }

  generateInvoice() {
    this.errorMessage = ''; this.invoice = null;
    if (!this.transactionId.trim()) { this.errorMessage = 'Please enter a Transaction ID.'; return; }
    this.loading = true;
    this.api.getInvoice(this.transactionId.trim()).subscribe({
      next: data => { this.invoice = data; this.loading = false; },
      error: err => { this.errorMessage = typeof err?.error === 'string' ? err.error : 'Unable to generate invoice.'; this.loading = false; }
    });
  }

  downloadInvoice() {
    if (!this.invoice) return;
    const lines = Object.entries(this.invoice).map(([k,v]) => `${k}: ${v ?? ''}`);
    const url = URL.createObjectURL(new Blob(['ELECTRA-CARE INVOICE\n\n', lines.join('\n')], {type:'text/plain'}));
    const a = document.createElement('a'); a.href=url; a.download=`Invoice-${this.invoice['invoiceNumber']}.txt`; a.click(); URL.revokeObjectURL(url);
  }

  clear() { this.transactionId=''; this.invoice=null; this.errorMessage=''; }
}
