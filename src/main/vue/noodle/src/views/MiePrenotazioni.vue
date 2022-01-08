<template>
  <table>
    <div id="warningDiv" v-if="warningActive">
      <form id="warningForm">
        <h2>Sicuro sicuro?</h2>
        <label>Si</label>
        <input  type="radio" value="true" name="confirmation" v-model="selectedConfirmation">
        <label>No</label>
        <input checked type="radio" value="false" name="confirmation" v-model="selectedConfirmation">
        <div>
          <button v-on:click="confirmChoice">OK</button>
        </div>
      </form>
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
async function confirmChoice(e){
  //to stop html automatic submit
  e.preventDefault();
    //if confirmed, sending the choice to servlet
  if(this.selectedConfirmation == "false") {
    this.waitingConfirmation.stato = "attiva";
    this.warningActive=false;
    this.waitingConfirmation = null;
    return
  }
  const response = await fetch("/Noodle_war/PrenotazioniServlet", {
                              method: 'PUT',
                              headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                              },
                              body: JSON.stringify(this.waitingConfirmation)
                            });
  this.waitingConfirmation = null;
  this.warningActive=false;
  const decodedResponse = await  response.json();
  if(!decodedResponse.error)
    return;

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
  this.warningActive=true;
  //store the
  this.waitingConfirmation = prenotazione;
}
export default {
  name: "MiePrenotazioni",
  //when the component is created this function is called
  //fetching data from server
  methods:{
    showWarning,
    confirmChoice

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
      warningActive:false,
      waitingConfirmation:null,
      selectedConfirmation:"false"
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