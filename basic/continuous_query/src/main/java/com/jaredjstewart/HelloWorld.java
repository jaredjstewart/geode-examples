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
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.query.CqAttributes;
import com.gemstone.gemfire.cache.query.CqAttributesFactory;
import com.gemstone.gemfire.cache.query.CqQuery;
import com.gemstone.gemfire.cache.query.QueryService;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        ClientCache cache = new ClientCacheFactory()
                .addPoolLocator("localhost", 10334)
                .setPoolSubscriptionEnabled(true)
                .create();

        Region<String, BigDecimal> region = cache
                .<String, BigDecimal>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                .create("regionA");

        CqAttributesFactory cqf = new CqAttributesFactory();
        cqf.addCqListener(new PriceThresholdContinuousQuery());
        CqAttributes cqa = cqf.create();


        String cqName = "priceOver999.75";
        String queryStr = "SELECT * FROM /regionA e where e > 999.75";

        QueryService queryService = region.getRegionService().getQueryService();
        CqQuery priceTracker = queryService.newCq(cqName, queryStr, cqa);
        priceTracker.execute();

        ExecutorService executorService = startSimulation(region);
        blockUntilUserPressesEnter();


        shutdownSimulation(executorService, cache, priceTracker);
    }

    private static ExecutorService startSimulation(Region<String, BigDecimal> region) {
        System.out.println("Starting the stock price simulation...");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(new PollingStockTicker(region), 1, 2, TimeUnit.SECONDS);
        executorService.scheduleWithFixedDelay(new RandomStockPriceGenerator(region), 0, 1, TimeUnit.MILLISECONDS);

        return executorService;
    }


    private static void shutdownSimulation(ExecutorService executorService, ClientCache cache, CqQuery cqQuery) {
        System.out.println("Shutting down the simulation...");
        executorService.shutdownNow();
        try {
            cqQuery.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        cache.close();
    }

    private static void blockUntilUserPressesEnter() {
        System.out.println("Press enter to end the simulation...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
