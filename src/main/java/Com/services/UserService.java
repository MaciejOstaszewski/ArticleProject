package Com.services;

import Com.models.Interests;
import Com.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void save(User user);

    boolean isUniqueLogin(String login);

    List<Interests> getInterestsList();

    User getCurrentUser();

    List<Interests> getCurrentUserInterests();
}
