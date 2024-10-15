import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';

const AUTH_API = 'https://localhost:8443/api/auth/';

const httpOptions = {
  headers: new HttpHeaders()
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private storageService: StorageService) {}

  saveToken(token: string): void {
    this.storageService.saveToken(token);
  }

  getToken(): string | null {
    return this.storageService.getToken();
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signin',
      {
        username,
        password,
      },
      httpOptions 
    );
  }

  register(username: string,name: string, surname: string, email: string, phone: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signup',
      {
        username, name, surname, email, phone, password
      },
      httpOptions
    );
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + 'signout', { }, httpOptions);
  }

  updateUser(username: string, email: string, selectedFile: File | null): Observable<any> {
    const token = this.storageService.getToken();
   
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
     // Create FormData to send the username, email, and file
     const formData = new FormData();
     formData.append('username', username);
     formData.append('email', email);
     
     if (selectedFile) {
       formData.append('file', selectedFile); // Append the file only if it exists
     }
     else{
      console.log('No file selected');
     }
     console.log('formData', formData.get('file'));
    return this.http.post(AUTH_API + 'updateUser', formData, { headers });
  }

  updateUser1(updatedData: FormData): Observable<any> {
    const token = this.storageService.getToken();
    console.log('data :', updatedData.get('username'));
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    // Send the updated fields to the backend
    return this.http.post(AUTH_API + 'updateUser1', updatedData, { headers });
  }

  getProfileImage(): Observable<Blob> {
    const token = this.storageService.getToken();
    
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.get(AUTH_API + 'profileImage', { headers, responseType: 'blob' });
  } 

  getFriendProfileImage(username: String): Observable<Blob> {
    const token = this.storageService.getToken();

    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    console.log('username :', username);
    return this.http.get(AUTH_API + 'friendProfileImage/'+`${username}`, { headers, responseType: 'blob' });
  }

  getCVFile(): Observable<Blob> {
    const token = this.storageService.getToken();
    
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.get(AUTH_API + `cvFile`, { headers, responseType: 'blob' });
  }

  getApplicantCVFile(username: String): Observable<Blob> {
    const token = this.storageService.getToken();
    
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.get(AUTH_API + 'cvFile/'+`${username}`, { headers, responseType: 'blob' });
  }
}