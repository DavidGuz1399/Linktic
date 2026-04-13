<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <h2 class="text-2xl font-bold text-gray-800">Control de Inventario</h2>
      <button 
        @click="openInitModal()"
        class="bg-gray-100 hover:bg-gray-200 text-gray-700 px-4 py-2 rounded-lg font-medium transition-all"
      >
        + Inicializar Stock
      </button>
      <button 
        @click="openPurchaseModal()"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium transition-all shadow-sm"
      >
        🛒 Realizar Compra
      </button>
    </div>

    <div class="bg-white shadow-md rounded-xl overflow-hidden border border-gray-200">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID Producto</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Stock Actual</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Estado</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="item in inventory" :key="item.productId" class="hover:bg-gray-50">
            <td class="px-6 py-4 whitespace-nowrap font-mono text-sm text-gray-600">
              {{ item.productId }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap font-bold text-gray-900">
              {{ item.available }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span 
                :class="item.available > 5 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'"
                class="px-2 py-1 rounded-full text-xs font-medium"
              >
                {{ item.available > 5 ? 'Disponible' : 'Stock Bajo' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="isInitModalOpen" class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-2xl shadow-xl max-w-md w-full p-6">
        <h3 class="text-xl font-bold mb-4">Cargar Inventario Inicial</h3>
        
        <form @submit.prevent="handleInitInventory" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Producto sin Inventario</label>
            <select v-model="initForm.productId" class="w-full mt-1 px-3 py-2 border rounded-lg outline-none" required>
              <option v-for="p in availableProducts" :key="p.id" :value="p.id">
                {{ p.name }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Stock Inicial</label>
            <input v-model.number="initForm.available" type="number" min="0" class="w-full mt-1 px-3 py-2 border rounded-lg" required />
          </div>
          <div class="flex justify-end space-x-3 mt-6">
            <button type="button" @click="isInitModalOpen = false" class="px-4 py-2 text-gray-400">Cancelar</button>
            <button type="submit" class="px-4 py-2 bg-emerald-600 text-white rounded-lg">Crear Registro</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="isModalOpen" class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-2xl shadow-xl max-w-md w-full p-6">
        <h3 class="text-xl font-bold mb-4">Nueva Orden de Compra</h3>
        
        <form @submit.prevent="handlePurchase" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Producto</label>
            <select v-model="purchaseForm.productId" class="w-full mt-1 px-3 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500" required>
              <option disabled value="">Seleccione un producto</option>
              <option v-for="p in productsInInventory" :key="p.id" :value="p.id">
                {{ p.name }} (SKU: {{ p.sku }})
              </option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Cantidad</label>
            <input 
              v-model.number="purchaseForm.quantity" 
              type="number" 
              min="1" 
              class="w-full mt-1 px-3 py-2 border rounded-lg outline-none focus:ring-2 focus:ring-blue-500" 
              required 
            />
          </div>

          <div class="flex justify-end space-x-3 mt-6">
            <button type="button" @click="isModalOpen = false" class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button 
              type="submit" 
              :disabled="loading"
              class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-blue-300"
            >
              {{ loading ? 'Procesando...' : 'Confirmar Compra' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import { api, inventoryApi } from '../api/axios';
import { useNotificationStore } from '../stores/notification';

const inventory = ref([]);
const availableProducts = ref([]);
const isInitModalOpen = ref(false);
const isModalOpen = ref(false);
const loading = ref(false);
const notify = useNotificationStore();


const initForm = reactive({
  productId: '',
  available: 0
});

const purchaseForm = reactive({
  productId: '',
  quantity: 1
});

const loadData = async () => {
  try {
    const invRes = await inventoryApi.get('/inventory');
    inventory.value = invRes.data.data;

    const prodRes = await api.get('/products');
    availableProducts.value = prodRes.data.data;
  } catch (error) {
    console.error("Error al cargar datos", error);
  }
};

const openInitModal = () => {
  initForm.productId = '';
  initForm.available = 0;
  isInitModalOpen.value = true;
};

const openPurchaseModal = () => {
  purchaseForm.productId = '';
  purchaseForm.quantity = 1;
  isModalOpen.value = true;
};

const handleInitInventory = async () => {
  try {
    await inventoryApi.post('/inventory', initForm);
    notify.add("Inventario creado", "success");
    isInitModalOpen.value = false;
    await loadData();
  } catch (error) {
    notify.add("Error al crear registro de inventario", "error");
  }
};

const handlePurchase = async () => {
  loading.value = true;
  try {
    await inventoryApi.post('/inventory/purchases', purchaseForm);
    notify.add("¡Compra exitosa!", "success");
    isModalOpen.value = false;
    await loadData();
  } catch (error) {
    notify.add(error.response?.data?.message || "Error en la transacción", "error");
  } finally {
    loading.value = false;
  }
};

const productsInInventory = computed(() => {
  const existingIds = inventory.value.map(item => item.productId);
  return availableProducts.value.filter(p => existingIds.includes(p.id));
});

onMounted(loadData);
</script>