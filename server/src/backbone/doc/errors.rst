Errors
======

Errors are reported using with http status codes, check documentation for possible error codes for each end-point.

=============
Common errors
=============

If an endpoint has an **Example post body** in it's documentation then
it could also raise one of the following errors:

* 400, Invalid input - Not all arguments were included in the post body
* 400, Method body must be a valid JSON - post body was not a valid JSON
