export interface Product {
    id: number;
    code: string;
    description: string;
    type: 'ELECTRONICS' | 'HOME_APPLIANCE' | 'FURNITURE';
    supplierValue: number;
    supplierValueFormatted: string;
    stockQuantity: number;
    stockMovements: StockMovement[];
}