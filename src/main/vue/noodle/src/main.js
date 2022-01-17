import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createStore } from 'vuex'
import "bootstrap/dist/css/bootstrap.min.css"

import { realState, lorenzo, toneTuga } from "@/mockUserData";

export const store = createStore({

        state: function(){

            return lorenzo }
})
createApp(App).use(store).use(router).mount('#app')
