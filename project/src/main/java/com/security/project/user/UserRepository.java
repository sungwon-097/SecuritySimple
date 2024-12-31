package com.security.project.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    /** username, user*/
    private final Map<String, User> userInfo = new HashMap<>();

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userInfo.get(username));
    }

    public void save(User user) {
        userInfo.put(user.getUsername(), user);
    }

    public void updateRefreshByUser(String username, String token){
        User user = userInfo.get(username);
        user.updateRefreshToken(token);
    }

    public Optional<User> findUserByToken(String token) {
        Optional<Map.Entry<String, User>> userEntry = userInfo.entrySet().stream()
                .filter(entry -> entry.getValue().getRefreshToken().equals(token))
                .findFirst();
        return userEntry.map(Map.Entry::getValue);
    }

    public boolean existByUsername(String username) {
        return userInfo.containsKey(username);
    }
}
