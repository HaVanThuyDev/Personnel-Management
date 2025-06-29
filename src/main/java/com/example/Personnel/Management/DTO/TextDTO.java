package com.example.Personnel.Management.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TextDTO {
    private String text;
    private LocalDateTime createdAt;
}