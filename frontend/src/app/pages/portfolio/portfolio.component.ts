import {Component, OnInit} from '@angular/core';
import {Portfolio} from "../../core/models/portfolio";
import {Router} from "@angular/router";
import {StartupService} from "../../core/services/startup.service";
import {AddPortfolioModelComponent} from "../add-portfolio-model/add-portfolio-model.component";
import {MatDialog} from "@angular/material/dialog";
import {NotifierService} from "angular-notifier";

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.scss']
})
export class PortfolioComponent implements OnInit{
  constructor(private router:Router, private startupService:StartupService,  public dialog: MatDialog,private notifierService:NotifierService) {
  }
  portfolios: Portfolio[] = [];
  displayedColumns: string[] = ['Название', 'Действие'];
  goToNextPage(portfolio: Portfolio) {
    this.router.navigate(['portfolio-futures', portfolio.id],  { state: { portfolioState: portfolio} })

  }

  ngOnInit(): void {
    this.getAllPortfolios()
  }
  getAllPortfolios() {
    this.startupService.getAllPortfolios().subscribe((res)=> this.portfolios = res);

  }
  openDialog() {
    const dialogRef = this.dialog.open(AddPortfolioModelComponent, {

    });
    dialogRef.afterClosed().subscribe((res: string) => {
      if (res != null) {
        this.startupService.createPortfolio(res).subscribe({
          next: (r) => {
            this.notifierService.notify('success', 'Портфель успешно добавлен!');
            this.getAllPortfolios();
          },
          error: () => {
          },
          complete: () => {
          }
        })
    }
    });
  }

  deleteP(row: Portfolio) {
    this.startupService.deletePortfolio(row.id).subscribe({
      next: (r) => {
        this.notifierService.notify('success', 'Портфель успешно удален!');
        this.getAllPortfolios();
      },
      error: () => {
      },
      complete: () => {
      }
    })
  }
}
