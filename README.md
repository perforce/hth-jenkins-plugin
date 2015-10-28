# Deveo Jenkins Notification Plugin

Use this plugin to create build events to Deveo from your Jenkins builds. See the [official documentation](https://wiki.jenkins-ci.org/display/JENKINS/Deveo+Plugin) for more information.

## Contributing

To contribute, please utilize Pull Requests as you would in any other project. Development is done on `develop` branch.

### Prerequisites

You'll need [Vagrant](https://www.vagrantup.com/) to get the development environment running. You can simply install it via [Homebrew Cask](http://caskroom.io/) with:

    $ brew cask install vagrant

### Getting started

Start the virtual machine

    $ vagrant up

SSH into the machine

    $ vagrant ssh

Navigate to the shared working directory

    $ cd /vagrant

Build the plugin and start Jenkins

    $ mvn hpi:run

If you are only doing template changes, you can simply hit Enter on the console to reload the context. In other cases you'll need to re-execute the aforementioned command.

You can now access Jenkins from your host machine on http://10.11.12.100:8080/jenkins.
