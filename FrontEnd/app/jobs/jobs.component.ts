import { Component, OnInit} from '@angular/core';
import { UserService } from '../_services/user.service';
import { StorageService } from '../_services/storage.service';
import { JobService } from '../_services/job.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';





@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent  implements OnInit{
  isSuccessful = false;
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

  jobs: any[] = [];
  userJobs: any[] = [];
  form: any = {
    title: '',
    description: '',
    skills: [] // Initialize the skills array
  };
  constructor(private storageService: StorageService, private userService: UserService, private jobService: JobService, private http: HttpClient) { }

  ngOnInit(): void {  
    this.getUserJobs();
    this.jobService.getRecommendedAds().subscribe((result: any) => {
      this.jobs = result;
      if (this.jobs == null) {
        console.log('No Jobs:');
      }
      else {
        console.log('Jobs:', this.jobs);
      }
      },
    error => {
      console.log(error);
    }); 

    
  }


  onSubmit(): void {
    const {Title,Description,skills} = this.form;
    console.log('Title:', Title);
    console.log('Description:', Description);
    this.jobService.createJob(Title,Description,skills ).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;


      },
      error: err => {
        console.log(err);
      }
    });
  }

  applyJob(id:number): void {
    console.log('Applying for job:', id);
    this.jobService.applyJob(id).subscribe({
      next: (response) => {
        console.log('Job applied successfully:', response);
       
      },
      error: (err) => {
        console.log('Error applying for job:', err);
      }
    });
  }

  getSkillNames(job: any): string {
    return job.skills ? job.skills.map((skill: any) => skill.name).join(', ') : '';
  }
  
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

  getUserJobs(): void {
    this.jobService.getUserJobs().subscribe({
      next: (response) => {
        console.log('User jobs:', response);
        this.userJobs = response;
      },
      error: (err) => {
        console.error('Error fetching user jobs:', err);
      }
    });
  }
  

}
