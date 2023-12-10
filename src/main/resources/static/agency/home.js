var destination = null
var departure = null
document.getElementById("close-departure-icon").addEventListener("click", function() {
    document.getElementById("departure-choose").style.display = "none"
})
document.getElementById("input-departure").addEventListener("click", function() {
    document.getElementById("departure-choose").style.display = "block"
})
document.getElementById("close-destination-icon").addEventListener("click", function() {
    document.getElementById("destination-choose").style.display = "none"
})
document.getElementById("destination-input").addEventListener("click", function() {
    document.getElementById("destination-choose").style.display = "block"
})
document.querySelectorAll(".destination-item").forEach(function(ele) {
    ele.addEventListener("click", function() {
        document.getElementById("destination-choose").style.display = "none"
        document.getElementById("destination-input").value = this.innerText
    })
})
document.querySelectorAll(".departure-item").forEach(function(ele) {
    ele.addEventListener("click", function() {
        document.getElementById("departure-choose").style.display = "none"
        document.getElementById("input-departure").value = this.innerText
    })
})
document.getElementById("two-way").addEventListener("click", function() {
    document.getElementById("date-return").removeAttribute("readonly")
})
document.getElementById("one-way").addEventListener("click", function() {
    document.getElementById("date-return").readOnly = "true"
})
document.querySelectorAll(".departure-item").forEach(function(e) {
    e.addEventListener("click",function () {
        departure=e.getAttribute("value")
        console.log(departure)
    })
})
document.querySelectorAll(".destination-item").forEach(function(e) {
    e.addEventListener("click",function () {
        destination=e.getAttribute("value")
        console.log(destination)
    })
})
document.getElementById("submit").addEventListener("click", function() {
    var date = document.getElementById("datedeparture").value
    var adt = document.getElementById("ADT").value
    var chd = document.getElementById("CHD").value
    console.log(adt + chd)
    window.location.href = "/agency/flight/search?date=" + date + "&departure=" + departure+ "&arrival=" + destination + "&ADT=" + adt  + "&CHD=" + chd
})
