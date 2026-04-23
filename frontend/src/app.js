document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL = ""; // nginx proxies requests, so relative URLs are enough

const UI_ELEMENTS = {
    loginForm: null,
    messageDiv: null,
    userInfo: null
};

async function createCisData() {
    const csrfToken = getCsrfToken();
    const response = await fetch(`${BASE_URL}/api/cisdata`, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-XSRF-TOKEN": csrfToken
        }
    });
}

async function initApp() {
    setupUi();
    showLoginForm();
    // await createCisData();
    try {
        // This request serves two purposes:
        // 1. If the user is already logged in, we get their user info
        // 2. If no CSRF cookie exists yet, Spring can generate one here
        const user = await bootstrapCsrfAndMaybeGetUser();

        hideLoginForm();
        clearDisplayMessage();
        showUserInfo(user);
        showMethods();
    } catch (error) {
        // This is the normal path for anonymous users on first page load
        showLoginForm();
        hideUserInfo();
        clearDisplayMessage();
        hideMethods();
    }
}

async function showMethods() {
    const divMethods = document.createElement("div");
    divMethods.id = "methods";
    divMethods.style.padding = "1em";
    divMethods.classList.add("containerForChat");
    divMethods.innerHTML = `
    <img class="elefantGif" src="image/elefant.gif" alt="">
  <p id="connection" class="connection"></p>
  <div id="chatContainer" class="chat-container"></div>

  <div class="input-row">
    <input type="text" id="promptInput" class="promptInput" placeholder="Ask AI about anything">
    <button id="promptBtn">Send</button>
  </div>
`;

    document.body.appendChild(divMethods);

    const connectionText = document.getElementById("connection");

    try {
        connectionText.textContent = await getConnection();
    } catch (e) {
        connectionText.textContent = `Failed to connect: ${e.message}`;
    }

    const promptBtn = document.getElementById("promptBtn");
    const promptInput = document.getElementById("promptInput");
    const responseField = document.getElementById("responseField");

    promptBtn.addEventListener("click", async () => {
        const value = promptInput.value;
        if (!value.trim()) return;

        connectionText.textContent = "";
        addMessage(value, "user");
        promptInput.value = "";

        // show loading FIRST
        const loadingMsg = addMessage("image/buildBall.gif", "ai", true);

        // wait for response
        const result = await sendPrompt(value);

        // remove loading
        loadingMsg.remove();

        // show response
        addMessage(result.response, "ai");
    });
}
function addMessage(content, type, isImage = false) {
    const chat = document.getElementById("chatContainer");

    const msg = document.createElement("div");
    msg.classList.add("message", type);

    if (isImage) {
        const img = document.createElement("img");
        img.src = content;
        img.classList.add("loadingGif");
        msg.appendChild(img);
    } else {
        msg.textContent = content;
    }

    chat.appendChild(msg);
    chat.scrollTop = chat.scrollHeight;

    return msg;
}
async function getConnection() {
    const csrfToken = getCsrfToken();

    const response = await fetch(`/api/prompt/establish`, {
        method: "GET",
        credentials: "include",
        headers: csrfToken ? { "X-XSRF-TOKEN": csrfToken } : {}
    });

    const text = await response.text();
    if (!response.ok) throw new Error(`${response.status}: ${text}`);
    return text;
}
async function sendPrompt(value) {
    const csrfToken = getCsrfToken();

    const response = await fetch(`${BASE_URL}/api/prompt`, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken
        },
        body: JSON.stringify({
            prompt: value
        })
    });

    return await response.json();
}

function hideMethods() {
    const divMethods = document.querySelector("#methods");
    if (divMethods) {
        divMethods.remove();
    }
}

function setupUi() {
    UI_ELEMENTS.loginForm = document.getElementById("login-form");
    UI_ELEMENTS.messageDiv = document.getElementById("message");
    UI_ELEMENTS.userInfo = document.getElementById("user-info");
    UI_ELEMENTS.ghLogin = document.querySelector("#gh-provider");

    UI_ELEMENTS.loginForm.addEventListener("submit", handleLogin);
    // UI_ELEMENTS.ghLogin.addEventListener("click", () => {
    //     // Redirect to the backend endpoint that starts the GitHub OAuth flow
    //     window.location.href = `${BASE_URL}/oauth2/authorization/github`;
    // })
}

