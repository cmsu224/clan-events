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

    public void setKey(String appKey)
    {
        API_KEY = appKey;
    }

    public void setSheetId(String sheetID)
    {
        spreadsheetId = sheetID;
    }

    private static Sheets getSheets() {
        NetHttpTransport transport = new NetHttpTransport.Builder().build();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpRequestInitializer httpRequestInitializer = request -> {
            request.setInterceptor(intercepted -> intercepted.getUrl().set("key", API_KEY));
        };

        return new Sheets.Builder(transport, jsonFactory, httpRequestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<List<Object>> getValues(String range) throws IOException {
        return getSheets()
                .spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute()
                .getValues();
    }

}