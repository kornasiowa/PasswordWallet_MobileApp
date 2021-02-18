# Password Wallet
The mobile application for Android allowing storage encrypted passwords.

## About
The application is addressed to users who want to store their access passwords for various platforms in one place in a safe form.

The user is obliged to sign up. Main password to access the application will be encrypted with one of the implemented encryption algorithms: SHA512 or HMAC. 
The mechanism of encrypting passwords stored in the wallet is based on the AES algorithm with the use of a key in the form of the hash 
of main access password to the user's account. The application has been protected against unauthorized access and brute force attacks 
(checking failed login attempts based on IP address).

Additionally, the possibility of sharing passwords, two operating modes of the application and a mechanism for restoring changed or lost passwords have been implemented.

## Overview
The login screen allows the user to authorize with an application account or go to the registration form 
in which user chooses individual username, main account password and encryption algorithm.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-133016_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-133047_Password%20Wallet.jpg" width="250">
</p>

The home screen contains a list of stored and shared user passwords. Selecting an item from the list displays the details of the password and its encrypted form. 
Pressing the key symbol will start the function responsible for decryption and displaying the password preview.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-144905_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145008_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145014_Password%20Wallet.jpg" width="250">
</p>

Additionally, in the lower right corner there is a button responsible for displaying the form for adding a new password to the wallet. The screen with the form is shown below.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145625_Password%20Wallet.jpg" width="300">
</p>

In the details view, a field with the name of the owner of the password will also be displayed if the password has been shared with you. 
The user can only view such a password. There is no access to edit or delete this password. Alternatively, the user can resign from sharing the password by selecting the only active *stop* button.
The password will disappear from the list of all passwords on the home screen.

If the user is the owner of the password, selecting the password sharing option will display an appropriate dialog in which the user must provide the username with whom he wants to share the password.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145044_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145442_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145118_Password%20Wallet.jpg" width="250">
</p>

After logging in, the application is in Read-Only Mode by default, 
in which we can add, view and share passwords. Access to editing and deleting passwords is blocked.

Switching the application into the Modify Mode is possible from the Settings screen. 
In this mode, it is possible to edit and delete passwords. The buttons associated with these functions become active.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145410_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145352_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145637_Password%20Wallet.jpg" width="250">
</p>

After two unsuccessful login attempts from a given IP address, access to the account will be blocked temporarily or permanently if the number of attempts exceeds 4 attempts.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145244_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145741_Password%20Wallet.jpg" width="250">
</p>

On the Settings screen, the user can switch the application operating mode, change the main account password and check the data related to the attempts to access the account.

Changing the master password re-encrypts the passwords stored in the wallet.

User can unblock the IP address with blocked access.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145832_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-145840_Password%20Wallet.jpg" width="250">
</p>

The Activity Log screen contains information about operations related to creating, modifying, sharing and viewing passwords by the application user. 
The items on the list can be sorted according to the selected operation type.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-150334_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-150149_Password%20Wallet.jpg" width="250">
</p>

In the case of unwanted editing or deletion of the password, the changed data will be secured and the user will be able to restore the modified / lost passwords at any time.

For other operations, it is not possible to restore the previous data form.

<p float="left">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-150058_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-150032_Password%20Wallet.jpg" width="250">
<img src="https://github.com/kornasiowa/PasswordWallet_MobileApp/blob/master/screenshots/Screenshot_20210217-150108_Password%20Wallet.jpg" width="250">
</p>

## Tools
- Java 8
- Android
- SQLite

##### *The graphics used in the application have been designed and created by me.*
