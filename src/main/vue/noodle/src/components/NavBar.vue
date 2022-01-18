<template>
  <div id="placeholder"></div>
  <nav class="navbar navbar-expand-md navbar-light bg-light fixed-top">
    <div class="container-fluid">
      <router-link class="navbar-brand" id="logo" :to="{ name:'Home' }"><img alt="logo"  src="../assets/logo.png"></router-link>
      <router-link id="appNameLogo" class="navbar-brand"  :to="{ name:'Home' }"><span>Happy learn</span></router-link>
      <!-- toggle button for smaller screens-->
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navigation-options">
        <span class="navbar-toggler-icon"></span>
      </button>
<div class="collapse navbar-collapse  justify-content-end align-center" id="navigation-options">
<ul class="navbar-nav " >
  <li class="navbar-item "><router-link   class="nav-link" :to="{ name:'Home' }">Home page</router-link></li>
  <li class="navbar-item"  v-if="$store.state.role == 'guest'"><router-link  class="nav-link" :to="{ name:'Login' }">Login</router-link></li>
  <li class="navbar-item" ></li>
  <li  class="navbar-item" v-if="$store.state.role == 'cliente'"><router-link  class="nav-link" :to="{ name:'MiePrenotazioni', query:{username: $store.state.username} }">Mie Prenotazioni</router-link></li>

  <li  class="navbar-item d-md-none d-sm-block" v-if="$store.state.role == 'amministratore'"><router-link  class="nav-link" :to="{ name:'MiePrenotazioni', query:{username: $store.state.username} }">Mie Prenotazioni</router-link></li>
  <li class="navbar-item  d-md-none d-sm-block" v-if="$store.state.role == 'amministratore'"><router-link class=" nav-link" :to="{ name:'Prenotazioni' }">Prenotazioni</router-link></li>

  <li class="navbar-item" v-bind:class="{dropdown:$store.state.role == 'amministratore'}" v-if="$store.state.role != 'guest'"></li>
  <li  class="nav-item dropdown d-sm-none d-md-block"  v-if="$store.state.role == 'amministratore'">
    <div class="nav-link dropdown-toggle"   id="navbarDropdownMenuLink"  role="button"  data-bs-toggle="dropdown"  aria-expanded="false">
      Prenotazioni
    </div>
    <ul class="dropdown-menu"  id="my-dropdown" aria-labelledby="navbarDropdownMenuLink">
       <li><router-link class="dropdown-item nav-link" :to="{ name:'MiePrenotazioni', query:{username: $store.state.username} }">Mie Prenotazioni</router-link></li>
      <li><router-link class="dropdown-item  nav-link" :to="{ name:'Prenotazioni' }">Prenotazioni</router-link></li>
    </ul>
  </li>

  <li class="navbar-item" v-if="$store.state.role == 'amministratore'"><router-link class="nav-link" :to="{ name:'Docenti' }">Docenti</router-link></li>
  <li class="navbar-item" v-if="$store.state.role == 'amministratore'"><router-link class="nav-link" :to="{ name:'Corsi' }">Corsi</router-link></li>
  <li class="navbar-item" v-if="$store.state.role != 'guest'"><router-link class="nav-link" :to="{ name:'Logout' }">Logout</router-link></li>
</ul>
</div>
    </div>

  </nav>
</template>
<script>
export default {
  name: "NavBar"
}
</script>

<style scoped>
#placeholder{
  width: 100%;
  height: 50px;
  opacity: 0;
}
/*ul{*/
/*  margin: 0;*/
/*  display: flex;*/
/*  height: 50px;*/
/*  background-color: #203a50;*/
/*  align-content: center;*/
/*  justify-content: flex-end;*/
/*}*/
/*li{*/
/*  text-align: center;*/
/*  font-size: 1.3em;*/
/*  margin: 0 20px;*/
/*}*/
/*a{*/
/*  display: flex;*/
/*  align-items: center;*/
/*  justify-content: center;*/
/*  height: 100%;*/
/*  width: fit-content;*/
/*  text-decoration: none;*/
/*  color: white;*/
/*}*/
/*a:hover{*/
/*  color: #ffdbdb;*/
/*}*/
.router-link-active {
  text-decoration: underline;
  font-weight: bold;

}

/*}*/
#logo{
  width: 50px;
  height: 50px;
}
#logo>img{
  width: 100%;
  height: 100%;
}
#appNameLogo{
  text-decoration: none;
  font-weight: inherit;
}
</style>