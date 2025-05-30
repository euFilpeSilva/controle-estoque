import axios from "axios";
import type {Movement} from "../types/Movement.ts";

const API_BASE_URL = "http://localhost:8080/api";

export async function listarMovimentos(page: number, size: number) {
    return axios.get(`${API_BASE_URL}/movements`, {
        params: {page, size},
    });
}

export async function salvarMovimento(movimento: Movement) {
    return axios.post(`${API_BASE_URL}/movements`, movimento);
}