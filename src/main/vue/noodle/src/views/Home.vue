<template>
	<header>
		<h1>HappyLearn</h1>
<!--		<img alt="Vue logo" src="../assets/logo.png">-->
	</header>

	<p v-if="$store.state.role === 'guest'">Clicca su un corso per poter visualizzare i docenti disponibili</p>
	<p v-else-if="$store.state.role !== 'guest'">Clicca su un corso per poterlo prenotare</p>

	<table class="table table-bordered">
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
			<!--				slotTime = matrCorsi[indexTime] qui sotto :)-->
			<td v-for="(slotDay, indexDay) in slotTime" :key="indexDay">
				<!--					slotDay = matrCorsi[indexTime][indexDay] qui sotto :)-->
				<p class="courseToClick" v-for="(slot, indexSlot) in slotDay" :key="indexSlot"  v-on:click="showTeachersAndUsersFunction(slot)"
				   data-bs-toggle="modal" data-bs-target="#teachersBooking">
					{{ slot.course }}
				</p>
			</td>
		</tr>
		</tbody>
	</table>

	<!-- Modal -->

	<div class="modal fade" id="teachersBooking" tabindex="-1" aria-labelledby="teachersBookingLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="teachersBookingLabel">{{ selectedSlot.course + " " + days[selectedSlot.day] + " " + times[selectedSlot.time] }}</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
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
							{{ teacher.nome + " " + teacher.cognome + " " + teacher.id + " " + selectedTeacher.id}}
						</label>
					</div>
					<br/>
					<p v-if="$store.state.role === 'amministratore'"> Seleziona l'utente per cui vuoi effettuare la prenotazione: </p>
					<select class="form-select" aria-label="User select" v-if="$store.state.role === 'amministratore'">
						<option v-for="user in selectedSlotUsers" v-bind:value=user.username v-on:change="showSelectedUser(user)">
							{{ user.username + " " + selectedUser.username }}
						</option>
					</select>
				</div>
				<div class="modal-footer" v-if="$store.state.role !== 'guest'">
					<button type="button" class="btn btn-success" data-bs-dismiss="modal" v-on:click="addBookings">
						Prenota
					</button>
				</div>
			</div>
		</div>
	</div>


	<ul>
		<!--		prima un for sugli slot e poi un for sui docenti nell'array di docenti-->
		<li v-for="slotAll in $store.state.slots"
			:key="slotAll.course + slotAll.day + slotAll.time">
			<div class="course">{{ slotAll.course }}</div>
			<div class="teacher" v-for="teacher in slotAll.teacherList">
				{{ teacher.nome + " " + teacher.cognome }}
			</div>
			<div class="day">{{ slotAll.day }}</div>
			<div class="time">{{ slotAll.time }}</div>
		</li>
	</ul>
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

		console.log(booking);

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

				// set timeout --> cosa che metto sora con un v-if
				// filter funziona come in Haskell: gli passi una lambda --> per ogni elemento dell'array SE ritorna vero viene tenuto l'elemento, altrimenti viene buttato via
				// filter ritorna un altro array dove tutti gli elementi del nuovo array rispettano la proprietà true (...)

				this.selectedSlot.teacherList = this.selectedSlot.teacherList.filter(teacher => teacher.id !== booking.idDocente);
				if (this.selectedSlot.teacherList.length === 0) {
					this.$store.state.slots = this.$store.state.slots.filter(slot => (slot.course !== booking.corso) && (slot.day !== booking.giorno) && (slot.time !== booking.orario));
				}
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
				}
			},
			showSelectedTeacher: function (teacher) {
				this.selectedTeacher = teacher;
			},
			showSelectedUser: function(user) {
				this.selectedUser = user;
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
				allNotDeletedBookings: []
			}
		}
	}
</script>

<style scoped>
	#labels {
		/*background-color: aqua;*/
		/*height: 40px;*/
		/*font-size: 25px;*/
	}

	table {
		cursor: default;
		width: 70%;
		height: 100%;
		margin: auto auto;
		background: rgba(230, 255, 234, 1);
	}

	.light {
		color: white;
		background-color: #41a433;
	}

	.dark {
		color: white;
		background-color: #0f5132;
	}

	/*.home {*/
	/*	background-color: #f2e7c3;*/
	/*}*/

	.courseToClick {
		color: #0d6efd;
		text-decoration: underline;
	}

	li {
		/*text-align: left;*/
		text-decoration: none;
	}

	h1{
		margin-top: 50px;
	}

	.slot {
		display: flex;
	}
</style>
