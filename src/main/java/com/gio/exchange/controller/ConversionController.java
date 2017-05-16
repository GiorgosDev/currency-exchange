package com.gio.exchange.controller;

import com.gio.exchange.business.ConversionConstants;
import com.gio.exchange.business.CurrencyExchangeAppException;
import com.gio.exchange.business.calculation.ExchangeCalculator;
import com.gio.exchange.business.vo.CurrencyExchangeRequest;
import com.gio.exchange.business.vo.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class ConversionController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final String EXCEPTION_MESSAGE = "Unknown issue occurred during calculation.";

    @Autowired
    private ExchangeCalculator calculator;

    @RequestMapping(value = "/api/convert", method = RequestMethod.GET)
    public @ResponseBody  ServiceResponse convert(@RequestParam("from")String currencyFrom,
                          @RequestParam("to")String currencyTo,
                          @RequestParam(value = "date",  required = false)
                          @DateTimeFormat(pattern=ConversionConstants.DATE_ATTRIBUTE_FORMAT)
                               LocalDate date,
                          @RequestParam("amount")BigDecimal amount) {
        log.debug("Method : convert - ENTRY");
        ServiceResponse response = new ServiceResponse();
        try {

            if(date == null)
                date = LocalDate.now();

            CurrencyExchangeRequest request = new CurrencyExchangeRequest(currencyFrom, currencyTo,
                    date, amount);

            response.setPayload(calculator.calculate(request));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(SUCCESS_MESSAGE);
        } catch (CurrencyExchangeAppException appExp){
            throw appExp;
        } catch (Exception exp){
            throw new CurrencyExchangeAppException(EXCEPTION_MESSAGE, exp);
        }

        return  response;
    }

}
