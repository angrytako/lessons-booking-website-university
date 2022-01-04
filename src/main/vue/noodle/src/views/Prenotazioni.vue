<template>
<ul>
  <li v-for="prenotazione in $store.state.prenotazioni" :key="prenotazione.utente + prenotazione.docente + prenotazione.giorno + prenotazione.ora">
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
async function getPrenotazioni() {
  try {
    const response = await fetch("/Noodle_war/PrenotazioniServlet");
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
    this.$store.state.prenotazioni = await getPrenotazioni();
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