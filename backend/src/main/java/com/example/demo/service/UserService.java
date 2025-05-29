package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User editUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setJoinDate(java.time.LocalDateTime.now().toString());
        // Set default profile picture for form registration
        if (user.getProfilePicture() == null || user.getProfilePicture().isEmpty()) {
            user.setProfilePicture(
                    "https://scontent.fhan17-1.fna.fbcdn.net/v/t1.15752-9/470051940_2099017523860364_1882855945771681630_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=9f807c&_nc_eui2=AeFN5boU2KcZDGJmSE3Cgi-Vlf_rnRgfxBGV_-udGB_EEexCAvQVXPMWfyiRAI3v_3WNGkSq2ME8DdszQc6ccxRK&_nc_ohc=lrsoNii7vmEQ7kNvwFMn70n&_nc_oc=Adlqv69VF49aOYULBUN5VKdWweW1wEUz8OmGLDXwu089zn-Qquf_ztCqA7CobL3tkVE&_nc_zt=23&_nc_ht=scontent.fhan17-1.fna&oh=03_Q7cD2QGAkQVLwcyi-KGwY-zz0p5nxI0793o5FmqPCaFwFMClow&oe=685FBB85");
        }
        userRepository.save(user);
    }

    public User updateProfile(String userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(updatedUser.getName());
        existingUser.setBio(updatedUser.getBio());
        existingUser.setLocation(updatedUser.getLocation());
        existingUser.setProfilePicture(updatedUser.getProfilePicture());

        return userRepository.save(existingUser);
    }

    public User updateProfileByEmail(String email, User updatedUser) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(updatedUser.getName());
        existingUser.setBio(updatedUser.getBio());
        existingUser.setLocation(updatedUser.getLocation());
        existingUser.setProfilePicture(updatedUser.getProfilePicture());

        return userRepository.save(existingUser);
    }

    public User setModeratorStatus(String userId, boolean isModerator) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setModerator(isModerator);
        return userRepository.save(user);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}