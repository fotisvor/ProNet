import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  originalUser: any; // Store the fetched user data
  editMode: { [key: string]: boolean } = {}; // Track which fields are in edit mode
  newUsername: string = '';
  newPosition: string = '';
  newCompany: string = '';
  newPhone: string = '';
  newPositionbool: string = '';
  newPhonebool: string = '';
  newCompanybool: string = '';  
  newCvURLbool: string = '';
  newEmailbool: string = '';  
  newNamebool: string = '';
  newSurnamebool: string = '';  

  newRoles: string[] = [];
  newName: string = '';
  newSurname: string = '';
  passwordError: boolean = false; 
  profileImage: File | null = null;   // Store the selected profile image
  cvFile: File | null = null;         // Store the selected CV file
  imagePreview: string | null = null;
  cvTitle: string | null = null;
  
  isUsernamePublic: boolean = false;
  isEmailPublic: boolean = false;
  isPositionPublic: boolean = false;
  isCompanyPublic: boolean = false;
  isPhonePublic: boolean = false;
  isProfilePhotoUrlPublic: boolean = false;
  isCvURLPublic: boolean = false;
  isNamePublic: boolean = false;
  isSurnamePublic: boolean = false;

  // Skill selection
  skills: string[] = [
    'Java', 'Spring Boot', 'Angular', 'React', 'Node.js', 'Python', 'Django', 'Flask', 
    'Machine Learning', 'Data Science', 'DevOps', 'Kubernetes', 'Docker', 'AWS', 
    'Azure', 'GCP', 'SQL', 'NoSQL', 'MongoDB', 'PostgreSQL', 'Redis', 'RabbitMQ',
    'C++', 'C#', 'Ruby', 'Ruby on Rails', 'PHP', 'Laravel', 'Vue.js', 'Svelte',
    'Next.js', 'Nuxt.js', 'Express.js', 'Jenkins', 'Ansible', 'Terraform',
    'GraphQL', 'RESTful APIs', 'SOAP', 'Microservices', 'Blockchain', 'Solidity',
    'TensorFlow', 'PyTorch', 'OpenCV', 'Cybersecurity', 'Penetration Testing',
    'Network Security', 'System Administration', 'Linux', 'Unix', 'Windows Server',
    'Agile Methodologies', 'Scrum', 'JIRA', 'Confluence', 'Salesforce', 'SAP',
    'UI/UX Design', 'Figma', 'Sketch', 'Adobe XD', 'Bootstrap', 'Tailwind CSS',
    'Material UI', 'SASS', 'LESS', 'Webpack', 'Babel', 'Gulp', 'Grunt',
    'Mobile Development', 'Android', 'iOS', 'React Native', 'Flutter',
    'Unity', 'Unreal Engine', 'Game Development', 'Artificial Intelligence',
    'Natural Language Processing', 'Computer Vision', 'Robotics', 'Quantum Computing'
  ];
  
  form: any = {
    skills: []  // This will hold selected skills
  };



  constructor(
    private storageService: StorageService, 
    private authService: AuthService, 
    private changeDetectorRef: ChangeDetectorRef,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    // Fetch the user data from the service and initialize originalUser
    this.originalUser = this.storageService.getUser();
    setTimeout(() => {
    // Set public/private fields based on the user's current settings
      this.isEmailPublic = this.originalUser.emailPublic;
      
      this.isPositionPublic = this.originalUser.positionPublic;


      this.isCompanyPublic = this.originalUser.companyPublic;
      this.isCvURLPublic = this.originalUser.cvpublic;
      this.isPhonePublic = this.originalUser.phonePublic;
      this.isNamePublic = this.originalUser.namePublic;
      this.isSurnamePublic = this.originalUser.surnamePublic;
    }, 1000);
    console.log('isPositionPublic:', this.isPositionPublic);
    this.changeDetectorRef.detectChanges();
    
    this.authService.getProfileImage().subscribe(
      (response) => {
        const reader = new FileReader();
        reader.readAsDataURL(response);  // assuming response is a Blob
        reader.onloadend = () => {
          this.imagePreview = reader.result as string; // Set the image preview
        };
      },
      (error) => {
        console.error("Failed to load profile image", error);
        this.imagePreview = 'assets/images/default_profile.jpg'; // Fallback image
      }
    );
    
    this.authService.getCVFile().subscribe(
      (response) => {
        if (response.size > 0) {
          this.cvTitle = this.originalUser.username + "_cv.pdf"; // Set default file name
        } else {
          this.cvTitle = "No CV "; // Set default file name
          console.log('No CV found for the user');
        }
      },
      (error) => {
        console.error("Failed to download CV file", error);
      }
    );
    this.userService.getUser(this.originalUser.username).subscribe(
      (userData) => {
        // Initialize form values with the fetched data
        this.newName = userData.name;
        this.newSurname = userData.surname;
        this.newPosition = userData.position;
        this.newCompany = userData.company;
        this.newPhone = userData.phone;
        
        // Set public/private fields based on the user's current settings
        this.isEmailPublic = userData.EmailPublic;
        this.isPositionPublic = userData.PositionPublic;
        this.isCompanyPublic = userData.CompanyPublic;
        this.isPhonePublic = userData.PhonePublic;
        this.isCvURLPublic = userData.CvURLPublic;
        this.isNamePublic = userData.NamePublic;
        this.isSurnamePublic = userData.SurnamePublic;
      },
      (error) => {
        console.error("Error fetching user details", error);
      }
    );
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
    this.authService.getCVFile().subscribe(
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

  toggleEdit(field: string): void {
    this.editMode[field] = !this.editMode[field];
    if (this.editMode[field]) {
      switch (field) {
        case 'username':
          this.newUsername = this.originalUser.username;
          break;
        case 'position':
          this.newPosition = this.originalUser.position;
          break;
        case 'company':
          this.newCompany = this.originalUser.company;
          break;
        case 'phone':
          this.newPhone = this.originalUser.phone;
          break;
        case 'roles':
          this.newRoles = [...this.originalUser.roles]; // Clone the roles array
          break;
      }
    }
  }

  onProfileImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.profileImage = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  onCvFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.cvFile = file;
    }
  }

  saveEdit(): void {
    const updatedData: FormData = new FormData();
    // Add each field to the FormData if it has changed or exists
    updatedData.append('username', this.originalUser.username);
    updatedData.append('name', this.newName );
    updatedData.append('surname', this.newSurname );
    updatedData.append('email', this.originalUser.email);
    updatedData.append('position', this.newPosition );
    updatedData.append('company', this.newCompany );
    updatedData.append('phone', this.newPhone );
    
    // Handle roles update if modified
    if (this.newRoles.length) {
      updatedData.append('roles', JSON.stringify(this.newRoles));
    }

    if (this.profileImage) {
      updatedData.append('file', this.profileImage);
    }
    if (this.cvFile) {
      updatedData.append('csvFile', this.cvFile);
    }

    // Append public/private visibility fields
    if(this.isEmailPublic==true){
      this.newEmailbool="1";
      updatedData.append('EmailPublic', this.newEmailbool.toString());
      console.log('EmailPublic:', this.isPositionPublic);
      console.log('EmailPublic:', updatedData.get('EmailPublic'));
    }
    else{
      this.newEmailbool="0";
      updatedData.append('EmailPublic', this.newEmailbool.toString());
    }

    if(this.isPositionPublic==true){
      this.newPositionbool="1";
      updatedData.append('PositionPublic', this.newPositionbool.toString());
      console.log('isPositionPublic:', this.isPositionPublic);
      console.log('isPositionPublic:', updatedData.get('isPositionPublic'));
    }
    else{
      this.newPositionbool="0";
      updatedData.append('PositionPublic', this.newPositionbool.toString());
    }
    //updatedData.append('isPositionPublic', this.isPositionPublic.toString() );
    if(this.isCompanyPublic==true){
      this.newCompanybool="1";
      updatedData.append('CompanyPublic', this.newCompanybool.toString());
    }
    else{
      this.newCompanybool="0";
      updatedData.append('CompanyPublic', this.newCompanybool.toString());
    }

    if(this.isPhonePublic==true){
      this.newPhonebool="1";
      updatedData.append('PhonePublic', this.newPhonebool.toString());
    }
    else{
      this.newPhonebool="0";
      updatedData.append('PhonePublic', this.newPhonebool.toString());
    }

    if(this.isNamePublic==true){
      this.newNamebool="1";
      updatedData.append('NamePublic', this.newNamebool.toString());
    }
    else{
      this.newNamebool="0";
      updatedData.append('NamePublic', this.newNamebool.toString());
    }

    if(this.isSurnamePublic==true){
      this.newSurnamebool="1";
      updatedData.append('SurnamePublic', this.newSurnamebool.toString());
    }
    else{
      this.newSurnamebool="0";
      updatedData.append('SurnamePublic', this.newSurnamebool.toString());
    }

    if(this.isCvURLPublic==true){
      this.newCvURLbool="1";
      updatedData.append('CVPublic', this.newCvURLbool.toString());
    }
    else{
      this.newCvURLbool="0";
      updatedData.append('CVPublic', this.newCvURLbool.toString());
    }
  
    console.log('updatedData pos public:', updatedData.get('PositionPublic'));
    console.log('updatedData email public:', updatedData.get('EmailPublic'));
    console.log('updatedData comp public:', updatedData.get('CompanyPublic'));
    console.log('updatedData phone public:', updatedData.get('PhonePublic'));

    // Send the updated fields to the backend
    this.authService.updateUser1(updatedData).subscribe(
      (response) => {
        console.log('User updated successfully:', response);
      },
      (error) => {
        console.error('Error updating user:', error);
      }
    );
  }

   // Skill selection change handler
   onSkillChange(event: any): void {
    const skill = event.target.value;
    if (event.target.checked) {
      this.form.skills.push(skill); // Add skill if checked
    } else {
      const index = this.form.skills.indexOf(skill);
      if (index > -1) {
        this.form.skills.splice(index, 1); // Remove skill if unchecked
      }
    }
    console.log('Skills:', this.form.skills);
  }
  // Save skills function (dedicated request for updating skills)
  saveSkills(): void {
    const selectedSkills = this.form.skills.map((skill: string) => ({ name: skill }));

    // Send skill updates to the backend
    this.userService.updateUserSkills(selectedSkills).subscribe(
      (response) => {
        console.log('Skills updated successfully:', response);
      },
      (error) => {
        console.error('Error updating skills:', error);
      }
    );
  }
}