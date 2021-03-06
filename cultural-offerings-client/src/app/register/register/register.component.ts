import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterUser } from 'src/app/core/model/register-user';
import { AuthService } from 'src/app/core/services/security/auth-service/auth.service';
import { RegisterService } from 'src/app/core/services/security/register-service/register.service';
import { mustMatchValidator } from 'src/app/shared/validators/must-match/must-match.directive';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;

  submitted: boolean = false;
  errorMsg: string;
  successMsg: string;

  constructor(
    public formBuilder: FormBuilder,
    public registerService: RegisterService) 
  {
    this.registerForm = this.formBuilder.group({
      "nameField": ["", Validators.required],
      "surnameField": ["", Validators.required],
      "emailField": ["", [Validators.required, Validators.email]],
      "passField": ["", [Validators.required, Validators.minLength(3)]],
      "confPassField": ["", [Validators.required]]
    }, { validators: mustMatchValidator });
  }

  ngOnInit(): void {

  }

  register(): void {
    // if someone tries to send invalid data do not send request
    if (this.registerForm.invalid) {
      return;
    }

    // hide form controls when user send request
    this.submitted = true;
    this.errorMsg = undefined;
    this.successMsg = undefined;

    let email: string = this.registerForm.value['emailField'];
    let newUser: RegisterUser = {
      firstName: this.registerForm.value['nameField'],
      lastName: this.registerForm.value['surnameField'],
      email,
      password: this.registerForm.value['passField']
    };

    this.registerService.sendRegistrationRequest(newUser).subscribe(
      data => {
        this.successMsg = `Uspesno ste izvrsili registraciju. Aktivacioni link je poslat na Vasu email adresu (${email}).`;
      },
      error => {
        this.submitted = false;
        this.errorMsg = "Unesena email adresa je zauzeta, molimo Vas unesite neku drugu.";
      }
    );
  }

}
