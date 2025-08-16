import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from './shared/services/auth.service';
import { AppstateService } from './shared/services/appstate.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'front';
  userAuthService = inject(AuthService);
  appstateService = inject(AppstateService);
  ngOnInit(): void {
    this.userAuthService.checkAuthStatus().subscribe();
  }
}
