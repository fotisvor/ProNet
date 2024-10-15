import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StorageService } from '../_services/storage.service';
import { UserService } from '../_services/user.service';
import { AuthService } from '../_services/auth.service';

@Component({
  selector: 'app-viewprofile',
  templateUrl: './viewprofile.component.html',
  styleUrls: ['./viewprofile.component.css']
})
export class ViewprofileComponent {
  Username: string | null = null; 
  currentUser: any;
  messageContent: string = '';
  user: any; 
  isEmailPublic: boolean = false;
  isNamePublic: boolean = false;
  isSurnamePublic: boolean = false;
  isPhonePublic: boolean = false;
  isPositionPublic: boolean = false;
  isCompanyPublic: boolean = false;
  isCvURLPublic: boolean = false;

  cvFile: File | null = null;         // Store the selected CV file
  imagePreview: string | null = null;
  cvTitle: string | null = null;
  
  constructor(
    private route: ActivatedRoute,
    private storageService: StorageService,
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.Username = this.route.snapshot.paramMap.get('Username');
    this.currentUser = this.storageService.getUser();
    this.loadUser();
  }

  loadUser(): void {
    if (this.Username){
      this.userService.getUser(this.Username).subscribe({
          next: (response) => {
            console.log('User loaded successfully:', response);
            this.user = response;
            console.log('User:', this.user);
            this.isPositionPublic = this.user.positionPublic;
            console.log('isPositionPublic:', this.isPositionPublic);
            this.isEmailPublic = this.user.emailPublic ;
            this.isNamePublic = this.user.namePublic ;
            this.isSurnamePublic = this.user.surnamePublic ;
            this.isPhonePublic = this.user.phonePublic ;
            this.isCompanyPublic = this.user.companyPublic ;
            
          },
          error: (err) => {
            console.error('Error loading messages:', err);
          }
      });
    }
  }

  viewCV(): void {
    this.authService.getCVFile().subscribe(
      (response) => {
        const fileURL = URL.createObjectURL(response);
        window.open(fileURL);
      },
      (error) => {
        console.error("Failed to download CV file", error);
      }
    );
  }

  downloadCV(): void {
    if(this.Username){
      this.authService.getApplicantCVFile(this.Username).subscribe(
        (response) => {
          const fileURL = URL.createObjectURL(response);
          const a = document.createElement('a');
          a.href = fileURL;
          a.download = this.cvTitle || 'cv.pdf';
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
        },
        (error) => {
          console.error("Failed to download CV file", error);
        }
      );
    }
  }



}
