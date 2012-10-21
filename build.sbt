
organization := "name.heikoseeberger.sbt.groll"

name := "sbt-groll"

// version is defined in version.sbt in order to support sbt-release

sbtPlugin := true

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq("org.scalaz" %% "scalaz-core" % "6.0.3")

publishTo <<= isSnapshot(
  if (_) 
    Some(Resolver.url("ivy-snapshots", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-snapshots"))(Resolver.ivyStylePatterns))
  else
    Some(Resolver.url("ivy-releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns))
)

publishMavenStyle := false

scalariformSettings

releaseSettings
