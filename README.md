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

## User Guide 

1. Download the APK
2. For login:
    Create an email and a password or
    Register with google authentication
    
<em> Description </em> 

The application will use the images to perform an analysis of the emotions displayed and will generate a tag for the images and a category to which it belongs.
1. Inputs: A batch of images.
2. Source: Google Photos or user’s phone gallery.
3. Output: A category and tag for each image. 
4. Destination: Main application interface.

