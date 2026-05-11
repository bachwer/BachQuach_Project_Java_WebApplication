async function getMe() {

    const token = getToken();

    if (!token) {

        window.location.href =
            "/login";

        return;
    }

    try {

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

        if (!response.ok) {

            logout();

            return;
        }

        const data =
            await response.json();

        console.log(data);

        document.getElementById("fullName")
            .innerHTML = data.fullName;

        document.getElementById("email")
            .innerHTML = data.email;

        document.getElementById("phone")
            .innerHTML = data.phone;

        document.getElementById("gender")
            .innerHTML = data.gender;

        document.getElementById("dob")
            .innerHTML = data.dob;

        document.getElementById("address")
            .innerHTML = data.address;

    } catch (error) {

        console.error(error);

        logout();
    }
}



async function checkLogin() {

    const me =
        await fetchMe();

    // CHƯA LOGIN

    if (!me) {

        return;
    }

    // ADMIN

    if (
        me.roles.includes(
            "ADMIN"
        )
    ) {

        window.location.href =
            "/admin/dashboard";
    }

    // STUDENT

    else if (

        me.roles.includes(
            "STUDENT"
        )
    ) {

        window.location.href =
            "/student/dashboard";
    }

    // LECTURER

    else if (

        me.roles.includes(
            "ROLE_LECTURER"
        )
    ) {

        window.location.href =
            "/lecturer/dashboard";
    }
}