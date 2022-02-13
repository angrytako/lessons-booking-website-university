import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createStore } from 'vuex'
import "bootstrap/dist/css/bootstrap.min.css"

import { realState, lorenzo, toneTuga } from "@/mockUserData";


async function getUserInfo() {
    try {
        const response = await fetch("/Noodle_war/MyInfoServlet");
        return await response.json();
    } catch (e) {
        console.log(e);
    }

}



const store = createStore({

        state: function(){

            return realState },
    actions: {
        initApp(context, data) {
            return new Promise((resolve, reject) => {
                // Do something here... lets say, a http call using vue-resource
                    if(!this.state.username) {
                        getUserInfo().then((userInfo => {
                            this.state.role = userInfo.role ? userInfo.role : "guest";
                            this.state.username = userInfo.username ? userInfo.username : undefined;
                            resolve();
                        })).catch((e) => reject(e));
                    }
                    else{resolve()}
            })
        }
    }
})
export default createApp(App).use(store).use(router).mount('#app')
