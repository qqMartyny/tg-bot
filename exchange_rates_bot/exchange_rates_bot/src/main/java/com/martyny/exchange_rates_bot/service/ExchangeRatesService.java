package com.martyny.exchange_rates_bot.service;

import com.martyny.exchange_rates_bot.exception.ServiceException;

public interface ExchangeRatesService {

    String getUSDExchangeRate() throws ServiceException;


    String getEURExchangeRate() throws ServiceException;
}
