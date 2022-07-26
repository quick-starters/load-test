from locust import HttpUser, task
import json
import pprint
import sseclient

def with_urllib3(url, headers):
    """Get a streaming response for the given event feed using urllib3."""
    import urllib3
    http = urllib3.PoolManager()
    return http.request('GET', url, preload_content=False, headers=headers)

def with_requests(url, headers):
    """Get a streaming response for the given event feed using requests."""
    import requests
    return requests.get(url, stream=True, headers=headers)

class HelloWorldUser(HttpUser):

    @task(99)
    def getStream(self):
        pprint.pprint("시작")
        # 유저 아이디, 룸 할당
        # 유저 1~100 룸 1에 입장
        # 유저 1~70까지 request 메시지 요청
        # 유저 1~90까지만 sse 이벤트가 돌아오고 나머지 10은 오류난다
        client = sseclient.SSEClient(with_urllib3('http://localhost:8080/rooms/5/msgs', {'Accept': 'text/event-stream'}))
        for event in client.events():
            pprint.pprint(json.loads(event.data))
            deltaTime = responseTime - requestTime
            record("delta", deltaTime)
            # 내 메시지 송신에 대한 반응성 체크
        # self.client.get("/rooms?userId=1")

    @task(1)
    def sendMessage(self):
        self.client.post("http://localhost:8080/rooms/5/msgs", json= {
            "userId": 2,
            "msg": "Hello Locust."
        })
        client = sseclient.SSEClient(with_urllib3('http://localhost:8080/rooms/5/msgs', {'Accept': 'text/event-stream'}))
        for event in client.events():
            pprint.pprint(json.loads(event.data))
