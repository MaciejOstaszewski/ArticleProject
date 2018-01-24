package Com.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "interests")
@Getter
@Setter
@NoArgsConstructor
public class Interests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    public long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Interests(String name) {
        this.name = name;
    }
}
