package com.paca.paca.mail.utils;

import java.io.*;
import java.util.Map;
import org.jsoup.Jsoup;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
public class MailUtils {
        public static String htmlToString(String filePath, Map<String, String> data) {
            String fileContents = "";

            // Parse the html file to string
            try {
                Resource resource = new ClassPathResource(filePath);
                File input = resource.getFile();
                fileContents = String.valueOf(Jsoup.parse(input, "UTF-8", ""));
            } catch (IOException e) {
                // handle exception
            }

            // Add data
            for (Map.Entry<String, String> entry : data.entrySet()) {
                fileContents = fileContents.replace(entry.getKey(), entry.getValue());
            }

             return fileContents;
        }

}
