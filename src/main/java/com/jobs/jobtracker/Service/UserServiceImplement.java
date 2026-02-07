package com.jobs.jobtracker.Service;
import ch.qos.logback.classic.spi.IThrowableProxy;
import com.jobs.jobtracker.DTO.LoginResponse;
import com.jobs.jobtracker.DTO.SignUpResponse;
import com.jobs.jobtracker.DTO.LoginRequest;
import com.jobs.jobtracker.DTO.SignUpRequest;
import com.jobs.jobtracker.Model.Role;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.UserRepository;
import com.jobs.jobtracker.Security.JWTUtil;
import com.jobs.jobtracker.exceptions.AccountInactiveException;
import com.jobs.jobtracker.exceptions.EmailException;
import com.jobs.jobtracker.exceptions.InvalidCredentialsException;
import com.jobs.jobtracker.exceptions.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@Transactional
public class UserServiceImplement implements UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JWTUtil jwtUtil;


  @Override
  public LoginResponse loginUser(LoginRequest login) {
        User user = userRepo.findByUserName(login.getUserName())
                .orElseThrow(()->new InvalidCredentialsException("Invalid userEmail or User not found"));

        boolean isValid = passwordEncoder.matches(login.getPassword(), // password given by user during login
                user.getPassword()); // encoded password from DB

      if(!user.getIsActive()){
          throw new AccountInactiveException("Account is inactive");
      }
        if (!isValid) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
// login successfully
      log.info("User logged in successfully ", login.getUserName());

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
           throw  new UserAlreadyExistsException("Email already registered " + signUp.getUserEmail());
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
        log.info("User Signed up successfully", signUp.getUserEmail());
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
