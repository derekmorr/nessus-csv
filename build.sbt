name := "nessus-csv-loader"

version := "1.0"

scalaOrganization := "org.typelevel"
scalaVersion := "2.12.2-bin-typelevel-4"

lazy val root = project in file(".")

libraryDependencies ++= {
  val circe = "0.8.0"
  val refined = "0.8.2"
  Seq(
    "com.github.derekmorr"        %% "refined-anorm"              % "0.1"     % Compile,
    "com.github.melrief"          %% "purecsv"                    % "0.0.9"   % Compile,
    "com.github.pureconfig"       %% "pureconfig"                 % "0.7.2"   % Compile,
    "com.google.guava"            %  "guava"                      % "22.0"    % Compile,
    "com.typesafe.play"           %% "anorm"                      % "2.5.3"   % Compile,
    "com.zaxxer"                  %  "HikariCP"                   % "2.6.3"   % Compile,
    "eu.timepit"                  %% "refined"                    % refined   % Compile,
    "io.circe"                    %% "circe-core"                 % circe     % Compile,
    "io.circe"                    %% "circe-generic"              % circe     % Compile,
    "io.circe"                    %% "circe-java8"                % circe     % Compile,
    "io.circe"                    %% "circe-refined"              % circe     % Compile,
    "org.typelevel"               %% "cats-core"                  % "0.9.0"   % Compile,

    "com.github.alexarchambault"  %% "scalacheck-shapeless_1.13"  % "1.1.5"   % Test,
    "eu.timepit"                  %% "refined-scalacheck"         % refined   % Test,
    "org.scalacheck"              %% "scalacheck"     	          % "1.13.5"  % Test,
    "org.scalatest"               %% "scalatest"      	          % "3.0.3"   % Test,
    "org.pegdown"                 %  "pegdown"        	          % "1.6.0"   % Test,

    "org.mariadb.jdbc"            %  "mariadb-java-client"        % "2.0.2"   % Runtime
  )
}

//scalacOptions ++= Seq(
//  "-feature", "-unchecked", "-deprecation", "-Xcheckinit", "-Xlint",
//  "-Xfatal-warnings", "-g:line", "-Ywarn-dead-code", "-Ywarn-numeric-widen")

// make shapeless fast
scalacOptions += "-Yinduction-heuristics"

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

scapegoatVersion := "1.3.1"
