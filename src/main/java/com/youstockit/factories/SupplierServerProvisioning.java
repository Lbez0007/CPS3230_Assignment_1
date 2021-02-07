package com.youstockit.factories;

import com.youstockit.ProductCatalogue;
import com.youstockit.StockItem;
import com.youstockit.SupplierServer;
import com.youstockit.users.Supplier;

public class SupplierServerProvisioning {

    SupplierServer supplierServer;
    Supplier supplier;

    public SupplierServer provideSupplierServer(){
        supplierServer =  new SupplierServer();
        supplier = new Supplier("Mr Test", "test@test.com");
        supplierServer.supplier = supplier;
        return supplierServer;
    }


}
