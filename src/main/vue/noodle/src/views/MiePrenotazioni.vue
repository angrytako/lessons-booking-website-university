<template>
	<header>
		<div class="overlay">
			<h1>Le mie prenotazioni</h1>
		</div>
	</header>

	<div class = "par" v-if="$store.state.role !== 'guest'">Clicca sullo stato delle prenotazioni attive per poterle modificare</div>
	<div class="container">
		<table class="table table-bordered">
			<thead>
				<tr id="labels">
					<th scope="col">Giorno</th>
					<th scope="col">Orario</th>
					<th scope="col">Corso</th>
					<th scope="col">Docente</th>
					<th scope="col">Stato</th>
				</tr>
			</thead>
			<tbody>
				<tr class="itemPrenotazione"
					v-bind:class="{completed: prenotazione.stato==='effettuata',
						   cancelled: prenotazione.stato==='cancellata',
						   active: prenotazione.stato==='attiva'}"
					v-for="prenotazione in $store.state.miePrenotazioni"
					:key="prenotazione.utente + prenotazione.docente + prenotazione.giorno + prenotazione.ora">
					<td>{{ Days[prenotazione.giorno] }}</td>
					<td>{{ Hours[prenotazione.orario] }}</td>
					<td>{{ prenotazione.corso }}</td>
					<td>{{ prenotazione.nomeDocente + " " + prenotazione.cognomeDocente }}</td>
					<td v-if="prenotazione.stato !== 'attiva'">{{ prenotazione.stato }}</td>
					<td v-else>
						<select name="state" v-model="prenotazione.stato" v-on:change="showWarning($event,prenotazione)">
							<option value="attiva">attiva</option>
							<option value="cancellata">cancellata</option>
							<option value="effettuata">effettuata</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="modal fade" id="confirmation" ref="confirmation" tabindex="-1"
		 aria-labelledby="confirmationLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="teachersBookingLabel">Vuoi segnare la prenotazione di
						{{ this.waitingConfirmation.corso }} <br> come {{ this.waitingConfirmation.stato }}?
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<p>Potresti non poter più tornare indietro!</p>
				</div>
				<div class="modal-footer">
					<button v-on:click="dismissChoice" type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
					<button v-on:click="confirmChoice" type="button" class="btn btn-primary" data-bs-dismiss="modal">Conferma</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {Modal} from "bootstrap";

	async function confirmChoice() {
		const response = await fetch("/Noodle_war/PrenotazioniServlet", {
			method: 'PUT',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(this.waitingConfirmation)
		});
		this.waitingConfirmation = {};
		const decodedResponse = await response.json();
		if (decodedResponse.error) {
			this.waitingConfirmation.stato = "attiva";
			//TODO show error
		}

	}

	function dismissChoice(e) {
		this.waitingConfirmation.stato = "attiva";
		this.waitingConfirmation = {};
		return;
	}

	async function getMiePrenotazioni(username) {
		try {
			let response;
			if (username)
				response = await fetch("/Noodle_war/PrenotazioniServlet?username=" + username);
			else
				window.location.href = "/Noodle_war/login";
			if (response.status == 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			}
			return await response.json();
		} catch (e) {
			console.log(e);
		}
	}

	function showWarning(e, prenotazione) {
		if (!this.myModal)
			this.myModal = new Modal(this.$refs.confirmation)
		this.myModal.show();
		//store the
		this.waitingConfirmation = prenotazione;
	}

	export default {
		name: "MiePrenotazioni",
		//when the component is created this function is called
		//fetching data from server
		methods: {
			showWarning,
			confirmChoice,
			dismissChoice

		},
		async created() {
			const prenotazioni = await getMiePrenotazioni(this.$route.query.username);
			if (prenotazioni)
				this.$store.state.miePrenotazioni = prenotazioni;
		},
		data() {
			return {
				Days: {0: "Lunedì", 1: "Martedì", 2: "Mercoledì", 3: "Giovedì", 4: "Venerdì", 5: "Sabato", 6: "Domenica"},
				Hours: {0: "15-16", 1: "16-17", 2: "17-18", 3: "18-19"},
				waitingConfirmation: {},
				myModal: undefined
			};
		}
	}
</script>

<style scoped>
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

	h1 {
		font-family: 'Arial', cursive;
		font-size: 50px;
		margin-top: 30px;
		margin-bottom: 30px;
		text-shadow: 2px 2px #000000;
	}

	table {
		cursor: default;
		width: 70%;
		height: 100%;
		margin: auto auto;
		box-shadow: 0 3px 20px 0 #0000003b;
	}

	.modal-header{
		/*background-color: rgba(65, 164, 51, 0.25);*/
		border-bottom: 1px solid rgba(0, 0, 0, 0.3);
		font-weight: bold;
	}

	.modal-title{
		margin: 0 auto 0 25px;
	}

	.modal-footer{
		border-top: 1px solid rgba(0, 0, 0, 0.3);
	}

	.btn-primary:hover{
		color: #0a53be;
		background: #ffffff;
		border: 1px solid #0a53be;
	}

	#labels {
		background-color: rgba(65, 164, 51, 0.25);
		height: 40px;
		font-size: 22px;
	}

	.completed {
		background-color: #03be76;
		color: white;

	}

	.active {
		background-color: #48769f;
		color: white;
	}

	.cancelled {
		opacity: 0.4;
		color: #ff0000;
	}

	select {
		background-color: inherit;
		color: inherit;
		text-align: center;
	}

	select option {
		background-color: #6e6e6e;
		color: white;
	}

	.itemPrenotazione {
		height: 30px;
		font-size: 20px;
		position: relative;
	}

	li {
		text-decoration: none;
	}

	.par{
		margin: 10vh auto auto;
		font-weight: bold;
		font-size: 20px;
		margin-bottom: 30px;
	}
</style>
