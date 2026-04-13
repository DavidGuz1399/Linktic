<template>
    <nav class="bg-white shadow-sm border-b border-gray-200">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center space-x-8">
            <h1 class="text-xl font-bold text-emerald-600 flex items-center">
              <span class="mr-2">🚀</span> Linktic
            </h1>
            
            <div class="hidden md:flex space-x-4">
              <router-link 
                to="/catalog" 
                class="text-gray-600 hover:text-emerald-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                active-class="text-emerald-600 bg-emerald-50"
              >
                Catálogo
              </router-link>
              <router-link 
                to="/inventory" 
                class="text-gray-600 hover:text-emerald-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                active-class="text-emerald-600 bg-emerald-50"
              >
                Inventario
              </router-link>
            </div>
          </div>

          <div class="flex items-center">
            <button 
              @click="handleLogout"
              class="ml-4 bg-gray-100 hover:bg-gray-200 text-gray-700 px-4 py-2 rounded-lg text-sm font-medium transition-all"
            >
              Cerrar Sesión
            </button>
          </div>
        </div>
      </div>
    </nav>
</template>

<script setup>
import { useAuthStore } from '../stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const handleLogout = () => {
  authStore.logout();
  router.push('/');
};
</script>