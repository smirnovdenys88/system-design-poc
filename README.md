# System Design POC: Gaming Platform & Game Provider A Integration

This project is a **Proof of Concept (POC)** demonstrating a secure and scalable integration protocol between a **Gaming Platform** and a **Game Provider** using **JWT (JSON Web Tokens)** with **RSA asymmetric encryption**.  
The solution addresses key use cases for player authentication, access control, and dynamic content updates.

---

## 🔑 Key Features

- **Secure Authentication**  
  Players are authenticated by the Gaming Platform, which then provides a signed JWT for accessing games on the Game Provider's site.

- **Authorization**  
  The Game Provider validates the JWT and checks the payload to ensure the player is authorized to access the specific game requested.

- **Access Denial**  
  The system automatically denies access for non-authenticated calls, invalid tokens, or unauthorized games.

- **Dynamic Content**  
  The Game Provider can dynamically add new games to the Gaming Platform's catalog via a dedicated API endpoint.

---

## 📂 Project Structure

The project consists of two independent **Quarkus microservices** and a `docker-compose.yml` file to orchestrate their deployment.

- `gaming-platform/`  
  Service representing the Gaming Platform. It handles player login, JWT generation, and management of the platform's game catalog.

- `game-provider-a/`  
  Service representing the Game Provider. It handles JWT validation, authorization checks, and provides an API to dynamically update the Gaming Platform's game list.

- `docker-compose.yml`  
  Defines the services and their networking for local deployment.

---

## ⚙️ Technologies Used

- **Java 21**
- **Maven**
- **Quarkus.io** — lightweight Java framework
- **JSON Web Tokens (JWT)** — secure, stateless authentication
- **RSA Encryption** — asymmetric key signing for JWTs
- **Docker & Docker Compose**
- **cURL** — for testing API endpoints

---

## 🚀 Running the POC

### Prerequisites
- Docker
- Docker Compose

### Steps

##Build and Run with Docker Compose:
```
docker-compose up --build
```
**The Gaming Platform will run on http://localhost:8081 and Game Provider A on http://localhost:8082.**

## 🧪 Testing Scenarios

Use curl to test the API endpoints and verify the behavior of the system.

### 1. Use Case 1: Authenticated Player Login and Token Retrieval
**Goal:** Get a JWT from the Gaming Platform for an authenticated user. The token will be valid for 5 minutes.

- **Action:** Send a login request to the Gaming Platform.

```curl -X POST --location 'http://localhost:8081/auth/login' \
--header 'Content-Type: application/json' \
--data '{
"name": "testuser",
"password": "password"
}'
```
- **Expected Result:**

- HTTP Status 200 OK.

- The response body will contain a JWT token. Copy it for subsequent requests.

### 2. Use Case 2: Access Denied (Invalid/Forbidden Access)
**Goal:** Verify that access is denied for unauthorized calls.

- **Action A (Invalid Token):** Try to access a game on Game Provider A with an incorrect token.

```
curl --location 'http://localhost:8082/games/play/103' \
--header 'Authorization: Bearer invalid_token_example'
Expected Result: HTTP Status 401 Unauthorized.
```
- **Action B (Forbidden Game):** Use a valid JWT to access a game that is not yet in the allowed list for the platform (e.g., game 103).
```
curl --location 'http://localhost:8082/games/play/103' \
--header 'Authorization: Bearer <YOUR_JWT_TOKEN>'
```
Expected Result: HTTP Status 403 Forbidden. The message will state that the game is not allowed for the platform.

### 3. Use Case 3: Dynamic Game List Expansion and Access
**Goal:** Verify the process of adding a new game dynamically and then accessing it.

- **Action A (Add Game):** Simulate Game Provider A providing a new game (103) to the platform.
```
curl -X POST --location 'http://localhost:8082/games/provide' \
--header 'Content-Type: application/json' \
--data '{
"id": 103,
"platformId": "platform_a"
}'
```
- **Expected Result:** HTTP Status 200 OK. The gaming-platform service's console will show that a new game has been added.

- **Action B (Get New Token):** The old JWT token does not include the newly added game. You must get a new token.
```
curl -X POST --location 'http://localhost:8081/auth/login' \
--header 'Content-Type: application/json' \
--data '{
"name": "testuser",
"password": "password"
}'
```
- **Action C (Access New Game):** Use the new JWT to access the newly added game (103).
```
curl --location 'http://localhost:8082/games/play/103' \
--header 'Authorization: Bearer <YOUR_NEW_JWT_TOKEN>'
```
Expected Result: HTTP Status 200 OK. The response confirms successful access to the game.

### 4. Verifying Token Expiration
**Goal:** Confirm the token becomes invalid after 5 minutes.

- **Action:** Get a fresh JWT, wait for over 5 minutes, then try to access a game with the now-expired token.

- **Expected Result:** HTTP Status 401 Unauthorized with an error indicating the token has expired.