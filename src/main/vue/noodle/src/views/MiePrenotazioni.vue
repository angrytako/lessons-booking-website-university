<template>
  <table>

    <div class="modal fade" id="confirmation" ref="confirmation" tabindex="-1" aria-labelledby="confirmationLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="teachersBookingLabel">Vuoi segnare la prenotazione per {{this.waitingConfirmation.corso}} come {{this.waitingConfirmation.stato}}?</h5>
          </div>
          <div class="modal-body">
            <h5>Potresti non poter più tornare indietro!</h5>
          </div>
          <div class="modal-footer">
            <button v-on:click="confirmChoice" type="button" class="btn btn-primary" data-bs-dismiss="modal">Si</button>
            <button v-on:click="dismissChoice" type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>

          </div>
        </div>
      </div>
    </div>

    <tr id="labels">
      <th>Giorno</th>
      <th>Orario</th>
      <th>Corso</th>
      <th>Docente</th>
      <th>Stato</th>
    </tr>
    <tr class="itemPrenotazione"
        v-bind:class="{completed: prenotazione.stato=='effettuata',
                       cancelled: prenotazione.stato=='cancellata',
                       active: prenotazione.stato=='attiva'}"
                       v-for="prenotazione in $store.state.miePrenotazioni"
                       :key="prenotazione.utente + prenotazione.docente + prenotazione.giorno + prenotazione.ora">
      <td>{{Days[prenotazione.giorno]}}</td>
      <td>{{Hours[prenotazione.orario]}}</td>
      <td>{{prenotazione.corso}}</td>
      <td>{{prenotazione.nomeDocente + " " + prenotazione.cognomeDocente  }}</td>
      <td v-if="prenotazione.stato != 'attiva'">{{prenotazione.stato}}

      </td>
      <td v-else>
        <select  name="state"  v-model="prenotazione.stato" v-on:change = "showWarning($event,prenotazione)">
          <option value="attiva">attiva</option>
          <option value="cancellata" >cancellata</option>
          <option value="effettuata">effettuata</option>
        </select>
      </td>

    </tr>
  </table>

</template>

<script>
import { Modal } from "bootstrap";
async function confirmChoice(){
  const response = await fetch("/Noodle_war/PrenotazioniServlet", {
                              method: 'PUT',
                              headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                              },
                              body: JSON.stringify(this.waitingConfirmation)
                            });
  this.waitingConfirmation = {};
  const decodedResponse = await  response.json();
  if(decodedResponse.error) {
    this.waitingConfirmation.stato = "attiva";
    //TODO show error
  }

}
function dismissChoice(e){
    this.waitingConfirmation.stato = "attiva";
    this.waitingConfirmation = {};
    return
}
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
function showWarning(e, prenotazione){
  if(!this.myModal)
  this.myModal = new Modal(this.$refs.confirmation)
  this.myModal.show();
  //store the
  this.waitingConfirmation = prenotazione;
}
export default {
  name: "MiePrenotazioni",
  //when the component is created this function is called
  //fetching data from server
  methods:{
    showWarning,
    confirmChoice,
    dismissChoice

  },
  async created() {
    const prenotazioni = await getMiePrenotazioni(this.$route.query.username);
    if(prenotazioni)
      this.$store.state.miePrenotazioni = prenotazioni;
  },
  data(){
    return {
      Days: { 0:"Lunedì",1:"Martedì",2:"Mercoledì",3:"Giovedì",4:"Venerdì",5:"Sabato",6:"Domenica" },
      Hours:{ 0: "15-16", 1: "16-17",  2: "17-18", 3: "18-19"},
      waitingConfirmation: {},
      myModal: undefined
    };
  }
}
</script>

<style scoped>
#warningDiv{
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0%;
  left: 0%;
  background-color: gray;
  z-index: 2;
}

#labels{
  background-color: aqua;
  height: 40px;
  font-size: 25px;
}
.completed{
  background-color: #03be76;
  color: white;

}
.active{
  background-color: #48769f;
  color: white;
}
.cancelled{
  opacity: 0.4;
  color: #ff0000;
}
select{
/*display: none;*/
  background-color: inherit;
  color: inherit;
  text-align: center;
}
select option {
  background-color: #6e6e6e;
  color: white;
}
.editImg{
  width: 20px;
  height: 20px;
  cursor: pointer;
}
.itemPrenotazione{
  height: 30px;
  font-size: 20px;
  position: relative;
}
table{
  cursor: default;
  width: 70%;
  height: 100%;
  margin: 30vh auto auto;
}
li{
  text-decoration: none;
}

</style>