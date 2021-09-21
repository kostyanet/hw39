package ua.kostya.hw39.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RaceReport {
    private Long id;
    private LocalDateTime created;
    private int horsesTotal;
    private String horsesOrdered;
    private int betHorseResult;
}
