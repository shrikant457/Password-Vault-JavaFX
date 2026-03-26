import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Optional;

public class VaultApp extends Application {

    // ── Data ────────────────────────────────────────────────────────────────────
    private ArrayList<Account> accounts = new ArrayList<>();
    private ObservableList<Account> tableData = FXCollections.observableArrayList();

    // ── Color Palette ───────────────────────────────────────────────────────────
    private static final String BG_DARK    = "#0f1117";
    private static final String BG_CARD    = "#1a1d2e";
    private static final String BG_SIDEBAR = "#13151f";
    private static final String ACCENT     = "#6c63ff";
    private static final String ACCENT2    = "#a78bfa";
    private static final String SUCCESS    = "#22c55e";
    private static final String DANGER     = "#ef4444";
    private static final String TEXT_PRI   = "#f1f5f9";
    private static final String TEXT_SEC   = "#94a3b8";
    private static final String BORDER     = "#2d3148";

    // ── State ───────────────────────────────────────────────────────────────────
    private Button activeNavBtn = null;
    private StackPane contentArea = new StackPane();

    // ════════════════════════════════════════════════════════════════════════════
    @Override
    public void start(Stage stage) {
        accounts = Project.loadFromFile();
        tableData.setAll(accounts);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_DARK + ";");
        root.setLeft(buildSidebar());
        root.setCenter(buildMainContent());

