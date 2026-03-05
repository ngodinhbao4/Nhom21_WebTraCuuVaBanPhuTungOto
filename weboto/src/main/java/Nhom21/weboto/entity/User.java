package Nhom21.weboto.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // [cite: 98]

    @Column(nullable = false, unique = true, length = 50)
    private String username; // [cite: 99]

    @Column(nullable = false, length = 255)
    private String password; // [cite: 100]

    @Column(nullable = false, unique = true, length = 100)
    private String email; // [cite: 101]

    @Column(name = "is_active")
    private Boolean isActive = false; // [cite: 102]

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // [cite: 103]

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles", // [cite: 106]
        joinColumns = @JoinColumn(name = "user_id"), // [cite: 107]
        inverseJoinColumns = @JoinColumn(name = "role_id") // [cite: 108]
    )
    private Set<Role> roles = new HashSet<>();
}

