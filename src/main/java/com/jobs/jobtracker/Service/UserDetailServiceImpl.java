package com.jobs.jobtracker.Service;


import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Model.UserPrincipals;
import com.jobs.jobtracker.Repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl  implements UserDetailsService {

    private  UserRepository userRepo;

    public UserDetailServiceImpl(UserRepository userRepo) {

        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {

        User user = userRepo.findByUserName(userName)
                .orElseThrow(() ->
                        new UsernameNotFoundException(STR."User not found with name: \{userName}")
                );

        return new UserPrincipals(user);
    }

}

