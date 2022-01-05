<template>
I Prof:
  <ul>
    <li v-for="docente in $store.state.professori" :key="docente.id + docente.nome + docente.cognome">
      <div class="docente">
        <div class="id">{{docente.id}}</div>
        <div class="nome">{{docente.nome}}</div>
        <div class="cognome">{{docente.cognome}}</div>
        <div class="elimina" onclick="eliminaDocente()">Elimina</div>
      </div>

    </li>
    <li>
      <form>
        <div>
          <label class="form-label">Aggiungi nuovo Docente:</label>
          <input class="form-control" ref="docenteNome" type="text" id="docenteNome" name="docenteNome" v-model="docenteNome" aria-describedby="passwordHelpBlock">
          <input class="form-control" ref="docenteCognome" type="text" id="docenteCognome" name="docenteCognome" v-model="docenteCognome" aria-describedby="passwordHelpBlock">
        </div>
        <button class="btn btn-primary" ref="submitBtn" v-on:click.prevent="submitDocente">Aggiungi</button>
      </form>
    </li>
  </ul>

  I Corsi:
  <ul>
    <li v-for="corso in $store.state.corsi" :key="corso.materia ">
      <div class="corso">
        <div class="id">{{corso.materia}}</div>
      </div>
    </li>
    <li>
      <form>
        <div>
          <label class="form-label"  for="corso">Aggiungi nuovo corso:</label>
          <input class="form-control" ref="corso" type="text" id="corso" name="corso" v-model="corso" aria-describedby="passwordHelpBlock">
        </div>

        <button class="btn btn-primary" ref="submitBtn" v-on:click.prevent="submitCorso">Aggiungi</button>
      </form>
    </li>
  </ul>




</template>

<script>


async function eliminaDocente(id) {
  console.log(id);
}

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
    return {
      corso: undefined,
      docenteNome: undefined,
      docenteCognome: undefined
    }},
    methods:{
      submitCorso: async function(e){
        try {
          const response = await fetch("/Noodle_war/CorsiServlet", {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({corso: this.corso})
          });
          window.location.reload();
        }catch (e){
          console.log(e);
        }
      },

      submitDocente: async function(e){
        try {
          const response = await fetch("/Noodle_war/ProfessoriServlet", {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({nome: this.docenteNome,cognome: this.docenteCognome})
          });
          window.location.reload();
        }catch (e){
          console.log(e);
        }
      }
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