<template>
</template>

<script>
	function logout() {
		this.$store.state.username = undefined;
		this.$store.state.role = "guest";
		this.$store.state.miePrenotazioni = undefined;
		this.$store.state.prenotazioni = undefined;
		this.$store.state.professori = undefined;
		this.$store.state.corsi = undefined;
	}

	export default {
		name: "Logout",
		async mounted() {
			try {
				await fetch("/Noodle_war/LogoutServlet");
				logout();
			} catch (e) {
				console.log(e);
			}
			this.$store.state.username = undefined;
			this.$store.state.role = "guest";
			this.$router.push({name: "Home"});
		}
	}
</script>

<style scoped>

</style>
