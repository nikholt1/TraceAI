document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL = ""; // nginx proxies requests, so relative URLs are enough

const UI_ELEMENTS = {
    loginForm: null,
    messageDiv: null,
    userInfo: null
};

async function initApp() {
    setupUi();
    showLoginForm();

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

function showMethods() {
    const divMethods = document.createElement("div");
    divMethods.id = "methods";
    divMethods.style.padding = "1em";
    divMethods.classList.add("container");
    divMethods.innerHTML = `
        <h2>Available API Methods:</h2>
        <ul>
            <li><a href="/api/user"><code>GET /api/user</code></a></li>
            <li><a href="/api/protected"><code>GET /api/protected</code></a></li>
        </ul>
    `;
    document.querySelector("body").appendChild(divMethods);
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

    const article = document.createElement("article");
    article.style.padding = "1em";

    const usernamePtag = document.createElement("p");
    usernamePtag.textContent = `Logged in as: ${user.username}`;

    const rolesPtag = document.createElement("p");
    rolesPtag.textContent = `Roles: ${user.roles.join(", ")}`;

    const logoutButton = document.createElement("button");
    logoutButton.textContent = "Logout";
    logoutButton.addEventListener("click", handleLogout);

    article.appendChild(usernamePtag);
    article.appendChild(rolesPtag);
    article.appendChild(logoutButton);
    UI_ELEMENTS.userInfo.appendChild(article);

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
