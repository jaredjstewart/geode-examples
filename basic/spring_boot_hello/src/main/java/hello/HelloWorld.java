package hello;
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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
//import com.gemstone.gemfire.cache.Region;
//import com.gemstone.gemfire.cache.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

@SpringBootApplication
public class HelloWorld {
  public static void main(String[] args) {
    SpringApplication.run(HelloWorld.class, args);
  }
}

@RestController
class Controller {
  @Autowired
  private Geode cache;
  
  @RequestMapping("/put")
  @ResponseBody
  String put(
      @RequestParam(value="key") String key,
      @RequestParam(value="value") String value) {
    return cache.getRegion().put(key, value);
  }

  @RequestMapping("/get")
  @ResponseBody
  Map.Entry<String, String> get(@RequestParam(value="key") String key) {
    return cache.getRegion().getEntry(key);
  }
}

@Component
class Geode {
  private final ClientCache cache;
  private final Region<String, String> region;
  
  public Geode() {
    cache = new ClientCacheFactory()
        .addPoolLocator("localhost", 10334)
        .create();
    region = cache
      .<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
      .create("region");
  }
  
  Region<String, String> getRegion() {
    return region;
  }
}


