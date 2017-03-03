# Nessus CSV loader

 Sample loader for Nessus CSV export files, using the PureCSV library.

# Prerequisites

You will need [Java 8 installed](http://java.oracle.com/). The app will download any additional dependencies.

# Running the app:

To run the sample program:

    ./sbt "run nessus_test.csv" 

The app uses [sbt](http://www.scala-sbt.org/) to build and package the app.
If you don't have sbt installed, you can use the bundled sbt runner (`./sbt`).

The first time you build the app, it will need to download dependencies and compile support code.
This will take several minutes, but it only happens on the first build.

# Testing the app

To run unit tests, run

    ./sbt test

# Code coverage report

To generate a code coverage report run,

    ./sbt clean coverage test coverageReport

The HTML report will be written to `target/scala-2.12/scoverage-report/index.html`.

# Code quality analysis

The project uses the [scapegoat tool](https://github.com/sksamuel/scapegoat) for code quality analysis.
Run run a scapegoat report, run

    ./sbt scapegoat

The HTML report will be written to `target/scala-2.12/scapegoat-report/scapegoat.html`

# Vulnerability checks

The project uses the [sbt-dependency-check](https://github.com/albuch/sbt-dependency-check) tool to query the
[OWASP Dependency Check database](https://www.owasp.org/index.php/OWASP_Dependency_Check). You can check the project's
depenencies for known security vulnerability by running

     ./sbt check

The HTML report will be written to `target/scala-2.12/dependency-check-report.html`

Note - the first time you run this command it will take *much* longer than normal because it has to download the
vulnerability database from OWASP.

# Manually installing dependencies

The project depends on [Scala](http://www.scala-lang.org/) and its build tool, [sbt](http://www.scala-sbt.org/). If the `sbt` script fails to install Scala and SBT, you can manually install them.

On OS X, assuming you have [homebrew](http://brew.sh/) installed, the easiest way to install these is via:

    brew install scala sbt

Alternatively, you can download native OS packages from the links above.
