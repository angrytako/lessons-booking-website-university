<template>
<ul>
  <router-link id="logo" :to="{ name:'Home' }"><img alt="logo"  src="../assets/logo.png"></router-link>
  <li><router-link :to="{ name:'Home' }">Home page</router-link></li>
  <li v-if="$store.state.role == 'guest'"><router-link :to="{ name:'Login' }">Login</router-link></li>
  <li v-if="$store.state.role != 'guest'"><router-link :to="{ name:'MiePrenotazioni', query:{username: $store.state.username} }">Mie Prenotazioni</router-link></li>
  <li v-if="$store.state.role == 'amministratore'"><router-link :to="{ name:'Prenotazioni' }">Prenotazioni</router-link></li>
  <li v-if="$store.state.role != 'guest'"><router-link :to="{ name:'Logout' }">Logout</router-link></li>
</ul>
</template>

<script>
export default {
  name: "NavBar"
}
</script>

<style scoped>

ul{
  margin: 0;
  display: flex;
  height: 50px;
  background-color: #203a50;
  align-content: center;
  justify-content: flex-end;
}
li{
  text-align: center;
  font-size: 1.3em;
  margin: 0 20px;
}
a{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: fit-content;
  text-decoration: none;
  color: white;
}
a:hover{
  color: #ffdbdb;
}
.router-link-active{
  text-decoration: underline;

}
#logo{
  width: 50px;
  height: 50px;
  margin-right: auto;
  margin-left: 20px;
}
#logo>img{
  width: 100%;
  height: 100%;
}
</style>