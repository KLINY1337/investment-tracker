import {Component, OnInit} from '@angular/core';
import {GetProfit, InvestmentPosition, InvestmentPositionsByPortfolio, Portfolio} from "../../core/models/portfolio";
import {Location} from "@angular/common";
import {StartupService} from "../../core/services/startup.service";
import {Color, ScaleType} from "@swimlane/ngx-charts";
import {Router} from "@angular/router";

@Component({
  selector: 'app-portfolio-futures',
  templateUrl: './portfolio-futures.component.html',
  styleUrls: ['./portfolio-futures.component.scss']
})
export class PortfolioFuturesComponent  implements OnInit{
  constructor(private location:Location, private startupService:StartupService, private router:Router) {
  }
  potfolio: Portfolio;
  profit: GetProfit | null;
  price: number;
  single: any[] = [
    {
      "name": "Germany",
      "series": [
        {
          "name": "1990",
          "value": 62000000
        },
        {
          "name": "2010",
          "value": 73000000
        },
        {
          "name": "2011",
          "value": 89400000
        }
      ]
    }
    ]

  view: [number, number] = [1300, 300];
  public showXAxis = true;
  public showYAxis = true;
  public gradient = false;
  public showLegend = true;
  public showXAxisLabel = true;
  public xAxisLabel: "Years";
  public showYAxisLabel = true;
  public yAxisLabel: "Salary";
  public graphDataChart: any[];
  colorScheme: Color = {
    name: 'myScheme',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: [ '#1b3dff', '#0ff'],
  };
  displayedColumns: string[] = ['Позиция', 'Биржа', 'Тип позиции', 'Цена входа', 'Цена выхода', 'Размер позиции' , 'Прибыль'];
  positions: InvestmentPosition[] = [];

  ngOnInit(): void {
    // @ts-ignore
    this.potfolio = this.location.getState().portfolioState;
    this.startupService.getAllPositions(this.potfolio.id).subscribe((res)=> this.positions = res.investmentPositions);
    this.startupService.getProfit(this.potfolio.id).subscribe((res)=> this.profit = res)
    this.startupService.getAllPrices(this.potfolio.id).subscribe(res => this.price = res ?? 0);
    this.startupService.getNumbersForGraph(this.potfolio.id).subscribe(res => {
      let result: { series: any[]; name: string }[] = [
        {
          name: 'Стоимость портфеля',
          series: []
        }
      ];
      res.forEach((currentElement: number, index: number) => result[0].series.push({name: index+1, value: currentElement}))
      this.single = result;
    });
  }
  goToNextPage(portfolio: Portfolio) {
    this.router.navigate(['position/new/', portfolio.id],  { state: { portfolioState: portfolio} })

  }
}
