# Ola-Music-Player
Fully material designed basic music player developed for OLA music player challenge 2017.

# Pre-work - *Ola-Music-Player*

**Ola-Music-Player** is an android app that play music online, developed for OLA music challange 2017. Mainly focused on design part, contains some bugs so needs to be worked on.

Submitted by: **Suraj Nair**

Time spent: **16** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **browse the aviable songs** from the music list
* [x] User can **play by music by taping on the desired music item**
* [x] User can **marke there faviourate musics**

## Image Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://user-images.githubusercontent.com/20771959/36082358-ad34563c-0fce-11e8-9d9f-94421571f52a.png' title='Home Screen' width='' alt='Image Walkthrough' />

<img src='https://user-images.githubusercontent.com/20771959/36082540-73b3fa0a-0fd0-11e8-8b91-ee42386e294a.png' title='Detail screen' width='' alt='Image Walkthrough' />


## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** Android app development is really cool. We can develop eye catching UI using material design or by any other open source library. One thing really cool about android is open source and lots of developer support, you can find solution of almost everything, which I guess makes android more developer friendly.

Creating and maintaining  layouts/views are very easy, thanks to Android Studio.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:**  Adapters are like binders for views specially for List view or Grid view. It binds the data set with listview. It also know the size and how to represent the each cell of list/grids.

I have used Base adapter for my custom grid view. It is important part of android structure because at runtime you can change UI elements of View Group.
The concept of convertView is introduced to increase the run time and GPU drawing efficiency of listview. It only initialize once through out the app because of its static nature and reuse the views for all data elements.

## License

    Copyright 2018 Suraj Nair

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
