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

import com.gemstone.gemfire.cache.Region;
import org.apache.commons.lang.time.StopWatch;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class RandomStockPriceGenerator implements Runnable {
    private static final long TWO_SECONDS = 2000L;
    private final List<String> tickerSymbols = Arrays.asList("AAPL", "AMZN", "DELL", "GOOG");
    private final Region<String, BigDecimal> region;
    private final Random random = new Random();

    public RandomStockPriceGenerator(Region<String, BigDecimal> region) {
        this.region = region;
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int putCounter = 0;
        while (stopWatch.getTime() < TWO_SECONDS ) {
            String tickerSymbol = getRandomTickerSymbol();
            setRandomStockPriceInGeode(tickerSymbol);
            putCounter++;
        }
        stopWatch.stop();
        System.out.println("==============");
        System.out.println("RandomStockPriceGenerator made " + putCounter + " updates in the last two seconds");
    }

    private void setRandomStockPriceInGeode(String tickerSymbol) {
        region.put(tickerSymbol, getRandomPrice());
    }

    private BigDecimal getRandomPrice() {
        int priceInCents =  random.nextInt(100000);
        BigDecimal stockPrice = BigDecimal.valueOf((double) priceInCents / 100);

        return stockPrice;
    }

    private String getRandomTickerSymbol() {
        int index = random.nextInt(tickerSymbols.size());
        return tickerSymbols.get(index);
    }
}
