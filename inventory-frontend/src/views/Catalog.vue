<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <h2 class="text-2xl font-bold text-gray-800">Catálogo de Productos</h2>
      <button 
        @click="openModal()"
        class="bg-emerald-600 hover:bg-emerald-700 text-white px-4 py-2 rounded-lg font-medium transition-all flex items-center"
      >
        <span class="mr-2">+</span> Nuevo Producto
      </button>
    </div>

    <div class="bg-white shadow-md rounded-xl overflow-hidden border border-gray-200">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Producto</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sku</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Precio</th>
            <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Acciones</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900">{{ product.name }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              {{ product.sku }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">
              ${{ product.price }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-3">
              <button @click="openModal(product)" class="text-emerald-600 hover:text-emerald-900">Editar</button>
              <button @click="viewInventory(product.id)" class="text-blue-600 hover:text-blue-900">Inventario</button>
            </td>
          </tr>
          <tr v-if="products.length === 0">
            <td colspan="4" class="px-6 py-10 text-center text-gray-500">No hay productos disponibles.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <ProductFormComponent
      v-if="isModalOpen"
      :initialData="form"
      :isEditing="isEditing"
      @save="saveProduct"
      @close="isModalOpen = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { api } from '../api/axios';
import ProductFormComponent from '../components/ProductFormComponent.vue';
import { useNotificationStore } from '../stores/notification';

const router = useRouter();
const products = ref([]);
const notify = useNotificationStore();
const isModalOpen = ref(false);
const isEditing = ref(false);

const form = reactive({
  id: null,
  name: '',
  sku: '',
  price: 0
});

const fetchProducts = async () => {
  try {
    const response = await api.get('/products');
    products.value = response.data.data;
  } catch (error) {
    console.error("Error cargando productos", error);
  }
};

const openModal = (product = null) => {
  if (product) {
    isEditing.value = true;
    Object.assign(form, product);
  } else {
    isEditing.value = false;
    form.id = null;
    form.name = '';
    form.sku = '';
    form.price = 0;
  }
  isModalOpen.value = true;
};

const saveProduct = async (productData) => {
  try {
    if (isEditing.value) {
      await api.put(`/products/${productData.id}`, productData);
    } else {
      await api.post('/products', productData);
    }
    await fetchProducts();
    isModalOpen.value = false;
    notify.add("Producto guardado exitosamente", "success");
  } catch (error) {
    notify.add("Error al guardar el producto", "error");
  }
};

const viewInventory = (id) => {
  router.push({ path: '/inventory', query: { productId: id } });
};

onMounted(fetchProducts);
</script>