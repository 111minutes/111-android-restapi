111 REST api
============

The purpose of the library is to implement [android client-server interaction, suggested by Google][1].

Major features:
 * Requests are executed in Service in separate thread, receivers are notified about results
 * Handling responses is loosely coupled with requests
 * Sending List<Request> is supported
 * Http MultipartEntity is supported
 * Partial wake lock is aquired for service
 * [Logback-android][2] logging icluded
 * [cwac-wakeful][3] is included


Change Log
==========

Version 9
---------
 * Initial GitHub commit

Roadmap
=======
 * Add javadocs
 * Cleanup `DefaultRequestComposer`
 * Add ability to set Content type, Content encoding, gzip

Developed By
============

* 111 minutes android team - http://111minutes.com

 [1]: http://www.google.com/events/io/2010/sessions/developing-RESTful-android-apps.html
 [2]: http://tony19.github.com/logback-android/
 [3]: https://github.com/commonsguy/cwac-wakeful