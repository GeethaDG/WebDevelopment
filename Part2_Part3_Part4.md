
# Part 2: JavaScript 

## Question 1
What is the output of the following JavaScript code snippet? 
Promise.resolve(1) 
 .then((x) => x + 1) 
 .then((x) => { throw new Error('My Error') }) 
 .catch(() => 1) 
 .then((x) => x + 1) 
 .then((x) => console.log(x)) 


The output of the given code snippet is 2. 

## Question 2
When using third-party (npm) packages, how do you keep them up-to-date? Imagine you want to update a package from v1.2.4 to v2.0.5. What do you need to consider first? 
- By using a single JSON file(packages) for all the dependencies. And then periodically checking 'npm outdated' this give a clear picture of the status of the packages.
- Once we have checked and see we have some outdated packages we run 'npm update' and then 'npm oudated' to see what is the status of the packages.
- But incase of major updates this won't work and we see a ^ against the package. If we still want to update we use the command 'npx npm-update-all'(some packages are not updated to ensure the applications don't break)

### Question 3
What is the main difference between AngularJS and NodeJS? 
- Angular JS is a great option for building long size projects where Node JS is ideal for developing small size projects.
- Angular JS don’t have to be added installed separately, one must add it like any other JavaScript file so that it be used in applications whereas Node JS must be installed on the machine.
- Angular JS is a front-end framework and can be used with any backend programming language like PHP, Java etc. whereas Node JS is simply a server-side language, in a web application like context it acts as a Java on the server side.
- Angular JS is created entirely using JavaScript whereas Node JS is written in JavaScript, C++ and C languages.
- Angular JS support real-time applications like instant messaging or chat apps whereas Node JS is best suited for real-time collaborative drawing or editing applications like Google Docs.
- Angular JS is an open source framework for the client side of the application whereas Node JS is a cross-platform runtime system and environment for applications written in JavaScript.
- Angular JS runs on the client browser whereas Node JS runs on the server side.
- Angular JS is compatible with Chrome, Firefox, Internet Explorer, Safari, Opera and mobile-based browsers such as an Android browser, Chrome for Mobile and so on whereas Node JS is available for Windows, Linux, Sun OS, and Mac OS.
- Angular JS is a web application framework whereas a number of frameworks are based on Node JS like Express JS, Sails JS etc.
- Angular JS is best suited for interactive single page web applications whereas Node JS is used to build fast and server-based web applications.

- Note: Since I do not know in depth the details, I used this [link](https://www.educba.com/angular-js-vs-node-js/) to understand the differences. 


# Part 3: Server administration 
## Question 1
Please explain how file permissions work in Linux. What different kinds of permissions are there?  How are they assigned to a file or directory? 
- File permissions are extra layer of security to eliminate any vulnerabilities in the system. 
- File Permissions : 
  - Permission Groups:  Each file and directory has three user based permission groups:
        - owner(u) – The Owner permissions apply only the owner of the file or directory, they will not impact the actions of other users.
        - group(g) – The Group permissions apply only to the group that has been assigned to the file or directory, they will not effect the actions of other users.
        - all users(a) – The All Users permissions apply to all other users on the system, this is the permission group that you want to watch the most.
   - Permission Types: 
        - read(r) – The Read permission refers to a user’s capability to read the contents of the file.
        - write(w) – The Write permissions refer to a user’s capability to write or modify a file or directory.
        - execute(x) – The Execute permission affects a user’s capability to execute a file or view the contents of a directory.
   - Example : drwxr-xr--, here d represents that we are hadling a directors, here the user/owner has all the permissions, the group has only read and execute permission and all other users have only read permission.
   - We have the permission varying from 0(--- or no permission) to 7(rwx or all permissions), we can change this using the command 'chmod permissions filename'. One example for permission is '777' which means all the groups can read, modify and execute.
   - Based on the sensitivity and the application, these have to be considered and accordingly assigned. 
   
## Question 2 
How can you find out if a web server is running on a Linux system?
- We can check if a web server is running on Linux by using curl(client for URL) command.
- If the server is 'Apache' or 'Tomcat' implies it is running on Linux system.

## Question 3 
Name some HTTP request methods and briefly explain their meaning. 
- The internet boasts a vast array of resources hosted on different servers. For us to access these resources, browser needs to be able to send a request to the servers and display the resources. HTTP (Hypertext Transfer Protocol), is the underlying format that is used to structure request and responses for effective communication between a client and a server. The message that is sent by a client to a server is what is known as an HTTP request. When these requests are being sent, clients can use various methods. Therefore, HTTP request methods are the assets that indicate the specific desired action to be performed on a given resource. Each method implements a distinct semantic, but there are some standard features shared by the various HTTP request methods.
  - GET : The GET method requests a representation of the specified resource. Requests using GET should only retrieve data
  - HEAD : The HEAD method asks for a response identical to that of a GET request, but without the response body.
  - POST : The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server.
  - PUT : The PUT method replaces all current representations of the target resource with the request payload.
  - DELETE : The DELETE method deletes the specified resource.
  - CONNECT : The CONNECT method establishes a tunnel to the server identified by the target resource.
  - OPTIONS : The OPTIONS method is used to describe the communication options for the target resource.
  - TRACE : The TRACE method performs a message loop-back test along the path to the target resource.
  - PATCH : The PATCH method is used to apply partial modifications to a resource.

## Question 4 
In HTTPS, how is the identity of a server authenticated? 
The server is authenticated as follows: 
- SSL server authentication lets a client application confirm the identity of the server application.
- The client application, through SSL, uses standard public-key cryptography to verify that the server’s certificate and public key are valid. 
- The client application also verifies that the certificate has been signed by a trusted certificate authority that is known to the client application. 
- The client and the server then use the negotiated session keys and begin encrypted communications.

# Part 4: Git 

## Question 1
In a git repository, what is a branch? Why are they used? 
- A branch represents an independent line of development. 
- Branches serve as an abstraction for the edit/stage/commit process. They are usually used to keep the stable version of the code protected and also using branches helps the team to review the changes of a line of development.






