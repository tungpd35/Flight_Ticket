document.getElementById("submit-btn").addEventListener("click", function () {
    let email = document.getElementById("email")
    let pass = document.getElementById("password")
    let repass = document.getElementById("repass")

    let mess_email = document.getElementById("email-mess-error")
    let mess_pass = document.getElementById("pass-mess-error")
    let mess_repass = document.getElementById("reEmail-mess-error")
    mess_email.style.display = "none"
    mess_pass.style.display = "none"
    mess_repass.style.display = "none"

    console.log(pass.value)
    if (pass.value === "" || email.value === "") {
        if(pass.value === "") {
            mess_pass.innerText = "Không được để trống"
            mess_pass.style.display = "block"
        }
        if (email.value === "") {
            mess_email.innerText = "Không được để trống"
            mess_email.style.display = "block"
        }
        if(repass.value === "") {
            mess_repass.innerText = "Không được để trống"
            mess_repass.style.display = "block"
        }
    } else if(validatePassword(pass.value) === false) {
        mess_pass.innerText = "Mật khẩu phải >= 8 ký tự, bao gồm cả chữ cái và chữ số"
        mess_pass.style.display = "block"
    } else if (pass.value !== repass.value) {
        mess_repass.innerText = "Mật khẩu không khớp"
        mess_repass.style.display = "block"
    } else {
        console.log(validatePassword(pass.value))
        if(validateEmail(email.value)===null) {
                    mess_email.innerText = "Email không đúng định dạng"
                    mess_email.style.display = "block"
                } else {
                    fetch("/register", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            "email": email.value,
                            "password": pass.value
                        })
                    }).then(function (response) {
                        if (response.status === 200) {
                            console.log("oke")
                            window.location.href = "/register/confirm"
                        } else {
                            mess_email.innerText = "Email đã tồn tại"
                            mess_email.style.display = "block"
                        }
                    })

                }
        }
})
const validateEmail = (email) => {
    return String(email)
        .toLowerCase()
        .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
};
function validatePassword(pw) {
    return (/[a-z]/       .test(pw) || /[A-Z]/.test(pw)) &&
        /[0-9]/       .test(pw) &&
        pw.length >= 8;
}