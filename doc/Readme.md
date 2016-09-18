# How to

## Create new docker image

In the main project folder run

`docker image -t repository/imagename .`

or

`docker image -t repository/imagename:tag .`

With this command we can create new image or a new version of existing one. Without the tag parameter
 the image will be tagged as "latest".

## Push your image to repository

## Create and run container

`docker run -d -p 127.0.0.1:8080:8080 
-v /home/greg/d/www:/usr/cricket/www 
-v /home/greg/d/data:/usr/cricket/data 
-v /home/greg/d/config:/usr/cricket/config 
--name test
gskorupa/basicservice:1.0.0`

where `gskorupa/basicservice:1.0.0` is your repository/imagename:tag
 
The image has 3 volumes to which you can mount your filesystem folders, as in above example. 
By mounting filesystem folders to the container volumes we can overwrite original Cricket files when needed. We can use it to 
Mounting volumes is optional. We can mount all, none or only selected volume.