
# ProNet: Professional Networking Platform - A LinkedIn Clone

**Developed by:**
- Fotios Vorlou (1115201900026)
- Konstantinos-Achilleios Ritsos (1115201900164)

## Table of Contents
1. Introduction
2. Design Decisions and Assumptions
    - 2.1 Backend Architecture
    - 2.2 Database Design (MySQL)
    - 2.3 Frontend Architecture
    - 2.4 Key Design Decisions
    - 2.5 Assumptions
    - 2.6 Challenges and Decisions
3. Implementation Details
    - 3.1 User and Job Models
    - 3.2 Skill Management
    - 3.3 User-Job Matching Algorithms
    - 3.4 Posts and Notifications
4. Application Security
    - 4.1 JWT for Authentication and Authorization
    - 4.2 SSL for Data Encryption
5. Installation and Execution Guide
    - 5.1 Backend (Spring Boot) Installation
    - 5.2 Frontend (Angular) Installation
    - 5.3 User Registration and Login
    - 5.4 Application Usage
6. Future Improvements
7. Conclusion

---

## 1. Introduction
This project aims to develop a professional networking platform that allows users to create profiles, post job listings, apply for jobs, and connect with others based on their professional skills. Essentially, it is a simplified clone of LinkedIn, offering core features like user profiles, job listings, and skill-based matching between professionals and job opportunities.

The platform uses **Spring Boot** for the backend to handle data management and ensure system security. **MySQL** serves as the database for storing user profiles, job listings, skills, and other related data. The **Angular** framework is used for the frontend, providing users with a dynamic interface to interact with the platform.

### Key Features:
- **User Profiles:** Users can create personal profiles that display information like their username, email, skills, and professional background.
- **Job Listings:** Users can post job listings, apply for jobs, and view applications submitted by other professionals.
- **Skill-Based Matching:** The system matches jobs to users based on their declared skills.
- **Connections and Friendships:** Users can send and accept connection requests to build professional networks.

---

## 2. Design Decisions and Assumptions

### 2.1 Backend Architecture
The backend is implemented using **Spring Boot**, following an MVC (Model-View-Controller) architecture. Key components include:
- **Entity-Relationship Mapping:** **JPA** (Java Persistence API) is used to map entities like users, skills, and job postings to MySQL tables.
- **JWT Authentication:** **JSON Web Tokens (JWT)** are used for secure user authentication and authorization.

### 2.2 Database Design (MySQL)
The database contains tables for users, job listings, skills, and friendships. Relationships like **Many-to-Many** between users and skills, and **Many-to-One** between jobs and users are clearly defined.

### 2.3 Frontend Architecture
The frontend is built with **Angular**, using a component-based architecture for modularity and maintainability. Reactive forms and **HTTP client integration** are used to manage data input and backend communication.

### 2.4 Key Design Decisions
- **Skill-Based Matching:** Job recommendations are personalized for users based on their skills.
- **Notification System:** Notifications inform users about activities like job applications and new connection requests.

### 2.5 Assumptions
- **Unique Usernames and Emails:** Each user must have a unique username and email.
- **Verified Users:** Only authenticated users can perform job-related actions like posting or applying for jobs.

### 2.6 Challenges and Decisions
Managing JWT authentication and handling file uploads were among the technical challenges. Decisions were made to store profile images on the server for simplicity.

---

## 3. Implementation Details

### 3.1 User and Job Models
User and job models were designed using Spring JPA to map relational database tables. Users can have multiple skills, and job listings include attributes like employer and job title.

### 3.2 Skill Management
Users can dynamically add and manage skills. A **SkillRepository** handles the addition, deletion, and retrieval of skills.

### 3.3 User-Job Matching Algorithms
The matching algorithm ranks users for job positions based on their skill sets. The more overlapping skills a user has with the job requirements, the higher they are ranked.

### 3.4 Posts and Notifications
Users can post job listings and receive notifications related to their job posts, such as applications, likes, or comments.

---

## 4. Application Security

### 4.1 JWT Authentication
JWT tokens are used for user authentication. Upon successful login, a token is issued, stored in the browser’s local storage, and sent with every subsequent request.

### 4.2 SSL Encryption
The platform uses **SSL** for data encryption between the frontend and backend, ensuring that sensitive user information, such as login credentials, is securely transmitted.

---

## 5. Installation and Execution Guide

### Prerequisites:
- **JDK** (version 8 or later)
- **Maven**
- **Node.js** and **npm**
- **MySQL**

### 5.1 Backend (Spring Boot) Installation
1. Clone the repository and navigate to the backend folder:
   ```bash
   git clone https://github.com/your-repository-url.git
   cd backend
   ```
2. Install the project dependencies using Maven:
   ```bash
   mvn clean install
   ```
3. Configure MySQL in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

### 5.2 Frontend (Angular) Installation
1. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Angular development server:
   ```bash
   ng serve --ssl
   ```

### 5.3 User Registration and Login
- **Register:** Navigate to `https://localhost:4200/register` to create a new user account.
- **Login:** Log in at `https://localhost:4200/login`.

### 5.4 Application Usage
After logging in, you can:
- Edit your profile.
- Create and manage job postings.
- Connect with other professionals.

---

## 6. Future Improvements
- **Recommendation System:** Enhancing the matching algorithm with machine learning.
- **Scalability:** Optimizing backend queries and possibly transitioning to a distributed architecture for better performance.
- **Improved User Experience:** Real-time chat features and more interactive feeds.
- **Security Enhancements:** Introducing two-factor authentication (2FA) and more granular access control.

---

## 7. Conclusion
This project successfully developed a professional networking platform with features similar to LinkedIn. The journey highlighted the importance of design, error handling, and adaptability in building a full-stack application. The platform is secure, scalable, and ready for further enhancement.
