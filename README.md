# joons fork #

Read wiki [here](https://github.com/monkstone/joons-fork/wiki/_pages) 


I've been messing with the joons renderer [gnu gpl3 licence](http://www.gnu.org/licenses/gpl-3.0.txt), here is my fork that is modified to work with processing-2.0b8, requires sunflow version 0.73 [MIT licence](http://en.wikipedia.org/wiki/MIT_License) to build (not the release version, sunflow-0.72). My modified sunflow 0.73 is now available as source code [here] (https://github.com/monkstone/sunflow), reqd janino is included [new BSD licence](http://dist.codehaus.org/janino/new_bsd_license.txt). You will need to build the sunflow and joons libraries from source (ant build files are included, in both), so all you need is ant and jdk (you should probably use version 1.6.0_43), but you could import the projects into eclipse or netbeans, or even use jEdit with antfarm plugin. Build sunflow first unless you've already got a binary.
