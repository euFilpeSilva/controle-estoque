<template>
  <el-card id="content">
    <div class="title-container">
      <h2>Lista de Produtos</h2>
    </div>
    <el-button id="bot-cadastro" type="primary" @click="openDrawer()">Cadastrar</el-button>
    <el-select v-model="selectedType" placeholder="Filtrar por tipo" style="margin: 15px 15px 15px 15px; width: 200px;">
      <el-option label="Todos" value=""></el-option>
      <el-option label="Eletrônico" value="ELECTRONICS"></el-option>
      <el-option label="Eletrodoméstico" value="HOME_APPLIANCE"></el-option>
      <el-option label="Móvel" value="FURNITURE"></el-option>
    </el-select>
    <el-table :data="filteredProducts" style="width: 100%">
      <el-table-column prop="description" label="Descrição"/>
      <el-table-column
          prop="type"
          label="Tipo"
          :formatter="(row) => {
    const typeTranslations = {
      ELECTRONICS: 'ELETRÔNICO',
      HOME_APPLIANCE: 'ELETRODOMÉSTICO',
      FURNITURE: 'MÓVEL',
    };
    return typeTranslations[row.type] || row.type;
  }"
      />
      <el-table-column prop="stockQuantity" label="Estoque"/>
      <el-table-column prop="supplierValueFormatted" label="Valor do Fornecedor"/>
      <el-table-column label="Ações">
        <template #default="scope">
          <el-button type="primary" @click="openDrawer(scope.row)">Editar</el-button>
          <el-button type="danger" @click="excluirProduto(scope.row.id)">Excluir</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalItems"
        id="paginacao"
        layout="prev, pager, next, jumper"
        prev-text="Anterior"
        next-text="Próximo">
    </el-pagination>

    <el-drawer
        id="drower"
        :title="drawerTitle"
        v-model="drawerVisible"
        direction="rtl"
        size="35%"
    >
      <ProductForm :product="selectedProduct" @close="closeDrawer" @product-saved="handleProductSaved"/>
    </el-drawer>
  </el-card>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from 'vue';
import {deletarProduto, listarProdutos} from "@/services/productService";
import {Product} from "@/types/Product";
import ProductForm from "@/pages/product/ProductForm.vue";

const produtos = ref<Product[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const totalItems = ref(0);
const drawerVisible = ref(false);
const selectedProduct = ref<Partial<Product> | null>(null);
const selectedType = ref(""); // Store the selected type

const filteredProducts = computed(() => {
  if (!selectedType.value) {
    return produtos.value; // Show all products if no type is selected
  }
  return produtos.value.filter(product => product.type === selectedType.value);
});

const drawerTitle = computed(() => {
  return selectedProduct.value ? "Editar Produto" : "Cadastrar Produto";
});

async function fetchProducts() {
  const response = await listarProdutos(currentPage.value - 1, pageSize.value);
  produtos.value = response.data.content;
  totalItems.value = response.data.totalElements;
}

async function excluirProduto(id: number) {
  try {
    await deletarProduto(id);
    await fetchProducts();
  } catch (error) {
    console.error("Erro ao excluir produto:", error);
  }
}

function handlePageChange(page: number) {
  currentPage.value = page;
  fetchProducts();
}

function openDrawer(product: Partial<Product> | null = null) {
  selectedProduct.value = product;
  drawerVisible.value = true;
}

function closeDrawer() {
  drawerVisible.value = false;
  selectedProduct.value = null;
}

function handleProductSaved() {
  drawerVisible.value = false;
  fetchProducts();
}

onMounted(fetchProducts);
</script>

<style scoped>
#drower {
  flex: 1;
  overflow: auto;
  padding: 0 0 0 0 !important;
}

:deep(.el-drawer__body) {
  padding: 0 !important; /* Remove o padding */
}

.title-container {
  background-color: #4b4747;
  padding: 10px 20px;
  margin-bottom: 15px;
}

.el-card {
  --el-card-padding: 0px 0px 10px 0px;
}

.title-container h2 {
  color: #ffffff;
  margin: 0;
  font-size: 20px;
}

#bot-cadastro {
  margin-left: 10px;
}

#paginacao {
  margin-top: 20px;
  margin-left: 5px;
}
</style>