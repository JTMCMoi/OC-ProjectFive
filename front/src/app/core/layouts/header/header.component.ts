import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/services/auth.service';
import { User } from 'src/app/shared/models/user/user.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  isMobile = false;
  showMenu = false;
  currentUser: User | null = null;
  constructor(private router: Router, private auth: AuthService) {
    this.checkMobile();
    this.auth.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  @HostListener('window:resize')
  onResize() {
    this.checkMobile();
  }

  private checkMobile() {
    const previous = this.isMobile;
    this.isMobile = window.innerWidth <= 600;

    if (previous !== this.isMobile && !this.isMobile) {
      this.showMenu = false;
    }
  }

  logout() {
    this.auth.logout();
    this.showMenu = false;
    this.router.navigate(['/auth/login']);
  }

  hideHeader(): boolean {
    const hiddenRoutes = ['/', '/login', '/register'];
    return hiddenRoutes.includes(this.router.url);
  }

  get isLogged(): boolean {
    return !!this.currentUser;
  }
}
