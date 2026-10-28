package kh.gov.mptc.koklim.ecommerce.user.repository;

import kh.gov.mptc.koklim.ecommerce.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByUsername(String username);
}
