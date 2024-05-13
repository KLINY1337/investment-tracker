import {Component, OnInit} from '@angular/core';
import {GetTotalPrice, Portfolio, PortfolioDistribution} from "../../core/models/portfolio";
import {Color, ScaleType} from "@swimlane/ngx-charts";
import {Router} from "@angular/router";
import {StartupService} from "../../core/services/startup.service";
import {InputModalityDetector} from "@angular/cdk/a11y";
import {AddPortfolioModelComponent} from "../add-portfolio-model/add-portfolio-model.component";
import {MatDialog} from "@angular/material/dialog";
import {NotifierService} from "angular-notifier";
export interface SingleData {
  name: string,
  value: number
}
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit{
  constructor(private router:Router, private startupService:StartupService, public dialog: MatDialog, private notifierService:NotifierService) {
  }
  dataT: null | PortfolioDistribution = {
    spotDistribution: 0,
    futuresDistribution:0
  }
  view: [number, number] = [1000, 300];
  gradient: boolean = true;
  showLegend: boolean = true;
  showLabels: boolean = true;
  isDoughnut: boolean = false;
  colorScheme: Color = {
    name: 'myScheme',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: [ '#1b3dff', '#0ff'],
  };

  single: SingleData[] = [
    {
      name: 'СПОТ',
      value: this.dataT?.spotDistribution!
    },
    {
      name: 'ФЬЮЧЕРС',
      value: this.dataT?.futuresDistribution !
    },
  ];
  portfolios: Portfolio[] = [];
  displayedColumns: string[] = ['Название', 'Действие'];
  priceTotal: null | GetTotalPrice;
  goToNextPage(portfolio: Portfolio) {
    this.router.navigate(['portfolio-futures', portfolio.id],  { state: { portfolioState: portfolio} })

  }

  ngOnInit(): void {
    this.getInfo();
  }
  getInfo() {
    this.startupService.getAllPortfolios().subscribe((res)=> this.portfolios = res.slice(0,5));
    this.startupService.getDistribution().subscribe((res)=> this.dataT = res);
    this.startupService.getInvestmentPrice().subscribe(res => this.priceTotal = res)
  }
  openDialog() {
    const dialogRef = this.dialog.open(AddPortfolioModelComponent, {

    });
    dialogRef.afterClosed().subscribe((res: string) =>  {
      if (res != null) {
        this.startupService.createPortfolio(res).subscribe({
          next: (r) => {
              this.notifierService.notify('success', 'Портфель успешно добавлен!');
              this.getInfo();
          },
          error: () => {
          },
          complete: () => {
          }
        })
      }
    });
  }
}
