import { Injectable } from '@angular/core';
import {BehaviorSubject, catchError, map, of, tap} from "rxjs";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../enviroments/enviroment";
import {LoginRequest, LoginUser, RegisterUser, User, UserReq} from "../models/user";
import {NotifierService} from "angular-notifier";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  $user = new BehaviorSubject<LoginRequest | null>(null);

  constructor( private http: HttpClient,
               private router: Router, private notifierService:NotifierService) { }

  login(name: string, pass: string) {

    return this.http.post<LoginRequest>(`${environment.auth}/login`,
      {'usernameOrEmail':name, 'password':pass}).pipe(
        tap(user => this.$user.next(user))
    );
  }
  signUp(user: RegisterUser) {
    return this.http.post<LoginUser>(`${environment.auth}/signup`,
      user).pipe(
      catchError(() => {
          this.notifierService.notify('error', 'Произошла ошибка!');
          return of(null)
        }
      ));
    // return this.http.get<LoginUser>(`https://investment.lexa2hk.ru/actuator`);
  }
  logout() {
    localStorage.clear();
    void this.router.navigate(['/login'])
  }

  changePassword(value: UserReq) {
    return this.http.put(`${environment.api}/users`,value)

  }
  getUserInfo() {
    return this.http.get<User>(`${environment.api}/users/info`)
  }

}
