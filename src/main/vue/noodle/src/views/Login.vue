<template>
  <div>
    <h1>Login</h1>
    <form>
      <label for="username">Username</label>
      <input type="text" id="username" name="username">
      <label for="password">Password</label>
      <input type="password" id="password" name="password">
      <button>Login</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "Login"
}
</script>

<style scoped>
div{
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