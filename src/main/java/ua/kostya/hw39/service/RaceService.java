package ua.kostya.hw39.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostya.hw39.dto.Bet;
import ua.kostya.hw39.dto.RaceReport;
import ua.kostya.hw39.dto.RaceStat;
import ua.kostya.hw39.repository.RaceRepository;
import ua.kostya.hw39.repository.StatsRepository;
import ua.kostya.hw39.repository.entity.RaceReportEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class RaceService {
    private static final List<String> raceResult = new ArrayList<>();
    private final StatsRepository statsRepository;
    private final RaceRepository raceRepository;
    private Bet bet;

    @Autowired
    public RaceService(StatsRepository statsRepository, RaceRepository raceRepository) {
        this.statsRepository = statsRepository;
        this.raceRepository = raceRepository;
    }

    public List<RaceStat> getStats() {
        List<RaceStat> stats = new ArrayList<>();
        statsRepository
                .findAll()
                .forEach((s) -> {
                    var stat = new RaceStat(s.getId(), s.getBetHorseResult(), s.getHorsesTotal());
                    stats.add(stat);
                });
        return stats;
    }

    public RaceReport getRaceById(Long id) {
        var race = raceRepository.findById(id);
        if (race.isPresent()) {
            return race
                    .map((r) ->
                            new RaceReport(r.getId(), r.getCreated(), r.getHorsesTotal(), r.getHorsesOrdered(), r.getBetHorseResult())
                    ).get();
        }

        return null;
    }

    public void runRace(Bet bet) {
        this.bet = bet;
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable barrierAction = () -> {
            System.out.println("Well done, guys!");
            System.out.println(raceResult.toString());
            saveReport();
        };

        CyclicBarrier cyclicBarrier = new CyclicBarrier(bet.getHorsesTotal(), barrierAction);
        Consumer<String> onFinish = (String name) -> {
            raceResult.add(name);
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < bet.getHorsesTotal(); i++) {
            HorseRun h = new HorseRun("Horse " + (i + 1), onFinish);
            executor.execute(h);
        }

        executor.shutdown();
    }

    private void saveReport() {
        RaceReportEntity report = new RaceReportEntity(
                LocalDateTime.now(),
                bet.getHorsesTotal(),
                String.join(", ", raceResult),
                getBetHorsePosition()
        );
        raceRepository.save(report);
        raceResult.clear();
    }

    private int getBetHorsePosition() {
        String name = "Horse " + bet.getBetHorsePosition();
        for (int i = 0; i < raceResult.size(); i++) {
            if (raceResult.get(i).equals(name))
                return i + 1;
        }
        return -1;
    }
}
