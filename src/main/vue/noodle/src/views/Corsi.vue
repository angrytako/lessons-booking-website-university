<template>
	<header>
		<div class="overlay">
			<h1>Corsi</h1>
		</div>
	</header>

	<form>
		<div class="par">Aggiungi un nuovo corso:</div>
		<div class="container">
			<div class="row">
				<div class="col">
					<label>Corso:</label>
				</div>
				<div class="col-6">
					<input type="text" class="form-control" id="inputCorso" v-model="corso">
				</div>
				<div class="col">
					<button class="btn btn-primary" v-on:click.prevent="submitCorso">Aggiungi Corso</button>
				</div>
			</div>
		</div>
	</form>
	<br><br>

	<div class="par">Elenco corsi disponibili:
		<!--		<br>-->
		<!--		<div class = "p3">-->
		<!--			Clicca sul <img src="../assets/delate.png" alt="Delate" width="20" height="20"> per eliminare un corso.-->
		<!--		</div>-->
	</div>
	<div id="allGrid" class="row">
		<div class="col-4">
			<div class="list-group" id="list-tab-corsi" role="tablist">
				<a v-for="corso in $store.state.corsi" :key="corso.materia "
				   class="list-group-item list-group-item-action"
				   v-bind:id="'corso-list-'+corso.materia.replace(/\s+/g, '')" data-bs-toggle="list"
				   v-bind:href="'#corso-'+corso.materia.replace(/\s+/g, '')" role="tab"
				   v-bind:aria-controls="'corso-'+corso.materia.replace(/\s+/g, '')">
					{{ corso.materia }}
				</a>
			</div>
		</div>

		<div class="col-8">
			<div class="tab-content">
				<div v-for="insegnamentoCorsi in $store.state.insegnamentoCorsi"
					 :key="insegnamentoCorsi.corso + insegnamentoCorsi.docenti"
					 v-bind:class="{ 'tab-pane fade active show':corsoSelected==insegnamentoCorsi.corso,
                             'tab-pane fade': corsoSelected!=insegnamentoCorsi.corso}"
					 v-bind:id="'corso-'+insegnamentoCorsi.corso.replace(/\s+/g, '')" role="tabpanel"
					 v-bind:aria-labelledby="'corso-list-'+insegnamentoCorsi.corso.replace(/\s+/g, '')">

					<div class="card">
						<div class="card-header">
							Corso: {{ insegnamentoCorsi.corso }}
							<img src="../assets/delate.png" alt="Delate" width="20" height="20"
								 v-on:click="showWarning(insegnamentoCorsi.corso, null)">
						</div>
						<div class="card-body">
							<p>Docenti che insegnano questo corso:</p>
							<ul>
								<div v-for="docenti in insegnamentoCorsi.docenti" :key="docenti.id">
									<li>
										{{ docenti.nome }} {{ docenti.cognome }}
										<img src="../assets/delate.png" alt="Delate" width="20" height="20"
											 v-on:click="showWarning(insegnamentoCorsi.corso,docenti.id)">
									</li>
								</div>
							</ul>
							<hr>
							<form>
								<p> Inserisci un nuovo insegnamento: </p>
								<div class="container">
									<div class="row">
										<div class="col">
											<label>Docente </label>
										</div>
										<div class="col-6">
											<select class="form-select" aria-label="Default select example"
													v-bind:id="'insegnamentoCorsiDocente' +insegnamentoCorsi.corso">
                        <option class="nonDisplay">Seleziona Un Docente</option>
                        <option v-for="docente in $store.state.professori" :key="docente.id"
														v-bind:class="{nonDisplay:insegnamentoCorsi.docenti.find(docenti => docenti.id==docente.id)}"
														v-bind:value=docente.id>
													{{ docente.nome }} {{ docente.cognome }}
												</option>
											</select>
										</div>
										<div class="col-3">
											<button class="btn btn-primary"	v-on:click.prevent="submitInsegnamentoCorsi(insegnamentoCorsi.corso)">Aggiungi</button>
										</div>
									</div>
								</div>
								<br>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="confirmation" ref="confirmation" tabindex="-1" aria-labelledby="confirmationLabel"
		 aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header text-center">
					<h5 class="modal-title" id="teachersBookingLabel">Sei sicuro di voler eliminare il {{this.message}}</h5>
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
	<ErrorShower ref="errShower" message="Operazione non eseguita a causa di un errore inatteso"/>
</template>

