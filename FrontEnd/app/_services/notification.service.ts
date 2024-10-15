// notification.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private baseUrl = 'https://localhost:8443/api/auth';

  constructor(private http: HttpClient, private storageService: StorageService) { }

  getNotifications(): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.get(`${this.baseUrl}/notifications`,{ headers: headers });
  }

  markAsRead(id: number): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.post(`${this.baseUrl}/notifications/markAsRead`, id,{ headers: headers } );
  }
}
