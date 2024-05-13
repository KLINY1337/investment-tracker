import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { MainComponent } from './main/main.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatCardModule} from "@angular/material/card";
import {RouterModule} from "@angular/router";
import {MatDialogModule} from "@angular/material/dialog";
import {MatDividerModule} from "@angular/material/divider";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatSelectModule} from "@angular/material/select";
import {MatNativeDateModule, MatOptionModule} from "@angular/material/core";
import {MatIconModule} from "@angular/material/icon";
import { DashboardComponent } from './dashboard/dashboard.component';
import { PortfolioComponent } from './portfolio/portfolio.component';
import { AccountComponent } from './account/account.component';
import { HeaderComponent } from './header/header.component';
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {NotifierModule, NotifierOptions} from "angular-notifier";
import { AddPortfolioModelComponent } from './add-portfolio-model/add-portfolio-model.component';
import {LineChartModule, PieChartModule} from "@swimlane/ngx-charts";
import {MatTableModule} from "@angular/material/table";
import {CdkColumnDef, CdkTableModule} from "@angular/cdk/table";
import { PortfolioFuturesComponent } from './portfolio-futures/portfolio-futures.component';
import { NewPositionComponent } from './new-position/new-position.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
const customNotifierOptions: NotifierOptions = {
  position: {
    horizontal: {
      position: 'right',
      distance: 12
    },
    vertical: {
      position: 'top',
      distance: 12,
      gap: 10
    }
  },
  theme: 'material',
  behaviour: {
    autoHide: 5000,
    onClick: 'hide',
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease'
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50
    },
    shift: {
      speed: 300,
      easing: 'ease'
    },
    overlap: 150
  }
};
@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    MainComponent,
    DashboardComponent,
    PortfolioComponent,
    AccountComponent,
    HeaderComponent,
    AddPortfolioModelComponent,
    PortfolioFuturesComponent,
    NewPositionComponent
  ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatProgressBarModule,
        MatCardModule,
        RouterModule,
      MatNativeDateModule,
      MatDialogModule,
        MatIconModule,
        MatOptionModule,
        MatSelectModule,
        MatCheckboxModule,
        MatButtonToggleModule,
        NotifierModule.withConfig(customNotifierOptions),
        PieChartModule,
        MatTableModule,
        CdkTableModule,
        LineChartModule,
        MatDatepickerModule,
    ],
  providers:[CdkColumnDef]

})
export class PagesModule { }
