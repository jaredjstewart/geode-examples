/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaredjstewart;


import org.apache.geode.cache.Region;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PollingStockTicker implements Runnable {
    private Region<String, BigDecimal> region;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public PollingStockTicker(Region<String, BigDecimal> region) {
        this.region = region;
    }

    @Override
    public void run() {
            printPriceForEveryStock();
    }

    private void printPriceForEveryStock() {
        System.out.println("==============");
        System.out.format("Current stock prices as of %s\n", getCurrentTimestamp());

        for (String tickerSymbol : region.keySetOnServer()) {
            BigDecimal stockPrice = region.get(tickerSymbol);
            printStockPrice(tickerSymbol, stockPrice);
        }
    }

    private void printStockPrice(String key, BigDecimal stockPrice) {
        System.out.format("[%s] %s\n", key, NumberFormat.getCurrencyInstance().format(stockPrice));
    }


    private String getCurrentTimestamp() {
        return timeFormatter.format(LocalDateTime.now());
    }


}
