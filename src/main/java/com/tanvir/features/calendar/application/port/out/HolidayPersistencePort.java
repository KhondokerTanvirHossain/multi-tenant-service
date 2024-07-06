package com.tanvir.features.calendar.application.port.out;


import com.tanvir.features.calendar.domain.Holiday;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface HolidayPersistencePort {
	
	Mono<Holiday> getNextHolidayEntryForOffice(String officeId, LocalDate currentBusinessDate);

	Flux<Holiday> getAllHolidaysOfASamityByLoanAccountId(String loanAccountId);
	Flux<Holiday> getAllHolidaysOfASamityBySavingsAccountId(String savingsAccountId);
}
