package com.team2.backend.DTO.User;

import com.team2.backend.Enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayUserInfoDTO {
    private Long userId;
    private String displayName;
    private String username;
    private UserRole userRole;
}
