async function register() {

    const fullName =
        document.getElementById("fullName").value;

    const email =
        document.getElementById("email").value;

    const phone =
        document.getElementById("phone").value;

    const gender =
        document.getElementById("gender").value;

    const dob =
        document.getElementById("dob").value;

    const address =
        document.getElementById("address").value;

    const password =
        document.getElementById("password").value;

    const confirmPassword =
        document.getElementById("confirmPassword").value;

    const message =
        document.getElementById("message");

    if (

        password !== confirmPassword

    ) {

        message.classList.remove(
            "d-none"
        );

        message.classList.add(
            "alert-danger"
        );

        message.innerHTML =
            "Password not match";

        return;
    }

    try {

        const response = await fetch(

            API_URL + "/auth/register",

            {

                method: "POST",

                headers: {

                    "Content-Type":
                        "application/json"
                },

                body: JSON.stringify({

                    fullName,
                    email,
                    phone,
                    gender,
                    dob,
                    address,
                    password
                })
            }
        );

        const result =
            await response.text();

        if (!response.ok) {

            throw new Error(result);
        }

        message.classList.remove(
            "d-none"
        );

        message.classList.add(
            "alert-success"
        );

        message.innerHTML =
            "Register success";

        setTimeout(() => {

            window.location.href =
                "/login";

        }, 1500);

    } catch (error) {

        console.error(error);

        message.classList.remove(
            "d-none"
        );

        message.classList.add(
            "alert-danger"
        );

        message.innerHTML =
            error.message;
    }
}