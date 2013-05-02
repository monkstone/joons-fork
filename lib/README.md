#Jar Files to include in this folder to build joons library
I don't think the jogl runtime libraries are required just for build, but they will be required if I get round to writing a test task.

##Processing

macosx=core.jar,jogl-all.jar,gluegen-rt.jar,jogl-all-natives-macosx-universal.jar,gluegen-rt-natives-macosx-universal.jar
windows32=core.jar,jogl-all.jar,gluegen-rt.jar,jogl-all-natives-windows-i586.jar,gluegen-rt-natives-windows-i586.jar
windows64=core.jar,jogl-all.jar,gluegen-rt.jar,jogl-all-natives-windows-amd64.jar,gluegen-rt-natives-windows-amd64.jar
linux32=core.jar,jogl-all.jar,gluegen-rt.jar,jogl-all-natives-linux-i586.jar,gluegen-rt-natives-linux-i586.jar
linux64=core.jar,jogl-all.jar,gluegen-rt.jar,jogl-all-natives-linux-amd64.jar,gluegen-rt-natives-linux-amd64.jar

##Janino

all=janino.jar
