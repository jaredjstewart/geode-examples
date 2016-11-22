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

import com.google.common.base.Stopwatch;

import org.apache.geode.cache.Region;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class RandomStockPriceGenerator implements Runnable {
    private final List<String> tickerSymbols = Arrays.asList("AAPL", "AMZN", "DELL", "GOOG");
    private final Random random = new Random();
    private final Region<String, BigDecimal> region;

    public RandomStockPriceGenerator(Region<String, BigDecimal> region) {
        this.region = region;
    }

    @Override
    public void run() {
        int putCounter = 0;
        Stopwatch stopWatch = Stopwatch.createStarted();

        while (stopWatch.elapsed(TimeUnit.SECONDS) < 2) {
            String tickerSymbol = getRandomTickerSymbol();
            putRandomStockPriceInGeode(tickerSymbol);
            putCounter++;
        }
        stopWatch.stop();
        System.out.println("==============");
        System.out.format("RandomStockPriceGenerator made %d updates in the last two seconds\n", putCounter);
    }

    private void putRandomStockPriceInGeode(String tickerSymbol) {
        region.put(tickerSymbol, getRandomPrice());
    }

    private BigDecimal getRandomPrice() {
        int priceInCents = random.nextInt(100_000);
        BigDecimal stockPrice = BigDecimal.valueOf((double) priceInCents / 100);

        return stockPrice;
    }

    private String getRandomTickerSymbol() {
        int index = random.nextInt(tickerSymbols.size());
        return tickerSymbols.get(index);
    }
}
