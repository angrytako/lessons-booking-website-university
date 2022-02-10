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



  <h1>I Corsi:</h1>
  <div class="row">
    <div class="col-4">
      <div class="list-group" id="list-tab-corsi" role="tablist">
        <a v-for="corso in $store.state.corsi" :key="corso.materia "
           class="list-group-item list-group-item-action" v-bind:id="'corso-list-'+corso.materia.replace(/\s+/g, '')" data-bs-toggle="list"
           v-bind:href="'#corso-'+corso.materia.replace(/\s+/g, '')" role="tab" v-bind:aria-controls="'corso-'+corso.materia.replace(/\s+/g, '')">
          {{corso.materia}}</a>
      </div>
    </div>
    <div class="col-8">
      <div class="tab-content" id="nav-tabContent-corsi">
        <div v-for="insegnamentoCorsi in $store.state.insegnamentoCorsi" :key="insegnamentoCorsi.corso + insegnamentoCorsi.docenti"
             v-bind:class="{ 'tab-pane fade active show':corsoSelected==insegnamentoCorsi.corso,
                             'tab-pane fade': corsoSelected!=insegnamentoCorsi.corso}"
             v-bind:id="'corso-'+insegnamentoCorsi.corso.replace(/\s+/g, '')"  role="tabpanel" v-bind:aria-labelledby="'corso-list-'+insegnamentoCorsi.corso.replace(/\s+/g, '')">

          Corso: {{insegnamentoCorsi.corso}}.
          <img src="../assets/delate.png" alt="Delate" width="20" height="20" v-on:click="showWarning(insegnamentoCorsi.corso, null)">
          <div class="pe-auto" v-on:click="showWarning(insegnamentoCorsi.corso, null)">Elimina Corso</div>

          Professori che insegnano questo corso:
          <div v-for="docenti in insegnamentoCorsi.docenti" :key="docenti.id">
            ~ {{docenti.id}} {{docenti.nome}} {{docenti.cognome}}
            <img src="../assets/delate.png" alt="Delate" width="20" height="20" v-on:click="showWarning(insegnamentoCorsi.corso,docenti.id)">
          </div>

          <form>
            <br>
            Inserisci un nuovo insegnamento:
            <div class="form-group row">
              <label class="col-sm-2 col-form-label">Professore </label>
              <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" v-bind:id="'insegnamentoCorsiDocente' +insegnamentoCorsi.corso">

                  <option  v-for="docente in $store.state.professori" :key="docente.id"
                          v-bind:class="{nonDisplay:insegnamentoCorsi.docenti.find(docenti => docenti.id==docente.id)}"
                           v-bind:value=docente.id>
                           {{docente.id}} {{docente.nome}} {{docente.cognome}}
                  </option>
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
      <label class="col-sm-2 col-form-label">Corso:</label>
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

import {Modal} from "bootstrap";

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
    console.log(this.$store.state.corsi);
    this.$store.state.corsi=this.$store.state.corsi.filter(corso=>corso.materia!=materia);

    // window.location.reload();
  }catch (e){
    console.log(e);
  }
}


async function eliminaInsegnamento(mat,id) {

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

      this.corsoSelected = mat;

    }

    // window.location.reload();
  }catch (e){
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

function showWarning(corso,idDocente){
  if (idDocente==null)  {
    this.chois="eliminaCorso";
    this.corsoDaEliminare = corso;
    this.message= "il corso: "+corso+"?";

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
  this.corsoDaEliminare=undefined;
  this.eliminaInsegnamentoDocente=undefined;
  this.eliminaInsegnamentoCorso=undefined;
}

async function confirmChoise() {
  if(this.chois=="eliminaCorso"){
    console.log("elimina corso");
    eliminaCorso.bind(this)(this.docenteDaEliminare);
    this.corsoDaEliminare=undefined;
  }
  else if(this.chois=="eliminaInsegnamento"){
    console.log("elimina insegnamento");
    eliminaInsegnamento.bind(this)(this.eliminaInsegnamentoCorso,this.eliminaInsegnamentoDocente);
    this.eliminaInsegnamentoDocente=undefined;
    this.eliminaInsegnamentoCorso=undefined;
  }
  else console.log("errore nella choise");
  this.choise=undefined;
}

export default {
  name: "Corsi",
  async created() {

    if  (this.$store.state.corsi==undefined) {  this.$store.state.corsi = await getCorsi();}
    if  (this.$store.state.professori==undefined) {this.$store.state.professori= await getProfessori();}

    if  (this.$store.state.insegnamentoDocenti==undefined) {this.$store.state.insegnamentoDocenti= await getInsegnamentoDocenti();}
    if  ( this.$store.state.insegnamentoCorsi==undefined) { this.$store.state.insegnamentoCorsi = await getInsegnamentoCorsi();}

  },
  data(){
    return {
      corso: undefined,
      corsoSelected: "aaa",
      corsoDaEliminare: undefined,
      eliminaInsegnamentoDocente: undefined,
      eliminaInsegnamentoCorso:undefined,
      chooise: undefined,
      message: undefined
    }},
  methods: {
    showWarning,
    dismissChoice,
    confirmChoise,

    submitCorso: async function (e) {
      try {
        const response = await fetch("/Noodle_war/CorsiServlet", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({corso: this.corso})
        });
        if (response.status == 401) {
          console.log("Errore nella servlet post corso");
        } else {
          this.$store.state.corsi.push({materia: this.corso});
          this.$store.state.insegnamentoCorsi.push({corso: this.corso, docenti:[]})
        }
      } catch (e) {
        console.log(e);
      }
    },

    submitInsegnamentoCorsi: async function (mat) {
      var id = document.getElementById("insegnamentoCorsiDocente" + mat);
      id = id.options[id.selectedIndex].value;

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


          this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti.push({
            id: id,
            nome: this.$store.state.professori.find(docente => docente.id == id).nome,
            cognome: this.$store.state.professori.find(docente => docente.id == id).cognome,
            rimosso: false
          });

          this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.push({
            materia: mat,
            rimosso: false
          });

          this.corsoSelected = mat;
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