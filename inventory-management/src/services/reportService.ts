import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

// Serviço para obter relatório de lucro
export async function obterRelatorioLucro() {
    return axios.get(`${API_BASE_URL}/relatorio/lucro`);
}