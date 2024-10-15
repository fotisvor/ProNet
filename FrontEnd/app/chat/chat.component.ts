import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from '../_services/message.service';
import { StorageService } from '../_services/storage.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  friendUsername: string | null = null; 
  currentUser: any;
  messageContent: string = '';
  messages: any[] = []; // Assuming you have a way to load/display existing messages

  constructor(
    private route: ActivatedRoute,
    private messageService: MessageService,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.friendUsername = this.route.snapshot.paramMap.get('friendUsername');
    this.currentUser = this.storageService.getUser();
    // Load existing messages (optional)
    this.loadMessages();
  }

  onSubmit(): void {
    if (this.messageContent.trim()) {
      if (this.friendUsername){
        this.messageService.sendMessage(this.friendUsername, this.messageContent).subscribe({
            next: (response) => {
              console.log('Message sent successfully:', response);
              this.loadMessages();
            },
            error: (err) => {
              console.error('Error sending message:', err);
            }
        });
      }
    }
  }

  loadMessages(): void {
    if (this.friendUsername){
      this.messageService.getMessages(this.friendUsername).subscribe({
          next: (response) => {
            console.log('Messages loaded successfully:', response);
            this.messages = response;
          },
          error: (err) => {
            console.error('Error loading messages:', err);
          }
      });
    }
  }
}
