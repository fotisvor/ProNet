import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { StorageService } from '../_services/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-board-user',
  templateUrl: './board-user.component.html',
  styleUrls: ['./board-user.component.css']
})
export class BoardUserComponent implements OnInit {
  //content?: string;
  currentUser: any;
  content: any;
  form: any = {
    content: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  other_users: any[] = [];
  requests: any[] = [];
  friends: any[] = [];
  selectedFile: File | null = null;
  constructor(private storageService: StorageService, private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.currentUser = this.storageService.getUser();
    this.userService.getUserBoard().subscribe((result: any) => {
      this.content = result;
    });
    this.userService.getAllUsers().subscribe((result: any) => {
      
      this.other_users = result;
    });
    this.getRequests();
    this.getFriends();
  }

   // Handle file selection
  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0]) {
      this.selectedFile = fileInput.files[0];
    }
  }

  onSubmit(): void {
    const {content} = this.form;
    const formData: FormData = new FormData();
    formData.append('content', content);
    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.userService.createPost(content, this.selectedFile ).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    });
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

  getRequests(): void {
    this.userService.getRequests().subscribe({
      next: (response) => {
        console.log('Friend requests:', response);
        this.requests = response;
      },
      error: (err) => {
        console.error('Error fetching friend requests:', err);
      }
    });
  }

  acceptFriendRequest(id: number): void {
    this.userService.acceptFriendRequest(id).subscribe({
      next: (response) => {
        console.log('Friend request accepted successfully:', response);
        // Optionally, you can update the UI to reflect the friend request status
      },
      error: (err) => {
        console.error('Error accepting friend request:', err);
      }
    });
  }

  rejectFriendRequest(id: number): void {
    this.userService.rejectFriendRequest(id).subscribe({
      next: (response) => {
        console.log('Friend request rejected successfully:', response);
        // Optionally, you can update the UI to reflect the friend request status
      },
      error: (err) => {
        console.error('Error rejecting friend request:', err);
      }
    });
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

  Jobs(): void {
    this.router.navigate(['/jobs']);
  }
   
}
