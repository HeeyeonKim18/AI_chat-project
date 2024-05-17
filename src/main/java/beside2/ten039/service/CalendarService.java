package beside2.ten039.service;

import beside2.ten039.domain.Calendar;
import beside2.ten039.dto.calendar.CalendarDDayRequest;
import beside2.ten039.dto.calendar.LatestScheduleResponse;
import beside2.ten039.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public LatestScheduleResponse findLatestSchedule() {
        Calendar calendar = calendarRepository.latestSchedule();
        LocalDate specialDate = calendar.getDate();
        long dDay = specialDate.toEpochDay() - LocalDate.now().toEpochDay();
        return new LatestScheduleResponse(calendar.getId(), calendar.getInfo(), dDay);
    }

    @Transactional
    public Long savedDate(CalendarDDayRequest request) {
        Calendar calendar = Calendar.builder()
                .info(request.getInfo())
                .date(request.getDate()).build();

        Calendar savedCalendar = calendarRepository.save(calendar);
        return savedCalendar.getId();
    }


}
