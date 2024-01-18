# Facility Augmented-Reality Map (FARM)
This project developed for Google Developer Student Clubs Indonesia's Hackfest 2024. 
Innovation proposed by 4GEN team to help United Nation's Sustainable Development Goals. 
We focus on solving the problems pointed by SDG 9 and SDG 11. This project proposes a local area map with detailed 
information and various additional features. We implement Augmented-Reality (AR) to create better User Experience (UX).

## Technologies

### Android
We develop this application to run on Android. Supporting for at least Android 7.

### ARSceneView
This SDK to handle AR (ARCore) and 3D rendering (Filament) on Android. It's composable too.

### Firebase
This enables back-end to our application. Authenticate users using many methods, managing database in real-time, storing various file, etc.

### Jetpack Compose
UI implemented using composable part. It uses Material Theme and works good on Android.


## How does it work?
We separate role between administrator `ADMIN` and regular user `USER` in terms of authority to access several features
related to map configuration.
- `USER`: Purposed only to use our application as simple as possible. You can interact with map (e.g. walk around, asking navigation, reading information / presented data, etc.) without worrying about technical stuffs.
- `ADMIN`: We grant several _regular_ users to modify existing map, or even creating the new one. These users are responsible for everything on the map. Of course, we implement UI for `ADMIN` to keep everything simple.

With AR, we augment user experiences with virtually-created contents in 3D environment. AR contents reside in 3D space 
and can be represented as nodes which have their own translational & rotational properties. All nodes on an AR session 
are relative to **world coordinates**, the coordinates where the session starts. So, every session might have different 
world coordinates. Ignoring such **positional & orientation drifting** and relying on saved coordinates & orientation are
bad idea. So, we do adjustment to saved data correspond to current session. We use a calibrator once session start to translocate
AR contents properly.

### Nodes

AR contents are divided into 4 main category.
- `CALIBRATOR`: Node that have same physical and virtual representation to calibrate between _worlds_.
- `ENTRY`: Node for navigation that can be chosen as start / end point.
- `PATH`: Node that doesn't have representation, used for path planing, and act as a way.
- `RENDERABLE`: The just exist node. _Implemented for future usage._

### User

We expect `USER` to follow this workflow.
- Input map ID. Only one map loaded.
- Search destination to navigate. Search bar available.
- View information provided about destination. Next: AR router.
- Scan surface and calibration object for map initialization.
- Follow navigation once all set up.

### Administrator

We grant `ADMIN` more control over the application, which include
- Create map if current ID doesn't exist.
- Add detail about map: name, address/detail, and logo.
- Edit entry/destination information.
- Create, delete, link, edit nodes on map with specialized `ADMIN` panel.

## How is it going?
We need a lot of improvement ~~major feature still not properly working yet~~ with AR and can't be combined in one repository (somehow). So, we separate the **experimental** project
in different repository. Feel free to visit our experimental project. _Yes, we do a lot of crazy stuffs._

https://github.com/hanzamzamy/nexus-farmap

## How to reproduce?

This repository is compile-ready. We also provide our firebase credential. Authentication is not implemented yet, _debugging purpose_. Just use switch to toggle between `ADMIN` and `USER`.
Because we use firebase's free plan, limitation does exist. We subject to revoke the credential if such action needed. 

You can also use your own firebase credential. Just set up firestore. Image assets used in this application majorly stored in firebase storage.
But, it accessed over HTTP with URL stored in firestore. So, everything should be fine.

# About Us
4 Gentlemen en Nexus (4GEN) is a group of students from various universities in Indonesia who tended to become a **Nexus**.
Nexus is connection, Nexus is relation, Nexus is focal, Nexus is central. In Nexus, we tied, grew, and became better together.
- **Desman Dwi Saputra** from Universitas Singaperbangsa Karawang as Hacker.
- **Fikri Mahendra** from President University as Hustler.
- **Nathanca Arya Vijita** from President University as Hipster.
- **Rayhan Rizqi Zamzamy** from Institut Teknologi Sepuluh Nopember as Hacker. _<- Beware, non-human being!_
