import java.util.Scanner;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.io.*;

public class Project {

    static final String FILE_NAME = "vault.txt";
    static final String SECRET_KEY = "1234567890123456";

   
    public static void saveToFile(Account acc) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(acc.Website + ":" + acc.Username + ":" + acc.Password + ":" + acc.ID);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void saveAllToFile(ArrayList<Account> accounts) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

            for (Account acc : accounts) {
                writer.write(acc.Website + ":" + acc.Username + ":" + acc.Password + ":" + acc.ID);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static ArrayList<Account> loadFromFile() {
        ArrayList<Account> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(":");

                if (parts.length == 4) {
                    Account acc = new Account(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3]
                    );
                    list.add(acc);
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("No previous data found.");
        }

        return list;
    }

    
    public static String encrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   
    public static String decrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        ArrayList<Account> accounts = loadFromFile();

        while (true) {
            System.out.println("\n1 Add  2 View  3 Search  4 Delete  5 Exit");
            int choice = s.nextInt();
            s.nextLine();

            if (choice == 1) {
                Account a = new Account();
                a.show(s);
                accounts.add(a);
                saveToFile(a);
                System.out.println("Saved");

            } else if (choice == 2) {
                for (Account acc : accounts) {
                    System.out.println(acc.Website + " | " + acc.Username + " | "
                            + decrypt(acc.Password) + " | " + acc.ID);
                }

            } else if (choice == 3) {
                System.out.print("Search: ");
                String q = s.nextLine().toLowerCase();

                for (Account acc : accounts) {
                    if (acc.Website.toLowerCase().contains(q) ||
                        acc.Username.toLowerCase().contains(q)) {
                        System.out.println(acc.Website + " | " + acc.Username + " | "
                                + decrypt(acc.Password) + " | " + acc.ID);
                    }
                }

            } else if (choice == 4) {
                System.out.print("Enter ID: ");
                String id = s.nextLine();

                boolean removed = accounts.removeIf(a -> a.ID.equals(id));

                if (removed) {
                    saveAllToFile(accounts); 
                    System.out.println("Deleted");
                } else {
                    System.out.println("Not found");
                }

            } else if (choice == 5) {
                break;
            }
        }

        s.close();
    }
}