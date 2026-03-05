package com.nff.NextFirstFiltrex.auth.dto;


import com.nff.NextFirstFiltrex.auth.entities.Role;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
   private String username;
    private String password;
    private Role role; // USER or ADMIN
}
  

