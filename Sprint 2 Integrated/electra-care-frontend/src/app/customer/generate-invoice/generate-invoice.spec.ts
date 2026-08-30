import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenerateInvoice } from './generate-invoice';

describe('GenerateInvoice', () => {
  let component: GenerateInvoice;
  let fixture: ComponentFixture<GenerateInvoice>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenerateInvoice],
    }).compileComponents();

    fixture = TestBed.createComponent(GenerateInvoice);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
