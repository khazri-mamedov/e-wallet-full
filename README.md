# e-wallet-full
Simple E-Wallet application with front and back end side (Spring Boot + React)

### Running server with docker

```sh
cd e-wallet-back
docker-compose -f docker-compose.dev.yml up
```
(-d in detached mode)

**(Optinal)** without docker. **Check env vars** from docker-compose before running with the below command

```sh
cd e-wallet-back
mvn spring-boot:run
```
Server port and other configs are in env vars. Check docker-compose file. **project-lombok** isn't used for clarity reasons

Endpoints:
- h2-console: http://localhost:9090/h2-console (jdbc url: jdbc:h2:mem:ewallet;LOCK_TIMEOUT=10000)
- swagger: http://localhost:9090/swagger-ui/index.html (add 'Bearer valid_token' to **authorize**)

### Running client

```sh
cd e-wallet-front
npm install
npm start
```
Default client endpoint is http://localhost:3000 , username: **admin** , password: **admin**

### Tested on:

```sh
Windows 10 (1909)
5.10.13-arch1-2
```

PS: spring application can be further abstracted by AbstractService<T>, AbstractController<T> and etc. 
