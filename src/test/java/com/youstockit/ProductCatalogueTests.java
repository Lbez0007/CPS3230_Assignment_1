package com.youstockit;

import com.youstockit.factories.CatalogueProvisioning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ProductCatalogueTests {

    CatalogueProvisioning provisioning;
    ProductCatalogue productCatalogue;
    StockItem stockItem;

    @BeforeEach
    public void setup(){
        productCatalogue = new ProductCatalogue();
        // Instantiating object of Product Catalogue Factory
        provisioning = new CatalogueProvisioning();

        stockItem = new StockItem(2, "Test Product 2", "Validating", "Validation Item", 5,
                10, 20, 1.50);
    }

    @Test
    public void testEmptyProductCatalogue() {
        //Setup
        ProductCatalogue catalogue = provisioning.provideEmptyCatalogue();

        //Exercise
        List<StockItem> items = catalogue.items;

        //Verify
        Assertions.assertEquals(0, items.size());
    }

    @Test
    public void testProductCatalogueWithOneItem() {
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideStockedCatalogue();

        //Exercise
        List<StockItem> items = productCatalogue.items;

        //Verify
        Assertions.assertEquals(1, productCatalogue.items.size());
    }

    @Test
    public void testAddItemToEmptyProductCatalogue() {
        //Setup
        ProductCatalogue catalogue = provisioning.provideEmptyCatalogue();

        //Exercise
        catalogue.addItem(stockItem);
        List<StockItem> items = catalogue.items;

        //Verify
        Assertions.assertEquals(1, items.size());
    }

    @Test
    public void testRemoveItemFromStockedProductCatalogue() {
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);

        //Exercise
        productCatalogue.removeItem(item.id);

        //Verify
        Assertions.assertEquals(0, productCatalogue.items.size());
    }

    @Test
    public void testRemoveItemFromMultiStockedProductCatalogue() {
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);
        int catSize = productCatalogue.items.size();

        //Exercise
        productCatalogue.removeItem(stockItem.id);

        //Verify
        Assertions.assertEquals(catSize - 1, productCatalogue.items.size());
    }

    @Test
    public void testRemoveItemFromEmptyProductCatalogue() {
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideEmptyCatalogue();
        int catSize = productCatalogue.items.size();

        //Exercise
        productCatalogue.removeItem(stockItem.id);

        //Verify
        Assertions.assertEquals(catSize, productCatalogue.items.size());
    }

    @Test
    public void testSearchProductCatalogue(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);

        //Exercise
        StockItem stockItem = productCatalogue.searchCatalogue(item.id);

        //Verify
        Assertions.assertNotNull(stockItem);
    }

    @Test
    public void testSearchProductCatalogueWrongId(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);

        //Exercise
        StockItem stockItem = productCatalogue.searchCatalogue(item.id + 1);

        //Verify
        Assertions.assertNull(stockItem);
    }

    @Test
    public void testSearchProductCatalogueWithNoItem(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideEmptyCatalogue();

        //Exercise
        StockItem item = productCatalogue.searchCatalogue(1);

        //Verify
        Assertions.assertNull(item);
    }

    @Test
    public void testSearchProductCatalogueCategory(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);

        //Exercise
        List<StockItem> stockItem = productCatalogue.searchCatalogueCategory(item.category);

        //Verify
        Assertions.assertNotEquals(0, stockItem.size());
    }

    @Test
    public void testSearchProductCatalogueWrongCategory(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);

        //Exercise
        List<StockItem> stockItem = productCatalogue.searchCatalogueCategory("Abc");

        //Verify
        Assertions.assertEquals(0, stockItem.size());
    }

    @Test
    public void testSellItem(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);
        int itemQuantity = item.quantity;
        int itemNumTimesSold = item.numTimesSold;

        //Exercise
        productCatalogue.sellItem(item.id, 5);

        //Verify
        Assertions.assertEquals(itemQuantity - 5, productCatalogue.searchCatalogue(item.id).quantity );
        Assertions.assertEquals(productCatalogue.searchCatalogue(item.id).numTimesSold, itemNumTimesSold + 1);
    }

    @Test
    public void testSellItemWrongId(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);
        int itemQuantity = item.quantity;

        //Exercise
        productCatalogue.sellItem(0, 5);

        //Verify
        Assertions.assertEquals(itemQuantity, productCatalogue.searchCatalogue(item.id).quantity);
    }

    @Test
    public void testSellItemIncorrectQuantity(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);
        int itemQuantity = item.quantity;

        //Exercise
        productCatalogue.sellItem(item.id, item.quantity + 1);

        //Verify
        Assertions.assertEquals(itemQuantity, productCatalogue.searchCatalogue(item.id).quantity);
    }

    @Test
    public void testReorderItemExceedingMinQtyLimit(){

        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);
        int itemQuantity = item.quantity;
        int quantityOrdered = item.quantity - item.minimumOrderQty;

        //Exercise
        productCatalogue.sellItem(item.id, quantityOrdered);
        ItemOrder itemOrder = productCatalogue.checkOrderQuantity(item);

        //Verify
        Assertions.assertNotNull(itemOrder);
    }

    @Test
    public void testReorderItemNotExceedingMinQtyLimit(){
        //Setup
        ProductCatalogue productCatalogue = provisioning.provideMultiStockedCatalogue();
        StockItem item = productCatalogue.items.get(0);
        int itemQuantity = item.quantity;
        int quantityOrdered = item.quantity - item.minimumOrderQty;

        //Exercise
        productCatalogue.sellItem(item.id, quantityOrdered - 1);
        ItemOrder itemOrder = productCatalogue.checkOrderQuantity(item);

        //Verify
        Assertions.assertNull(itemOrder);
    }
}
