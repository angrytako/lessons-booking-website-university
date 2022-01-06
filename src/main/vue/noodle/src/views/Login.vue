<template>
    <div id="login-div">
        <h1 class="mb-5">Login</h1>
        <form class="bg-light">
            <div class="mb-3">
                <label class="form-label" for="username">Username</label>
                <input class="form-control" ref="username" type="text" id="username" name="username" v-model="username"
                       aria-describedby="passwordHelpBlock">
                <div id="usernameHelpBlock" ref="usernameHelpBlock" class="form-text text-danger">
                    Insert username
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="password">Password</label>
                <input class="form-control" ref="password" type="password" id="password" name="password"
                       v-model="password" aria-describedby="passwordHelpBlock">
                <div id="passwordHelpBlock" ref="passwordHelpBlock" class="form-text text-danger">
                    Insert password
                </div>
            </div>
            <div id="loginErrorBlock" ref="loginErrorBlock" class="form-text text-danger"> prop text</div>
            <button class="btn btn-primary" ref="submitBtn" v-on:click.prevent="submit">Login</button>
        </form>
    </div>
</template>

<script>
function showError(alertDivElem, inputElem, message, submitBtn) {
    alertDivElem.style.opacity = "1";
    if (message)
        alertDivElem.innerText = message;
    if (inputElem) {
        inputElem.classList.add("border");
        inputElem.classList.add("border-danger");
    }
    submitBtn.disabled = true;
    setTimeout(
        () => {
            submitBtn.disabled = false;
            alertDivElem.style.opacity = "0";
            if (inputElem) {
                inputElem.classList.remove("border");
                inputElem.classList.remove("border-danger");
            }
        }
        , 2000);
}

export default {
    name: "Login",
    data() {
        return {
            username: undefined,
            password: undefined
        }
    },
    methods: {
        submit: async function (e) {
            if (!this.username) {
                const usernameInput = this.$refs.username;
                const usernameAlertDiv = this.$refs.usernameHelpBlock;
                showError(usernameAlertDiv, usernameInput, undefined, this.$refs.submitBtn);

        return;
      }
      if(!this.password){
        const passwordInput = this.$refs.password;
        const passwordAlertDiv =  this.$refs.passwordHelpBlock;
        showError(passwordAlertDiv, passwordInput, undefined, this.$refs.submitBtn);
        return;
      }
      try {
        const response = await fetch("/Noodle_war/loginServlet", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({username: this.username, password: this.password})
        });
        const res = await response.json();
        if(!res.error) {
          this.$router.push({name:"Home"});
        }else{
          showError(this.$refs.loginErrorBlock,undefined,res.error, this.$refs.submitBtn)
        }
    }
}
</script>

<style scoped>

form {
    border-radius: 10%;
    padding: 15px;
    width: 300px;
    height: fit-content;
    text-align: center;
}

#passwordHelpBlock, #usernameHelpBlock, #loginErrorBlock {
    opacity: 0;
    transition: opacity 300ms ease-out;
}

#login-div {
    width: 100%;
    height: 85vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.mb-3 {
    text-align: start;
}

</style>