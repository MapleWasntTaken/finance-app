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

    fetch("/test", {
        method: "POST",
        credentials:"include",
        headers: {
            "Content-Type": "application/json",
            [window.csrfHeader]: window.csrfToken
        }
    })
    
});

const linkbtn = document.querySelector(".institutionlink");
linkbtn.addEventListener("click",()=>{
    fetch("/api/plaid/create-link-token",{
        method:"POST",
        credentials:"include",
        headers: {
            "Content-Type": "application/json",
            [window.csrfHeader]: window.csrfToken
        }
    })
    .then(res => res.json())
    .then(data =>{
        const linkToken = data.link_token;

        const handler = Plaid.create({
        token: linkToken,
        onSuccess: function(public_token, metadata) {
            // ⬇️ Step 2: Send this public_token to your backend
            fetch("/api/plaid/exchange-public-token", {
                method: "POST",
                headers: { "Content-Type": "application/json",[window.csrfHeader]: window.csrfToken },
                body: JSON.stringify({ public_token,
                    institution_id: metadata.institution?.institution_id,
                    institution_name: metadata.institution?.name
                })
            })
            .then(res => res.json())
            .then(data => {
                console.log("Access token exchange response:", data);
            });
        },
        onExit: function(err, metadata) {
            console.warn("User exited Plaid Link", err, metadata);
        }
        });

        handler.open();

    });

})

const transbtn = document.querySelector(".transgrab");
transbtn.addEventListener("click",()=>{

    fetch("/api/plaid/refresh-transactions",{
        method:"POST",
        credentials:"include",
        headers: {
            "Content-Type": "application/json",
            [window.csrfHeader]: window.csrfToken
        }
    })
    .then(res => res.json())
    .then(data =>{
        console.log(data);
    })
})