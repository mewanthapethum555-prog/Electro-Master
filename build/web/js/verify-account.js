async function VerifyAccount(){
    const verificationCode = document.getElementById("verificationCode").value;
    
    const verification = {
        verificationCode: verificationCode
    };
    
    const verificationJson = JSON.stringify(verification);
    
    const response = await fetch(
        "VerifyAccount",
        {
            method:"POST",
            body:verificationJson,
            header:{
                "Content-Type":"application/json"
            }
        }
    );
    
    if(response.ok){
        const json = await response.json();
        
        if(json.status){//if true
            window.location = "index.html";
        }else{//when status false
            
            if(json.message === "1") { // Email not found.
                window.location = "signin.html";
            }else{
                 document.getElementById("message").innerHTML = json.message;
            }
        }    
    }else{
        document.getElementById("message").innerHTML = "Verification failed.Please try again";
    }
}


