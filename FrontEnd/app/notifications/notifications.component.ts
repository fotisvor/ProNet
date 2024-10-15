// notifications.component.ts
import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../_services/notification.service';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html'
})
export class NotificationsComponent implements OnInit {
  notifications: any[] = [];
  requests: any[] = [];


  constructor(private notificationService: NotificationService, private userService: UserService) { }

  ngOnInit() {
    this.loadNotifications();
    this.getRequests();
  }

  loadNotifications() {
    this.notificationService.getNotifications().subscribe(
      data => {
        this.notifications = data;
      },
      err => {
        console.error(err);
      }
    );
  }

  markAsRead(id: number) {
    this.notificationService.markAsRead(id).subscribe(
      () => {
        this.loadNotifications();
      },
      err => {
        console.error(err);
      }
    );
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
}
