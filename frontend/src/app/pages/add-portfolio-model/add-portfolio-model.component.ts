import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-add-portfolio-model',
  templateUrl: './add-portfolio-model.component.html',
  styleUrls: ['./add-portfolio-model.component.scss']
})
export class AddPortfolioModelComponent {
  constructor( public dialogRef: MatDialogRef<AddPortfolioModelComponent>) {
    dialogRef.disableClose = true;
  }
  onNoClick(): void {
    this.dialogRef.close(null);
  }
  name: FormControl = new FormControl<string>('', Validators.required);

  save() {
    if(this.name.valid) {
      this.dialogRef.close(this.name.value)
    }
  }
}
