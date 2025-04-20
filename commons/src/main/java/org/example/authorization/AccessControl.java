package org.example.authorization;

public class AccessControl {

    public enum Role {
        ADMIN,
        USER
    }

    public enum Group {
        ADMIN("admin"),
        API("api");

        Group(String admin) {

        }
    }
}
