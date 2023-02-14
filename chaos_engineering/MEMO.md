* 카오스 엔지니어링을 도와주는 툴 - Chaos Monkey 사용
* 카오스 멍키 스프링 부트 : 카오스 멍키를 스프링부트에 적용해주는 툴 (스프링 부트를 망가트릴 수 있는 툴)

* 공격 대상(Watcher)
  * @RestController
  * @Controller
  * @Service
  * @Repository 
  * @Component

* 공격 유형(Assaults)
  * 응답 지연 (Latency Assault)
  * 예외 발생 (Exception Assault)
  * 애플리케이션 종료 (AppKiller Assault)
  * 메모리 누수 (Memory Assault)

* http1~4 : 카오스멍키 활성화시키거나 상태 보거나 설정하는 방법들임.
* JMeter로 inifinite로 해두고 카오스 멍키 활성화해서 성능 테스트 해보는거임.
* H2로는 이유는 모르겠으나 너무 빠른 요청 시 제대로 동작하지 않아서 도커에 포스트그레 올려서 테스트함. (docker-scripts.sh 실행 후 하면 됨)

1. applications.properties에 설정을 통해 카오스 멍키 활성화하면 카오스 멍키 로고가 보임.