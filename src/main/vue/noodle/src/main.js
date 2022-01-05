import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createStore } from 'vuex'
import "bootstrap/dist/css/bootstrap.min.css"
const store = createStore({

        state: function(){
            return {
                username:undefined,
                role: "guest",
                miePrenotazioni:undefined,
                prenotazioni:undefined,
                professori:undefined,
                corsi:undefined
        }}
})
createApp(App).use(store).use(router).mount('#app')

import "bootstrap/dist/js/bootstrap.js"