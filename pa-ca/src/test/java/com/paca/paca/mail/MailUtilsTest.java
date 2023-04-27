package com.paca.paca.mail;

import com.paca.paca.mail.utils.MailUtils;
import com.paca.paca.exception.exceptions.IOException;

import junit.framework.TestCase;

import org.jsoup.Jsoup;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Map;

import static java.util.Map.entry;

@ExtendWith(MockitoExtension.class)
public class MailUtilsTest {
    @InjectMocks
    private MailUtils mailUtils;

    @Test
    void shouldGetIOExceptionDueToResourceNotExists() {
        String filePath = "/static/templates/error.html";
        Map<String, String> data = Map.ofEntries(
                entry("${KEY1}", "${VALUE1}"),
                entry("${KEY2}", "${VALUE2}"));

        try {
            MailUtils.htmlToString(filePath, data);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
            Assert.assertEquals(e.getMessage(), "Can't Read email template");
            Assert.assertEquals(((IOException) e).getCode(), (Integer) 40);
        }
    }

    @Test
    void shouldParseHtmlToString() throws java.io.IOException {
        String filePath = "/static/templates/index.html";
        Map<String, String> data = Map.ofEntries(
                entry("${KEY1}", "${VALUE1}"),
                entry("${KEY2}", "${VALUE2}"));

        Resource expectedResource = new ClassPathResource(filePath);
        InputStream inputStream = expectedResource.getInputStream();
        String expected = String.valueOf(Jsoup.parse(inputStream, "UTF-8", ""));
        for (Map.Entry<String, String> entry : data.entrySet()) {
            expected = expected.replace(entry.getKey(), entry.getValue());
        }

        String fileContents = MailUtils.htmlToString(filePath, data);

        Assert.assertEquals(fileContents, expected);
    }
}
