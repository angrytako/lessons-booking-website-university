import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home.vue'
import app from "../main.js"

const routes = [
	{
		path: '/',
		name: 'Home',
		component: Home
	},
	{
		path: '/login',
		name: 'Login',
		component: () => import('../views/Login.vue')
	},
	{
		path: '/logout',
		name: 'Logout',
		component: () => import('../views/Logout.vue')
	},
	{
		path: '/prenotazioni',
		name: 'Prenotazioni',
		component: () => import('../views/Prenotazioni.vue'),
		beforeEnter(to, from, next) {
			app.$store.dispatch('initApp').then(response => {

				if (to.query.username) {
				next({name: 'MiePrenotazioni', query: to.query});
				return;
			} else {
				//check authentication
				if (!app.$store.state.role || app.$store.state.role !== "amministratore") {
					next({name: 'Login'});			//manda in login
					return;
				}
				next();
			}}, error => {
				console.log(error);
			})
		}
	},
	{
		path: '/prenotazioni',
		name: 'MiePrenotazioni',
		component: () => import('../views/MiePrenotazioni.vue'),
		beforeEnter(to, from, next) {
			app.$store.dispatch('initApp').then(response => {
			//check authentication
			if (!app.$store.state.role || (app.$store.state.role != "cliente" && app.$store.state.role != "amministratore"))
				next({name: 'Login'});
			else next();}, error => {
				console.log(error);
			})
		}
	},
	{
		path: '/docenti',
		name: 'Docenti',
		component: () => import('../views/Docenti.vue'),
		beforeEnter(to, from, next) {
			app.$store.dispatch('initApp').then(response => {
					if (!app.$store.state.role || app.$store.state.role !== "amministratore") {
						next({name: 'Login'});
						return;
					}
					next();
				}, error => {
				console.log(error);
			})}
	},
	{
		path: '/corsi',
		name: 'Corsi',
		component: () => import('../views/Corsi.vue'),
		beforeEnter(to, from, next) {
			app.$store.dispatch('initApp').then(response => {
				if (!app.$store.state.role || app.$store.state.role !== "amministratore") {
					next({name: 'Login'});
					return;
				}
				next();
			}, error => {
				console.log(error);
			})}
	},
]

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes
})

export default router
