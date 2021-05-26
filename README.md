# BoundedBuffer
Implementation of the Bounded Buffer/Producer-Consumer problem. 
There are 2 kinds of producers, one type reads info from a website and puts it into the shared queue, 
while the other type of producer reads info from a mongo database and puts it into the shared queue.

To run the code successfully kindly follow these steps:
1) Create/Install a docker image of mongodb. Also install bazel.

2) Create a docker container to connect to and run mongodb in it using this command: -`docker run --name myMongo -d -p 27017:27017 --name myMongo mongo`

Note: myMongo can be replaced with any name of your choice

3) Run MongoDocker.java -- To do this, run the following commands: `bazel build //:MongoDocker`

`bazel-bin/MongoDocker`

Step 3) ensures that a databse has been created and a collection has been added to it.

4) Run PC.java (similar to step 3)

5) To stop the docker container from running, use the following command: sudo docker stop myMongo. 

In case you get the following error: Cannot kill container. unknown error after kill: runc did not terminate sucessfully. signaling init process caused "permission denied"

Run the following commands sequentially: 
`sudo aa-remove-unknown`

`docker container kill myMongo` 

6) To rerun the container(if required), use the following command: docker restart myMongo
