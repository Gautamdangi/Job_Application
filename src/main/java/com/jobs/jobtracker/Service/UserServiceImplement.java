package com.jobs.jobtracker.Service;
import com.jobs.jobtracker.DTO.LoginResponse;
import com.jobs.jobtracker.DTO.SignUpResponse;
import com.jobs.jobtracker.DTO.LoginRequest;
import com.jobs.jobtracker.DTO.SignUpRequest;
import com.jobs.jobtracker.Model.User;
import com.jobs.jobtracker.Repository.UserRepository;
import com.jobs.jobtracker.Security.JWTUtil;
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

        if (!isValid) {
            throw new RuntimeException("Invalid username or password");
        }
// login successfully


        String token = jwtUtil.generateToken(user.getUserName());

        return new LoginResponse(token,
                "user login successful");
    }

    @Override
    public SignUpResponse registerUser(SignUpRequest signUp) {

        User user = new User();
        user.setUserName(signUp.getUserName());
        user.setUserEmail(signUp.getUserEmail());
        //
        user.setPassword(passwordEncoder.encode(signUp.getPassword()));
        // save to db
       User savedUser = userRepo.save(user);
        // Use cases-> userEmail id find already in db so login
//add security question
        return new SignUpResponse (
                savedUser.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                "User registered successfully"


        );

    }
}
