<template>
  <div class="min-h-screen bg-gray-50 font-sans text-gray-900">
    <NavBarComponent v-if="authStore.token" />

    <ToastContainer />
    <main :class="authStore.token ? 'max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8' : ''">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import ToastContainer from './components/ToastContainer.vue';
import { useAuthStore } from './stores/auth';
import NavBarComponent from './components/NavBarComponent.vue';

const authStore = useAuthStore();
</script>

<style>
/* Animación suave entre cambios de ruta */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>