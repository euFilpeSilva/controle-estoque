<template>
  <div class="modal-container" v-if="isVisible">
    <div class="modal-content">
      <div class="modal-title">
        <h2>Histórico de Edição do Produto</h2>
      </div>
      <div class="modal-body">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  props: {
    isVisible: {
      type: Boolean,
      required: true,
    },
  },
  emits: ["close"],
  methods: {
    close() {
      this.$emit("close");
    },
  },
};
</script>

<style scoped>
.modal-container {
  position: relative;
  width: 100%;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 100%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.modal-title {
  background-color: #4b4747;
  color: white;
  padding: 5px 20px;
  font-size: 14px;
  font-weight: bold;
  position: sticky;
  top: 0;
  z-index: 10;
}

.modal-body {
  overflow-y: auto;
  max-height: 400px; /* Adjust scroll height */
  padding: 15px; /* Add padding for content */
}
</style>