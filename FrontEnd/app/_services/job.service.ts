import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';

const API_URL = 'https://localhost:8443/api/auth/';
@Injectable({
  providedIn: 'root'
})
export class JobService {

  constructor(private http: HttpClient, private storageService: StorageService) {}

  createJob(title: string, description: string, skills: string[]): Observable<any> {
    const token = this.storageService.getToken();
    
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    console.log('Title:', title);
    return this.http.post('https://localhost:8443/api/auth/jobs/createJob', {title,description,skills}, { headers: headers});
  }

  getJobs(): Observable<any> {  
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.get('https://localhost:8443/api/auth/jobs', { headers: headers });
  }

  getUserJobs(): Observable<any> {  
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.get('https://localhost:8443/api/auth/jobs/user_jobs', { headers: headers });
  }

  applyJob(id: number): Observable<any> {
    const token = this.storageService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
    console.log('headers', headers);  
    return this.http.post('https://localhost:8443/api/auth/jobs/apply/',id, { headers: headers });
  }

  getRecommendedAds(): Observable<any> {
    const token = this.storageService.getToken();
  
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization',  `Bearer ${token}`);
    }
  
    return this.http.get('https://localhost:8443/api/auth/recommended', { headers: headers });
  }


}
