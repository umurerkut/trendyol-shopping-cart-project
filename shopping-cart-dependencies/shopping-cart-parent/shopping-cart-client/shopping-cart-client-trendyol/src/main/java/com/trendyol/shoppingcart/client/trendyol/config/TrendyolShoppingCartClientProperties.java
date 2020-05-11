package com.trendyol.shoppingcart.client.trendyol.config;

public class TrendyolShoppingCartClientProperties {

    private DeliverCost deliverCost = new DeliverCost();

    public DeliverCost getDeliverCost() {
        return this.deliverCost;
    }

    public void setDeliverCost(DeliverCost deliverCost) {
        this.deliverCost = deliverCost;
    }

    public static class DeliverCost {
        private Double costPerDelivery = 0D;
        private Double costPerProduct = 0D;
        private Double fixedCost = 2.99D;

        public Double getCostPerDelivery() {
            return costPerDelivery;
        }

        public void setCostPerDelivery(Double costPerDelivery) {
            this.costPerDelivery = costPerDelivery;
        }

        public Double getCostPerProduct() {
            return costPerProduct;
        }

        public void setCostPerProduct(Double costPerProduct) {
            this.costPerProduct = costPerProduct;
        }

        public Double getFixedCost() {
            return fixedCost;
        }

        public void setFixedCost(Double fixedCost) {
            this.fixedCost = fixedCost;
        }
    }

}
