<template>
	<header>
		<div class="overlay">
			<h1>HappyLearn</h1>
			<h3>Imparare in allegria</h3>
			<div class ="par" v-if="$store.state.role === 'guest'">Clicca su un corso per poter visualizzare i docenti disponibili</div>
			<div class = "par" v-else-if="$store.state.role !== 'guest'">Clicca su un corso per poterlo prenotare</div>
		</div>
	</header>

	<br><br>
	<div class = "container">
		<table class="table table-bordered table-hover table-hover-cells">
			<thead>
			<tr id=labels>
				<th scope="col"></th>
				<th scope="col" class="light">Lunedì</th>
				<th scope="col" class="dark">Martedì</th>
				<th scope="col" class="light">Mercoledì</th>
				<th scope="col" class="dark">Giovedì</th>
				<th scope="col" class="light">Venerdì</th>
			</tr>
			</thead>

			<!--		mettere con slot orari diversi colori diversi!!!!!!!-->
			<tbody>
			<tr v-for="(slotTime, indexTime) in matrCorsi" :key="indexTime">
				<th scope="row" v-bind:class="{light:indexTime % 2===0, dark:indexTime % 2===1}">{{ times[indexTime] }}</th>
				<td v-for="(slotDay, indexDay) in slotTime" :key="indexDay">
					<p class="courseToClick" v-for="(slot, indexSlot) in slotDay" :key="indexSlot"  v-on:click="showTeachersAndUsersFunction(slot)"
					   data-bs-toggle="modal" data-bs-target="#teachersBooking">
						{{ slot.course }}
					</p>
				</td>
			</tr>
			</tbody>
		</table>
	</div>

	<!-- Modal -->

	<div class="modal fade" id="teachersBooking" tabindex="-1" aria-labelledby="teachersBookingLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header text-center">
					<h5 class="modal-title w-100" id="teachersBookingLabel">{{ "Corso: " + selectedSlot.course + " ---> " + days[selectedSlot.day] + ": " + times[selectedSlot.time] }}</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="p2"> Seleziona il docente con cui vuoi effettuare la prenotazione:</div>
				<div class="modal-body" v-if="$store.state.role === 'guest'">
					<ul>
						<li v-for="teacher in selectedSlot.teacherList">
							{{ teacher.nome + " " + teacher.cognome }}
						</li>
					</ul>
				</div>
				<div class="modal-body" v-else-if="$store.state.role !== 'guest'">
					<div class="form-check" v-for="(teacher, index) in selectedSlot.teacherList">
							<!--		v-bind: l'espressione dipende da qlc di javascript
										checked: attributo di html settato a true/false
										-------segnalo come checked solo se l'id del professore è uguale al prof selezionato
										-->
						<input class="form-check-input" type="radio" v-on:change="showSelectedTeacher(teacher)" name="teacherBookingSelect" id="teacherBooking" v-bind:value=teacher.id v-bind:checked="teacher.id === selectedTeacher.id">
						<label class="form-check-label" for="teacherBooking">
							{{ teacher.nome + " " + teacher.cognome}}
						</label>
					</div>
					<br/>
					<div class = "p2" v-if="$store.state.role === 'amministratore'"> Seleziona l'utente per cui vuoi effettuare la prenotazione: </div>
					<select class="form-select" aria-label="User select" v-if="$store.state.role === 'amministratore'" v-model="selectedUser.username">
						<option v-for="(user, index) in selectedSlotUsers" v-bind:value="user.username">
							{{ user.username + " " + selectedUser.username }}
						</option>
					</select>
				</div>
				<div class="modal-footer" v-if="$store.state.role !== 'guest'">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
					<button type="button" class="btn btn-success" data-bs-dismiss="modal" v-on:click="addBookings">Prenota</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal confirmed booking -->
	<div class="modal fade" id="exampleModal" tabindex="-1" ref="confirmed" aria-labelledby="modalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
				<symbol id="check-circle-fill" fill="currentColor" viewBox="0 0 16 16">
					<path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
				</symbol>
			</svg>

			<div class="alert alert-success d-flex align-items-center" role="alert">
				<svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Success:"><use xlink:href="#check-circle-fill"/></svg>
				<div>
					La prenotazione è stata effettuata con successo!
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import { Modal } from "bootstrap";
	async function addBookings() {
		const booking = {};
		booking.corso = this.selectedSlot.course;
		booking.idDocente = this.selectedTeacher.id;
		if(this.$store.state.role === "amministratore")
			booking.utente = this.selectedUser.username;
		else
			booking.utente = this.$store.state.username;
		booking.giorno = this.selectedSlot.day;
		booking.orario = this.selectedSlot.time;

		try {
			const response = await fetch("/Noodle_war/PrenotazioniServlet", {
				method: 'POST',
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(booking)
			});
			if (response.status === 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			} else if(response.status === 500) {
				console.log(response);
				return [];
			} else if (response.status === 200) {
				// se sono un client svuota la casella
				if (this.$store.state.role === "cliente")
					this.matrCorsi[this.selectedSlot.time][this.selectedSlot.day] = [];
				else if(this.$store.state.role === "amministratore")		// vero se fa parte del nuovo array
					this.allNotDeletedBookings.push(booking);
				// set timeout --> cosa che metto sora con un v-if
				// filter funziona come in Haskell: gli passi una lambda --> per ogni elemento dell'array SE ritorna vero viene tenuto l'elemento, altrimenti viene buttato via
				// filter ritorna un altro array dove tutti gli elementi del nuovo array rispettano la proprietà true (...)

				this.selectedSlot.teacherList = this.selectedSlot.teacherList.filter(teacher => teacher.id !== booking.idDocente);
				if (this.selectedSlot.teacherList.length === 0) {
					this.$store.state.slots = this.$store.state.slots.filter(slot => (slot.course !== booking.corso) && (slot.day !== booking.giorno) && (slot.time !== booking.orario));
				}
				if(!this.myModal)
					this.myModal = new Modal(this.$refs.confirmed)
				this.myModal.show();
				const supModal = this.myModal;
				setTimeout(()=> supModal.hide(),3000);
			}
		} catch (e) {
			console.log(e);
		}
	}

	async function getMyBookings() {
		try {
			let response;
			if(this.$store.state.role === "cliente")
				response = await fetch("/Noodle_war/PrenotazioniServlet?username=" + this.$store.state.username);
			else if (this.$store.state.role === "amministratore") {
				const myResponse = await fetch("/Noodle_war/PrenotazioniServlet?username=" + this.$store.state.username);
				const allResponse = await fetch("/Noodle_war/PrenotazioniServlet");
				if(myResponse.status === 401 || allResponse.status === 401){
					window.location.href = "/Noodle_war/login";
					return [];
				}
				const myResponseSupport = await myResponse.json();
				const allResponseSupport = await allResponse.json();
				return myResponseSupport.concat(allResponseSupport);
			}
			else
				window.location.href = "/Noodle_war/login";
			if(response.status === 401){
				window.location.href = "/Noodle_war/login";
				return [];
			}
			return await response.json();
		}catch (e){
			console.log(e);
		}
	}

	async function getAllUsers(){
		try {
			const response = await fetch("/Noodle_war/AllUsersServlet");
			if (response.status === 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			}
			return await response.json();
		} catch (e) {
			console.log(e);
		}
	}

	async function getSlots() {
		try {
			const response = await fetch("/Noodle_war/AvailableSlotsServlet");
			if (response.status === 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			}
			return await response.json();
		} catch (e) {
			console.log(e);
		}
	}

	export default {
		name: 'Home',
		async created() {
			const servletSlots = await getSlots();

			// filter funziona come in Haskell: gli passi una lambda --> per ogni elemento dell'array SE ritorna vero viene tenuto l'elemento, altrimenti viene buttato via
			// filter ritorna un altro array dove tutti gli elementi del nuovo array rispettano la proprietà true (...)
			if(servletSlots) {
				if (this.$store.state.role === "guest")
					this.$store.state.slots = servletSlots;
				else if (this.$store.state.role === "cliente") {
					// bind serve per dare un this alle funzioni perché altrimenti le funzioni userebbero un loro this
					// --> a quale this ci si riferisce???
					let myNotDeletedBookings = await getMyBookings.bind(this)();
					myNotDeletedBookings = myNotDeletedBookings.filter(booking => booking.stato !== "cancellata");
					// find: prende un elemento su cui sto facendo find
					// myNotDeletedBookings.find(notDelBooking => (notDelBooking.giorno === slot.day) && (notDelBooking.orario === slot.time)
					// se c'è uno slot lo trova
					this.$store.state.slots = servletSlots.filter(slot => !myNotDeletedBookings.find(notDelBooking => (notDelBooking.giorno === slot.day) && (notDelBooking.orario === slot.time)));
				}
				else if (this.$store.state.role === "amministratore"){
					this.allNotDeletedBookings = await getMyBookings.bind(this)();
					this.allNotDeletedBookings = this.allNotDeletedBookings.filter(booking => booking.stato !== "cancellata");
					// vado a prendere tutti gli utenti --> SOLO AMMINISTRATORE!!!
					// li metto in una variabile LOCALE --> allAvailableUsers
					this.allAvailableUsers = await getAllUsers.bind(this)();
					this.$store.state.slots = servletSlots;
				}
			}

			/*[{corso, docenteNome, docenteCognome, giorno, ora}...]*/
			/*[0:[0:[0:corso1,1:corso2, etc...],[1:[0:...]],1:[],2:[],3:[]]*/
			for (let slot of this.$store.state.slots) {
				this.matrCorsi[slot.time][slot.day].push(slot);
			}
		},
		methods: {
			showTeachersAndUsersFunction: function (slot) {
				this.selectedSlot = slot;
				if(this.$store.state.role === "cliente" || this.$store.state.role === "amministratore")
					this.selectedTeacher = slot.teacherList[0];		//seleziona sempre il primo docente della lista --> il checked di sopra è uguale solo se è il primo!!!!!!!
				if (this.$store.state.role === "amministratore") {
					let allAvailableUsersSlot = [];
					let toAdd = true;
					for(let user of this.allAvailableUsers){
						toAdd = true;
						for (let booking of this.allNotDeletedBookings) {
							if(slot.day === booking.giorno && slot.time === booking.orario && user.username === booking.utente)
								toAdd = false;
						}
						if (toAdd)
							allAvailableUsersSlot.push(user);
					}
					this.selectedSlotUsers = allAvailableUsersSlot;
					// ... spread operator --> ...arr -> arr[0], arr[1], ..etc
					// ...obj -> key1:value1, key2:value2, ..etc
					// allAvailableUsersSlot[0] return user object at the zero position
					this.selectedUser = allAvailableUsersSlot || allAvailableUsersSlot.length ? {...allAvailableUsersSlot[0]} : undefined;
				}
			},
			showSelectedTeacher: function (teacher) {
				this.selectedTeacher = teacher;
			},
			addBookings,
			getMyBookings,
			getAllUsers
		},
		data() {
			return {
				matrCorsi: [
					[[], [], [], [], []],
					[[], [], [], [], []],
					[[], [], [], [], []],
					[[], [], [], [], []]
				],
				days: {0: "Lunedì", 1: "Martedì", 2: "Mercoledì", 3: "Giovedì", 4: "Venerdì"},
				times: {0: "15-16", 1: "16-17", 2: "17-18", 3: "18-19"},
				selectedSlot: {},
				selectedUser: {},
				selectedTeacher: {},
				allAvailableUsers: [],		// tutti gli utenti
				selectedSlotUsers: [],
				allNotDeletedBookings: [],
				myModal: undefined
			}
		}
	}
