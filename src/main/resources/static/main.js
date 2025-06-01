const signupbutton = document.querySelector(".signupbutton");
signupbutton.addEventListener("click",() => {
    location.href = "/login";
});
const askForCurrentRole = document.querySelector(".askForCurrentRole");
askForCurrentRole.addEventListener("click",() => {
    let y = document.createElement("p");
    let x = fetch("/getUserRole").then(response => response.text()).then(data => y.innerText = data);
    document.body.appendChild(y);
});