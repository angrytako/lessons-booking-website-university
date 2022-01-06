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
    this.$store.state.username = undefined;
    this.$store.state.role = "guest";
    this.$router.push({name:"Home"});
  }
}
</script>

<style scoped>

</style>