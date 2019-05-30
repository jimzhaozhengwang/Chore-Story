Headers
=======

There are 2 possibly **required** headers in the API.

If an endpoint documentation has **Login required** in it's documentation
then it needs the following header:

.. code-block:: none

    Authorization: <api_key>

where ``api_key`` can be obtained from the ``/login`` endpoint.

If an endpoint has an **Example post body** in it's documentation then
it needs the following header:

.. code-block:: none

    Content-Type: application/json
