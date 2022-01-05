<template>
I Prof:
  <ul>
    <li v-for="docente in $store.state.professori" :key="docente.id + docente.nome + docente.cognome">
      <div class="docente">
        <div class="id">{{docente.id}}</div>
        <div class="nome">{{docente.nome}}</div>
        <div class="cognome">{{docente.cognome}}</div>
      </div>

    </li>
  </ul>

  I Corsi:
  <ul>
    <li v-for="corso in $store.state.corsi" :key="corso.materia ">
      <div class="corso">
        <div class="id">{{corso.materia}}</div>
      </div>

    </li>
  </ul>




</template>

<script>
async function getProfessori() {
  try {
    const response = await fetch("/Noodle_war/ProfessoriServlet");
    if (response.status == 401) {
      window.location.href = "/Noodle_war/login";
      return [];
    }
    return await response.json();
  } catch (e) {
    console.log(e);
  }
}

  async function getCorsi() {
    try {
      const response = await fetch("/Noodle_war/CorsiServlet");
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
  name: "Professori",
  async created() {
    this.$store.state.professori = await getProfessori();
    this.$store.state.corsi = await getCorsi();
  },
  data(){
}
}
</script>

<style scoped>
li{
  text-decoration: none;
}
.docente{
  display: flex;
}
.corso{
  display: flex;
}
</style>