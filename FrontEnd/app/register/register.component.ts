import { Component } from '@angular/core';
import { AuthService } from '../_services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  form: any = {
    username: null,
    name: null,
    surname: null,
    email: null,
    phone: null,
    password: null,
    confirmPassword: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService) { }

  onSubmit(): void {
    const { username, name, surname, email, phone, password, confirmPassword} = this.form;
    if (password !== confirmPassword) {
      this.errorMessage = 'Passwords not matching!';
      this.isSignUpFailed = true;
      return;
    }

    this.authService.register(username, name, surname, email, phone, password).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    });
  }
}
