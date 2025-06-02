<template>
  <el-card>
    <div class="title-container">
      <h2>Relatório de Lucro</h2>
    </div>
    <el-form :model="filters" label-width="120px">
      <el-form-item label="Data Inicial">
        <el-date-picker v-model="filters.startDate" type="date"/>
      </el-form-item>
      <el-form-item label="Data Final">
        <el-date-picker v-model="filters.endDate" type="date"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="buscarLucro">Buscar</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="lucro.content" style="width: 100%">
      <el-table-column label="Produto">
        <template #default="scope">
          {{ scope.row.code }} - {{ scope.row.description }}
        </template>
      </el-table-column>
      <el-table-column prop="totalProfitFormatted" label="Lucro Total"/>
    </el-table>
    <div class="pagination-container">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
          :current-page="pagination.page"
          :page-size="pagination.size"
          :total="lucro.totalElements"
          layout="total, sizes, prev, pager, next, jumper"
          class="custom-pagination"
          prev-text="Anterior"
          next-text="Próximo"
      >
        <template #jumper>
          <span>Ir para</span>
        </template>
      </el-pagination>
    </div>
  </el-card>
</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import {listarLucroPaginado} from "@/services/productService";

const filters = ref({
  startDate: null,
  endDate: null,
});

const lucro = ref({
  content: [],
  totalElements: 0,
});

const pagination = ref({
  page: 1,
  size: 10,
});

async function buscarLucro() {
  try {
    const res = await listarLucroPaginado(
        pagination.value.page - 1,
        pagination.value.size,
        filters.value.startDate,
        filters.value.endDate
    );
    lucro.value = res.data;
    console.log("Lucro carregado:", lucro.value);
  } catch (error) {
    console.error("Erro ao buscar lucro:", error);
  }
}

function handlePageChange(newPage: number) {
  pagination.value.page = newPage;
  buscarLucro();
}

function handleSizeChange(newSize: number) {
  pagination.value.size = newSize;
  buscarLucro();
}

onMounted(buscarLucro);
</script>

<style scoped>
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

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.custom-pagination {
  --el-pagination-bg-color: #f5f5f5;
  --el-pagination-border-color: #dcdcdc;
  --el-pagination-hover-bg-color: #e6e6e6;
  --el-pagination-active-bg-color: #409eff;
  --el-pagination-active-color: #ffffff;
  --el-pagination-font-size: 14px;
}

.custom-pagination .el-pagination__pager li {
  border-radius: 4px;
  padding: 5px 10px;
  margin: 0 5px;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.custom-pagination .el-pagination__pager li:hover {
  background-color: var(--el-pagination-hover-bg-color);
}

.custom-pagination .el-pagination__pager li.is-active {
  background-color: var(--el-pagination-active-bg-color);
  color: var(--el-pagination-active-color);
}
</style>