import {Component, OnInit} from '@angular/core';
import {StartupService} from "../../core/services/startup.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AddPosition, Portfolio, Ticker} from "../../core/models/portfolio";
import {Location} from "@angular/common";
import {NotifierService} from "angular-notifier";

@Component({
  selector: 'app-new-position',
  templateUrl: './new-position.component.html',
  styleUrls: ['./new-position.component.scss']
})
export class NewPositionComponent implements OnInit{
  constructor(private service:StartupService, private notifierService:NotifierService,private startupService:StartupService,private fb: FormBuilder, private location:Location) {
  }
  createStartup: FormGroup;
  tickers: Ticker[] =[];
  potfolio: Portfolio;
  ngOnInit(): void {
    // @ts-ignore
    this.potfolio = this.location.getState().portfolioState;
    this.service.getAllTickers().subscribe((res)=> this.tickers = res);
    this.createStartup = this.fb.group({
      tickerId: [null, Validators.required],
      openDate: [null, Validators.required],
      closeDate: [null, Validators.required],
      openQuoteAssetPrice: [null, Validators.required],
      closeQuoteAssetPrice: [null, Validators.required],
      baseAssetAmount: [null, Validators.required],
    })
  }
  get tickerId(): any {
    return this.createStartup.get('tickerId')
  };
  get openDate(): any {
    return this.createStartup.get('openDate')
  };
  get closeDate(): any {
    return this.createStartup.get('closeDate')
  };
  get openQuoteAssetPrice(): any {
    return this.createStartup.get('openQuoteAssetPrice')
  };
  get closeQuoteAssetPrice(): any {
    return this.createStartup.get('closeQuoteAssetPrice')
  };
  get baseAssetAmount(): any {
    return this.createStartup.get('baseAssetAmount')
  };

  submit() {
    console.log(new Date(this.closeDate.value).toISOString())
    if(this.createStartup.valid) {
      let adding: AddPosition = {
        "tickerId": this.tickerId.value,
        "portfolioId": this.potfolio.id,
        "openDate": new Date(this.openDate.value).toISOString(),
        "closeDate": new Date(this.closeDate.value).toISOString(),
        "openQuoteAssetPrice": this.openQuoteAssetPrice.value,
        "closeQuoteAssetPrice": this.closeQuoteAssetPrice.value,
        "baseAssetAmount": this.baseAssetAmount.value
      }
      this.startupService.createPosition(adding).subscribe({
        next: (r) => {
          this.notifierService.notify('success', 'Позиция успешно добавлен!');
          this.location.back();
        },
        error: () => {
        },
        complete: () => {
        }
      })
    }
  }

  goBack() {
    this.location.back();
  }
}
