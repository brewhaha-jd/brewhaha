# Important code parts for Code Review

1. Android
  * All of the code is stored inside the java/com.example.brewhaha_android/ folder
  * The Models directory is just a bunch of data stores, dont worry about reviewing this
  * Api directory contains boilerplate code for Backend connections, don't worry about reviewing this
  * The main code to be reviewed is in the Controllers/ package
    * There are many files in this folder so just focus on the main ones:
      - HomeActivity.kt
      - RegisterActivity.kt
      - LoginActivity.kt
      - ViewBreweryActivity.kt
      - MapsActivity.kt
      - EditBreweryActivity.kt
      
2. Backend
 * All written code can be found in the following folders
      - controllers
      - error_handlers
      - middleware
      - models
      - services
 * Typical code path is controller -> middleware -> service -> entityMapper -> entityRepository -> Entity
