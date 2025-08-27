package com.springboot.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String response_msg;
    
    @Override
    public String toString() {
        return String.format("ApiResponse{response_msg='%s'}", response_msg);
    }
}
