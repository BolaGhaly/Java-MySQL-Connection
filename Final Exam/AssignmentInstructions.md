Build a music recommendation service.

You are given a template database with genres, regions, and bands. See attached file.

Tasks:
  1. (9 points) Create db user called “api” with limited access of read only of initially given tables in the template, and read/write/update permissions for all additional tables created for this project in the next steps.
  2. (9 points) Create a "User" relation. User needs an ID, name, and home country.
  3. (9 points) Create a "Favorites" relation. Favorites needs to reference user, and bands.
  4. (9 points) Create a query to determine which sub_genres come from which regions.
  5. (9 points) Create a query to determine what other bands, not currently in their favorites, are of the same sub_genres as those which are.
  6. (9 points) Create a query to determine what other bands, not currently in their favorites, are of the same genres as those which are.
  7. (9 points) Create a query which finds other users who have the same band in their favorites, and list their other favorite bands.
  8. (9 points) Create a query to list other countries, excluding the user’s home country, where they could travel to where they could hear the same genres as the bands in their favorites.
  9. (9 points) Add appropriate indexing to all tables and optimize all queries.
  10. (9 points) Write an application in the back end language of your choice to access the database using the created user in task # 1 with a function to run each query in tasks 4-8 with the user ID passed as a parameter.
  11. (10 points) Create functions to insert users and insert & delete favorites. Insert at least 3 users and 4 favorites for each user. You may use static data in the program to call these functions programmatically so long as SQL calls are still properly parameterized. Note that a user may only add existing bands to favorites list and function should return an error if they add a band not in the database. (You may add your favorite bands to the template provided, just remember to add matching genre/sub_genre and region/country).

Extra Credit: Create a User with "Paul Pena" and "The Hu" in their favorites list. Create 2 other Users, each with only one of those in their favorites, (1 each) as well as both users with "Tengger Cavalry", "Sade", and "Battuvshin" in their favorites. Modify query #7 so that Tengger Cavalry will be ranked first in the results order. Hint: consider country, genre, and/or sub_genre.

Submit both the updated SQL file and program source code. These should be concise files in proper format (.sql, .txt, .py, .cpp, etc) and not screenshots, executables, build files or anything besides your source code - preferably a single file for each. Don't forget to comment your source, specifying which task number a query answers, and to exercise proper db security.
