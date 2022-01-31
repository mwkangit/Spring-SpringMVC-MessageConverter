# Spring-SpringMVC-MessageConverter


## Description

본 프로젝트에서는 Logger, Spring MVC Pattern, HTTP Message Converter을 사용하고 알아본다. Spring MVC의 DispatcherServlet의 기능을 실용적으로 사용한 Controller을 생성한다. 각 메소드에 대한 조건을 명시하여 요청을 받고 요청 header에 따라 응답을 생성하는 동시에 메소드를 Spring MVC의 기능을 사용하여 간결하게 표현하였다. Servlet MVC와 다르게 추가된 Spring MVC의 Message Converter 기능을 심층적으로 알아보았다.



------



## Environment

<img alt="framework" src ="https://img.shields.io/badge/Framework-SpringBoot-green"/><img alt="framework" src ="https://img.shields.io/badge/Language-java-b07219"/> 

Framework: `Spring Boot` 2.6.2

Project: `Gradle`

Packaging: `Jar` 

IDE: `Intellij`

Template Engine: `Thymeleaf`

Dependencies: `Spring Web`, `Lombok`, `Thymeleaf`



------



## Installation

![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black) 

```
./gradlew build
cd build/lib
java -jar hello-spring-0.0.1-SNAPSHOT.jar
```



![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white) 

```
gradlew build
cd build/lib
java -jar hello-spring-0.0.1-SNAPSHOT.jar
```



------



## Core Feature

```java
@ResponseBody
@PostMapping("/request-body-json-v5")
private HelloData requestBodyJsonV5(HttpEntity<HelloData> httpEntity){
    HelloData data = httpEntity.getBody();
    log.info("username={}, age={}", data.getUsername(), data.getAge());
    return data;
}
```

![argument reslover & converter location](/media/mwkang/Klevv/Spring 일지/MVC1/01.15/argument reslover & converter location.png)

Image 출처 : [김영한 스프링 MVC1](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1)



JSON 객체를 요청으로 받는 controller로 요청과 응답을 모두 JSON 객체로 해결하기 때문에 HTTP Message Converter을 이용하게 되어 Spring MVC의 기능을 많이 사용하게 된다.  Spring MVC Pattern을 간결하게 표현한 코드와 HTTP Message Converter을 모두 확인할 수 있는 코드이다.



------



## Demonstration Video

![Spring-SpringMVC-MessageConverter](/home/mwkang/Downloads/Spring-SpringMVC-MessageConverter.gif)



------



## More Explanation
