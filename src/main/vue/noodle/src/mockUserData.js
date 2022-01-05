export const realState = {
    username:undefined,
    role: "guest",
    miePrenotazioni:undefined,
    prenotazioni:undefined
}
export const lorenzo = {
    username:"Lorenzo",
    role: "cliente",
    miePrenotazioni:[{ corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"attiva",giorno:1,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"effettuata",giorno:0,orario:0},
        { corso:"Architetture", docente:"Pippo", utente:"Lorenzo",stato:"cancellata",giorno:2,orario:1}],
    prenotazioni:undefined
}
export const toneTuga = {
    username:"ToneTuga",
    role: "amministratore",
    miePrenotazioni:[],
    prenotazioni:[]
}