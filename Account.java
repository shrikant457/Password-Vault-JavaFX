import java.util.Scanner;

class Account {
    String Website;
    String Username;
    String Password;
    String ID;
    
    public Account() {}

    
    public Account(String website, String username, String password, String id) {
        this.Website = website;
        this.Username = username;
        this.Password = password;
        this.ID = id;
    }

    void show(Scanner s) {
        System.out.print("Enter Website: ");
        Website = s.nextLine();

        System.out.print("Enter Username: ");
        Username = s.nextLine();

        System.out.print("Enter Password: ");
        String pass = s.nextLine();
        Password = Project.encrypt(pass);

        System.out.print("Enter ID: ");
        ID = s.nextLine();
    }
}