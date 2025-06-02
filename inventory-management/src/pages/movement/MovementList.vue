<template>
  <el-card>
    <div class="title-container">
      <h2>Movimentações</h2>
    </div>
    <el-button id="bot-cadastro" type="primary" @click="drawerVisible = true">Nova Movimentação</el-button>

    <el-select v-model="selectedType" placeholder="Filtrar por tipo" style="margin: 15px 15px 15px 15px; width: 200px;">
      <el-option label="Todos" value=""></el-option>
      <el-option label="ENTRADA" value="ENTRY"></el-option>
      <el-option label="SAÍDA" value="EXIT"></el-option>
    </el-select>

    <el-input
        v-model="searchCode"
        placeholder="Pesquisar por código"
        clearable
        style="width: 200px;"
    />

    <el-table :data="paginatedMovements" style="width: 100%">
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
        :total="filteredMovements.length"
        id="paginacao"
        layout="prev, pager, next, jumper, total"
        prev-text="Anterior"
        next-text="Próximo"
    />

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
import {computed, onMounted, ref, watch} from 'vue';
import {Movement} from "@/types/Movement";
import MovementForm from "@/pages/movement/MovementForm.vue";
import {listarMovimentos} from "@/services/movementService.ts";

const movements = ref<Movement[]>([]);
const selectedType = ref('');
const searchCode = ref('');

const currentPage = ref(1);
const pageSize = ref(10);

const drawerVisible = ref(false);

const filteredMovements = computed(() => {
  return movements.value.filter(movement => {
    const matchesType = !selectedType.value || movement.movementType === selectedType.value;
    const matchesCode = !searchCode.value || movement.productCode?.includes(searchCode.value);
    return matchesType && matchesCode;
  });
});


const totalItems = computed(() => filteredMovements.value.length);

const paginatedMovements = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredMovements.value.slice(start, end);
});

watch([selectedType, searchCode], () => {
  currentPage.value = 1;
});

async function fetchMovements() {
  const response = await listarMovimentos(); // sem paginação no back-end
  movements.value = response.data.content;
}

function handlePageChange(page: number) {
  currentPage.value = page;
  fetchMovements()
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
  padding: 0 !important;
}

#bot-cadastro {
  margin-left: 10px;
}

#paginacao {
  margin-top: 20px;
  margin-left: 5px;
}

:deep(.el-pagination__rightwrapper) {
    flex: none;
}
</style>