fetch("/csrf-token", { credentials: "include" })
  .then(res => res.json())
  .then(data => {
      window.csrfToken = data.token;
      window.csrfHeader = data.headerName;
  });
const form =document.querySelector(".formclass");
document.addEventListener("DOMContentLoaded",()=>{
    form.addEventListener("submit",(e)=>{
        e.preventDefault();
        const formdata =  e.target;
        const dataf = {
            email: formdata.username.value,
            password: formdata.password.value
        }
        console.log(dataf);

        let error = false;
        fetch("/signup",{
            method:"POST",
            body: JSON.stringify(dataf),
            headers: {
                "Content-Type": "application/json",
                [window.csrfHeader]: window.csrfToken
            },
            credentials:"include"

        })
        .then(response => {
            if(!(response.status === 200)){error = true;}
            return response.text();
        })
        .then(data =>{
            if(error){
                let x = document.createElement("p");
                x.text = data;
                x.textContent = data;
                form.appendChild(x);
                console.log("here");
            }else{
                location.href="/login";
                console.log("valid to signup");
            }
        });
    });
    
});
