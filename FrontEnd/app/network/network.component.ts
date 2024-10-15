import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { AuthService } from '../_services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit {
  searchText: string = ''; // Holds the search query
  friends: any[] = []; // Connected professionals
  searchResults: any[] = []; // Search results from users
  similarSkillsUsers: any[] = []; // Users with similar skills

  constructor(private router: Router, private userService: UserService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadFriends();
  }

  // Load friends when component initializes
  loadFriends(): void {
    this.userService.getFriends().subscribe({
      next: (response: any[]) => {
        this.friends = response;
        this.friends.forEach(friend => this.loadProfileImage(friend)); // Load profile images for friends
      },
      error: (err) => {
        console.error('Error loading friends:', err);
      }
    });
  }

  // Search for users based on the search query
  onSearch(): void {
    if (this.searchText) {
      this.userService.getAllUsers().subscribe({
        next: (response: any[]) => {
          console.log('Search results:', response);
          // Filter users based on the search query
          this.searchResults = response.filter((user: any) => 
            user.username.toLowerCase().includes(this.searchText.toLowerCase())
          );
          // Load profile images for the search results
          this.searchResults.forEach(user => this.loadProfileImage(user));
        },
        error: (err) => {
          console.error('Error searching users:', err);
        }
      });
    } else {
      this.searchResults = [];
    }
  }

  // Load the profile image of the user
  loadProfileImage(user: any): void {
    this.authService.getFriendProfileImage(user.username).subscribe(
      (response) => {
        const reader = new FileReader();
        reader.readAsDataURL(response);  // assuming response is a Blob
        reader.onloadend = () => {
          user.profilePhotoUrl = reader.result as string; // Set profile image for each user
        };
      },
      (error) => {
        console.error("Failed to load profile image", error);
        user.profilePhotoUrl = 'assets/images/default_profile.jpg'; // Fallback image if error occurs
      } 
    );
  }

  // Start a chat with the selected user
  startChat(username: string): void {
    console.log(`Starting chat with ${username}`);
    this.router.navigate(['/chat', username]);
    // Add navigation or functionality to start the chat
  }
  isFriend(username: string): boolean {
    return this.friends.some(friend => friend.username === username);
  }
  // View the profile of the selected user
  viewProfile(username: string): void {
    console.log(`Viewing profile of ${username}`);
    this.router.navigate(['/viewprofile', username]);
  }

  sendFriendRequest(username: string): void {
    this.userService.sendFriendRequest(username).subscribe({
      next: (response) => {
        console.log('Friend request sent successfully:', response);
        // Optionally, you can update the UI to reflect the friend request status
      },
      error: (err) => {
        console.error('Error sending friend request:', err);
      }
    });
  }
}