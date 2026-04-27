async function signUp(){
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const user = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password
    };

    const userJson = JSON.stringify(user);

    const response = await fetch(
            "SignUp",
            {
                method: "POST",
                body: userJson,
                header: {
                    "Content-type": "application/json"
                }
                
            }
    );
    
    if(response.ok){   //success
        const json = await response.json();
        if(json.status){ //if true
            window.location = "account-verification.html";
        }else{
            document.getElementById("message").innerHTML= json.message;
        }
    }else{
        document.getElementById("message").innerHTML="Registraiton failed. Please try again";
    }


}





