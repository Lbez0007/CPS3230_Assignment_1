package com.youstockit;

import com.youstockit.services.EmailService;
import com.youstockit.services.OrderService;
import com.youstockit.users.Manager;
import java.util.LinkedList;
import java.util.List;

public class ProductCatalogue {

    protected List<StockItem> items;
    protected EmailService emailService;
    protected OrderService orderService;
    protected SupplierServer supplierServer;
    public Manager manager;

    public ProductCatalogue (){
        items = new LinkedList<StockItem>();
    }

    public StockItem searchCatalogue(int itemId){
        for (int i = 0; i < items.size(); i++){
            if (items.get(i).id == itemId){
                return items.get(i);
            }
        }
        return null;
    }

    public List<StockItem> searchCatalogueCategory(String category){
        List<StockItem> itemsRet = new LinkedList<StockItem>();

        for (int i = 0; i < items.size(); i++){
            if (items.get(i).category == category){
                itemsRet.add(items.get(i));
            }
        }
        return itemsRet;
    }

    public void addItem(StockItem item) {
        items.add(item);
    }

    public boolean removeItem(int itemId) {
        StockItem item = searchCatalogue(itemId);
        if (item != null) {
            items.remove(item);
            return true;
        }
        return false;
    }

    public void sellItem(int itemId, int qty) {
        StockItem item = searchCatalogue(itemId);

        if (item != null && (item.quantity >= qty)) {
            item.quantity = item.quantity - qty;
            //checkOrderQuantity(item);
            item.numTimesSold++;
            removeNilQuantityItems();
        }
    }

    public ItemOrder checkOrderQuantity(StockItem item){
        if (item.quantity <= item.minimumOrderQty){
            ItemOrder itemOrder = new ItemOrder(item.id, item.orderAmount);
            return itemOrder;
        } return null;
    }

    public void increaseItemQuantity(int itemId, int qty){
        StockItem item = searchCatalogue(itemId);
        if (item != null) {
            item.quantity = item.quantity + qty;
        }
    }

    public void setMinimumOrderQuantityZero(int itemId){
        StockItem item = searchCatalogue(itemId);
        if (item != null) {
            item.minimumOrderQty = 0;
        }
    }

    public void removeNilQuantityItems(){
        for (int i=0; i < items.size(); i++){
            StockItem item = items.get(i);
            if (item.quantity == 0 && item.minimumOrderQty == 0){
                removeItem(item.id);
            }
        }
    }

    public void setEmailService(EmailService emailService){
        this.emailService = emailService;
    }

    public void setOrderService(OrderService orderService){
        this.orderService = orderService;
    }

    public void setSupplierServer(SupplierServer supplierServer){
        this.supplierServer = supplierServer;
    }

    public SupplierResponse[] automatedStockOrdering(ItemOrder[] items){
        SupplierResponse[] responses = supplierServer.orderItems(items);

        for (int i = 0; i < items.length; i++) {
            ItemOrder item = items[i];
            SupplierErrorCode errorCode = responses[i].supplierErrorCode;
            int qtySupplied = responses[i].qtyItemsProvided;

            if (errorCode.equals(SupplierErrorCode.SUCCESS)) {
                increaseItemQuantity(item.itemId, qtySupplied);
            }
            if (errorCode.equals(SupplierErrorCode.OUT_OF_STOCK)) {
                emailService.sendEmail(manager);
                increaseItemQuantity(item.itemId, qtySupplied);
            }
            if (errorCode.equals(SupplierErrorCode.ITEM_NOT_FOUND)) {
                setMinimumOrderQuantityZero(item.itemId);
            }
            if (errorCode.equals(SupplierErrorCode.COMMUNICATION_ERROR)) {
                for (int j = 0; j < 3; j++) {
                    try {
                        Thread.sleep(5000);
                        orderService.reOrder();
                        if (!errorCode.equals(SupplierErrorCode.COMMUNICATION_ERROR)) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                emailService.sendEmail(manager);
            }
        }

        return responses;
    }
}
