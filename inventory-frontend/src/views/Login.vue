<template>
  <div class="min-h-screen flex items-center justify-center px-4">
    <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8 border border-gray-100">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-extrabold text-gray-900">Bienvenido</h2>
        <p class="text-gray-500 mt-2">Gestión de Inventario</p>
      </div>
      
      <form @submit.prevent="handleLogin" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Usuario</label>
          <input 
            v-model="credentials.username" 
            type="text" 
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition-all"
            placeholder="admin"
            required
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Contraseña</label>
          <input 
            v-model="credentials.password" 
            type="password" 
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition-all"
            placeholder="********"
            required
          />
        </div>

        <button 
          type="submit" 
          :disabled="isLoading"
          class="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-2 px-4 rounded-lg transition-colors disabled:bg-emerald-300 disabled:cursor-not-allowed"
        >
          {{ isLoading ? 'Autenticando...' : 'Iniciar Sesión' }}
        </button>

        <p v-if="errorMessage" class="text-red-500 text-sm text-center font-medium">
          {{ errorMessage }}
        </p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useAuthStore } from '../stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const credentials = reactive({
  username: '',
  password: ''
});

const isLoading = ref(false);
const errorMessage = ref('');

const handleLogin = async () => {
  console.log(credentials);
  isLoading.value = true;
  errorMessage.value = '';
  
  try {
    await authStore.login(credentials);
    router.push('/catalog');
  } catch (error) {
    errorMessage.value = 'Credenciales inválidas. Intenta de nuevo.';
  } finally {
    isLoading.value = false;
  }
};
</script>