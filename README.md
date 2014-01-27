Pop500
======

Simple demo application that shows infinite 'Popular' stream from 500px.com.


Features
--------

 - "Inifinite" ListAdapter. Only 4 bytes are permanently stored in memory for each image. Given that 'Popular' stream is about 15000 photos, that means that only 60k of memory is used.
 - Photo metadata is stored in LRU cache and loaded on demand.
 - Images are stored in LRU cache. About 1/3 of application memory is allocated for images. That allows cache to hold about 120 pictures on Nexus 4. Images are stored in RGB_555 format to lower memory consumption.
 - Both metadata and photos are prefetched for improved user expirience.
 

Limitations
-----------

Application only works on Android version 4.0 and higher. Tested only on Nexus 4 and Nexus 7 on Android 4.4.
Error handling is simple - logging to ADB and toast messages.
If you click on an image that is not yet loaded you'll get empty screen - there is no notification from downloader to image view activity.

Build
-----

Maven 3.1.1 is required. 

To build:
  `mvn clean package android:apk`
  
To run:
  `mvn clean package android:run`

Disclaimer
----------

This application is in no way affiliated with 500px, Inc, except for the fact that it uses their awesome API.
