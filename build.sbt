name := """foo_java"""

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.springframework" % "spring-context" % "3.2.2.RELEASE",
  "org.springframework" % "spring-aop" % "3.2.2.RELEASE",
  "org.springframework" % "spring-expression" % "3.2.2.RELEASE",
  "com.google.inject" % "guice" % "3.0",
  "com.typesafe.play.plugins" %% "play-plugins-redis" % "2.3.1",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "io.iron.ironmq" % "ironmq" % "3.0.4",
  "com.nimbusds" % "nimbus-jose-jwt" % "3.8.2"
)

resolvers += "Sedis repository" at "http://pk11-scratch.googlecode.com/svn/trunk/"


