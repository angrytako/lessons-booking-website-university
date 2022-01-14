<template>
I Docenti:
  <div class="row">
    <div class="col-4">
      <div class="list-group" id="list-tab" role="tablist">
        <a v-for="docente in $store.state.professori" :key="docente.id + docente.nome + docente.cognome"
           class="list-group-item list-group-item-action" v-bind:id="'docente-list-'+docente.id" data-bs-toggle="list"
           v-bind:href="'#docente'+docente.id" role="tab" v-bind:aria-controls="'docente'+docente.id">
          {{docente.id}} {{docente.nome}} {{docente.cognome}}</a>
      </div>
    </div>
    <div class="col-8">
      <div class="tab-content" id="nav-tabContent">
        <div v-for="insegnamentoDocenti in $store.state.insegnamentoDocenti" :key="insegnamentoDocenti.id + insegnamentoDocenti.corsi"
            class="tab-pane fade" v-bind:id="'docente'+insegnamentoDocenti.id"  role="tabpanel" v-bind:aria-labelledby="'docente-list-'+insegnamentoDocenti.id">
          Professore: {{insegnamentoDocenti.id}}. <div class="pe-auto" v-on:click="eliminaDocente(insegnamentoDocenti.id)">Elimina Docente</div>
          Questi sono i corsi in cui insegna:
          <div v-for="corsi in insegnamentoDocenti.corsi" :key="corsi.materia">
            ~ {{corsi.materia}}
          </div>
        </div>
      </div>
    </div>
  </div>



  <ul>
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
        <div class="pe-auto" v-on:click="eliminaCorso(corso.materia)">Elimina</div>
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
  try {
    const response = await fetch("/Noodle_war/ProfessoriServlet", {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({docente: id})
    });
    if (response.status == 401) {
      window.location.href = "/Noodle_war/login";
      return [];
    }
    this.$store.state.professori=this.$store.state.professori.filter(professore=>professore.id!=id);
    this.$store.state.insegnamentoDocenti=this.$store.state.insegnamentoDocenti.filter(insegnamentoDocenti=>insegnamentoDocenti.id!=id);


    // window.location.reload();
  }catch (e){
    console.log(e);
  }
}
async function eliminaCorso(materia) {
  try {
    const response = await fetch("/Noodle_war/CorsiServlet", {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({corso: materia})
    });
    if (response.status == 401) {
      window.location.href = "/Noodle_war/login";
      return [];
    }
    this.$store.state.corsi=this.$store.state.corsi.filter(corso=>corso.materia!=materia);

    // window.location.reload();
  }catch (e){
    console.log(e);
  }
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

async function getInsegnamentoDocenti() {
  try {
    const response = await fetch("/Noodle_war/InsegnamentoDocentiSevlet");

    if (response.status == 401) {
      window.location.href = "/Noodle_war/login";
      return [];
    }
    return await response.json();
  } catch (e) {
    console.log(e);
  }
}


  export default {
  name: "Professori",
  async created() {
    this.$store.state.professori = await getProfessori();
    this.$store.state.corsi = await getCorsi();
    this.$store.state.insegnamentoDocenti = await getInsegnamentoDocenti();
  },
  data(){
    return {
      corso: undefined,
      docenteNome: undefined,
      docenteCognome: undefined
    }},
    methods:{
      eliminaDocente,
      eliminaCorso,

      submitCorso: async function(e){
        try {
          const response = await fetch("/Noodle_war/CorsiServlet", {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({corso: this.corso})
          });
          if(response.status == 401){
            console.log("Errore nella servlet post corso");
          }else {
            this.$store.state.corsi.push({materia: this.corso});
          }
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
          if(response.status == 401){
            console.log("Errore nella servlet post docente");
          }else{
            const docenteResponse = await response.json();
            this.$store.state.professori.push({id:docenteResponse.id, nome: this.docenteNome,cognome: this.docenteCognome});
            this.$store.state.insegnamentoDocenti.push({id:docenteResponse.id})
          }



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