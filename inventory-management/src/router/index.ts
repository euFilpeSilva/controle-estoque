import {createRouter, createWebHistory} from 'vue-router';
import ProductList from "../pages/product/ProductList.vue";
import MovementList from "../pages/movement/MovementList.vue";
import MovementForm from "../pages/movement/MovementForm.vue";
import ProfitReport from "../pages/relatorio/ProfitReport.vue";
import ProductForm from "../pages/product/ProductForm.vue";

const routes = [
    {path: '/', redirect: '/produtos'},
    {path: '/produtos', component: ProductList},
    {path: '/movimentacoes', component: MovementList},
    {path: '/relatorio/lucro', component: ProfitReport},
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;