# Facility Augmented-Reality Map (FARM)
This project developed for Google Developer Student Clubs Indonesia's Hackfest 2024. 
Innovation proposed by 4GEN team to help United Nation's Sustainable Development Goals. 
We focus on solving the problems pointed by SDG 9 and SDG 11. This project proposes a local area map with detailed 
information and various additional features. We implement Augmented-Reality (AR) to create better User Experience (UX).

## How Does It Work?
We separate role between administrator `ADMIN` and regular user `USER` in terms of authority to access several features
related to map configuration.
- `USER`: Purposed only to use our application as simple as possible. You can interact with map (e.g. walk around, asking navigation, reading information / presented data, etc.) without worrying about technical stuffs.
- `ADMIN`: We grant several _regular_ users to modify existing map, or even creating the new one. These users are responsible for everything on the map. Of course, we implement UI for `ADMIN` to keep everything simple.

