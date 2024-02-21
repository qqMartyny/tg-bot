package com.martyny.exchange_rates_bot.service.imple;

import com.martyny.exchange_rates_bot.client.CbrClient;
import com.martyny.exchange_rates_bot.exception.ServiceException;
import com.martyny.exchange_rates_bot.service.ExchangeRatesService;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";

    @Autowired
    private CbrClient client;

    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    private static String extractCurrencyValueFromXML (String xml, String xPathExpression) throws ServiceException {
        var source = new InputSource(new StringReader(xml));
        try {
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xPathExpression, document);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Не удалось распарсить XML", e);
        }

    }
}
