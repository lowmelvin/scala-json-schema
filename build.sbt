val scala3Version = "3.3.3"
val CirceVersion  = "0.14.10"

ThisBuild / scalaVersion := scala3Version

ThisBuild / licenses := Seq(
  "APL2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")
)
ThisBuild / homepage := Some(
  url("https://github.com/lowmelvin/scala-json-schema")
)

ThisBuild / developers := List(
  Developer(
    "lowmelvin",
    "Melvin Low",
    "me@melvinlow.com",
    url("https://melvinlow.com")
  )
)

ThisBuild / organization     := "com.melvinlow"
ThisBuild / organizationName := "Melvin Low"

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

usePgpKeyHex("EA39099104314A0169EA2DC5531F0807E5F7D750")

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-json-schema",
    libraryDependencies ++= Seq(
      "io.circe"      %% "circe-core" % CirceVersion,
      "org.scalameta" %% "munit"      % "1.0.4" % Test
    ),
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Wunused:all",
      "-Wvalue-discard",
      "-Werror",
      "-no-indent",
      "-explain",
      "-rewrite",
      "-source:future-migration"
    )
  )

lazy val docs = (project in file("scala-json-schema-docs"))
  .dependsOn(root)
  .enablePlugins(MdocPlugin)
  .settings(
    mdocIn  := file("docs/README.md"),
    mdocOut := file("README.md"),
    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )
