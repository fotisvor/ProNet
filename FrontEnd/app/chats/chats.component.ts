import { Component } from '@angular/core';
import { UserService } from '../_services/user.service';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-chats',
  templateUrl: './chats.component.html',
  styleUrls: ['./chats.component.css']
})
export class ChatsComponent {
  isLoggedIn = false;
  isSuccessful = false;
  isLoginFailed = false;
  friends: any[] = [];
  originalUser: any;  
  errorMessage = '';

  constructor(private userService: UserService,private router: Router, private authService: AuthService, private storageService: StorageService) { }
  ngOnInit(): void {
    this.getFriends();
    this.isLoggedIn = this.storageService.isLoggedIn();
    this.originalUser = this.storageService.getUser();
}

  getFriends(): void {  
    this.userService.getFriends().subscribe({
      next: (response) => {
        console.log('Friends:', response);
        this.friends = response;
      },
      error: (err) => {
        console.error('Error fetching friends:', err);
      }
    });
  }
  StartChat(friendUsername: string): void {
    this.router.navigate(['/chat', friendUsername]);
  }
}
