import { Injectable } from '@angular/core';

const USER_KEY = 'auth-user';
const TOKEN_KEY = 'auth-token';


@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private IMAGE_KEY_PREFIX = 'user-image-'; // Key prefix for image storage

  constructor() {}

  clean(): void {
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
    console.log('token saved', token);
  }

  public getToken(): string | null {
    console.log('token get', window.sessionStorage.getItem(TOKEN_KEY));
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    console.log('user get', user);
    if (user) {
      return JSON.parse(user);
    }

    return null;
  }

  // Save the user's profile image (Base64)
  saveImage(username: string, image: string | null): void {
    if (image) {
      window.localStorage.setItem(this.IMAGE_KEY_PREFIX + username, image);
    }
  }

  // Retrieve the user's profile image (Base64)
  getImage(username: string): string | null {
    return window.localStorage.getItem(this.IMAGE_KEY_PREFIX + username);
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return true;
    }
    return false;
  }
}
