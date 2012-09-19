The purpose of the library is to implement android client-server interaction, suggested by Google.

Major features:
* Requests are executed in Service in separate thread, receivers are notified about results
* Handling responses is loosely coupled with requests
* Sending List<Request> is supported
* Http MultipartEntity is supported
* Partial wake lock is aquired for service
* Logback-android logging icluded
* cwac-wakeful is included


Change Log
==========

Version 9
* Initial GitHub commit

 
http://www.google.com/events/io/2010/sessions/developing-RESTful-android-apps.html
http://tony19.github.com/logback-android/
https://github.com/commonsguy/cwac-wakeful