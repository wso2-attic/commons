#!/usr/bin/env ruby

require 'zip/zip'

# find jars that needs patching
def find_jars_to_patch()
  all_jars = Dir.glob(File.join("**", "*.jar"))
  jars_to_patch = Array.new
  finalized_jars = Array.new

  File.open("finalized-jarlist.txt").each_line do |f|
    # get the package name

    # +1 to rindex is to get rid of the begining /
    f = f.slice!(f.rindex('/')+1..f.length)
    f = f.slice(0...f.rindex('-'))
    finalized_jars.push(f)
  end

  all_jars.each do |jar|
    found = 0

    finalized_jars.each do |f|
      if not jar.match(f).nil? then
        found = 1
        break
      end
    end

    jars_to_patch.push(jar) if found == 1
  end
  return jars_to_patch
end

# do the emma instrumenting thing
def instrument_jars(jars)
  jars.each do |jar|
    system("$JAVA_HOME/jre/bin/java -cp emma.jar emma instr -m overwrite -cp #{jar}") if jar.match('webapps')
  end
end

# add the dymamic manifest header thing
def patch_jars(jars)
  jars.each do |jar|
    tmp = File.new("MANIFEST.MF", "w")
    Zip::ZipFile.open(jar) do |zip|
      cont = zip.read("META-INF/MANIFEST.MF").rstrip
      tmp.puts(cont)
      if cont.match('DynamicImport-Package').nil? then
        tmp.puts("DynamicImport-Package: com.vladium.*\n")
      end

      tmp.close
      zip.replace("META-INF/MANIFEST.MF", "MANIFEST.MF")
    end
  end
end

# start the server instance
def start_instance()
  system("sh wso2wsas-SNAPSHOT/bin/wso2server.sh")
end

# run the full test framework
# def run_testframework()
# end

# code coverage report
def generate_report()
  system("java -cp emma.jar emma report -r html -in coverage.em,coverage.ec")
end

jar_list = find_jars_to_patch()
instrument_jars(jar_list)
patch_jars(jar_list)
start_instance()
# run_testframework()
generate_report()

