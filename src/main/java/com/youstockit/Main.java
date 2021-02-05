package com.youstockit;

import com.youstockit.factories.CatalogueProvisioning;
import com.youstockit.services.EmailService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Main main = new Main();

        System.out.println("YouStockIt");
        System.out.println("----------");
        System.out.println("1. View Catalogue");
        System.out.println("2. View items in a category");
        System.out.println("3. Add item to Catalogue");
        System.out.println("4. Remove item from Catalogue");
        System.out.println("5. Place customer order");
        System.out.println("6. Calculate profit from sales");
        System.out.println("7. Exit");

        Scanner choice = new Scanner(System.in);
        System.out.println();
        System.out.print("Please select an option: ");
        int choiceEntry = choice.nextInt();

        main.menu(choiceEntry);
    }

    public void menu(int choice) {
        CatalogueProvisioning provisioning = new CatalogueProvisioning();
        ProductCatalogue catalogue = provisioning.provideMultiStockedCatalogue();

        switch (choice) {
            case 1:
                for (StockItem item : catalogue.items) {
                    System.out.print("Item ID: " + item.id);
                    System.out.print(" Name: " + item.name);
                    System.out.print(" Quantity: " + item.quantity);
                }
                break;

            case 2:
                Scanner choiceCase2 = new Scanner(System.in);
                System.out.println();
                System.out.print("Insert Category of items: ");
                String choiceEntry2 = choiceCase2.nextLine();

                List<StockItem> categoryItems = catalogue.searchCatalogueCategory(choiceEntry2);

                for (StockItem item : categoryItems) {
                    System.out.print("Item ID: " + item.id);
                    System.out.print(" Name: " + item.name);
                    System.out.print(" Quantity: " + item.quantity);
                }
                break;
            case 3:
                Scanner choiceCase3 = new Scanner(System.in);
                System.out.println();
                System.out.print("Insert Item ID: ");
                int id = choiceCase3.nextInt();

                System.out.print("Insert Item Name: ");
                String name = choiceCase3.nextLine();

                System.out.print("Insert Item Category: ");
                String category = choiceCase3.nextLine();

                System.out.print("Insert Item Description: ");
                String description = choiceCase3.nextLine();

                System.out.print("Insert Minimum Order Quantity for Item: ");
                int minQty = choiceCase3.nextInt();

                System.out.print("Insert Item Quantity: ");
                int qty = choiceCase3.nextInt();

                System.out.print("Insert Order Amount for Item: ");
                int orderAmt = choiceCase3.nextInt();

                System.out.print("Insert Selling Price for Item: ");
                double sellingPrice = choiceCase3.nextDouble();

                StockItem item = new StockItem(id, name, category, description, minQty, qty, orderAmt, sellingPrice);
                catalogue.addItem(item);

                System.out.println("Item added to catalogue!");
                break;
            case 4:
                Scanner choiceCase4 = new Scanner(System.in);
                System.out.println();
                System.out.print("Insert ID of item to remove: ");
                int choiceEntry4 = choiceCase4.nextInt();

                boolean removed =  catalogue.removeItem(choiceEntry4);
                if(removed) {
//                    System.out.println("Item Removed");
//                    EmailServiceSpy emailServiceSpy = new EmailServiceSpy();
//                    catalogue.setEmailService(EmailServiceSpy);
                }
                else
                    System.out.println("Item not found in catalogue!");
                break;
            case 5:
                //do logic
                break;
            case 6:
                //do logic
                break;
            case 7:
                System.exit(0);
        }
    }
}
