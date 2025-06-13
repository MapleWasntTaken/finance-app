console.log("fart");
const form = document.querySelector(".formclass");
const signin = document.querySelector(".signinbutton");
fetch("/csrf-token", { credentials: "include" })
  .then(res => res.json())
  .then(data => {
      window.csrfToken = data.token;
      window.csrfHeader = data.headerName;
  });
document.addEventListener("DOMContentLoaded",()=>{
    form.addEventListener("submit",(e)=>{
        e.preventDefault();
        const formdata = new FormData(form);
        fetch("/login",{
            method: "POST",
            body: new URLSearchParams(formdata),
            headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            [window.csrfHeader]: window.csrfToken
            },
            credentials:"include"
            })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                console.log("Login succeeded");
            } else {
                console.warn("Login failed", response.status);
            }
            });

    });
    signin.addEventListener("click",()=>{
        location.href = "/signup";
    });
});
