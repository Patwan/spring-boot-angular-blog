import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SignupRequestPayload } from './singup-request.payload';
import { AuthService } from '../shared/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupRequestPayload: SignupRequestPayload;
  signupForm: FormGroup;

  
  //Constructor is used to create new instance of a class. ... 
  //The ngOnInit is called after the constructor is executed.
  //The variables are private meaning can only be accessed inside this class
  constructor(private authService: AuthService, 
              private router: Router,
              private toastr: ToastrService) {
    this.signupRequestPayload = {
      username: '',
      email: '',
      password: ''
    };
  }

  //Inside the ngOnInit() lifecycle hook (runs immediately after this class is instantiated)
  //we initialized this signupForm variable by assigning it to a new FormGroup which takes two FormControl objects email, username, and password.
  //We also add Validations on each field
  ngOnInit() {
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
    });
  }

  //Inside this method we read the values from our formgroup and map them to respectve
  //fields in the model
  signup() {
    this.signupRequestPayload.email = this.signupForm.get('email').value;
    this.signupRequestPayload.username = this.signupForm.get('username').value;
    this.signupRequestPayload.password = this.signupForm.get('password').value;

    //We make a http call inside authService and since it returns an Observable(which
    //is a promise in Angular) we will subscribe to the response and inside it navigate to login 
    //page, if tehre is an error we display a custom message to the user on the UI and log
    //If the registation is successful we redirect the user to login page, and append a parameter
    //to the url called true
    this.authService.signup(this.signupRequestPayload)
      .subscribe(data => {
        this.router.navigate(['/login'],
          { queryParams: { registered: 'true' } });
      }, error => {
        console.log(error);
        this.toastr.error('Registration Failed! Please try again');
      });
  }
}
