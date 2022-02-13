<template>
	<header>
		<div class="overlay">
			<h1>Docenti</h1>
		</div>
	</header>

	<form>
		<div class = "par">Aggiungi un nuovo docente:</div>
		<div class = "container">
			<div class="row">
				<div class="col-sm-3">
					<label>Nome</label>
				</div>
				<div class="col-sm-6">
					<input type="text" class="form-control" id="inputNome" v-model="docenteNome">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-3">
					<label>Cognome</label>
				</div>
				<div class="col-sm-6">
					<input type="text" class="form-control" id="inputCognome" v-model="docenteCognome">
				</div>
				<div class="col-sm">
					<button id="addTeacher" class="btn btn-primary" v-on:click.prevent="submitDocente">Aggiungi docente</button>
				</div>
			</div>
		</div>
	</form>
	<br><br>

	<div class = "par">Elenco docenti disponibili:</div>
	<div id="allGrid" class="row">
		<div class="col-4">
			<div class="list-group" id="list-tab" role="tablist">
				<a v-for="docente in $store.state.professori" :key="docente.id + docente.nome + docente.cognome"
				   class="list-group-item list-group-item-action" v-bind:id="'docente-list-'+docente.id"
				   data-bs-toggle="list"
				   v-bind:href="'#docente'+docente.id" role="tab" v-bind:aria-controls="'docente'+docente.id">
					{{ docente.nome }} {{ docente.cognome }}
				</a>
			</div>
		</div>

		<div class="col-8">
			<div class="tab-content">
				<div v-for="insegnamentoDocenti in $store.state.insegnamentoDocenti"
					 :key="insegnamentoDocenti.id + insegnamentoDocenti.corsi"
					 v-bind:class="{ 'tab-pane fade active show':docenteSelected===insegnamentoDocenti.id,
							 'tab-pane fade': docenteSelected!==insegnamentoDocenti.id}"
					 v-bind:id="'docente'+insegnamentoDocenti.id" role="tabpanel"
					 v-bind:aria-labelledby="'docente-list-'+insegnamentoDocenti.id">

					<div class="card">
						<div class="card-header">
							Professore: {{ insegnamentoDocenti.id }}
							<img src="../assets/delate.png" alt="Delate" width="20" height="20"
								 v-on:click="showWarning(insegnamentoDocenti.id, null)">
