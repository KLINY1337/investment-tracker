import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortfolioFuturesComponent } from './portfolio-futures.component';

describe('PortfolioFuturesComponent', () => {
  let component: PortfolioFuturesComponent;
  let fixture: ComponentFixture<PortfolioFuturesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PortfolioFuturesComponent]
    });
    fixture = TestBed.createComponent(PortfolioFuturesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
