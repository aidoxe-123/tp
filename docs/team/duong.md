---
layout: page
title: Huynh Thai Duong's Project Portfolio Page
---

## Project: McGymmy

McGymmy is a **desktop app for Software Engineers who need help managing their diet, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). 
This project has about 10 kLoC.

Given below are my contributions to the project:

* **Code contributed**:
[RepoSense link](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=aidoxe-123&sort=groupTitle&sortWithin=title&since=2020-08-14&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

* **New Feature**: Create the food model.
  * What it does: this is the food item that McGymmy keeps track of
  * Justification: 
  * Highlights: It is important that this feature must be bug-free, otherwise it could cause subtle bugs in other parts of the code base, such as the one mentioned in
 this [pull request](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/120)
  
* **New Feature**: Added the date property for food.
  * What it does: allows user to add date for the food item
  * Justification: This enhancement includes updating the UI so that it shows date property, and updating the storage to store date
  * Highlights: This date property can accept inputs in various formats
  * Credits: The idea of implementing this is taken from this StackOverflow's [post](https://stackoverflow.com/questions/4024544/how-to-parse-dates-in-multiple-formats-using-simpledateformat)

* **New Feature**: Added the ability to undo previous commands.
  * What it does: allows the user to undo all previous commands one at a time.
  * Justification: This feature improves the product significantly because a user can make mistakes in commands and the app should provide a convenient way to rectify them.
  The process of implementing this feature is difficult and time-consuming, as it includes testing if it interacts correctly with all existing components.
  This process help me found a bug relating to mutable `Tag` set in `Food` and fix it
  * Highlights: This enhancement affects existing commands and commands to be added in future. 
  It required an in-depth analysis of design alternatives. The implementation too was challenging as it required changes to existing commands.

 * **New Feature**: Allowed finding food by tag. [\#82](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/82)
  * What it does: allows user to categorize food by tag
  * Justification: I found that user can add tag to food item, but there is no meaningful usage of them beside viewing. 
  Therefore I decided to make find command to work with tag, so that users can use tag as a way to categorize their food.

* **Enhancement**: 
  * Tweaked [AB3's UniquePersonList](https://github.com/nus-cs2103-AY2021S1/tp/blob/master/src/main/java/seedu/address/model/person/UniquePersonList.java) to become a `Fridge` class that allows duplicate foods.  
  * Added tests for codes written by my teammates to minimize the possibility of getting bugs: [\#70](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/70),
[\#69](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/69)
  
* **Project management**:
  * Enabled assertion on Java CI [\#109](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/109) 
  * Set due date for v1.4 milestone
  
* **Documentation**:
  * User Guide:
    * Added documentation for the `undo` feature [\#131](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/131)
    * Updated the existing commands to include date, and add an appendix to show supported date formats [\#91](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/91)
  * Developer Guide:
    * Document the undo feature in the DG, including updated all the diagrams inside that section
    * Updated the user profile, value proposition, and user stories [\#17](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/17)
    * Updated the glossary [\#150](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/150), [\#41](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/41)
    * Update storage class diagram [\#100](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/100)

* **Community**:
 * PRs reviewed (with non-trivial review comments): [\#39](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/39),
 [\#57](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/57), [\#76](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/76),
 [\#104](https://github.com/AY2021S1-CS2103T-W17-3/tp/pull/104)
 * Contributed to forum discussion: 
   1. Help fix trailing white space [forum#276](https://github.com/nus-cs2103-AY2021S1/forum/issues/276)
   2. Share how to temporary disable checkstyle [forum#260](https://github.com/nus-cs2103-AY2021S1/forum/issues/260)
   3. Explain why a test fails: [forum\#65](https://github.com/nus-cs2103-AY2021S1/forum/issues/65)
 * Reported bugs and suggestions for other teams in the class: [List of bugs and suggestions](https://github.com/aidoxe-123/ped/issues)