</script>

<style scoped>
	/*#labels {*/
	/*	background-color: aqua;*/
	/*	height: 40px;*/
	/*	font-size: 25px;*/
	/*}*/

	table {
		cursor: default;
		width: 90%;
		height: 100%;
		margin: auto auto;
		background-image: linear-gradient( 135deg, rgba(35, 252, 4, 0.2) 10%, rgba(230, 255, 234, 1) 100%);
	}

	.light {
		color: white;
		background-color: #41a433;
	}

	.dark {
		color: white;
		background-color: #0f5132;
	}

	.courseToClick {
		color: #0d6efd;
		text-decoration: underline;
	}

	ul{
		text-align: left;
		text-decoration: none;
	}

	li {
		text-align: left;
		text-decoration: none;
	}

	header {
		text-align: center;
		width: 100%;
		height: auto;
		background-size: cover;
		background-attachment: fixed;
		position: relative;
		overflow: hidden;
		border-radius: 0 0 85% 85% / 30%;
	}
	header .overlay{
		width: 100%;
		height: 100%;
		padding: 50px;
		color: #FFF;
		text-shadow: 1px 1px 1px #333;
		background-image: linear-gradient( 135deg, rgb(27, 86, 19) 10%, rgba(27, 215, 52, 0.84) 100%);
	}

	.btn-success:hover{
		color: #41a433;
		background: #ffffff;
		border: 1px solid #41a433;
	}

	.modal-header{
		background-color: rgba(65, 164, 51, 0.2);
	}

	/*.modal-body{*/
	/*	text-align: left;*/
	/*}*/

	h1 {
		font-family: 'Open Sans', cursive;
		font-size: 80px;
		margin-top: 30px;
		margin-bottom: 30px;
	}

	h3, .par {
		font-family: 'Open Sans', sans-serif;
		margin-bottom: 30px;
	}

	p{
		margin-bottom: 10px;
	}

	.p2{
		margin-top: 15px;
		margin-bottom: 15px;
	}
	.table-hover.table-hover-cells > tbody > tr:hover > td:hover {
		background-color: #e8e8e8;
	}

</style>
