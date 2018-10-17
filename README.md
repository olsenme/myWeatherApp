# Android Weather App
By Meagan Olsen 

# Instructions
1) Navigate to <a href="https://github.com/olsenme">repository</a> <br>
2) Clone locally ```git clone https://github.com/olsenme/WeatherApp.git```<br>
3) Obtain Open Weather Map API key <a href="https://openweathermap.org/appid">here</a><br>
4) Open up project in Android studio<br>
5) Navigate to app->src->main->java->com->example->android->weatherwithsqlite->utils->OpenWeatherMapUtils.java<br>
6) Replace OWM_FORECAST_APPID with your api key in between "".<br>
6) Click "Run" at the top.<br>

# Discussuion
I used Java,Sqlite, and OpenWeatherMap API to create this app. 

# Requirements
 <ul>
  <li>Build a baisc Android App to display scrollable weather data for a given location displayed at the top of the main page of the application. </li>
  <li>Displayed Data is pulled from Open Weather Map API's 5-day forecast and includes: date and time of calculation(UTC),the temperature(Kelvin, Metric),a genreal description of the weather.</li>
  <p>A progress bar is displayed while data is feteched and hidden after it is fetched. If an error occurs while featching data, an informative error message is displayed.</p>
  <li>Handle user clicks on indivividual items in the forecast list. </li>
  <p>When an item is clicked on,a new activity is started, which displays a deatiled forecast with a description of the forecast item.</p> 
  <li>Incorates a feature into the action bar which allows user to see a map of the location for which the forecast is displayed.  </li>
  <li>Incorates a feature into the action bar which allows user to share the contents of the detailed forecast.</li>
  <li>Handle screen rotations without reloading weather data. </li>
  <li>Handles viewing preferences for weather units(Imperial,Metric,Kelvin) and weather location(any valid city entry).</li>
  <li>saves previously viewed locations in SQLite database. </li>
  <li>Displays saved locations in navgation drawer in naviation bar on Main page. </li>
  <li>Handles clicks on a previously saved location from the navigation drawer and updates current viewing location.</li> 
  <li>Provides a dialog for which a new location can be added to the database from within the navigation drawer. </li>
  
</ul> 


