<template>
  <ul>
    <li v-for="prenotazione in $store.state.miePrenotazioni" :key="prenotazione.utente + prenotazione.docente + prenotazione.giorno + prenotazione.ora">
      <div class="prenotazione">
        <div class="corso">{{prenotazione.corso}}</div>
        <div class="utente">{{prenotazione.utente}}</div>
        <div class="giorno">{{prenotazione.giorno}}</div>
        <div class="orario">{{prenotazione.orario}}</div>
      </div>

    </li>
  </ul>
</template>

<script>
async function getMiePrenotazioni(username) {
  try {
    let response;
    if(username)
      response = await fetch("/Noodle_war/PrenotazioniServlet?username=" + username);
    else
      window.location.href = "/Noodle_war/login";
    if(response.status == 401){
      window.location.href = "/Noodle_war/login";
      return [];
    }
    return await response.json();
  }catch (e){
    console.log(e);
  }
}
export default {
  name: "MiePrenotazioni",
  async created() {
    this.$store.state.miePrenotazioni = await getMiePrenotazioni(this.$route.query.username);
  },
  data(){
  }
}
</script>

<style scoped>
li{
  text-decoration: none;
}
.prenotazione{
  display: flex;
}
</style>