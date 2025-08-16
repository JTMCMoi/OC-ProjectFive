import { Location } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
@Input() sidenav!: MatSidenav;

  toggleSidenav() {
    this.sidenav.toggle();
  }
 constructor(private location:Location) { }

  ngOnInit(): void {
  }
  goBack() {
    this.location.back();
  }
 
}
