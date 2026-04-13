import { defineStore } from "pinia";
import { api } from "../api/axios";

export const useAuthStore = defineStore("auth", {
    state: () => ({
        token: localStorage.getItem('token') || null,
        user: null,
    }),

    actions: {
        async login(credentials) {
            try {
                const response = await api.post('/auth/login', credentials);
                this.token = response.data.token;
                localStorage.setItem('token', this.token);
                api.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
            } catch (error) {
                this.token = null;
                localStorage.removeItem('token');
                delete api.defaults.headers.common['Authorization'];
                throw error;
            }
        },
        logout() {
            this.token = null;
            localStorage.removeItem('token');
            delete api.defaults.headers.common['Authorization'];
        },
    },
});