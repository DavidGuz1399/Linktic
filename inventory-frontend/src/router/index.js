import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Catalog from '../views/Catalog.vue';
import Inventory from '../views/Inventory.vue';
import { useAuthStore } from '../stores/auth';

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login,
    },
    {
        path: '/catalog',
        name: 'Catalog',
        component: Catalog,
        meta: {
            requiresAuth: true,
        },
    },
    {
        path: '/inventory',
        name: 'Inventory',
        component: Inventory,
        meta: {
            requiresAuth: true,
        },
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    if (to.meta.requiresAuth && !authStore.token) {
        next({ name: 'Login' });
    } else {
        next();
    }
});

export default router;