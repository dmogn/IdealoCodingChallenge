**Language:** Java

**System Requirements:** JDK 8+, Maven

**Dependencies:** Spring Boot, Lombok, JUnit 5

**Single command endpoint URL:** http://host:port/api/v1/robot/command

**Batch scrypt endpoint URL:** http://host:port/api/v1/robot/batchScrypt

**Swagger documentation:** http://host:port/swagger-ui.html - UI
http://host:port/v2/api-docs - JSON

**Build & run production app:**

```
$ mvn clean package
$ java -jar java -jar target/IdealoCodingChallenge-1.0.jar
```

**Endpoint call examples:**

```
$ curl \
    --header "Content-type: text/plain" \
    --request POST \
    --data 'PLACE 0,0,NORTH' \
http://localhost:8080/api/v1/robot/command
```

```
$ curl \
    --header "Content-type: text/plain" \
    --request POST \
    --data 'REPORT' \
http://localhost:8080/api/v1/robot/command
```

**Batch scrypt example:**

```
    $ curl \
        --header "Content-type: text/plain" \
        --request POST \
        --data-binary @samples/scrypt1.txt \
    http://localhost:8080/api/v1/robot/batchScrypt
```

Best regards,

Dmitry Ognyannikov
