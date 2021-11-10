package com.example.demo.fcm;

import lombok.Data;

import java.util.Map;

@Data
public class Note {
    private String title;
    private String subtitle;
    private Map<String, String> data;
}
