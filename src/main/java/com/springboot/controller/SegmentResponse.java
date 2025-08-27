package com.springboot.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentResponse {
    private String segment;

    @Override
    public String toString() {
        return "SegmentResponse{" +
                "segment='" + segment + '\'' +
                '}';
    }
}
