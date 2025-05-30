<template>
  <el-card id="product-form-card">
    <el-form :model="form" :rules="rules" ref="productForm" label-width="120px">
      <el-form-item label="Descrição" prop="description" label-width="170px">
        <el-input v-model="form.description" />
      </el-form-item>
      <el-form-item label="Tipo" prop="type" label-width="175px">
        <el-select v-model="form.type" placeholder="Selecione">
          <el-option label="Eletrônico" value="ELECTRONICS" />
          <el-option label="Eletrodoméstico" value="HOME_APPLIANCE" />
          <el-option label="Móvel" value="FURNITURE" />
        </el-select>
      </el-form-item>
      <el-form-item label="Valor do Fornecedor" prop="supplierValue" label-width="180px">
        <el-input
            :model-value="formattedSupplierValue"
            @update:model-value="handleInput"
            placeholder="Digite o valor"
        />
      </el-form-item>
      <el-form-item label="Quantidade em Estoque" prop="stockQuantity" label-width="180px">
        <el-input-number v-model="form.stockQuantity" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :disabled="!isFormValid" @click="salvar" style="margin-left: 60px">Salvar</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import type { Product } from "@/types/Product.ts";
import {editarProduto, salvarProduto} from "@/services/productService.ts";
import { ElMessage } from "element-plus";

const router = useRouter();
const form = ref<Partial<Product>>({
  description: '',
  supplierValue: 0,
  stockQuantity: 0,
  type: '',
});

const rules = {
  description: [{ required: true, message: 'Descrição é obrigatória', trigger: 'blur' }],
  type: [{ required: true, message: 'Tipo é obrigatório', trigger: 'change' }],
  supplierValue: [{ required: true, message: 'Valor do Fornecedor é obrigatório', trigger: 'blur' }],
  stockQuantity: [{ required: true, message: 'Quantidade em Estoque é obrigatória', trigger: 'blur' }],
};

const productForm = ref();
const isFormValid = ref(false);

watch(form, async () => {
  if (productForm.value) {
    isFormValid.value = await productForm.value.validate().then(() => true).catch(() => false);
  }
}, { deep: true });

const props = defineProps<{ product: Partial<Product> | null }>();
const emit = defineEmits(['product-saved']);
watch(
    () => props.product,
    (newProduct) => {
      if (newProduct) {
        form.value = { ...newProduct };
      } else {
        form.value = { description: '', supplierValue: 0, stockQuantity: 0, type: '' };
      }
    },
    { immediate: true }
);

async function salvar() {
  try {
    if (form.value.id) {
      // Modo de edição
      await editarProduto(form.value as Product);
      ElMessage({
        message: 'Produto editado com sucesso!',
        type: 'success',
        iconClass: 'custom-toast-icon',
        duration: 3000,
        showClose: true,
      });
    } else {
      // Modo de criação
      await salvarProduto(form.value as Product);
      ElMessage({
        message: 'Produto salvo com sucesso!',
        type: 'success',
        iconClass: 'custom-toast-icon',
        duration: 3000,
        showClose: true,
      });
    }
    emit('product-saved');
    form.value = {
      description: '',
      supplierValue: 0,
      stockQuantity: 0,
      type: '',
    };
    await router.push('/produtos');
  } catch (error) {
    ElMessage({
      message: 'Erro ao salvar o produto!',
      type: 'error',
      iconClass: 'custom-toast-icon-error',
      duration: 3000,
      showClose: true,
    });
  }
}

const formattedSupplierValue = ref('');

function formatCurrency(value: number): string {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
    minimumFractionDigits: 2,
  }).format(value || 0);
}

function handleInput(value: string) {
  const onlyNumbers = value.replace(/\D/g, '');

  const numericValue = parseFloat((parseInt(onlyNumbers || '0', 10) / 100).toFixed(2));

  form.value.supplierValue = numericValue;
  formattedSupplierValue.value = formatCurrency(numericValue);
}

watch(
    () => form.value.supplierValue,
    (newValue) => {
      formattedSupplierValue.value = formatCurrency(newValue);
    },
    { immediate: true }
);
</script>

<style scoped>
#product-form-card {
  box-shadow: none;
  width: 100%;
  --el-card-padding: 30px 10px 10px 5px !important;
}

.custom-toast-icon {
  color: #4caf50;
  font-size: 20px;
}

.custom-toast-icon-error {
  color: #f44336;
  font-size: 20px;
}

.el-form-item {
  line-height: 0.2;
}
</style>