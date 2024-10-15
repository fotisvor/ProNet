import { Component } from '@angular/core';
import { StorageService } from '../_services/storage.service';
import { AuthService } from '../_services/auth.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {

  originalUser: any; // Store the fetched user data
  editMode: { [key: string]: boolean } = {}; // Track which fields are in edit mode
  newUsername: string = '';
  newEmail: string = '';
  newPosition: string = '';
  newCompany: string = '';
  newPassword: string = ''; 
  confirmPassword: string = ''; 
  passwordError: boolean = false; 
  profileImage: File | null = null;   // Store the selected profile image
  cvFile: File | null = null;         // Store the selected CV file
  imagePreview: string | null = null;
  cvTitle:string|null=null;
  errorMessage = '';
  constructor(private storageService: StorageService, private authService: AuthService) {}

  ngOnInit(): void {
    // Fetch the user data from the service and initialize originalUser
    this.originalUser = this.storageService.getUser();

  }

  toggleEdit(field: string): void {
    this.editMode[field] = !this.editMode[field];
    if (this.editMode[field]) {
      switch (field) {
        case 'email':
          this.newEmail = this.originalUser.email;
          break;
        
      }
    }
  }

  

  saveEdit(): void {
    const updatedData: FormData = new FormData();

    // Add each field to the FormData if it has changed or exists
    updatedData.append('username', this.newUsername || this.originalUser.username);
    updatedData.append('email', this.newEmail || this.originalUser.email);
    updatedData.append('position', this.newPosition || this.originalUser.position);
    updatedData.append('company', this.newCompany || this.originalUser.company);
    
    if (this.newPassword && this.newPassword === this.confirmPassword) {
      updatedData.append('password', this.newPassword);
    }
    else{
      this.errorMessage = 'Passwords not matching!';
    }

    if (this.profileImage) {
      updatedData.append('file', this.profileImage);
    }

    if (this.cvFile) {
      updatedData.append('csvFile', this.cvFile);
    }

    console.log('Updated csv:', updatedData.get('csvFile'));
    console.log('Updated phot:', updatedData.get('file'));

    // Send the updated fields to the backend
    this.authService.updateUser1(updatedData).subscribe(
      response => {
        console.log('User updated successfully:', response);
      },
      error => {
        console.error('Error updating user:', error);
      }
    );
  }
}
