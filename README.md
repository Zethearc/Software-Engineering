# Software-Engineering
Mobile application for emotion captioning using Google Photos.
## Libraries needed to run the application code

  plugins {
      id 'com.android.application' version '7.2.1' apply false
      id 'com.android.library' version '7.2.1' apply false
      id 'org.jetbrains.kotlin.android' version '1.6.10' apply false
      id "com.google.gms.google-services" version "4.3.13" apply false
      id implementation 'com.google.firebase:firebase-analytics:21.1.0'
      id implementation 'com.google.firebase:firebase-auth:21.0.6'
  }
## Importing the Project on Android Studio
1. Clone or download this repository:
     ```
     git clone (https://github.com/Zethearc/Software-Engineering.git)
     ```
2. Open Android Studio
3. Click on File -> New -> Import Project
     - **First Time using Android Studio:** Click Open an existing Android Studio project
4. Search for the directory `Sotfware-Enginnering.git` and select it.
5.  click OK and wait for it to sync.

The application will use the images to perform an analysis of the emotions displayed and will generate a tag for the images and a category to which it belongs.
1. **Inputs:** A batch of images.
2. **Source:** Google Photos or user’s phone gallery.
3. **Output:** A category and tag for each image. 
4. **Destination:** Main application interface.
5. **Requirements:** An initial batch of 8 photos. Photographs containing different elements on scene.


## User Guide 

1. Download the APK.
2. Copy the apk to your phone.
3. In your phone, search for the apk and install it. If your phone asks for confirmation or permission to install the apk, accept.
4. For login:
    Create an email and a password or
    Register with google authentication.
5. Select the photos from the Google Photos repository.
6. Begin the sentiment analysis and display the results.
 
 ## Troubleshooting
Bugs can be reported in the issue tracker on our GitHub repo: https://github.com/Zethearc/Software-Engineering/issues4

## Authors of this project
- Jaime Astudillo - jaime.astudillo@yachaytech.edu.ec - [LinkedIn](https://www.linkedin.com/in/jaime-astudillo-664754228/)

