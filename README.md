# `Bobson`

`Bobson` 은 `Bob Park` 이 만든 Event Sourcing 을 위한 Project 이다.

## 구성

* Bobson Server API
* Bobson Client (Spring boot Starter)

## Spec

* Java 17
* Spring boot (3.1.2)
* Spring Boot Auto Configuration

## 상세 설명

### Bobson Server API

Event Sourcing 방식을 구현하기 위해 `Event Store` 역할 및 Event 를 관리하는 Server API 이다.

#### 구성 정보

* `Flyway`
    * Event 저장소 역할을 DB 로 하고 있으며, DB 초기화 작업을 위해 사용
* `PostgreSQL`
    * 현재 `PostgreSQL` 만 지원하도록 되어 있지만, 추후 추가 예정

#### Restfull API 목록

##### Event 생성 (`POST` /event)

Event 를 생성하는 API

###### Request Header

* Content-Type: `application/json`

###### Request Body

* eventName `String`
    * 이벤트 이름으로 `bobson-client-spring-boot-starter` 사용시 해당 `Event` class 의 이름이 된다.
* eventData `Object`
    * 이벤트 Data 로 `bobson-client-spring-boot-starter` 사용시 해당 `Event` class 가 `json` 으로 변환된다.
* createdModuleName `String`
    * 해당 이벤트를 생성한 module name 으로 `bobson-client-spring-boot-starter` 사용시 해당 client 의 `instanceId` 가 된다.

###### Request Example

```http request
POST /event HTTP/1.1
Host: localhost:9090
Content-Type: application/json
Content-Length: 105

{
    "eventName": "CreatedSampleEvent",
    "eventData": {
        "test": "test"
    },
    "createdModuleName": "cbfc161f-0688-40be-82e2-7cb4f61e2b82"
}
```

##### Fetch Event (`GET` /event/fetch)

Event 를 각 client 에게 할당하는 API

###### Request Parameter

* eventName
    * 할당할 `Event` 의 이름으로, `bobson-client-spring-boot-starter` 사용시 해당 `Event` class 의 이름이 된다.
* moduleName
    * 할당한 module의 이름으로 `bobson-client-spring-boot-starter` 사용시 해당 client 의 `instanceId` 가 된다.

###### Request Example

```http request
GET /event/fetch?eventName=CreatedSampleEvent&moduleName=cbfc161f-0688-40be-82e2-7cb4f61e2b82 HTTP/1.1
Host: localhost:9090
```

##### Complete Even (`PUT` /event/complete/{eventId})

할당 받은 `Event` 를 처리 결과를 처리하는 API

###### Request Header

* Content-Type: `application/json`

###### Request Body

* isSuccess `boolean`
    * 처리 결과 성공 여부
* message `String`
    * 처리 결과 detail message

### Bobson Client Spring Boot Starter

`Bobson Server API` 와의 연동을 손쉽게 하는 Spring Boot Starter

#### 구성 정보

* Java 17
* Spring Boot 3.1.2
* Spring Boot Auto Configuration

#### 환경설정

* Prefix - (`bobson.client`)

| name          | 설명                                                             | type      | default value         | 비고 |
|---------------|----------------------------------------------------------------|-----------|-----------------------|----|
| enabled       | 활성화 여부                                                         | `boolean` | true                  |    |
| host          | bobson server api host 정보                                      | `string`  | http://localhost:8080 |    |
| instance-id   | bobson client 의 id                                             | `string`  | random uuid           |    |
| fetch-time-ms | bobson client 가 bobson server 로 부터 event 를 fetch 하는 주기 (단위 ms) | `Number`  | 1000                  |    |

#### Annotation

| annotation             | 설명                                                                           | 선언 위치  | 비고 |
|------------------------|------------------------------------------------------------------------------|--------|----|
| `Aggregate`            | aggregate 로 bobson client annotation 을 사용시 class 에 선언해야함                     | class  |    |
| `CommandHandler`       | command handler 로 method 에 선언시 해당 method 실행시 bobson server api 에 event 가 추가됨 | method |    |
| `EventSourcingHandler` | event 처리시 환경설정된 fetch 주기마다 해당 method 가 실행됨                                   | method |    |

## Docker Image 정리

### repository or tag 가 none 인 image 삭제
docker rmi $(docker images -q --filter "dangling=true")

### 패턴을 이용한 삭제
docker rmi $(docker images -q --filter "reference=hwpark/bobson-server-api/0.0.*")