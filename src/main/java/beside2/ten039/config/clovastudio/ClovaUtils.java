package beside2.ten039.config.clovastudio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClovaUtils {

    @Value("${clova.learning.url}")
    private String postUrl;
    private Timestamp timeStamp;

    @Value("${clova.learning.access-key}")
    private String accessKey;

    @Value("${clova.learning.signature}")
    private String signature;

    @Value("${clova.learning.file-path}")
    private String filePath;

    private final RestTemplate restTemplate;

    public void makeLearning() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.set("X-NCP-APIGW-TIMESTAMP", timeStamp.toString());
        httpHeaders.set("X-NCP-IAM-ACCESS-KEY", accessKey);
        httpHeaders.set("X-NCP-APIGW-SIGNATURE-V2", signature);

        HttpEntity request = new HttpEntity(getReqMessage(), httpHeaders);

        try {
            Map response = restTemplate.postForObject(postUrl, request, Map.class);

            for (Object result : response.entrySet()) {
                log.info("result={}", result);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getReqMessage() {
        String requestBody = "";

        try {
            JSONObject obj = new JSONObject();
            obj.put("name", "baoFamily");
            obj.put("model", "HCX-002");
            obj.put("trainingDataSet", filePath);

            requestBody = obj.toString();
        } catch (Exception e) {
            System.out.println("## Exception : " + e);
        }
        return requestBody;
    }
}


