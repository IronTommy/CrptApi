package myProj;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 2);

        for (int i = 0; i < 5; i++) {
            String document = "Document " + i;
            String signature = "Signature " + i;
            crptApi.createDocument(document, signature);
        }

        System.out.println("Total documents created: " + crptApi.getTotalDocumentsCreated());
        System.out.println("Total signatures received: " + crptApi.getTotalSignaturesReceived());
    }
}
