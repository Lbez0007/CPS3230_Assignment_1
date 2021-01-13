package com.youstockit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ProductCatalogueTests {

    ProductCatalogue productCatalogue;
    StockItem stockItem;

    @BeforeEach
    public void setup(){
        productCatalogue = new ProductCatalogue();
        stockItem = new StockItem(1,"Test Product", "Testing", "Test Item", 10, 1000, 250, "Test Co Ltd", 1.50, 0);
    }

    @Test
    public void testEmptyProductCatalogue() {
        //Verify
        Assertions.assertEquals(0, productCatalogue.items.size());
    }

    @Test
    public void testProductCatalogueWithOneItem() {
        //Exercise
        productCatalogue.addItem(stockItem);

        //Verify
        Assertions.assertEquals(1, productCatalogue.items.size());
    }

    @Test
    public void testRemoveItemFromProductCatalogue() {
        //Setup
        productCatalogue.addItem(stockItem);

        //Exercise
        productCatalogue.removeItem(stockItem.id);

        //Verify
        Assertions.assertEquals(0, productCatalogue.items.size());
    }

    @Test
    public void testSearchProductCatalogue(){
        //Setup
        productCatalogue.addItem(stockItem);

        //Exercise
        StockItem item = productCatalogue.searchCatalogue(stockItem.id);

        //Verify
        Assertions.assertNotNull(item);
    }

    @Test
    public void testSearchProductCatalogueWrongId(){
        //Setup
        productCatalogue.addItem(stockItem);

        //Exercise
        StockItem item = productCatalogue.searchCatalogue(stockItem.id + 1);

        //Verify
        Assertions.assertNull(item);
    }

    @Test
    public void testSearchProductCatalogueWithNoItem(){
        //Exercise
        StockItem item = productCatalogue.searchCatalogue(stockItem.id);

        //Verify
        Assertions.assertNull(item);
    }

    @Test
    public void testSellItem(){
        //Setup
        productCatalogue.addItem(stockItem);
        int itemQuantity = productCatalogue.searchCatalogue(stockItem.id).quantity;
        int itemNumTimesSold = productCatalogue.searchCatalogue(stockItem.id).numTimesSold;

        //Exercise
        productCatalogue.sellItem(stockItem.id, 5);

        //Verify
        Assertions.assertEquals(itemQuantity - 5, productCatalogue.searchCatalogue(stockItem.id).quantity );
        Assertions.assertEquals(productCatalogue.searchCatalogue(stockItem.id).numTimesSold, itemNumTimesSold + 1);
    }

    @Test
    public void testSearchProductCatalogueCategory(){
        //Setup
        productCatalogue.addItem(stockItem);

        //Exercise
        List<StockItem> item = productCatalogue.searchCatalogueCategory(stockItem.category);

        //Verify
        Assertions.assertEquals(1, item.size());
    }

    @Test
    public void testSearchProductCatalogueWrongCategory(){
        //Setup
        productCatalogue.addItem(stockItem);

        //Exercise
        List<StockItem> item = productCatalogue.searchCatalogueCategory("Abc");

        //Verify
        Assertions.assertNotEquals(1, item.size());
    }

    @Test
    public void testSellItemWrongId(){
        //Setup
        productCatalogue.addItem(stockItem);
        int itemQuantity = productCatalogue.searchCatalogue(stockItem.id).quantity;

        //Exercise
        productCatalogue.sellItem(0, 5);

        //Verify
        Assertions.assertEquals(itemQuantity, productCatalogue.searchCatalogue(stockItem.id).quantity);
    }

    @Test
    public void testSellItemIncorrectQuantity(){
        //Setup
        productCatalogue.addItem(stockItem);
        int itemQuantity = productCatalogue.searchCatalogue(stockItem.id).quantity;

        //Exercise
        productCatalogue.sellItem(stockItem.id, stockItem.quantity + 1);

        //Verify
        Assertions.assertEquals(itemQuantity, productCatalogue.searchCatalogue(stockItem.id).quantity);
    }

    @Test
    public void testReorderItemExceedingMinQtyLimit(){

        //Setup
        productCatalogue.addItem(stockItem);
        int itemQuantity = productCatalogue.searchCatalogue(stockItem.id).quantity;

        //Exercise
        productCatalogue.sellItem(stockItem.id, 990);
        ItemOrder itemOrder = productCatalogue.checkOrderQuantity(stockItem);

        //Verify
        Assertions.assertNotNull(itemOrder);
    }

    @Test
    public void testReorderItemNotExceedingMinQtyLimit(){

        //Setup
        productCatalogue.addItem(stockItem);
        int itemQuantity = productCatalogue.searchCatalogue(stockItem.id).quantity;

        //Exercise
        productCatalogue.sellItem(stockItem.id, 989);
        ItemOrder itemOrder = productCatalogue.checkOrderQuantity(stockItem);

        //Verify
        Assertions.assertNull(itemOrder);
    }
}
