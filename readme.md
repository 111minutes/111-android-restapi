111 REST api
============

The purpose of the library is to implement [android client-server interaction, suggested by Google][1].

Major features:
 * Requests are executed in Service in separate thread, receivers are notified about results
 * Handling responses is loosely coupled with requests
 * Sending List<Request> is supported
 * Http MultipartEntity is supported
 * Partial wake lock is aquired for service
 * [cwac-wakeful][2] is included


Change Log
==========

Version 15
---------
 * Logback removed

Version 10-14.
---------
 * See commit log...

Version 9
---------
 * Initial GitHub commit

Roadmap
=======
 * Add javadocs


Developed By
============

* 111 minutes android team - http://111minutes.com

 [1]: http://www.google.com/events/io/2010/sessions/developing-RESTful-android-apps.html
 [2]: https://github.com/commonsguy/cwac-wakeful