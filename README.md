# OS Challenge  - Groupe 27
## Part 1
This is our OS Challenge project from the course [02335](https://kurser.dtu.dk/course/02335) at [DTU](https://www.dtu.dk/uddannelse/diplomingenioer/uddannelsesretninger/softwareteknologi).

##Our team consists of 3 students:
1. S215713 - Mohamad Anwar Meri
2. S224298 - Safi Meissam
3. S193401 - Hossien Heidari

## What is the goal of this project? 
1. We will create a server that accepts requests for reverse hashing.
2. Our server will accept requests messages that include a hash generated by the SHA256 algorithm.
3. Our server must be able to handle multiple requests and respond to all of them as fast as possible
with the correct answer.


## How to run the server (Through command prompt)
1. Firstly, install virtualBox and make sure the server is running
2. Then install [os-challenge-common](https://github.com/dtu-ese/os-challenge-common) and save it in your pc 
3. Open the command terminal and find the "os-challenge-common\x86_64" by using cd and the path for exempel
   "cd "C:\Users\morai\os-challenge-common\x86_64"
4. Then use "vagrant ssh server"
5. Now it's look like this "vagrant@server:~$"
6. use "ls" to see what you have inside
7. "cd os-challenge-common/" or to run our server "cd server/"
8. "gcc -o server_file_name server_file_name.c -lcrypto"
9. "sudo ./server_file_name" to run the server

## How to run the client (Through command prompt)
1. Firstly, install virtualBox and make sure the client is running
2. Open the command terminal and find the "os-challenge-common\x86_64" by using cd and the path for exempel
   "cd "C:\Users\morai\os-challenge-common\x86_64"
3. Then use "vagrant ssh client"
4. Now it's look like this "vagrant@client:~$"
5. use "ls" to see what you have inside
6. "cd os-challenge-common/"
7. run-client.sh


