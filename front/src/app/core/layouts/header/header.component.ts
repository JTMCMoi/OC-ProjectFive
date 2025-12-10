import { Component, Input, HostListener, OnChanges, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnChanges {

  @Input() isLogged = false;
  isMobile = false;
  showMenu = false;

  constructor(private router: Router, private auth: AuthService) {
    this.checkMobile();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Si on se déconnecte → fermer le menu mobile
    if (changes['isLogged'] && !this.isLogged) {
      this.showMenu = false;
    }
  }

  @HostListener('window:resize')
  onResize() {
    this.checkMobile();
  }

  private checkMobile() {
    const previous = this.isMobile;
    this.isMobile = window.innerWidth <= 600;

    // Si on passe en desktop → fermer menu mobile
    if (previous !== this.isMobile && !this.isMobile) {
      this.showMenu = false;
    }
  }

  logout() {
    this.auth.logout();
    this.showMenu = false;
    this.router.navigate(['/auth/login']);
  }
}
