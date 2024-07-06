package com.tanvir.features.calendar.adapter.in.router;

import com.tanvir.features.calendar.adapter.in.handler.CalendarHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.tanvir.core.routes.RouteNames.GET_NEXT_DAY_FROM_CALENDAR;
import static com.tanvir.core.routes.RouteNames.BASE_URL;


@Configuration
@RequiredArgsConstructor
public class CalendarRouter {

	private final CalendarHandler handler;

	@Bean
	public RouterFunction<ServerResponse> calendarRouterConfig() {
		return RouterFunctions.route()
				.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
						.POST(BASE_URL.concat(GET_NEXT_DAY_FROM_CALENDAR), handler::getNextBusinessDateForOffice))
				.build();
	}
}
