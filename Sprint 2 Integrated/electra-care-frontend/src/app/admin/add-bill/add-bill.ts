import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-add-bill',
  imports: [FormsModule],
  templateUrl: './add-bill.html',
  styleUrl: './add-bill.css'
})
export class AddBill {
  private api = inject(ApiService);
  consumerNo=''; billingPeriod=''; billDate=''; dueDate=''; disconnectionDate='';
  billAmount:number|null=null; lateFee:number|null=0; unitsConsumed:number|null=null; status='UNPAID';
  errorMessage=''; successMessage=''; generatedBillId:number|null=null; generatedBillNumber=''; loading=false;

  saveBill() {
    this.errorMessage=''; this.successMessage='';
    if(!this.consumerNo||!this.billingPeriod||!this.billDate||!this.dueDate||!this.billAmount) { this.errorMessage='Please fill all required fields.'; return; }
    if(this.billAmount<=0){this.errorMessage='Bill Amount must be greater than zero.';return;}
    if(this.lateFee!==null&&this.lateFee<0){this.errorMessage='Late Fee cannot be negative.';return;}
    if(this.billDate>this.dueDate){this.errorMessage='Bill Date cannot be after Due Date.';return;}
    if(this.disconnectionDate&&this.disconnectionDate<this.dueDate){this.errorMessage='Disconnection Date cannot be before Due Date.';return;}
    this.loading=true;
    this.api.getCustomerByConsumerNumber(this.consumerNo.trim()).subscribe({
      next:c=>this.api.addBill({customerId:c.customerId,billingPeriod:this.billingPeriod,billDate:this.billDate,dueDate:this.dueDate,disconnectionDate:this.disconnectionDate||undefined,billAmount:this.billAmount,lateFee:this.lateFee??0,unitsConsumed:this.unitsConsumed??undefined,status:this.status}).subscribe({
        next:b=>{this.generatedBillId=b.billId;this.generatedBillNumber=b.billNumber;this.successMessage='Bill has been successfully added.';this.loading=false;},
        error:e=>{this.errorMessage=this.msg(e,'Unable to save bill.');this.loading=false;}
      }),
      error:e=>{this.errorMessage=this.msg(e,'Consumer number not found.');this.loading=false;}
    });
  }
  clearForm(){this.consumerNo='';this.billingPeriod='';this.billDate='';this.dueDate='';this.disconnectionDate='';this.billAmount=null;this.lateFee=0;this.unitsConsumed=null;this.status='UNPAID';this.errorMessage='';this.successMessage='';this.generatedBillId=null;this.generatedBillNumber='';}
  private msg(e:any,f:string){return typeof e?.error==='string'?e.error:(e?.error?.message??f);}
}
