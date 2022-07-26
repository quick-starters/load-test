**Gatling 학습 목차**

```
1. 개요 및 설치
2. 스크립트 생성
3. 테스트 시뮬레이션 디자인, 실행 & 리포트 분석
```



# 1. 개요 및 설치

## **목차**

```
1-1. 개요
1-2. 소개
1-3. 전제조건
1-4. Standalone 모드 설치
1-5. Gradle로 설치
```



## 1-1. 개요

- 간략하게 Gatling이 어떻게 동작하고, 강력한 스트레스, 퍼포먼스 테스트를 수행하는 툴인지 알아봅니다.
- Gatling 스크립트 개발 환경을 위해 적절한 전제조건을 알아봅니다.
- Gatling 설치 방식을 알아봅니다. (Standalone 또는 Build tool)



## 1-2. 소개

1. 2012년 소개된 오픈소스 부하, 성능 테스트 프레임워크입니다. 
2. 코드 기반으로 부하테스트를 할 수 있습니다. 즉 버전 컨트롤이 가능합니다.
3. Pure Scala로 작성되었으며 AKKA와 Netty 프레임워크 기반으로 만들어졌습니다.

![스크린샷 2022-07-26 오후 2.55.13](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 2.55.13.png)

***JMeter의 Per Thread 모델***

전통적인 JVM 기반 테스팅 툴인 JMeter는 하나의 유저당 하나의 스레드에서 테스트를 진행하여 성능상 한계가 있었습니다.

![스크린샷 2022-07-26 오후 2.55.39](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 2.55.39.png)

***Gatling의 메시지 드리븐 아키텍처***

반면에 Gatling은 메시지 드리븐 아키텍처를 통해 더 많은 가상 유저, 메시지를 한 스레드에서 다룰 수 있습니다.

4. Gatling은 Scala, Java, Kotlin으로 스크립트를 작성할 수 있지만 문법을 잘 알지 못해도 사용할 수 있는 DSL을 제공합니다. 즉 읽기 쉬운 스크립트를 제공합니다.

5. 스크립트를 직접 작성하지 않아도 사용자의 동작을 캡쳐하여 스크립트를 생성하는 레코딩 기능을 제공합니다.

![스크린샷 2022-07-26 오후 2.58.44](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 2.58.44.png)

***Gatling Recorder***



## 1-3. 전제조건

Gatling 3.7 기준

- Java, Kotlin, Scala로 테스트 스크립트 작성이 가능합니다. 이전 버전은 Scala로만 작성할 수 있습니다.
- JDK 8, 11, 17에 대해서만 지원합니다.



## 1-4. Standalone 모드 설치

![스크린샷 2022-07-26 오후 3.03.35](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 3.03.35.png)

***Standalone 모드 번들 설치***

