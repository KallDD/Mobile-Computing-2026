# Mobile-Computing-2026

# Project

### Adding API weather data to the app database
To add the weather data to the app database I created 4 new classes (WeatherWithHourly, WeatherDataEntity, HourlyDataEntity and weatherDataDao). I also needed to create mappings from the already exsisting WeatherData (Used by the API request) model to the new app database model (Ai helped to create mappings). 

#### Entities
The API weather data is split into two differen entities due to room databes not handling Lists as single items. There are now two tables one for weather information and the other holds the hourly data. The wheaterDataEntity is simple and easy, because it is only fields and an autoincrementing id. The HourlyDataEntity is more complex, because it uses foreignkey element and its own basic columns. The foreingkey is wheaterId which is the id of the "parent" information in WheaterDataEntity table.

#### Loading Entities as usable data
The WheaterWithHourly data class is used to "format" the data form both entities to one class. It combines the hourly data from its table to the corresponding wheater information using @Relations. This class is used as the responsevalue of the database querries.

#### Querries
WheaterDataDao contains all querries used to Insert and Select both WheaterDataEntities and HourlyDataEntites. Most complex @Transaction is inserWeatherAndHourly which is called when new wheater data is saved in WheaterScreen. insertWheaterAndHourly takes two parameters, wheater and hourly which are made by taking the current WheaterData that the api has given to the user and calling .toEntity to get the correct data for wheater parameter and .toHourlyEntities to get the correct data for hourly parameter. Inside the @Transaction first the wheater data is inserted by calling another Insert querry and then the hourly data is Inserted to its table with the correct foreingkey gotten from the wheater Insert.

#### Displaying the data
Wheater data is displaed in WheaterScreen. Inside WheaterScreen if database contains data the latest entity is shown to the user. Else no data is shown. The user can get the current days wheater data by pressing "Get wheater" button. Then the data that the api provided is displayed and a new save data button is displayed. When the data is saved to the database, it will be loaded automatically the next time user opens the page. If there are multiple entries in the database they can be seen with the previous and next buttons. This was implemented by making a list of all the ids of the wheater data in the data base and then displaying a certain id. The displayed entity could be changed by adding or substracting 1 from local index variable which then changes the current id which changes the displayed data.
```
var ids by remember { mutableStateOf<List<Long>>(emptyList()) }
var index by remember { mutableStateOf(0) }

val canPrev = hasDb && index < ids.lastIndex

if(canPrev){
    index++
    dbWeather = withContext(Dispatchers.IO) {
        weatherDataDao.getWeatherWithHourly(ids.get(index))
    }
}
```

### Splash Screen
The spalsh screen is made utilizing ```androidx.core:core-splashscreen:1.0.0```
#### Replacing the default splash screen
Setting my splash over the default android is done with ```themes.xml``` and ```AndroidManifest.xml```. I added a new style to the ```themes.xml``` file that uses itmes provided by ```androidx.core:core-splashscreen:1.0.0```
```
<style name="Theme.App.Starting" parent="Theme.SplashScreen">

        <item name="windowSplashScreenBackground">@color/white</item>
        <item name="windowSplashScreenAnimatedIcon">@drawable/animated_logo</item>
        <item name="postSplashScreenTheme">@style/Theme.ComposeTutorial</item>

    </style>
```
The items are pretty much self explanotary, but I will ellaborate more for the sace of this report.

```<item name="windowSplashScreenBackground">@color/white</item>``` This line defines the background color of the splash screen. It could be a drawable resource like in the default splash screen.

```<item name="windowSplashScreenAnimatedIcon">@drawable/animated_logo</item>``` This item is the animated logo which requires and logo recource that can contain animations. How my app logo is animated is explaind in detail down below.

```<item name="postSplashScreenTheme">@style/Theme.ComposeTutorial</item>``` This item is what style will be used after the splach screen. My app changes back to the default style.

Inside the android manifest the main activity theme is set to the added splash screen style ```Theme.App.Starting```. 

```
android:theme="@style/Theme.App.Starting">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.App.Starting">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```

#### Logo
The logi an svg logo from androidsuriods own svg bank. This was the cleanest way to get a professional looking logo with out spending too much time on creating the image my self.
#### Logo Animation
The logo animation is created by making another drawable recourse of type ```animated-vector```. This resource is given the original unanimated svg as a "parameter" ```(android:drawable="@drawable/logo")```. Then a target is defined with the animation and which part of the logo will be animated. 
``` 
<target
        android:animation="@animator/logo_animator"
        android:name="animationGroup"/>
```
The animationGroup refers to the original logo drawable xml file which I added a group with name animationGroup.

```
<vector xmlns:android="http://schemas.android.com/apk/res/android" android:height="24dp" android:tint="#000000" android:viewportHeight="24" android:viewportWidth="24" android:width="24dp">
    <group
        android:name="animationGroup"
        android:pivotY="12"
        android:pivotX="12">
        <path
            android:fillColor="@android:color/white"
            android:pathData="M19.35,...."/>
    </group>
</vector>
```
The "@animator/logo_animator" refers to a animator recourse that contains the actual animation(movement) of the logo.
```
<?xml version="1.0" encoding="utf-8"?>
<objectAnimator xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="4000">

    <propertyValuesHolder
        android:propertyName="translateX"
        android:valueType="floatType"
        android:valueFrom="-10"
        android:valueTo="0"/>

    <propertyValuesHolder
        android:propertyName="scaleX"
        android:valueType="floatType"
        android:valueFrom="0.0"
        android:valueTo="0.4"/>

    <propertyValuesHolder
        android:propertyName="scaleY"
        android:valueType="floatType"
        android:valueFrom="0.0"
        android:valueTo="0.4"/>
</objectAnimator>

```
