<template>
  <el-container class="sidebar-container">
    <el-aside width="200px">
      <el-menu :default-active="active" mode="vertical" style="border-right: none">
        <el-menu-item index="1" @click="() => $router.push('/produtos')">Produtos</el-menu-item>
        <el-menu-item index="2" @click="() => $router.push('/movimentacoes')">Movimentações</el-menu-item>
        <el-menu-item index="3" @click="() => $router.push('/relatorio/lucro')">Relatório de Lucro</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container style="background-color: #ffffff;">
      <el-header class="title-header">
        <h1 style="margin: 0; padding: 10px 20px;">Gestão de Estoque</h1>
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import {ref} from 'vue';

const active = ref('1');
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

:root {
  --font-family: 'Inter', sans-serif;
  --primary-color: #111010;
  --secondary-color: #d6d4d1;
  --el-menu-bg-color: #d4c0c0 !important;
  --background-color: #dbdada;
  --header-text-color: #0a0a0a;
  --header-shadow: 0px 4px 6px rgba(31, 30, 30, 0.1);
  --border-color: #d4cfcf;
}

.sidebar-container {
  height: 100vh;
  background-color: #4b4747;
  color: #e5d8d8;
}

.title-header {
  font-family: Serifa, sans-serif;
  font-size: 24px;
  font-weight: 600;
  color: #131313;
  margin: 0;
}

.aside-menu {
  background-color: #e0e7e0;
  color: #e0d3d3;
}

.el-menu {
  background-color: #4b4747;
}

.el-menu-item {
  color: #ffffff;
}
</style>