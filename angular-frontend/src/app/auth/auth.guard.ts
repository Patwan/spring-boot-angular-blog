import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './shared/auth.service';

//@Injectable() lets Angular know that a class can be used with the dependency injector.
//@Injectable() is not strictly required if the class has other Angular decorators on it or does not
//have any dependencies.
@Injectable({
  providedIn: 'root'
})

//CanActivate is an interface in Angular that a class can implement (for example the class below)
//to be a guard deciding if a route can be activated. If all guards return true , navigation continues.
export class AuthGuard implements CanActivate {

  //Runs immediately this class is instantiated/called
  constructor(private authService: AuthService, private router: Router) { }

  //This method takes in ActivatedRouteSnapshot and RouterStateSnapshot parameters and return either
  //an Observable or a Promise or a boolean
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const isAuthenticated = this.authService.isLoggedIn();
    if (isAuthenticated) {
      return true;
    } else {
      this.router.navigateByUrl('/login');
    }
    return true;
  }
}
