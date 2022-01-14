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
            <img src="../assets/delate.png" alt="Delate" width="20" height="20" v-on:click="eliminaInsegnamentoDocenti(insegnamentoDocenti.id,corsi.materia)">
          </div>

          <form>
            <br>
            Inserisci un nuovo insegnamento:
            <div class="form-group row">
              <label for="inputCognome" class="col-sm-2 col-form-label">Corso</label>
              <div class="col-sm-10">
                <input type="email" class="form-control"  v-model="insegnamentoDocentiCorso">
              </div>
            </div>
            <div class="form-group row">
              <div class="col-sm-10">
                <button  class="btn btn-primary" v-on:click.prevent="submitInsegnamentoDocenti(insegnamentoDocenti.id,insegnamentoDocentiCorso)" >Aggiungi insegnamento</button>
              </div>
            </div>
          </form>


        </div>
      </div>
    </div>
  </div>


  <form>
    Aggiungi un nuovo docente:
    <div class="form-group row">
      <label for="inputNome" class="col-sm-2 col-form-label">Nome</label>
      <div class="col-sm-10">
        <input type="email" class="form-control" id="inputNome" v-model="docenteNome">
      </div>
    </div>
    <div class="form-group row">
      <label for="inputCognome" class="col-sm-2 col-form-label">Cognome</label>
      <div class="col-sm-10">
        <input type="email" class="form-control" id="inputCognome" v-model="docenteCognome">
      </div>
    </div>
    <div class="form-group row">
      <div class="col-sm-10">
        <button  class="btn btn-primary" v-on:click.prevent="submitDocente" >Aggiungi docente</button>
      </div>
    </div>
  </form>





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

async function eliminaInsegnamentoDocenti(id,mat) {
  try {
    const response = await fetch("/Noodle_war/InsegnamentoDocentiSevlet", {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({docente:id,corso: mat})
    });
    if (response.status == 401) {
      window.location.href = "/Noodle_war/login";
      return [];
    }else if(response.status==200){
      this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.filter(corso=>corso.materia!=mat);
    }

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
      docenteCognome: undefined,
      insegnamentoDocentiCorso: undefined
    }},
    methods:{
      eliminaDocente,
      eliminaCorso,
      eliminaInsegnamentoDocenti,

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
      },

      submitInsegnamentoDocenti: async function(id,mat){

        try {
          const response = await fetch("/Noodle_war/InsegnamentoDocentiSevlet", {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({docente:id,corso:mat})
          });
          if(response.status == 401){
            console.log("Errore nella servlet post docente");
          }else if (response.status == 200){
            this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.push({materia:mat});
          }

        }catch (e){
          console.log(e);
        }
      }



    }
}
</script>