        showDashboard();

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Password Vault");
        stage.setMinWidth(850);
        stage.setMinHeight(550);
        stage.show();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // SIDEBAR
    // ════════════════════════════════════════════════════════════════════════════
    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: " + BG_SIDEBAR + ";"
                + "-fx-border-color: " + BORDER + "; -fx-border-width: 0 1 0 0;");

        VBox logoBox = new VBox(4);
        logoBox.setPadding(new Insets(28, 20, 24, 20));
        Label icon  = new Label("PASSWORD VAULT");
        icon.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + ";");
        Label sub   = new Label("Secure  |  Encrypted  |  Private");
        sub.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SEC + ";");
        logoBox.getChildren().addAll(icon, sub);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + BORDER + ";");

        VBox nav = new VBox(4);
        nav.setPadding(new Insets(16, 12, 16, 12));
        Label menuLabel = new Label("NAVIGATION");
        menuLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: " + TEXT_SEC
                + "; -fx-padding: 0 0 8 8; -fx-font-weight: bold;");

        Button btnDash   = makeNavButton("Dashboard");
        Button btnAll    = makeNavButton("All Accounts");
        Button btnAdd    = makeNavButton("Add Account");
        Button btnSearch = makeNavButton("Search");

        activeNavBtn = btnDash;
        setActiveNav(btnDash);

        btnDash.setOnAction  (e -> { setActiveNav(btnDash);   showDashboard();   });
        btnAll.setOnAction   (e -> { setActiveNav(btnAll);    showAllAccounts(); });
        btnAdd.setOnAction   (e -> { setActiveNav(btnAdd);    showAddForm();     });
        btnSearch.setOnAction(e -> { setActiveNav(btnSearch); showSearch();      });

        nav.getChildren().addAll(menuLabel, btnDash, btnAll, btnAdd, btnSearch);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox bottom = new VBox(5);
        bottom.setPadding(new Insets(12, 16, 20, 16));
        bottom.setStyle("-fx-border-color: " + BORDER + "; -fx-border-width: 1 0 0 0;");
        Label stored = new Label(accounts.size() + " passwords stored");
        stored.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SEC + ";");
        Label enc = new Label("AES-128 Encrypted");
        enc.setStyle("-fx-font-size: 11px; -fx-text-fill: " + SUCCESS + "; -fx-font-weight: bold;");
        bottom.getChildren().addAll(stored, enc);

        sidebar.getChildren().addAll(logoBox, sep, nav, spacer, bottom);
        return sidebar;
    }

    private Button makeNavButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(11, 14, 11, 14));
        btn.setStyle(navStyle(false));
        btn.setOnMouseEntered(e -> { if (btn != activeNavBtn) btn.setStyle(navHoverStyle()); });
        btn.setOnMouseExited (e -> { if (btn != activeNavBtn) btn.setStyle(navStyle(false)); });
        return btn;
    }

    private void setActiveNav(Button btn) {
        if (activeNavBtn != null) activeNavBtn.setStyle(navStyle(false));
        activeNavBtn = btn;
        btn.setStyle(navStyle(true));
    }

    private String navStyle(boolean active) {
        return "-fx-background-color: " + (active ? ACCENT : "transparent") + ";"
             + "-fx-text-fill: "        + (active ? "#ffffff" : TEXT_SEC)   + ";"
             + "-fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;";
    }

    private String navHoverStyle() {
        return "-fx-background-color: #2d3148; -fx-text-fill: " + TEXT_PRI
             + "; -fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;";
    }

    // ════════════════════════════════════════════════════════════════════════════
    // MAIN CONTENT
    // ════════════════════════════════════════════════════════════════════════════
    private StackPane buildMainContent() {
        contentArea.setStyle("-fx-background-color: " + BG_DARK + ";");
        contentArea.setPadding(new Insets(30));
        return contentArea;
    }

    private void setContent(javafx.scene.Node node) {
        contentArea.getChildren().setAll(node);
        FadeTransition ft = new FadeTransition(Duration.millis(180), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // DASHBOARD
    // ════════════════════════════════════════════════════════════════════════════
    private void showDashboard() {
        VBox view = new VBox(24);
        view.setAlignment(Pos.TOP_LEFT);

        Label heading = pageHeading("Dashboard");
        Label sub     = subLabel("Welcome back. Your vault is secure and encrypted.");

        HBox cards = new HBox(16);
        cards.getChildren().addAll(
            statCard("Total Accounts", String.valueOf(accounts.size()), ACCENT),
            statCard("Encryption",     "AES-128",                       SUCCESS),
            statCard("Storage",        "vault.txt",                     "#f59e0b")
        );

        Label recentLabel = new Label("Recent Accounts");
        recentLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRI + ";");

        VBox recentList = new VBox(10);
        int count = Math.min(accounts.size(), 5);
        if (count == 0) {
            Label empty = new Label("No accounts yet. Click 'Add Account' to get started!");
            empty.setStyle("-fx-text-fill: " + TEXT_SEC + "; -fx-font-size: 13px;");
            recentList.getChildren().add(empty);
        } else {
            for (int i = accounts.size() - 1; i >= accounts.size() - count; i--) {
                recentList.getChildren().add(recentRow(accounts.get(i)));
            }
        }

        view.getChildren().addAll(heading, sub, cards, recentLabel, recentList);
        ScrollPane sp = scrollWrap(view);
        setContent(sp);
    }

    private VBox statCard(String label, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setPrefWidth(190);
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 12;"
                    + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SEC + ";");
        card.getChildren().addAll(val, lbl);
        return card;
    }

    private HBox recentRow(Account acc) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 8;"
                   + "-fx-border-color: " + BORDER + "; -fx-border-radius: 8; -fx-border-width: 1;");
        VBox info = new VBox(2);
        Label site = new Label(acc.Website);
        site.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRI + ";");
        Label user = new Label(acc.Username);
        user.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SEC + ";");
        info.getChildren().addAll(site, user);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label idLbl = new Label("ID: " + acc.ID);
        idLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SEC + ";");
        row.getChildren().addAll(info, spacer, idLbl);
        return row;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // ALL ACCOUNTS TABLE
    // ════════════════════════════════════════════════════════════════════════════
    private void showAllAccounts() {
        VBox view = new VBox(20);
        view.getChildren().add(pageHeading("All Accounts"));

        TableView<Account> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(470);
        table.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 10;"
                     + "-fx-border-color: " + BORDER + "; -fx-border-radius: 10;");
        table.setPlaceholder(new Label("No accounts found."));

        TableColumn<Account, String> colSite = new TableColumn<>("Website");
        colSite.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().Website));
        colSite.setPrefWidth(180);

        TableColumn<Account, String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().Username));
        colUser.setPrefWidth(160);

        TableColumn<Account, String> colPass = new TableColumn<>("Password  (click to reveal)");
        colPass.setPrefWidth(230);
        colPass.setCellValueFactory(d -> new SimpleStringProperty(""));
        colPass.setCellFactory(tc -> new TableCell<Account, String>() {
            boolean revealed = false;
            {
                setTooltip(new Tooltip("Click to reveal / hide"));
                setStyle("-fx-cursor: hand;");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }
                Account acc = (Account) getTableRow().getItem();
                setText(revealed ? Project.decrypt(acc.Password) : "••••••••");
                setStyle("-fx-text-fill: " + (revealed ? SUCCESS : TEXT_PRI) + "; -fx-cursor: hand;");
                setOnMouseClicked(e -> {
                    Account a = (Account) getTableRow().getItem();
                    revealed = !revealed;
                    setText(revealed ? Project.decrypt(a.Password) : "••••••••");
                    setStyle("-fx-text-fill: " + (revealed ? SUCCESS : TEXT_PRI) + "; -fx-cursor: hand;");
                });
            }
        });

        TableColumn<Account, String> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().ID));
        colID.setPrefWidth(100);

        TableColumn<Account, Void> colDel = new TableColumn<>("Actions");
        colDel.setPrefWidth(110);
        colDel.setCellFactory(tc -> new TableCell<Account, Void>() {
            final Button del = new Button("Delete");
            {
                del.setStyle("-fx-background-color: " + DANGER + "; -fx-text-fill: white;"
                           + "-fx-background-radius: 6; -fx-cursor: hand; -fx-font-size: 11px;");
                del.setOnAction(e -> deleteAccount(
                    getTableView().getItems().get(getIndex()), table));
            }
            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : del);
            }
        });

        table.getColumns().addAll(colSite, colUser, colPass, colID, colDel);
        tableData.setAll(accounts);
        table.setItems(tableData);

        Label hint = subLabel("Click any password cell to reveal or hide it.");
        view.getChildren().addAll(table, hint);
        setContent(view);
    }

    // ════════════════════════════════════════════════════════════════════════════
    // ADD ACCOUNT
    // ════════════════════════════════════════════════════════════════════════════
    private void showAddForm() {
        VBox outer = new VBox(24);
        outer.setMaxWidth(520);
        outer.getChildren().add(pageHeading("Add New Account"));

        VBox card = new VBox(18);
        card.setPadding(new Insets(28));
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 14;"
                    + "-fx-border-color: " + BORDER + "; -fx-border-radius: 14; -fx-border-width: 1;");

        TextField     tfWebsite  = styledField("e.g. github.com");
        TextField     tfUsername = styledField("e.g. john_doe");
        PasswordField tfPassword = new PasswordField();
        tfPassword.setPromptText("Enter your password");
        styleInput(tfPassword);
        TextField tfID = styledField("e.g. acc001 (must be unique)");

        Label errLabel = new Label("");
        errLabel.setStyle("-fx-text-fill: " + DANGER + "; -fx-font-size: 12px;");

        Button btnSave = accentButton("Save Account");
        btnSave.setOnAction(e -> {
            String web  = tfWebsite.getText().trim();
            String user = tfUsername.getText().trim();
            String pass = tfPassword.getText().trim();
            String id   = tfID.getText().trim();

            if (web.isEmpty() || user.isEmpty() || pass.isEmpty() || id.isEmpty()) {
                errLabel.setText("All fields are required.");
                return;
            }
            for (Account a : accounts) {
                if (a.ID.equals(id)) {
                    errLabel.setText("ID already exists. Choose a unique ID.");
                    return;
                }
            }
            Account acc = new Account(web, user, Project.encrypt(pass), id);
            accounts.add(acc);
            Project.saveToFile(acc);
            tableData.setAll(accounts);
            showInfo("Account for \"" + web + "\" saved successfully!");
            tfWebsite.clear(); tfUsername.clear(); tfPassword.clear(); tfID.clear();
            errLabel.setText("");
        });

        card.getChildren().addAll(
            formRow("Website",    tfWebsite),
            formRow("Username",   tfUsername),
            formRow("Password",   tfPassword),
            formRow("Account ID", tfID),
            errLabel,
            btnSave
        );
        outer.getChildren().add(card);
        setContent(outer);
    }

    // ════════════════════════════════════════════════════════════════════════════
    // SEARCH
    // ════════════════════════════════════════════════════════════════════════════
    private void showSearch() {
        VBox view = new VBox(20);
        view.getChildren().add(pageHeading("Search Accounts"));

        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        TextField tfQ = styledField("Search by website or username...");
        HBox.setHgrow(tfQ, Priority.ALWAYS);
        Button btnGo = accentButton("Search");
        bar.getChildren().addAll(tfQ, btnGo);

        Label resultLabel = new Label("");
        resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SEC + ";");

        VBox results = new VBox(10);

        Runnable doSearch = () -> {
            String q = tfQ.getText().trim().toLowerCase();
            results.getChildren().clear();
            if (q.isEmpty()) { resultLabel.setText(""); return; }
            int found = 0;
            for (Account acc : accounts) {
                if (acc.Website.toLowerCase().contains(q) ||
                    acc.Username.toLowerCase().contains(q)) {
                    results.getChildren().add(searchCard(acc));
                    found++;
                }
            }
            resultLabel.setText(found == 0 ? "No results found." : found + " result(s) found.");
        };

        btnGo.setOnAction(e -> doSearch.run());
        tfQ.setOnAction  (e -> doSearch.run());
        tfQ.textProperty().addListener((obs, o, n) -> doSearch.run());

        view.getChildren().addAll(bar, resultLabel, results);
        setContent(scrollWrap(view));
    }

    private VBox searchCard(Account acc) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 10;"
                    + "-fx-border-color: " + BORDER + "; -fx-border-radius: 10; -fx-border-width: 1;");

        Label site = new Label(acc.Website);
        site.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + ";");

        final boolean[] shown = {false};
        Label passVal = new Label("(hidden)");
        passVal.setStyle("-fx-text-fill: " + TEXT_SEC + "; -fx-font-size: 13px;");

        Button revBtn = new Button("Show Password");
        revBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ACCENT
                      + "; -fx-cursor: hand; -fx-font-size: 11px;"
                      + "-fx-border-color: " + ACCENT + "; -fx-border-radius: 4; -fx-padding: 3 10 3 10;");
        revBtn.setOnAction(e -> {
            shown[0] = !shown[0];
            passVal.setText(shown[0] ? Project.decrypt(acc.Password) : "(hidden)");
            passVal.setStyle("-fx-text-fill: " + (shown[0] ? SUCCESS : TEXT_SEC) + "; -fx-font-size: 13px;");
            revBtn.setText(shown[0] ? "Hide Password" : "Show Password");
        });

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(8);
        addGridRow(grid, 0, "Username:", acc.Username);
        addGridRow(grid, 2, "ID:",       acc.ID);

        Label kPass  = gridKey("Password:");
        HBox passRow = new HBox(10, passVal, revBtn);
        passRow.setAlignment(Pos.CENTER_LEFT);
        grid.add(kPass,   0, 1);
        grid.add(passRow, 1, 1);

        card.getChildren().addAll(site, grid);
        return card;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // DELETE
    // ════════════════════════════════════════════════════════════════════════════
    private void deleteAccount(Account acc, TableView<Account> table) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete account for \"" + acc.Website + "\"?");
        alert.setContentText("ID: " + acc.ID + "\nThis action cannot be undone.");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            accounts.removeIf(a -> a.ID.equals(acc.ID));
            Project.saveAllToFile(accounts);
            tableData.setAll(accounts);
            showInfo("Account deleted successfully.");
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ════════════════════════════════════════════════════════════════════════════
    private Label pageHeading(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRI + ";");
        return l;
    }

    private Label subLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SEC + ";");
        return l;
    }

    private VBox formRow(String label, javafx.scene.Node input) {
        VBox box = new VBox(6);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_SEC + ";");
        box.getChildren().addAll(lbl, input);
        return box;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        styleInput(tf);
        return tf;
    }

    private void styleInput(TextInputControl tf) {
        tf.setStyle("-fx-background-color: #0f1117; -fx-text-fill: " + TEXT_PRI
                  + "; -fx-prompt-text-fill: " + TEXT_SEC
                  + "; -fx-border-color: " + BORDER + "; -fx-border-radius: 8;"
                  + "-fx-background-radius: 8; -fx-padding: 10 14 10 14; -fx-font-size: 13px;");
        tf.setMaxWidth(Double.MAX_VALUE);
    }

    private Button accentButton(String text) {
        Button btn = new Button(text);
        String base = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white;"
                    + "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8;"
                    + "-fx-cursor: hand; -fx-padding: 11 22 11 22;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(base.replace(ACCENT, ACCENT2)));
        btn.setOnMouseExited (e -> btn.setStyle(base));
        return btn;
    }

    private Label gridKey(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: " + TEXT_SEC + "; -fx-font-size: 12px;");
        return l;
    }

    private void addGridRow(GridPane g, int row, String key, String val) {
        Label v = new Label(val);
        v.setStyle("-fx-text-fill: " + TEXT_PRI + "; -fx-font-size: 13px;");
        g.add(gridKey(key), 0, row);
        g.add(v,            1, row);
    }

    private ScrollPane scrollWrap(javafx.scene.Node node) {
        ScrollPane sp = new ScrollPane(node);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Password Vault");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
