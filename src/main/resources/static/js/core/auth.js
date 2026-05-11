// SAVE TOKEN

function saveToken(token) {

    localStorage.setItem(
        "token",
        token
    );
}

// GET TOKEN

function getToken() {

    return localStorage.getItem(
        "token"
    );
}

// REMOVE TOKEN

function removeToken() {

    localStorage.removeItem(
        "token"
    );
}

// LOGOUT

function logout() {

    removeToken();

    window.location.href =
        "/login";
}