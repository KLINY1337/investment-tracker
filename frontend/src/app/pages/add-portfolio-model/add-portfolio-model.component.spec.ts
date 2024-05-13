import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPortfolioModelComponent } from './add-portfolio-model.component';

describe('AddPortfolioModelComponent', () => {
  let component: AddPortfolioModelComponent;
  let fixture: ComponentFixture<AddPortfolioModelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddPortfolioModelComponent]
    });
    fixture = TestBed.createComponent(AddPortfolioModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
