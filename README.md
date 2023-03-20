# Simple Object Storage Web Server

A simple web server that provides object storage functionality. The server allows you to create and manage buckets and objects within them.

### Usage
To start the server, run the following command:
`java -jar webserver.jar`

The server listens on port 8090 by default. You can configure the port by setting the HTTP_PORT environment variable.

### API Endpoints
**POST /buckets**

Create a new bucket.
`{
"name": "my-bucket",
"location": "us-east-1"
}`

Response
* 201 Created: Bucket created successfully
* 400 Bad Request: Invalid or missing request body
* 409 Conflict: Bucket already exists

Examples

Using curl:
`$ curl -X POST -H "Content-Type: application/json" -d '{"name": "my-bucket", "location": "us-east-1"}' http://localhost:8090/buckets
`

Using Postman:
* Set the request method to POST.
* Set the URL to http://localhost:8090/buckets.
* Set the request body to {"name": "my-bucket", "location": "us-east-1"}.
* Click the "Send" button.

**GET /buckets/:bucketId**

Get information about a bucket.

Response

{`
"bucketId": "123",
"bucketName": "my-bucket",
"bucketLocation": "us-east-1"
}`

Examples

Using curl:
`$ curl http://localhost:8090/buckets/123`

Using Postman:

* Set the request method to GET.
* Set the URL to http://localhost:8090/buckets/123.
* Click the "Send" button.

**POST /buckets/:bucketId/objects**

Create a new object in the specified bucket.

Request Body
`
{
"name": "my-object",
"id": "abc",
"content": "Hello, world!"
}`

Response

* 201 Created: Object created successfully
* 400 Bad Request: Invalid or missing request body
* 404 Not Found: Bucket not found
* 409 Conflict: Object already exists

Examples

Using curl:
`$ curl -X POST -H "Content-Type: application/json" -d '{"name": "my-object", "id": "abc", "content": "Hello, world!"}' http://localhost:8090/buckets/123/objects
`

Using Postman:

* Set the request method to POST.
* Set the URL to http://localhost:8090/buckets/123/objects.
* Set the request body to {"name": "my-object", "id": "abc", "content": "Hello, world!"}.
* Click the "Send" button.

**GET /buckets/:bucketId/objects/:objectId**

Get the content of an object in the specified bucket.

Response 

`Hello, world!`

Examples

Using curl:

`$ curl http://localhost:8090/buckets/123/objects/abc
`

Using Postman:

* Set the request method to GET.
* Set the URL to http://localhost:8090/buckets/123/objects/abc.
* Click the "Send" button





**WebServer**: This is the main class that starts the HTTP server and defines the routing rules for different API endpoints. It creates a Vert.x HttpServer and a Router to define the API endpoints, and creates instances of the different handlers to handle the requests. It also maintains a map of Bucket objects to store the data.

**Bucket**: This class represents a bucket that can hold a list of objects. It has a unique id, a name, a location, and a list of Object instances that it contains. It also has methods to add an object to the list and to get the list of objects.

**Object**: This class represents an object that can be stored in a bucket. It has a unique id, a name, and a content (the data associated with the object).

**CreateBucketHandler**: This class is responsible for handling HTTP POST requests to create a new bucket. It extracts the bucket information from the request body, creates a new Bucket instance, and adds it to the map of buckets.

**GetBucketHandler**: This class is responsible for handling HTTP GET requests to retrieve information about a bucket. It extracts the bucket ID from the request path, retrieves the corresponding Bucket instance from the map, and returns its information in the response body.

**CreateObjectHandler**: This class is responsible for handling HTTP POST requests to add a new object to a bucket. It extracts the object information from the request body, checks if the object already exists in the bucket, creates a new Object instance, and adds it to the Bucket object's list of objects.

**GetObjectHandler**: This class is responsible for handling HTTP GET requests to retrieve an object from a bucket. It extracts the bucket ID and object ID from the request path, retrieves the corresponding Bucket instance from the map, finds the object in its list of objects, and returns its information in the response body.

Each of these classes implements the io.vertx.core.Handler interface to define the request handling logic for the respective endpoints.





