import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BillTable } from './bill-table';

describe('BillTable', () => {
  let component: BillTable;
  let fixture: ComponentFixture<BillTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BillTable],
    }).compileComponents();

    fixture = TestBed.createComponent(BillTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
