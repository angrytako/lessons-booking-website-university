import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import  {store} from "../main.js"

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/logout',
    name: 'Logout',
    component: () => import('../views/Logout.vue'),
    /*beforeEnter(to, from, next) {
      //check authentication
      if(!store.state.role || store.state.role != "cliente" || store.state.role != "amministratore" )
        next({ name: 'Login'});
      else next();
    }*/
  },
  {
    path: '/prenotazioni',
    name: 'Prenotazioni',
    component: () => import('../views/Prenotazioni.vue'),
    beforeEnter (to, from, next) {

      if (to.query.username) {
        next({ name: 'MiePrenotazioni', query: to.query });
        return;
      } else {
        //check authentication
        if(!store.state.role || store.state.role != "amministratore" ) {
          next({name: 'Login'});
          return;
        }
        next();
      }
    }
  },
  {
    path: '/prenotazioni',
    name: 'MiePrenotazioni',
    component: () => import('../views/MiePrenotazioni.vue'),
    beforeEnter(to, from, next) {
      //check authentication
      if(!store.state.role || store.state.role != "cliente" || store.state.role != "amministratore" )
        next({ name: 'Login'});
      else next();
    }},
  {
    path: '/professori',
    name: 'Professori',
    component: () => import('../views/Professori.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router