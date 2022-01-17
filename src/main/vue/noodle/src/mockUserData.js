export const realState = {
    username:undefined,
    role: "guest",
    miePrenotazioni:undefined,
    prenotazioni:undefined,
    slots: undefined
}
export const lorenzo = {
    username:"Lorenzo",
    role: "cliente",
    miePrenotazioni:[
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"effettuata",giorno:0,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1}],
    prenotazioni:undefined,
    slots: [
        { course:"Algoritmi", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false},
                                            {id:1, nome: "Lilli", cognome: "Vagabondo", rimosso: false}],day:1,time:0},
        { course:"Architettura", teacherList:[{id:0, nome: "Pippo", cognome: "Apo", rimosso: false}],day:1,time:0},
        { course:"Fisica", teacherList:[{id:1, nome: "Lilli", cognome: "Vagabondo", rimosso: false},
                                        {id:2, nome: "Pippi", cognome: "Calzelunghe", rimosso: false}],day:1,time:0}]
}
export const toneTuga = {
    username:"ToneTuga",
    role: "amministratore",
    miePrenotazioni:[],
    prenotazioni:[]
}
