# Spring MVC Setting



```groovy
plugins {
	id 'org.springframework.boot' version '2.6.2'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
}

group = 'hello'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()
}

```

- #### JSP를 사용하지 않고 Thymeleaf를 사용할 것이기 때문에 War가 아닌 Jar을 선택했다.

- #### War를 사용하면 내장 서버도 사용 가능 하지만 주로 외부 서버에 배포하는 목적으로 사용한다. War는 서버(WAS)를 별도로 설치하고 설치된 서버에 빌드된 파일을 넣는 형식으로 사용한다.

- #### Jar은 내장 서버(tomcat)을 사용하고 webapp 경로를 사용하지 않는다.

- #### Jar을 사용 시 `/resource/static` 위치에 `index.html`을 두면 Welcome 페이지로 처리해준다. 즉, 스프링 부트가 지원하는 정적 컨텐츠 위치에 약속한 형식인 `index.html`을 작성하는 것이다.



# Logging



```java
@RestController
public class LogTestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest(){
        String name = "Spring";

        System.out.println("name = " + name);
        log.trace("trace log = {}", name);
        log.debug("debug log = {}", name);
        log.info("info log = {}", name);
        log.warn("warn log = {}", name);
        log.error("error log = {}", name);

        return "ok";
    }
}
```

- #### 스프링 부트 라이브러리 사용 시 로깅 라이브러리 `spring-boot-starter-logging` 이 포함된다.

- #### SLF4J는 수 많은 로깅 라이브러리를 모두 통합할 수 있는 인터페이스이다.

- #### 스프링 부트는 SLF4J로 접근 가능한 구현체인 Logback 로그 라이브러리를 제공하며 대부분 이 라이브러리를 실무에서 사용한다.

- #### @Slf4j는 롬복의 어노테이션으로 로거 객체를 미리 생성해서 바로 이용가능하게 해준다.

- > 2022-01-15 19:04:42.089  INFO 13679 --- [nio-8080-exec-1] hello.springmvc.basic.LogTestController  : info log = Spring

  #### 로그는 시간, 로그 레벨, 프로세스 ID, 쓰레드 명, 클래스 명, 로그 메시지를 순서대로 출력한다.

- #### 로그 레벨은 TRACE > DEBUG > INFO > WARN > ERROR 순서로 되어있다. 

  ```properties
  #전체 로그 레벨 설정(기본 info)
  logging.level.root=info
  #hello.springmvc 패키지와 그 하위 로그 레벨 설정
  logging.level.hello.springmvc=debug
  ```

  #### 전체 로그에 대한 레벨을 설정가능하며 이때 default는 info이고 각 패키지에 대해 로그를 설정 가능하다. 설정을 마치면 각 로그 레벨 이하의 로그가 모두 출력되게 하는 것이다. 위 코드가 같이 있는 경우 더 자세히 명시 되어있는 설정이 우선권을 가지게 된다.

- #### Debug 이상의 로그 레벨부터는 라이브러리부터 모든 파일의 수많은 debug 로그가 발생하여 로그의 수가 많아진다.

- #### 보통 개발 서버는 debug로 로그 레벨을 설정하며 운영 서버는 info 로그 레벨로 설정한다.

- #### @RestController는 뷰를 찾는 것이 아닌 HTTP 메시지 바디에 바로 반환 값을 입력한다. @Controller는 반환 값이 String이면 그 논리 이름으로 이루어진 뷰를 찾는다.

- ```java
  // 더 많은 연산 발생 가능
  log.debug("data="+data);
  // 연산 최소화 하는 올바른 방법
  log.debug("data={}", data);
  ```

  #### 로그 레벨이 info로 설정되어 있을 경우 위 코드의 경우는 일단 문자열을 만드는 연산을 수행 후 debug가 로그 레벨에 부합하지 않는 것을 알고 실행하지 않는다. 하지만 아래 코드의 경우 템플릿 방식으로 연산을 수행하기 이전에 현재 로그 레벨을 확인한다. 즉, 아래 코드는 연산을 수행하지 않아서 더 빠른 처리가 가능하다.

