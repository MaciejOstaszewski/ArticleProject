package Com.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 4, max = 36)
    private String username;
    private String password;
    @Transient//nie będzie odwzorowana w db
    private String passwordConfirm;
    private boolean enabled = false;



    @AssertTrue
    private boolean isPasswordsEquals(){
        return password == null || passwordConfirm == null || password.equals(passwordConfirm);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)//LAZY powoduje dociągnięcie tych elementów dopiero wtedy, gdy są używane
    private List<Interests> interests;


    public User(String username){
        this(username, true);
    }

    public User() {
        interests = new ArrayList<Interests>();
    }

    public User(String username, boolean enabled){
        this.username = username;
        this.enabled = enabled;
    }

}
