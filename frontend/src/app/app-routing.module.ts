import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {RegisterComponent} from "./pages/register/register.component";
import {MainComponent} from "./pages/main/main.component";
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {AccountComponent} from "./pages/account/account.component";
import {PortfolioComponent} from "./pages/portfolio/portfolio.component";
import {PortfolioFuturesComponent} from "./pages/portfolio-futures/portfolio-futures.component";
import {NewPositionComponent} from "./pages/new-position/new-position.component";

const routes: Routes = [
  {
    path:'login',
    component: LoginComponent,
    // canActivate: [AuthGuard]
  },
  {
    path:'register',
    component: RegisterComponent,
    // canActivate: [AuthGuard]
  },
  {
    path:'',
    component: MainComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'account',
        component: AccountComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'portfolio',
        component: PortfolioComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'portfolio-futures/:id',
        component: PortfolioFuturesComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'position/new/:id',
        component: NewPositionComponent,
        canActivate: [AuthGuard],
      },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
