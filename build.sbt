name := "InterviewCalendar"

version := "1.1.0"

organization := "cookbook.liftweb.net"

scalaVersion := "2.11.2"

resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"  at "http://oss.sonatype.org/content/repositories/releases",
   "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

jetty()

parallelExecution in Test := false

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= {
  val liftVersion = "2.6-RC1"
  Seq(
    "net.liftweb"     %% "lift-webkit"            % liftVersion,
    "net.liftmodules" %% "lift-jquery-module_2.6" % "2.8",
    "ch.qos.logback"   % "logback-classic"        % "1.1.2",
    "org.specs2"      %% "specs2" % "2.4.4"       % "test",
    "joda-time" % "joda-time" % "2.10.1",
    "com.typesafe.play" %% "play-json" % "2.7.0"
  )
}
