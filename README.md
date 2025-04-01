# Installation
To install this plugin, go to the plugin hub and search for "Clan Events" or "Elysium" and click install on the first plugin.  The overlay will work as long as you modify the password.  To get the clan hub panel to show, you must include the Google Sheet ID and Google Sheet API key provided by your clan:

![](https://i.gyazo.com/afcdfeb5a09ea9f84d635a359eab25fb.gif)

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

**Example spreadsheet:** 

https://docs.google.com/spreadsheets/d/1YMcXxSL3s1NEzsPVMMkPn7EdGNFKENiwqNyDKkJTO80/

Make sure the sheet is public, otherwise the plugin will not be able to read the Google Sheet:
![image](https://user-images.githubusercontent.com/14130954/120233818-9fc48600-c224-11eb-85f3-86eb7f31290e.png)

The Participant list of BoTW and SoTW on the spreadsheet is directly grabbed from https://templeosrs.com.

The Google Sheet ID and API must be placed in the config in order for the plugin panel to work.
You can simply grab the Google Sheet ID from the url of the spreadsheet:

![image](https://user-images.githubusercontent.com/14130954/111088373-4722fe00-84fd-11eb-9407-ff972e29c5c0.png)

**Google API Key**

Before you create your API Key, you must first login to your Google account and enable the Google Sheet API from the developers console found here: https://console.developers.google.com/apis/library/sheets.googleapis.com

Once you enable the API, you can create your own API Key from here: https://console.developers.google.com/apis/credentials.

**Note:** There is a limit of 300 requests per minute for your API Key (or 60 requests per minute for an individual).  If you ever run into a limit, its best to have multiple API Keys for your usage.

If you are using this plugin for your clan, you can simply share your spreadsheet ID and API Key with the rest of your clan members (I suggest you don't create it with your personal email).

# Wise Old Man Integration

Example Spreadsheet and instructions (by hypnolope): https://docs.google.com/spreadsheets/d/1jruc7-QoezuP2d0jPNfgaPnIERezhRQ4FuQRuQswP1M/edit?usp=sharing

![image](https://github.com/user-attachments/assets/292c0b02-fa80-4ee3-840b-e01420933ee2)

