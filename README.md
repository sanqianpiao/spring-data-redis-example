##About
This project was forked & modified to test redis concurrency and its efficiency.

In case you don't have redis, I recommend to you install one using docker.
## Install docker

[Mac](https://docs.docker.com/docker-for-mac/install/#install-and-run-docker-for-mac)

[Windows 10](https://docs.docker.com/docker-for-windows/install/)

## Install & Start redis-docker container
Pull redis image

`$ docker pull redis`

Run redis container

`$ docker run -d --name redis -p 6379:6379 redis`

Check running containers

`$ docker ps`


