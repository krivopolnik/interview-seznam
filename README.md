**How to Run the "Runner" Program**

The "Runner" program allows you to process data in a text format using three available processors: "top," "contentType," and "grep." Below is a guide on how to run the program with the "grep" command using additional arguments based on the provided code.

**Step 1: Building the Program**

Before running the program, ensure that it has been successfully built. To build the project, use the Maven build system. Navigate to the project's root directory and execute the following command:

```shell
mvn clean package
```

This will create a JAR file for the program in the "target" directory.

**Step 2: Running the Program with the "grep" Command**

After successfully building the program, you can run it with the "grep" command and specify the arguments. Here is an example command:

```shell
java -cp target/interview-1.0-SNAPSHOT.jar cz.seznam.fulltext.robot.Runner grep "search_string" 10000.txt
```

- `java`: The command to run Java applications.
- `-cp`: A parameter for specifying the classpath, where we provide the path to the JAR file of the compiled program.
- `cz.seznam.fulltext.robot.Runner`: The main class of the "Runner" program.
- `grep`: The command name, in this case, "grep."
- `"search_string"`: The string you want to search for in the "10000.txt" file.
- `10000.txt`: The name of the input file in which the search will be performed.

**Note:**
- Make sure that the JAR file and the input file are located in the same directory from which you are running the command.
- The "grep" command allows you to search for a string in the input file and display the corresponding results.
- The search will be performed in all lines of the "10000.txt" input file, and the results will be displayed.
- In this case, the "grep" command will search for the string "search_string" in the "10000.txt" file.
