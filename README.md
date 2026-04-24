<p align="center">
  <img width="200" height="200" alt="elefant" src="https://github.com/user-attachments/assets/d3ab6586-fc31-4393-abf1-96afe13dde94" />
</p>



### Trace
Currently working with a weather API, but any API can be integrated to feed the Instructions of the AI.
The program allows you to spin up a security layer proxy for calling Ollama local LLMs.
This way, the service can be protected by a API key. For more security tweak ollama to loopback address on 127.0.0.1


<img width="1123" height="655" alt="image" src="https://github.com/user-attachments/assets/afacde63-a4aa-4b8d-ac03-201140f8374c" />

### Dependency setup
Install Ollama
```bash
curl -fsSL https://ollama.com/install.sh | sh
```

Check status
```bash
sudo systemctl status ollama
```
If not started run
```bash
sudo systemctl start ollama
```

To allow on boot start
```bash
sudo systemctl enable ollama
```
Donwload LLM model, this should also happen automatially on first endpoint to ollama localhost:11434
```bash
ollama run llama3.2:latest
```

To stop Ollama 
```bash
/bye
```

### Project Setup
### 1. Change BFF client to use real endpoint

in bff/src/main/java/org/example/bff/AI/client/AIClient.java 
line 32
Remove the "/testWithoutCPU" to enable the real ollama call.


### 2. Start nginx + frontend

```bash
docker compose up
```

The SPA is now available at [http://localhost](http://localhost). on port 80
If you have nginx running locally on the host machine, make sure to stop the process
Inpect if you have nginx running with:
```bash
sudo systemctl status nginx
```
If active run 
```bash
sudo systemctl stop nginx
```
on the host machine and refresh the page.

### 3. Starting the BFF

```bash
cd bff
./mvnw spring-boot:run
```

### 4. Starting the Resource server

```bash
cd resource
./mvnw spring-boot:run
```

### 5. Starting the ai-api
```bash
cd ai-api
./mvnw spring-boot:run
```



## Project Structure
<img width="1166" height="747" alt="image" src="https://github.com/user-attachments/assets/5937508a-e2a0-4a6f-ba51-3d1bda2231b2" />




## Key endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/login` | Form login — sets session cookie |
| `POST` | `/api/logout` | Clears session |
| `GET`  | `/api/protected` | Protected resource (requires valid JWT) |
| `GET`  | `/.well-known/jwks.json` | BFF's public RSA key (used by Resource server) |
| `POST` | `/api/prompt` | BFF's endpoint to ai-api prompt 
| `GET`  | `/api/prompt/establish` | BFF's endpoint to ai-api fetch check endpoint connection
| `GET`  | `/api/ollama/v1/establish` | ai-api establish and respond endpoint (check API key)
| `POST` | `/api/ollama/v1` | ai-api prompt endpoint called by BFF
| `POST` | `/api/ollama/v1/testWithoutCPU` | ai-api prompt endpoint called by BFF if testing connection without read ai prompt
| `POST` | `http://localhost:11434/api/generate` | ollama local endpoint

