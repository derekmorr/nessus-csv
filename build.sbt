name := "scala-skeleton"

version := "1.0"

scalaVersion := "2.11.8"

lazy val root = (project in file("."))

libraryDependencies ++= {
  Seq(
    "org.scalaz"          %% "scalaz-core"    % "7.2.1"   % Compile,
    "org.scalacheck"      %% "scalacheck"     % "1.12.5"  % Test,
    "org.scalatest"       %% "scalatest"      % "2.2.6"   % Test,
    "org.pegdown"         %  "pegdown"        % "1.6.0"   % Test
  )
}

scalacOptions ++= Seq(
  "-feature", "-unchecked", "-deprecation", "-Xcheckinit", "-Xlint",
  "-Xfatal-warnings", "-g:line", "-Ywarn-dead-code", "-Ywarn-numeric-widen")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

// don't let Ctrl-C exit
cancelable in Global := true

mainClass in (Compile, packageBin) := Some("")

// don't include scaladoc in distribution
doc in Compile <<= target.map(_ / "none")

// generate HTML reports for tests
(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/report")

// show test durations
(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

// run tests in parallel
parallelExecution in Test := true

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _ *) =>
    (xs map { _.toLowerCase}) match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
      case _ => MergeStrategy.discard
    }
  case _ => MergeStrategy.first
}

scapegoatVersion := "1.2.1"
