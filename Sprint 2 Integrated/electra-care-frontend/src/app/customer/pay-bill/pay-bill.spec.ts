import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PayBill } from './pay-bill';

describe('PayBill', () => {
  let component: PayBill;
  let fixture: ComponentFixture<PayBill>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PayBill],
    }).compileComponents();

    fixture = TestBed.createComponent(PayBill);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
