import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../core/services/user.service";
import {Router} from "@angular/router";
import {take} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
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
    return this.loginForm.get('userName')
  };

  get pass(): any {
    return this.loginForm.get('userPass')
  };

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      userName: ['', Validators.required],
      userPass: ['', Validators.required],
    })
  }

  submit() {
    if (this.loginForm.valid) {
      this.loading = true
      this.userService.login(this.name.value, this.pass.value).pipe(take(1)).subscribe({
        next: (r) => {
            this.loading = false;
            this.loginError = false;
            // sessionStorage.setItem('token', r.data);
            void this.router.navigate(
              ['/'],
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
