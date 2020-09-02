# FleetMan-Android

Android companion app for a fleet management system implemented [here](https://github.com/Aayushjn/FleetMan).

Import the app in Android Studio and build it. The `BASE_URL` in [Constants.kt](./app/src/main/java/com/aayush/fleetmanager/util/common/Constants.kt) will need to be modified to reflect the IP address hosting the API server. Along with that, also replace the IP address in [network_security_config.xml](./app/src/main/res/xml/network_security_config.xml) with the API server's URL.
