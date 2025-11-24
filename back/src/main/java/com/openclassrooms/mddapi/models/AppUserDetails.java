// package com.openclassrooms.mddapi.models;

// import java.time.LocalDateTime;
// import java.util.Collection;
// import java.util.List;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

// /**
//  * Implémentation de {@link UserDetails} adaptée à l'entité {@link UserEntity}.
//  * 
//  * Cette classe est utilisée par Spring Security pour représenter un utilisateur authentifié.
//  */
// public class AppUserDetails implements UserDetails {

//     private final UserEntity user;

//     public AppUserDetails(UserEntity user) {
//         this.user = user;
//     }

//     public UserEntity getUser() {
//         return this.user;
//     }


//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//          // TODO: Retrieve roles from the database
//         // Future improvement: Use a dedicated role table
//         return List.of(() -> "ROLE_USER");
//     }

//     @Override
//     public String getPassword() {
//         return user.getPassword();
//     }

//     public String getEmail() {
//         return user.getEmail();
//     }

//     public String getUserName() {
//         return user.getEmail();
//     }

    
//     @Override
//     public boolean isAccountNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isAccountNonLocked() {
//         return true;
//     }

//     @Override
//     public boolean isCredentialsNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isEnabled() {
//         return true;
//     }

//     public Long getId() {
//         return user.getId();
//     }

//     public LocalDateTime getCreatedAt() {
//         return user.getCreatedAt();
//     }

//     public LocalDateTime getUpdatedAt() {
//         return user.getUpdatedAt();
//     }

//     @Override
//     public String toString() {
//         return "AppUserDetails{" +
//                 "id=" + getId() +
//                 ", username='" + getUserName() + '\'' +
//                 ", email='" + getEmail() + '\'' +
//                 ", createdAt=" + getCreatedAt() +
//                 ", updatedAt=" + getUpdatedAt() +
//                 '}';
//     }

//     @Override
//     public String getUsername() {
//          return user.getEmail();
//     }

// }
package com.openclassrooms.mddapi.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails implements UserDetails {

    private final UserEntity user;
    private final String loginIdentifier; // email ou username utilisé pour se connecter

    public AppUserDetails(UserEntity user, String loginIdentifier) {
        this.user = user;
        this.loginIdentifier = loginIdentifier;
    }

    public UserEntity getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return loginIdentifier; // retourne l'identifiant utilisé (email ou username)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return user.getId();
    }

    public String getUserName() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public LocalDateTime getCreatedAt() {
        return user.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return user.getUpdatedAt();
    }

    @Override
    public String toString() {
        return "AppUserDetails{" +
                "id=" + getId() +
                ", username='" + getUserName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}