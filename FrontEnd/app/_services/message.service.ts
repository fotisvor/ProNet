import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private baseUrl = 'https://localhost:8443/api/auth/messages';

  constructor(private http: HttpClient, private storageService: StorageService) { }

  sendMessage(toUser: string, message: string): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  

    return this.http.post(this.baseUrl, {toUser, message} , { headers: headers });
  }

  getMessages(fromUser: string): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  

    return this.http.get(this.baseUrl + `/${fromUser}`, { headers: headers });
  }
}
