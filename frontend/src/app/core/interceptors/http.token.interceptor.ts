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
    if (sessionStorage.getItem('token') !== null) {
      req = req.clone({
        setHeaders:{
          Authorization: sessionStorage.getItem('token') as string
        }
      })
    }
    return next.handle(req).pipe(catchError((e: HttpErrorResponse) => {
      if(e.error && e.error.error_code == 1 && (e.status === 401 || e.status === 403)) {
        this.userService.logout();
      }
      return throwError(() => e.message);
    }));
  }
}
