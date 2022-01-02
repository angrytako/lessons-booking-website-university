<template>
    <NavBar :user-type="userType" />
    <router-view/>
</template>

<script>
    import NavBar from "@/components/NavBar";
    export default {
        components: {NavBar},
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
