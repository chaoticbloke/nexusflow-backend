package io.canduer.nexusflow.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
