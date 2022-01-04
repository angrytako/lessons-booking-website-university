<template>
    <div></div>
</template>

<script>
export default {
    name: "Logout",
    async mounted() {
        try {
            await fetch("/Noodle_war/LogoutServlet");
        } catch (e) {
            console.log(e);
        }
        window.location.replace("/Noodle_war/");
    }
}
</script>

<style scoped>

</style>