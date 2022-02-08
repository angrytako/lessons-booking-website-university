<template>

  <div class="modal fade" id="confirmation" ref="confirmation" tabindex="-1" aria-labelledby="confirmationLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="teachersBookingLabel">Sei sicuro di voler eliminare il {{this.message}}?</h5>
        </div>
        <div class="modal-body">
          <h5>Potresti non poter più tornare indietro!</h5>
        </div>
        <div class="modal-footer">
          <button v-on:click="confirmChoise" type="button" class="btn btn-primary" data-bs-dismiss="modal">Si</button>
          <button v-on:click="dismissChoice" type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>

        </div>
      </div>
    </div>
  </div>


  <h1>I Docenti:</h1>
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
          Professore: {{insegnamentoDocenti.id}}.
          <img src="../assets/delate.png" alt="Delate" width="20" height="20" v-on:click="showWarning(insegnamentoDocenti.id, null)">
          <div class="pe-auto" v-on:click="showWarning(insegnamentoDocenti.id,null)">Elimina Docente</div>

          Questi sono i corsi in cui insegna:
          <div v-for="corsi in insegnamentoDocenti.corsi" :key="corsi.materia">
            ~ {{corsi.materia}}
            <img src="../assets/delate.png" alt="Delate" width="20" height="20" v-on:click="showWarning(insegnamentoDocenti.id,corsi.materia)">
          </div>

          <form>
            <br>
            Inserisci un nuovo insegnamento:
            <div class="form-group row">
              <label class="col-sm-2 col-form-label">Corso:</label>
              <div class="col-sm-10">

                <select class="form-select" aria-label="Default select example" v-bind:id="'insegnamentoDocentiCorso' +insegnamentoDocenti.id">

                  <option v-for="corso in $store.state.corsi"
                          v-bind:class="{nonDisplay:insegnamentoDocenti.corsi.find(corsi => corsi.materia==corso.materia)}"
                          v-bind:value=corso.materia>
                    {{corso.materia}}
                  </option>
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
      <label class="col-sm-2 col-form-label">Nome</label>
      <div class="col-sm-10">
        <input type="email" class="form-control" id="inputNome" v-model="docenteNome">
      </div>
    </div>
    <div class="form-group row">
      <label class="col-sm-2 col-form-label">Cognome</label>
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
</template>

<script>


import {Modal} from "bootstrap";

async function eliminaDocente(id) {
  console.log("eliminaDocente:  "+id);
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
    }else{

      console.log(this.$store.state.corsi);
      console.log(this.$store.state.insegnamentoDocenti);
      console.log(this.$store.state.professori);

     // this.$store.state.professori=this.$store.state.professori.filter(prof=>prof.id!=id);
      console.log(this.$store.state.professori);
      //  this.$store.state.insegnamentoDocenti=this.$store.state.insegnamentoDocenti.filter(insegnamentoDocenti=>insegnamentoDocenti.id!=id);
      //TODO togliere da insegnamentoCorsi il docente eliminato

    }


  }catch (e){
    console.log(e);
  }
}


async function eliminaInsegnamento(id,mat) {
  console.log("eliminaInsegnamento:  "+id+mat);

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

      this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti =
          this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti.filter(docenti=> docenti.id!=id);
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
function showWarning(idDocente,corso){
  if (corso==null)  {
    this.chois="eliminaDocente";
    this.docenteDaEliminare = idDocente;
    this.message= "il docente: "+idDocente+"?";

  }
  else{
    this.chois="eliminaInsegnamento";
    this.eliminaInsegnamentoDocente=idDocente;
    this.eliminaInsegnamentoCorso=corso;
    this.message= "l'insegnamento con docente: "+idDocente+" e corso: "+corso+"?";

  }
  if(!this.myModal)
    this.myModal = new Modal(this.$refs.confirmation)
  this.myModal.show();

}

function dismissChoice(){
  this.docenteDaEliminare=undefined;
  this.eliminaInsegnamentoDocente=undefined;
  this.eliminaInsegnamentoCorso=undefined;
}

async function confirmChoise() {
if(this.chois=="eliminaDocente"){
  console.log("elimina docente");
  eliminaDocente.bind(this)(this.docenteDaEliminare);
  this.docenteDaEliminare=undefined;
}
else if(this.chois=="eliminaInsegnamento"){
  console.log("elimina insegnamento");
  eliminaInsegnamento(this.eliminaInsegnamentoDocente,this.eliminaInsegnamentoCorso);
  this.eliminaInsegnamentoDocente=undefined;
  this.eliminaInsegnamentoCorso=undefined;
}
else console.log("errore nella choise");
this.choise=undefined;
}





export default {
  name: "Docenti",
  async created() {
    if ( this.$store.state.professori==undefined) {this.$store.state.professori = await getProfessori();}
    if  (this.$store.state.corsi==undefined) {this.$store.state.corsi = await getCorsi();}

    if  ( this.$store.state.insegnamentoCorsi==undefined) {this.$store.state.insegnamentoCorsi = await getInsegnamentoCorsi();}
    if  ( this.$store.state.insegnamentoDocenti ==undefined) { this.$store.state.insegnamentoDocenti = await getInsegnamentoDocenti();}

  },

  data(){
    return {
      docenteDaEliminare: undefined,
      docenteNome: undefined,
      docenteCognome: undefined,
      eliminaInsegnamentoDocente: undefined,
      eliminaInsegnamentoCorso:undefined,
      choise:undefined,
      message:undefined
    }},
  methods: {
    eliminaDocente,
    eliminaInsegnamento,
    showWarning,
    dismissChoice,
    confirmChoise,

    submitDocente: async function (e) {
      try {
        const response = await fetch("/Noodle_war/ProfessoriServlet", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({nome: this.docenteNome, cognome: this.docenteCognome})
        });
        if (response.status == 401) {
          console.log("Errore nella servlet post docente");
        } else {
          const docenteResponse = await response.json();
          this.$store.state.professori.push({
            id: docenteResponse.id,
            nome: this.docenteNome,
            cognome: this.docenteCognome
          });
          this.$store.state.insegnamentoDocenti.push({id: docenteResponse.id, corsi:[]})
        }

      } catch (e) {
        console.log(e);
      }
    },

    submitInsegnamentoDocenti: async function (id) {
      var mat = document.getElementById("insegnamentoDocentiCorso" + id);
      mat = mat.options[mat.selectedIndex].text;
      try {
        const response = await fetch("/Noodle_war/InsegnamentoDocentiSevlet", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({docente: id, corso: mat})
        });
        if (response.status == 401) {
          console.log("Errore nella servlet post docente");
        } else if (response.status == 200) {
          this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.push({
            materia: mat,
            rimosso: false
          });

          this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti.push({
            id: id,
            nome: this.$store.state.professori.find(docente => docente.id == id).nome,
            cognome: this.$store.state.professori.find(docente => docente.id == id).cognome,
            rimosso: false
          });
        }

      } catch (e) {
        console.log(e);
      }
    }

  }
}
</script>

<style scoped>
.nonDisplay{
  display:none;
}

h1{
  margin-top: 50px;
}
</style>