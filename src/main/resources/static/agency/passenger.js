document.getElementById("visa-mastercard").addEventListener("click", function() {
    document.querySelector(".logo-visa-mastercard").style.display = "block"
    document.querySelector(".logo-atm").style.display = "none"
})
document.getElementById("atm").addEventListener("click", function() {
    document.querySelector(".logo-atm").style.display = "block"
    document.querySelector(".logo-visa-mastercard").style.display = "none"
})

document.getElementById("submit").addEventListener("click", function () {
    var namecontact = document.getElementById("namecontact").value
    var phonecontact = document.getElementById("phonecontact").value
    var email = document.getElementById("email").value
    var name = document.getElementById("name").value
    var birth = document.getElementById("birth").value
    var gener = document.getElementById("gener").value
    fetch("http://localhost:8080/flight/passengerinfo", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + getCookie("token")
        },
        body: JSON.stringify({
            "name": namecontact,
            "email": email,
            "phoneNumber": phonecontact,
            "totalPrice": price,
            "flightNumber": "Hn",
            "status": "ok",
            "customers": [{
                "name": name,
                "Age": 1,
                "sex": gener,
                "address": "12",
                "phoneNumber": "12",
                "ticket": {
                    "seatNumber": "12F",
                    "seatClass": "Phổ thông",
                    "flightNumber": "Hn"
                }
            }]
        })
    }).then(function (response) {
        if(response.status == 200) {
            return response.text()
        }
    }).then(function (result) {
        window.location.href = result
    })
})
function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
