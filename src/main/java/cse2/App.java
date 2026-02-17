package cse2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.io.File;

public class App {

    public static void main(String[] args) {

        // ==========================================
        // 🛠️ AUTO-FIXER: RENAME THE BROKEN FILE
        // ==========================================
        File folder = new File("src/main/resources");
        File[] files = folder.listFiles();
        
        if (files != null) {
            for (File f : files) {
                // Check if the file name is "hibernate.cfg.xml" but has hidden characters (like \n)
                String name = f.getName();
                if (name.trim().equals("hibernate.cfg.xml") && !name.equals("hibernate.cfg.xml")) {
                    System.out.println("⚠️ FOUND BROKEN FILENAME: [" + name + "]");
                    
                    File fixedFile = new File(folder, "hibernate.cfg.xml");
                    if (f.renameTo(fixedFile)) {
                        System.out.println("✅ SUCCESS: Renamed file to 'hibernate.cfg.xml'");
                    } else {
                        System.err.println("❌ FAILED to rename. You must rename it manually in Finder.");
                    }
                }
            }
        }
        // ==========================================

        // 1. Now load the (hopefully fixed) file
        File configFile = new File("src/main/resources/hibernate.cfg.xml");

        // 2. Debug Check
        if (configFile.exists()) {
            System.out.println("✅ FOUND CONFIG: " + configFile.getAbsolutePath());
        } else {
            System.err.println("❌ STILL MISSING: " + configFile.getAbsolutePath());
            System.err.println("👉 STOP. Go to 'src/main/resources' and rename the file manually.");
            return;
        }

        // 3. Start Hibernate
        SessionFactory factory = new Configuration()
                .configure(configFile)
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();

        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.println("Creating products...");
        
        Product p1 = new Product();
        p1.setName("Pen");
        p1.setDescription("Blue");
        p1.setPrice(10);
        p1.setQuantity(2);

        Product p2 = new Product();
        p2.setName("Book");
        p2.setDescription("Math");
        p2.setPrice(50);
        p2.setQuantity(1);

        session.persist(p1);
        session.persist(p2);

        tx.commit();

        System.out.println("🎉 Data inserted successfully!");

        session.close();
        factory.close();
    }
}