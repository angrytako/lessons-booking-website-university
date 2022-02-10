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
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:3,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:0,orario:3},
    ],
    prenotazioni:undefined,
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
export const toneTuga = {
    username:"Lorenzo",
    role: "amministratore",
    miePrenotazioni:[
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"effettuata",giorno:0,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:3,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:0,orario:3},
    ],
    prenotazioni:[
        { corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"attiva",giorno:3,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:0,orario:2},
        { corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"effettuata",giorno:4,orario:3},
        { corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"cancellata",giorno:2,orario:1},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:2},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Anna",stato:"effettuata",giorno:0,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1},
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
