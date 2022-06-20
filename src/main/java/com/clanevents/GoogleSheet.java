/*
 * Copyright (c) 2022, cmsu224 <https://github.com/cmsu224>
 * Copyright (c) 2022, Brianmm94 <https://github.com/Brianmm94>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.clanevents;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;

import java.io.*;
import java.util.List;

public class GoogleSheet {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static Object API_KEY;
    private static String spreadsheetId;
    private static int httpTimeout;

    public void setKey(String appKey)
    {
        API_KEY = appKey;
    }

    public void setSheetId(String sheetID)
    {
        spreadsheetId = sheetID;
    }

    public void setTimeout(int timeout) { httpTimeout = timeout; }

    private static Sheets getSheets() {
        NetHttpTransport transport = new NetHttpTransport.Builder().build();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpRequestInitializer httpRequestInitializer = request -> {
            request.setReadTimeout(httpTimeout * 1000);
            request.setInterceptor(intercepted -> intercepted.getUrl().set("key", API_KEY));
        };

        return new Sheets.Builder(transport, jsonFactory, httpRequestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<List<Object>> getValues(String range) throws IOException {
        List<List<Object>> ret = null;

        try {
            ret = getSheets()
                    .spreadsheets()
                    .values()
                    .get(spreadsheetId, range)
                    .execute()
                    .getValues();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

}