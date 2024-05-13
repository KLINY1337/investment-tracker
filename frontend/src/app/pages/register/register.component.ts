import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../core/services/user.service";
import {Router} from "@angular/router";
import {take} from "rxjs";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit{
  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
  ) {
  }
  loginForm: FormGroup;
  loading = false;
  loginError = false;

  get name(): any {
    return this.loginForm.get('username')
  };

  get pass(): any {
    return this.loginForm.get('password')
  };
  get email(): any {
    return this.loginForm.get('email')
  };

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required],
    })
  }

  submit() {
    if (this.loginForm.valid) {
      this.loading = true
      this.userService.signUp(this.loginForm.value).pipe(take(1)).subscribe({
        next: (r) => {
          this.loading = false;
          this.loginError = false;
          // sessionStorage.setItem('token', r.data);
          void this.router.navigate(
            ['/login'],
          );
        },
        error: () => {
          this.loading = false;
          this.loginError = true;
        },
        complete: () => {
          this.loading = false;
        }})
    }
  }
}
