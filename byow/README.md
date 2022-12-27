# byow

In my junior year of high school, a friend majoring in CS at UC Berkeley challenged me to do the final project of Berkeley's CS 61B (Data Structures) course.  Since I had the time and I thought it would be a fun side project, I did.

The spec was on https://datastructur.es, although I can't guarantee that the page still exists.

I was unable to follow the spec completely because I was unable to access their starting code and their libraries, so I programmed using Java graphics library and Java Swing instead of their graphics library.
Despite the differences, the idea is mostly the same: generate a pseudo-random network of rooms connected by passages, such that when the same seed is used, the world generated will be exactly the same.

I was happy with the end product.

**To run:**
Compile the .java files in the src folder.  Driver.java contains main().

**Usage:**
The program prompts the seed from the user, which is a 16-digit number.  If a 16-digit number is not entered, it will be parsed into a 16-digit number.  The program will then generate a pseudo-random map.
