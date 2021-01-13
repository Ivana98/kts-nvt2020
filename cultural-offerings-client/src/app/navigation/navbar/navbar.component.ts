import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/security/auth-service/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(private authService: AuthService) { }

  userEmail : string = "";

  ngOnInit(): void {
    this.userEmail = this.authService.getEmail();
  }

  isLoggedIn(): boolean{
    return this.authService.isLoggedIn();
  }

  logout() : void {
    this.userEmail = "";
    this.authService.logout();
  }

}
