organization := "name.heikoseeberger"

name := "sbt-groll"

// version in version.sbt for sbt-release

scalaVersion := Version.scala

libraryDependencies ++= Dependencies.sbtGroll

unmanagedSourceDirectories in Compile := List((scalaSource in Compile).value)

unmanagedSourceDirectories in Test := List((scalaSource in Test).value)

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

initialCommands := "import name.heikoseeberger.sbtgroll._"

sbtPlugin := true

publishTo := Some(if (isSnapshot.value) Classpaths.sbtPluginSnapshots else Classpaths.sbtPluginReleases)

publishMavenStyle := false
