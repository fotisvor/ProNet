import { ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  form: any = {
    content: null
  };
  content?: string;
  posts: any[] = [];  
  isLoggedIn = false;
  isSuccessful = false;
  selectedFile: File | null = null;
  user: any;
  isLoginFailed = false;
  errorMessage = '';
  commentContent: { [key: number]: string } = {};
  friends: any[] = [];
  originalUser: any;  

  constructor(private ngZone: NgZone, private http: HttpClient, private userService: UserService,private router: Router, private authService: AuthService, private storageService: StorageService) { }

  ngOnInit(): void {
        this.getFriends();
        this.getPosts();
        this.isLoggedIn = this.storageService.isLoggedIn();
        this.user= this.storageService.getUser();
        
        this.originalUser = this.getOriginalUser();
        console.log('Original user:', this.originalUser);
        
  }

  getPosts(): void {  
    this.userService.getAllPosts().subscribe({
      next: (posts: any[]) => {
        this.posts = posts.map((postWithImage: any) => {
          const post = postWithImage.advertisment;  // Extract the Advertisment object
          if (postWithImage.imageBytes) {
            const reader = new FileReader();
            const imageBlob = new Blob([postWithImage.imageBytes], { type: 'image/jpeg' });

            reader.readAsDataURL(imageBlob);  // Convert Blob to base64 string
            reader.onloadend = ((post) => () => {
              this.ngZone.run(() => {
                post.imagePreview = reader.result as string;  // Set the base64 string as the image preview
              });
            })(post);  // Pass the current post to the closure
          } else {
            post.imagePreview = null; // No image available, set to null or fallback
          }
          return post;
        });

        console.log('Posts:', this.posts);
      },
      error: (err) => {
        console.error('Error getting posts:', err);
      }
    });
  }
  
  getOriginalUser(): any {
    return this.userService.getOriginalUser();
  }
  
  loadPostImage(post: any): void {
    const imageUrl = `https://localhost:8443/api/auth/getImage?id=${post.id}`;
    this.http.get(imageUrl, { responseType: 'blob' }).subscribe(
      (response: Blob) => {
        const reader = new FileReader();
        reader.readAsDataURL(response);  // Convert Blob to base64 string
        reader.onloadend = () => {
          post.imagePreview = reader.result as string;  // Set the base64 string as the image preview
        };
      },
      (error) => {
        console.error("Failed to load post image", error);
        post.imagePreview = 'assets/images/default_post_image.jpg'; // Fallback image
      }
    );
  }
  
  
  like(id: number): void {
    this.userService.like(id).subscribe({
      next: (response) => {
        console.log('Post liked successfully:', response);
        this.getPosts();
      },
      error: (err) => {
        console.error('Error liking post:', err);
      }
    });
  }
  comment(id: number, content: string): void {
    this.userService.comment(id, content).subscribe({
      next: (response) => {
        console.log('Comment posted successfully:', response);
        this.getPosts();
        this.commentContent[id] = ''; // Clear the input field

      },
      error: (err) => {
        console.error('Error posting comment:', err);
      }
    });
  }
 
  getFriends(): void {  
    this.userService.getFriends().subscribe({
      next: (response) => {
        console.log('Friends:', response);
        this.friends = response;
      },
      error: (err) => {
        console.error('Error fetching friends:', err);
      }
    });
  }
  StartChat(friendUsername: string): void {
    this.router.navigate(['/chat', friendUsername]);
  }

   // Handle file selection
   onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0]) {
      this.selectedFile = fileInput.files[0];
    }
  }

  onSubmit(): void {
    const {content} = this.form;
    const formData: FormData = new FormData();
    formData.append('content', content);
    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.userService.createPost(content, this.selectedFile ).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isLoginFailed = false;
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    });
  }
}
