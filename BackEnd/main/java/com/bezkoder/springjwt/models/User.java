package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    private String position;


    @Size(max = 50)
    private String name;


    @Size(max = 50)
    private String surname;


    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;


    private String company;
    private String profilePhotoUrl;

    private String cvURL;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Advertisment> posts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    @JsonIgnore
    private Set<User> friends = new HashSet<>();

    @ManyToMany(mappedBy = "friends", fetch = FetchType.LAZY)
    @JsonIgnore // Prevents infinite recursion
    private Set<User> friendsOf = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_skills",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY)
    private List<Conversation> conversations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    //Public-Private  flag
    private boolean isEmailPublic ;
    private boolean isPositionPublic ;
    private boolean isCompanyPublic ;
    private boolean isCVPublic ;
    private boolean isPhonePublic;

    private boolean isNamePublic;

    private boolean isSurnamePublic;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String email, String password, String name, String surname, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password);
    }
    public Set<Advertisment> getPosts() {
        return posts;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Set<User> getFriends(){
        return friends;
    }

    public Set<User> getFriendsOf(){
        return friendsOf;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setUser(this);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
        notification.setUser(null);
    }

    public String getPosition() {return position;}

    public void setPosition(String position) {this.position = position;}

    public Set<Skill> getSkills(){return skills;}

    public void setSkills(Set<Skill> skills) {this.skills = skills;}

    public String getCompany() {return company;}

    public void setCompany(String company) {this.company = company;}

    public String getCvURL() {return cvURL;}

    public void setCvURL(String cvURL) {this.cvURL = cvURL;}
    public boolean isEmailPublic() {
        return this.isEmailPublic;
    }

    public void setEmailPublic(String emailPublic) {
        String one = "1";
        if (emailPublic != null && emailPublic.equals(one)) {
            this.isEmailPublic = true;
        } else {
            // If emailPublic is null, handle it appropriately
            if (emailPublic == null) {
                System.out.println("emailPublic is null");
            } else {
                System.out.println(emailPublic);
            }
        }
    }

    public boolean isPositionPublic() {
        return this.isPositionPublic;
    }

    public void setPositionPublic(String positionPublic) {
        String one = "1";
        if (positionPublic != null && positionPublic.equals(one)) {
            this.isPositionPublic = true;
        } else {

            // If emailPublic is null, handle it appropriately
            if (positionPublic == null) {
                System.out.println("positionPublic is null");
            } else {
                System.out.println(positionPublic);
            }
        }
    }

    public boolean isPhonePublic() {
        return this.isPhonePublic;
    }
    public void setPhonePublic(String phonePublic) {
        String one = "1";
        if (phonePublic != null && phonePublic.equals(one)) {
            this.isPhonePublic = true;
        } else {

            // If emailPublic is null, handle it appropriately
            if (phonePublic == null) {
                System.out.println("PhonePublic is null");
            } else {
                System.out.println(phonePublic);
            }
        }
    }

    public boolean isNamePublic() {
        return this.isNamePublic;
    }
    public void setNamePublic(String namePublic) {
        String one = "1";
        if (namePublic != null && namePublic.equals(one)) {
            this.isNamePublic = true;
        } else {

            // If emailPublic is null, handle it appropriately
            if (namePublic == null) {
                System.out.println("NamePublic is null");
            } else {
                System.out.println(namePublic);
            }
        }
    }

    public boolean isSurnamePublic() {
        return this.isSurnamePublic;
    }
    public void setSurnamePublic(String SurnamePublic) {
        String one = "1";
        if (SurnamePublic != null && SurnamePublic.equals(one)) {
            this.isSurnamePublic = true;
        } else {

            // If emailPublic is null, handle it appropriately
            if (SurnamePublic == null) {
                System.out.println("SurnamePublic is null");
            } else {
                System.out.println(SurnamePublic);
            }
        }
    }

    public boolean isCompanyPublic() {
        return this.isCompanyPublic;
    }

    public void setCompanyPublic(String companyPublic) {
        String one = "1";
        if (companyPublic != null && companyPublic.equals(one)) {
            this.isCompanyPublic = true;
        } else {
            // If emailPublic is null, handle it appropriately
            if (companyPublic == null) {
                System.out.println("companyPublic is null");
            } else {
                System.out.println(companyPublic);
            }
        }
    }

    public boolean isCVPublic() {
        return this.isCVPublic;
    }

    public void setCVPublic(String cvPublic) {
        String one = "1";
        if (cvPublic != null && cvPublic.equals(one)) {
            this.isCVPublic = true;
        } else {
            // If emailPublic is null, handle it appropriately
            if (cvPublic == null) {
                System.out.println("cvPublic is null");
            } else {
                System.out.println(cvPublic);
            }
        }
    }
}
