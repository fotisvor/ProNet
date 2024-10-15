import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';

const API_URL = 'https://localhost:8443/api/auth/';
const USER_URL = 'https://localhost:8443/api/auth/originaluser';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient, private storageService: StorageService) {}

  getPublicContent(): Observable<any> {
    return this.http.get(API_URL + 'all', { responseType: 'text' });
  }

  getUserBoard(): Observable<any> {
    const token = this.storageService.getToken();
  
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.get('https://localhost:8443/api/test/user', { headers: headers, responseType: 'text' });
  }

  createPost(content: string, file: File | null ): Observable<any> {
    const token = this.storageService.getToken();
    const username = this.storageService.getUser().username;

    // Create a FormData object
    const formData: FormData = new FormData();
    formData.append('post', content);
    formData.append('username', username);

    // Append the file if one was selected
    if (file) {
      formData.append('file', file, file.name);
    }
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.post('https://localhost:8443/api/auth/createPost', formData, { headers: headers});
  }
  
  getModeratorBoard(): Observable<any> {
    return this.http.get(API_URL + 'mod', { responseType: 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(API_URL + 'admin', { responseType: 'text' });
  }

  getAllUsers(): Observable<any> {  
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.get('https://localhost:8443/api/auth/allUsers', { headers: headers });
  }
  sendFriendRequest(username: string): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.post(API_URL + 'sendFriendRequest',username, { headers: headers });
  }

  getRequests(): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.get('https://localhost:8443/api/auth/Requests', { headers: headers });
  }

  acceptFriendRequest(id: number): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.post(API_URL + `acceptFriendRequest`,id, { headers: headers });
  }

  rejectFriendRequest(id: number): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.post(API_URL + `rejectFriendRequest`,id, { headers: headers });
  }

  getFriends(): Observable<any> { 
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.get('https://localhost:8443/api/auth/friends', { headers: headers });
  }

  getAllPosts(): Observable<any> {  
    return this.http.get('https://localhost:8443/api/auth/allImagePosts');
  }

  like(postId: number): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.post('https://localhost:8443/api/auth/addLike', null, {
      headers: headers,
      params: { id: postId.toString() }
  });
  }

  comment(postId: number, comment: string): Observable<any> {
    const token = this.storageService.getToken();
    const commentObj = {
      content: comment,
      id: postId
    };
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.post('https://localhost:8443/api/auth/addComment', commentObj, { headers: headers });
  }

  getUser(username: string): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.get('https://localhost:8443/api/auth/user/' + username, { headers: headers });
  }

  getOriginalUser(): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.get('https://localhost:8443/api/auth/originaluser', { headers: headers });
  }

  updateUserSkills(skills: any): Observable<any> {
    const token = this.storageService.getToken();
  
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    
    // Make sure the `skills` parameter is an array of objects.
    console.log('skillsWithObjects', skills);
    return this.http.post('https://localhost:8443/api/auth/addSkills', skills, { headers: headers });
  }
}
