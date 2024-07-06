package com.tanvir.features.calendar.domain;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {
	
	public String holidayId;
	public String calendarDayId;
	public String officeId;
	public LocalDate holidayDate;
	public String holidayType;
	public String titleEn;
	public String titleBn;
	public String mfiId;
	public String status;
/*
	@Override
	public String toString() {
		return CommonFunctions.buildGsonBuilder(this);
	}*/
}
