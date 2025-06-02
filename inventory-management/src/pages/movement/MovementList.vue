<template>
  <el-card>
    <div class="title-container">
      <h2>Movimentações</h2>
    </div>
    <el-button id="bot-cadastro" type="primary" @click="drawerVisible = true">Nova Movimentação</el-button>
    <el-table :data="movements" style="width: 100%">
      <el-table-column label="Produto">
        <template #default="scope">
          {{ scope.row.productCode }} - {{ scope.row.productDescription }}
        </template>
      </el-table-column>

      <el-table-column
          prop="movementType"
          label="Tipo"
          :formatter="(row) => row.movementType === 'ENTRY' ? 'ENTRADA' : 'SAÍDA'"
      />
      <el-table-column prop="quantityMovement" label="Quantidade"/>
      <el-table-column prop="salePriceFormatted" label="Valor venda"/>
      <el-table-column prop="saleDateFormatted" label="Data"/>
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
        title="Cadastrar Nova Movimentação"
        v-model="drawerVisible"
        direction="rtl"
        size="35%"
    >
      <MovementForm @close="drawerVisible = false" @movement-saved="handleMovementSaved" product/>
    </el-drawer>
  </el-card>
</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import {Movement} from "@/types/Movement";
import MovementForm from "@/pages/movement/MovementForm.vue";
import {listarMovimentos} from "@/services/movementService.ts";

const movements = ref<Movement[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const totalItems = ref(0);
const drawerVisible = ref(false);

async function fetchMovements() {
  const response = await listarMovimentos(currentPage.value - 1, pageSize.value);
  movements.value = response.data.content;
  totalItems.value = response.data.totalElements;
}

function handlePageChange(page: number) {
  currentPage.value = page;
  fetchMovements();
}

function handleMovementSaved() {
  drawerVisible.value = false;
  fetchMovements();
}

onMounted(fetchMovements);
</script>

<style scoped>
#drower {
  flex: 1;
  overflow: auto;
  padding: 0 0 0 0 !important;
}

.title-container {
  background-color: #4b4747;
  padding: 10px 20px;
  margin-bottom: 15px;
}

.title-container h2 {
  color: #ffffff;
  margin: 0;
  font-size: 20px;
}

.el-card {
  --el-card-padding: 0px 0px 10px 0px;
}

:deep(.el-drawer__body) {
  padding: 0 !important; /* Remove o padding */
}

#bot-cadastro {
  margin-left: 10px;
}

#paginacao {
  margin-top: 20px;
  margin-left: 5px;
}
</style>