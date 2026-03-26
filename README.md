# 🔐 Password Vault (JavaFX)

## 📌 About
This is a desktop password manager built using Java and JavaFX.  
It allows users to securely store and manage account credentials using AES encryption.

---

## 🚀 Features
- Modern UI built using JavaFX
- Add, view, search, and delete accounts
- AES encryption for password storage
- Password reveal / hide functionality
- File-based storage using `vault.txt`
- Interactive dashboard and navigation

---

## 🛠️ Technologies Used
- Java
- JavaFX
- AES Encryption (`javax.crypto`)
- File Handling

---

## 🔐 How It Works
- User enters account details (website, username, password, ID)
- Password is encrypted using AES
- Encrypted data is stored in a local file (`vault.txt`)
- Passwords are decrypted only when displayed in the UI

---

## ⚠️ Disclaimer
This is a **learning project**.  
Security can be improved by:
- Using secure key management instead of a hardcoded key
- Implementing stronger encryption modes (CBC/GCM)
- Adding user authentication (master password)

---

## 🧠 Note
The frontend (JavaFX UI) was built with the help of AI tools.  
The backend logic, encryption, and file handling were implemented as part of my learning.

---

## ▶️ How to Run
1. Clone the repository
2. Open in any Java IDE (IntelliJ / Eclipse)
3. Run `VaultApp.java`

---

## 🔮 Future Improvements
- Master password authentication
- Stronger encryption techniques
- Database integration
- UI enhancements

---

## 📸 Screenshots
![Dashboard](https://github.com/user-attachments/assets/54f06e7e-9838-48e9-b9d9-228d2ea8cdc1)
![Add Account](https://github.com/user-attachments/assets/dee2838b-3307-4288-acce-56e16b8e9bd1)
![All Accounts](https://github.com/user-attachments/assets/bd2671e0-e9a6-490b-ae9f-cad87bc36acf)
![Search Accounts](https://github.com/user-attachments/assets/13179fb2-1193-46fd-895c-1788eb887ec3)

---
## 🙌 Author
Shrikant Shinde
