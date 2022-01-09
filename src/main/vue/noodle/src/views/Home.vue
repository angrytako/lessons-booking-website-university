<template>
	<div class="home">
		<img alt="Vue logo" src="../assets/logo.png">
	</div>

	Tabella delle ripetizioni:

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

		<tbody>
			<tr v-for="(slotTime, indexTime) in matrCorsi" :key="indexTime">
				<th scope="row">{{times[indexTime]}}</th>
<!--				slotTime = matrCorsi[indexTime] qui sotto :)-->
				<td v-for="(slotDay, indexDay) in slotTime" :key="indexDay">
<!--					slotDay = matrCorsi[indexTime][indexDay] qui sotto :)-->
					<p v-for="(slot, indexSlot) in slotDay" :key="indexSlot">
						{{slot.course}}
					</p>
				</td>
			</tr>
		</tbody>
	</table>


	<ul>
<!--		prima un for sugli slot e poi un for sui docenti nell'array di docenti-->
		<li v-for="slotAll in $store.state.slots"
			:key="slotAll.course + slotAll.day + slotAll.time">
			<div class="course">{{ slotAll.course }}</div>
			<div class="teacher" v-for="teacher in slotAll.teacherList">
				{{ teacher.nome + " " + teacher.cognome}}
			</div>
			<div class="day">{{ slotAll.day }}</div>
			<div class="time">{{ slotAll.time }}</div>
		</li>
	</ul>
</template>

<script>
async function getSlots() {
	try {
		const response = await fetch("/Noodle_war/AvailableSlotsServlet");
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
	name: 'Home',
	async created() {
		this.$store.state.slots = await getSlots();
		/*[{corso, docenteNome, docenteCognome, giorno, ora}...]*/
		/*[0:[0:[0:corso1,1:corso2, etc...],[1:[0:...]],1:[],2:[],3:[]]*/
		for(let slot of this.$store.state.slots){
			this.matrCorsi[slot.time][slot.day].push(slot);
		}
	},
	data() {
		return {
			matrCorsi: [
				[[],[],[],[],[]],
				[[],[],[],[],[]],
				[[],[],[],[],[]],
				[[],[],[],[],[]]
			],
			days: { 0:"Lunedì", 1:"Martedì", 2:"Mercoledì", 3:"Giovedì", 4:"Venerdì"},
			times:{ 0: "15-16", 1: "16-17", 2: "17-18", 3: "18-19"}
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
}

.light{
	color: white;
	background-color: #41a433;
}

.dark{
	color: white;
	background-color: #0f5132;
}

.home {
	background-color: #f2e7c3;
}

li {
	text-decoration: none;
}

.slot {
	display: flex;
}

</style>
