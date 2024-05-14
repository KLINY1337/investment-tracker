import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {environment} from "../../../enviroments/enviroment";
import {
  AddPosition,
  GetPortfoliosCountByUserIdResponse,
  GetProfit,
  GetTotalPrice, InvestmentPosition, InvestmentPositionsByPortfolio,
  Portfolio,
  PortfolioDistribution, Ticker
} from "../models/portfolio";
import {catchError, of} from "rxjs";
import {NotifierService} from "angular-notifier";

@Injectable({
  providedIn: 'root'
})
export class StartupService {

  constructor(private http: HttpClient,
              private router: Router, private notifierService: NotifierService) {
  }

  getPortfoliosCountByUserId() {
    return this.http.get<GetPortfoliosCountByUserIdResponse>(`${environment.portfolios}/count`).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
  }

  getInvestmentPrice() {
    return this.http.get<GetTotalPrice>(`${environment.investment_positions}/price/total`).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
  }

  getAllPortfolios() {
    return this.http.get<Portfolio[]>(`${environment.portfolios}`).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of([])
        }
      ));
  }

  getDistribution() {
    return this.http.get<PortfolioDistribution>(`${environment.investment_positions}/distribution`).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
  }

  createPortfolio(res: string) {
    let a = {
      name: res
    }
    return this.http.post<Portfolio>(`${environment.portfolios}`, a).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
  }

  deletePortfolio(id: number) {
    let q = new HttpParams().append('idList', id)
    return this.http.delete<Portfolio>(`${environment.portfolios}`, {params: q}).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
  }

  getProfit(portfolioId: number) {
    let q = new HttpParams().append('portfolioId', portfolioId)
    return this.http.get<GetProfit>(`${environment.investment_positions}/profit/byPortfolioId`, {params: q}).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
  }

  getAllPrices(portfolioId: number) {
    let q = new HttpParams().append('portfolioId', portfolioId)
    return this.http.get<number>(`${environment.portfolios}/price`,{params: q})
  }

  getNumbersForGraph(portfolioId: number) {
    let q = new HttpParams().append('portfolioId', portfolioId)
    return this.http.get<number[]>(`${environment.portfolios}/chart/price`, {params: q});
  }
  getAllPositions(idList: number) {
    let q = new HttpParams().append('idList', idList)
    return this.http.get<InvestmentPositionsByPortfolio>(`${environment.investment_positions}/byPortfoliosIds`, {params: q})
  }
  getAllTickers() {
    return this.http.get<Ticker[]>(`${environment.api}/tickers/all`)
  }

  createPosition(adding: AddPosition) {
    return this.http.post<Portfolio>(`${environment.investment_positions}`, adding).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
  }
}
