<template>
  <div class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
    <div class="bg-white rounded-2xl shadow-xl max-w-md w-full p-6">
      <h3 class="text-xl font-bold mb-4 text-gray-800">
        {{ isEditing ? 'Editar Producto' : 'Crear Nuevo Producto' }}
      </h3>
      
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">Nombre</label>
          <input 
            v-model="localForm.name" 
            type="text" 
            id="name-input"
            class="w-full mt-1 px-3 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-emerald-500" 
            required 
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">SKU</label>
          <input 
            v-model="localForm.sku" 
            type="text" 
            class="w-full mt-1 px-3 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-emerald-500" 
            required 
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Precio</label>
          <input 
            v-model.number="localForm.price" 
            type="number" 
            step="0.01" 
            class="w-full mt-1 px-3 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-emerald-500" 
            required 
          />
        </div>

        <div class="flex justify-end space-x-3 mt-6">
          <button 
            type="button" 
            @click="$emit('close')" 
            class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg"
          >
            Cancelar
          </button>
          <button 
            type="submit" 
            class="px-4 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700 transition-colors"
          >
            {{ isEditing ? 'Actualizar' : 'Guardar' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue';

const props = defineProps({
  initialData: { type: Object, default: () => ({ id: null, name: '', sku: '', price: 0 }) },
  isEditing: { type: Boolean, default: false }
});

const emit = defineEmits(['save', 'close']);

const localForm = reactive({ ...props.initialData });

watch(() => props.initialData, (newData) => {
  Object.assign(localForm, newData);
}, { deep: true });

const handleSubmit = () => {
  emit('save', { ...localForm });
};
</script>