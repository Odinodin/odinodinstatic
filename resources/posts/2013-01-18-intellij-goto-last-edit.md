:title IntelliJ - a nerd crush
:published 2013-01-18 23:14
:body

I've been a huge [IntelliJ](http://www.jetbrains.com/idea) fan ever since I started programming Java and I've spent a lot of effort learning the tool. I thought I'd share some of my favourite features; perhaps you will find them useful too.

Navigation
===========
Have you ever gotten lost in a codebase? Once a project gets to a certain size, being able to navigate effectively around is vital. IntelliJ makes this a breeze.

If you know the name of a class or file, you can easily find them with the following shortcuts: 

* **Go to class:** ctrl + n
* **Go to file:** ctrl + shift + n

<img src="/images/intellij/file.png" width=694 height=184 >

If you only remember a fragment from the file, such as a method name or a variable, you can use that information too:

* **Go to symbol:** ctrl + shift + alt + n

If that fails, the search for text function is always handy. The speed with which IntelliJ finds text fragments across large numbers of files never seizes to amaze me.  

* **Search all files:** ctrl + shift + f

<img src="/images/intellij/find.png" width=417 height=430 >

Usually, the number of files that is changed at a time are relatively few. IntelliJ lets you search through your recently changed files.

* **Recently changed files:** ctrl + e

<img src="/images/intellij/recent.png" width=402 height=281 >

There is also the possibility of backtracking your navigation stack.

* **Navigate the stack:** ctrl + shift + left/right

I saved my favourite navigation feature for last. Usually I  move back and forth between several files in order to understand the context. However, sometimes I fail to remember where I was before I started moving around. Most of the time I want to go to where I changed something last.

* **Navigate to previous edit:** ctrl + shift + backspace