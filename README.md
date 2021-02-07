# e-wallet-full
Simple E-Wallet application with front and back end side

### Running server with docker

```sh
cd e-wallet-back
docker-compose -f docker-compose.dev.yml up
```
(-d in detached mode)

**(Optinal)** without docker

```sh
cd e-wallet-back
mvn spring-boot:run
```
Server port and other configs are in env vars. Check docker-compose file

Endpoints:
- h2-console: http://localhost:9090/h2-console (jdbc url: jdbc:h2:mem:ewallet;LOCK_TIMEOUT=10000)
- swagger: http://localhost:9090/swagger-ui/index.html

### Running client

```sh
cd e-wallet-front
npm install
npm start
```
Default client endpoint is http://localhost:3000