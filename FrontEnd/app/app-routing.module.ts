import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatsComponent } from './chats/chats.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { BoardUserComponent } from './board-user/board-user.component';
import { BoardModeratorComponent } from './board-moderator/board-moderator.component';
import { BoardAdminComponent } from './board-admin/board-admin.component';
import { ChatComponent } from './chat/chat.component';
import { JobsComponent } from './jobs/jobs.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { NetworkComponent } from './network/network.component';
import { ViewprofileComponent } from './viewprofile/viewprofile.component';
import { SettingsComponent } from './settings/settings.component';

const routes: Routes = [
  
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'user', component: BoardUserComponent },
  { path: 'chat/:friendUsername', component: ChatComponent },
  { path: 'viewprofile/:Username', component: ViewprofileComponent },
  { path: 'mod', component: BoardModeratorComponent },
  { path: 'admin', component: BoardAdminComponent },
  { path: 'jobs', component: JobsComponent },
  { path: 'notifications', component: NotificationsComponent },
  { path: 'admin/users/:id', component: UserDetailsComponent },
  { path: 'network', component: NetworkComponent },
  { path: 'chats', component: ChatsComponent },
  { path: 'settings', component: SettingsComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
