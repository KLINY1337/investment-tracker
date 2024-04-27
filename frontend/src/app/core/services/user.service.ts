import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../enviroments/enviroment";
import {LoginUser} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  $user = new BehaviorSubject(null);

  constructor( private http: HttpClient,
               private router: Router) { }


  login(name: string, pass: string) {
    return this.http.post<LoginUser>(`${environment.auth}/login`,
      {'usernameOrEmail':name, 'password':pass});
  }
  logout() {
    sessionStorage.clear();
    void this.router.navigate(['/login']);
  }
}
