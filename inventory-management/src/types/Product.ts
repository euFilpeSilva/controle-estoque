export interface Product {
    id: number;
    description: string;
    type: 'ELECTRONICS' | 'HOME_APPLIANCE' | 'FURNITURE';
    supplierValue: number;
    supplierValueFormatted: string;
    stockQuantity: number;
    stockMovements: StockMovement[];
}