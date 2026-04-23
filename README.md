<p align="center">
  <img width="200" height="200" alt="elefant" src="https://github.com/user-attachments/assets/d3ab6586-fc31-4393-abf1-96afe13dde94" />
</p>



### Trace
Currently working with a weather API, but any API can be integrated to feed the Instructions of the AI.
The program allows you to spin up a security layer proxy for calling Ollama local LLMs.
This way, the service can be protected by a API key. 

<img width="1123" height="655" alt="image" src="https://github.com/user-attachments/assets/afacde63-a4aa-4b8d-ac03-201140f8374c" />



### Project Setup

### 1. Start nginx + frontend

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

### 2. Starting the BFF

```bash
cd bff
./mvnw spring-boot:run
```

### 3. Starting the Resource server

```bash
cd resource
./mvnw spring-boot:run
```

### 3. Starting the ai-api
```bash
cd ai-api
./mvnw spring-boot:run
```

## Project Structure
<img width="785" height="456" alt="image" src="https://github.com/user-attachments/assets/c5def42f-821e-46fa-b4d6-5d107cc97199" />


