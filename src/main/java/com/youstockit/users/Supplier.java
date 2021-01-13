package com.youstockit.users;

import com.youstockit.SupplierServer;

import java.util.UUID;

public class Supplier extends User {

    protected String id;
    protected String name;
    protected SupplierServer supplierServer;
    protected String email;

    public Supplier(String name, SupplierServer supplierServer, String email) {
        super(name, email);
        id = UUID.randomUUID().toString();
        this.name = name;
        this.supplierServer = supplierServer;
        this.email = email;
    }
}
