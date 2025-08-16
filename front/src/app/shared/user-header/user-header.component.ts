import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AppstateService } from 'src/app/shared/services/appstate.service';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-user-header',
  templateUrl: './user-header.component.html',
  styleUrls: ['./user-header.component.scss']
})
export class UserHeaderComponent implements OnInit {

  @Output() menuToggle = new EventEmitter<void>();

  appstateService = inject(AppstateService);
  userAuthService = inject(AuthService);
  router = inject(Router);
  
  constructor() { }

  ngOnInit(): void {
  }

  toggleSidenav() {
    this.menuToggle.emit();
  }
  logOut(){
    this.userAuthService.logout().subscribe({
      next: (res) => {  
        this.appstateService.clearUserInfo();
        this.appstateService.clearLoggedIn();
        this.router.navigate(['']);
       },
      error: (err) => {
        console.error(err);
      }
    });
  }
}
