import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {
  users: any[] = [];
  selectedUserIds: number[] = [];
  isLoading = false;

  constructor(private userService: UserService, private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<any[]>(`https://localhost:8443/api/auth/admin/users`).subscribe(data => {
      this.users = data;
    });
  }

  toggleUserSelection(userId: number): void {
    if (this.selectedUserIds.includes(userId)) {
      this.selectedUserIds = this.selectedUserIds.filter(id => id !== userId);
    } else {
      this.selectedUserIds.push(userId);
    }
  }

  exportSelectedUsers(format: string): void {
    this.isLoading = true;
    if (format === 'json') {
      this.exportUsersToJson(this.selectedUserIds).subscribe({
        next: (data) => {
          const blob = new Blob([JSON.stringify(data)], { type: 'application/json' });
          saveAs(blob, 'users.json');
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error exporting users:', err);
          this.isLoading = false;
        }
      });
    } else if (format === 'xml') {
      this.exportUsersToXml(this.selectedUserIds).subscribe({
        next: (data) => {
          const blob = new Blob([data], { type: 'application/xml' });
          saveAs(blob, 'users.xml');
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error exporting users:', err);
          this.isLoading = false;
        }
      });
    }
  }

  exportUsers(format: string): void {
    const url = format === 'json' ? `https://localhost:8443/api/auth/admin/users/export/json` : `https://localhost:8443/api/auth/admin/users/export/xml`;
    window.open(url, '_blank');
  }

  exportUsersToJson(userIds: number[]): Observable<any> {
    return this.http.post(`https://localhost:8443/api/auth/admin/users/export/json`, userIds);
  }

  exportUsersToXml(userIds: number[]): Observable<any> {
    return this.http.post(`https://localhost:8443/api/auth/admin/users/export/xml`, userIds, { responseType: 'text' });
  }
}
