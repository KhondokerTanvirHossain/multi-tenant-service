package com.tanvir.features.calendar.application.port.in;

import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface CalendarUseCase {

	Mono<LocalDate> getNextBusinessDateForOffice(String officeId, LocalDate currentBusinessDate);
//	Mono<LocalDate> getLastBusinessDateForOffice(String officeId, LocalDate currentBusinessDate);
}
