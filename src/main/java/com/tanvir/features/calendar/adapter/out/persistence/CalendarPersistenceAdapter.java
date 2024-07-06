package com.tanvir.features.calendar.adapter.out.persistence;

import com.google.gson.Gson;
import com.tanvir.core.util.CommonFunctions;
import com.tanvir.core.util.exception.ExceptionHandlerUtil;
import com.tanvir.features.calendar.adapter.out.persistence.repository.CalendarRepository;
import com.tanvir.features.calendar.application.port.out.CalendarPersistencePort;
import com.tanvir.features.calendar.domain.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
//import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@Slf4j
public class CalendarPersistenceAdapter implements CalendarPersistencePort {

	private final CalendarRepository repository;

	private final ModelMapper modelMapper;
	private final Gson gson;

	public CalendarPersistenceAdapter(CalendarRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.gson = CommonFunctions.buildGson(this);
	}

	@Override
	public Mono<Calendar> getNextBusinessCalendarDateForOffice(String officeId, LocalDate currentBusinessDate) {
		return repository.findFirstByOfficeIdAndIsWorkingDayAndCalendarDateAfterOrderByCalendarDate(officeId, "Yes", currentBusinessDate)
				.map(entity -> gson.fromJson(entity.toString(), Calendar.class))
				.switchIfEmpty(Mono.error(new ExceptionHandlerUtil(HttpStatus.NO_CONTENT, "No business date found")));
	}

	@Override
	public Mono<LocalDate> getNextBusinessDateForOffice(String officeId, LocalDate currentBusinessDate) {
		return Mono.just(LocalDate.now());
	}

	@Override
	public Mono<LocalDate> getLastBusinessDateForOffice(String officeId, LocalDate currentBusinessDate) {
		return repository
				.getLastBusinessDateForOffice(officeId, currentBusinessDate)
				.doOnNext(date -> log.info("last business date for office : {} -> {}", officeId, date));
	}
}
