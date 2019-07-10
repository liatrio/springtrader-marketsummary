package org.springframework.nanotrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nanotrader.web.controller.MarketSummaryController;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SpringtraderMarketSummaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringtraderMarketSummaryApplication.class, args);
	}
}
