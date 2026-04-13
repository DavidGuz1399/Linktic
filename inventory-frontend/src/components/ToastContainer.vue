<template>
  <div class="fixed top-4 right-4 z-[100] flex flex-col gap-3 w-80">
    <TransitionGroup name="list">
      <div 
        v-for="toast in store.toasts" 
        :key="toast.id"
        :class="[
          'p-4 rounded-xl shadow-lg border flex items-center justify-between transition-all duration-300',
          toast.type === 'success' ? 'bg-white border-emerald-100 text-emerald-800' : 'bg-white border-red-100 text-red-800'
        ]"
      >
        <div class="flex items-center gap-3">
          <span v-if="toast.type === 'success'" class="text-emerald-500 text-xl">✅</span>
          <span v-else class="text-red-500 text-xl">⚠️</span>
          <p class="text-sm font-medium">{{ toast.message }}</p>
        </div>
        <button @click="store.remove(toast.id)" class="text-gray-400 hover:text-gray-600 ml-4">
          ✕
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { useNotificationStore } from '../stores/notification';
const store = useNotificationStore();
</script>

<style scoped>
.list-enter-from { opacity: 0; transform: translateX(30px); }
.list-leave-to { opacity: 0; transform: scale(0.9); }
</style>