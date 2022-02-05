<template>
  <NavBar />
  <router-view/>
<!--	TODO: footer coi nostri nomi :) -->
<!--	footer-->
</template>

<script>

import NavBar from "@/components/NavBar";
async function getUserInfo() {
  try {
    const response = await fetch("/Noodle_war/MyInfoServlet");
    return await response.json();
  }catch (e){
    console.log(e);
  }

}
export default {
  components: {NavBar},
  async created() {
    let userInfo;
    try {
      userInfo = await getUserInfo();
    } catch (e){
      console.log(e);
      return;
    }
    try {
      if(userInfo) {
        this.$store.state.role = userInfo.role ? userInfo.role : "guest";
        this.$store.state.username = userInfo.username ? userInfo.username : undefined;
      }
    }catch (e){
      console.log(e);
    }
  }
}

</script>

<style>
*{
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
a{
  text-decoration: none;
  color: white;
}
</style>