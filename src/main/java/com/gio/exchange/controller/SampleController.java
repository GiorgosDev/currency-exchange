package com.gio.exchange.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

@Controller
public class SampleController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping("/api/hello")
    public String helloWorld() {
        log.debug("Logging works!");
        ExchangeRateProvider ecbExchangeRateProvider = MonetaryConversions.getExchangeRateProvider("ECB");

        return "Hello World!";

    }
}
