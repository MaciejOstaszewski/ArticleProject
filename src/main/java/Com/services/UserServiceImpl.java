package Com.services;


import Com.configuration.ProfileNames;
import Com.models.Interests;
import Com.models.Role;
import Com.repositories.InterestsRepository;
import Com.repositories.RoleRepository;
import Com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userDetailsService")
@Profile(ProfileNames.DATABASE)
public class UserServiceImpl implements UserService {

    Authentication auth;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InterestsRepository interestsRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Com.models.User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return convertToUserDetails(user);
    }

    private UserDetails convertToUserDetails(Com.models.User user) {
        Set<GrantedAuthority> grantedAuthorities =
                user.getRoles().stream().map(//mapowanie Role na GrantedAuthority
                        r -> new SimpleGrantedAuthority(r.getType().toString())
                ).collect(Collectors.toSet());

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);//klasa ma też drugi konstruktor – więcej parametrów
    }

    @Override
    public void save(Com.models.User user) {

        Role userRole = roleRepository.findRoleByType(Role.Types.ROLE_USER);
        List roles = Arrays.asList(userRole);
        user.setRoles(new HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm(null);//wyzerowanie jest potrzebne ze względu na walidację
        userRepository.saveAndFlush(user);
    }

    @Override
    public boolean isUniqueLogin(String username) {
        return userRepository.findByUsername(username) == null;
    }

    @Override
    public List<Interests> getInterestsList() {
        return interestsRepository.findAll();
    }

    @Override
    public Com.models.User getCurrentUser() {
        auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName());
    }

    @Override
    public List<Interests> getCurrentUserInterests() {
        auth = SecurityContextHolder.getContext().getAuthentication();
        return  userRepository.findByUsername(auth.getName()).getInterests();
    }

}
