
import os

allfiles = os.listdir(".")

images = [f for f in allfiles if f.startswith("bind") and f.endswith(".jpg") ]
images.sort()

for jpgfile in images:
    basename = jpgfile.split(".")[0]
    command1 = "tesseract " + jpgfile +  " " + basename + "_dan -l dan_frak"
    command2 = "tesseract " + jpgfile +  " " + basename + "_isl -l isl" 

    print(command1)
    os.system(command1)
    #print(command2)
    #os.system(command2)
