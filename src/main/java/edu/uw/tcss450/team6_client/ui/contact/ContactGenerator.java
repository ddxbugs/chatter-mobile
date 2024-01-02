package edu.uw.tcss450.team6_client.ui.contact;


import java.util.Arrays;
import java.util.List;

public final class ContactGenerator {

    private static ContactCard[] CONTACTS = new ContactCard[0];
    private static final int COUNT = 20;

    private ContactGenerator() {

    }

    static {
        CONTACTS = new ContactCard[COUNT];
        for (int i = 0; i < CONTACTS.length; i++) {
            CONTACTS[i] = new ContactCard("Jane Doe");
        }
    }

    public static List<ContactCard> getContactList() {
        return Arrays.asList (CONTACTS);
    }
}
