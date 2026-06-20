package io.canduer.nexusflow.repository;
import io.canduer.nexusflow.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
   Optional<Permission> findByName(String name);

    List<Permission> findByNameContaining(String read);
}
