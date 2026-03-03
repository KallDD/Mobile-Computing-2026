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