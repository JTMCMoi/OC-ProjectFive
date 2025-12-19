import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthBootstrapService } from './core/auth/services/auth-bootstrap.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  constructor(public router: Router, public bootstrap: AuthBootstrapService) {
    this.bootstrap.init();
  }

  hideHeader(): boolean {
    const hiddenRoutes = ['/', '/login', '/register'];
    return hiddenRoutes.includes(this.router.url);
  }
}