async function bootstrapCsrfAndMaybeGetUser() {
    const response = await fetch(`${BASE_URL}/api/user`);

    if (response.status === 401) {
        throw new Error("User is not logged in.");
    }

    if (!response.ok) {
        throw new Error("Failed to initialize application.");
    }

    return await response.json();
}

function showUserInfo(user) {
    UI_ELEMENTS.userInfo.innerHTML = "";

    const nav = document.createElement("nav");
    nav.classList.add("top-nav");

    const left = document.createElement("div");
    left.classList.add("nav-left");
    left.textContent = "<Trace"; // optional title/logo

    const right = document.createElement("div");
    right.classList.add("nav-right");

    const username = document.createElement("span");
    username.textContent = `${user.username}`;

    const roles = document.createElement("span");
    roles.textContent = `(${user.roles.join(", ")})`;

    const logoutButton = document.createElement("button");
    logoutButton.textContent = "Logout";
    logoutButton.classList.add("logoutBtn")
    logoutButton.addEventListener("click", handleLogout);

    right.appendChild(username);
    right.appendChild(roles);
    right.appendChild(logoutButton);

    nav.appendChild(left);
    nav.appendChild(right);

    UI_ELEMENTS.userInfo.appendChild(nav);
    UI_ELEMENTS.userInfo.classList.remove("hidden");
}

function hideUserInfo() {
    UI_ELEMENTS.userInfo.innerHTML = "";
    UI_ELEMENTS.userInfo.classList.add("hidden");
}

function hideLoginForm() {
    UI_ELEMENTS.loginForm.classList.add("hidden");
}

function showLoginForm() {
    UI_ELEMENTS.loginForm.classList.remove("hidden");
    UI_ELEMENTS.loginForm.querySelector("#username").focus();
}

async function handleLogin(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const username = formData.get("username");
    const password = formData.get("password");

    try {
        const csrfToken = getCsrfToken();
        const response = await fetch(`${BASE_URL}/api/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "X-XSRF-TOKEN": csrfToken
            },
            body: new URLSearchParams({username, password})
        });

        if (!response.ok) {
            throw new Error("Login failed. Please check your credentials.");
        }

        // After login, fetch the logged-in user's details
        const user = await bootstrapCsrfAndMaybeGetUser();

        form.reset();
        setDisplayMessage("Login successful!", true);
        hideLoginForm();
        showUserInfo(user);
        showMethods();
    } catch (error) {
        setDisplayMessage(error.message, false);
    }
}

async function handleLogout() {
    try {
        const csrfToken = getCsrfToken();

        const response = await fetch(`${BASE_URL}/api/logout`, {
            method: "POST",
            headers: {
                "X-XSRF-TOKEN": csrfToken || ""
            }
        });

        if (!response.ok) {
            throw new Error("Logout failed.");
        }

        hideUserInfo();
        showLoginForm();
        setDisplayMessage("Logged out successfully.", true);
        hideMethods();

        // Optional: call /api/user again so Spring can refresh/recreate the CSRF token if needed
        try {
            await bootstrapCsrfAndMaybeGetUser();
        } catch {
            // Expected after logout because the user is anonymous
        }
    } catch (error) {
        setDisplayMessage(error.message, false);
    }
}

function setDisplayMessage(text, isSuccess) {
    UI_ELEMENTS.messageDiv.textContent = text;
    UI_ELEMENTS.messageDiv.style.color = isSuccess ? "green" : "red";
}

function clearDisplayMessage() {
    UI_ELEMENTS.messageDiv.textContent = "";
}

function getCsrfToken() {
    const match = document.cookie.match(/XSRF-TOKEN=([^;]+)/);
    return match ? decodeURIComponent(match[1]) : null;
}