<!--							<div class="pe-auto" v-on:click="showWarning(insegnamentoDocenti.id,null)">Elimina Docente</div>-->
						</div>
						<div class="card-body">
							<p>Questi sono i corsi in cui insegna:</p>
							<ul>
								<div v-for="corsi in insegnamentoDocenti.corsi" :key="corsi.materia">
									<li>
										{{ corsi.materia }}
										<img src="../assets/delate.png" alt="Delate" width="20" height="20"
											 v-on:click="showWarning(insegnamentoDocenti.id,corsi.materia)">
									</li>
								</div>
							</ul>
							<hr>
							<form>
								<p> Inserisci un nuovo insegnamento: </p>
								<div class="container">
									<div class="row">
										<div class="col">
											<label>Corso:</label>
										</div>
										<div class="col-6">
											<select class="form-select" aria-label="Default select example"
													v-bind:id="'insegnamentoDocentiCorso' +insegnamentoDocenti.id">
												<option v-for="corso in $store.state.corsi"
														v-bind:class="{nonDisplay:insegnamentoDocenti.corsi.find(corsi => corsi.materia==corso.materia)}"
														v-bind:value=corso.materia>
													{{ corso.materia }}
												</option>
											</select>
										</div>
										<div class="col">
											<button class="btn btn-primary" v-on:click.prevent="submitInsegnamentoDocenti(insegnamentoDocenti.id)">Aggiungi</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="confirmation" ref="confirmation" tabindex="-1" aria-labelledby="confirmationLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header text-center">
					<h5 class="modal-title w-100" id="teachersBookingLabel">Sei sicuro di voler eliminare il {{ this.message }}</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<p>Potresti non poter più tornare indietro!</p>
				</div>
				<div class="modal-footer">
					<button v-on:click="dismissChoice" type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
					<button v-on:click="confirmChoise" type="button" class="btn btn-primary" data-bs-dismiss="modal">Conferma</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {Modal} from "bootstrap";

	async function eliminaDocente(id) {
		// console.log("eliminaDocente:  " + id);
		try {
			const response = await fetch("/Noodle_war/ProfessoriServlet", {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({docente: id})
			});
			if (response.status === 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			} else {
				this.$store.state.professori = this.$store.state.professori.filter(prof => prof.id != id);
				this.$store.state.insegnamentoDocenti = this.$store.state.insegnamentoDocenti.filter(insegnamentoDocenti => insegnamentoDocenti.id != id);

				this.$store.state.corsi.forEach(mat =>
					this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat.materia).docenti =
						this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat.materia).docenti.filter(docenti => docenti.id != id));
			}
		} catch (e) {
			console.log(e);
		}
	}

	async function eliminaInsegnamento(id, mat) {
		// console.log("eliminaInsegnamento:  " + id + mat);
		try {
			const response = await fetch("/Noodle_war/InsegnamentoDocentiSevlet", {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({docente: id, corso: mat})
			});
			if (response.status === 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			} else if (response.status === 200) {

				this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi =
					this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.filter(corsi => corsi.materia != mat);

				this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti =
					this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti.filter(docenti => docenti.id != id);

				this.docenteSelected = id;
			}

			// window.location.reload();
		} catch (e) {
			console.log(e);
		}
	}


	async function getProfessori() {
		try {
			const response = await fetch("/Noodle_war/ProfessoriServlet");
			if (response.status === 401) {
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
			if (response.status === 401) {
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

			if (response.status === 401) {
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

			if (response.status === 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			}
			return await response.json();
		} catch (e) {
			console.log(e);
		}
	}

	function showWarning(idDocente, corso) {
		if (corso == null) {
			this.chois = "eliminaDocente";
			this.docenteDaEliminare = idDocente;
			this.message = "il docente: " + idDocente + "?";

		} else {
			this.chois = "eliminaInsegnamento";
			this.eliminaInsegnamentoDocente = idDocente;
			this.eliminaInsegnamentoCorso = corso;
			this.message = "l'insegnamento con docente: " + idDocente + " e corso: " + corso + "?";

		}
		if (!this.myModal)
			this.myModal = new Modal(this.$refs.confirmation)
		this.myModal.show();

	}

	function dismissChoice() {
		this.docenteDaEliminare = undefined;
		this.eliminaInsegnamentoDocente = undefined;
		this.eliminaInsegnamentoCorso = undefined;
	}

	async function confirmChoise() {
		if (this.chois === "eliminaDocente") {
			eliminaDocente.bind(this)(this.docenteDaEliminare);
			this.docenteDaEliminare = undefined;
		} else if (this.chois === "eliminaInsegnamento") {
			eliminaInsegnamento.bind(this)(this.eliminaInsegnamentoDocente, this.eliminaInsegnamentoCorso);
			this.eliminaInsegnamentoDocente = undefined;
			this.eliminaInsegnamentoCorso = undefined;
		} else console.log("errore nella choise");
		this.choise = undefined;
	}


	export default {
		name: "Docenti",
		async created() {
			if (this.$store.state.professori === undefined) {
				this.$store.state.professori = await getProfessori();
			}
			if (this.$store.state.corsi === undefined) {
				this.$store.state.corsi = await getCorsi();
			}

			if (this.$store.state.insegnamentoCorsi === undefined) {
				this.$store.state.insegnamentoCorsi = await getInsegnamentoCorsi();
			}
			if (this.$store.state.insegnamentoDocenti === undefined) {
				this.$store.state.insegnamentoDocenti = await getInsegnamentoDocenti();
			}

		},

		data() {
			return {
				docenteSelected: 1,
				docenteDaEliminare: undefined,
				docenteNome: undefined,
				docenteCognome: undefined,
				eliminaInsegnamentoDocente: undefined,
				eliminaInsegnamentoCorso: undefined,
				choise: undefined,
				message: undefined
			}
		},
		methods: {
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
					if (response.status === 401) {
						console.log("Errore nella servlet post docente");
					} else {
						const docenteResponse = await response.json();
						this.$store.state.professori.push({
							id: docenteResponse.id,
							nome: this.docenteNome,
							cognome: this.docenteCognome
						});
						this.$store.state.insegnamentoDocenti.push({id: docenteResponse.id, corsi: []})
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
					if (response.status === 401) {
						console.log("Errore nella servlet post docente");
					} else if (response.status === 200) {
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

						this.docenteSelected = id;
					}

				} catch (e) {
					console.log(e);
				}
			}
		}
	}
</script>

<style scoped>
	.nonDisplay {
		display: none;
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

	h1 {
		font-family: 'Arial', cursive;
		font-size: 50px;
		margin-top: 30px;
		margin-bottom: 30px;
		text-shadow: 2px 2px #000000;
	}

	.par{
		margin: 3vh auto auto;
		/*text-align: left;*/
		font-weight: bold;
		font-size: 20px;
		margin-bottom: 30px;
		/*margin-left: 30px;*/
	}

	.card-body{
		border: 1px solid rgba(0, 0, 0, 0.22);
	}

	.card-header{
		font-weight: bold;
		font-size: 18px;
		background: rgba(65, 164, 51, 0.2);
		border: 1px solid #41a433;
	}

	.btn-primary:hover{
		color: #0a53be;
		background: rgba(161, 196, 195, 0.6);
		border: 1px solid #0a53be;
	}

	ul {
		margin-left: 30%;
		text-align: left;
	}

	li{
		margin: 10px;
	}

	hr{
		margin-top: 30px;
		margin-bottom: 20px;
	}

	#allGrid{
		margin-left: 15%;
		margin-right: 15%;
	}
</style>
