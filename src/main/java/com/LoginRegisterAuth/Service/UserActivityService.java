package com.LoginRegisterAuth.Service;

import com.LoginRegisterAuth.Model.UserActivity;
import com.LoginRegisterAuth.Repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

//    public void logLoginActivity(String email, LocalDateTime loginTimestamp) {
//        UserActivity activity = new UserActivity(email, loginTimestamp);
//        userActivityRepository.save(activity);
//    }
//
//    public void logLogoutActivity(String email, LocalDateTime logoutTimestamp) {
//        UserActivity activity = userActivityRepository.findTopByEmailOrderByLoginTimestampDesc(email);
//        if (activity != null) {
//            activity.setLogoutTimestamp(logoutTimestamp);
//            userActivityRepository.save(activity);
//        }
//    }
//
//    public List<UserActivity> getAllActivities() {
//        return userActivityRepository.findAllByOrderByLoginTimestampDesc();
//    }

    public void logLoginActivity(String email, LocalDateTime loginTimestamp) {
        UserActivity activity = new UserActivity(email, loginTimestamp);
        userActivityRepository.save(activity);
    }

    public void logLogoutActivity(String email, LocalDateTime logoutTimestamp) {
        UserActivity activity = userActivityRepository.findTopByEmailOrderByLoginTimestampDesc(email);
        System.out.println("Logging out activity for email: " + email + " at " + logoutTimestamp);

        if (activity != null) {
            activity.setLogoutTimestamp(logoutTimestamp);
            userActivityRepository.save(activity);

            System.out.println("Updated logout timestamp for email: " + email + " to " + logoutTimestamp);
        } else {
            System.out.println("No login activity found for email: " + email);
        }
    }

    public List<UserActivity> getAllActivities() {
        return userActivityRepository.findAllByOrderByLoginTimestampDesc();
    }

    public List<UserActivity> getActivitiesByEmail(String email) {
        return userActivityRepository.findByEmail(email);
    }
}

