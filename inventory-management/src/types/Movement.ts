import type {Product} from "./Product.ts";

export interface Movement {
    id: number;
    product: Product;
    movementType: 'ENTRY' | 'EXIT';
    salePrice: number;
    salePriceFormatted: string;
    saleDate: string;
    saleDateFormatted: string;
    quantityMovement: number;
}