<template>
  <div id="login-div">
    <h1>Login</h1>
    <form>
      <label for="username">Username</label>
      <input type="text" id="username" name="username" v-model="username">
      <label for="password">Password</label>
      <input type="password" id="password" name="password" v-model="password">
      <button class="btn btn-primary" v-on:click.prevent="submit">Login</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return {
      username: undefined,
      password: undefined
    }},
  methods:{
    submit: async function(e){
        const response = await fetch("/Noodle_war//loginServlet", {
          method: 'POST', // *GET, POST, PUT, DELETE, etc.
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({username:this.username, password:this.password})
        });
        const res = await response.json();
        console.log(this.username,this.password);
    }
  }
}
</script>

<style scoped>
#login-div{
  height: 80vh;
  width: 90vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
label{
  display: block;
  margin-bottom: 10px;

}

input{
  display: block;
  margin-bottom: 30px;
}

</style>