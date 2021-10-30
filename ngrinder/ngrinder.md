# nGrinder

- 네이버에서 성능 측정 목적으로 jython(JVM위에서 동작하는 Python)으로 개발되어 2011년에 공개된 오픈소스 프로젝트이다.
- The Grinder라는 오픈소스 기반으로 개발됐다.
- 서버에 대한 부하를 테스트하는 것이므로 서버의 성능 측정이라고도 할 수 있다.

# Architecture

nGrinder는 `Controller`, `Agent`, `Target` 으로 나눠져있다.

- Controller
    - 테스트 프로세스에 대한 웹 인터페이스 제공
    - 테스트 결과를 수집해 통계로 제공
- Agent
    - Controller의 명령을 받아 부하 발생
- Target
    - 부하 테스트를 받는 대상 머신

![Architecture](./images/image-1.png)
