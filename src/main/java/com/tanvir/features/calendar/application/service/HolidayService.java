package com.tanvir.features.calendar.application.service;

import com.tanvir.features.calendar.application.port.in.HolidayUseCase;
import com.tanvir.features.calendar.application.port.in.dto.response.HolidayResponseDTO;
import com.tanvir.features.calendar.application.port.out.HolidayPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
@Service
@Slf4j
public class HolidayService implements HolidayUseCase {
    private final HolidayPersistencePort port;
    private final ModelMapper modelMapper;
    public HolidayService(HolidayPersistencePort port, ModelMapper modelMapper) {
        this.port = port;
        this.modelMapper = modelMapper;
    }

    @Override
    public Flux<HolidayResponseDTO> getAllHolidaysOfASamityByLoanAccountId(String loanAccountId) {
        return port.getAllHolidaysOfASamityByLoanAccountId(loanAccountId)
                .map(holiday -> modelMapper.map(holiday, HolidayResponseDTO.class));
    }

    @Override
    public Flux<HolidayResponseDTO> getAllHolidaysOfASamityBySavingsAccountId(String savingsAccountId) {
        return port.getAllHolidaysOfASamityBySavingsAccountId(savingsAccountId)
                .map(holiday -> modelMapper.map(holiday, HolidayResponseDTO.class));
    }

}