[링크](https://gatling.io/open-source/)에서 Download Now 버튼을 통해 설치합니다.



![image-20220726233900180](/Users/addpage/Library/Application Support/typora-user-images/image-20220726233900180.png)

***번들 폴더 구조***

설치 후 압축을 풀고 들어가면 다음과 같은 형태의 폴더구조가 나옵니다.



![스크린샷 2022-07-26 오후 3.11.43](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 3.11.43.png)

***부하테스트 시작***

`bin/gatling.sh` 를 실행하면 어떤 환경에서 테스트를 돌릴지와 `user-files/simulations` 아래의 테스트 스크립트 중 어떤 스크립트를 실행할지 여부를 클릭하여 시작할 수 있습니다.



![스크린샷 2022-07-26 오후 3.11.58](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 3.11.58.png)

***부하 테스트 결과***

각 테스트 결과를 보여줍니다.



[![스크린샷 2022-07-26 오후 3.12.19](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 3.12.19.png)](https://kakao.agit.in/secure_link?sub=group&key=300069077&h=wL%2BT4tJTqV8gPYPjWIJ%2F%2FTJNO7U%3D&act=view&ih=164&iw=1356&ref=306607067&wmk=ZXZhbi5uYXZl&wsc=0.5&url=https%3A%2F%2Fmud-kage.kakao.com%2Fwmk%2FWyvN2%2FbtrIfRUi7eO%2Fpd65i8ro70uo38y14yj6xz%2F1200_1200.jpg)

***파일로 떨어지는 결과***

커맨드에서 결과를 확인할수도 있지만, 결과로 나오는 파일을 통해 좀 더 자세한 수치를 확인할 수 있습니다.



[![스크린샷 2022-07-26 오후 3.12.40](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 3.12.40.png)](https://kakao.agit.in/secure_link?sub=group&key=300069077&h=vYa7N99O4KIXBCOnTPMzwvnQh6I%3D&act=view&ih=2296&iw=4024&ref=306607070&wmk=ZXZhbi5uYXZl&wsc=5.0&url=https%3A%2F%2Fmud-kage.kakao.com%2Fwmk%2Ftt6bW%2FbtrIdaABbMo%2Fg8o07rumfcofe6njw0cixz%2F1200_1200.jpg)

***부하테스트 결과***



## 1-5. Gradle로 설치

Gradle Plugin으로 구성된 [예제](https://github.com/gatling/gatling-gradle-plugin-demo)를 받아서 커스터마이징 가능합니다. [Maven](https://github.com/gatling/gatling-maven-plugin), [SBT](https://github.com/gatling/gatling-sbt-plugin)도 각각 예시가 제공되고 있습니다.



# 2. 스크립트 생성

## **목차**

```
2-1. 개요
2-2. 예시 애플리케이션
2-3. Gatling 레코더
2-4. Gatling 스크립트 톺아보기
2-5. 입력 파라미터화
```



## 2-1. 개요

Gatling 스크립트 생성에 대해 다뤄봅니다.

- 테스트할 예시 애플리케이션 둘러보기
- Gatling 레코더에 대해서 알아본다.
- 레코더로부터 만들어진 Gatling 스크립트 톺아보기
- `feeder` 와 `correlation` 으로 스크립트 발전시키기



## 2-2. 예시 애플리케이션

![스크린샷 2022-07-26 오후 5.16.50](/Users/addpage/Desktop/스크린샷 2022-07-26 오후 5.16.50.png)

***컴퓨터 데이터베이스 애플리케이션***

Gatling에서 제공하는 [예시 애플리케이션](https://computer-database.gatling.io/computers)은 컴퓨터 데이터베이스 관리 툴입니다. 

- 컴퓨터를 필터링할 수 있다. (검색)

- 새로운 컴퓨터를 추가할 수 있다.

- 컴퓨터 정보를 조회 또는 수정할 수 있다.

- 페이지 기반으로 리스트를 탐색할 수 있다.



## 2-3 Gatling 레코더

![스크린샷 2022-07-26 오후 5.31.44](/Users/addpage/Downloads/images/스크린샷 2022-07-26 오후 5.31.44.png)

***Gatling 레코더***

Gatling은 `Http Proxy`나 `HAR converter` 방식으로 행동 기반 스크립트를 추출할 수 있습니다. 자세한 방법은 [문서](https://gatling.io/docs/gatling/reference/current/http/recorder/)를 확인해주세요. 

예제로는 다음의 시나리오를 레코딩합니다.

> 1. 홈 화면 조회
> 2. 맥북 검색
> 3. 6번 아이디 컴퓨터 조회
> 4. 홈 조회
> 5. 페이지 1~4까지 조회
> 6. 새로운 컴퓨터 생성 페이지 조회
> 7. 새로운 컴퓨터 생성
>
> 자 이제 Gatling 레코더로부터 캡쳐된 스크립트를 봅시다.



## 2-4 Gatling 스크립트 톺아보기

![image-20220726210404317](/Users/addpage/Downloads/images/image-20220726210404317.png)

***레코딩 된 스크립트***

Gatling의 모든 스크립트는 기본적으로 세부분으로 구성됩니다:

1. 프로토콜 설정
   - 기본 URL을 설정하고 원하는 프로토콜을 설정합니다.
2. 시나리오 정의
   - 시나리오는 각 가상 사용자가 수행하는 실제 단계를 구성합니다. 모든 트랜잭션은 이전에 레코드로 기록한 대로입니다.

3. 부하 시뮬레이션 디자인
   - 실행할 사용자 수와 속도를 결정합니다.



해당 코드는 충분히 동작하지만 약간의 냄새가 나죠. 하나씩 고쳐보도록 하겠습니다.



### 시나리오 정의 오브젝트화

![image-20220726212721952](/Users/addpage/Downloads/images/image-20220726212721952.png)

***오브젝트화 된 시나리오***

> 1, 9, 21: 각 시나리오는 [ChainBuilder](https://gatling.io/docs/gatling/reference/current/core/scenario/) 객체로 나눌 수 있습니다.



### 트랜잭션 이름 읽기 쉽게하기

![image-20220726212805095](/Users/addpage/Downloads/images/image-20220726212805095.png)

***읽기 쉬워진 시나리오***

> 2: 레코딩 된 시나리오의 트랜잭션은 `request_n` 형식으로 어떤 작업인지 알기 어렵습니다. 이름을 설정해줍니다.
>
> 10: [반복문](https://gatling.io/docs/gatling/reference/current/core/scenario/#loop-statements)을 이용해 중복 코드를 제거합니다.



### 오브젝트화된 시나리오 재활용

![image-20220726215838287](/Users/addpage/Downloads/images/image-20220726215838287.png)

***재활용 가능한 오브젝트***

> 1, 2: 오브젝트화 된 시나리오는 재활용이 가능합니다. 유저는 `search`와 `browse` 작업만을 테스트하고, 어드민은 `search`, `browse`, `create`까지 테스트합니다.



## 2-5. 입력 파라미터화

![image-20220726220015702](/Users/addpage/Downloads/images/image-20220726220015702.png)

***입력이 파라미터화 된 search 시나리오***

> ![image-20220726220321789](/Users/addpage/Downloads/images/image-20220726220321789.png)
> ***feeader로 넘긴 search.csv***
>
> 1: [Feeder](https://gatling.io/docs/gatling/reference/current/core/session/feeder/)를 사용하여 CSV 같은 외부 파일로부터 입력을 주입 받습니다. 뒤의 `random`은 입력을 주입하는 방식이며 `queue, random, shuffle, circular` 타입이 있습니다.
>
> 5: `feed()` 메소드에 1에서 주입받은 `feeder`를 넘깁니다.
>
> 6: `search.csv`의 searchCriterion 값을 주입합니다. 
>
> 7: [Checks](https://gatling.io/docs/gatling/reference/current/core/check/)를 이용해 값 검사를 합니다. 다양한 검사를 할 수 있고 현재 예제에서는 css 검증을합니다. `href.a` 태그가 searchComputerName을 가지고 있는지 여부를 검사한 후 맞다면 computerUrl로 해당 url을 저장합니다.
>
> 11: 위에서 저장한 `computerUrl`을 통해 검색을 진행합니다.

