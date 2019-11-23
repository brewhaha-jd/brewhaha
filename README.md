# Brewhaha
This repository will contain the backend (NodeJS) and frontend (Android) for the Brewhaha application for Junior Design 2019


# Zenhub Organization
Let’s keep the new issues column empty if possible.

Icebox can be issues that we need to implement whenever we have free time, later sprints

Backlog are for issues that need to be done in this sprint but we havent started yet.

In Progress is what’s in progress right now. Like you’re coding it.

Review is for when you have something finished but needs to be merged in/reviewed by someone on the team.

Done is when you have merged it.

We’ll move things to closed at the end of each sprint to help us keep track of what we’ve done in the sprint.

Please add story points whenever you create a new ticket/issue.

Story points are measured typically in hours. Realistically I think we’ll only allocate a few hours a week to the class. So each person should be able to handle maybe 10 points a sprint. So think about how much work each ticket requires and point it accordingly. Adding a simple UI button is 1 point. Adding a new view or data model might be a few.


# Github Organization
Remember to associate your commits with the correct issue as well, and assign it to a reviewer or to the team as a whole.

Please add comments on whats happening when you create a PR. That will help others quickly read and review your code.

# Code Development Steps
To develop on this repository, there's some simple steps.

To get the repo on your laptop, `git clone https://github.com/brewhaha-jd/brewhaha.git`

cd into this repo, get master: `git checkout master`
make a new branch to develop: `git checkout -b my_name.task`
code away!
to create a Pull Request (when you want to add your code changes to the master branch), `git commit -a -m "Add a message about what you did" `
then go to Github and click "Create Pull Request" and add reviewers.
Then when approved, click "Squash and Merge"

# Release Notes for Family Friendly Brewery Tracker v1.0
 
-New Software Features
Users can now add reviews for family friendliness.
Added the option to leave comments for amenities
Added user profiles
Updated Mongoose from v5.6.11 to v5.7.5
Photos placeholders added

-Bug Fixes
Fixed an issue with slow loading times upon opening brewery pages

-Known Bugs and Defects
Photos are currently placeholders and not occupied
Users cannot dynamically refresh the view brewery page to see their new review
They need to instead back out of the view and reload the list view
The back button on the Android OS in the app is sometimes buggy
Sometimes the app hangs and can’t load the brewery list
Filtering by some reviews either don’t work or takes a long time

# Install Guide for Android
The android application can be installed through android studio. The instructions for android studio can be found here: https://developer.android.com/studio/install

After following the instructions to install Android Studio, the SDK also needs to be installed in addition to the Android emulator that will allow you to run the app on a virtual phone on your computer: https://developer.android.com/studio/run/emulator

To open the app, simply open Android Studio and click ‘open’. This will bring up a file explorer. Simply navigate to the Android/ folder of the github repository you downloaded in the first section of this guide. This folder contains all the Android app code. To run the app, simply click the green play button that will be located on the top toolbar of Android Studio. To the left of the toolbar there are two dropdowns, one for the emulator and one for the build variant. The emulator drop down will be pre-populated with whichever emulator you downloaded (ie. Pixel, Pixel 2, etc.). 
The build-variant dropdown should say ‘Android-app’. If this is not the case, then simply click on the drop down, click ‘edit configurations’, then the ‘+’ icon in the top left of the popup window. This will bring a drop-down menu, from which you will select ‘Android App’. After selecting this a new build variant configuration will appear on the right side. In the top bar name the configuration ‘Android-App’ and then in the ‘Module:’ selection select ‘Android-app’.

After this is setup (if need be) then you can simply just make sure its selected in the dropdown to the left of the play button and then just click the play button. If you have any issues with the build/it stops working after a few runs simply go to the ‘Build’ option in the apps menubar and then click on ‘Clean Project’ and then ‘Rebuild Project’. This will delete all the generated files and rebuild them. This method usually fixes 90% of problems with the build/Android Studio.
