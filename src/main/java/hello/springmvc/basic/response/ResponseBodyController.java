package hello.springmvc.basic.response;

import hello.springmvc.basic.HelloData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
//@ResponseBody
//@RestController
public class ResponseBodyController {

    // 가장 기본적인 response 방법
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    // ResponseEntity 사용 방법
    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() throws IOException {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    // @ResponseBody 사용 방법
    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() throws IOException {
        return "ok";
    }

    // ResponseEntity로 JSON 객체를 넘기는 방법
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);

        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    // JSON 객체 그대로 넘기는 방법
    @ResponseStatus(HttpStatus.OK) // @ResponseStatus로 상태코드 작성가능
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);

        return helloData;
    }



}
