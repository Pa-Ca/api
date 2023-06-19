package com.paca.paca.mail.utils;

import java.io.*;
import java.util.Map;
import org.jsoup.Jsoup;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import com.paca.paca.exception.exceptions.IOException;

public class MailUtils {
    public static String htmlToString(String filePath, Map<String, String> data) throws IOException {
        String fileContents = "";

        // Parse the html file to string
        try {
            Resource resource = new ClassPathResource(filePath);

            if (!resource.exists()) {
                throw new IOException("Can't Read email template", 40);
            }

            InputStream inputStream = resource.getInputStream();
            fileContents = String.valueOf(Jsoup.parse(inputStream, "UTF-8", ""));
        } catch (java.io.IOException e) {
            throw new IOException("Can't Read email template", 40);
        }

        // Add data
        for (Map.Entry<String, String> entry : data.entrySet()) {
            fileContents = fileContents.replace(entry.getKey(), entry.getValue());
        }

        return fileContents;
    }

}