- #### `System.out.print` 는 항상 콘솔에 출력을 하지만 로깅 라이브러리는 설정한 특정 로그만 필요의 경우 출력하기 때문에 성능 저하를 방지할 수 있다. 사실 출력 성능도 `System.out.print` 보다 내부 버퍼링, 멀티 쓰레드 등 면에서 좋다.

- #### 로깅 기술은 콘솔만 아니라 파일이나 네트워크 등 별도의 위치에 로그를 남길 수 있다. 특히 파일로 남길 때는 일별, 특정 욜량에 따라 로그를 분할하는 것이 가능하다. 즉, 적정량의 파일 크기와 개수만 유지하고 추가, 삭제를 반복할 수 있으며 압축해서 백업 자동화하는 것도 가능하다.

> SLF4J : [SLF4J](http://www.slf4j.org)
>
> Logback : [Logback](http://logback.qos.ch)
>
> SpringBoot Log : [SpringBoot Log](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-logging)



# Request Mapping



## Basic Mapping



```java
@RequestMapping("/hello-basic")
public String helloBasic(){
    log.info("helloBasic");
    return "OK";
}
```

- #### 속성은 대부분 배열로 제공하기 때문에 다중 설정이 가능하다.

  - #### ex) `@RequestMapping({"/hello-basic", "hello-go"})`

- #### 스프링은 가장 뒤에 오는 '/'는 있을 경우와 없을 경우 모두 갖은 url로 간주한다.

  #### ex) `/hello-basic`, `hello-basic`



```java
/**
     * method 특정 HTTP 메서드 요청만 허용
     * GET, HEAD, POST, PUT, PATCH, DELETE
     */
@RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
public String mappingGetV1() {
    log.info("mappingGetV1");
    return "ok";
}

/**
* 편리한 축약 애노테이션 (코드보기)
* @GetMapping
* @PostMapping
* @PutMapping
* @DeleteMapping
* @PatchMapping
*/
@GetMapping(value = "/mapping-get-v2")
public String mappingGetV2() {
    log.info("mapping-get-v2");
    return "ok";
}
```

- #### @RequestMapping에 method 속성으로 HTTP 메소드를 지정하지 않으면 HTTP 메소드와 무관하게 호출된다. 즉, 모든 요청이 허용되는  url이 되는 것이다.

- #### HTTP 메소드 매핑 축약 어노테이션을 사용해도 내부 로직을 보면 @RequestMapping, method 속성을 사용하고 있다.

- #### 지정한 HTTP 메소드와 다른 메소드로 요청하면 405 상태코드(Method Not Allowed)를 반환한다.



```java
/**
* PathVariable을 지정해서 사용하는 것을 확인할 수 있다.
* 변수명이 같으면 생략 가능
* @PathVariable("userId") String userId -> @PathVariable userId
*/
@GetMapping("/mapping/{userId}")
// @PathVariable String userId는 변수명이 식별자와 같아서 사용가능
public String mappingPath(@PathVariable("userId") String data){
	log.info("mappingPath userId={}", data);
	return "ok";
}

/**
* PathVariable 사용 다중
*/
@GetMapping("/mapping/users/{userId}/orders/{orderId}")
public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
    log.info("mappingPath userId={}, orderId={}", userId, orderId);
    return "ok";
}
```

- #### 최근 HTTP API는 리소스 경로에 식별자를 넣는 스타일을 선호한다. 즉, 동적으로 값을 경로에 넣는 것이다.

  - #### ex) `/mapping/userA`

    #### `/users/1`

- #### 서버 측에서는 메소드의 매개변수로 @PathVariable로 경로 식별자를 인식할 수 있다. @PathVariable 이름과 파라미터 이름이 같으면 생략 가능하다.



```java
/**
* 파라미터로 추가 매핑
* params="mode",
* params="!mode"
* params="mode=debug"
* params="mode!=debug" (! = )
* params = {"mode=debug","data=good"}
*/
@GetMapping(value = "/mapping-param", params = "mode=debug")
public String mappingParam() {
	log.info("mappingParam");
	return "ok";
}

/**
* 특정 헤더로 추가 매핑
* headers="mode",
* headers="!mode"
* headers="mode=debug"
* headers="mode!=debug" (! = )
*/
@GetMapping(value = "/mapping-header", headers = "mode=debug")
public String mappingHeader() {
	log.info("mappingHeader");
	return "ok";
}
```

- #### Param과 header 속성을 통해 특정 파라미터가 있거나 없는 조건을 추가하는 것이 가능하다. 즉, 특정 정보를 추가하여 세부적으로 매핑하는 것이다.



```java
/**
* Content-Type 헤더 기반 추가 매핑 Media Type
* consumes="application/json"
* consumes="!application/json"
* consumes="application/*"
* consumes="*\/*"
* MediaType.APPLICATION_JSON_VALUE
*/
@PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
public String mappingConsumes() {
	log.info("mappingConsumes");
	return "ok";
}

/*** Accept 헤더 기반 Media Type
* produces = "text/html"
* produces = "!text/html"
* produces = "text/*"
* produces = "*\/*"
*/
@PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
public String mappingProduces() {
	log.info("mappingProduces");
	return "ok";
}
```

- #### Consume 속성을 통해 content-type 헤더를 기반으로 특정 미디어 타입으로 매핑하는 것이 가능하다. 미디어 타입이 맞지 않을 경우 415 상태코드(Unsupported Media Type)를 반환한다. 서버 입장에서 요청을 소비하는 것이므로 consume이라고 부른다.

- #### Produces는 Accept 헤더를 기반으로 특정 미디어 타입으로 매핑한다. 즉, accept에 produces에 지정한 타입이 존재해야 하며 우선 순위를 통해 응답한다. 맞는 미디어 타입이 없는 경우 406 상태코드(Not Acceptable)를 반환한다. 서버 입장에서 응답을 생산하는 것이므로 produces라고 부른다.



## HTTP API Mapping



```java
@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {

    /**
     * 회원 목록 조회: GET /users
     * 회원 등록: POST /users
     * 회원 조회: GET /users/{userId}
     * 회원 수정: PATCH /users/{userId}
     * 회원 삭제: DELETE /users/{userId}
     */

    @GetMapping
    public String user(){
        return "get users";
    }

    @PostMapping
    public String addUser(){
        return "post user";
    }

    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId){
        return "get userId" + userId;
    }

    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId){
        return "update userId=" + userId;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId){
        return "delete userId=" + userId;
    }

}
```

- #### HTTP API는 GET, POST, PUT, PATCH, DELETE을 모두 지원한다.

- #### 같은 url을 이용해도 메소드가 다르기 때문에 각각 요청을 수행하는 것이 가능하다.

- #### 클래스 레벨에 매핑 정보를 두면 메소드 레벨에서 해당 정보를 조합해서 사용한다. 즉, url의 prefix를 클래스 레벨의 매핑 정보에 작성하는 것이다.



# Request



## Header



```java
@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie
    ){
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);

        return "ok";
    }

}
```

- #### Locale은 언어 정보를 말하며 가장 우선 순위 높은 언어가 저장된다.

  - #### ex) `KO-KR`

- #### MultiValueMap은 모든 HTTP 헤더를 조회할 때 사용한다. MultiValueMap은 하나의 키에 여러 값을 받을 수 있는 특징이 있다.

  - ```java
    MultiValueMap<String, String> map = new LinkedMultiValueMap();
    map.add("keyA", "value1");
    map.add("keyA", "value2");
    //[value1,value2]
    List<String> values = map.get("keyA");
    ```

    #### 헤더 중 accept-encoding 경우 `[gzip, deflate, br]` 등 배열로 요청되기 때문에 MultiValueMap로 조회하는 것이 좋다.

@Controller 파라미터 목록 : [Controller Parameter](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-
arguments)

@Controller 응답 목록 : [Controller Response](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types)



## Query Parameter



### @RequestParam



```java
@RequestMapping("/request-param-v1")
public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String username = request.getParameter("username");
	int age = Integer.parseInt(request.getParameter("age"));
	log.info("username={}, age={}", username, age);

	response.getWriter().write("ok");
}

// v1 개선형
@ResponseBody
@RequestMapping("/request-param-v2")
public String requestParamV2(
    @RequestParam("username") String memberName,
    @RequestParam("age") int memberAge
){
    log.info("username={}, age={}", memberName, memberAge);
    return "ok";
}

// v2 개선형
@ResponseBody
@RequestMapping("/request-param-v3")
public String requestParamV3(
    @RequestParam String username,
    @RequestParam int age
){
    log.info("username={}, age={}", username, age);
    return "ok";
}

// v3 개선형
@ResponseBody
@RequestMapping("/request-param-v4")
public String requestParamV4(String username, int age){
    log.info("username={}, age={}", username, age);
    return "ok";
}
```

- #### @RequestParam으로 쿼리 파라미터를 받아서 로직 처리에 사용 가능하다.

- #### 스프링이 자동으로 자료형 변환하여 원하는 자료형으로 데이터를 저장해준다.

- #### @Controller은 반환 문자열로 뷰를 찾지만 @ResponseBody로 설정되면 반환 값을 메시지 바디에 넣어서 클라이언트에게 전송한다.

- #### 사실 @RestController 내부 로직을 보면 @Controller와 @ResponseBody를 사용하고 있는 것을 볼 수 있다.

- #### 쿼리 파라미터가 단순 타입(String, int, Integer)일 경우 @RequestParam이 생략 가능하다. 하지만 @RequestParam으로 명확하게 요청 파라미터를 표시하는 것이 좋기도 하다.



```java
// 필수 쿼리 파라미터 지정
// required로 필수 파라미터 지정가능
@ResponseBody
@RequestMapping("/request-param-required")
public String requestParamRequired(
	@RequestParam(required = true) String username,
	@RequestParam(required = false) Integer age
){
    log.info("username={}, age={}", username, age);
    return "ok";
}

// 쿼리 파라미터가 null인경우 defaultValue로 넣어줄 수 있다
@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault(
	@RequestParam(required = true,defaultValue = "guest") String username,
	@RequestParam(required = false, defaultValue = "-1") Integer age
){
    log.info("username={}, age={}", username, age);
    return "ok";
}

// 쿼리 파라미터를 모두 map으로 조회한다
@ResponseBody
@RequestMapping("/request-param-map")
public String requestParamMap(@RequestParam Map<String, Object> paramMap){
    log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
    return "ok";
}
```

- #### Required 속성 값이 true 이면 반드시 값이 포함되어 요청되어야 한다는 뜻이며 false는 없어도 괜찮다는 뜻이다.

- #### DefaultValue에 지정한 쿼리 파라미터가 요청에 없는 경우 default 값을 설정해줄 수 있다. 

- #### 파라미터 이름만 사용한 경우 "" 와 같은 빈 문자로 전송된다. 즉, null이  아닌 빈 문자가 요청되는데 defaultValue는 빈 문자가 요청되면 설정된 default 값으로 대체한다.

- #### 헤더와 동일하게 쿼리 파라미터를 Map, MultiValueMap으로 모두 조회하는 것이 가능하다.



### @ModelAttribute



```java
// @ModelAttribute 쿼리 파라미터를 한번에 객체에 넣는다
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData){
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    log.info("HelloData={}", helloData);
    return "ok";
}

// @ModelAttribute 생략 가능
@ResponseBody
@RequestMapping("/model-attribute-v2")
public String modelAttributeV2( HelloData helloData){
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    log.info("HelloData={}", helloData);
    return "ok";
}
```

- #### @ModelAttribute는 미리 클래스를 생성 후 그 클래스에 쿼리 파라미터를 바로 저장한다. 즉, 쿼리 파라미터의 키 값과 같은 이름의 변수가 클래스에 설정되어 있어야 한다.

- #### 요청 파라미터의 키 값으로 저장할 객체의 프로퍼티를 찾고 해당 프로퍼티의 setter을 호출하여 파라미터 값을 바인딩하다.

- #### 프로퍼티는 객체에 변수에 대한 getter, setter 메소드가 있으면 그 객체는 그 변수에 대한 프로퍼티가 있다고 한다. 변수 프로퍼티의 값을 변경하면 setter이 호출되며 조회하면 getter이 호출된다. 프로퍼티를 찾을 때에는 getter, setter에 설정된 변수 이름의 캐멀케이스된 첫 글자를 소문자로 변환 후 찾는다. 하지만 숫자가 들어가야할 곳에 문자를 넣는 등 바인딩 오류가 발생하면 BindException이 발생한다.

- #### 쿼리 파라미터 매핑 어노테이션이 없는 경우 단순 타입으로 쿼리 파라미터를 받는 경우가 아니라면 모두 @ModelAttribute로 인식한다. 하지만 argument resolver로 지정된 타입은 @ModelAttribute로 인식하지 않는다. 나중에 @RequestBody 사용 시 조심해야 한다.



## TEXT Body



### InputStream



```java
@PostMapping("/request-body-string-v1")
public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messageBody={}", messageBody);

    response.getWriter().write("ok");
}

// v1에서 request, response를 InputStream과 Writer로 받로 받는다.
@PostMapping("/request-body-string-v2")
public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messageBody={}", messageBody);

    responseWriter.write("ok");
}
```

- #### Stream은 byte 코드이므로 byte 코드를 변환해야 한다. 이때 byte 코드가 인코딩 된 방식을 명시해줘야 알맞은 디코더로 변환할 수 있다.

  - #### ex) `StandardCharsets.UTF_8`

- #### InputStream은 HTTP 요청 메시지 바디의 내용을 직접 조회하는 것이고 OutputStream은 HTTP 응답 메시지의 바디에 직접 결과를 출력하는 것이다.



### HttpEntity



```java
// HttpEntity로 메시지 바디를 사용가능하게 정제하는 로직을 생략가능. HttpEntity 형식에 맞게 스프링에서 알아서 메시지 바디를 넣어준다.
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(RequestEntity<String> httpEntity) throws IOException {
    // 메시지 바디를 꺼낸것이다.
    String messageBody = httpEntity.getBody();
    log.info("messageBody={}", messageBody);

    return new ResponseEntity<>("ok", HttpStatus.CREATED);
}
```

- #### HttpEntity는 HTTP 헤더, 바디 정보를 편리하게 조회하게 해준다. 즉, 메시지 바디 정보를 직접 조회할 수 있게 한다.

- #### HttpEntity는 응답에도 사용이 가능하다. 메시지 바디 정보를 직접 변환하며 헤더 정보를 포함하는 것이 가능하다. HttpEntity 응답 시 뷰를 조회하지 않고 메시지 바디로 응답한다.

- #### HttpEntity를 상속 받는 두 가지 객체가 존재한다.

  - #### RequestEntity : HttpMethod, url 정보가 추가, 요청에서 사용한다. 즉, 요청 메시지에 대한 내용을 저장한다.

  - #### ResponseEntity : HTTP 상태 코드를 설정 가능 하며 응답에서 사용한다.



### @RequestBody



```java
// HttpEntity가 아닌 @RequestBody로 바로 문자열로 받는다.
@ResponseBody
@PostMapping("/request-body-string-v4")
public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {
    log.info("messageBody={}", messageBody);
    return "ok";
}
```

- #### @RequestBody로 편리하게 메시지 바디를 조회할 수 있으며 헤더 정보가 필요하다면 HttpEntity를 사용하거나 @RequestHeader를 사용하면 된다.

- #### @ResponseBody를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다. 이때 뷰를 호출하지 않는다.

- #### 현재 스프링 MVC가 자동으로 바디 메시지를 문자나 객체로 변환하는 과정이 발생하는데 이것을 담당해주는 기능이 HTTP 메시지 컨버터(HTTPMessageConverter)이다.



## JSON Body



```java
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
```

- #### @RequestBody를 사용하면 요청 JSON 데이터를 변환 과정을 거치지 않고 바로 객체에 저장할 수 있다. HTTP 메시지 컨버터가 JSON을 변환하고 argument resolver이 데이터를 매핑한다. 물론 여기서는 HTTP 메시지 컨버터를 위해 content-type이 application/json으로 설정되어 있어야 한다.

- #### @RequestBody 사용 시 생략이 불가능하다. 그 이유는 메시지 변환 어노테이션 생략 시 단순 타입이면 @RequestParam으로 작용하고 나머지 상황은 @ModelAttribute로 작용하기 때문이다. 즉, @RequestBody를 생략하면 @ModelAttribute로 작용하여 오류가 발생할 수 있다.

- #### @ResponseBody 사용 시 해당 객체를 HTTP 메시지 바디에 직접 넣어서 응답하는 것이 가능하다. 물론 HttpEntity를 응답에 사용하는 것도 가능하다.

- #### @RequestBody 요청과 @ResponseBody 응답은 처리 순서가 반대이다.

  - #### @RequestBody : JSON 요청 -> HTTP 메시지 컨버터 -> 객체

  - #### @ResponseBody : 객체 -> HTTP 메시지 컨버터 -> 응답



# Response



## Static Resource & View Template



- #### 스프링 부트는 특정 클래스패스에 정적 리소스를 제공하게 설정되어 있다.

  - #### ex) `/static`, `/public`, `resources`, `/META-INF/resources`

- #### `src/main/resources` 는 리소스를 보관하는 곳이고 클래스패스의 시작 경로이다. 내부에 리소스를 저장하면 스프링 부트가 정적 리소스로 서비스를 제공한다.

- #### 뷰 템플릿을 이용하면 뷰 템플릿을 거쳐서 HTML이 생성되고 뷰가 응답을 만들어서 전달한다.

- #### 스프링 부트는 기본 뷰 템플릿 경로를 제공한다

  - #### ex) `src/main/resources/templates`

- #### 타임리프 사용 시 `build.gradle` 에 디펜던시를 추가해야 한다.

  > implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'

- #### 스프링 부트는 자동으로 `ThymeleafViewResolver` 와 필요한 스프링 빈들을 등록한다.

- #### 타임리프는 default prefix, suffix 설정 값이 있으며 `application.properties` 로 설정을 변경하는 것도 가능하다

  ```properties
  # application.properties
  
  spring.thymeleaf.prefix=classpath:/templates/
  spring.thymeleaf.suffix=.html
  ```



```java
// ModelAndView 이용
@RequestMapping("/response-view-v1")
public ModelAndView responseViewV1(){
    ModelAndView mav = new ModelAndView("response/hello")
        .addObject("data", "hello!");
    return  mav;
}

// Model과 논리이름 분리
@RequestMapping("/response-view-v2")
public String responseViewV2(Model model){
    model.addAttribute("data", "hello!");
    return  "response/hello";
}
```

- #### ModelAndView 객체를 이용할 수도 있지만 Model과 View를 분리하여 처리하는 것도 가능하며 명확하게 볼 수 확인하는 것이 가능하다.

- #### 뷰를 이용할 때에는 항상 모델에 넣을 데이터를 정하여 저장하고 뷰의 논리 이름을 반드시 저장하거나 반환해야 한다.



```java
// url과 논리이름 같을 시 반환값 생략가능
@RequestMapping("/response/hello")
public void responseViewV3(Model model){
    model.addAttribute("data", "hello!");
}
```

- #### Void를 반환하는 경우 요청 url을 논리 이름으로 사용한다.

- #### 이 방법은 명시성이 떨어지고 위험하다. 또한, 이렇게 맞는 경우도 별로 없어서 사용이 권장되지 않는다.



## HTTP API



### String



```java
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
```

- #### 메시지 바디를 통해 응답할 때에는 HttpServletResponse, ResponseEntity, @ReponseBody를 사용한다.

- #### 각각의 반환형을 다르며 ResponseEntity, @ResponseBody를 사용하는 것이 편리하다.



### JSON



```java
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
```

- #### JSON 객체를 그대로 반환할 때에도 ResponseEntity, @ResponseBody를 사용한다.

- #### 이 때 두 방법 모두 객체를 그대로 반환한다는 점이 있으며 HTTP 메시지 컨버터가 JSON 형식으로 변환하여 응답을 전송한다.



# HTTP Message Converter



- #### 스프링 MVC는 다음의 경우에 HTTP 메시지 컨버터를 적용한다.

  - ####  HTTP 요청 : @RequestBody, HttpEntity(RequestEntity).

  - #### HTTP 응답 : @ResponseBody, HttpEntity(ResponseEntity).

- #### 스프링 부트 기본 메시지 컨버터이다(우선순위대로 나열한 것이다).

  >0 = ByteArrayHttpMessageConverter
  >
  >1 = StringHttpMessageConverter
  >
  >2 = MappingJackson2HttpMessageConverter

- #### 메시지 컨버터는 대상 클래스 타입과 미디어 타입을 모두 체크하여 사용여부를 체크한다. 둘중 하나라도 만족하지 않는다면 다음 메시지 컨버터로 우선 순위가 넘어간다.

- #### 우선순위 0순위는 ByteArrayHttpMessageConverter이며 `byte[]` 데이터를 처리한다.

  - #### 클래스 타입 : `byte[]`, 미디어타입 : `*/*`

  - #### 요청 ex)  `@RequestBody byte[] data`

  - #### 응답 ex) `@ResponseBody return byte[]`, 쓰기 미디어타입 : `application/octet-stream`

- #### 우선순위 1순위는 StringHttpMessageConverter이며 `String` 문자로 데이터를 처리한다. Byte로 오는 데이터를 `String`으로 변환하여 객체에 저장한다.

  - #### 클래스 타입 : `String`, 미디어타입 : `*/*`

  - #### 요청 ex) `@RequestBody String data`

  - #### 응답 ex) `@ResponseBody return "ok"`, 쓰기 미디어타입 : `text/plain`

- #### 우선순위 2순위는 MappingJackson2HttpMessageConverter이며 `application/json` 타입을 처리한다.

  - #### 클래스 타입 : 객체 또는 `HashMap`, 미디어타입 : `application/json` 관련

  - #### 요청 ex) `@RequestBody HelloData data`

  - #### 응답 ex) `@ResponseBody return helloData`, 쓰기 미디어타입 : `application/json` 관련

- #### 메시지 컨버터의 canRead(), canWrite() 메소드는 메시지 컨버터가 해당 클래스, 미디어타입을 지원하는지 체크한다.

- #### 메지시 컨버터의 read(), write() 메소드는 메시지 컨버터를 통해서 메시지를 읽고 쓰는 기능을 한다.



### Reading Request Data



- #### HTTP 요청이 오면 @RequestBody, HttpEntity를 파라미터로 사용한다.

- #### 메시지 컨버터가 컨버터를 대조하여 사용가능한 컨버터를 선별하는 작업을 canRead() 메소드를 통해 진행한다.

  - #### 대상 클래스 타입을 지원하는지 알아본다.

    - #### ex) `byte[], String, HelloData`

  - #### HTTP 요청의 content-type 미디어타입을 지원하는지 알아본다.

    - #### ex) `text/plain, application/json, */*`

- #### CanRead() 메소드에서 컨버터가 선별되면 read() 메소드를 호출하여 객체를 생성하고 반환한다.



### Writing Response Data



- #### 컨트롤러에서 @ResponseBody, HttpEntity로 값이 반환된다.

- #### 메시지 컨버터가 컨버터를 대조하여 사용가능한 컨버터를 선별하는 작업을 canWrite() 메소드를 통해 진행한다.

  - #### 대상 클래스 타입을 지원하는지 알아본다.

    - #### ex) `byte[], String, HelloData`

  - #### HTTP 요청의 Accept 미디어타입을 지원하는지 알아본다(@RequestMapping의 produces로 이것을 세팅하지 않는다면 Accept를 이용한다).

    - #### ex) `text/plain, application/json, */*`

- #### CanWrite() 메소드에서 컨버터가 선별되면 write() 메소드르 호출하여 HTTP 응답 메시지 바디에 데이터를 생성한다.



### Argument Resolver & Converter Location



![argument reslover & converter location](/media/mwkang/Klevv/Spring 일지/MVC1/01.15/argument reslover & converter location.png)

- #### RequestMappingHandlerAdapter에서 핸들러의 매개변수에 알맞은 데이터를 생성해야 핸들러에서 비즈니스 로직 수행이 가능하다.

- #### 핸들러에서 응답을 메시지 바디에 알맞은 데이터를 넣어서 전송해야 한다.

- #### 어댑터는 ArgumentResolver를 호출하여 핸들러가 필요로 하는 다양한 파라미터의 객체를 생성한다. 객체 데이터가 모두 준비되면 핸들러를 호출하여 값을 넘겨주게 된다.

- #### ArgumentResolver은 supportsParameter() 메소드를 호출하여 해당 파라미터를 지원하는지 체크한다.

- #### SupportsParameter()을 성공적으로 거친 후 resolveArgument()를 호출하여 실제 객체를 생성하고 핸들러에 넘겨주게 된다.

- #### 직접 인터페이스를 확장하여 원하는 ArgumentResolver를 만들 수도 있다.

가능한 파라미터 목록 : [ArgumentResolver](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments)

- #### ReturnValueHandler은 응답 타입을 변환한다. 즉, 반환값이 정확한 형식으로 응답에 저장되어 전송하는 것을 도와준다.

- #### 핸들러에서 String으로 뷰 이름을 반환해도 동작하는 이유가 ReturnValueHandler 덕분이다.

가능한 응답 값 목록 : [ReturnValueHandler](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types)

- #### 요청의 경우 ArgumentResolver 들이 HTTP 메시지 컨버터를 사용하여 필요한 객체를 생성한다. 즉, 메시지 바디에 있거나 넣어야 한다면 HTTP 메시지 컨버터를 호출하여 이외의 경우(쿼리 파라미터)는 ArgumentResolver이 수행한다.

- #### 응답의 경우 메시지 바디에 데이터를 넣어야할 때 ArgumentResolver이 HTTP 메시지 컨버터를 호출하여 응답 결과를 만든다.

- #### 스프링은 대부분의 기능을 인터페이스로 제공하여 언제든지 확장이 가능하다.

  - #### ex) `HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler, HttpMessageConverter`

- #### 스프링이 필요한 대부분의 기능을 제공하기 때문에 실제 기능을 확장할 일이 많지는 않지만 확장이 필요할 때  WebMvcConfigurer를 사용한다.



# Additional Knowledge



- #### Int는 기본형이어서 null이 저장될 수 없다. 하지만 Integer와 같은 제네릭 타입은 객체이므로 null이 저장되는 것이 가능하다. @RequestParam 시 null이 요청되어 오류가 발생할 때 int를 Integer로 바꾸는 방법이 자주 사용된다.