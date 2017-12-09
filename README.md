# dictionaryutil

Two Java utilities to generate a dictionary based on multiple words lists while blocking obscene terms and another to display MD5s or other binary hashes as dictionary words.

For example, rather than displaying a unique id as `d86d41a993da2ff7a6136c0e3f1f9762`, it could be displayed as `Faintier Harbinson 4LT`.

## Sample output from DictionaryGenerator: 

```
Obscene words loaded: 1012
All words read:       1171988
Big words ignored:    204586
Weird words ignored:  88521
Possible words read:  867728
Duplicates:           436291
Obscene words ignored:86269
Dictionary capacity:  262144
Final clean list:     345168
-----------------------------
Extra unused words:   83024
Reloaded 262144 words of 262144 capacity
Sample words: 
         Ad      Okehs     Infers    Babtist    Mudsill   Bewizard   Muckrake  Antiquary  Nonhectic Bridalveil
    Humulus  Rathgeber    Immixed    Oblasts  Puchalski     Misake   Vargason      Crone  Cavillous    Nephros
  Rainfalls    Savoyed    Vujovic     Loxing   Fineschi    Thienyl    Benisha     Fannys      Qatar  Errickson
      Nouss   Emperces      Youtz    Trishaw      Mabee      Rafiq  Unsternly Absorbency  Impeccant      Benda
   Santalum   Untrusty  Altenburg  Edeoscopy  Resurface  Uniocular  Drakonite      Oasts   Bicycled     Yrnehs
   Thibeaux  Countered   Hindfeet   Donnetti   Vulcanic     Hodads     Bidart  Leninakan   Pinksome    Hairdos
  Gunderman  Phytosaur     Risker      Pawns      Umble      Turri    Trollys      Gonks   Nosewort     Helvin
     Jumped    Inesdes     Semble   Nicolett    Shottle   Squantum   Bedemans     Tulnic  Copperahs    Zinging
     Tolyls     Narded   Menderes     Tomato     Larosa    Pronaoi   Hydroids       Tyee     Alikee   Dreamery
    Steinem      Adopt    Emprise    Outkeep    Maormor  Spanbauer     Dampen    Hiroshi   Nautches   Saltings
   Gracindo    Rogalla   Sorbello      Coyer      Plews     Dingly    Unleads    Maclisp   Volantes  Hippiedom
   ```


## Sample output from DictionaryMask:

```
MD5 hashes:
d41d8cd98f00b204e9800998ecf8427e -> Alutaceous Honold 3WV
9f6290f4436e5a2351f12e03b6433c3c -> Bellinger Iain GS6
feb0fc2091ac843ab3af3a080527da23 -> Cellager Buffed 4PA
577986039ff41a291583daba48752735 -> Bobbiner Flappiest 79F
458203663623d98d152cce0d8c3ded00 -> Flatters Downy D6Z
53a1bd7cdf5b0e06649927e5887767c4 -> Gaqo Granulated NN5
d86d41a993da2ff7a6136c0e3f1f9762 -> Faintier Harbinson 4LT
3a81fbd3bc71c785a8d2d04594a8eebb -> Flame Betises F3N
dd3c0d101fc7bd1d0fe79e90e1dc5418 -> Durwan Fogeater 7ES
f736a57b955831cdaf3fe7f5850cdee1 -> Drout Chicky 5HL

SHA-256 hashes:
e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 -> Celibatic Ammiel 6PF
f223faa96f22916294922b171a2696d868fd1f9129302eb41a45b2a2ea2ebbfd -> Anagenesis Hardliners R4Z
be09eb9b2b823fee4f7ed35ba59f1ef1e0a1b9134594460853ae4eabc39fa05a -> Deedfully Hadjis A4Y
fc85205708f2c11a24d3c8e88927cf56cda201e772ba676c603eae688996b1a9 -> Bransle Antedate 24V
ff4d616a3d8dbd5843452a7a5330ae6245303bed1450b8470831b80c012b8b2c -> Emulging Chafin FQY
8f0d67668935b6f8fdcaa7ae9cb93a06dbe9983614d147901dc5d6e9034a8e9d -> Delaura Cerealist 2B3
ee80ad9910434ccc25fc4d540c4fcf61478a64e040cac65ae0db815b31339d96 -> Flagman Babyfied 464
63efd224c60478818eab329b4420747033e4a3d81d5c0f0d970b2c913b3a436  -> Dyeing Agrippa JVM
9615eccb5e7379d872c4531eb7134740fdc0be6ed8cad520be40a91142e00d21 -> Albespine Evolver N6N
4b21b09aaf34b0a2d3b460ee00e19d11bb2d10e4f3e20aea03c9405c85433bc1 -> Amli Habitude B93
```

