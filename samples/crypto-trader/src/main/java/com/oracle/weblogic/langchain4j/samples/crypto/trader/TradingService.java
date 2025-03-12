/*
 * Copyright (c) 2025 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oracle.weblogic.langchain4j.samples.crypto.trader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.oracle.weblogic.langchain4j.samples.crypto.trader.Order.Side;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;

//Not thread safe
@ApplicationScoped
public class TradingService {

    private static final Logger LOGGER = Logger.getLogger(TradingService.class.getName());
    private static final String BINANCE_URL = "https://api.binance.com";
    private Client webClient;
    private final List<Order> history = new ArrayList<>();
    private final Map<String, List<Candlestick>> candlesPerSymbol = new HashMap<>();
    private final Map<String, List<Order>> openPositions = new HashMap<>();
    private BigDecimal profit = new BigDecimal(0);

    // CDI required
    TradingService() {
    }

    @Inject
    public TradingService(Client webClient) {
        this.webClient = webClient;
    }

    @Tool("Obtain data of a {{symbol}} to analyze what is going to happen in the future")
    public List<Candlestick> symbolCandles(@V("symbol") String symbol) {
        List<Candlestick> candles = candlesPerSymbol.get(symbol);
        LOGGER.info("In obtainData " + symbol);
        if (candles == null) {
            candles = loadCandlestick(symbol);
            candlesPerSymbol.put(symbol, candles);
        }
        return candles;
    }
    
    @Tool("Gets the history of orders that were executed")
    public List<Order> history() {
        LOGGER.info("In history");
        return List.copyOf(history);
    }

    @Tool("Given an amount in {{dollars}} to spend and the {{currentPrice}}, obtains the quantity")
    public BigDecimal calculateQuantity(@V("dollars") BigDecimal dollars, @V("currentPrice") BigDecimal currentPrice) {
        LOGGER.info("In calculateQuantity");
        return dollars.divide(currentPrice, 6, RoundingMode.DOWN);
    }

    @Tool("Execute a buy order specifying the {{symbol}} and {{quantity}} and {{currentPrice}}")
    public Order buy(@V("symbol") String symbol, @V("quantity") BigDecimal quantity, @V("currentPrice") BigDecimal currentPrice) {
        LOGGER.info("In buy " + symbol + ", " + quantity);
        Order order = Order.create(symbol, System.currentTimeMillis(), Side.BUY, currentPrice, quantity);
        List<Order> orders = openPositions.computeIfAbsent(symbol, c -> new ArrayList<>());
        orders.add(order);
        history.add(order);
        return order;
    }

    @Tool("Execute a sell order specifying the {{symbol}} and {{currentPrice}}")
    public Order sell(@V("symbol") String symbol, @V("currentPrice") BigDecimal currentPrice) {
        LOGGER.info("In sell " + symbol);
        Order order;
        List<Order> open = openPositions.remove(symbol);
        if (open == null) {
            throw new IllegalArgumentException("You don't own " + symbol + " to sell");
        } else {
            LOGGER.info("There are " + open.size() + " orders");
            BigDecimal totalQuantity = new BigDecimal(0);
            BigDecimal totalSpent = new BigDecimal(0);
            for (Order o : open) {
                // Sum all the money spent
                totalSpent = (o.getPrice().multiply(o.getQuantity())).add(totalSpent);
                // Sum all quantity
                totalQuantity = o.getQuantity().add(totalQuantity);
            }
            order = Order.create(symbol, System.currentTimeMillis(), Side.SELL, currentPrice, totalQuantity);
            BigDecimal totalSold = expectedProfit(symbol, currentPrice, open);
            profit = profit.add(totalSold);
        }
        history.add(order);
        return order;
    }

    @Tool("It calculates the expected benefit if the user sells {{symbol}} and {{currentPrice}}")
    public BigDecimal sellExpectedProfit(@V("symbol") String symbol, @V("currentPrice") BigDecimal currentPrice) {
        LOGGER.info("In sellExpectedProfit " + symbol);
        List<Order> open = openPositions.get(symbol);
        return expectedProfit(symbol, currentPrice, open);
    }

    private BigDecimal expectedProfit(String symbol, BigDecimal currentPrice, List<Order> open) {
        BigDecimal totalQuantity = new BigDecimal(0);
        BigDecimal totalSpent = new BigDecimal(0);
        for (Order o : open) {
            // Sum all the money spent
            totalSpent = (o.getPrice().multiply(o.getQuantity())).add(totalSpent);
            // Sum all quantity
            totalQuantity = o.getQuantity().add(totalQuantity);
        }
        BigDecimal totalSold = (currentPrice.multiply(totalQuantity)).subtract(totalSpent);
        LOGGER.info("Current price " + currentPrice + ". Expected profit: " + totalSold);
        return totalSold;
    }
    
    @Tool("Show the current profit")
    public BigDecimal profit() {
        LOGGER.info("In profit " + profit);
        return profit;
    }

    @Tool("Current price of {{symbol}}")
    public BigDecimal currentPrice(@V("symbol") String symbol) {
        LOGGER.info("In currentPrice " + symbol);
        Map<String, String> response = webClient.target(BINANCE_URL + "/api/v3/ticker/price")
                .queryParam("symbol", symbol + "USDT").request().get(new GenericType<Map<String, String>>(){});
        return new BigDecimal(response.get("price"));
    }

    private List<Candlestick> loadCandlestick(String symbol) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastHour = now.minusHours(1).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = lastHour.atZone(ZoneId.systemDefault());
        long timestamp = zonedDateTime.toInstant().toEpochMilli();
        WebTarget request = webClient.target(BINANCE_URL + "/api/v3/klines").queryParam("symbol", symbol + "USDT")
                .queryParam("interval", "1d").queryParam("limit", "20").queryParam("endTime", Long.toString(timestamp));
        List<List<Object>> data = request.request().get(new GenericType<List<List<Object>>>(){});
        List<Candlestick> candles = new ArrayList<>(data.size());
        for (List<Object> d : data) {
            candles.add(Candlestick.create(symbol, d.toArray()));
        }
        return candles;
    }
}
