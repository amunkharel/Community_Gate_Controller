# B-STACK Community Gate Keypad system

The community gate keypad system is a system that allows controlled access to property by protecting the property with a keypad and gate combination system. This repo contains the code for the keypad system that receives input from the physical keypad, authenticates the entered code, and tells the gate system to open when a valid code has been entered.

## B-STACK: The team
The B-STACK team consists of 

**B**randon Stringham \
\-\
**S**hreeman Gautam \
**T**anner Hunt \
**A**mun Kharel \
**C**ody Crane \
**K**rista Conley

We are a team of young and energetic developers that are ready to take on your software engineering problems and create unique solutions by leveraging our unique talents and the latest design methodologies and latest technologies.

## Running the program
To run the program, you can run it in intellij running the main function in 
`KeypadManager`, or you can build a jar with `KeypadManager` as the entrypoint.

In both cases, you will need to create the following empty files in the 
directory where you are running the program from (so for intellij that would 
be the project root if you are using the default run)

```
resources/codes/Admin.txt
resources/codes/General.txt
resources/codes/Public_Service.txt
```

After creating these files, you should be able to run the program successfully

### Note on the display
The ASCII display likely will not clear the screen properly on the windows 
command prompt, but it should work in PowerShell and on Mac and Linux.