Errors
======

Errors are not reported with http status codes, except when a **Login required**
endpoint is accessed by someone not logged in, then a ``404`` is returned for
security reasons.

The rest of the error are reported using the response JSON body.

An example error from endpoint ``GET /api/child/99``:

.. code-block:: json

    {
      "data": null,
      "error": {
        "detail": "Child account not found",
        "status": 404
      }
    }

The easiest way to check for error would be to check whether the response body has ``error`` as a key in it.

=============
Common errors
=============

If an endpoint has an **Example post body** in it's documentation then
it could also raise one of the following errors:
* 400, Invalid input - Not all arguments were included in the post body
* 400, Method body must be a valid JSON - post body was not a valid JSON
