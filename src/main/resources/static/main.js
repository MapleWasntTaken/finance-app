const signupbutton = document.querySelector(".signupbutton");
signupbutton.addEventListener("click",() => {
    location.href = "/login";
});
const askForCurrentRole = document.querySelector(".askForCurrentRole");
askForCurrentRole.addEventListener("click",() => {
    let y = document.createElement("p");
    fetch("/getUserRole",{
        method:"GET"
    }).then(response => response.text())
    .then(data => {y.innerText = data});
    document.body.appendChild(y);
});

//This is the function to call and store csrf in window memory
fetch("/csrf-token",{credentials:"include"})
.then(response => response.json())
.then(data => {
    window.csrfToken = data.token;
    window.csrfHeader = data.headerName;


});

//////////////////////////////////////////////////////////////



const logoutbtn = document.querySelector(".logoutbutton");
logoutbtn.addEventListener("click",() => {
    fetch("/logout", {
        method: "POST",
        credentials: "include",
        headers:{
            [window.csrfHeader]:window.csrfToken
        }
    }).then(res => {
    if (res.ok) {
        window.location.href = "/login?logout";
    }
    });
});


const testbtn = document.querySelector(".test");
testbtn.addEventListener("click",() => {
    const loginData = {
         id: "5",
         name: "fart"
    };
    fetch("/test", {
        method: "POST",
        credentials:"include",
        headers: {
            "Content-Type": "application/json",
            [window.csrfHeader]: window.csrfToken
        },
        body: JSON.stringify(loginData)
    })
    .then(response => response.text())
    .then(data => console.log("Server response:", data))
    .catch(error => console.error("Error:", error));
    fetch("/test",{
        method:"GET",
        credentials:"include",
        headers: {
            "Content-Type": "application/json",
            [window.csrfHeader]: window.csrfToken
        }
    }).then(response =>{return response.json();})
    .then(data =>{console.log(data)});
    
});