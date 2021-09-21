package ua.kostya.hw39.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * дата проведения,
 * общее кол-во лошадей,
 * место каждой из них и
 * номер лошади на которую ставил пользователь.
 */
@Entity(name = "race_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    private int horsesTotal;
    private String horsesOrdered;
    private int betHorseResult;

    public RaceReportEntity(
            @NonNull LocalDateTime created,
            @NonNull int horsesTotal,
            @NonNull String horsesOrdered,
            @NonNull int betHorseResult
    ) {
        this.created = created;
        this.horsesTotal = horsesTotal;
        this.horsesOrdered = horsesOrdered;
        this.betHorseResult = betHorseResult;
    }
}
