<template>
  <NavBar :user-type="userType" />
  <router-view/>
</template>

<script>

import NavBar from "@/components/NavBar";
async function getUserInfo() {
  try {
    const response = await fetch("/Noodle_war/MyInfoServlet");
    console.log(response);
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
      console.log(userInfo);
      try {
        if(userInfo)
          this.userType = userInfo.role ? userInfo.role : undefined;
      }catch (e){
        console.log(e);
      }
    },
  data(){
    return {
      userType: undefined
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
