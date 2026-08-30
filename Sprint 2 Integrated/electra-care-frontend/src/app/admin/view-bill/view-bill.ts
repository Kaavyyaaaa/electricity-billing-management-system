import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService, Bill } from '../../services/api.service';

@Component({
  selector: 'app-view-bill',
  imports: [FormsModule],
  templateUrl: './view-bill.html',
  styleUrl: './view-bill.css'
})
export class ViewBill {
  private api=inject(ApiService);
  consumerNo=''; paymentStatus='All'; billingPeriod='All'; searched=false; errorMessage=''; selectedBill:Bill|null=null; bills:Bill[]=[]; filteredBills:Bill[]=[]; loading=false;

  searchCustomer(){
    this.errorMessage='';this.selectedBill=null;this.searched=true;
    if(!this.consumerNo.trim()){this.errorMessage='Please enter a Consumer Number.';this.filteredBills=[];return;}
    this.loading=true;
    this.api.getAdminBillsByConsumer(this.consumerNo.trim()).subscribe({
      next:b=>{this.bills=b;this.applyFilters();this.loading=false;},
      error:e=>{this.bills=[];this.filteredBills=[];this.errorMessage=this.msg(e,'Customer not found.');this.loading=false;}
    });
  }
  applyFilters(){
    let r=[...this.bills];
    if(this.paymentStatus!=='All')r=r.filter(b=>(b.status||'').toLowerCase()===this.paymentStatus.toLowerCase());
    if(this.billingPeriod!=='All')r=r.filter(b=>b.billingPeriod===this.billingPeriod);
    r.sort((a,b)=>b.billDate.localeCompare(a.billDate));this.filteredBills=r;
  }
  onFilterChange(){this.applyFilters();}
  viewDetails(b:Bill){this.selectedBill=b;}
  closeDetails(){this.selectedBill=null;}
  exportCSV(){
    const rows=[['Bill ID','Consumer No','Billing Period','Bill Date','Due Date','Bill Amount','Late Fee','Payment Status'],...this.filteredBills.map(b=>[String(b.billId),this.consumerNo,b.billingPeriod,b.billDate,b.dueDate,String(b.billAmount),String(b.lateFee??0),b.status])];
    const csv=rows.map(r=>r.join(',')).join('\n');const url=URL.createObjectURL(new Blob([csv],{type:'text/csv'}));const a=document.createElement('a');a.href=url;a.download='bill-history.csv';a.click();URL.revokeObjectURL(url);
  }
  clearSearch(){this.consumerNo='';this.paymentStatus='All';this.billingPeriod='All';this.bills=[];this.filteredBills=[];this.selectedBill=null;this.errorMessage='';this.searched=false;}
  private msg(e:any,f:string){return typeof e?.error==='string'?e.error:(e?.error?.message??f);}
}
