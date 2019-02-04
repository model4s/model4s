import sbt.Keys.{developers, homepage, licenses, scmInfo}
import sbt.{CrossVersion, url}

name := "model4s"

val shared = Seq(
  organization := "io.github.model4s",
  scalaVersion := "2.12.1",
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M8" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scalameta" %% "scalameta" % "1.7.0" % Provided)
)

lazy val core = (project in file("model4s"))
  .settings(
    shared,
    name := "model4s",
    version := "1.0",
    description := "Library for generation boilerplate-free code for models in compile time with Scala macro",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.1",
      "com.twitter" %% "bijection-core" % "0.9.6",
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    ),

    homepage := Some(url("https://github.com/model4s/model4s")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/model4s/model4s"),
        "git@github.com:model4s/model4s.git"
      )
    ),

    developers := List(
      Developer(
        id = "artem0",
        name = "Artem Rukavytsia",
        email = "artem.rukavitsya@gmail.com",
        url = url("https://github.com/artem0")
      )
    ),

    licenses += ("MIT", url("https://github.com/model4s/model4s/blob/master/LICENSE")),
    publishMavenStyle := true,

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    updateOptions := updateOptions.value.withGigahorse(false)

  )

lazy val sample = (project in file("sample"))
  .aggregate(core)
  .settings(
    shared, name := "sample")
  .dependsOn(core)