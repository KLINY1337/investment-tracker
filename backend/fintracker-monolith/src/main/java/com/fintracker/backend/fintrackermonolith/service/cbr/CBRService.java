package com.fintracker.backend.fintrackermonolith.service.cbr;

import com.fintracker.backend.fintrackermonolith.exception.cbr.CBRRatesException;
import com.fintracker.backend.fintrackermonolith.service.cbr.dao.ValKursCBR;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;

@Service
@Slf4j
public class CBRService {

    public ValKursCBR getDailyCurrencyRate() throws CBRRatesException {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";
        try {
            String xml = fetchXML(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(ValKursCBR.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ValKursCBR valCurs = (ValKursCBR) jaxbUnmarshaller.unmarshal(new StringReader(xml));
            return valCurs;

        } catch (Exception e) {
            log.error("CBR rates excepton",e);
            e.printStackTrace();
            throw CBRRatesException.builder().message(e.getMessage()).build();
        }
    }


    public static String fetchXML(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
