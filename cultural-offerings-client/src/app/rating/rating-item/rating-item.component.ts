import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Rating } from '../../../app/core/model/rating';
import { User } from '../../../app/core/model/user';
import { AuthService } from '../../../app/core/services/security/auth-service/auth.service';
import { UserService } from '../../../app/core/services/user/user.service';

@Component({
  selector: 'app-rating-item',
  templateUrl: './rating-item.component.html',
  styleUrls: ['./rating-item.component.scss']
})
export class RatingItemComponent implements OnInit {

  @Input()
  rating: Rating;

  deleteStarted = false;

  user: User;

  @Output()
  removeRatingEvent: EventEmitter<Rating> = new EventEmitter<Rating>();

  constructor(
    public userService: UserService,
    public authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.fetchUser();
  }

  isUserAdmin(): boolean{
    return this.authService.getUserRole() == 'ADMIN';
  }

  async fetchUser(): Promise<void>{
    if (this.rating) {
      this.user = await this.userService.getOne(this.rating.userId).toPromise();
    }
  }

  delete(): void{
    this.deleteStarted = true;
    this.removeRatingEvent.emit(this.rating);
  }
}
