# new-gen-integration
MongoDB setup :-
1.	Download mongoDB
      https://www.mongodb.com/download-center/community

2.	Unzip the file and move to /usr/local
sudo mv mongodb-osx-x86_64-4.0.5 /usr/local/mongodb

3.	It will ask for password, give your machine password and enter.

4.	Go to the mongodb folder
              cd /usr/local/mongodb

5.	By default, mongodb write or store data into a folder called /data/db so we need to create this folder
sudo mkdir -p /data/db
 
6.	Manually change the permission of this folder
cd /data/db
sudo chown mohitkumar.gupta /data/db

Note - mohitkumar. gupta is my user name so just change it by your user name.

7.	Go to the home directory
type cd and press enter you will move to your home directory

8.	Open bash_profile if exist otherwise create new file.
open .bash_profile

9.	Add mongodb path to bash_profile.
export MONGO_PATH=/usr/local/mongodb
Export PATH=$PATH:$MONGO_PATH/bin

10.	 Save .bash_profile and restart the terminal.

11.	 Setup is done. Now start mongo deamon
              cd /usr/local/mongodb/bin
              . /mongod

12.	 Open new terminal and start mongo client to connect mongodb
. /mongo
