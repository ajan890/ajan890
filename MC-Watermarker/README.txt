README v1.0
SOFTWARE v1.0

#ABOUT
This software was originally written for ENDER SMP Technical Minecraft Server in order
to facilitate watermarking screenshots produced by players while connected to the ENDER
SMP survival or creative servers.

#FIRST LAUNCH
On the first launch, the user will be greeted with a welcome screen, and prompted to choose
an image, either a .jpg or .png (support for other file types may be added in the future),
to use as a watermark.  This can be changed later.  The file can be selected either by opening 
the file explorer in the program (by clicking on the button marked "Choose File"), or by 
pasting/typing the path of the image in the text field.  Selecting the file will close the window 
and prompt the user to restart the program, completing the first boot setup.

#INSTRUCTIONS FOR USE

-Settings:

Location - dictates where the watermark should be placed.
Selecting the radio button "Top Left of Image" places the watermark at the top left corner of
the image.  Selecting the radio button "Top Right of Image" places the watermark at the top right
corner of the image, et cetera for the other buttons.

Watermark Opacity - controls the transparency of the watermark.
Opacity is set to an integer value between 0 and 100 percent, inclusive, with 0 representing 
complete transparency and 100 representing complete opaqueness.  Opacity can be set by 
either typing in the text field or dragging the slider.  If an invalid input is entered in the 
text field, it will automatically correct to "100".

-More Settings: can be accessed by clicking the "More Settings" button.  This will open a new window.

Minecraft Screenshots Folder - the path of your Minecraft Screenshots Folder.
This will be the folder Watermarker queries for new images when tracking is started.  The file path 
can be changed to refer to any file.  Clicking the "Auto-locate" button will show the directory of 
your Minecraft screenshots folder, if it exists.

Set Watermark - Allows the user to set a new image as a watermark.
The new watermark image can be chosen by either pasting/typing the path into the text field, or 
selected by opening up the file explorer using the "Choose File" button.  The file selected must 
be an image file, with a .png or .jpg file extension.  (Support for more file types may be added 
later.)  If the user does not wish to change the watermark image, the textfield should be left
blank.

Final Image Opacity - controls the transparency of the watermarked screenshot.
Final Image Opacity is set to an integer value between 0 and 100 percent, inclusive, 
with 0 representing complete transparency and 100 representing complete opaqueness.  Opacity can 
be set by either typing in the text field or dragging the slider.  If an invalid input is entered 
in the text field, it will automatically correct to "100".

Watermark size (corners) - controls the size of the watermark if the set location is at a corner
Watermark size is set to an integer value between 0 and 100 percent, inclusive, with the number 
representing the size of the watermark compared to the maximum size that can fit on the image.
Watermark size can be set by either typing in the text field or dragging the slider.  If an 
invalid input is entered in the text field, it will automatically correct to "100".

Watermark size (center) - controls the size of the watermark if the set location is at a center
Watermark size is set to an integer value between 0 and 100 percent, inclusive, with the number 
representing the size of the watermark compared to the maximum size that can fit on the image.
Watermark size can be set by either typing in the text field or dragging the slider.  If an 
invalid input is entered in the text field, it will automatically correct to "100".

File check timer interval - controls how often the folder is queried for new images
Every time the timer fires, the folder provided in the "Minecraft Screenshots Folder" textfield 
will be queried for new screenshots.  The minimum possible amount of time that can be set is 
1 second.  If an invalid input is entered in the text field, it will automatically correct to 
"5".

#Resetting to defaults
Clicking on the "Reset to defaults" button will reset all settings to their defaults.
The default settings are as follows:
Location: Bottom Right of Image
Watermark Opacity: 100%
Final Image Opacity: 100%
Watermark size (corners): 10%
Watermark size (center): 100%
File check timer interval: 5 seconds

#Apply
Clicking on this button will apply the settings selected.
WARNING: NOT CLICKING APPLY AND CLOSING THE WINDOW WILL DISCARD ALL CHANGES MADE TO THE SETTINGS.

#Cancel
Clicking on the "Cancel" button will close the window and discard all changes made to the settings.

#Minimize Window
Makes the window smaller.  Original window size can be restored by clicking on the "Expand Window"
button.

#Start Tracking
Starts the program!  The timer will begin firing and every image file with a .png or .jpg extension 
added in the selected folder will automatically be watermarked and placed in the a "watermarked" 
folder.  The original screenshot will not be deleted or altered in any way.  To stop the program, 
click on "Stop Tracking".

#Log
Prints out the images that are watermarked.  The log is deleted when the program is exited.

#Files
The setting files for this program, as well as the selected watermark and a backup watermark images 
are stored at file path C:\Watermarker.  Settings may be changed through the file, although it is 
discouraged, since it may cause unexpected errors in the program.  If the settings.txt file is 
deleted, the program will restart at the "Welcome" screen, and the settings.txt file will be 
recreated.  The user may change the watermark photo at this location, but it must be a .png or .jpg 
image file, and it must be named "watermark".
