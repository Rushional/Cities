# Running the project
1) Amazon's AWS S3 is used to store images in this project. Locally, this is done in Docker, using MinIO - an AWS compatible, free-to-use storage. 
   So, before starting the project itself, the docker-compose for the MinIO container should be run first, found in main project directory.
2) Then, when MinIO is successfully started, open 127.0.0.1:9090 to access the MinIO UI. Then go to Access Keys, and create a new access key.
   Save the generated public and secret keys, they'll be needed for the next step, and the private key won't be shown again after it's generated.
3) Set these environment variables:
   * IMAGE_HOST= S3 host address. If running S3 locally, use "http://127.0.0.1:9000" (without quotaion marks);
   * AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY - S3 access keys that were generated and saved on the previous step;
   * AUTHENTICATION_SECRET_KEY=qcG4erZzPglTze3E3GYBIqI9/V6arYaJI6oHWsS4SE0r+hVs7ZXKoBE+69Ojp0/N
   * Set profile as dev or test. This can be done in intellij, with Active profiles options, or as an environment variable: spring.profiles.active=test
  
# Endpoints:
1) Get paginated cities with flags of their countries:
   * /cities (by default - 25 cities per page) (GET)
   * /cities?per_page=50&page=2 (GET)
2) Get the list of unique city names:
   * /cities/unique (GET)
3) Get all cities by country name
   * /cities?per_page=-1&country_name=Russia (GET)
4) Search by city name:
    * /cities?per_page=-1&city_name=St. Petersburg (GET)
5) Edit the city:
    * /cities/9?name=new city name (PATCH) - update name
    * /cities/9/upload-logo (POST) - change city's logo
    * This can only be done by a user with the EDITOR role, with JWT Bearer authentication, using HS256
6) Authentication and refresh token:
    * /authenticate
      With JSON body:

      {
        "username": "editor_user",
        "password": "!Password1"
      }

    * /refresh-token
      With JSON body:

      {
        "refresh_token": "string"
      }

      For application testing, 2 customers were created: editor_user and customer user. They both have the password !Password1

# Decisions made:
1) Why using S3?

   The requirement is to make an enterprise-grade application. That implies fault tolerance.

   If the images were simply sored in-memory, a server failure could mean the permanent loss of the images, or at least their temporary inaccessibility.
   With AWS S3, we can have multiple servers running at the same time, each accessing the S3 storage, which is much more fault tolerant.

2) Filling the application with data.

   The images of flags for some of the cities are uploaded to the S3 on application startup. Also, there are migrations to add some cities, countries and users data.

   That said, this is meant as test data to quickly show that the application is indeed working. For maintainability and extendability, I decided that this app would require endpoints for adding and removing countries and cities. I did not implement these endpoints, because it's outside of the scale of the task requirements.

3) Why endpoints to get all cities, search by country/city name are all made in a single endpoint
  * This endpoint structure makes sense, and is easy to understand
  * Also, this implementation allows much more flexibility with the endpoint.

    Besides the required options, it's also possible to:
    * http://localhost:8080/cities?per_page=-1 - get all cities in one page
    * http://localhost:8080/cities?per_page=-1&city_name=St. Petersburg&country_name=Russia - search by both country and city names
