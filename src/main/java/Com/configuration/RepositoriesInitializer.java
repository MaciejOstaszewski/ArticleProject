package Com.configuration;

import Com.models.Interests;
import Com.models.Role;
import Com.models.User;
import Com.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class RepositoriesInitializer {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired CommentsRepository commentsRepository;

    @Autowired InterestsRepository interestsRepository;

    @Bean
    InitializingBean init() {

        return () -> {
            if (interestsRepository.findAll().isEmpty()) {
                interestsRepository.save(new Interests("Motoryzacja"));
                interestsRepository.save(new Interests("Nauka"));
                interestsRepository.save(new Interests("Historia"));
                interestsRepository.save(new Interests("Medycyna"));
                interestsRepository.save(new Interests("Astronomia"));
                interestsRepository.save(new Interests("Ekonomia"));
                interestsRepository.save(new Interests("Geografia"));
                interestsRepository.save(new Interests("Sztuka"));
                interestsRepository.save(new Interests("Humor"));
                interestsRepository.save(new Interests("Film"));
                interestsRepository.save(new Interests("Gra"));




            }


            if (roleRepository.findAll().isEmpty()) {
                try {
                    Role roleUser = roleRepository.save(new Role(Role.Types.ROLE_USER));
                    Role roleAdmin = roleRepository.save(new Role(Role.Types.ROLE_ADMIN));

                    User admin = new User("admin", true);
                    admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
                    admin.setPassword(passwordEncoder.encode("admin"));

                    User user = new User("user", true);
                    user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                    user.setPassword(passwordEncoder.encode("user"));

                    userRepository.save(admin);
                    userRepository.save(user);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

}