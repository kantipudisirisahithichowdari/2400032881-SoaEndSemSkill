# Round Robin API Gateway + Signup Service

A small Spring Boot / Spring Cloud demo for learning service discovery and client-side
Round Robin load balancing.

## Requirements
- JDK 17
- Spring Tool Suite (STS) or Eclipse with Spring Tools
- Maven (STS can use its embedded Maven)

## Projects
1. `eureka-server` — service registry, port 8761
2. `api-gateway` — Spring Cloud Gateway (WebFlux), port 8081
3. `signup-service` — REST service; run two instances on ports 8082 and 8083

The gateway uses `lb://SIGNUP-SERVICE`. Spring Cloud LoadBalancer chooses registered
instances using its default Round Robin algorithm.

## Import into STS
1. Extract the ZIP file.
2. Open STS.
3. Choose **File > Import > Maven > Existing Maven Projects**.
4. Browse to the extracted `RoundRobin-APIGateway` folder.
5. Select the parent `pom.xml` and finish. Wait for Maven dependencies to download.

## Run in this order
Run each application as **Spring Boot App**:

1. `EurekaServerApplication` (port 8761)
2. `SignupServiceApplication` (first instance, port 8082)
3. Start a second copy of `SignupServiceApplication` on port 8083:
   - In STS, open **Run > Run Configurations...**
   - Select **Spring Boot App**, create a new launch configuration for `SignupServiceApplication`.
   - Open the **Arguments** tab.
   - In **Program arguments**, enter: `--server.port=8083`
   - Apply and Run.
4. `ApiGatewayApplication` (port 8081)

Open `http://localhost:8761` and confirm that both `SIGNUP-SERVICE` instances
are registered before testing the gateway.

## Test Round Robin
Send repeated GET requests to:

`http://localhost:8081/signup/test`

Example response from one request:
```json
{
  "message": "Signup service is working",
  "instancePort": "8082"
}
```

The next request should be served by the other instance (8083), then alternate
between the available instances. Load balancing applies across requests; do not
expect a fixed order if instances restart or registration changes.

## Test signup POST
POST to:

`http://localhost:8081/signup`

Set header `Content-Type: application/json` and body:

```json
{
  "name": "Sahithi",
  "email": "sahithi@example.com"
}
```

The response includes `servedByPort`, so you can see which instance handled it.

## Important notes
- This is a learning demo. The signup endpoint returns a response only; it does not
  save users to a database and does not implement password handling or authentication.
- Do not add `spring-boot-starter-web` to the API Gateway project. Gateway uses
  Spring WebFlux/reactive infrastructure; the signup service uses Spring MVC.
- If your college lab uses different Spring Boot / Spring Cloud versions, keep the
  Spring Boot and Spring Cloud release train compatible.
