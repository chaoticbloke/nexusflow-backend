package io.canduer.nexusflow.data;

import io.canduer.nexusflow.entity.Permission;
import io.canduer.nexusflow.entity.Role;
import io.canduer.nexusflow.enums.Permissions;
import io.canduer.nexusflow.enums.RolesEnum;
import io.canduer.nexusflow.repository.PermissionRepository;
import io.canduer.nexusflow.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    private final PermissionRepository permissionRepository;

    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            if(permissionRepository.count() == 0){
                for (Permissions permission : Permissions.values()) {

                    Permission p = new Permission();
                    p.setName(permission);

                    permissionRepository.save(p);
                }
            }

            if (roleRepository.count() == 0) {

                Role userRole = new Role();

                Set<Permissions> userPermissions = Set.of(
                        Permissions.CUSTOMER_READ,
                        Permissions.INVOICE_READ
                );

                List<Permission> permissions = permissionRepository.findAll();

                List<Permission> read = permissions.stream()
                        .filter(permission -> userPermissions.contains(permission.getName()))
                        .toList();

                userRole.setPermissions(Set.copyOf(read));
                userRole.setRoleName(RolesEnum.ROLE_USER);


                Role adminRole = new Role();
                adminRole.setRoleName(RolesEnum.ROLE_ADMIN);
                adminRole.setPermissions(Set.copyOf(permissions));
                roleRepository.saveAll(List.of(userRole, adminRole));
            }
        };
    }
}
