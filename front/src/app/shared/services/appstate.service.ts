import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppstateService {

  $loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  $userInfo: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor() { }

  setLoggedIn(loggedIn: boolean) {
    this.$loggedIn.next(loggedIn);
  }
  setUserInfo(userInfo: any) {
    this.$userInfo.next(userInfo);
  }
  getLoggedIn() {
    return this.$loggedIn.asObservable();
  }
  getUserInfo() {
    return this.$userInfo.asObservable();
  }
  clearUserInfo() {
    this.$userInfo.next(null);
  }
  clearLoggedIn() {
    this.$loggedIn.next(false);
  }
  setUserInfoAndLoggedIn(userInfo: any, loggedIn: boolean) {
    this.$userInfo.next(userInfo);
    this.$loggedIn.next(loggedIn);
  }
}
