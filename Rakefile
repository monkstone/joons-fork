require 'ant'

PROJECT_NAME ='joons'
MAIN_SRC_DIR = 'src'
RUNTIME_LIB_DIR = 'lib'
BUILDTIME_LIB_DIR = 'lib'
BUILD_DIR = 'build'
DIST_DIR = 'joons/library'

task :default => [:clean, :make_jars]

task :clean do
  ant.delete :dir => BUILD_DIR
  puts
end

task :setup do
  ant.path :id => 'classpath' do
    fileset :dir => RUNTIME_LIB_DIR
    fileset :dir => BUILDTIME_LIB_DIR
  end
end

task :make_jars => :setup do
  make_jar MAIN_SRC_DIR, "#{PROJECT_NAME}.jar"
end

def make_jar(source_folder, jar_file_name) 
    ant.mkdir :dir => BUILD_DIR
    ant.mkdir :dir => DIST_DIR
    ant.javac :srcdir => source_folder, :destdir => BUILD_DIR, :classpathref => 'classpath',
              :source => "1.6", :target => "1.6", :debug => "yes", :includeantruntime => "no"
    ant.jar :jarfile => "#{DIST_DIR}/#{jar_file_name}", :basedir => BUILD_DIR
    ant.delete :dir => BUILD_DIR
    puts
end

