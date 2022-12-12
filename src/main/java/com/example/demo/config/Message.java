package com.example.demo.config;

import lombok.*;

@Data
@Setter
@Getter

@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String content;
    private String type;


}
