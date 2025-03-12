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

public class Candlestick {

    private final String symbol;
    private final long kLineOpenTimestamp;
    private final String openPrice;
    private final String highPrice;
    private final String lowPrice;
    private final String closePrice;
    private final String volume;
    private final long kLineCloseTimestamp;
    private final String quoteAssetVolume;
    private final int numberOfTrades;
    private final String buyBaseAssetVolume;
    private final String buyQuoteAssetVolume;

    private Candlestick(String symbol, long kLineOpenTimestamp, String openPrice, String highPrice, String lowPrice, String closePrice,
            String volume, long kLineCloseTimestamp, String quoteAssetVolume, int numberOfTrades,
            String buyBaseAssetVolume, String buyQuoteAssetVolume) {
        this.symbol = symbol;
        this.kLineOpenTimestamp = kLineOpenTimestamp;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.volume = volume;
        this.kLineCloseTimestamp = kLineCloseTimestamp;
        this.quoteAssetVolume = quoteAssetVolume;
        this.numberOfTrades = numberOfTrades;
        this.buyBaseAssetVolume = buyBaseAssetVolume;
        this.buyQuoteAssetVolume = buyQuoteAssetVolume;
    }

    public long getkLineOpenTimestamp() {
        return kLineOpenTimestamp;
    }
    public String getOpenPrice() {
        return openPrice;
    }
    public String getHighPrice() {
        return highPrice;
    }
    public String getLowPrice() {
        return lowPrice;
    }
    public String getClosePrice() {
        return closePrice;
    }
    public String getVolume() {
        return volume;
    }
    public long getkLineCloseTimestamp() {
        return kLineCloseTimestamp;
    }
    public String getQuoteAssetVolume() {
        return quoteAssetVolume;
    }
    public int getNumberOfTrades() {
        return numberOfTrades;
    }
    public String getBuyBaseAssetVolume() {
        return buyBaseAssetVolume;
    }
    public String getBuyQuoteAssetVolume() {
        return buyQuoteAssetVolume;
    }
    public String text() {
        return "[symbol=" + symbol + ", kLineOpenTimestamp=" + kLineOpenTimestamp + ", openPrice="
                + openPrice + ", highPrice=" + highPrice + ", lowPrice=" + lowPrice + ", closePrice=" + closePrice
                + ", volume=" + volume + ", kLineCloseTimestamp=" + kLineCloseTimestamp + ", quoteAssetVolume="
                + quoteAssetVolume + ", numberOfTrades=" + numberOfTrades + ", buyBaseAssetVolume=" + buyBaseAssetVolume
                + ", buyQuoteAssetVolume=" + buyQuoteAssetVolume + "]";
    }

    public static Candlestick create(String symbol, Object[] data) {
        if (data == null || data.length < 11) {
            throw new IllegalArgumentException("Invalid data array: must contain at least 11 elements.");
        }
        long kLineOpenTimestamp = ((Number) data[0]).longValue();
        String openPrice = (String) data[1];
        String highPrice = (String) data[2];
        String lowPrice = (String) data[3];
        String closePrice = (String) data[4];
        String volume = (String) data[5];
        long kLineCloseTimestamp = ((Number) data[6]).longValue();
        String quoteAssetVolume = (String) data[7];
        int numberOfTrades = ((Number) data[8]).intValue();
        String buyBaseAssetVolume = (String) data[9];
        String buyQuoteAssetVolume = (String) data[10];
        return new Candlestick(symbol, kLineOpenTimestamp, openPrice, highPrice, lowPrice, closePrice, volume, 
                               kLineCloseTimestamp, quoteAssetVolume, numberOfTrades, 
                               buyBaseAssetVolume, buyQuoteAssetVolume);
    }
}
