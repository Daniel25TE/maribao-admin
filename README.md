# Overview

As a software engineer, I am focused on building strong backend development skills. I chose to learn Java because it
remains one of the most in-demand languages in the job market, and I wanted to be competitive in that space. Beyond   
just learning the syntax, I wanted to understand how Java handles object-oriented design, project structure, and how
modern frameworks like Spring Boot make it possible to build production-ready REST APIs. This was my first time       
working with Java and I wanted to make sure I was learning it in a real context, not just through isolated exercises.

The software I built is a REST API backend for a hotel admin panel called Maribao Admin. It gives the hotel owner the
ability to manage guest reservations, upload photos and videos to the website gallery, configure discount dates for
the booking calendar, and view real-time visit statistics — all through a clean set of HTTP endpoints.

I chose this project because it is not a practice project — it is a real business. About six months ago I built the   
frontend of this hotel website for a friend's business, and every semester when I learn something new I come back and
improve it. This project has already helped me land an internship in the past. Now that I am learning Java for the    
first time, rebuilding the backend from Node.js to Java is the perfect opportunity — it keeps me motivated because I
know the work I am doing is impacting a real business, and at the same time it is pushing my backend skills to a level
that will open doors to better internships and job opportunities.


[Software Demo Video](https://youtu.be/alzA1KOXCvs)

# Development Environment

I built this project using IntelliJ IDEA as the primary IDE. I used Spring Initializr (start.spring.io) to generate  
the initial project structure and select dependencies. Maven was used as the build and dependency management tool —   
similar to npm in the JavaScript world. Bruno was used to test all REST API endpoints locally. Git and GitHub were
used for source control and to host the public repository.

The project is written in Java 21 using Spring Boot 3.5.14 as the main framework. Spring Boot handles all the HTTP    
routing, dependency injection, and server startup through an embedded Tomcat server. The following libraries were
included as dependencies:

- Spring Boot Starter Web — enables building REST APIs with annotations like @RestController and @GetMapping
- Spring Boot DevTools — automatically restarts the server when code changes are saved during development
- Lombok — reduces boilerplate code in Java classes
- Spring Boot Starter Test — provides testing utilities included by default with Spring Initializr

# Useful Websites

- [Spring](https://docs.spring.io/spring-boot/index.html)
- [Spring Initializr](https://start.spring.io)
- [JDK 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Baeldung](https://www.baeldung.com/rest-with-spring-series)
- [Collection Framework Overview](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/overview.html)
- [Wikipedia](https://en.wikipedia.org/wiki/Java_(programming_language))

# Future Work

- Connect the backend to AWS DynamoDB to persist data permanently instead of storing it in memory
- Integrate Cloudinary to handle real photo and video uploads directly through the admin panel
- Add admin authentication to protect all endpoints from unauthorized access
- Implement email notifications when a reservation is created or cancelled
- Add Stripe payment integration to handle credit card reservations
- Build the React admin panel frontend that consumes all these REST API endpoints
- Deploy the backend to AWS Lambda and API Gateway so it runs on always-free cloud infrastructure