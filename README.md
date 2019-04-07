# Setup envirenment (Prerequisites)

1- Install the chocolatey
-
#### Chocolatey is a package manager for Windows. Let's use it!

- Open your `cmd.exe` (Run as Administrator)
- Go to the root of your C: drive `C:\>`
- Copy `@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"` and run it in the command promt to install Chocolatey onto your system.
- After the installation is complete, restart your `cmd.exe` and type `choco -v` to verify the installation completed without a problem.

2- Install Cmder
-
#### Using cmd.exe is not an alternative, you first thing to do is a download and install a good Console Emulator.

- Open your `cmd.exe` (Run as Administrator) (for the last time)
- Run: `C:\> choco install cmdermini`
- Find the executable `Cmder.exe` file on your system and pin it to your taskbar for easy access.

3- Install Java JDK 8
-
#### Now, we are ready to use Cmder to install all kind of applications from a command line.

- Open your command line (Cmder)
- Run: C:\> `choco install jdk8`
- Restart your console and run `javac -version` to verify installation.

4- Install Maven
-
#### Now, we are going to install Maven (Project management tool)

- Open your command line (Cmder)
- Run: C:\> `Y`
- Restart your console and run `mvn -v` to verify installation.

___

# How to install FirmadanTeklif

1- Clone the project on your local pc
-
#### First thing first. We will clone the project from github and save it on our local pc.

- Copy the repository url [from here](https://github.com/yonetmen/firmadanteklif-application.git)
- On your local pc, create a folder in a location of your choise. e.g. `c:\my_projects\firmadanteklif`
- Run this command to clone the repository on your local machine.
`C:\>my_projects\firmadanteklif>git clone https://github.com/yonetmen/firmadanteklif-application.git`

2- Importing the project into Intellij
- 
#### After cloning the project, we will import the project into Intellij IDEA. 

- `File` > `New` > `Project from existing code...`
- Locate and chose the root folder of your project (**firmadanteklif**)
- Chose `import project from external model` and select `maven` and click `Next`
- On the next meny, check the `import maven projects automatically` box  and click `Next`
- In the remaining menues, just click `Next` and finally click `Finish` to complete import process
- Intellij will resolve all the dependencies for the project at this point, just wait Intellij to complete.

3- Install MySQL
-
#### Now, when our development envirenment is ready to write some code, we need to setup MySQL server as well before we can run the application successfully.

- Click [here](https://dev.mysql.com/downloads/file/?id=484920) and download the **mysql-installer-community-8.0.15.0.msi**
- When you click on the link above, it will take you to mysql download page. At this point follow the instructions on this [youtube clip](https://youtu.be/u96rVINbAUI?t=80) from the current time. But read the next step before you doing that.
- While you are watching it, when you come to [this point](https://youtu.be/u96rVINbAUI?t=150) you need to select one more component before continue to follow the instructions. You need to click on `MySQL Connectors`, expend the submenu of `Connector/J` and select the available version of that as well. Read next step as well.
- When you come to [this point](https://youtu.be/u96rVINbAUI?t=217) to choose a username and password, choose `admin` for both username and password since our application is configured to use these credentials to connect to the database.
- When you complete installation and while your MySQL Workbench is open, click your `admin` connection to open up mysql console and type `CREATE SCHEMA firmadanteklif` and click `CTRL` + `Enter` in order to run the query & create the application database.

4- Run the application
-
#### At this point everything should be ready to start our application. In order to that go to Intellij and click on the little green play button (right under the top navigation menues) and watch the console . You should see something like this at the end of console output to confirm that everything worked fine.

`Tomcat started on port(s): 8080 (http) with context path ''`

`Started Application in 5.941 seconds (JVM running for 6.968)`

Now you can open your browser and navigate to `http://localhost:8080` to test the application.
