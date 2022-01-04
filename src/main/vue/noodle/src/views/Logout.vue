<template>
<div></div>
</template>

<script>
export default {
  name: "Logout",
  async mounted() {
    try {
      await fetch("/Noodle_war/LogoutServlet");
      this.$store.logout();
    }
    catch (e){
      console.log(e);
    }
    this.$router.push({name:"Home"});
//    window.location.replace("/Noodle_war/");
  }
}
</script>

<style scoped>

</style>