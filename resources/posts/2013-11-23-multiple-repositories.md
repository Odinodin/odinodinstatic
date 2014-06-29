:title Multiple repositories
:published 
:body 2013-11-23 21:17

Have you ever had the need to manage several related repositories in a project? Say for instance you want to pull or push changes to multiple repositories in one go. 

If you are like me, it doesn't take long before you grow tired of repeating commands for each repository. But before you start rolling your own update scripts, there is a better solution that you should try first.

mr (myrepos tool)
========================
The [mr tool](http://joeyh.name/code/mr/), made by the guy who develops [git-annex](http://git-annex.branchable.com/), enables you to handle multiple repos with a breeze.

Its pretty simple to use; run the following command in each repository you want to manage:

```
mr register
```

The effect is that mr now knows about your repository. It is based on a configuration file, ~/.mrconfig. After you've added a couple of repos, it will look something like this.

```
[git/kodemaker/projectx/web]
checkout = 
	     git clone 'git@github.com:projectx/web.git' 'web'

[git/kodemaker/projectx/backend]
checkout = 
	     git clone 'git@github.com:projectx/backend.git' 'backend'
```

There is also support for global commands that will be available for all repos.

```
# the git_ prefix means this will be available for all git repos by running 'mr update'
[DEFAULT]
git_update = 
	  git pull 
	  git submodule update --init --recursive
```

Note that you can bind several commands to a single alias; just separate them by a line feed. The example DEFAULT section above will, when run through "mr update", first do a git pull and then update any submodules.

To top it all of, just when you thought your mind could not get more exploded by mr's awesomeness, there is the -j parameter which will run the mr command as concurrent jobs, greatly speeding up the process. 

```
mr -j20 update
```