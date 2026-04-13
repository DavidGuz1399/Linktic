import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useNotificationStore = defineStore('notifications', () => {
    const toasts = ref([]);

    const add = (message, type = 'success', duration = 3000) => {
        const id = Date.now();
        toasts.value.push({ id, message, type });

        setTimeout(() => {
            remove(id);
        }, duration);
    };

    const remove = (id) => {
        toasts.value = toasts.value.filter(t => t.id !== id);
    };

    return { toasts, add, remove };
});