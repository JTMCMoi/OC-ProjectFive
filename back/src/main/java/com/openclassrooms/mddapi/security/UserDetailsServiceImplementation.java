// package com.openclassrooms.mddapi.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.openclassrooms.mddapi.models.AppUserDetails;
// import com.openclassrooms.mddapi.models.UserEntity;
// import com.openclassrooms.mddapi.repositorys.UserRepository;


// @Service
// public class UserDetailsServiceImplementation implements UserDetailsService {
//     @Autowired
//     private UserRepository userRepository;

//     // @Override
//     // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//     //     System.out.println("Recherche de l'utilisateur : " + email);
//     //     UserEntity user = userRepository.findByEmail(email)
//     //             .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

//     //     org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder =
//     //             new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
//     //     boolean matches = encoder.matches("motdepasse", user.getPassword());
//     //     System.out.println("Mot de passe correspond ? " + matches);

//     //     return new AppUserDetails(user);
//     // }
//     // @Override
//     // public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//     // System.out.println("Recherche de l'utilisateur : " + usernameOrEmail);

//     // UserEntity user;

//     // if (usernameOrEmail.contains("@")) {
//     //     user = userRepository.findByEmail(usernameOrEmail)
//     //             .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + usernameOrEmail));
//     // } else {
//     //     user = userRepository.findByUsername(usernameOrEmail)
//     //             .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le username : " + usernameOrEmail));
//     // }

//     // return new AppUserDetails(user);
//     // }

//     @Override
//     public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//         System.out.println("Recherche de l'utilisateur : " + usernameOrEmail);

//         UserEntity user = userRepository.findByEmailOrUsername(usernameOrEmail)
//                 .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + usernameOrEmail));

//         return new AppUserDetails(user);
//     }

// }

package com.openclassrooms.mddapi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.models.AppUserDetails;
import com.openclassrooms.mddapi.models.UserEntity;
import com.openclassrooms.mddapi.repositorys.UserRepository;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("Recherche de l'utilisateur : " + usernameOrEmail);

        UserEntity user = userRepository.findByEmailOrUsername(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + usernameOrEmail));

        return new AppUserDetails(user, usernameOrEmail);
    }
}