package beside2.ten039.repository;

import beside2.ten039.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query(value = "select * from calendar where date >= curdate() " +
            "order by date limit 1", nativeQuery = true)
    Calendar latestSchedule();
}
