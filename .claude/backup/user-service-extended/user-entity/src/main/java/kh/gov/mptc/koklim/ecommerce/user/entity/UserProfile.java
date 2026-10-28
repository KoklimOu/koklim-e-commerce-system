package kh.gov.mptc.koklim.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

// Public profile only. Keycloak owns identity and security data:
// email, password, phone, roles, enabled/disabled, MFA. None of that is stored here.
// The id is the Keycloak user id (the "sub" claim of the token), so no generator.
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private UUID id;

    // Copy of Keycloak's preferred_username, kept for display and search. Refreshed on every /me.
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(length = 100)
    private String displayName;

    @Column(length = 500)
    private String avatarUrl;

    @Column(length = 500)
    private String bio;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
