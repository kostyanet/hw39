package ua.kostya.hw39.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class RaceStat {
    private Long id;
    private int betHorseResult;
    private int horsesTotal;
}