<script>
	import {Modal} from "bootstrap";
	import ErrorShower from "@/components/ErrorShower";

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
			// console.log(this.$store.state.corsi);
			this.$store.state.corsi = this.$store.state.corsi.filter(corso => corso.materia != materia);

			this.$store.state.insegnamentoCorsi =
				this.$store.state.insegnamentoCorsi.filter(insegnamentoCorso => insegnamentoCorso.corso != materia);

			this.$store.state.professori.forEach(docente =>
				this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == docente.id).corsi =
					this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == docente.id).corsi.filter(corso => corso.materia != materia));

		} catch (e) {
			this.$refs.errShower.toggle();
			console.log(e);
		}
	}


	async function eliminaInsegnamento(mat, id) {
		try {
			const response = await fetch("/Noodle_war/InsegnamentoDocentiSevlet", {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({docente: id, corso: mat})
			});
			if (response.status == 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			} else if (response.status == 200) {
				this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi =
					this.$store.state.insegnamentoDocenti.find(insegnamentoDocente => insegnamentoDocente.id == id).corsi.filter(corsi => corsi.materia != mat);

				this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti =
					this.$store.state.insegnamentoCorsi.find(insegnamentoCorso => insegnamentoCorso.corso == mat).docenti.filter(docenti => docenti.id != id);

				this.corsoSelected = mat;
			}

		} catch (e) {
			this.$refs.errShower.toggle();
			console.log(e);
		}
	}


	async function getCorsi() {
		try {
			const response = await fetch("/Noodle_war/CorsiServlet");
			if (response.status == 401) {
				window.location.href = "/Noodle_war/login";
				return [];
			}
			return await response.json();
		} catch (e) {
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

	function showWarning(corso, idDocente) {
		if (idDocente == null) {
			this.chois = "eliminaCorso";
			this.corsoDaEliminare = corso;
			this.message = "il corso: " + corso + "?";

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
		this.corsoDaEliminare = undefined;
		this.eliminaInsegnamentoDocente = undefined;
		this.eliminaInsegnamentoCorso = undefined;
	}

	async function confirmChoise() {
		if (this.chois == "eliminaCorso") {
			eliminaCorso.bind(this)(this.corsoDaEliminare);
			this.corsoDaEliminare = undefined;
		} else if (this.chois == "eliminaInsegnamento") {
			eliminaInsegnamento.bind(this)(this.eliminaInsegnamentoCorso, this.eliminaInsegnamentoDocente);
			this.eliminaInsegnamentoDocente = undefined;
			this.eliminaInsegnamentoCorso = undefined;
		} else console.log("errore nella choise");
		this.choise = undefined;
	}

	export default {
		name: "Corsi",
		components: {
			ErrorShower
		},
		async created() {
			if (this.$store.state.corsi == undefined) {
				this.$store.state.corsi = await getCorsi();
			}
			if (this.$store.state.professori == undefined) {
				this.$store.state.professori = await getProfessori();
			}

			if (this.$store.state.insegnamentoDocenti == undefined) {
				this.$store.state.insegnamentoDocenti = await getInsegnamentoDocenti();
			}
			if (this.$store.state.insegnamentoCorsi == undefined) {
				this.$store.state.insegnamentoCorsi = await getInsegnamentoCorsi();
			}

		},
		data() {
			return {
				corso: undefined,
				corsoSelected: "aaa",
				corsoDaEliminare: undefined,
				eliminaInsegnamentoDocente: undefined,
				eliminaInsegnamentoCorso: undefined,
				chooise: undefined,
				message: undefined
			}
		},
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
						this.$store.state.insegnamentoCorsi.push({corso: this.corso, docenti: []})
            this.corso="";
					}
				} catch (e) {
					this.$refs.errShower.toggle();
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
					this.$refs.errShower.toggle();
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

	header .overlay {
		width: 100%;
		height: 100%;
		padding: 50px;
		color: #FFF;
		text-shadow: 1px 1px 1px #333;
		background-image: linear-gradient(135deg, rgb(27, 86, 19) 10%, rgba(27, 215, 52, 0.84) 100%);
	}

	h1 {
		font-family: 'Arial', cursive;
		font-size: 50px;
		margin-top: 30px;
		margin-bottom: 30px;
		text-shadow: 2px 2px #000000;
	}

	.par {
		margin: 3vh auto auto;
		/*text-align: left;*/
		font-weight: bold;
		font-size: 20px;
		margin-bottom: 30px;
		/*margin-left: 30px;*/
	}

	.p3 {
		margin-top: 5px;
		font-size: 17px;
		font-weight: normal;
	}

	.card-body {
		border: 1px solid rgba(0, 0, 0, 0.22);
	}

	.card-header {
		font-weight: bold;
		font-size: 18px;
		background: rgba(65, 164, 51, 0.2);
		border: 1px solid #41a433;
	}

	.btn-primary:hover {
		color: #0a53be;
		background: rgba(161, 196, 195, 0.6);
		border: 1px solid #0a53be;
	}

	ul {
		margin-left: 30%;
		text-align: left;
	}

	li {
		margin: 10px;
	}

	hr {
		margin-top: 30px;
		margin-bottom: 20px;
	}

	#allGrid {
		margin-left: 15%;
		margin-right: 15%;
	}
</style>
