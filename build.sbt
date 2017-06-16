name := "nessus-csv-loader"

version := "1.0"

scalaVersion := "2.12.2"

lazy val root = project in file(".")

libraryDependencies ++= {
  Seq(
    "com.github.melrief"    %% "purecsv"              % "0.0.9"   % Compile,
    "com.github.pureconfig" %% "pureconfig"           % "0.7.2"   % Compile,
    "com.google.guava"      %  "guava"                % "22.0"    % Compile,
    "com.typesafe.play"     %% "anorm"                % "2.5.3"   % Compile,
    "com.zaxxer"            %  "HikariCP"             % "2.6.2"   % Compile,
    "org.scalaz"            %% "scalaz-core"    	    % "7.2.13"  % Compile,

    "org.scalacheck"        %% "scalacheck"     	    % "1.13.5"  % Test,
    "org.scalatest"         %% "scalatest"      	    % "3.0.3"   % Test,
    "org.pegdown"           %  "pegdown"        	    % "1.6.0"   % Test,
    "org.typelevel"	        %% "scalaz-scalatest"     % "1.1.2"   % Test,

    "org.mariadb.jdbc"      %  "mariadb-java-client"  % "2.0.2"   % Runtime
  )
}

scalacOptions ++= Seq(
  "-feature", "-unchecked", "-deprecation", "-Xcheckinit", "-Xlint",
  "-Xfatal-warnings", "-g:line", "-Ywarn-dead-code", "-Ywarn-numeric-widen")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

// don't let Ctrl-C exit
cancelable in Global := true

mainClass in (Compile, packageBin) := Some("NessusLoader")

// don't include scaladoc in distribution
//doc in Compile <<= target.map(_ / "none")

autoAPIMappings := true

// generate HTML reports for tests
(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/report")

// show test durations
(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

// run tests in parallel
parallelExecution in Test := true

// cache dependency resolution information
updateOptions := updateOptions.value.withCachedResolution(true)

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

enablePlugins(GitBranchPrompt)

assemblyJarName in assembly := {
  name.value + "-" + version.value + ".jar"
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _ *) =>
    xs map { _.toLowerCase} match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
      case _ => MergeStrategy.discard
    }
  case _ => MergeStrategy.first
}

scapegoatVersion := "1.3.0"
