# Clan Events
A plugin helpful for clan events.  It creates an overlay that displays the event password and time (UTC) to your screen.
This plugin can utilize Google Sheets API to retrieve data from Google Spreadsheets.  

The plugin will retrieve data from the spreadsheet provided and print out the first two columns as a side panel on runelite:

**Home Panel:**

![image](https://user-images.githubusercontent.com/14130954/111088303-e8f61b00-84fc-11eb-8d36-86e507e454e4.png)
![image](https://user-images.githubusercontent.com/14130954/111088168-4342ac00-84fc-11eb-824d-55417141d9ee.png)

**Skill of the Week:**

![image](https://user-images.githubusercontent.com/14130954/111088314-fe6b4500-84fc-11eb-8cfc-deee73274396.png)
![image](https://user-images.githubusercontent.com/14130954/111088186-56557c00-84fc-11eb-957f-f4195722d838.png)

**Boss of the Week:**

![image](https://user-images.githubusercontent.com/14130954/111088333-104ce800-84fd-11eb-9623-5b7479df3fec.png)
![image](https://user-images.githubusercontent.com/14130954/111088200-666d5b80-84fc-11eb-929e-7797c237f853.png)

# Create your own Google spreadsheet and API Key
The spreadsheat must be set to public and the sheet tab names must be one or more of the following: home, sotw, botw, event, hof.

**Example spreadsheet (Work in progress):** 

https://docs.google.com/spreadsheets/d/1tB8g7EdOnZ5zC2mX1kJJlq0-ZBxPKf9Abz_51UY3JDU/edit#gid=597865158

The Participant list of BoTW and SoTW on the spreadsheet is directly grabbed from https://templeosrs.com.

The Google Sheet ID and API must be placed in the config in order for the plugin panel to work.
You can simply grab the Google Sheet ID from the url of the spreadsheet:

![image](https://user-images.githubusercontent.com/14130954/111088373-4722fe00-84fd-11eb-9407-ff972e29c5c0.png)

You can create your own API Key from here: https://console.developers.google.com/apis/credentials.

**Note:** In order for you API Key to work you must enable the Google Sheet API from the developers console found here: https://console.developers.google.com/apis/library/sheets.googleapis.com

If you are using this plugin for your clan, you can simply share your spreadsheet ID and API Key with the rest of your clan members.
