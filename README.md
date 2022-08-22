## Panjika
#### A realtime panchangam

### Development mode
```
npm install
npx shadow-cljs watch app
```
start a ClojureScript REPL
```
npx shadow-cljs browser-repl
```

### TODO 
#### Basic
- [X] Calculate/Display Tithi, Nakshatra, Raasi, Maasa, Vaara, Yoga, Karana for selected Date-Time.
- [X] Progress percentage for Tithi, Nakshatra, Raasi, Yoga, Karana.
- [X] Chart to display realtime Su, Mo coordinates on the ecliptic.
- [X] Add Input to change selected time.
- [X] Add Time lapse, from general (1 sec per sec) to 1 hr, 1 day, 1 month per sec.
- [X] Add basic Bhaava Chakra.
- [ ] Make this a PWA.
#### Chart
- [X] Show the Ecliptic Line.
- [X] Ayanaamsa, 0deg of Raasis on X-Axis.
- [X] Full Moon & New Moon lines to indicate phases.
- [ ] Lunar Nodes (Maybe ?)
- [ ] Make it expandable & downloadable.
#### Bhaava Chakra
- [ ] Denote East (Lagna), West, Up & Down directions. 
- [ ] Get users approx. location from IP (avoid prompting), to show relevant chakra.
- [ ] Ability to switch selected location.
- [ ] Calcuate & Highlight Retrograde.
