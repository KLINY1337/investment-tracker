import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {UserService} from "../services/user.service";

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(private userService: UserService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (localStorage.getItem('token') !== null) {
      req = req.clone({
        setHeaders:{
          Authorization: localStorage.getItem('token') as string
        }
      })
    }
    return next.handle(req).pipe(catchError((e: HttpErrorResponse) => {
      if(e.status === 401 || e.status === 403) {
        this.userService.logout();
      }
      return throwError(() => e.message);
    }));
  }
}
