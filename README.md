# LJA
Lama Java Annotation


# General philosophy
As with most of my projects, this one was a cool idea that I wanted to code for the sake of coding, not for practical use. While my projects don't serve a real purpose on their own, I intend to use them within other projects. I often avoid mainstream solutions because they're too convenient for my taste. If you've found this repository and wish to use it, proceed at your own risk

Also, don't expect the development to follow any strict regimen, as my energy for extra coding projects is very sporadic. Specifically for Java.

For java things I usually create "plug-and-play" solutions because I find setting up a remote repository annoying (though I may do so in the future to learn). As a result, you might find copied code throughout the project, but I only include JAR files once I'm reasonably sure they're stable.

# Project philosophy
I wanted to create some java annotation to understand how they work. Also, I don't want to use a build system just to make it a bit more interesting and avoid any depencies (Because you can't use compile annotation without exporting them as JAR)
It turns out the real way lombok works it very complicated (it play around with the AST directly) and that the Java Annotation system doesn't really suit what I wanted to do, so I built a system to modify files as freely as I wanted. but of course it
require some work. this doesn't mean I won't create any normal annotation, but it may be delayed to when I would implement a framework for dependency injection



# How To Use

Of course Java would complain about annotation not defined as he wished so to define you have to put it in a comment
```java
//@annotation @secondannotation
/* @thirdAnnotattion
*/
```

## Raw Java
It takes the code root folder and the target folder. it will create a folder processed/ int the target root with all the code modified with the annotations
```bash
java -jar LamaProcessor.jar ../src/test ../target
```

Then just compile and use the code inside target/processed

EX:
```bash
java -jar LamaProcessor.jar ../src/test ../target
find ../target/processed -name "*.java" -print | xargs javac -d ../target/processed/compiled
java -cp ../target/processed/compiled test.TestEntryPoint
```
