import {Component, signal} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {UserService} from "../../core/services/user.service";
import {StartupService} from "../../core/services/startup.service";
import {UserReq} from "../../core/models/user";
import {NotifierService} from "angular-notifier";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent {
  isOpenedForm = signal<boolean>(false);
  newPassword: FormControl = new FormControl('', Validators.required);
  currencyControl: FormControl = new FormControl<string>('RUB');
  constructor(private userService:UserService, private startupService:StartupService, private notifierService:NotifierService) {
  }
  user = this.userService.getUserInfo();
  readonly portfoliosCountByUserId = this.startupService.getPortfoliosCountByUserId();
  readonly investmentPrice = this.startupService.getInvestmentPrice();

  logout() {
    this.userService.logout();
  }

  changePassword(email: string, username: string) {
    if(this.newPassword.valid) {
      let user: UserReq = {
        email: email,
        username: username,
        password: this.newPassword.value
      }
      this.userService.changePassword(user).subscribe({
        next: (r) => {
          this.notifierService.notify('success', 'Пароль успешно сменен!');
          this.user = this.userService.getUserInfo();        },
        error: () => {
        },
        complete: () => {
        }
      })
    }
  }
}
