package com.example.Erp.Project.DTO;

import com.example.Erp.Project.Entity.Role;

public record RegisterRequest(
        String username,
        String email,
        String password,
        Role role
) {}
