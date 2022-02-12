export const realState = {
	username:undefined,
	role: "guest",
	miePrenotazioni:undefined,
	prenotazioni:undefined,
	corsi:undefined,
	professori:undefined,
	insegnamentoCorsi:undefined,
	insegnamentoDocenti:undefined,
	slots: undefined
}
export const lorenzo = {
	username:"Lorenzo",
	role: "cliente",
	miePrenotazioni:[
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"effettuata",giorno:0,orario:0 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:3,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:0,orario:3 },
	],
	prenotazioni:undefined,
	slots: [
		{ course:"Algoritmi", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false},
				{id:1, nome: "Lilli", cognome: "Vagabondo", rimosso: false}],day:1,time:0 },
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:1,time:0 },
		{ course:"Fisica", teacherList:[{id:1, nome: "Lilli", cognome: "Vagabondo", rimosso: false},
				{id:2, nome: "Pippi", cognome: "Calzelunghe", rimosso: false}],day:1,time:0 },
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:2,time:3 },
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:1,time:2 },
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:4,time:3 },
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:1,time:1 },
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:3,time:3 },
	]
}
export const toneTuga = {
	username:"Lorenzo",
	role: "amministratore",
	miePrenotazioni:[
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"effettuata",giorno:0,orario:0 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:3,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:0,orario:3 },
	],
	prenotazioni:[
		{ corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"attiva",giorno:3,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:0,orario:2 },
		{ corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"effettuata",giorno:4,orario:3 },
		{ corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"cancellata",giorno:2,orario:1 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:2 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0 },
		{ corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"effettuata",giorno:0,orario:0 },
		{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1 },
	],
	corsi:[
		{ materia:"Architetture", rimosso: false },
		{ materia:"LPP", rimosso: false },
		{ materia:"SAS", rimosso: false},
		{ materia:"RetiI", rimosso: false},
		{ materia:"ProgrammazioneIII", rimosso: false },
		{ materia:"Sistemi intelligenti", rimosso: false },
		{ materia:"TWEB", rimosso: false }
	],
	professori: [
		{ id:1, nome:"Liliana", cognome:"Ardissono", rimosso:false },
		{ id:2, nome:"Viviana", cognome:"Bono", rimosso:false },
		{ id:3, nome:"Viviana", cognome:"Patti", rimosso:false },
		{ id:4, nome:"Marco", cognome:"Aldinucci", rimosso:false },
		{ id:5, nome:"Marco", cognome:"Botta", rimosso:false },
		{ id:6, nome:"Claudio", cognome:"Schifanella", rimosso:false },
		{ id:7, nome:"Luca", cognome:"Padovani", rimosso:false },
		{ id:8, nome:"Felice", cognome:"Cardone", rimosso:false },
		{ id:9, nome:"Rossano", cognome:"Schifanella", rimosso:false },
		{ id:10, nome:"Marco", cognome:"Segnan", rimosso:false },
	],
	insegnamentoCorsi : [
		{ corso: "TWEB", docenti:[{ id:1, nome:"Liliana", cognome:"Ardissono", rimosso:false }]},
		{ corso: "LPP", docenti:[{ id:2,nome:"Viviana", cognome:"Bono", rimosso:false }]},
		{ corso: "ProgrammazioneIII", docenti:[{ id:1, nome:"Liliana", cognome:"Ardissono", rimosso:false },
				{ id:2, nome:"Viviana", cognome:"Bono", rimosso:false }]}
	],
	insegnamentoDocenti: [
		{ id: 1, corsi: [{ materia:"TWEB", rimosso: false }, { materia:"ProgrammazioneIII", rimosso: false }] },
		{ id: 2, corsi: [{ materia:"ProgrammazioneIII", rimosso: false }, { materia:"LPP", rimosso: false }] }
	],
	slots: [
		{ course:"Algoritmi", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false},
				{id:1, nome: "Lilli", cognome: "Vagabondo", rimosso: false}],day:1,time:0},
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:1,time:0},
		{ course:"Fisica", teacherList:[{id:1, nome: "Lilli", cognome: "Vagabondo", rimosso: false},
				{id:2, nome: "Pippi", cognome: "Calzelunghe", rimosso: false}],day:1,time:0},
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:2,time:3},
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:1,time:2},
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:4,time:3},
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:1,time:1},
		{ course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:3,time:3},
	]
}
