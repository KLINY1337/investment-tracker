import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {RegisterComponent} from "./pages/register/register.component";
import {MainComponent} from "./pages/main/main.component";

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
    canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
