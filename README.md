# ItemDisplayer

## Introduction
This project have been realized to practice the Android development.

## Objectives
* This app have to display a list of items sent by a webservice.
* All the data that have already been downloaded have to be available offline after a restart.

## What's this app doing?
This app is fetching a JSON file on a Web server containing 5000 JSON Object.
The data are stored in a SQLiteDatabase to make our work easier and allow us to fetch them offline.
When the app is starting, a new IntentService start to fetch the JSON and parse all the objects to store them in DB.
The file is quite big so we are just waiting to have enough updated object before showing the activity.
If the user don't have access to internet, we display the information that have been downloaded last time.

In the MainActivity, we are displaying all the items. Each item have an URL to a photo and to a thumbnail.
To make our app faster and not consume to much memory, i'm using an endless RecyclerView. When we are scrolling, the app is downloading automaticaly more photo in background so it feel like there is no end.

## Libraries used
* GSON: To parse the JSON into objects. https://github.com/google/gson
* Stetho: To inspect the content of the phone and the database. http://facebook.github.io/stetho/
* Picasso: To download, put in cache and show the images. https://github.com/square/picasso
* Butterkniffe: To have a code more clear and don't have to always link views to variables manually.  http://jakewharton.github.io/butterknife/
* Powermock mockito: To mock classes and static methods in unit tests. https://github.com/powermock/powermock/wiki/mockitousage
