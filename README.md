# Running the project
1) Amazon's AWS S3 is needed to store images and start the project.

   A native installation of MinIO can be used for that: https://min.io/docs/minio/windows/index.html

2) Set these environment variables:
   * IMAGE_HOST= S3 host address. If running S3 locally, use "http://127.0.0.1:9000" (without quotaion marks);
   * AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY - S3 access keys, they're needed to be created;
   * AUTHENTICATION_SECRET_KEY=qcG4erZzPglTze3E3GYBIqI9/V6arYaJI6oHWsS4SE0r+hVs7ZXKoBE+69Ojp0/N
   * Set profile as dev or test. This can be done in intellij, with Active profiles options, or as an environment variable: spring.profiles.active=test
  
# Endpoints:
1) Get paginated cities with flags of their countries:
   * /cities (by default - 25 cities per page) (GET)
   * /cities?per_page=50&page=2 (GET)
2) Get all countries, paginated:
   * /countries (25 countries per page) (GET)
   * /countries?per_page=10&page=2 (GET)
3) Get all cities by country name
   * /cities?per_page=-1&country_name=Russia (GET)
4) Search by city name:
    * /cities?per_page=-1&city_name=St. Petersburg (GET)
5) Edit the city:
    * /cities/9?name=new city name (PATCH) - update name
    * /countries/1/upload-flag (POST) - change country's flag
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

# Decision made:
1) Why using S3?

   The requirement is to make an enterprise-grade application. That implies fault tolerance.

   If the images were simply sored in-memory, a server failure could mean the permanent loss of the images, or at least their temporary inaccessibility.
   With AWS S3, we can have multiple servers running at the same time, each accessing the S3 storage, which is much more fault tolerant.

2) Editing the city

   The updated requirements state that instead of a city's logo we use the country's flag, so it made more sense to separate editing the city into 2 endpoints:
   editing the city data, and editing the country flag.

3) Filling the application with data.

   In my opinion, it doesn't really make sense to fill the application with scripts. This would mean that every time a country needs to be changed, added or removed,
   the application's code needs to be changed, a then the whole CI/CD and release process.

   For maintainability and extendability, I decided that this app would require endpoints for adding and removing countries and cities, and also renaming cities.
   I did not implement these endpoints, because it's outside of the scale of the task requirements.

   The images of flags for each country are also uploaded to the S3 un application startup. Also, there are migrations to add some cities, countries and users data.
   But, again, it is not my intention that this is the actual mechanism for the data to be added to the app. The added data is only for testing the application and checking the task completion.

5) Why all cities, search by country or city name are all made in a single endpoint
  * This endpoint structure makes sense, and is easy to understand
  * Also, this realization allows much more flexibility with the endpoint.

    Besides the required options, it's also possible to:
    * http://localhost:8080/cities?per_page=-1 - get all cities in one page
    * http://localhost:8080/cities?per_page=-1&city_name=St. Petersburg&country_name=Russia - search by both country and city names
