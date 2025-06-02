<template>
  <el-card id="product-form-card">
    <el-form :model="form" :rules="rules" ref="movementForm" label-width="120px">
      <el-form-item label="Produto" prop="productId">
        <el-select v-model="form.productId" placeholder="Selecione">
          <el-option v-for="p in produtos" :key="p.id" :label="p.description" :value="p.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="Tipo" prop="movementType">
        <el-select v-model="form.movementType">
          <el-option label="Entrada" value="ENTRY"/>
          <el-option label="Saída" value="EXIT"/>
        </el-select>
      </el-form-item>
      <el-form-item label="Valor de Venda" prop="salePrice">
        <el-input
            :model-value="formattedSalePrice"
            @update:model-value="handleSalePriceInput"
            :disabled="form.movementType === 'ENTRY'"
            placeholder="Digite o valor"
        />
      </el-form-item>
      <el-form-item label="Quantidade" prop="quantityMovement">
        <el-input-number v-model="form.quantityMovement"/>
      </el-form-item>
      <el-form-item label="Data da Venda" prop="saleDate">
        <el-date-picker
            id="data-picker"
            v-model="form.saleDate"
            type="date"
            style="width: 150px"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :disabled="!isFormValid" @click="salvar">Salvar</el-button>
      </el-form-item>
    </el-form>

  </el-card>
    <el-divider id="estoque-disponivel" v-if="produtoSelecionado" >
      Estoque disponível: {{ produtoSelecionado.stockQuantity }}
    </el-divider>
</template>

<script lang="ts" setup>
import {onMounted, ref, watch} from 'vue';
import {listarProdutosSemPaginacao} from "@/services/productService";
import {Movement} from "@/types/Movement";
import {Product} from "@/types/Product";
import {ElMessage} from "element-plus";
import {salvarMovimento} from "@/services/movementService.ts";

const produtos = ref<Product[]>([]);
const form = ref<Partial<Movement>>({
  productId: null,
  movementType: 'ENTRY',
  quantityMovement: null,
  salePrice: null,
  saleDate: new Date().toISOString().split('T')[0],
});

const rules = {
  productId: [{required: true, message: 'Produto é obrigatório', trigger: 'change'}],
  movementType: [{required: true, message: 'Tipo é obrigatório', trigger: 'change'}],
  quantityMovement: [{required: true, message: 'Quantidade é obrigatória', trigger: 'blur'}],
  saleDate: [{required: true, message: 'Data da Venda é obrigatória', trigger: 'change'}],
};

const movementForm = ref();
const isFormValid = ref(false);

const props = defineProps<{ product: Partial<Movement> | null }>();
const emit = defineEmits(['movement-saved']);
const produtoSelecionado = ref<Product | null>(null);
const formattedSalePrice = ref('');

watch(form, async () => {
  if (movementForm.value) {
    isFormValid.value = await movementForm.value.validate().then(() => true).catch(() => false);
  }
}, {deep: true});

watch(() => form.value.movementType, (newType) => {
  if (newType === 'ENTRY') {
    rules.salePrice = [];
  } else {
    rules.salePrice = [{ required: true, message: 'Valor de Venda é obrigatório', trigger: 'blur' }];
  }
});

watch(() => form.value.productId, (novoId) => {
  produtoSelecionado.value = produtos.value.find(p => p.id === novoId) || null;
});

onMounted(async () => {
  const res = await listarProdutosSemPaginacao();
  produtos.value = res.data;
});

watch(() => form.value.salePrice, (newVal) => {
  if (typeof newVal === 'number') {
    formattedSalePrice.value = formatCurrency(newVal);
  }
});


async function salvar() {
  try {
    if (form.value.saleDate && !form.value.saleDate.includes('T')) {
      form.value.saleDate = new Date(`${form.value.saleDate}T03:00:00.000Z`).toISOString();
    }

    await salvarMovimento(form.value as Movement);
    ElMessage({
      message: 'Movimentação salva com sucesso!',
      type: 'success',
      iconClass: 'custom-toast-icon',
      duration: 3000,
      showClose: true,
    });
    emit('movement-saved');
    // Reset the form
    form.value = {
      productId: null,
      movementType: 'ENTRY',
      quantityMovement: null,
      salePrice: null,
      saleDate: new Date().toISOString().split('T')[0],
    };
  } catch (error: any) {
    const message = error?.response?.data?.message || 'Erro ao salvar a movimentação!';
    ElMessage({
      message,
      type: 'error',
      iconClass: 'custom-toast-icon-error',
      duration: 3000,
      showClose: true,
    });
  }

}


function formatCurrency(value: number): string {
  return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function handleSalePriceInput(value: string) {
  const onlyNumbers = value.replace(/\D/g, '');
  const numericValue = parseFloat((parseInt(onlyNumbers || '0', 10) / 100).toFixed(2));

  form.value.salePrice = numericValue;
  formattedSalePrice.value = formatCurrency(numericValue);
}
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

#estoque-disponivel {
margin-top: -2.5px;
  color: #abacae !important;
}

:deep(.el-divider__text) {
  color: #abacae !important;
  font-weight: 500;
}
</style>