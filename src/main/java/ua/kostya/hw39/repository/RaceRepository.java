package ua.kostya.hw39.repository;

import org.springframework.data.repository.CrudRepository;
import ua.kostya.hw39.repository.entity.RaceReportEntity;

public interface RaceRepository extends CrudRepository<RaceReportEntity, Long> {
}
