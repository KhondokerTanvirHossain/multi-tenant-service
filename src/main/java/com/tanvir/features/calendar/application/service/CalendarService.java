package com.tanvir.features.calendar.application.service;

import com.tanvir.features.calendar.application.port.in.CalendarUseCase;
import com.tanvir.features.calendar.application.port.out.CalendarPersistencePort;
import com.tanvir.features.calendar.application.port.out.HolidayPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class CalendarService implements CalendarUseCase {

	private final CalendarPersistencePort calendarPort;
	private final HolidayPersistencePort holidayPort;

	public CalendarService(CalendarPersistencePort calendarPort, HolidayPersistencePort holidayPort) {
		this.calendarPort = calendarPort;
		this.holidayPort = holidayPort;
	}

	@Override
	public Mono<LocalDate> getNextBusinessDateForOffice(String officeId, LocalDate currentBusinessDate) {
		return this.getNextBusinessDateFromHolidayAndCalendar(officeId, currentBusinessDate);
	}

	/*@Override
	public Mono<LocalDate> getLastBusinessDateForOffice(String officeId, LocalDate currentBusinessDate) {
		return calendarPort.getLastBusinessDateForOffice(officeId, currentBusinessDate);
	}*/

	private Mono<LocalDate> getNextBusinessDateFromHolidayAndCalendar(String officeId, LocalDate currentBusinessDate) {
		AtomicReference<LocalDate> businessDate = new AtomicReference<>();
        log.info("Current Business Date: {}", currentBusinessDate);
		return calendarPort.getNextBusinessCalendarDateForOffice(officeId, currentBusinessDate)
				.doOnNext(calendar -> log.debug("Calendar: {}", calendar))
				.flatMap(calendar -> {
					businessDate.set(calendar.getCalendarDate());
					return holidayPort.getNextHolidayEntryForOffice(officeId, currentBusinessDate);
				})
				.doOnNext(holiday -> log.debug("Holiday: {}", holiday))
				.flatMap(holiday -> {
					if (!holiday.getHolidayDate().isEqual(businessDate.get())) {
						return Mono.just(businessDate.get());
					} else {
						return this.getNextBusinessDateFromHolidayAndCalendar(officeId, businessDate.get());
					}
				})
				.doOnNext(nextBusinessDate -> log.info("Next Business Date: {}", nextBusinessDate));
	}
}
