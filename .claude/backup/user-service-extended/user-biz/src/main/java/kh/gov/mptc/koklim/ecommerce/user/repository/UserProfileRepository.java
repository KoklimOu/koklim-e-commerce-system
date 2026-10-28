package kh.gov.mptc.koklim.ecommerce.user.repository;

import kh.gov.mptc.koklim.ecommerce.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    // Creates the profile row the first time a Keycloak user calls /me.
    // "on conflict do nothing" makes it safe when the browser fires two /me calls at the same moment.
    @Modifying
    @Query(value = """
            insert into user_profiles (id, username, display_name, created_at, updated_at)
            values (:id, :username, :displayName, now(), now())
            on conflict (id) do nothing
            """, nativeQuery = true)
    int insertIfAbsent(@Param("id") UUID id,
                       @Param("username") String username,
                       @Param("displayName") String displayName);
}
