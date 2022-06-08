# rsa 예제

대칭키, 비대칭키로 거래(통신)시 암호화하는 방법

공개키로 암호화 -> 개인키로 복호화  
개인키로 암호화 -> 공개키로 복호화

---

### 키 파일 생성
```sh
# 개인키 생성
$ openssl genrsa -out private.key 1024
Generating RSA private key, 1024 bit long modulus (2 primes)
.............................................+++++
...........+++++
e is 65537 (0x010001)

# 공개키 생성
openssl rsa -in private.key -out public_key.pem -pubout
writing RSA key
```

##### 개인키
```sh
-----BEGIN RSA PRIVATE KEY-----
MIICXQIBAAKBgQC/PjLP657ffdZMFbhvjuZxNBMZPoMG3DtBZNXJj80n4UMfgirv
6SIT4jwkrszvwMcl+l7zo4yUxOW1HmcMFUzlih84FQi341F3/UVqeSVxYJ6wE07c
ic7auwVOkUJIMphVAL0aKdlDcGbKSrG1KU1uMJpFx+k6gz2tHzCqcrJXdwIDAQAB
AoGAFDF1HA89D0wyPIZumxjzrDBbc+bt3uOSPi1vhYDxoKxgcgtEjWCIgKD6yMTM
TuSEIrZ1IDS8u1ivCJjVOKlnHQbjgqQQGu6Ywz5MGl7OUZtPG0kIU7v5C4Q0RmEC
SHdqmvD9xDvdeWs8LguwvPvesoRPGI5ZEu+11B2eIDA6XUECQQDiOCDMGJ04Zx84
IvpnzGvLzVWQ/Y0xunPuHJSxpHGxFsRIwB4WS/3gTekZGPLHx/pm+wsDend0ks9y
Wp9I5+xFAkEA2GtTYRrCOblyQcFXGZvkX9DQSyL2/aMR9qVy8fDyQz4AKQQwsh6v
fPLzQHB8EcYHpztQK6kEbx68/iP16ly2iwJBANnGtPQmYfxxceGF8tBIQCIDu3MI
VIvh9kbtJsxZBcY4p59tq8lFQNNVzagGl3pybwICfVjM5gDIcIKi8SKreo0CQQC1
NskOWjWf2CRP4vBnxR0pDdQb1h8yqkT0Xf0tkS+KFnRT4+pSe1c+LMB6iLWavFLU
ONocdN39naNqufnET/WZAkBcdEAfguaVBoE0XmGZDDVrkdBegxgWK8oC5K+3Mja8
gBFCD3zFJRi6mY8xqpKJnVaiIJNZnYPwrLGvWfgKGpeZ
-----END RSA PRIVATE KEY-----
```

##### 공개키
```sh
-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/PjLP657ffdZMFbhvjuZxNBMZ
PoMG3DtBZNXJj80n4UMfgirv6SIT4jwkrszvwMcl+l7zo4yUxOW1HmcMFUzlih84
FQi341F3/UVqeSVxYJ6wE07cic7auwVOkUJIMphVAL0aKdlDcGbKSrG1KU1uMJpF
x+k6gz2tHzCqcrJXdwIDAQAB
-----END PUBLIC KEY-----
```

### 실행
* http://localhost:8080/static/index.html
* http://localhost:8080/static/transaction.html

### 기타

* [openssl docs](https://www.openssl.org/docs/man1.0.2/man1/openssl-rsa.html)
* [offbyone tistory](https://offbyone.tistory.com/343)
* [cofs tistory](https://cofs.tistory.com/297)
