import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent {
  user: any;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id');
    this.http.get<any>(`https://localhost:8443/api/auth/admin/users/${userId}`).subscribe(data => {
      this.user = data;
    });
  }
}
