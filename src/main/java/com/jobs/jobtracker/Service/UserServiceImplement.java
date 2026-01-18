package com.jobs.jobtracker.Service;
import com.jobs.jobtracker.DTO.LoginResponse;
import com.jobs.jobtracker.DTO.SignUpResponse;
import com.jobs.jobtracker.DTO.LoginRequest;
import com.jobs.jobtracker.DTO.SignUpRequest;
import com.jobs.jobtracker.Model.Role;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.UserRepository;
import com.jobs.jobtracker.Security.JWTUtil;
import com.jobs.jobtracker.exceptions.EmailException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImplement implements UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JWTUtil jwtUtil;


  @Override
  public LoginResponse loginUser(LoginRequest login) {
        User user = userRepo.findByUserName(login.getUserName())
                .orElseThrow(()->new RuntimeException("Invalid userEmail or User not found"));

        boolean isValid = passwordEncoder.matches(login.getPassword(), // password given by user during login
                user.getPassword()); // encoded password from DB

      if(!user.getIsActive()){
          throw new RuntimeException("Account is inactive");
      }
        if (!isValid) {
            throw new RuntimeException("Invalid username or password");
        }
// login successfully


        String token = jwtUtil.generateToken(user.getUserName(), user.getRole().name());

        return new LoginResponse(
               user.getUserName(),
                user.getUserEmail(),
                user.getRole(),
                 "user login successfully!   " ,
                  token
        );
    }

    @Override
    @Transactional
    public SignUpResponse registerUser(SignUpRequest signUp) {


        //Checking if user is already present in db
        if (userRepo.findByUserEmail(signUp.getUserEmail()).isPresent()) {
            throw new EmailException("Email already registered");
        }
        User user = new User();
        user.setUserName(signUp.getUserName());
        user.setUserEmail(signUp.getUserEmail());
        user.setRole(Role.valueOf(signUp.getRole()));
        user.setPhoneNum(signUp.getPhoneNum());
        user.setIsActive(true);


        //
        user.setPassword(passwordEncoder.encode(signUp.getPassword()));
        // save to db
       User savedUser = userRepo.save(user);


        String token = jwtUtil.generateToken(user.getUserName(), user.getRole().name());

        return new SignUpResponse (
                savedUser.getId(),
                savedUser.getUserName(),
                savedUser.getUserEmail(),
                savedUser.getRole(),
                "User registered successfully! " +
                        "with token id " , token


        );

    }
}
