package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
// JSON 형식의 Request Body를 받는 클래스이다
@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    private void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    // v1에 비해 @RequestBody, @ResponseBody로 간편하게 바꾼다
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    private String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    // 바로 자바 객체에 JSON의 값을 넣어준다
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    private String requestBodyJsonV3(@RequestBody HelloData data){
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

    // @RequestBody 대신 HttpEntity 사용
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    private String requestBodyJsonV4(HttpEntity<HelloData> httpEntity){
        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

    //
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    private HelloData requestBodyJsonV5(HttpEntity<HelloData> httpEntity){
        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }
}
