# scala-skeleton
Skeleton for my Scala projects

This is a skeleton I use for most of my Scala projects. It includes common libraries, SBT settings and plugins, etc. Feel free to use if you find it useful.

After you checkout the project, edit `build.sbt` and set the `name` variable. Then run

    git remote remove origin

so you don't push project-specific changes back into the skeleton project.

# Boilerplate README:

# Prerequisites

You will need [Java 8 installed](http://java.oracle.com/). The app will download any additional dependencies.

# Running the app:

The app uses [sbt](http://www.scala-sbt.org/) to build and package the app.
If you don't have sbt installed, you can use the bundled sbt runner (./sbt).

The first time you build the app, it will need to download dependencies and compile support code.
This will take several minutes, but it only happens on the first build.

# Testing the app

To run unit tests, run

    ./sbt test

This will not interact with AWS, so you don't need to setup AWS credentials to test the app.

# Code coverage report

To generate a code coverage report run,

    ./sbt clean coverage test coverageReport

The HTML report will be written to `target/scala-2.11/scoverage-report/index.html`.

# Code quality analysis

The project uses the [scapegoat tool](https://github.com/sksamuel/scapegoat) for code quality analysis.
Run run a scapegoat report, run

    ./sbt scapegoat

The HTML report will be written to `target/scala-2.11/scapegoat-report/scapegoat.html`

# Running the app

Setup your AWS credentials, and run

    ./sbt run

# Packaging

To package for deployment to AWS, run

    ./sbt clean assembly

This will build a "fat jar" file with all the dependencies bundled into a single file.

The resulting JAR file will be written to `target/scala-2.11/<name>-<version>.jar`.

# Manually installing dependencies

The project depends on [Scala](http://www.scala-lang.org/) and its build tool, [sbt](http://www.scala-sbt.org/). If the `sbt` script fails to install Scala and SBT, you can manually install them.

On OS X, assuming you have [homebrew](http://brew.sh/) installed, the easiest way to install these is via:

    brew install scala sbt

Alternatively, you can download native OS packages from the links above.
