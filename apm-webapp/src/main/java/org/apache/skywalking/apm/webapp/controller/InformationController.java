/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.skywalking.apm.webapp.controller;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


@RestController
public class InformationController {


    private static String API_JSON = getJson("/apiInformation.json");
    private static String MENU_JSON = getJson("/menuInformation.json");


    private static String getJson(String fileNme) {
        InputStreamReader isr = new InputStreamReader(InformationController.class.getResourceAsStream(fileNme));
        BufferedReader bufferedReader = new BufferedReader(isr);
        String json = bufferedReader.lines().collect(Collectors.joining());

        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return json;
    }


    @GetMapping(value = "/apiInformation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String apiInformation() {

        return API_JSON;

    }


    @GetMapping(value = "/menuInformation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String menuInformation() {

        return MENU_JSON;

    }

}
