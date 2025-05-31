<template>
  <div class="product-history">
<!--    <div class="title-container">-->
<!--      <h2>Histórico de Edição do Produto</h2>-->
<!--    </div>-->
    <div class="content">
      <table v-if="history.length" class="modern-table">
        <thead>
        <tr>
          <th>Data da Alteração</th>
          <th>Campo Alterado</th>
          <th>Valor Anterior</th>
          <th>Valor Atual</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in history" :key="item.id">
          <td>{{ formatDate(item.changeDate) }}</td>
          <td>Descrição</td>
          <td>{{ item.previousState.description }}</td>
          <td>{{ item.updatedState.description }}</td>
        </tr>
        <tr v-for="item in history" :key="item.id">
          <td>{{ formatDate(item.changeDate) }}</td>
          <td>Tipo</td>
          <td>{{ translateType(item.previousState.type) }}</td>
          <td>{{ translateType(item.updatedState.type) }}</td>
        </tr>
        <tr v-for="item in history" :key="item.id">
          <td>{{ formatDate(item.changeDate) }}</td>
          <td>Valor do Fornecedor</td>
          <td>{{ item.previousState.supplierValueFormatted }}</td>
          <td>{{ item.updatedState.supplierValueFormatted }}</td>
        </tr>
        <tr v-for="item in history" :key="item.id">
          <td>{{ formatDate(item.changeDate) }}</td>
          <td>Quantidade em Estoque</td>
          <td>{{ item.previousState.stockQuantity }}</td>
          <td>{{ item.updatedState.stockQuantity }}</td>
        </tr>
        </tbody>
      </table>
      <h3 v-if="history.some(item => item.updatedState.stockMovements.length)">Movimentações de Estoque</h3>
      <table v-if="history.some(item => item.updatedState.stockMovements.length)" class="modern-table">
        <thead>
        <tr>
          <th>Tipo de Movimentação</th>
          <th>Preço de Venda</th>
          <th>Data de Venda</th>
          <th>Quantidade</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in history" :key="item.id">
          <td colspan="4">
            <table class="nested-table">
              <tbody>
              <tr v-for="movement in item.updatedState.stockMovements" :key="movement.id">
                <td>{{ movement.movementType === 'EXIT' ? 'SAÍDA' : 'ENTRADA' }}</td>
                <td>{{ movement.salePriceFormatted }}</td>
                <td>{{ new Date(movement.saleDateFormatted).toLocaleDateString() }}</td>
                <td>{{ movement.quantityMovement }}</td>
              </tr>
              </tbody>
            </table>
          </td>
        </tr>
        </tbody>
      </table>
      <p v-else>Nenhum histórico encontrado para este produto.</p>
    </div>
  </div>
</template>

<script lang="ts">
import {onMounted, ref} from "vue";
import {buscarHistoricoEdicao} from "../services/productService";

export default {
  props: {
    productId: {
      type: Number,
      required: true,
    },
  },
  setup(props) {
    const history = ref([]);

    onMounted(async () => {
      try {
        const response = await buscarHistoricoEdicao(props.productId);
        history.value = response.data;
      } catch (error) {
        console.error("Erro ao buscar histórico de edição:", error);
      }
    });

    const formatDate = (date: string) => {
      return new Date(date).toLocaleString();
    };

    return {history, formatDate};
  },

  methods: {
    translateType(type: string): string {
      const translations: Record<string, string> = {
        ELECTRONICS: "ELETRÔNICOS",
        HOME_APPLIANCE: "ELETRODOMÉSTICOS",
        FURNITURE: "MÓVEIS",
      };
      return translations[type] || "DESCONHECIDO";
    },
  },
};
</script>

<style scoped>
h2, h3 {
  margin-bottom: 15px;
  font-size: 18px;
  color: #333;
}

.modern-table, .nested-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed; /* Ensures consistent column widths */
}

.modern-table th, .modern-table td, .nested-table th, .nested-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
  color: #2f3133;
}

.modern-table th, .nested-table th {
  background-color: #767474;
  font-weight: bold;
}

.modern-table tr:nth-child(even), .nested-table tr:nth-child(even) {
  background-color: #fdf9f9;
}

.modern-table tr:hover, .nested-table tr:hover {
  background-color: #eeeded;
}

.product-history {
  padding: 0;
}

.title-container h2 {
  color: #ffffff;
  margin: 0;
  font-size: 20px;
}

.content {
  padding-top: 15px; /* Add padding to avoid overlap with the title */
}
</style>