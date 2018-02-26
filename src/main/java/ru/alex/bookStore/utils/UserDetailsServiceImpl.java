package ru.alex.bookStore.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;
import ru.alex.bookStore.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        /*for (UserRole userRole: user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getRole()));
        }*/
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
