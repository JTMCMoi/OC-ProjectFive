import { Component } from '@angular/core';
import { TokenService } from './core/auth/services/token.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  constructor(public token: TokenService, public router: Router) {}

   hideHeader(): boolean {
    const hiddenRoutes = ['/', '/login', '/register'];
    return hiddenRoutes.includes(this.router.url);
  }
}
