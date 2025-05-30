<!--<template>-->
<!--  <el-card>-->
<!--    <h2>Editar Produto</h2>-->
<!--    <el-form :model="produto" label-width="120px">-->
<!--      <el-form-item label="Descrição">-->
<!--        <el-input v-model="produto.description" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Tipo">-->
<!--        <el-input v-model="produto.type" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Estoque">-->
<!--        <el-input type="number" v-model="produto.stockQuantity" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Valor do Fornecedor">-->
<!--        <el-input type="number" v-model="produto.supplierValue" />-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" @click="salvarProduto">Salvar</el-button>-->
<!--        <el-button @click="voltar">Cancelar</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </el-card>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--import { ref, onMounted } from 'vue';-->
<!--import {salvarProduto, listarProdutosSemPaginacao, editarProduto} from "@/services/productService";-->
<!--import { Product } from "@/types/Product";-->
<!--import { useRoute, useRouter } from "vue-router";-->

<!--const route = useRoute();-->
<!--const router = useRouter();-->
<!--const produto = ref<Product>({-->
<!--  id: 0,-->
<!--  description: "",-->
<!--  type: "",-->
<!--  stockQuantity: 0,-->
<!--  supplierValue: 0,-->
<!--});-->

<!--async function fetchProduto() {-->
<!--  const id = route.params.id;-->
<!--  const response = await listarProdutosSemPaginacao();-->
<!--  const produtoEncontrado = response.data.find((p: Product) => p.id === Number(id));-->
<!--  if (produtoEncontrado) {-->
<!--    produto.value = produtoEncontrado;-->
<!--  }-->
<!--}-->

<!--async function salvarProduto() {-->
<!--  try {-->
<!--    await editarProduto(produto.value);-->
<!--    await router.push('/produtos'); // Redirect to the product list after saving-->
<!--  } catch (error) {-->
<!--    console.error("Erro ao salvar produto:", error);-->
<!--  }-->
<!--}-->

<!--function voltar() {-->
<!--  router.push('/produtos'); // Redirect back to the product list-->
<!--}-->

<!--onMounted(fetchProduto);-->
<!--</script>-->