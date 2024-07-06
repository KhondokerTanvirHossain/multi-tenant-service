package com.tanvir.features.calendar.adapter.out.persistence;

import com.google.gson.Gson;
import com.tanvir.core.util.CommonFunctions;
import com.tanvir.features.calendar.adapter.out.persistence.repository.HolidayRepository;
import com.tanvir.features.calendar.application.port.out.HolidayPersistencePort;
import com.tanvir.features.calendar.domain.Holiday;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@Slf4j
public class HolidayPersistenceAdapter implements HolidayPersistencePort {
	
	private final HolidayRepository repository;
	
	private final ModelMapper modelMapper;
	private final Gson gson;
	
	public HolidayPersistenceAdapter(HolidayRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.gson = CommonFunctions.buildGson(this);
	}
	
	@Override
	public Mono<Holiday> getNextHolidayEntryForOffice(String officeId, LocalDate currentBusinessDate) {
		return repository.findFirstByOfficeIdAndHolidayDateAfterOrderByHolidayDate(officeId, currentBusinessDate)
				.map(holidayEntity -> gson.fromJson(holidayEntity.toString(), Holiday.class));
	}

	@Override
	public Flux<Holiday> getAllHolidaysOfASamityByLoanAccountId(String loanAccountId) {
		return repository.getAllHolidaysOfASamityByLoanAccountId(loanAccountId)
				.map(holidayEntity -> modelMapper.map(holidayEntity, Holiday.class));
	}

	@Override
	public Flux<Holiday> getAllHolidaysOfASamityBySavingsAccountId(String savingsAccountId) {
		return repository.getAllHolidaysOfASamityBySavingsAccountId(savingsAccountId)
				.map(holidayEntity -> modelMapper.map(holidayEntity, Holiday.class));
	}
}
