# CU Bus Tracker
This is the android version of the BuffBus app. It's used to visualize the bus information from the BuffBusTracker, hosted at http://cherishapps.me:8080/. Information comes initially from http://buffbus.etaspot.net/.

## App description
CU Bus Tracker helps University of Colorado at Boulder students, faculty and affiliates commute by showing next bus times for CU operated buses.
Bus routes that are tracked:

* Buff Bus (Williams Village)
* Hop Clockwise and Counterclockwise
* Athens Route
* Late Night Transit Routes
* Discovery Express Route
  
## App link
  https://play.google.com/store/apps/details?id=com.cherish.BusTracker

Next Release:
  * Make start page more sharp (change buttons, feedback button, font+colors)
  * Make buttons responsive (splash effect?)
  * Stampede (RTD integration)
  * Set default zoom on location click
  * Add DNS record and IP redundency
  * Show network information to the user (no connection vs server not responding)
  * Add loading indicator?

TODO:
  * Show announcements somehow?
  * Show data integrity estimates on bus times?
  * Unit tests for each function (In progress)
  * Data JSON testing
  * Diagrams!
  * Change ParsedObjets to use SparseArray over array?
  * Guarantee shortest arrival time is placed on top
  * Exclude routes with excessive times (> 2 hours)
  * Make sure only one stop is defined as closest for RTD
  * Differentiate between an inactive route and a stop that doesn't report an approaching bus
