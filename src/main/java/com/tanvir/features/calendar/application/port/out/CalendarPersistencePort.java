package com.tanvir.features.calendar.application.port.out;

import com.tanvir.features.calendar.domain.Calendar;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface CalendarPersistencePort {
	Mono<Calendar> getNextBusinessCalendarDateForOffice(String officeId, LocalDate currentBusinessDate);
	
	Mono<LocalDate> getNextBusinessDateForOffice(String officeId, LocalDate currentBusinessDate);
	Mono<LocalDate> getLastBusinessDateForOffice(String officeId, LocalDate currentBusinessDate);
}
