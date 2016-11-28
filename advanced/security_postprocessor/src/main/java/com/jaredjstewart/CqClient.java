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


import static org.apache.geode.distributed.ConfigurationProperties.SECURITY_CLIENT_AUTH_INIT;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.CqAttributes;
import org.apache.geode.cache.query.CqAttributesFactory;
import org.apache.geode.cache.query.CqQuery;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.distributed.ConfigurationProperties;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class CqClient {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.setProperty("security-username", "guest");
        props.setProperty("security-password", "guest");
        props.setProperty(SECURITY_CLIENT_AUTH_INIT, UserPasswordAuthInit.class.getName());

        ClientCache cache = new ClientCacheFactory(props)
                .addPoolLocator("localhost", 10334)
                .setPoolSubscriptionEnabled(true)
                .set(ConfigurationProperties.SECURITY_MANAGER, SampleSecurityManager.class.getName())
                .create();

        Region<String, Customer> region = cache
                .<String, Customer>createClientRegionFactory(ClientRegionShortcut.PROXY)
                .create("customers");



        CqAttributesFactory cqf = new CqAttributesFactory();
        cqf.addCqListener(new SSNContinuousQuery());
        CqAttributes cqa = cqf.create();


        String cqName = "ssnCq";
        String queryStr = "SELECT * FROM /customers";

        QueryService queryService = region.getRegionService().getQueryService();
        CqQuery priceTracker = queryService.newCq(cqName, queryStr, cqa);
        priceTracker.execute();

        blockUntilUserPressesEnter();

    }

    private static void blockUntilUserPressesEnter() {
        System.out.println("Press enter to end the simulation...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
