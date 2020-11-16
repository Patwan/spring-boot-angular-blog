import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { LoginRequestPayload } from './login-request.payload';
import { AuthService } from '../shared/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage: string;
  isError: boolean;

  //Constructor is used to create new instance of a class. ... 
  //The ngOnInit is called after the constructor is executed.
  //The variables are private meaning can only be accessed inside this class
  constructor(private authService: AuthService, 
              private activatedRoute: ActivatedRoute,
              private router: Router, 
              private toastr: ToastrService) {
    this.loginRequestPayload = {
      username: '',
      password: ''
    };
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

    //We use this keyword and reach out to actiatedRoute variable (whose type is ActivatedRoute
    //that is imported from angular router) then reach out to queryParams  property
    //that returns an observable/promise. We subscribe/listen to the observable
    //and check if the property true exists, if true we show a toast on the U.I and
    //also show a success message. This is inside ngOnInit lifecycle hook
    //hence it will run immediately thsi component is created
    this.activatedRoute.queryParams
      .subscribe(params => {
        if (params.registered !== undefined && params.registered === 'true') {
          this.toastr.success('Signup Successful');
          this.registerSuccessMessage = 'Please Check your inbox for activation email '
            + 'activate your account before you Login!';
        }
      });
  }

  login() {
    //Once a user has loggedIn from the U.I, we capture the values and store them 
    // in the loginRequestPayload model
    this.loginRequestPayload.username = this.loginForm.get('username').value;
    this.loginRequestPayload.password = this.loginForm.get('password').value;
    
    //Next we use this keyword and reach out to authService variable (whose type is AuthService
    //class). We then reach out to login method (and pass in the loginRequestpayload 
    //method which holds our values temporalily) and then subscribe to the observable/promise.
    //This will contain teh response from the backend
    this.authService.login(this.loginRequestPayload).subscribe(data => {
      this.isError = false;
      this.router.navigateByUrl('');
      this.toastr.success('Login Successful');
    }, error => {
      this.isError = true;
      throwError(error);
    });
  }

}
