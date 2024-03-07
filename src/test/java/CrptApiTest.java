import myProj.CrptApi;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class CrptApiTest {

    @Test
    void testCreateDocument() throws InterruptedException {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 5);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                crptApi.createDocument("Document " + i, "Signature " + i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                crptApi.createDocument("Document " + (i + 5), "Signature " + (i + 5));
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(10, crptApi.getTotalDocumentsCreated());
    }

    @Test
    void testCreateDocumentWithInvalidParameters() {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 5);

        assertThrows(IllegalArgumentException.class, () -> crptApi.createDocument(null, "Signature"));
        assertThrows(IllegalArgumentException.class, () -> crptApi.createDocument("Document", null));
    }

    @Test
    void testExceedRequestLimit() throws InterruptedException {
        int requestLimit = 2;
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, requestLimit);

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                crptApi.createDocument("Document " + i, "Signature " + i);
            }
        });

        thread.start();
        thread.join();

        assertEquals(5, crptApi.getTotalDocumentsCreated());
    }

}
