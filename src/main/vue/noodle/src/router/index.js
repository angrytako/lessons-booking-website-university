import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/Home.vue'

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
		component: () => import('../views/Logout.vue')
	},
	{
		path: '/prenotazioni',
		name: 'Prenotazioni',
		component: () => import('../views/Prenotazioni.vue'),
		beforeEnter(to, from, next) {
			if (to.query.username) {
				next({name: 'MiePrenotazioni', query: to.query});
			} else {
				next();
			}
		}
	},
	{
		path: '/prenotazioni',
		name: 'MiePrenotazioni',
		component: () => import('../views/MiePrenotazioni.vue')
	}
]

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes
})

export default router
