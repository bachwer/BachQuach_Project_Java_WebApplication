window.addEventListener(

    "DOMContentLoaded",

    async () => {

        // ONLY RUN ON LOGIN PAGE

        if (
            window.location.pathname !== "/login"
        ) {
            return;
        }

        const token = getToken();

        // NO TOKEN

        if (!token) {
            return;
        }

        try {

            // VERIFY TOKEN

            const response = await fetch(

                API_URL + "/auth/me",

                {

                    method: "GET",

                    headers: {

                        "Authorization":
                            "Bearer " + token
                    }
                }
            );

            // TOKEN INVALID

            if (!response.ok) {

                removeToken();

                return;
            }

            // USER INFO

            const me =
                await response.json();

            console.log("ME:", me);

            // NORMALIZE ROLES

            const roles =

                (me.roles || []).map(role => {

                    if (
                        typeof role === "string"
                    ) {
                        return role;
                    }

                    return role.name;
                });

            console.log("AUTO LOGIN ROLES:", roles);

            redirectByRole(roles);

        } catch (error) {

            console.error(error);

            removeToken();
        }
    }
);

// LOGIN

async function login(event) {

    if (event) {

        event.preventDefault();
    }

    const email =
        document.getElementById("email").value;

    const password =
        document.getElementById("password").value;

    const message =
        document.getElementById("message");

    try {

        // LOGIN API

        const response = await fetch(

            API_URL + "/auth/login",

            {

                method: "POST",

                headers: {

                    "Content-Type":
                        "application/json"
                },

                body: JSON.stringify({

                    email: email,

                    password: password
                })
            }
        );

        // LOGIN FAIL

        if (!response.ok) {

            throw new Error(
                "Invalid email or password"
            );
        }

        // RESPONSE JSON

        const data =
            await response.json();

        console.log("LOGIN:", data);

        // SAVE TOKEN

        saveToken(data.token);

        // GET CURRENT USER

        const meResponse = await fetch(

            API_URL + "/auth/me",

            {

                method: "GET",

                headers: {

                    "Authorization":
                        "Bearer " + data.token
                }
            }
        );

        // TOKEN INVALID

        if (!meResponse.ok) {

            throw new Error(
                "Cannot get user info"
            );
        }

        // USER DATA

        const me =
            await meResponse.json();

        console.log("ME:", me);

        // SUCCESS MESSAGE

        message.classList.remove(
            "hidden"
        );

        message.classList.remove(
            "alert-danger"
        );

        message.className =
            "rounded-2xl px-4 py-3 text-sm font-bold bg-green-100 text-green-700";

        message.innerHTML =
            "Login success";

        // NORMALIZE ROLES

        const roles =

            (me.roles || []).map(role => {

                if (
                    typeof role === "string"
                ) {
                    return role;
                }

                return role.name;
            });

        console.log("LOGIN ROLES:", roles);

        // REDIRECT

        redirectByRole(roles);

    } catch (error) {

        console.error(error);

        // ERROR MESSAGE

        message.classList.remove(
            "d-none"
        );

        message.classList.remove(
            "alert-success"
        );

        message.classList.add(
            "alert-danger"
        );

        message.className =
            "rounded-2xl px-4 py-3 text-sm font-bold bg-red-100 text-red-700";
    }
}

// REDIRECT BY ROLE

function redirectByRole(roles) {

    console.log("REDIRECT:", roles);

    // ADMIN

    if (
        roles.includes("ADMIN")
    ) {

        console.log(
            "GO ADMIN"
        );

        window.location.assign(
            "/admin/dashboard"
        );

        return;
    }

    // STUDENT

    if (
        roles.includes("STUDENT")
    ) {

        console.log(
            "GO STUDENT"
        );

        window.location.assign(
            "/student/dashboard"
        );

        return;
    }

    // LECTURER

    if (
        roles.includes("LECTURER")
    ) {

        console.log(
            "GO LECTURER"
        );

        window.location.assign(
            "/lecturer/dashboard"
        );

        return;
    }

    // DEFAULT

    window.location.assign("/");
}

// TOKEN

function saveToken(token) {

    localStorage.setItem(
        "token",
        token
    );
}

function getToken() {

    return localStorage.getItem(
        "token"
    );
}

function removeToken() {

    localStorage.removeItem(
        "token"
    );
}