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
            <img src="../assets/delate.png" alt="Delate" width="20" height="20" v-on:click="eliminaInsegnamento(insegnamentoDocenti.id,corsi.materia)">
          </div>

          <form>
            <br>
            Inserisci un nuovo insegnamento:
            <div class="form-group row">
              <label for="inputCognome" class="col-sm-2 col-form-label">Corso:</label>
              <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" v-bind:id="'insegnamentoDocentiCorso' +insegnamentoDocenti.id">
                  <option  v-for="corso in $store.state.corsi" :key="corso.materia" v-bind:value=corso.materia>{{corso.materia}}</option>
                </select>
              </div>
            </div>
            <div class="form-group row">
              <div class="col-sm-10">
                <button  class="btn btn-primary" v-on:click.prevent="submitInsegnamentoDocenti(insegnamentoDocenti.id)" >Aggiungi insegnamento</button>
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


  --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

  <br>
  I Corsi:
  <div class="row">
    <div class="col-4">
      <div class="list-group" id="list-tab-corsi" role="tablist">
        <a v-for="corso in $store.state.corsi" :key="corso.materia "
           class="list-group-item list-group-item-action" v-bind:id="'corso-list-'+corso.materia" data-bs-toggle="list"
           v-bind:href="'#corso-'+corso.materia" role="tab" v-bind:aria-controls="'corso-'+corso.materia">
          {{corso.materia}}</a>
      </div>
    </div>
    <div class="col-8">
      <div class="tab-content" id="nav-tabContent-corsi">
        <div v-for="insegnamentoCorsi in $store.state.insegnamentoCorsi" :key="insegnamentoCorsi.corso + insegnamentoCorsi.docenti"
             class="tab-pane fade" v-bind:id="'corso-'+insegnamentoCorsi.corso"  role="tabpanel" v-bind:aria-labelledby="'corso-list-'+insegnamentoCorsi.corso">
          Corso: {{insegnamentoCorsi.corso}}. <div class="pe-auto" v-on:click="eliminaCorso(insegnamentoCorsi.corso)">Elimina Corso</div>
          Professori che insegnano questo corso:
          <div v-for="docenti in insegnamentoCorsi.docenti" :key="docenti.id">
            ~ {{docenti.id}} {{docenti.nome}} {{docenti.cognome}}
            <img src="../assets/delate.png" alt="Delate" width="20" height="20" v-on:click="eliminaInsegnamento(insegnamentoCorsi.corso,docenti.id)">
          </div>

          <form>
            <br>
            Inserisci un nuovo insegnamento:
            <div class="form-group row">
              <label for="inputNome" class="col-sm-2 col-form-label">Professore </label>
              <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" v-bind:id="'insegnamentoCorsiDocente' +insegnamentoCorsi.corso">
                  <option  v-for="docente in $store.state.professori" :key="docente.id" v-bind:value=docente.id >{{docente.id}} {{docente.nome}} {{docente.cognome}}</option>
                </select>
              </div>
            </div>
            <div class="form-group row">
              <div class="col-sm-10">
                <button  class="btn btn-primary" v-on:click.prevent="submitInsegnamentoCorsi(insegnamentoCorsi.corso)" >Aggiungi Docente</button>
              </div>
            </div>
          </form>

        </div>
      </div>
    </div>
  </div>


  <form>
    Aggiungi un nuovo corso:
    <div class="form-group row">
      <label for="inputCognome" class="col-sm-2 col-form-label">Corso:</label>
      <div class="col-sm-10">
        <input type="email" class="form-control" id="inputCorso" v-model="corso">
      </div>
    </div>
    <div class="form-group row">
      <div class="col-sm-10">
        <button  class="btn btn-primary" v-on:click.prevent="submitCorso" >Aggiungi Corso</button>
      </div>
    </div>
  </form>






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

async function eliminaInsegnamento(id,mat) {
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

      this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi =
          this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.filter(corsi=> corsi.materia!=mat);

      this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.materia == mat).docenti =
          this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.materia == mat).docenti.filter(docenti=> docenti.id!=id);
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

async function getInsegnamentoCorsi() {
  try {
    const response = await fetch("/Noodle_war/InsegnamentoCorsiSevlet");

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
    this.$store.state.insegnamentoCorsi = await getInsegnamentoCorsi();
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
      eliminaInsegnamento,

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

      submitInsegnamentoDocenti: async function(id){
        var mat = document.getElementById("insegnamentoDocentiCorso"+ id);
        mat=mat.options[mat.selectedIndex].value;

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
            this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.push({materia:mat , rimosso:false});
            this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.materia == mat).docenti.push({id:id, nome: "aggiornamento",cognome: "aggiornamento"});
          }

        }catch (e){
          console.log(e);
        }
      },

      submitInsegnamentoCorsi: async function(mat){
        var id = document.getElementById("insegnamentoCorsiDocente"+ mat);
        id=id.options[id.selectedIndex].value;

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
            this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.push({materia:mat , rimosso:false});
            this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.materia == mat).docenti.push({id:id, nome: "aggiornamento",cognome: "aggiornamento"});
          }

        }catch (e){
          console.log(e);
        }
      }



    }
}
</script>
