package com.tanvir.features.calendar.application.port.in;


import com.tanvir.features.calendar.application.port.in.dto.response.HolidayResponseDTO;
import reactor.core.publisher.Flux;

public interface HolidayUseCase {
    Flux<HolidayResponseDTO> getAllHolidaysOfASamityByLoanAccountId(String loanAccountId);
    Flux<HolidayResponseDTO> getAllHolidaysOfASamityBySavingsAccountId(String savingsAccountId);
}
