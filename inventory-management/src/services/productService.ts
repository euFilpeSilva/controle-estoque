import axios from "axios";
import type {Product} from "../types/Product.ts";

const API_BASE_URL = "http://localhost:8080/api";

export async function listarProdutos(page: number, size: number) {
    return axios.get(`${API_BASE_URL}/product`, {
        params: {page, size},
    });
}

export async function listarProdutosSemPaginacao() {
    return axios.get(`${API_BASE_URL}/product/all`);
}

export async function salvarProduto(produto: Product) {
    return axios.post(`${API_BASE_URL}/product`, produto);
}

export async function editarProduto(produto: Product) {
    return axios.put(`${API_BASE_URL}/product/${produto.id}`, produto);
}


export async function listarLucroPaginado(page: number, size: number, startDate?: string, endDate?: string) {
    return axios.get(`${API_BASE_URL}/product/lucro`, {
        params: {page, size, startDate, endDate},
    });
}

export async function deletarProduto(id: number) {
    return axios.delete(`${API_BASE_URL}/product/${id}`);
}

export async function buscarHistoricoEdicao(id: number) {
    return axios.get(`${API_BASE_URL}/product/${id}/history`);
}