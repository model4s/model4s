import sbt.CrossVersion

name := "model4s"
version := "1.0"

val shared = Seq(
  organization := "com.github.model4s",
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
    description := "Library for generation boilerplate-free code for models in compile time with Scala macro",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.1",
      "com.twitter" %% "bijection-core" % "0.9.6",
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    )
  )

lazy val sample = (project in file("sample"))
  .aggregate(core)
  .settings(
    shared, name := "sample")
  .dependsOn(core)